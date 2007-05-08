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


import static org.beedra.util_I.MultiLineToStringUtil.indent;
import static org.beedra.util_I.CollectionUtil.intersection;
import static org.beedra_II.edit.Edit.State.DONE;
import static org.beedra_II.edit.Edit.State.NOT_YET_PERFORMED;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.beedra_II.edit.AbstractEdit;
import org.beedra_II.edit.EditStateException;
import org.toryt.util_I.annotations.vcs.CvsInfo;


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
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class SetEdit<_Element_>
    extends AbstractEdit<EditableSetBeed<_Element_>, SetEvent<_Element_, SetEdit<_Element_>>> {

  /**
   * @pre target != null;
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
  }

  public final void removeElementToAdd(_Element_ element) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToAdd.remove(element);
  }

  private Set<_Element_> $elementsToAdd = new HashSet<_Element_>();

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
  }

  public final void removeElementToRemove(_Element_ element) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $elementsToRemove.remove(element);
  }

  private Set<_Element_> $elementsToRemove = new HashSet<_Element_>();

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
  protected final boolean isChange() {
    return ! ($elementsToAdd.isEmpty() && $elementsToRemove.isEmpty());
  }

  /**
   * @return {@code A &cap; ''S = &empty;} and {@code R &sube; ''S}
   */
  @Override
  protected final boolean isInitialStateCurrent() {
    return intersection(getTarget().get(), $elementsToAdd).isEmpty() &&
           getTarget().get().containsAll($elementsToRemove);
  }

  /**
   * @post
   */
  @Override
  protected void performance() {
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
   * @post
   */
  @Override
  protected void unperformance() {
    // order irrelevant because sets are disjunct
    getTarget().removeElements($elementsToAdd);
    getTarget().addElements($elementsToRemove);
  }

  @Override
  protected final void fireEvent(SetEvent<_Element_, SetEdit<_Element_>> event) {
    getTarget().fireEvent(event);
  }

  @Override
  protected SetEvent<_Element_, SetEdit<_Element_>> createEvent() {
    return new SetEvent<_Element_, SetEdit<_Element_>>(getTarget(),
                                                       (getState() == DONE) ? $elementsToAdd : $elementsToRemove,
                                                       (getState() == DONE) ? $elementsToRemove : $elementsToAdd,
                                                       this);
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

}

