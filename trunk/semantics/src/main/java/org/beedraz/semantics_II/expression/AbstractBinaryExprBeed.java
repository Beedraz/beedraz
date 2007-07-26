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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Abstract implementation of binary expression beeds, that represent a value derived
 * from 2 operands.
 *
 * @invar getLeftOprnd() == null || getRightOprnd() == null
 *          ? get() == null
 *          : true;
 * @invar getLeftOprnd() != null && getRightOprnd() != null &&
 *        getLeftOprnd().get() != null && getRightOprnd().get() != null
 *          ? get() != null
 *          : true;
 * @invar getLeftOprnd() != null && getRightOprnd() != null &&
 *        getLeftOprnd().get() == null
 *          ? get() == null
 *          : true;
 * @default (Overridden in {@link org.beedraz.semantics_II.expression.bool.AbstractBooleanConditionalBinaryExpressionBeed})
 * @invar getLeftOprnd() != null && getRightOprnd() != null &&
 *        getRightOprnd().get() == null
 *          ? get() == null
 *          : true;
 *
 * @protected
 * Accessor methods for the {@link #getLeftOprnd() left operand} and the {@link #getRightOprnd()
 * right operand} are kept protected, to force subclasses to provide meaningful public names for the
 * operands.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractBinaryExprBeed<_Result_ extends Object,
                                             _ResultEvent_ extends Event,
                                             _LeftOperandBeed_ extends Beed<? extends _LeftOperandEvent_>,
                                             _LeftOperandEvent_ extends Event,
                                             _RightOperandBeed_ extends Beed<? extends _RightOperandEvent_>,
                                             _RightOperandEvent_ extends Event>
    extends AbstractPrimitiveDependentExprBeed<_Result_, _ResultEvent_>  {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractBinaryExprBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected final _ResultEvent_ filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    /* Events are from the left operand, the left operand path, the right operand, the
     * right operand path, or any combination.
     * React to event from paths first, setting the operands. Then do a recalculate.
     */
    _Result_ oldValue = get();
    PathEvent<_LeftOperandBeed_> leftPathEvent = (PathEvent<_LeftOperandBeed_>)events.get($leftOperandPath);
    if (leftPathEvent != null) {
      setLeftOprnd(leftPathEvent.getNewValue());
    }
    PathEvent<_RightOperandBeed_> rightPathEvent = (PathEvent<_RightOperandBeed_>)events.get($rightOperandPath);
    if (rightPathEvent != null) {
      setRightOprnd(rightPathEvent.getNewValue());
    }
    recalculate();
    if (! equalValue(oldValue, get())) {
      return createNewEvent(oldValue, get(), edit);
    }
    else {
      return null;
    }
  }



  /*<property name="left operand">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   * @init null;
   */
  protected final Path<? extends _LeftOperandBeed_> getLeftOprndPath() {
    return $leftOperandPath;
  }

  /**
   * @return getLeftOprndPath() == null ? null : getLeftOprndPath().get();
   */
  protected final _LeftOperandBeed_ getLeftOprnd() {
    return $leftOperand;
  }

  protected final void setLeftOprndPath(Path<? extends _LeftOperandBeed_> beedPath) {
    _LeftOperandBeed_ oldLeftOperand = $leftOperand;
    if ($leftOperandPath instanceof AbstractDependentPath) {
      removeUpdateSource($leftOperandPath);
    }
    $leftOperandPath = beedPath;
    _LeftOperandBeed_ leftOperand = null;
    if ($leftOperandPath != null) {
      leftOperand = $leftOperandPath.get();
    }
    if ($leftOperandPath instanceof AbstractDependentPath) {
      addUpdateSource($leftOperandPath);
    }
    if (leftOperand != oldLeftOperand) {
      setLeftOprnd(leftOperand);
    }
  }

  private Path<? extends _LeftOperandBeed_> $leftOperandPath;

  /**
   * @post getLeftOprnd() == leftOperand;
   */
  private final void setLeftOprnd(_LeftOperandBeed_ leftOperand) {
    _Result_ oldValue = get();
    if ($leftOperand != null) {
      removeUpdateSource($leftOperand);
    }
    $leftOperand = leftOperand;
    recalculate();
    if ($leftOperand != null) {
      addUpdateSource($leftOperand);
    }
    if (! equalValue(oldValue, get())) {
      updateDependents(createNewEvent(oldValue, get(), null));
    }
  }

  private _LeftOperandBeed_ $leftOperand;

  /*</property>*/



  /*<property name="right operand">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   * @init null;
   */
  protected final Path<? extends _RightOperandBeed_> getRightOprndPath() {
    return $rightOperandPath;
  }

  /**
   * @return getRightOprndPath() == null ? null : getRightOprndPath().get();
   */
  protected final _RightOperandBeed_ getRightOprnd() {
    return $rightOperand;
  }

  protected final void setRightOprndPath(Path<? extends _RightOperandBeed_> beedPath) {
    _RightOperandBeed_ oldRightOperand = $rightOperand;
    if ($rightOperandPath instanceof AbstractDependentPath) {
      removeUpdateSource($rightOperandPath);
    }
    $rightOperandPath = beedPath;
    _RightOperandBeed_ rightOperand = null;
    if ($rightOperandPath != null) {
      rightOperand = $rightOperandPath.get();
    }
    if ($rightOperandPath instanceof AbstractDependentPath) {
      addUpdateSource($rightOperandPath);
    }
    if (rightOperand != oldRightOperand) {
      setRightOprnd(rightOperand);
    }
  }

  private Path<? extends _RightOperandBeed_> $rightOperandPath;

  /**
   * @post getRightOprnd() == rightOperand;
   */
  private final void setRightOprnd(_RightOperandBeed_ rightOperand) {
    _Result_ oldValue = get();
    if ($rightOperand != null) {
      removeUpdateSource($rightOperand);
    }
    $rightOperand = rightOperand;
    recalculate();
    if ($rightOperand != null) {
      addUpdateSource($rightOperand);
    }
    if (! equalValue(oldValue, get())) {
      updateDependents(createNewEvent(oldValue, get(), null));
    }
  }

  private _RightOperandBeed_ $rightOperand;

  /*</property>*/



  /**
   * This method recalculates the value of the binary beed, when one of
   * the operands has changed.
   * When one of the operands is null, or when the value of one of the operands
   * is null, the value of this beed should be changed into null,
   * which is simply done by setting the value of {@link #isEffective()} to false.
   *
   * This is the default implementation. The method is overridden in
   * {@link org.beedraz.semantics_II.expression.bool.AbstractBooleanConditionalBinaryExpressionBeed}.
   */
  protected void recalculate() {
    if (($leftOperand != null) && hasEffectiveLeftOperand() &&
        ($rightOperand != null) && hasEffectiveRightOperand()) {
      recalculateFrom($leftOperand, $rightOperand);
      assignEffective(true);
    }
    else {
      assignEffective(false);
    }
  }

  /**
   * Implement like {@code getLeftOprnd().isEffective()}, with
   * appropriate methods offered by {@code _LeftOperandBeed_}.
   */
  protected abstract boolean hasEffectiveLeftOperand();

  /**
   * Implement like {@code getRightOprnd().isEffective()}, with
   * appropriate methods offered by {@code _RightOperandBeed_}.
   */
  protected abstract boolean hasEffectiveRightOperand();

  /**
   * Recalculate the value of this beed, and store the result, where
   * implementations that return the result can pick it up.
   *
   * @pre leftOperand != null;
   * @pre leftOperand.isEffective(); // @mudo isEffective() does not exist in _OperandBeed_
   * @pre rightOperand != null;
   * @pre $rightOperand.isEffective(); // @mudo isEffective() does not exist in _OperandBeed_
   */
  protected abstract void recalculateFrom(_LeftOperandBeed_ leftOperand, _RightOperandBeed_ rightOperand);

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
    sb.append(indent(level + 1) + "operands:\n");
    if (getLeftOprnd() == null && getRightOprnd() == null) {
      sb.append(indent(level + 2) + "null");
    }
    if (getLeftOprnd() != null) {
      getLeftOprnd().toString(sb, level + 2);
    }
    if (getRightOprnd() != null) {
      getRightOprnd().toString(sb, level + 2);
    }
  }


  /**
   * The operator of this binary expression.
   */
  public abstract String getOperatorString();

}

