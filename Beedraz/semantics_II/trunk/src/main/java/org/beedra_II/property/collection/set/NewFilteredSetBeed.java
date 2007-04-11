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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedra_II.Beed;
import org.beedra_II.Event;
import org.beedra_II.aggregate.AbstractAggregateBeed;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.path.Path;
import org.beedra_II.path.PathEvent;
import org.beedra_II.path.PathFactory;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.bool.BooleanBeed;
import org.beedra_II.property.bool.BooleanEvent;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A {@link SetBeed} that returns a filtered subset of a given {@link SetBeed}.
 * The criterion is a {@link BooleanBeed}, selected through a {@link Path},
 * which is created for each element in the {@link #getSource() source set beed}
 * by a {@link PathFactory} (called {@link #getCriterion()} criterion) given at
 * construction.
 *
 * @author  Nele Smeets
 * @author  Jan Dockx
 * @author  Peopleware n.v.
 *
 * @invar  getCriterion() != null;
 * @invar  getSource() == null ? get().isEmpty();
 * @invar  getSource() != null ?
 *           get() == {element : getSource().get().contains(element) &&
 *                               getFilter().filter(element)};
 * @mudo invariants
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class NewFilteredSetBeed<_Element_ extends Beed<_Event_>, _Event_ extends Event>
    extends AbstractSetBeed<_Element_, SetEvent<_Element_>>
    implements SetBeed<_Element_, SetEvent<_Element_>> {

  /**
   * @pre   owner != null;
   * @pre   criterion != null;
   * @post  getOwner() == owner;
   * @post  getCriterion() == criterion;
   * @post  getSource() == null;
   * @post  get().isEmpty();
   */
  public NewFilteredSetBeed(PathFactory<_Element_, BooleanBeed> criterion, AggregateBeed owner) {
    super(owner);
    $criterion = criterion;
  }

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected SetEvent<_Element_> filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
        assert events != null;
        assert events.size() > 0;
        /* evenst can come from the source (elements added or removed) and from the
         * element criteria of each of the source elements, or from the source path
         */
        HashSet<_Element_> addedFilteredElements = new HashSet<_Element_>();
        HashSet<_Element_> removedFilteredElements = new HashSet<_Element_>();
        for (Event event : events.values()) {
          handleSourcePathEvent(event, addedFilteredElements, removedFilteredElements);
          handleSourceEvent(event, addedFilteredElements, removedFilteredElements);
          handleElementCriterionEvent(event, addedFilteredElements, removedFilteredElements);
        }
        return createEvent(addedFilteredElements, removedFilteredElements);
      }

      private void handleSourcePathEvent(Event event, HashSet<_Element_> addedFilteredElements, HashSet<_Element_> removedFilteredElements) {
        assert event != null;
        try {
          @SuppressWarnings("unchecked")
          PathEvent<SetBeed<_Element_, ?>> pathEvent = (PathEvent<SetBeed<_Element_, ?>>)event;
          SetBeed<_Element_, ?> newSource = pathEvent.getNewValue();
          setSource(newSource, addedFilteredElements, removedFilteredElements);
        }
        catch (ClassCastException ccExc) {
          // NOP
        }
      }

      private void handleSourceEvent(Event event, HashSet<_Element_> addedFilteredElements, HashSet<_Element_> removedFilteredElements) {
        assert event != null;
        try {
          @SuppressWarnings("unchecked")
          SetEvent<_Element_> setEvent = (SetEvent<_Element_>)event;
          assert setEvent.getSource() == $source;
          for (_Element_ e : setEvent.getAddedElements()) {
            sourceElementAdded(e, addedFilteredElements);
          }
          for (_Element_ e : setEvent.getRemovedElements()) {
            sourceElementRemoved(e, removedFilteredElements);
          }
        }
        catch (ClassCastException ccExc) {
          // NOP
        }
      }

      private void handleElementCriterionEvent(Event event, HashSet<_Element_> addedFilteredElements, HashSet<_Element_> removedFilteredElements) {
        assert event != null;
        try {
          BooleanEvent criterionEvent = (BooleanEvent)event;
          ElementCriterion ec = (ElementCriterion)criterionEvent.getSource();
          _Element_ element = ec.getElement();
          if (criterionEvent.getNewValue()) {
            $filteredSet.add(element);
            addedFilteredElements.add(element);
          }
          else {
            $filteredSet.remove(element);
            removedFilteredElements.add(element);
          }
        }
        catch (ClassCastException ccExc) {
          // NOP
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



  /*<property name="criterion">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final PathFactory<_Element_, BooleanBeed> getCriterion() {
    return $criterion;
  }

  /**
   * BooleanBeeds are PropertyBeeds and need an owner.
   * We don't wat that for ElementCriterion in the real
   * semantic graph, so whe use this stub.
   */
  private final static AbstractAggregateBeed ELEMENT_CRITERION_OWNER = new AbstractAggregateBeed() {
    // NOP
  };

  /**
   * Fabricated class. This {@link BooleanBeed} uses a
   * {@link Path} to a true {@link BooleanBeed} for an
   * element (created by {@link #getCriterion()}).
   * If the result of the path is {@code true}, this {@link BooleanBeed}
   * returns {@code true}.
   * If the result of the path is {@code false}, this {@link BooleanBeed}
   * returns {@code false}.
   * If the result of the path is {@code null} (either the result {@link BooleanBeed}
   * is not effective, or its {@link BooleanBeed#getBoolean()} method returns
   * {@code null}), this {@link BooleanBeed} returns {@code false}.
   * This {@link BooleanBeed} never returns {@code null}.
   * The value of this {@link BooleanBeed} can change if the resulting
   * {@link BooleanBeed}'s value changes, or the result of the give path
   * changes.
   *
   * @invar getElement() != null;
   */
  private class ElementCriterion
      extends AbstractPropertyBeed<BooleanEvent>
      implements BooleanBeed {

    /**
     * @pre element != null;
     */
    public ElementCriterion(_Element_ element) {
      super(ELEMENT_CRITERION_OWNER);
      assert element != null;
      $element = element;
      $bbPath = getCriterion().createPath(element);
      assert $bbPath != null;
      $dependent.addUpdateSource($bbPath);
      $bb = $bbPath.get();
      if ($bb != null) {
        $dependent.addUpdateSource($bb);
      }
      $value = ($bb == null) ? false : $bb.getboolean();
    }

    private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected BooleanEvent filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
        // events are from the $bbPath or the $bb
        assert events != null;
        assert events.size() > 0;
        boolean oldValue = getboolean();
        PathEvent<BooleanBeed> pathEvent = (PathEvent<BooleanBeed>)events.get($bbPath);
        if (pathEvent != null) {
          if ($bb != null) {
            $dependent.removeUpdateSource($bb);
          }
          $bb = pathEvent.getNewValue();
          if ($bb != null) {
            $dependent.addUpdateSource($bb);
          }
        }
        $value = $bb == null ? false : $bb.getboolean();
        if (oldValue != $value) {
          return new BooleanEvent(ElementCriterion.this, oldValue, $value, edit);
        }
        else {
          return null;
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


    /**
     * The element we are the criterion for.
     *
     */
    public final _Element_ getElement() {
      return $element;
    }

    /**
     * The element we are the criterion for.
     *
     * @invar element != null;
     */
    private _Element_ $element;

    /**
     * @invar $bbPath != null;
     */
    private final Path<BooleanBeed> $bbPath;

    /**
     * @invar $bb = $bbPath.get();
     */
    private BooleanBeed $bb;

    private boolean $value;

    public Boolean getBoolean() {
      return $value;
    }

    public boolean getboolean() {
      return $value;
    }

    public boolean isEffective() {
      return true;
    }

    public Boolean get() {
      return $value;
    }

    public Set<? extends UpdateSource> getUpdateSources() {
      return $dependent.getUpdateSources();
    }

    public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
      /* fixed to make it possible to use this method during construction,
       * before $dependent is initialized. But that is bad code, and should be
       * fixed.
       */
      return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
    }

  }

  private PathFactory<_Element_, BooleanBeed> $criterion;

  /*</property>*/



  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends SetBeed<_Element_, ?>> getSourcePath() {
    return $sourcePath;
  }

  /**
   * @return getSourcePath() == null ? null : getSourcePath().get();
   */
  public final SetBeed<_Element_, ?> getSource() {
    return $source;
  }

  public final void setSourcePath(Path<? extends SetBeed<_Element_, ?>> sourcePath) {
    if ($sourcePath != null) {
      $dependent.removeUpdateSource($sourcePath);
    }
    $sourcePath = sourcePath;
    if ($sourcePath != null) {
      $dependent.addUpdateSource($sourcePath);
      setSource($sourcePath.get());
    }
    else {
      setSource(null);
    }
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
  private final void setSource(SetBeed<_Element_, ?> source) {
    HashSet<_Element_> addedElements = new HashSet<_Element_>();
    HashSet<_Element_> removedElements = new HashSet<_Element_>();
    setSource(source, addedElements, removedElements);
    ActualSetEvent<_Element_> event = createEvent(addedElements, removedElements);
    if (event != null) {
      updateDependents(event);
    }
  }

  private final void setSource(SetBeed<_Element_, ?> source, HashSet<_Element_> addedElements, HashSet<_Element_> removedElements) {
    if ($source != null) {
      for (_Element_ beed : $source.get()) {
        sourceElementRemoved(beed, removedElements);
      }
      $dependent.removeUpdateSource($source);
    }
    // set the source
    $source = source;
    if ($source != null) {
      $dependent.addUpdateSource($source);
      for (_Element_ beed : $source.get()) {
        sourceElementAdded(beed, addedElements);
      }
    }
  }

  private ActualSetEvent<_Element_> createEvent(HashSet<_Element_> addedElements, HashSet<_Element_> removedElements) {
    ActualSetEvent<_Element_> event;
    @SuppressWarnings("unchecked")
    Set<_Element_> addedClone = (Set<_Element_>)addedElements.clone();
    addedElements.removeAll(removedElements);
    removedElements.removeAll(addedClone);
    if ((! removedElements.isEmpty()) || (! addedElements.isEmpty())) {
      event = new ActualSetEvent<_Element_>(NewFilteredSetBeed.this, addedElements, removedElements, null);
    }
    else {
      event = null;
    }
    return event;
  }

  private Path<? extends SetBeed<_Element_, ?>> $sourcePath;

  private SetBeed<_Element_, ?> $source;

  /*</property>*/


  /**
   * @pre element != null;
   * @pre ! $elementCriteria.containsKey(element);
   */
  private void sourceElementAdded(_Element_ element, Set<_Element_> addedFilteredElements) {
    assert element != null;
    assert ! $elementCriteria.containsKey(element);
    ElementCriterion ec = new ElementCriterion(element);
    $elementCriteria.put(element, ec);
    $dependent.addUpdateSource(ec);
    if (ec.getboolean()) {
      $filteredSet.add(element);
      addedFilteredElements.add(element);
    }
  }

  /**
   * @pre element != null;
   * @pre $elementCriteria.containsKey(element);
   */
  private void sourceElementRemoved(_Element_ element, Set<_Element_> removedFilteredElements) {
    assert element != null;
    assert $elementCriteria.containsKey(element);
    boolean removed = $filteredSet.remove(element);
    ElementCriterion ec = $elementCriteria.get(element);
    assert ec != null;
    $dependent.removeUpdateSource(ec);
    if (removed) {
      removedFilteredElements.add(element);
    }
  }

  private Map<_Element_, ElementCriterion> $elementCriteria = new HashMap<_Element_, ElementCriterion>();


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

