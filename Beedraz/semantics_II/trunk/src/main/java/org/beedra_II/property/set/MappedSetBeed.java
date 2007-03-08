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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.AbstractPropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A {@link SetBeed} that mappes a given {@link SetBeed}
 * to another {@link Set} using a {@link Mapping}.
 *
 * @mudo We veronderstellen hier dat een mapping injectief is, dwz dat twee
 *       verschillende waarden nooit gemapt worden op hetzelfde beeld
 *       (zie invariant in {@link Mapping}).
 *       Indien dit niet het geval is, moeten we immers een MappedCollectionBeed
 *       maken, die dubbels toelaat.
 * @mudo De bron van een MappedSetBeed definieren we hier als een SetBeed. Dit
 *       kan echter ook een OrderedSetBeed zijn. Dit betekent dat deze beeds in
 *       een hierarchie moeten geplaatst worden.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getMapping() != null;
 * @invar  get().size() ==
 *           getSource() != null
 *             ? getSource().get().size()
 *             : 0;
 * @invar  (forAll _From_ from;
 *            getSource().contains(from);
 *            get().contains(getMapping().map(from)));
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class MappedSetBeed<_From_, _To_>
    extends AbstractPropertyBeed<SetEvent<_To_>>
    implements SetBeed<_To_> {

  /**
   * @pre   owner != null;
   * @pre   mapping != null;
   * @post  getOwner() == owner;
   * @post  getMapping() == mapping;
   * @post  getSource() == null;
   */
  public MappedSetBeed(Mapping<_From_, _To_> mapping, AggregateBeed owner) {
    super(owner);
    $mapping = mapping;
  }


  /*<property name="mapping">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Mapping<_From_, _To_> getMapping() {
    return $mapping;
  }

  private Mapping<_From_, _To_> $mapping;

  /*</property>*/


  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<_From_> getSource() {
    return $source;
  }

  /**
   * @param  source
   * @post   getSource() == source;
   */
  public final void setSource(SetBeed<_From_> source) {
    $source = source;
  }

  private SetBeed<_From_> $source;

  /*</property>*/

  /**
   * @basic
   */
  public final Set<_To_> get() {
    return new AbstractSet<_To_>() {

      @Override
      public Iterator<_To_> iterator() {
        final Iterator<_From_> iteratorFrom = iteratorFrom();
        return new Iterator<_To_>() {

          /**
           * When the source is effective, we return true iff the
           * iterator over the source set returns true.
           * When the source is not effective, we return false.
           */
          public boolean hasNext() {
            return iteratorFrom != null
                     ? iteratorFrom.hasNext()
                     : false;
          }

          /**
           * When the source is effective, we return the mapping of the
           * next element in the iterator over the source set.
           * When the source is not effetive, a NoSuchElementException is thrown.
           */
          public _To_ next() {
            if (iteratorFrom == null) {
              throw new NoSuchElementException();
            }
            return getMapping().map(iteratorFrom.next());
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
        return getSource() != null
                 ? getSource().get().size()
                 : 0;
      }

      /**
       * The set that will be mapped.
       */
      private Set<_From_> setFrom() {
        return getSource() != null
                 ? getSource().get()
                 : null;
      }

      /**
       * An iterator on the set that will be mapped.
       */
      private Iterator<_From_> iteratorFrom() {
        return setFrom() != null
                 ? setFrom().iterator()
                 : null;
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
  protected SetEvent<_To_> createInitialEvent() {
    return new SetEvent<_To_>(this, get(), null, null);
  }

}

