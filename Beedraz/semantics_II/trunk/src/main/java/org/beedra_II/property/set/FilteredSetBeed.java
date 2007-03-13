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

import org.beedra.util_I.Comparison;
import org.beedra_II.Beed;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A {@link SetBeed} that returns a filtered subset of a given {@link SetBeed}
 * using a {@link FilterCriterionFactory}. Only the elements that meet their
 * filter criterion are in the resulting set.
 *
 * @mudo De bron van een FilterSetBeed definieren we hier als een SetBeed. Dit
 *       kan echter ook een OrderedSetBeed zijn. Dit betekent dat deze beeds in
 *       een hierarchie moeten geplaatst worden.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getFilterCriterionFactory() != null;
 * @invar  getSource() == null ==> get().isEmpty();
 * @invar  getSource() != null ==>
 *           get() == {element : getSource().get().contains(element) &&
 *                               getFilterCriterionFactory().
 *                                 createFilterCriterion(element).isValid()};
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
   * @pre   filterCriterionFactory != null;
   * @post  getOwner() == owner;
   * @post  getFilterCriterionFactory() == filterCriterionFactory;
   * @post  getSource() == null;
   * @post  get().isEmpty();
   */
  public FilteredSetBeed(FilterCriterionFactory<_Element_> filterCriterionFactory, AggregateBeed owner) {
    super(owner);
    $filterCriterionFactory = filterCriterionFactory;
  }


  /*<property name="filterCriterionFactory">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final FilterCriterionFactory<_Element_> getFilterCriterionFactory() {
    return $filterCriterionFactory;
  }

  private FilterCriterionFactory<_Element_> $filterCriterionFactory;

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
   * @param  source
   * @post   getSource() == source;
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
    Set<_Element_> oldValue = get();
    recalculate();
    if (! Comparison.equalsWithNull(oldValue, get())) {
      fireChangeEvent(
        new SetEvent<_Element_>(
          FilteredSetBeed.this, get(), oldValue, null));
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
      // add a criterion for each added beed
      Set<_Element_> added = event.getAddedElements();
      Set<_Element_> addedFiltered = new HashSet<_Element_>();
      for (_Element_ beed : added) {
        // add the FilteredSetBeed as listener of the added beed
        beed.addListener($beedListener);
        // add a criterion for the added beed
        FilterCriterion<_Element_> criterion =
          getFilterCriterionFactory().createFilterCriterion(beed);
        $filterCriterionSet.add(criterion);
        // check whether the beed will be in the resulting set
        if (criterion.isValid()) {
          addedFiltered.add(beed);
          $nbValidElements++;
        }
      }
      // remove the FilteredSetBeed as listener from all beeds that are removed from the source by the given event
      // remove the criterion of each removed beed
      Set<_Element_> removed = event.getRemovedElements();
      Set<_Element_> removedFiltered = new HashSet<_Element_>();
      for (_Element_ beed : removed) {
        // remove the FilteredSetBeed as listener of the removed beed
        beed.removeListener($beedListener);
        // remove the criterion of the removed beed
        FilterCriterion<_Element_> criterion = findFilterCriterion(beed);
        $filterCriterionSet.remove(criterion);
        // check whether the beed was in the resulting set
        if (criterion.isValid()) {
          removedFiltered.add(beed);
          $nbValidElements--;
        }
      }
      // notify the listeners if a beed has been added or removed
      if (!addedFiltered.isEmpty() || !removedFiltered.isEmpty()) {
        fireChangeEvent(
          new SetEvent<_Element_>(
            FilteredSetBeed.this,
            addedFiltered,
            removedFiltered,
            event.getEdit()));
      }
    }

  };


  private final Listener<_Event_> $beedListener = new Listener<_Event_>() {

    /**
     * @post    get() == the result of filtering the elements of the given source
     * @post    The listeners of this beed are notified.
     */
    public void beedChanged(_Event_ event) {
//      // check whether the validity of the beed has changed
//      Beed<?> element = event.getSource();
//      FilterCriterion<_Element_> criterion = findFilterCriterion(element);
//      Set<_Element_> added = new HashSet<_Element_>();
//      Set<_Element_> removed = new HashSet<_Element_>();
//      if (criterion.oldValue != criterion.newValue) {
//        if (criterion.newValue) {
//          $nbValidElements++;
//          added.add(beed);
//        }
//        else {
//          $nbValidElements--;
//          removed.add(beed);
//        }
//        // notify the listeners if the value has changed
//        fireChangeEvent(new SetEvent<_Element_>(FilteredSetBeed.this, added, removed, event.getEdit()));
//      }
    }

  };


  /**
   * The filter criterions that correspond to the elements in
   * {@link #getSource()}.
   *
   * @invar  $filterCriterionSet != null;
   */
  private Set<FilterCriterion<_Element_>> $filterCriterionSet =
    new HashSet<FilterCriterion<_Element_>>();


  /**
   * The number of elements in {@link #$filterCriterionSet} that
   * is valid.
   *
   * @invar  $nbValidElements >= 0;
   */
  private int $nbValidElements = 0;


  /**
   * @basic
   */
  public final Set<_Element_> get() {
    return filterElements($filterCriterionSet, $nbValidElements);
  }


  /**
   * Returns a set containing all elements of the {@link FilterCriterion criterions}
   * in the given set that are valid.
   *
   * @param   filterCriterionSet
   * @param   nbValidElements
   * @pre     filterCriterionSet != null;
   * @pre     (forAll FilterCriterion criterion; filterCriterionSet.contains(criterion); criterion != null);
   * @pre     nbValidElements == card{element : filterCriterionSet.contains(element) &&
   *                                           criterion.isValid()};
   * @return  result == {criterion.getElement() : filterCriterionSet.contains(criterion) &&
   *                                              criterion.isValid()};
   */
  private Set<_Element_> filterElements(
      final Set<FilterCriterion<_Element_>> filterCriterionSet,
      final int nbValidElements) {

    return new AbstractSet<_Element_>() {

      @Override
      public Iterator<_Element_> iterator() {
        final Iterator<FilterCriterion<_Element_>> filteredCriterionIterator =
          filterCriterionSet.iterator();
        return new Iterator<_Element_>() {

          private int $nbVisited = 0;

          /**
           * Delegation.
           */
          public boolean hasNext() {
            return $nbVisited < nbValidElements;
          }

          /**
           * Delegation.
           */
          public _Element_ next() {
            FilterCriterion<_Element_> next = filteredCriterionIterator.next();
            while (!next.isValid()) {
              next = filteredCriterionIterator.next();
            }
            $nbVisited++;
            return next.getElement();
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
        return nbValidElements;
      }

    };

  }

  /**
   * Remove the given element from the set of filter criterions.
   */
  private FilterCriterion<_Element_> findFilterCriterion(_Element_ element) {
    for (FilterCriterion<_Element_> criterion : $filterCriterionSet) {
      if (criterion.getElement().equals(element)) {
        return criterion;
      }
    }
    return null;
  }

  /**
   * The value of $filterCriterionSet and $nbValidElements is recalculated.
   * This is done by iterating over the beeds in the source set beed.
   * When the source is null, $filterCriterionSet is an empty set and
   * $nbValidElements is 0.
   * When the source contains zero beeds, $filterCriterionSet is an empty set
   * and $nbValidElements is 0.
   * Otherwise, $filterCriterionSet contains a filter criterion for each
   * beed in the given set and $nbValidElements is the number of valid
   * elements.
   */
  public void recalculate() {
    $filterCriterionSet = new HashSet<FilterCriterion<_Element_>>();
    $nbValidElements = 0;
    Set<_Element_> fromSet = getSource() != null
                              ? getSource().get()
                              : new HashSet<_Element_>();
    for (_Element_ from : fromSet) {
      FilterCriterion<_Element_> filterCriterion =
        getFilterCriterionFactory().createFilterCriterion(from);
      $filterCriterionSet.add(filterCriterion);
      if (filterCriterion.isValid()) {
        $nbValidElements++;
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

