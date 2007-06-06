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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashMap;
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
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEvent;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.beedraz.semantics_II.path.PathFactory;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A {@link SetBeed} that returns a mapped set of a given {@link SetBeed}.
 * Mapping is done with a {@link Path} which is created for each element
 * in the {@link #getSource() source set beed} by a {@link PathFactory}
 * (called {@link #getMapping()} mapping) given at construction.
 *
 * If the mapping returns {@code null} for an element of the source,
 * it is not made part of the result. If several elements of the
 * source map to the same object, it is only once in the result (this
 * is a set). This means that the cardinality of this set can be
 * smaller than the cardinality of the source.
 *
 * @author  Jan Dockx
 * @author  Peopleware n.v.
 *
 * @invar  getMapping() != null;
 * @invar  getMapping() == 'getMapping();
 * @invar  getSourcePath() != null
 *           ? getSource() == getSourcePath().get()
 *           : getSource() == null;
 * @invar  getSource() == null ? get() == null;
 * @invar  getSource() != null ? get() != null;
 * @invar  getSource() != null ?
 *           for (_From_ from : getSource()) {
 *             get().contains(getMapping().createPath(from).get())
 *           };
 * @invar  getSource() != null ?
 *           for (_To_ to : get()) {
 *             exists(_From_ from : getSource()) {getMapping.createPath(from).get() == to}
 *           };
 *           ? get() == {element : getSource().get().contains(element) &&
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class MappedSetBeed<_From_ extends Beed<?>, _To_ extends Beed<?>>
    extends AbstractSetBeed<_To_, SetEvent<_To_>> {

  /**
   * @pre   mapping != null;
   * @post  getMapping() == mapping;
   * @post  getSource() == null;
   * @post  getSourcePath() == null;
   * @post  get() == null;
   */
  public MappedSetBeed(PathFactory<_From_, _To_> mapping) {
    this(mapping, null);
  }

  /**
   * @pre   mapping != null;
   * @post  getMapping() == mapping;
   * @post  getSource() == null;
   * @post  getSourcePath() == null;
   * @post  get() == null;
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  public MappedSetBeed(PathFactory<_From_, _To_> mapping, AggregateBeed owner) {
    super(owner);
    $mapping = mapping;
  }

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected SetEvent<_To_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
        assert events != null;
        assert events.size() > 0;
        /* Events can come:
         * - from the source path,
         * - from the source (elements added or removed) and
         * - from the mappings (paths) for each element
         * If an event comes from the source path, we might also get
         * events from the source or even elements. But these don't
         * matter, since we have a new source, and all elements have
         * been removed, and new added.
         * If an event comes from the source, about added or removed elements,
         * we might also get events from elements. Events from elements that
         * are just removed should not be dealt with.
         */
        HashSet<_To_> addedFilteredElements = new HashSet<_To_>();
        HashSet<_To_> removedFilteredElements = new HashSet<_To_>();
        PathEvent<SetBeed<_From_, ?>> pathEvent = (PathEvent<SetBeed<_From_, ?>>)events.get($sourcePath);
        if (pathEvent != null) {
          handleSourcePathEvent(pathEvent, addedFilteredElements, removedFilteredElements);
          // if there is a path event, don't deal with other events
        }
        else {
          @SuppressWarnings("unchecked")
          SetEvent<_From_> setEvent = (SetEvent<_From_>)events.get($source);
          if (setEvent != null) {
            handleSourceEvent(setEvent, addedFilteredElements, removedFilteredElements);
          }
          for (Event event : events.values()) {
            handleElementMappingEvent(event, addedFilteredElements, removedFilteredElements);
          }
        }
        return createEvent(addedFilteredElements, removedFilteredElements, edit);
      }

      /**
       * If the given event is of type {@link PathEvent}, then it is caused by
       * the {@link MappedSetBeed#getSourcePath()}. In this case, we
       * replace the old source by the new value in the given event.
       * The elements that are added by this operation are gathered in
       * <code>addedFilteredElements</code>.
       * The elements that are removed by this operation are gathered in
       * <code>removedFilteredElements</code>.
       *
       * @pre  pathEvent != null;
       * @pre  addedFilteredElements != null;
       * @pre  removedFilteredElements != null;
       */
      private void handleSourcePathEvent(PathEvent<SetBeed<_From_, ?>> pathEvent,
                                         HashSet<_To_> addedFilteredElements,
                                         HashSet<_To_> removedFilteredElements) {
        assert pathEvent != null;
        assert addedFilteredElements != null;
        assert removedFilteredElements != null;
        assert pathEvent.getSource() == $sourcePath;
        SetBeed<_From_, ?> newSource = pathEvent.getNewValue();
        setSource(newSource, addedFilteredElements, removedFilteredElements);
      }

      /**
       * If the given event is of type {@link SetEvent}, then it is caused by
       * the {@link MappedSetBeed#getSource()}.
       * In this case, we remove the elements in {@link SetEvent#getRemovedElements()}
       * and we add the elements in {@link SetEvent#getAddedElements()}.
       * The elements that are added to {@link MappedSetBeed#$filteredSet}
       * by this operation are gathered in <code>addedFilteredElements</code>.
       * The elements that are removed from {@link MappedSetBeed#$filteredSet}
       * by this operation are gathered in <code>removedFilteredElements</code>.
       *
       * @pre  event != null;
       * @pre  addedFilteredElements != null;
       * @pre  removedFilteredElements != null;
       */
      private void handleSourceEvent(SetEvent<_From_> setEvent,
                                     HashSet<_To_> addedFilteredElements,
                                     HashSet<_To_> removedFilteredElements) {
        assert setEvent != null;
        assert addedFilteredElements != null;
        assert removedFilteredElements != null;
        assert setEvent.getSource() == $source;
        for (_From_ e : setEvent.getAddedElements()) {
          sourceElementAdded(e, addedFilteredElements);
        }
        for (_From_ e : setEvent.getRemovedElements()) {
          sourceElementRemoved(e, removedFilteredElements);
        }
      }

      /**
       * If the given event is of type {@link BooleanEvent}, then it is caused by
       * one of the {@link ElementCriterion filter criteria}.
       * Since a {@link ElementCriterion filter criterion} is a {@link BooleanBeed},
       * and since it can only be true or false (not null), this event is sent when
       * the value of the {@link ElementCriterion filter criterion} changed from true
       * to false, or from false to true.
       * When the {@link BooleanEvent#getNewValue() new value} of the event is true,
       * we add the element of the {@link ElementCriterion filter criterion} to
       * {@link MappedSetBeed#$filteredSet} and to <code>addedFilteredElements</code>.
       * When the {@link BooleanEvent#getNewValue() new value} of the event is false,
       * we remove the element of the {@link ElementCriterion filter criterion} from
       * {@link MappedSetBeed#$filteredSet} and add it to
       * <code>removedFilteredElements</code>.
       * We don't handle events of sources that are already in {@code removedFilteredElements}.
       *
       * @pre  event != null;
       * @pre  addedFilteredElements != null;
       * @pre  removedFilteredElements != null;
       */
      private void handleElementMappingEvent(Event event,
                                             HashSet<_To_> addedResultingElements,
                                             HashSet<_To_> removedResultingElements) {
        assert event != null;
        assert addedResultingElements != null;
        assert removedResultingElements != null;
        try {
          PathEvent<_To_> mappingEvent = (PathEvent<_To_>)event;
          if ($elementMappings.containsValue(event.getSource())) {
            _To_ oldElement = mappingEvent.getOldValue();
            removeResultingElement(oldElement, removedResultingElements);
            _To_ newElement = mappingEvent.getNewValue();
            addResultingElement(newElement, addedResultingElements);
          }
          /* otherwise, it means the from element of this mapping was removed from the
           * source a moment ago, and we don't need to deal with this event anymore
           */
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
  public final PathFactory<_From_, _To_> getMapping() {
    return $mapping;
  }

  private PathFactory<_From_, _To_> $mapping;

  /*</property>*/



  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends SetBeed<_From_, ?>> getSourcePath() {
    return $sourcePath;
  }

  /**
   * @return getSourcePath() == null ? null : getSourcePath().get();
   */
  public final SetBeed<_From_, ?> getSource() {
    return $source;
  }

  /**
   * The old source path is removed as update source.
   * The new source path is added as update source.
   * The source is replaced by the new source: see {@link #setSource(SetBeed)}.
   */
  public final void setSourcePath(Path<? extends SetBeed<_From_, ?>> sourcePath) {
    if ($sourcePath instanceof AbstractDependentPath) {
      $dependent.removeUpdateSource($sourcePath);
    }
    $sourcePath = sourcePath;
    if ($sourcePath instanceof AbstractDependentPath) {
      $dependent.addUpdateSource($sourcePath);
    }
    if ($sourcePath != null) {
      setSource($sourcePath.get());
    }
    else {
      setSource(null);
    }
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    get() == the result of applying this operation on the given source
   * @post    The {@link MappedSetBeed} is registered as a dependent of the given SetBeed.
   * @post    The {@link MappedSetBeed} is registered as a dependent of paths created
   *          with {@link #getMapping()} for all beeds in the given source.
   *          (The reason is that this instance should be notified (and then recalculate) when
   *          one of the beeds in the source changes.)
   * @post    The dependents and listeners of this beed are notified when the value changes.
   */
  private final void setSource(SetBeed<_From_, ?> source) {
    HashSet<_To_> addedResultingElements = new HashSet<_To_>();
    HashSet<_To_> removedResultingElements = new HashSet<_To_>();
    setSource(source, addedResultingElements, removedResultingElements);
    ActualSetEvent<_To_> event = createEvent(addedResultingElements, removedResultingElements, null);
    if (event != null) {
      updateDependents(event);
    }
  }

  /**
   * Replace the old {@link #getSource() source} by the given source.
   * Remove the old {@link #getSource() source} and all the
   * {@link ElementCriterion filter criteria} of its elements as
   * {@link UpdateSource}.
   * Add the given source and all the {@link ElementCriterion filter criteria}
   * of its elements as {@link UpdateSource}.
   * Update the {@link #$filteredSet}:
   * - remove the elements that were there, and put them in
   *   <code>removedFilteredElements</code>.
   * - add the elements in the given source that satisfy the
   *   {@link ElementCriterion filter criterion} and put them in
   *   <code>addedFilteredElements</code>.
   * Update the {@link #$elementCriteria}:
   * - remove the elements that were there.
   * - add the elements in the given source, with their corresponding
   *   {@link ElementCriterion filter criteria}.
   *
   * @pre  addedResultingElements != null;
   * @pre  removedResultingElements != null;
   */
  private final void setSource(SetBeed<_From_, ?> source,
                               HashSet<_To_> addedResultingElements,
                               HashSet<_To_> removedResultingElements) {
    assert addedResultingElements != null;
    assert removedResultingElements != null;
    if ($source != null) {
      for (_From_ from : $source.get()) {
        sourceElementRemoved(from, removedResultingElements);
      }
      $dependent.removeUpdateSource($source);
    }
    // set the source
    $source = source;
    if ($source != null) {
      $dependent.addUpdateSource($source);
      for (_From_ from : $source.get()) {
        sourceElementAdded(from, addedResultingElements);
      }
    }
  }

  private ActualSetEvent<_To_> createEvent(HashSet<_To_> addedResultingElements,
                                           HashSet<_To_> removedResultingElements,
                                           Edit<?> edit) {
    ActualSetEvent<_To_> event;
    @SuppressWarnings("unchecked")
    Set<_To_> addedClone = (Set<_To_>)addedResultingElements.clone();
    addedResultingElements.removeAll(removedResultingElements);
    removedResultingElements.removeAll(addedClone);
    if ((! removedResultingElements.isEmpty()) || (! addedResultingElements.isEmpty())) {
      event = new ActualSetEvent<_To_>(MappedSetBeed.this,
                                       addedResultingElements,
                                       removedResultingElements,
                                       edit);
    }
    else {
      event = null;
    }
    return event;
  }

  private Path<? extends SetBeed<_From_, ?>> $sourcePath;

  private SetBeed<_From_, ?> $source;

  /*</property>*/


  /**
   * Add the {@link ElementCriterion filter criterion} of the given element as
   * {@link UpdateSource}.
   * Update the {@link #$filteredSet}: add the element if it satisfies
   * the {@link ElementCriterion filter criterion} and put it in
   * <code>addedFilteredElements</code>.
   * Update the {@link #$elementCriteria}: add the given element.
   *
   * @pre  element != null;
   * @pre  ! $elementCriteria.containsKey(element);
   * @pre  addedResultingElements != null;
   */
  private void sourceElementAdded(_From_ from, Set<_To_> addedResultingElements) {
    assert from != null;
    assert addedResultingElements != null;
    assert ! $elementMappings.containsKey(from);
    Path<? extends _To_> elementPath = getMapping().createPath(from);
    $elementMappings.put(from, elementPath);
    if (elementPath instanceof AbstractDependentPath) {
      $dependent.addUpdateSource(elementPath);
    }
    _To_ to = elementPath.get(); // to might be null or already in the result
    addResultingElement(to, addedResultingElements);
  }

  private void addResultingElement(_To_ to, Set<_To_> addedResultingElements) {
    if (to != null) {
      Integer count = $resultCount.get(to);
      if (count == null) {
        $result.add(to);
        $resultCount.put(to, 1);
        addedResultingElements.add(to);
      }
      else {
        $resultCount.put(to, count + 1);
      }
    }
  }

  /**
   * Remove the {@link ElementCriterion filter criterion} of the given element as
   * {@link UpdateSource}.
   * Update the {@link #$filteredSet}: remove the element (if it was there)
   * and then put it in <code>removedFilteredElements</code>.
   * Update the {@link #$elementCriteria}: remove the given element.
   *
   * @pre  from != null;
   * @pre  $elementMappings.containsKey(from);
   * @pre  removedResultingElements != null;
   */
  private void sourceElementRemoved(_From_ from, Set<_To_> removedResultingElements) {
    assert from != null;
    assert $elementMappings.containsKey(from);
    assert removedResultingElements != null;
    Path<? extends _To_> elementPath = $elementMappings.get(from);
    assert elementPath != null;
    $elementMappings.remove(from);
    if (elementPath instanceof AbstractDependentPath) {
      $dependent.removeUpdateSource(elementPath);
    }
    _To_ to = elementPath.get(); // to might be null or in the result for different froms
    if (to != null) {
      removeResultingElement(to, removedResultingElements);
    }
  }

  /**
   * @pre $resultCount.containsKey(to);
   */
  private void removeResultingElement(_To_ to, Set<_To_> removedResultingElements) {
    assert $resultCount.containsKey(to);
    if (to != null) {
      Integer count = $resultCount.get(to);
      assert count != null;
      if (count <= 1) {
        $result.remove(to);
        $resultCount.remove(to);
        removedResultingElements.add(to);
      }
      else {
        $resultCount.put(to, count - 1);
      }
    }
  }

  /**
   * @invar $elementMappings != null;
   * @invar $source != null ? $source.equals($elementMappings.keySet()) : $elementMappings.isEmpty();
   */
  private Map<_From_, Path<? extends _To_>> $elementMappings = new HashMap<_From_, Path<? extends _To_>>();

  /**
   * Since the result of the mapping is actually a bag, and we need
   * to return a set, here we keep the count of each element.
   *
   * @invar for (_To_ to : $result) {($resultCount.get(to) != null) && ($resultCount.get(to) > 0)};
   * @invar for (_To_ to : $resultCount.keySet()) {($result.contains(to)};
   * @invar Collections.noNull($resultCount);
   */
  private Map<_To_, Integer> $resultCount = new HashMap<_To_, Integer>();

  /**
   * The set of resulting elements.
   *
   * @invar $result != null;
   * @invar Collections.noNull($result);
   * @invar for (_To_ to : $result) {exists (Path<? extends _To_> p : $elementMappings.values()) {p.get() == to}};
   * @invar for (Path<? extends _To_> p : $elementMappings.values()) {
   *          p.get() != null ? $result.contains(p.get());
   *        };
   */
  private final HashSet<_To_> $result = new HashSet<_To_>();


  /**
   * @basic
   */
  public final Set<_To_> get() {
//    return $source == null ? null : Collections.unmodifiableSet($result);
    return Collections.unmodifiableSet($result);
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
      sb.append(indent(level + 2) + element.toString() + "\n");
    }
  }

}

