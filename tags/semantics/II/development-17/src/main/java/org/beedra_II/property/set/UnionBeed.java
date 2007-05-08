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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.AbstractPropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A union beed is a derived beed that returns the union of a given
 * set of {@link SetBeed set beeds}.
 *
 * @mudo Zelfde opmerking als bij MappedSetBeed: de bron(nen) van een UnionBeed
 *       kunnen SetBeed of OrderSetBeed zijn.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getSources() != null;
 * @invar  get().size() ==
 *           the size of the union of all sets in the given set beeds
 * @invar  (forAll SetBeed<_Element_> setBeed;
 *            getSources().contains(setBeed);
 *            (forAll _Element_ element;
 *               setBeed.get().contains(element);
 *               get().contains(element)));
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class UnionBeed<_Element_>
    extends AbstractPropertyBeed<SetEvent<_Element_>>
    implements SetBeed<_Element_> {

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  getSources().isEmpty();
   */
  public UnionBeed(AggregateBeed owner) {
    super(owner);
  }


  /*<property name="sources">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Set<SetBeed<_Element_>> getSources() {
    return Collections.unmodifiableSet($sources);
  }

  /**
   * @param  source
   * @post   getSources().contains(source);
   */
  public final void addSource(SetBeed<_Element_> source) {
    $sources.add(source);
  }

  /**
   * @param  source
   * @post   !getSources().contains(source);
   */
  public final void removeSource(SetBeed<_Element_> source) {
    $sources.remove(source);
  }

  private Set<SetBeed<_Element_>> $sources = new HashSet<SetBeed<_Element_>>();

  /*</property>*/

  /**
   * @basic
   */
  public final Set<_Element_> get() {
    return new AbstractSet<_Element_>() {

      @Override
      public Iterator<_Element_> iterator() {
        final Iterator<_Element_> unionIterator = union().iterator();
        return new Iterator<_Element_>() {

          public boolean hasNext() {
            return unionIterator.hasNext();
          }

          public _Element_ next() {
            return unionIterator.next();
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
       * The size of the union of the given source sets.
       */
      @Override
      public int size() {
        return union().size();
      }

      /**
       * Returns the union of all source beeds.
       */
      private Set<_Element_> union() {
        Set<_Element_> union = new HashSet<_Element_>();
        for (SetBeed<_Element_> setBeed: getSources()) {
          union.addAll(setBeed.get());
        }
        return union;
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

