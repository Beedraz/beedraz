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

package org.beedra_II.property.set;


import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.beedra_II.Beed;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.ppeew.annotations.vcs.CvsInfo;
import org.ppeew.smallfries.ComparisonUtil;


/**
 * A {@link SetBeed} that returns a filtered subset of a given {@link SetBeed}
 * using a {@link Filter}. Only the elements that meet the filter criterion
 * are in the resulting set.
 *
 * @mudo De bron van een FilterSetBeed definieren we hier als een SetBeed. Dit
 *       kan echter ook een OrderedSetBeed zijn. Dit betekent dat deze beeds in
 *       een hierarchie moeten geplaatst worden.
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
    extends AbstractPropertyBeed<SetEvent<_Element_>>
    implements SetBeed<_Element_> {

  /**
   * @pre   owner != null;
   * @pre   filter != null;
   * @post  getOwner() == owner;
   * @post  getFilter() == filter;
   * @post  getSource() == null;
   * @post  get().isEmpty();
   */
  public FilteredSetBeed(Filter<_Element_> filter, AggregateBeed owner) {
    super(owner);
    $filter = filter;
  }


  /*<property name="filterCriterionFactory">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Filter<_Element_> getFilter() {
    return $filter;
  }

  private Filter<_Element_> $filter;

  /*</property>*/


  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<_Element_> getSource() {
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
   */
  public final void setSource(SetBeed<_Element_> source) {
    $source = source;
    if (source != null) {
      // register the FilteredSetBeed as listener of the given source
      source.addListener($sourceListener);
      // register the FilteredSetBeed as listener of all beeds in the given source
      for (_Element_ beed : source.get()) {
        beed.addListener($beedListener);
      }
    }
    // recalculate and notify the listeners if the value has changed
    Set<_Element_> oldValue = $filteredSet;
    recalculate();
    if (! ComparisonUtil.equalsWithNull(oldValue, $filteredSet)) {
      fireChangeEvent(
        new SetEvent<_Element_>(
          FilteredSetBeed.this, $filteredSet, oldValue, null));
    }
  }

  private SetBeed<_Element_> $source;

  /*</property>*/


  /**
   * A listener that will be registered as listener of the {@link #getSource()}.
   */
  private final Listener<SetEvent<_Element_>> $sourceListener =
        new Listener<SetEvent<_Element_>>() {

    /**
     * @post    The FilteredSetBeed is registered as a listener of all beeds
     *          that are added to the source by the given event. (The reason is that
     *          the FilteredSetBeed should be notified (and then recalculate) when one
     *          of the beeds changes.)
     * @post    The FilteredSetBeed is removed as listener of all beeds
     *          that are removed from the source by the given event.
     * @post    get() == the result of mapping the elements of the given source
     * @post    The listeners of this beed are notified when the set changes.
     */
    public void beedChanged(SetEvent<_Element_> event) {
      // add the FilteredSetBeed as listener of all beeds that are added to the source by the given event
      // remember the beeds that satisfy the filter criterion
      Set<_Element_> added = event.getAddedElements();
      Set<_Element_> validAdded = new HashSet<_Element_>();
      for (_Element_ element : added) {
        // add the FilteredSetBeed as listener of the added beed
        element.addListener($beedListener);
        // check whether the beed will be in the resulting set
        if (getFilter().filter(element)) {
          $filteredSet.add(element);
          validAdded.add(element);
        }
      }
      // remove the FilteredSetBeed as listener from all beeds that are removed from the source by the given event
      // remember the beeds that satisfy the filter criterion
      Set<_Element_> removed = event.getRemovedElements();
      Set<_Element_> validRemoved = new HashSet<_Element_>();
      for (_Element_ element : removed) {
        // remove the FilteredSetBeed as listener of the removed beed
        element.removeListener($beedListener);
        // check whether the beed was in the resulting set
        if (getFilter().filter(element)) {
          $filteredSet.remove(element);
          validRemoved.add(element);
        }
      }
      // notify the listeners if a beed has been added or removed
      if (!validAdded.isEmpty() || !validRemoved.isEmpty()) {
        fireChangeEvent(
          new SetEvent<_Element_>(
            FilteredSetBeed.this, validAdded, validRemoved, event.getEdit()));
      }
    }

  };


  private final Listener<_Event_> $beedListener = new Listener<_Event_>() {

    /**
     * @post    get() == the result of filtering the elements of the given source
     * @post    The listeners of this beed are notified.
     */
    public void beedChanged(_Event_ event) {
      _Element_ element = (_Element_) event.getSource();
      // check whether the validity of the beed has changed
      Set<_Element_> added = new HashSet<_Element_>();
      Set<_Element_> removed = new HashSet<_Element_>();
      if (getFilter().filter(element)) {
        if (!$filteredSet.contains(element)) {
          // old: false, new: true
          $filteredSet.add(element);
          added.add(element);
          fireChangeEvent(
              new SetEvent<_Element_>(
                  FilteredSetBeed.this, added, removed, event.getEdit()));
        }
      }
      else {
        if ($filteredSet.contains(element)) {
          // old: true, new: false
          $filteredSet.remove(element);
          removed.add(element);
          fireChangeEvent(
              new SetEvent<_Element_>(
                  FilteredSetBeed.this, added, removed, event.getEdit()));
        }
      }
    }

  };


  /**
   * The set of filtered elements in the {@link #getSource()}.
   *
   * @invar  $filteredSet != null;
   */
  private Set<_Element_> $filteredSet = new HashSet<_Element_>();


  /**
   * @basic
   */
  public final Set<_Element_> get() {
    return new AbstractSet<_Element_>() {

      @Override
      public Iterator<_Element_> iterator() {
        final Iterator<_Element_> filteredSetIterator = $filteredSet.iterator();
        return new Iterator<_Element_>() {

          /**
           * Delegation.
           */
          public boolean hasNext() {
            return filteredSetIterator.hasNext();
          }

          /**
           * Delegation.
           */
          public _Element_ next() {
            return filteredSetIterator.next();
          }

          /**
           * optional operation
           */
          public void remove() {
            // NOP
          }

        };
      }

      /**
       * The number of valid filter criterions.
       */
      @Override
      public int size() {
        return $filteredSet.size();
      }

    };
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
    $filteredSet = new HashSet<_Element_>();
    Set<_Element_> elements = getSource() != null
                              ? getSource().get()
                              : new HashSet<_Element_>();
    for (_Element_ element : elements) {
      if (getFilter().filter(element)) {
        $filteredSet.add(element);
      }
    }
  }


  /**
   * @post  result != null;
   * @post  result.getAddedElements().equals(get());
   * @post  result.getRemovedElements().isEmpty();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected SetEvent<_Element_> createInitialEvent() {
    return new SetEvent<_Element_>(this, get(), null, null);
  }

}

