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

package org.beedra_II.property.simple;


import static org.beedra_II.edit.Edit.State.NOT_YET_PERFORMED;

import org.beedra.util_I.Comparison;
import org.beedra_II.edit.AbstractEdit;
import org.beedra_II.edit.EditStateException;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * @mudo definition
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class SimpleEdit<_Type_>
    extends AbstractEdit<EditableSimplePropertyBeed<_Type_, ? extends SimpleEditEvent<_Type_>>> {

  /**
   * @pre target != null;
   * @post getTarget() == target;
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   */
  public SimpleEdit(EditableSimplePropertyBeed<_Type_, ? extends SimpleEditEvent<_Type_>> target) {
    super(target);
  }

  /**
   * @basic
   */
  public final _Type_ getGoal() {
    return $goal;
  }
  /**
   * @post Comparison.equalsWithNull(goalValue, getGoalValue());
   */
  public void setGoal(_Type_ goalValue) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $goal = goalValue;
  }

  private _Type_ $goal;

  /**
   * @basic
   */
  public final _Type_ getInitial() {
    return $initial;
  }

  private _Type_ $initial;

  /**
   * @post Comparison.equalsWithNull(getTarget().get(), getInitial());
   */
  @Override
  protected void storeInitialState() {
    $initial = getTarget().get();
  }

  /**
   * @return Comparison.equalsWithNull(getInitial(), getTarget().get());
   */
  @Override
  protected boolean isInitialStateCurrent() {
    return Comparison.equalsWithNull(getInitial(), getTarget().get());
  }

  /**
   * @post Comparison.equalsWithNull(getGoal(), getTarget().get());
   */
  @Override
  protected final void performance() {
    getTarget().assign(getGoal());
  }

  /**
   * @return Comparison.equalsWithNull(getGoal(), getTarget().get());
   */
  @Override
  protected boolean isGoalStateCurrent() {
    return Comparison.equalsWithNull(getGoal(), getTarget().get());
  }

  /**
   * @post Comparison.equalsWithNull(getInitial(), getTarget().get());
   */
  @Override
  protected final void unperformance() {
    getTarget().assign(getInitial());
  }

  @Override
  protected void notifyListeners() {
// MUDO
//    getTarget().fireChangeEvent(createEditEvent());
  }

  protected abstract SimpleEditEvent<_Type_> createEditEvent();

}

