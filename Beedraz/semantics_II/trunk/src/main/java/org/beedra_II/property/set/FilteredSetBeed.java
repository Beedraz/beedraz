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
public class FilteredSetBeed<_Element_ extends Beed<_FromEvent_>, _FromEvent_ extends Event>
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
    recalculate();
  }

  private SetBeed<_Element_> $source;

  /*</property>*/

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
    return new AbstractSet<_Element_>() {

      @Override
      public Iterator<_Element_> iterator() {
        final Iterator<FilterCriterion<_Element_>> filteredCriterionIterator =
          $filterCriterionSet.iterator();
        return new Iterator<_Element_>() {

          private int $nbVisited = 0;

          /**
           * Delegation.
           */
          public boolean hasNext() {
            return $nbVisited < $nbValidElements;
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
        return $nbValidElements;
      }

    };
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

