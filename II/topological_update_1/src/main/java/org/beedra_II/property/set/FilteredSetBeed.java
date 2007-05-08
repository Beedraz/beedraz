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

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.AbstractPropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


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
 * @invar  getFilters() != null;
 * @invar  get() == {element : getSource().get().contains(element) &&
 *                             getFilter().filter()};
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class FilteredSetBeed<_Element_>
    extends AbstractPropertyBeed<SetEvent<_Element_>>
    implements SetBeed<_Element_> {

  /**
   * @pre   owner != null;
   * @pre   filter != null;
   * @post  getOwner() == owner;
   * @post  getFilter() == filter;
   * @post  getSource() == null;
   */
  public FilteredSetBeed(Filter<_Element_> filter, AggregateBeed owner) {
    super(owner);
    $filter = filter;
  }


  /*<property name="filter">*/
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
   * @param  source
   * @post   getSource() == source;
   */
  public final void setSource(SetBeed<_Element_> source) {
    $source = source;
  }

  private SetBeed<_Element_> $source;

  /*</property>*/

  /**
   * @basic
   */
  public final Set<_Element_> get() {
    return new AbstractSet<_Element_>() {

      @Override
      public Iterator<_Element_> iterator() {
        final Iterator<_Element_> filteredSetIterator = filteredSetIterator();
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
       * The size of the source set, if the source is effective.
       * Zero, if the source set is not effective.
       */
      @Override
      public int size() {
        return filteredSet().size();
      }

      /**
       * The filtered set.
       * Return an empty set when the given source is not effective.
       */
      private Set<_Element_> filteredSet() {
        Set<_Element_> sourceSet =
          getSource() != null
            ? getSource().get()
            : new HashSet<_Element_>();
        Set<_Element_> filteredSet = new HashSet<_Element_>();
        for (_Element_ element : sourceSet) {
          if (getFilter().filter(element)) {
            filteredSet.add(element);
          }
        }
        return filteredSet;
      }

      /**
       * An iterator on the filtered set.
       */
      private Iterator<_Element_> filteredSetIterator() {
        return filteredSet().iterator();
      }

    };
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

