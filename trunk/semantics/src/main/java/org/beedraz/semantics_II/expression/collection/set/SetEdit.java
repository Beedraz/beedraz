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


import static org.beedraz.semantics_II.Edit.State.DONE;
import static org.beedraz.semantics_II.Edit.State.NOT_YET_PERFORMED;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppeew.collection_I.CollectionUtil.intersection;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.AbstractSimpleEdit;
import org.beedraz.semantics_II.EditStateException;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <h3>Reversibility</h3>
 * <p>After {@link #perform()}, {@link #getElementsToAdd()} and
 *   {@link #getElementsToRemove()} are changed. Elements that
 *   already where in {@link #getTarget()} initially are removed from
 *   {@link #getElementsToAdd()}, and elements that where not
 *   in {@link #getTarget()} initially are removed from
 *   {@link #getElementsToRemove()}. This is done because, in set
 *   algebra, with {@code 'S = (S &cup; A) \ R}, {@code S &ne; ('S &cup; R) \ A}.</p>
 * <p>Define {@code 'S} as</p>
 * <pre>
 * 'S = S &cup; A = {x | x &isin; S || x &isin; A}
 * 'S \ A = {x | x &isin; S || x &isin; A} \ A
 *        = {x | (x &isin; S || x &isin; A) &amp; x &notin; A}
 *        = {x | (x &isin; S &amp; x &notin; A) || (x &isin; A &amp; x &notin; A)}
 *        = {x | (x &isin; S &amp; x &notin; A) || false}
 *        = {x | x &isin; S &amp; x &notin; A}
 *        = S \ A
 *        &ne; S
 * </pre>
 * <p>{@code 'S \ A = S \ A = S}, only if {@code S &cap; A = &empty;}.</p>
 * <p>Or, define {@code 'S} as</p>
 * <pre>
 * 'S = S \ R = {x | x &isin; S &amp; x &notin; R}
 * 'S &cup; R = {x | x &isin; S &amp; x &notin; R} &cup; R
 *        = {x | (x &isin; S &amp; x &notin; R) || x &isin; R}
 *        = {x | (x &isin; S || x &isin; R) &amp; (x &notin; R || x &isin; R)}
 *        = {x | (x &isin; S || x &isin; R) &amp; true}
 *        = {x | x &isin; S || x &isin; R}
 *        = S &cup; R
 *        &ne; S
 * </pre>
 * <p>{@code 'S &cup; R = S &cup; R = S}, only if {@code R &sube; S}.</p>
 * <p>Given that {@code S &cap; A = &empty;} and that {@code R &sube; S},
 *   we can assume that the current state of {@link #getTarget()} is the same
 *   as the goal state if {@code A &sube; 'S} and {@code R &cap; 'S = &empty;}.
 *   {@link #undo()} ({@code ''S = ('S &cup; R) \ A}) will not add elements ({@code R})
 *   that where not in {@code S} initially, and will not remove elements ({@code A})
 *   that where in {@code S} initially. We can also assume that the current state
 *   of {@link #getTarget()} is the same as the initial state if
 *   {@code A &cap; ''S = &empty;} and {@code R &sube; ''S}. {@link #redo()}
 *   ({@code 'S = (''S &cup; A) \ R}) will not add elements ({@code A}) that where
 *   not in {@code 'S} after performance, and will not remove elements ({@code R}) that
 *   where in {@code 'S} after performance.</p>
 * <p>Furthermore, {@code A} and {@code R} are disjunct if {@code S &cap; A = &empty;}
 *   and {@code R &sube; S}, which is trivial: if {@code A} doesn't contain any elements
 *   of {@code S}, it surely doesn't contain any elements of a subset of {@code S}.</p>
 *
 * @author Jan Dockx
 *
 * @invar  getElementsToAdd() != null;
 * @invar  getElementsToRemove() != null;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class SetEdit<_Element_>
    extends AbstractSimpleEdit<EditableSetBeed<_Element_>, SetEvent<_Element_>> {

  /**
   * @pre  target != null;
   * @post getTarget() == target;
   */
  public SetEdit(EditableSetBeed<_Element_> target) {
    super(target);
  }

  /**
   * @basic
   */
  public final Set<_Element_> getElementsToAdd() {
    return Collections.unmodifiableSet($elementsToAdd);
  }

  public final void addElementToAdd(_Element_ element) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToAdd.add(element);
    recalculateValidity();
  }

  public final void removeElementToAdd(_Element_ element) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToAdd.remove(element);
    recalculateValidity();
  }

  private final Set<_Element_> $elementsToAdd = new HashSet<_Element_>();

  /**
   * @basic
   */
  public final Set<_Element_> getElementsToRemove() {
    return Collections.unmodifiableSet($elementsToRemove);
  }

  public final void addElementToRemove(_Element_ element) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToRemove.add(element);
    recalculateValidity();
  }

  public final void removeElementToRemove(_Element_ element) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToRemove.remove(element);
    recalculateValidity();
  }

  private final Set<_Element_> $elementsToRemove = new HashSet<_Element_>();

  /**
   * <p>{@code S &cap; 'A = &empty;} by A' = A \ S</p>
   * <p>{@code 'R &sube; S} by </p>
   */
  @Override
  protected final void storeInitialState() {
    $elementsToAdd.removeAll(getTarget().get());
    $elementsToRemove.retainAll(getTarget().get());
  }

  @Override
  public final boolean isChange() {
    return ! ($elementsToAdd.isEmpty() && $elementsToRemove.isEmpty());
  }

  /**
   * Returns true when the following two conditions are true:
   * - none of the elements we want to add is already present in the target
   * - all targets we want to remove are present in the beed
   * @return {@code A &cap; ''S = &empty;} and {@code R &sube; ''S}
   */
  @Override
  protected final boolean isInitialStateCurrent() {
    return intersection(getTarget().get(), $elementsToAdd).isEmpty() &&
           getTarget().get().containsAll($elementsToRemove);
  }

  /**
   * @mudo Ontbreekt er geen preconditie bij de performance methode in AbstractEdit
   *       die zegt dat storeInitialState moet uitgevoerd zijn?
   * @pre  getTarget().get().contains(getElementsToRemove());
   * @post
   */
  @Override
  protected void performance() {
    assert getTarget().get().containsAll(getElementsToRemove());
    // order irrelevant because sets are disjunct
    getTarget().addElements($elementsToAdd);
    getTarget().removeElements($elementsToRemove);
  }

  /**
   * @return {@code A &sube; 'S} and {@code R &cap; 'S = &empty;}
   */
  @Override
  protected final boolean isGoalStateCurrent() {
    return getTarget().get().containsAll($elementsToAdd) &&
           intersection(getTarget().get(), $elementsToRemove).isEmpty();
  }

  /**
   * @mudo Ontbreekt er geen preconditie bij de unperformace methode in AbstractEdit?
   * @pre  getTarget().get().contains(getElementsToAdd());
   * @post
   */
  @Override
  protected void unperformance() {
    assert getTarget().get().containsAll(getElementsToAdd());
    // order irrelevant because sets are disjunct
    getTarget().removeElements($elementsToAdd);
    getTarget().addElements($elementsToRemove);
  }

  /**
   * @post  result.size() == 1;
   * @post  result.get(getTarget()) = event;
   * @post  result.get(getTarget()).getSource() == getTarget();
   * @post  result.get(getTarget()).getAddedElements().equals(getAddedElements());
   * @post  result.get(getTarget()).getRemovedElements().equals(getRemovedElements());
   * @post  result.get(getTarget()).getEdit() == this;
   */
  @Override
  protected Map<AbstractBeed<?>, SetEvent<_Element_>> createEvents() {
    return singletonEventMap(new ActualSetEvent<_Element_>(getTarget(),
                                                           getAddedElements(),
                                                           getRemovedElements(),
                                                           this));
  }

  /**
   * Returns the set of elements that are added during the last
   * action, i.e. the last perform, undo or redo.
   * E.g. during a perform, the set {@link #getAddedElements()}
   * was added, during an undo, the set {@link #getRemovedElements()}
   * was added.
   */
  public Set<_Element_> getAddedElements() {
    return (getState() == DONE) ? $elementsToAdd : $elementsToRemove;
  }

  /**
   * Returns the set of elements that are removed during the last
   * action, i.e. the last perform, undo or redo.
   * E.g. during a perform, the set {@link #getRemovedElements()}
   * was removed, during an undo, the set {@link #getAddedElements()}
   * was removed.
   */
  public Set<_Element_> getRemovedElements() {
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

}

