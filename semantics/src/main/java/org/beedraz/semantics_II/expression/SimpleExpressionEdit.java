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


import static org.beedraz.semantics_II.Edit.State.DONE;
import static org.beedraz.semantics_II.Edit.State.NOT_YET_PERFORMED;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import org.beedraz.semantics_II.AbstractSimpleEdit;
import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.Event;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * <p>Generic edit for {@link EditableSimpleExpressionBeed EditableSimpleExpressionBeeds}.
 *   The <em>edit target state</em> of {@link SimpleExpressionBeed SimpleExpressionBeeds}
 *   is simple, i.e., not compound (that is the definition of
 *   {@link SimpleExpressionBeed SimpleExpressionBeeds}). Therefor, the
 *   <em>edit target goal state</em> and the <em>edit target initial state</em>
 *   can be expressed with a simple inspector, {@link #getGoal()} and
 *   {@link #getInitial()} respectively.</p>
 * <p>Alternatively, the <em>edit target state</em> can be inspected using
 *   {@link #getNewValue()} and {@link #getOldValue()}, which return the appropriate
 *   <em>edit target state</em> depending on the {@link #getState() state} of the edit.
 *   When the edit is {@link State#DONE}, the {@link #getNewValue() new state} is
 *   the {@link #getGoal() <em>edit target goal state</em>} and the
 *   {@link #getOldValue() old state} is the {@link #getInitial() <em>edit target initial
 *   state</em>}.
 *   When the edit is {@link State#UNDONE} however, the {@link #getNewValue() new state} is
 *   the {@link #getInitial() <em>edit target initial state</em>} and the
 *   {@link #getOldValue() old state} is the {@link #getGoal() <em>edit target goal
 *   state</em>}.
 *   When the edit is {@link State#NOT_YET_PERFORMED}, the {@link #getNewValue() new state}
 *   is the {@link #getGoal() <em>edit target goal state</em>} and the
 *   {@link #getOldValue() old state} is undefined.
 *   When the edit is {@link State#DEAD}, both the {@link #getNewValue() new state}
 *   and the {@link #getOldValue() old state} are undefined.</p>
 *
 * @protected
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class SimpleExpressionEdit<_Type_,
                                         _Target_ extends EditableSimpleExpressionBeed<_Type_, _Event_>,
                                         _Event_ extends Event>
    extends AbstractSimpleEdit<_Target_, _Event_> {

  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @pre target != null;
   * @post getTarget() == target;
   * @post getState() == NOT_YET_PERFORMED;
   */
  public SimpleExpressionEdit(_Target_ target) {
    super(target);
  }

  /*</construction>*/



  /**
   * A safe copy of {@code value}. The returned object may
   * be {@code value}, but it must be a reference to an object
   * that is either immutable or is a copy of {@code value},
   * so that if it is changed, it does not change {@code value}.
   * Often, a {@link Object#clone()} will do the trick. The default
   * implementation returns {@code value}, but this should be overwritten
   * when {@code _Type_} is a type that is to be used by-value,
   * and is not immutable.
   *
   * @pre value != null;
   * @result ComparisonUtil.equalsWithNull(result, value);
   *
   * @protected-return value;
   */
  protected _Type_ safeCopy(_Type_ value) {
    return value;
  }


  /*<property name="goal">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final _Type_ getGoal() {
    return getTarget().safeValueCopyNull($goal);
  }

  /**
   * @post ComparisonUtil.equalsWithNull(goalValue, getGoalValue());
   */
  public final void setGoal(_Type_ goalValue) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $goal = getTarget().safeValueCopyNull(goalValue);
    recalculateValidity();
  }

  private _Type_ $goal;

  /*</property>*/



  /*<property name="initial">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final _Type_ getInitial() {
    return getTarget().safeValueCopyNull($initial);
  }

  private _Type_ $initial;

  /*</property>*/



  /*<section name="old and new">*/
  //-------------------------------------------------------

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

  /*</section>*/



  /*<section name="hook methods">*/
  //-------------------------------------------------------

  /**
   * Check whether {@link #getGoal()} would be acceptable currently
   * as value for the {@link #getTarget()}. This implementation
   * delegates to the target itself, and subtypes can add more
   * conditions by overriding this method.
   *
   * @result result ? getTarget().isAcceptable(getGoal());
   *
   * @idea generalize with event, that describes the change
   *
   * @protected
   * When this method is overridden, this implementation should
   * always be called.
   *
   * @todo non-final for BidirToOneEdit; try to fix that
   */
  @Override
  protected boolean isAcceptable() {
    return getTarget().isAcceptable(getGoal());
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getTarget().get(), getInitial());
   *
   * @todo non-final for OrderedBidirToOneEdit; try to fix that
   */
  @Override
  protected void storeInitialState() {
    $initial = getTarget().get();
  }

  /**
   * @return ! ComparisonUtil.equalsWithNull(getInitial(), getGoal());
   *
   * @todo non-final for OrderedBidirToOneEdit; try to fix that
   */
  @Override
  public boolean isChange() {
    return ! ComparisonUtil.equalsWithNull(getInitial(), getGoal());
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getGoal(), getTarget().get());
   *
   * @todo non-final for BidirToOneEdit; try to fix that
   */
  @Override
  protected void performance() {
    getTarget().assign(getGoal());
  }

  /**
   * @return ComparisonUtil.equalsWithNull(getInitial(), getTarget().get());
   *
   * @todo non-final for OrderedBidirToOneEdit; try to fix that
   */
  @Override
  protected boolean isInitialStateCurrent() {
    return ComparisonUtil.equalsWithNull(getInitial(), getTarget().get());
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getInitial(), getTarget().get());
   *
   * @todo non-final for BidirToOneEdit; try to fix that
   */
  @Override
  protected void unperformance() {
    getTarget().assign(getInitial());
  }

  /**
   * @return ComparisonUtil.equalsWithNull(getGoal(), getTarget().get());
   *
   * @todo non-final for OrderedBidirToOneEdit; try to fix that
   */
  @Override
  protected boolean isGoalStateCurrent() {
    return ComparisonUtil.equalsWithNull(getGoal(), getTarget().get());
  }

  /*</section>*/



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

