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

package org.beedraz.semantics_II.expression.collection.map;


import static org.beedraz.semantics_II.Edit.State.DONE;
import static org.beedraz.semantics_II.Edit.State.NOT_YET_PERFORMED;
import static org.ppeew.collection_I.CollectionUtil.intersection;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.AbstractEdit;
import org.beedraz.semantics_II.EditStateException;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * This edit can be used to add and remove elements from a {@link MapBeed}.
 *
 * When you want to replace the value of a key-value pair by a new value, you
 * should remove the old key-value pair first and then add the new key-value pair.
 * The reason why we do not allow a replace is that otherwise the result of
 * this edit depends on the order of the add-remove-replace operations:
 * when we allow a key-value in {@link #getElementsToAdd()}
 * whose key is already in the target, then this key could also be in
 * {@link #getElementsToRemove()} and the result of the edit depends on
 * the order in which the elements are added and removed. So, we demand
 * that the keys that are added are NOT in the target and that the keys
 * that are removed are in the target. In this way, {@link #getElementsToAdd()}
 * and {@link #getElementsToRemove()} are disjunct and it does not matter
 * which operation (add - remove) we do first.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar   getElementsToAdd() != null;
 * @invar   getElementsToRemove() != null;
 *
 * @mudo move to collection package; map is not a set
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State: Exp $",
         tag      = "$Name:  $")
public class MapEdit<_Key_, _Value_>
    extends AbstractEdit<EditableMapBeed<_Key_, _Value_>, MapEvent<_Key_, _Value_>> {

  /**
   * @pre  target != null;
   * @post getTarget() == target;
   */
  public MapEdit(EditableMapBeed<_Key_, _Value_> target) {
    super(target);
  }


  /*<property name="elementsToAdd">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final Map<_Key_, _Value_> getElementsToAdd() {
    return Collections.unmodifiableMap($elementsToAdd);
  }

  public final void addElementToAdd(_Key_ key, _Value_ value) throws EditStateException {
    if (getState() != State.NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToAdd.put(key, value);
    recalculateValidity();
  }

  public final void removeElementToAdd(_Key_ key) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToAdd.remove(key);
    recalculateValidity();
  }

  private final Map<_Key_, _Value_> $elementsToAdd = new HashMap<_Key_, _Value_>();

  /*</property>*/


  /*<property name="elementsToRemove">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final Map<_Key_, _Value_> getElementsToRemove() {
    return Collections.unmodifiableMap($elementsToRemove);
  }

  public final void addElementToRemove(_Key_ key, _Value_ value) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToRemove.put(key, value);
    recalculateValidity();
  }

  public final void removeElementToRemove(_Key_ key) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToRemove.remove(key);
    recalculateValidity();
  }

  /**
   * The elements to remove should be in a map, containing both key and value, to
   * make undo possible.
   */
  private final Map<_Key_, _Value_> $elementsToRemove = new HashMap<_Key_, _Value_>();

  /*</property>*/


  /**
   * Remove all keys from {@link #getElementsToAdd()} that are already in the
   * target.
   * Remove all keys from {@link #getElementsToRemove()} that are not in the
   * target.
   */
  @Override
  protected final void storeInitialState() {
    Set<_Key_> keysToAdd = new HashSet<_Key_>($elementsToAdd.keySet());
    for (_Key_ key : keysToAdd) {
      if (getTarget().keySet().contains(key)) {
        $elementsToAdd.remove(key);
      }
    }
    Set<_Key_> keysToRemove = new HashSet<_Key_>($elementsToRemove.keySet());
    for (_Key_ key : keysToRemove) {
      if (!getTarget().keySet().contains(key)) {
        $elementsToRemove.remove(key);
      }
    }
  }

  @Override
  public final boolean isChange() {
    return ! ($elementsToAdd.isEmpty() && $elementsToRemove.isEmpty());
  }

  /**
   * Returns true when the following two conditions are true:
   * - none of the keys we want to add is already present in the target
   * - all keys we want to remove are present in the target
   */
  @Override
  protected final boolean isInitialStateCurrent() {
    return intersection(getTarget().keySet(), $elementsToAdd.keySet()).isEmpty() &&
           getTarget().keySet().containsAll($elementsToRemove.keySet());
  }

  /**
   * @mudo Ontbreekt er geen preconditie bij de performance methode in AbstractEdit
   *       die zegt dat storeInitialState moet uitgevoerd zijn?
   * @pre  isInitialStateCurrent();
   * @post
   */
  @Override
  protected void performance() {
    assert isInitialStateCurrent();
    // order irrelevant because key sets are disjunct
    for (_Key_ key : $elementsToAdd.keySet()) {
      getTarget().put(key, $elementsToAdd.get(key));
    }
    for (_Key_ key : $elementsToRemove.keySet()) {
      getTarget().remove(key);
    }
  }

  /**
   * Returns true when the following two conditions are true:
   * - the keys in {@link #getElementsToAdd()} are all present in the target
   * - none of the keys in {@link #getElementsToRemove()} is present in the target
   */
  @Override
  protected final boolean isGoalStateCurrent() {
    return getTarget().keySet().containsAll($elementsToAdd.keySet()) &&
           intersection(getTarget().keySet(), $elementsToRemove.keySet()).isEmpty();
  }

  /**
   * @mudo Ontbreekt er geen preconditie bij de unperformace methode in AbstractEdit?
   * @pre  isGoalStateCurrent();
   * @post
   */
  @Override
  protected void unperformance() {
    assert isGoalStateCurrent();
    // order irrelevant because key sets are disjunct
    for (_Key_ key : $elementsToAdd.keySet()) {
      getTarget().remove(key);
    }
    for (_Key_ key : $elementsToRemove.keySet()) {
      getTarget().put(key, $elementsToRemove.get(key));
    }
  }

  @Override
  protected final void updateDependents(MapEvent<_Key_, _Value_> event) {
    getTarget().packageUpdateDependents(event);
  }

  /**
   * @post  result.getSource() == getTarget();
   * @post  result.getAddedElements().equals(getAddedElements());
   * @post  result.getRemovedElements().equals(getRemovedElements());
   * @post  result.getEdit() == this;
   */
  @Override
  protected MapEvent<_Key_, _Value_> createEvent() {
    return
      new ActualMapEvent<_Key_, _Value_>(
        getTarget(),
        getAddedElements(),
        getRemovedElements(),
        this);
  }

  /**
   * Returns the map of elements that are added during the last
   * action, i.e. the last perform, undo or redo.
   * E.g. during a perform, the map {@link #getAddedElements()}
   * was added, during an undo, the map {@link #getRemovedElements()}
   * was added.
   */
  public Map<_Key_, _Value_> getAddedElements() {
    return (getState() == DONE) ? $elementsToAdd : $elementsToRemove;
  }

  /**
   * Returns the map of elements that are removed during the last
   * action, i.e. the last perform, undo or redo.
   * E.g. during a perform, the map {@link #getRemovedElements()}
   * was removed, during an undo, the map {@link #getAddedElements()}
   * was removed.
   */
  public Map<_Key_, _Value_> getRemovedElements() {
    return (getState() == DONE) ? $elementsToRemove : $elementsToAdd;
  }

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", elements to add: " + getElementsToAdd() +
           ", elements to remove: " + getElementsToRemove();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    toStringGoalInitial(sb, level + 1);
  }

  protected void toStringGoalInitial(StringBuffer sb, int level) {
    sb.append(indent(level + 1) + "elements to add: " + getElementsToAdd() + "\n");
    sb.append(indent(level + 1) + "elements to remove: " + getElementsToRemove() + "\n");
  }

  @Override
  protected final boolean isAcceptable() {
    return getTarget().isAcceptable($elementsToAdd, $elementsToRemove);
  }

  @Override
  public final void kill() {
    localKill();
  }

  /*</property>*/

  @Override
  protected final void markPerformed() {
    localMarkPerformed();
  }

}

