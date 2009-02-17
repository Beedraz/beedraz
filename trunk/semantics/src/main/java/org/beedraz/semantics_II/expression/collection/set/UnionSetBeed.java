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

package org.beedraz.semantics_II.expression.collection.set;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * A union beed is a derived beed that returns the union of a given
 * set of {@link SetBeed set beeds}.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getSources() != null;
 * @invar  (forAll SetBeed source; getSources().contains(source); source != null);
 * @invar  get().size() ==
 *           the size of the union of all sets in the given set beeds
 * @invar  (forAll SetBeed<_Element_> setBeed;
 *            getSources().contains(setBeed);
 *            (forAll _Element_ element;
 *               setBeed.get().contains(element);
 *               get().contains(element)));
 *            get() is the union of all sets in the given set beeds
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class UnionSetBeed<_Element_>
    extends AbstractDependentSetBeed<_Element_, SetEvent<_Element_>> {

  /**
   * @post  getSources().isEmpty();
   * @post  get().isEmpty();
   */
  public UnionSetBeed() {
    this(null);
  }

  /**
   * @post  getSources().isEmpty();
   * @post  get().isEmpty();
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  public UnionSetBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @post    get() == the union of the sources
   */
  @Override
  protected SetEvent<_Element_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    /* Optimized update is too difficult (what if several events remove the same element?)
     * If 1 source removes an element, but it exists also in other sources, it must stay
     * in the union. But what if all the sources that have the element remove it? Then it
     * has to be removed too.
     */
    @SuppressWarnings("unchecked")
    HashSet<_Element_> oldUnion = (HashSet<_Element_>)$union.clone();
    recalculate();
    @SuppressWarnings("unchecked")
    HashSet<_Element_> added = (HashSet<_Element_>)$union.clone();
    added.removeAll(oldUnion);
    @SuppressWarnings("unchecked")
    HashSet<_Element_> removed = (HashSet<_Element_>)oldUnion.clone();
    removed.removeAll($union);
    // notify the listeners if elements are added or removed
    if (! added.isEmpty() || ! removed.isEmpty()) {
      return new ActualSetEvent<_Element_>(UnionSetBeed.this, added, removed, edit);
    }
    else {
      return null;
    }
  }




  /*<property name="sources">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Set<SetBeed<? extends _Element_, ?>> getSources() {
    return Collections.unmodifiableSet($sources);
  }

  /**
   * @param  source
   * @pre    source != null;
   * @post   getSources().contains(source);
   * @post   get() = the union of the sources
   * @post   The UnionBeed is added as listener of the given source.
   * @post   The listeners of this beed are notified when the value changes.
   * @post   The listeners of the size beed are notified when the size of this
   *         set has changed.
   */
  public final void addSource(SetBeed<? extends _Element_, ?> source) {
    if (! $sources.contains(source)) {
      assert source != null;
      @SuppressWarnings("unchecked")
      HashSet<_Element_> oldValue = (HashSet<_Element_>)$union.clone();
      // add the source
      addUpdateSource(source);
      $sources.add(source);
      // add the elements of the given source to the union
      $union.addAll(source.get());
      // notify the listeners of this beed if the union has changed
      updateDependents(oldValue);
    }
  }

  private void updateDependents(HashSet<_Element_> oldValue) {
    if (! ComparisonUtil.equalsWithNull(oldValue, $union)) {
      @SuppressWarnings("unchecked")
      HashSet<_Element_> added = (HashSet<_Element_>)$union.clone();
      added.removeAll(oldValue);
      @SuppressWarnings("unchecked")
      HashSet<_Element_> removed = (HashSet<_Element_>)oldValue.clone();
      removed.removeAll($union);
      updateDependents(new ActualSetEvent<_Element_>(UnionSetBeed.this, added, removed, null));
    }
  }

  /**
   * @param  source
   * @post   !getSources().contains(source);
   * @post   get() = the union of the sources
   * @post   The UnionBeed is removed as listener of the given source.
   * @post   The listeners of this beed are notified when the value changes.
   * @post   The listeners of the size beed are notified when the size of this
   *         set has changed.
   */
  public final void removeSource(SetBeed<_Element_, ?> source) {
    if ($sources.contains(source)) {
      assert source != null;
      @SuppressWarnings("unchecked")
      HashSet<_Element_> oldValue = (HashSet<_Element_>)$union.clone();
      // remove the source
      removeUpdateSource(source);
      $sources.remove(source);
      // remove the elements of the given source from the union
      for (_Element_ element : source.get()) {
        if (!contains($sources, element)) {
          $union.remove(element);
        }
      }
      // notify the listeners of this beed if the union has changed
      updateDependents(oldValue);
    }
  }

  static <_E_> boolean contains(Set<? extends SetBeed<? extends _E_, ?>> sources, _E_ element) {
    for (SetBeed<? extends _E_, ?> source : sources) {
      if (source.get().contains(element)) {
        return true;
      }
    }
    return false;
  }

  private Set<SetBeed<? extends _Element_, ?>> $sources = new HashSet<SetBeed<? extends _Element_, ?>>();

  /*</property>*/



  private void recalculate() {
    $union.clear();
    for (SetBeed<? extends _Element_, ?> sb : getSources()) {
      $union.addAll(sb.get());
    }
  }

  /**
   * @invar  $union != null;
   * @invar  Contains the union of the sets of all sources.
   */
  private HashSet<_Element_> $union = new HashSet<_Element_>();

  /**
   * @basic
   */
  public final Set<_Element_> get() {
    return Collections.unmodifiableSet($union);
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

