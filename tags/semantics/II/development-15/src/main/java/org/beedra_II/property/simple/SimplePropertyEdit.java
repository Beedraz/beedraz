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


import static org.beedra.util_I.MultiLineToStringUtil.indent;
import static org.beedra_II.edit.Edit.State.DONE;
import static org.beedra_II.edit.Edit.State.NOT_YET_PERFORMED;

import org.beedra.util_I.Comparison;
import org.beedra_II.edit.AbstractSimpleEdit;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.event.Event;
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
public abstract class SimplePropertyEdit<_Type_,
                                         _Target_ extends EditableSimplePropertyBeed<_Type_, _Event_>,
                                         _Event_ extends Event>
    extends AbstractSimpleEdit<_Target_, _Event_> {

  /**
   * @pre target != null;
   * @post getTarget() == target;
   */
  public SimplePropertyEdit(_Target_ target) {
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
  protected final boolean isAcceptable() {
    return getTarget().isAcceptable(getGoal());
  }

  /**
   * @post Comparison.equalsWithNull(getTarget().get(), getInitial());
   */
  @Override
  protected final void storeInitialState() {
    $initial = getTarget().get();
  }

  @Override
  public final boolean isChange() {
    return ! Comparison.equalsWithNull(getInitial(), getGoal());
  }

  /**
   * @return Comparison.equalsWithNull(getInitial(), getTarget().get());
   */
  @Override
  protected final boolean isInitialStateCurrent() {
    return Comparison.equalsWithNull(getInitial(), getTarget().get());
  }

  /**
   * @post Comparison.equalsWithNull(getGoal(), getTarget().get());
   */
  @Override
  protected void performance() {
    getTarget().assign(getGoal());
  }

  /**
   * @return Comparison.equalsWithNull(getGoal(), getTarget().get());
   */
  @Override
  protected final boolean isGoalStateCurrent() {
    return Comparison.equalsWithNull(getGoal(), getTarget().get());
  }

  /**
   * @post Comparison.equalsWithNull(getInitial(), getTarget().get());
   */
  @Override
  protected void unperformance() {
    getTarget().assign(getInitial());
  }

  @Override
  protected final void fireEvent(_Event_ event) {
    getTarget().fireEvent(event);
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

