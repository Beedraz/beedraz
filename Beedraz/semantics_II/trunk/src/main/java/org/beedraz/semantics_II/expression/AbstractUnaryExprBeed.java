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


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.edit.Edit;
import org.beedraz.semantics_II.expression.bool.BooleanNotNullBeed;
import org.beedraz.semantics_II.expression.bool.BooleanNullBeed;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.beedraz.semantics_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of unary expression beeds, that represent a value derived
 * from one operand.
 *
 * @invar getOperandPath() != null
 *          ? getOperand() == getOperandPath().get()
 *          : null;
 * @invar getOperand() == null
 *          ? get() == null
 *          : true;
 * @invar getOperand() != null && getOperand().get() != null
 *          ? get() != null
 *          : true;
 * @default (Overridden in {@link BooleanNotNullBeed} and {@link BooleanNullBeed})
 * @invar getOperand() != null && getOperand().get() == null
 *          ? get() == null
 *          : true;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractUnaryExprBeed<_Result_ extends Object,
                                            _ResultEvent_ extends Event,
                                            _OperandBeed_ extends Beed<?>>
    extends AbstractPrimitiveDependentExprBeed<_Result_, _ResultEvent_>  {

  @Override
  protected final _ResultEvent_ filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
    /* Events are from the operand or the operand path, or both.
     * React to event from path first, setting the operand. Then do a recalculate.
     */
    _Result_ oldValue = get();
    PathEvent<_OperandBeed_> pathEvent = (PathEvent<_OperandBeed_>)events.get($operandPath);
    if (pathEvent != null) {
      setOperand(pathEvent.getNewValue());
    }
    recalculate(); // we know at this point that the operand path is effective; otherwise
                   // it would be impossible to get an event from it (or from the operand),
                   // so the precondition of the recalculate method is fulfilled
    if (! equalValue(oldValue, get())) {
      return createNewEvent(oldValue, get(), edit);
    }
    else {
      return null;
    }
  }


  /*<property name="operand">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   * @init null;
   */
  public final Path<? extends _OperandBeed_> getOperandPath() {
    return $operandPath;
  }

  /**
   * @return getOperandPath() == null ? null : getOperandPath().get();
   */
  public final _OperandBeed_ getOperand() {
    return $operand;
  }

  public final void setOperandPath(Path<? extends _OperandBeed_> beedPath) {
    _OperandBeed_ oldOperand = $operand;
    if ($operandPath instanceof AbstractDependentPath) {
      removeUpdateSource($operandPath);
    }
    $operandPath = beedPath;
    _OperandBeed_ operand = null;
    if ($operandPath != null) {
      operand = $operandPath.get();
    }
    if ($operandPath instanceof AbstractDependentPath) {
      addUpdateSource($operandPath);
    }
    if (operand != oldOperand) {
      setOperand(operand);
    }
  }

  private Path<? extends _OperandBeed_> $operandPath;

  /**
   * @post getOperand() == operand;
   */
  private final void setOperand(_OperandBeed_ operand) {
    _Result_ oldValue = get();
    if ($operand != null) {
      removeUpdateSource($operand);
    }
    $operand = operand;
    if ($operand != null) {
      recalculate();
      addUpdateSource($operand);
    }
    else {
      assignEffective(false);
    }
    if (! equalValue(oldValue, get())) {
      _ResultEvent_ event = createNewEvent(oldValue, get(), null);
      updateDependents(event);
    }
  }

  private _OperandBeed_ $operand;

  /*</property>*/



  /**
   * This method recalculates the value of the unary beed, when the operand
   * has changed.
   * When the operand is changed into null, then this method is not called,
   * since in that case, the value of this beed should be changed into null,
   * which is simply done by setting the value of {@link #isEffective()} to false.
   *
   * @pre getOperand() != null;
   *
   * This is the default implementation. The method is overridden in
   * {@link BooleanNotNullBeed} and {@link BooleanNullBeed}.
   */
  protected void recalculate() {
    if (hasEffectiveOperand()) {
      recalculateFrom($operand);
      assignEffective(true);
    }
    else {
      assignEffective(false);
    }
  }

  /**
   * Implement like {@code getOperand().isEffective()}, with
   * appropriate methods offered by {@code _OperandBeed_}.
   *
   * @pre getOperand() != null;
   */
  protected abstract boolean hasEffectiveOperand();

  /**
   * @pre operand != null;
   * @pre operand.isEffective(); // @mudo isEffective() does not exist in _OperandBeed_
   */
  protected abstract void recalculateFrom(_OperandBeed_ operand);

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
    sb.append(indent(level + 1) + "operand:\n");
    if (getOperand() != null) {
      getOperand().toString(sb, level + 2);
    }
    else {
      sb.append(indent(level + 2) + "null");
    }
  }

  /**
   * The operator of this binary expression.
   */
  public abstract String getOperatorString();

}

