/*<license>
Copyright 2007 - $Date$ by the authors mentioned below.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.beedra_II.property.collection.set;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedra_II.Beed;
import org.beedra_II.BeedFilter;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * A {@link SetBeed} that returns a filtered subset of a given {@link SetBeed}
 * using a {@link BeedFilter}. Only the elements that meet the filter criterion
 * are in the resulting set.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getFilter() != null;
 * @invar  getSource() == null ==> get().isEmpty();
 * @invar  getSource() != null ==>
 *           get() == {element : getSource().get().contains(element) &&
 *                               getFilter().filter(element)};
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class FilteredSetBeed<_Element_ extends Beed<_Event_>, _Event_ extends Event>
    extends AbstractSetBeed<_Element_, SetEvent<_Element_>>
    implements SetBeed<_Element_, SetEvent<_Element_>> {

  /**
   * @pre   owner != null;
   * @pre   filter != null;
   * @post  getOwner() == owner;
   * @post  getFilter() == filter;
   * @post  getSource() == null;
   * @post  get().isEmpty();
   */
  public FilteredSetBeed(BeedFilter<_Element_> filter, AggregateBeed owner) {
    super(owner);
    $filter = filter;
  }

  private final Dependent<Beed<?>> $dependent =
    new AbstractUpdateSourceDependentDelegate<Beed<?>, SetEvent<_Element_>>(this) {

      @Override
      protected SetEvent<_Element_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
        // if the source changes (elements added and / or removed
        if (events.keySet().contains($source)) {
          @SuppressWarnings("unchecked")
          SetEvent<_Element_> setEvent = (SetEvent<_Element_>)events.get($source);
          sourceChanged(setEvent);
        }
        // if the value of one of the elements changes
        // we do nothing special here, just a total recalculate
        // IDEA there is room for optimalization here
        // recalculate and notify the listeners if the value has changed
        return recalculateEvent(edit);
      }

      /**
       * @post    All Beeds that are added to the SetBeed by the given event
       *          become update sources.
       *          (The reason is that this should be
       *          notified (and then recalculate) when one of the element Beeds
       *          changes.)
       * @post    All Beeds that are removed from the SetBeed by the given
       *          event are no longer update sources.
       * @post    getDouble() is recalculated.
       */
      private void sourceChanged(SetEvent<_Element_> event) {
        /* All Beeds that are added to the SetBeed by the given event
         * become update sources
         */
        Set<_Element_> added = event.getAddedElements();
        for (_Element_ beed : added) {
          addUpdateSource(beed);
        }
        /* All Beeds that are removed from the SetBeed by the given event
         * stop being update sources
         */
        Set<_Element_> removed = event.getRemovedElements();
        for (_Element_ beed : removed) {
          removeUpdateSource(beed);
        }
      }


    };

  public final int getMaximumRootUpdateSourceDistance() {
    /* FIX FOR CONSTRUCTION PROBLEM
     * At construction, the super constructor is called with the future owner
     * of this property beed. Eventually, in the constructor code of AbstractPropertyBeed,
     * this object is registered as update source with the dependent of the
     * aggregate beed. During that registration process, the dependent
     * checks to see if we need to ++ our maximum root update source distance.
     * This involves a call to this method getMaximumRootUpdateSourceDistance.
     * Since however, we are still doing initialization in AbstractPropertyBeed,
     * initialization code (and construction code) further down is not yet executed.
     * This means that our $dependent is still null, and this results in a NullPointerException.
     * On the other hand, we cannot move the concept of $dependent up, since not all
     * property beeds have a dependent.
     * The fix implemented here is the following:
     * This problem only occurs during construction. During construction, we will
     * not have any update sources, so our maximum root update source distance is
     * effectively 0.
     */
    /*
     * TODO This only works if we only add 1 update source during construction,
     *      so a better solution should be sought.
     */
    return $dependent == null ? 0 : $dependent.getMaximumRootUpdateSourceDistance();
  }



  /*<property name="filterCriterionFactory">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final BeedFilter<_Element_> getFilter() {
    return $filter;
  }

  private BeedFilter<_Element_> $filter;

  /*</property>*/


  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<_Element_, ?> getSource() {
    return $source;
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    get() == the result of filtering the given source
   * @post    The FilteredSetBeed is registered as a listener of the given SetBeed.
   * @post    The FilteredSetBeed is registered as a listener of all beeds in
   *          the given source. (The reason is that the MappedSetBeed should be
   *          notified (and then recalculate) when one of the beeds in the source
   *          changes.)
   * @post    The listeners of this beed are notified when the value changes.
   * @post    The listeners of the size beed are notified when the size of this
   *          set has changed.
   */
  public final void setSource(SetBeed<_Element_, ?> source) {
    if ($source != null) {
      if (getFilter().dependsOnBeed()) {
        for (_Element_ beed : $source.get()) {
          $dependent.removeUpdateSource(beed);
        }
      }
      $dependent.removeUpdateSource($source);
    }
    // set the source
    $source = source;
    if ($source != null) {
      $dependent.addUpdateSource($source);
      if (getFilter().dependsOnBeed()) {
        for (_Element_ beed : $source.get()) {
          $dependent.addUpdateSource(beed);
        }
      }
    }
    // recalculate and notify the listeners if the value has changed
    ActualSetEvent<_Element_> event = recalculateEvent(null);
    if (event != null) {
      updateDependents(event);
    }
  }

  private SetBeed<_Element_, ?> $source;

  /*</property>*/



  /**
   * The set of filtered elements in the {@link #getSource()}.
   *
   * @invar  $filteredSet != null;
   */
  private HashSet<_Element_> $filteredSet = new HashSet<_Element_>();


  /**
   * @basic
   */
  public final Set<_Element_> get() {
    return Collections.unmodifiableSet($filteredSet);
  }


  /**
   * The value of $filteredSet is recalculated.
   * This is done by iterating over the beeds in the source set beed.
   * When the source is null, $filteredSet is an empty set.
   * When the source contains zero beeds, $filteredSet is an empty set.
   * Otherwise, $filteredSet contains all beeds in the given set
   * that satisfy the filter criterion.
   */
  public void recalculate() {
    $filteredSet.clear();
    if (getSource() != null) {
      for (_Element_ element : getSource().get()) {
        if (getFilter().filter(element)) {
          $filteredSet.add(element);
        }
      }
    }
  }

  private ActualSetEvent<_Element_> recalculateEvent(Edit<?> edit) {
    @SuppressWarnings("unchecked")
    Set<_Element_> oldValue = (Set<_Element_>)$filteredSet.clone();
    recalculate();
    if (! ComparisonUtil.equalsWithNull(oldValue, $filteredSet)) {
      Set<_Element_> addedElements = new HashSet<_Element_>($filteredSet);
      addedElements.removeAll(oldValue);
      Set<_Element_> removedElements = new HashSet<_Element_>(oldValue);
      removedElements.removeAll($filteredSet);
      return new ActualSetEvent<_Element_>(FilteredSetBeed.this, addedElements,  removedElements, edit);
    }
    else {
      return null;
    }
  }

  public final Set<? extends UpdateSource> getUpdateSources() {
    return $dependent.getUpdateSources();
  }

  private final static Set<? extends UpdateSource> PHI = Collections.emptySet();

  public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
    /* fixed to make it possible to use this method during construction,
     * before $dependent is initialized. But that is bad code, and should be
     * fixed.
     */
    return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
  }

  @Override
  protected String otherToStringInformation() {
    return "hashCode: " + hashCode() +
           "; #: " + get().size();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "elements:\n");
    Iterator<_Element_> iter = get().iterator();
    while (iter.hasNext()) {
      _Element_ element = iter.next();
      sb.append(indent(level + 2) + element.toString() + "\n");
    }
  }

}

