/*<license>
Copyright 2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $ by the authors mentioned below.

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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.AbstractUpdateSourceDependentDelegate;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Dependent;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A {@link SetBeed} that returns the cartesian product of two given
 * {@link SetBeed set beeds}.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getSource1() == null || getSource2() == null
 *           ? get().isEmpty()
 *           : true;
 * @invar  getSource1() != null && getSource2() == null
 *           ? get() == {tuple : getSource1().get().contains(tuple.getElement1()) &&
 *                               getSource2().get().contains(tuple.getElement2()) &&}
 *           : true;
 * @invar  getSourcePath1() != null
 *           ? getSource1() == getSourcePath1().get()
 *           : getSource1() == null;
 */
@Copyright("2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 913 $",
         date     = "$Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $")
public class CartesianProductBeed<_Element1_ extends Beed<?>, _Element2_ extends Beed<?>>
    extends AbstractSetBeed<CartesianProductBeed<_Element1_, _Element2_>.Tuple, SetEvent<CartesianProductBeed<_Element1_, _Element2_>.Tuple>>
    implements SetBeed<CartesianProductBeed<_Element1_, _Element2_>.Tuple, SetEvent<CartesianProductBeed<_Element1_, _Element2_>.Tuple>> {

  /**
   * @post  getSourcePath1() == null;
   * @post  getSourcePath2() == null;
   * @post  getSource1() == null;
   * @post  getSource2() == null;
   * @post  owner != null
   *          ? owner.registerAggregateElement(this)
   *          : true;
   */
  public CartesianProductBeed(AggregateBeed owner) {
    super(owner);
  }

  public class Tuple extends AbstractBeanBeed {

    public Tuple(_Element1_ element1, _Element2_ element2) {
      $element1 = element1;
      $element2 = element2;
    }


    /*<property name="element1">*/
//  -----------------------------------------------------------------

    /**
     * @basic
     */
    public final _Element1_ getElement1() {
      return $element1;
    }

    private final _Element1_ $element1;

    /*</property>*/


    /*<property name="element2">*/
//  -----------------------------------------------------------------

    /**
     * @basic
     */
    public final _Element2_ getElement2() {
      return $element2;
    }

    private final _Element2_ $element2;

    /*</property>*/


    @Override
    public boolean equals(Object obj) {
      if (obj instanceof CartesianProductBeed.Tuple) {
        Tuple other = (Tuple) obj;
        return other.getElement1().equals(getElement1()) &&
        other.getElement2().equals(getElement2());
      }
      return false;
    }

  }


  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

    @Override
    protected SetEvent<Tuple> filteredUpdate(
        Map<Beed<?>, Event> events, Edit<?> edit) {
      assert events != null;
      assert events.size() > 0;
      /* Events can come:
       * - from the first source path,
       * - from the second source path,
       * - from the first source (elements added or removed) and
       * - from the second source (elements added or removed)
       * If an event comes from a source path, we might also get
       * events from the corresponding source. But these don't
       * matter, since we have a new source.
       */
      HashSet<Tuple> addedTuples = new HashSet<Tuple>();
      HashSet<Tuple> removedTuples = new HashSet<Tuple>();
      // source 1
      PathEvent<SetBeed<_Element1_, ?>> pathEvent1 =
        (PathEvent<SetBeed<_Element1_, ?>>)events.get($sourcePath1);
      if (pathEvent1 != null) {
        handleSourcePath1Event(pathEvent1, addedTuples, removedTuples);
        // if there is a path event, don't deal with other events
      }
      else {
        @SuppressWarnings("unchecked")
        SetEvent<_Element1_> setEvent1 = (SetEvent<_Element1_>)events.get($source1);
        if (setEvent1 != null) {
          handleSource1Event(setEvent1, addedTuples, removedTuples);
        }
      }
      // source 2
      PathEvent<SetBeed<_Element2_, ?>> pathEvent2 =
        (PathEvent<SetBeed<_Element2_, ?>>)events.get($sourcePath2);
      if (pathEvent2 != null) {
        handleSourcePath2Event(pathEvent2, addedTuples, removedTuples);
        // if there is a path event, don't deal with other events
      }
      else {
        @SuppressWarnings("unchecked")
        SetEvent<_Element2_> setEvent2 =
          (SetEvent<_Element2_>)events.get($source2);
        if (setEvent2 != null) {
          handleSource2Event(setEvent2, addedTuples, removedTuples);
        }
      }
      return createEvent(addedTuples, removedTuples, edit);
    }

    /**
     * If the given event is caused by {@link CartesianProductBeed#getSourcePath1()},
     * then we replace the old first source by the new value in the given event.
     * The tuples that are added by this operation are gathered in
     * <code>addedTuples</code>.
     * The tuples that are removed by this operation are gathered in
     * <code>removedTuples</code>.
     *
     * @pre  pathEvent != null;
     * @pre  addedTuples != null;
     * @pre  removedTuples != null;
     */
    private void handleSourcePath1Event(
        PathEvent<SetBeed<_Element1_, ?>> pathEvent1,
        HashSet<Tuple> addedTuples,
        HashSet<Tuple> removedTuples) {
      assert pathEvent1 != null;
      assert addedTuples != null;
      assert removedTuples != null;
      assert pathEvent1.getSource() == $sourcePath1;
      SetBeed<_Element1_, ?> newSource1 = pathEvent1.getNewValue();
      setSource1(newSource1, addedTuples, removedTuples);
    }

    /**
     * If the given event is caused by {@link CartesianProductBeed#getSourcePath2()},
     * then we replace the old second source by the new value in the given event.
     * The tuples that are added by this operation are gathered in
     * <code>addedTuples</code>.
     * The tuples that are removed by this operation are gathered in
     * <code>removedTuples</code>.
     *
     * @pre  pathEvent != null;
     * @pre  addedTuples != null;
     * @pre  removedTuples != null;
     */
    private void handleSourcePath2Event(
        PathEvent<SetBeed<_Element2_, ?>> pathEvent2,
        HashSet<Tuple> addedTuples,
        HashSet<Tuple> removedTuples) {
      assert pathEvent2 != null;
      assert addedTuples != null;
      assert removedTuples != null;
      assert pathEvent2.getSource() == $sourcePath2;
      SetBeed<_Element2_, ?> newSource2 = pathEvent2.getNewValue();
      setSource2(newSource2, addedTuples, removedTuples);
    }

    /**
     * When the given event is caused by {@link CartesianProductBeed#getSource1()},
     * we remove the tuples whose first elements are in
     * {@link SetEvent#getRemovedElements()} and we add the new tuples
     * whose first elements are in {@link SetEvent#getAddedElements()}.
     * The elements that are added to {@link CartesianProductBeed#$cartesianProduct}
     * by this operation are gathered in <code>addedTuples</code>.
     * The elements that are removed from {@link FilteredSetBeed#$cartesianProduct}
     * by this operation are gathered in <code>removedTuples</code>.
     *
     * @pre  event != null;
     * @pre  addedTuples != null;
     * @pre  removedTuples != null;
     */
    private void handleSource1Event(SetEvent<_Element1_> setEvent1,
        HashSet<Tuple> addedTuples,
        HashSet<Tuple> removedTuples) {
      assert setEvent1 != null;
      assert addedTuples != null;
      assert removedTuples != null;
      assert setEvent1.getSource() == $source1;
      for (_Element1_ element1 : setEvent1.getAddedElements()) {
        addElement1(element1, addedTuples);
      }
      for (_Element1_ element1 : setEvent1.getRemovedElements()) {
        removeElement1(element1, removedTuples);
      }
    }

    /**
     * When the given event is caused by {@link CartesianProductBeed#getSource2()},
     * we remove the tuples whose second elements are in
     * {@link SetEvent#getRemovedElements()} and we add the new tuples
     * whose second elements are in {@link SetEvent#getAddedElements()}.
     * The elements that are added to {@link CartesianProductBeed#$cartesianProduct}
     * by this operation are gathered in <code>addedTuples</code>.
     * The elements that are removed from {@link FilteredSetBeed#$cartesianProduct}
     * by this operation are gathered in <code>removedTuples</code>.
     *
     * @pre  event != null;
     * @pre  addedTuples != null;
     * @pre  removedTuples != null;
     */
    private void handleSource2Event(SetEvent<_Element2_> setEvent2,
        HashSet<Tuple> addedTuples,
        HashSet<Tuple> removedTuples) {
      assert setEvent2 != null;
      assert addedTuples != null;
      assert removedTuples != null;
      assert setEvent2.getSource() == $source2;
      for (_Element2_ element2 : setEvent2.getAddedElements()) {
        addElement2(element2, addedTuples);
      }
      for (_Element2_ element2 : setEvent2.getRemovedElements()) {
        removeElement2(element2, removedTuples);
      }
    }

  };

  private void addElement1(_Element1_ element1, HashSet<Tuple> addedTuples) {
    if (containsElement1(element1, $cartesianProduct)) {
      // NOP
    }
    else {
      for (_Element2_ element2 : getSource2().get()) {
        Tuple tuple = new Tuple(element1, element2);
        $cartesianProduct.add(tuple);
        addedTuples.add(tuple);
      }
    }
  }

  private void removeElement1(_Element1_ element1, HashSet<Tuple> removedTuples) {
    if (!containsElement1(element1, $cartesianProduct)) {
      // NOP
    }
    else {
      for (Tuple tuple : new HashSet<Tuple>($cartesianProduct)) {
        if (tuple.getElement1() == element1) {
          $cartesianProduct.remove(tuple);
          removedTuples.add(tuple);
        }
      }
    }
  }

  private boolean containsElement1(_Element1_ element1, HashSet<Tuple> tuples) {
    for (Tuple tuple : tuples) {
      if (tuple.getElement1() == element1) {
        return true;
      }
    }
    return false;
  }

  private void addElement2(_Element2_ element2, HashSet<Tuple> addedTuples) {
    if (containsElement2(element2, $cartesianProduct)) {
      // NOP
    }
    else {
      for (_Element1_ element1 : getSource1().get()) {
        Tuple tuple = new Tuple(element1, element2);
        $cartesianProduct.add(tuple);
        addedTuples.add(tuple);
      }
    }
  }

  private void removeElement2(_Element2_ element2, HashSet<Tuple> removedTuples) {
    if (!containsElement2(element2, $cartesianProduct)) {
      // NOP
    }
    else {
      for (Tuple tuple : new HashSet<Tuple>($cartesianProduct)) {
        if (tuple.getElement2() == element2) {
          $cartesianProduct.remove(tuple);
          removedTuples.add(tuple);
        }
      }
    }
  }

  private boolean containsElement2(_Element2_ element2, HashSet<Tuple> tuples) {
    for (Tuple tuple : tuples) {
      if (tuple.getElement2() == element2) {
        return true;
      }
    }
    return false;
  }

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


  /*<property name="source1">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends SetBeed<_Element1_, ?>> getSourcePath1() {
    return $sourcePath1;
  }

  /**
   * @return getSourcePath1() == null ? null : getSourcePath1().get();
   */
  public final SetBeed<_Element1_, ?> getSource1() {
    return $source1;
  }

  /**
   * The old first source path is removed as update source.
   * The new first source path is added as update source.
   * The first source is replaced by the new first source: see {@link #setSource1(SetBeed)}.
   */
  public final void setSourcePath1(Path<? extends SetBeed<_Element1_, ?>> sourcePath1) {
    if ($sourcePath1 instanceof AbstractDependentPath) {
      $dependent.removeUpdateSource($sourcePath1);
    }
    $sourcePath1 = sourcePath1;
    if ($sourcePath1 instanceof AbstractDependentPath) {
      $dependent.addUpdateSource($sourcePath1);
    }
    if ($sourcePath1 != null) {
      setSource1($sourcePath1.get());
    }
    else {
      setSource1(null);
    }
  }

  /**
   * @param   source1
   * @post    getSource1() == source1;
   * @post    get() == the cartesian product of {@link #getSource1()} and
   *          {@link #getSource2()}.
   * @post    The {@link CartesianProductBeed} is registered as a listener of the given SetBeed.
   * @post    The listeners of this beed are notified when the value changes.
   */
  private final void setSource1(SetBeed<_Element1_, ?> source1) {
    HashSet<Tuple> addedTuples =
      new HashSet<Tuple>();
    HashSet<Tuple> removedTuples =
      new HashSet<Tuple>();
    setSource1(source1, addedTuples, removedTuples);
    ActualSetEvent<Tuple> event =
      createEvent(addedTuples, removedTuples, null);
    if (event != null) {
      updateDependents(event);
    }
  }

  /**
   * Replace the old {@link #getSource1() first source} by the given source.
   * Remove the old {@link #getSource1() first source} as update source.
   * Add the given source as update source.
   * Update the {@link #$cartesianProduct}:
   * - remove the tuples that were there, and put them in
   *   <code>removedTuples</code>.
   * - create new tuples: the elements in the given source are at the first
   *   position and the elements in the {@link #getSource2() second source}
   *   are at the second position. Put these tuples in <code>addedTuples</code>.
   *
   * @pre  addedTuples != null;
   * @pre  removedTuples != null;
   */
  private final void setSource1(SetBeed<_Element1_, ?> source1,
      HashSet<Tuple> addedTuples,
      HashSet<Tuple> removedTuples) {
    assert addedTuples != null;
    assert removedTuples != null;
    if ($source1 != null) {
      $dependent.removeUpdateSource($source1);
    }
    // set the source
    $source1 = source1;
    if ($source1 != null) {
      $dependent.addUpdateSource($source1);
    }
    recalculate(addedTuples, removedTuples);
  }

  private ActualSetEvent<Tuple> createEvent(
      HashSet<Tuple> addedTuples,
      HashSet<Tuple> removedTuples,
      Edit<?> edit) {
    ActualSetEvent<Tuple> event;
    @SuppressWarnings("unchecked")
    Set<Tuple> addedTuplesClone =
      (Set<Tuple>)addedTuples.clone();
    removeTuples(addedTuples, removedTuples);
    removeTuples(removedTuples, addedTuplesClone);
    if ((! removedTuples.isEmpty()) || (! addedTuples.isEmpty())) {
      event = new ActualSetEvent<CartesianProductBeed<_Element1_, _Element2_>.Tuple>(
                           CartesianProductBeed.this,
                           addedTuples,
                           removedTuples,
                           edit);
    }
    else {
      event = null;
    }
    return event;
  }

  private Path<? extends SetBeed<_Element1_, ?>> $sourcePath1;

  private SetBeed<_Element1_, ?> $source1;

  /*</property>*/

  private void removeTuples(Set<Tuple> tuples,
      Set<Tuple> tuplesToRemove) {
    for (Tuple tuple : new HashSet<Tuple>(tuples)) {
      if (containsTuple(tuple, tuplesToRemove)) {
        tuples.remove(tuple);
      }
    }
  }

  private boolean containsTuple(Tuple tuple,
      Set<Tuple> tuples) {
    for (Tuple currentTuple : tuples) {
      if (tuple.getElement1() == currentTuple.getElement1() &&
          tuple.getElement2() == currentTuple.getElement2()) {
        return true;
      }
    }
    return false;
  }

  private Tuple findTuple(_Element1_ element1, _Element2_ element2, Set<Tuple> tuples) {
    for (Tuple currentTuple : tuples) {
      if (element1 == currentTuple.getElement1() &&
          element2 == currentTuple.getElement2()) {
        return currentTuple;
      }
    }
    return null;
  }

  /**
   * @return  (forSome Tuple<_Element1_, _Element2_> currentTuple;
   *             get().contains(currentTuple);
   *             currentTuple.getElement1() == tuple.getElement1() &&
   *             currentTuple.getElement2() == tuple.getElement2());
   */
   public boolean containsTuple(Tuple tuple) {
     return containsTuple(tuple, $cartesianProduct);
   }

   /**
    * @return  (forSome Tuple<_Element1_, _Element2_> currentTuple;
    *             get().contains(currentTuple);
    *             currentTuple.getElement1() == element1 &&
    *             currentTuple.getElement2() == element2)
    *             ? result != null &&
    *               get().contains(result) &&
    *               result.getElement1() == element1 &&
    *               result.getElement2() == element2
    *             : result == null;
    */
   public Tuple findTuple(_Element1_ element1, _Element2_ element2) {
     return findTuple(element1, element2, $cartesianProduct);
   }


  /*<property name="source2">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends SetBeed<_Element2_, ?>> getSourcePath2() {
    return $sourcePath2;
  }

  /**
   * @return getSourcePath2() == null ? null : getSourcePath2().get();
   */
  public final SetBeed<_Element2_, ?> getSource2() {
    return $source2;
  }

  /**
   * The old second source path is removed as update source.
   * The new second source path is added as update source.
   * The second source is replaced by the new second source: see {@link #setSource2(SetBeed)}.
   */
  public final void setSourcePath2(Path<? extends SetBeed<_Element2_, ?>> sourcePath2) {
    if ($sourcePath2 instanceof AbstractDependentPath) {
      $dependent.removeUpdateSource($sourcePath2);
    }
    $sourcePath2 = sourcePath2;
    if ($sourcePath2 instanceof AbstractDependentPath) {
      $dependent.addUpdateSource($sourcePath2);
    }
    if ($sourcePath2 != null) {
      setSource2($sourcePath2.get());
    }
    else {
      setSource2(null);
    }
  }

  /**
   * @param   source2
   * @post    getSource2() == source2;
   * @post    get() == the cartesian product of {@link #getSource1()} and
   *          {@link #getSource2()}.
   * @post    The {@link CartesianProductBeed} is registered as a listener of the given SetBeed.
   * @post    The listeners of this beed are notified when the value changes.
   */
  private final void setSource2(SetBeed<_Element2_, ?> source2) {
    HashSet<Tuple> addedTuples =
      new HashSet<Tuple>();
    HashSet<Tuple> removedTuples =
      new HashSet<Tuple>();
    setSource2(source2, addedTuples, removedTuples);
    ActualSetEvent<Tuple> event =
      createEvent(addedTuples, removedTuples, null);
    if (event != null) {
      updateDependents(event);
    }
  }

  /**
   * Replace the old {@link #getSource2() second source} by the given source.
   * Remove the old {@link #getSource2() second source} as update source.
   * Add the given source as update source.
   * Update the {@link #$cartesianProduct}:
   * - remove the tuples that were there, and put them in
   *   <code>removedTuples</code>.
   * - create new tuples: the elements in the given source are at the first
   *   position and the elements in the {@link #getSource2() second source}
   *   are at the second position. Put these tuples in <code>addedTuples</code>.
   *
   * @pre  addedTuples != null;
   * @pre  removedTuples != null;
   */
  private final void setSource2(SetBeed<_Element2_, ?> source2,
      HashSet<Tuple> addedTuples,
      HashSet<Tuple> removedTuples) {
    assert addedTuples != null;
    assert removedTuples != null;
    if ($source2 != null) {
      $dependent.removeUpdateSource($source2);
    }
    // set the source
    $source2 = source2;
    if ($source2 != null) {
      $dependent.addUpdateSource($source2);
    }
    recalculate(addedTuples, removedTuples);
  }

  private void recalculate(HashSet<Tuple> addedTuples, HashSet<Tuple> removedTuples) {
    removedTuples.addAll($cartesianProduct);
    recalculate();
    addedTuples.addAll($cartesianProduct);
  }

  private Path<? extends SetBeed<_Element2_, ?>> $sourcePath2;

  private SetBeed<_Element2_, ?> $source2;

  /*</property>*/

  private void recalculate() {
    $cartesianProduct.clear();
    if (getSource1() != null && getSource2() != null) {
      for (_Element1_ element1 : getSource1().get()) {
        for (_Element2_ element2 : getSource2().get()) {
          Tuple tuple =
            new Tuple(element1, element2);
          $cartesianProduct.add(tuple);
        }
      }
    }
  }

  /**
   * The set of filtered elements in the {@link #getSource()}.
   *
   * @invar  $filteredSet != null;
   */
  private final HashSet<Tuple> $cartesianProduct =
    new HashSet<Tuple>();


  /**
   * @basic
   */
  public final Set<Tuple> get() {
    return Collections.unmodifiableSet($cartesianProduct);
  }

  public final Set<? extends Beed<?>> getUpdateSources() {
    return $dependent.getUpdateSources();
  }

  private final static Set<? extends Beed<?>> PHI = Collections.emptySet();

  public final Set<? extends Beed<?>> getUpdateSourcesTransitiveClosure() {
    /* fixed to make it possible to use this method during construction,
     * before $dependent is initialized. But that is bad code, and should be
     * fixed.
     */
    return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
  }


  @Override
  protected String otherToStringInformation() {
    return "#: " + get().size();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "elements:\n");
    Iterator<Tuple> iter = get().iterator();
    while (iter.hasNext()) {
      Tuple tuple = iter.next();
      sb.append(indent(level + 2) +
          "(" +
          tuple.getElement1().toString() + ", " + tuple.getElement2().toString() +
          ")" + "\n");
    }
  }

}

