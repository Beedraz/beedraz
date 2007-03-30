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

import org.beedra_II.AbstractBeed;
import org.beedra_II.Beed;
import org.beedra_II.BeedMapping;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * A {@link SetBeed} that mappes the {@link Beed beeds} in
 * a given {@link SetBeed} to a set of other elements using
 * a {@link BeedMapping}.
 *
 * @mudo We veronderstellen hier dat een BeedMapping injectief is, dwz dat twee
 *       verschillende waarden nooit gemapt worden op hetzelfde beeld
 *       (zie invariant in {@link BeedMapping}).
 *       Indien dit niet het geval is, moeten we immers een MappedCollectionBeed
 *       maken, die dubbels toelaat.
 * @mudo De bron van een MappedSetBeed definieren we hier als een SetBeed. Dit
 *       kan echter ook een OrderedSetBeed zijn. Dit betekent dat deze beeds in
 *       een hierarchie moeten geplaatst worden.
 * @remark  The value of a SetBeed is always effective (this is an invariant of
 *          the SetBeed class). So, when the source is null, the resulting
 *          mapped set is an effective, empty set.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getBeedMapping() != null;
 * @invar  get().size() ==
 *           getSource() != null
 *             ? getSource().get().size()
 *             : 0;
 * @invar  (forAll _From_ from;
 *            getSource().contains(from);
 *            get().contains(getBeedMapping().map(from)));
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class MappedSetBeed<_From_ extends Beed<_FromEvent_>,
                           _FromEvent_ extends Event, _To_>
    extends AbstractSetBeed<_To_, SetEvent<_To_>>
    implements SetBeed<_To_, SetEvent<_To_>> {

  /**
   * @pre   owner != null;
   * @pre   mapping != null;
   * @post  getOwner() == owner;
   * @post  getBeedMapping() == mapping;
   * @post  getSource() == null;
   * @post  get() != null;
   * @post  get().isEmpty();
   */
  public MappedSetBeed(BeedMapping<_From_, _To_> mapping, AggregateBeed owner) {
    super(owner);
    $mapping = mapping;
  }



  private final Dependent<Beed<?>> $dependent =
    new AbstractUpdateSourceDependentDelegate<Beed<?>, SetEvent<_To_>>(this) {

      @Override
      protected SetEvent<_To_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
        // if the source changes (elements added and / or removed
        if (events.keySet().contains($source)) {
          @SuppressWarnings("unchecked")
          SetEvent<_From_> setEvent = (SetEvent<_From_>)events.get($source);
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
      private void sourceChanged(SetEvent<_From_> event) {
        /* All Beeds that are added to the SetBeed by the given event
         * become update sources
         */
        Set<_From_> added = event.getAddedElements();
        for (_From_ beed : added) {
          addUpdateSource(beed);
        }
        /* All Beeds that are removed from the SetBeed by the given event
         * stop being update sources
         */
        Set<_From_> removed = event.getRemovedElements();
        for (_From_ beed : removed) {
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



  /*<property name="mapping">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final BeedMapping<_From_, _To_> getMapping() {
    return $mapping;
  }

  private BeedMapping<_From_, _To_> $mapping;

  /*</property>*/


  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<_From_, ?> getSource() {
    return $source;
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    get() == the result of mapping the given source
   * @post    The MappedSetBeed is registered as a listener of the given SetBeed.
   * @post    The MappedSetBeed is registered as a listener of all beeds in
   *          the given source. (The reason is that the MappedSetBeed should be
   *          notified (and then recalculate) when one of the beeds in the source
   *          changes.)
   * @post    The listeners of this beed are notified when the value changes.
   * @post    The listeners of the size beed are notified when the size of this
   *          set has changed.
   */
  public final void setSource(SetBeed<_From_, ?> source) {
    if ($source != null) {
      for (_From_ beed : $source.get()) {
        $dependent.removeUpdateSource(beed);
      }
      $dependent.removeUpdateSource($source);
    }
    // set the source
    $source = source;
    if ($source != null) {
      $dependent.addUpdateSource($source);
      for (_From_ beed : $source.get()) {
        $dependent.addUpdateSource(beed);
      }
    }
    // recalculate and notify the listeners if the value has changed
    ActualSetEvent<_To_> event = recalculateEvent(null);
    if (event != null) {
      updateDependents(event);
    }
  }

  private SetBeed<_From_, ?> $source;

  /*</property>*/


  /**
   * @invar  $mappedSet != null;
   */
  private HashSet<_To_> $mappedSet = new HashSet<_To_>();

  /**
   * @basic
   */
  public final Set<_To_> get() {
    return Collections.unmodifiableSet($mappedSet);
  }


  /**
   * The value of $mappedSet is recalculated. This is done by iterating over the beeds
   * in the source set beed.
   * When the source is null, the result is an empty set.
   * When the source contains zero beeds, the result is an empty set.
   * Otherwise, the resulting set contains the maps of all beeds in the given set.
   */
  private void recalculate() {
    $mappedSet = new HashSet<_To_>();
    Set<_From_> fromSet = getSource() != null? getSource().get(): Collections.<_From_>emptySet();
    for (_From_ from : fromSet) {
      $mappedSet.add(getMapping().map(from));
    }
  }

  private ActualSetEvent<_To_> recalculateEvent(Edit<?> edit) {
    @SuppressWarnings("unchecked")
    Set<_To_> oldValue = (Set<_To_>)$mappedSet.clone();
    recalculate();
    if (! ComparisonUtil.equalsWithNull(oldValue, $mappedSet)) {
      Set<_To_> addedElements = new HashSet<_To_>($mappedSet);
      addedElements.removeAll(oldValue);
      Set<_To_> removedElements = new HashSet<_To_>(oldValue);
      removedElements.removeAll($mappedSet);
      return new ActualSetEvent<_To_>(MappedSetBeed.this, addedElements,  removedElements, edit);
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
    Iterator<_To_> iter = get().iterator();
    while (iter.hasNext()) {
      _To_ element = iter.next();
      if (element instanceof AbstractBeed<?>) {
        ((AbstractBeed<?>)element).toString(sb, level + 2);
      }
      else {
        sb.append(indent(level + 2) + element.toString() + "\n");
      }
    }
  }

}

