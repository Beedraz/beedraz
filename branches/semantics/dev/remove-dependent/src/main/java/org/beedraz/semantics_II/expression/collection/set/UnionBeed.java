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
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.indent;

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
import org.ppwcode.util.smallfries_I.ComparisonUtil;


/**
 * A {@link SetBeed} that returns the union of a given {@link SetBeed} containing
 * beeds of type {@link SetBeed}.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getSource() == null ==> get().isEmpty();
 * @invar  getSource() != null ==>
 *           get() == union {setBeed : getSource().get().contains(setBeed)};
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class UnionBeed<_Element_>
    extends AbstractDependentSetBeed<_Element_, SetEvent<_Element_>> {

  /**
   * @post  getSource() == null;
   * @post  get().isEmpty();
   */
  public UnionBeed() {
    this(null);
  }

  /**
   * @post  getSource() == null;
   * @post  get().isEmpty();
   * @post  owner != null ? owner.regsiterAggregateElement(this);
   */
  public UnionBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected SetEvent<_Element_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    // if the source changes (elements added and / or removed
    if (events.keySet().contains($source)) {
      @SuppressWarnings("unchecked")
      SetEvent<SetBeed<_Element_, SetEvent<_Element_>>> setEvent =
        (SetEvent<SetBeed<_Element_, SetEvent<_Element_>>>)events.get($source);
      sourceChanged(setEvent);
    }
    // recalculate and notify the listeners if the value has changed
    return recalculateEvent(edit);
  }

  /**
   * @post    All {@link SetBeed set beeds} that are added to the source
   *          by the given event become update sources.
   *          (The reason is that this union beed should be notified
   *          (and then recalculate) when one of the element {@link SetBeed set beeds}
   *          changes.)
   * @post    All {@link SetBeed set beeds} that are removed from the source
   *          by the given event are no longer update sources.
   * @post    get() is recalculated.
   */
  private void sourceChanged(SetEvent<SetBeed<_Element_, SetEvent<_Element_>>> event) {
    /* All beeds that are added to the source by the given event
     * become update sources
     */
    Set<SetBeed<_Element_, SetEvent<_Element_>>> added = event.getAddedElements();
    for (SetBeed<_Element_, SetEvent<_Element_>> setBeed : added) {
      addUpdateSource(setBeed);
    }
    /* All beeds that are removed from the source by the given event
     * stop being update sources
     */
    Set<SetBeed<_Element_, SetEvent<_Element_>>> removed = event.getRemovedElements();
    for (SetBeed<_Element_, SetEvent<_Element_>> setBeed : removed) {
      removeUpdateSource(setBeed);
    }
  }



  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<? extends SetBeed<_Element_, ?>, ?> getSource() {
    return $source;
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    get() == the union of the sets in the given source
   * @post    The UnionBeed is registered as a listener of the given SetBeed.
   * @post    The UnionBeed is registered as a listener of all beeds in
   *          the given source. (The reason is that the UnionBeed should be
   *          notified (and then recalculate) when one of the beeds in the source
   *          changes.)
   * @post    The listeners of this beed are notified when the value changes.
   * @post    The listeners of the size beed are notified when the size of this
   *          set has changed.
   */
  public final void setSource(SetBeed<? extends SetBeed<_Element_, ?>, ?> source) {
    if ($source != null) {
      for (SetBeed<_Element_, ?> setBeed : $source.get()) {
        removeUpdateSource(setBeed);
      }
      removeUpdateSource($source);
    }
    // set the source
    $source = source;
    if ($source != null) {
      addUpdateSource($source);
      for (SetBeed<_Element_, ?> setBeed : $source.get()) {
        addUpdateSource(setBeed);
      }
    }
    // recalculate and notify the listeners if the value has changed
    ActualSetEvent<_Element_> event = recalculateEvent(null);
    if (event != null) {
      updateDependents(event);
    }
  }

  private SetBeed<? extends SetBeed<_Element_, ?>, ?> $source;

  /*</property>*/



  /**
   * The union of the elements in {@link #getSource()}.
   *
   * @invar  $union != null;
   */
  private HashSet<_Element_> $union = new HashSet<_Element_>();


  /**
   * @basic
   */
  public final Set<_Element_> get() {
    return Collections.unmodifiableSet($union);
  }


  /**
   * The value of $union is recalculated.
   * This is done by iterating over the set beeds in the source.
   * When the source is null, $union is an empty set.
   * When the source contains zero beeds, $union is an empty set.
   * Otherwise, $union contains all elements in the given set beeds.
   */
  public void recalculate() {
    $union.clear();
    if (getSource() != null) {
      for (SetBeed<_Element_, ?> element : getSource().get()) {
        $union.addAll(element.get());
      }
    }
  }

  private ActualSetEvent<_Element_> recalculateEvent(Edit<?> edit) {
    @SuppressWarnings("unchecked")
    Set<_Element_> oldValue = (Set<_Element_>)$union.clone();
    recalculate();
    if (! ComparisonUtil.equalsWithNull(oldValue, $union)) {
      Set<_Element_> addedElements = new HashSet<_Element_>($union);
      addedElements.removeAll(oldValue);
      Set<_Element_> removedElements = new HashSet<_Element_>(oldValue);
      removedElements.removeAll($union);
      return new ActualSetEvent<_Element_>(UnionBeed.this, addedElements,  removedElements, edit);
    }
    else {
      return null;
    }
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

