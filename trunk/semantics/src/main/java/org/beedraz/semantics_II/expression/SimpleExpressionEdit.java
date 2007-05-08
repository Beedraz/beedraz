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

package org.beedraz.semantics_II.expression;


import static org.beedraz.semantics_II.edit.Edit.State.DONE;
import static org.beedraz.semantics_II.edit.Edit.State.NOT_YET_PERFORMED;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.edit.AbstractSimpleEdit;
import org.beedraz.semantics_II.edit.EditStateException;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * @mudo definition
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class SimpleExpressionEdit<_Type_,
                                         _Target_ extends EditableSimpleExpressionBeed<_Type_, _Event_>,
                                         _Event_ extends Event>
    extends AbstractSimpleEdit<_Target_, _Event_> {

  /**
   * @pre target != null;
   * @post getTarget() == target;
   */
  public SimpleExpressionEdit(_Target_ target) {
    super(target);
  }


  /**
   * @basic
   */
  public final _Type_ getGoal() {
    return $goal;
  }
  /**
   * @post ComparisonUtil.equalsWithNull(goalValue, getGoalValue());
   */
  public final void setGoal(_Type_ goalValue) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $goal = goalValue;
    recalculateValidity();
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
   * @return return getState() == DONE ? getInitial() : getGoal();
   */
  public final _Type_ getOldValue() {
    return getState() == DONE ? getInitial() : getGoal();
  }

  /**
   * @return return getState() == DONE ? getGoal() : getInitial();
   */
  public final _Type_ getNewValue() {
    return getState() == DONE ? getGoal() : getInitial();
  }

  // generalize with event
  @Override
  protected boolean isAcceptable() {
    return getTarget().isAcceptable(getGoal());
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getTarget().get(), getInitial());
   */
  @Override
  protected void storeInitialState() {
    $initial = getTarget().get();
  }

  @Override
  public boolean isChange() {
    return ! ComparisonUtil.equalsWithNull(getInitial(), getGoal());
  }

  /**
   * @return ComparisonUtil.equalsWithNull(getInitial(), getTarget().get());
   */
  @Override
  protected boolean isInitialStateCurrent() {
    return ComparisonUtil.equalsWithNull(getInitial(), getTarget().get());
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getGoal(), getTarget().get());
   */
  @Override
  protected void performance() {
    getTarget().assign(getGoal());
  }

  /**
   * @return ComparisonUtil.equalsWithNull(getGoal(), getTarget().get());
   */
  @Override
  protected boolean isGoalStateCurrent() {
    return ComparisonUtil.equalsWithNull(getGoal(), getTarget().get());
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getInitial(), getTarget().get());
   */
  @Override
  protected void unperformance() {
    getTarget().assign(getInitial());
  }

  @Override
  protected final void updateDependents(_Event_ event) {
    getTarget().packageUpdateDependents(event);
  }

//  @Override
//  protected void notifyListeners() {
//    getTarget().fireEdit(this);
//  }

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", goal: " + getGoal() +
           ", initial: " + getInitial();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    toStringGoalInitial(sb, level + 1);
  }

  protected void toStringGoalInitial(StringBuffer sb, int level) {
    sb.append(indent(level + 1) + "goal: " + getGoal() + "\n");
    sb.append(indent(level + 1) + "initial: " + getInitial() + "\n");
  }

}

