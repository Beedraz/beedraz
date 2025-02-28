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

package org.beedraz.semantics_II.expression.bool;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * A binary logical expression with two operands of type {@link BooleanBeed}.
 *
 * @mudo same code as AbstractBooleanBinaryRelationalExpressionBeed; generalize
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractBooleanConditionalBinaryExpressionBeed
    extends AbstractBooleanArgBooleanBinaryExpressionBeed {


  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractBooleanConditionalBinaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }


  /*<property name="leftOperand">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends BooleanBeed> getLeftOperandPath() {
    return getLeftOprndPath();
  }

  /**
   * @return getLeftOperandPath().get();
   */
  public final BooleanBeed getLeftOperand() {
    return getLeftOprnd();
  }

  /**
   * @post getLeftOperandPath() == leftOperandPath;
   */
  public final void setLeftOperandPath(Path<? extends BooleanBeed> leftOperandPath) {
    setLeftOprndPath(leftOperandPath);
  }

  /*</property>*/


  /*<property name="rightOperand">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends BooleanBeed> getRightOperandPath() {
    return getRightOprndPath();
  }

  /**
   * @return getRightOperandPath().get();
   */
  public final BooleanBeed getRightOperand() {
    return getRightOprnd();
  }

  /**
   * @post getRightOperandPath() == rightOperandPath;
   */
  public final void setRightOperandPath(Path<? extends BooleanBeed> rightOperandPath) {
    setRightOprndPath(rightOperandPath);
  }

  /*</property>*/

  /**
   * The logical boolean expressions are conditional: evaluating the
   * left operand is sometimes enough to know the result of the whole expression.
   * In this case, it is not necessary that the value of the
   * right operand is effective.
   */
  @Override
  protected void recalculate() {
    if ((getLeftOperand() != null) && hasEffectiveLeftOperand()) {
      if (isDecisive(getLeftOperand()) ||
          ((getRightOperand() != null) && hasEffectiveRightOperand())) {
        recalculateConditionalFrom(getLeftOperand(), getRightOperand());
        assignEffective(true);
      }
      else {
        assignEffective(false);
      }
    }
    else {
      assignEffective(false);
    }
  }

  protected abstract boolean isDecisive(BooleanBeed leftOperand);

  /**
   * Recalculate the value of this beed, and store the result, where
   * implementations that return the result can pick it up.
   *
   * @pre  isDecisive(leftOperand) ||
   *       ((rightOperand != null) && rightOperand.isEffective())
   */
  protected abstract void recalculateConditionalFrom(BooleanBeed leftOperand, BooleanBeed rightOperand);

  @Override
  protected final void recalculateFrom(BooleanBeed leftOperand, BooleanBeed rightOperand) {
    throw new IllegalStateException();
  }

  @Override
  protected final boolean calculateValue(boolean leftOperand, boolean rightOperand) {
    throw new IllegalStateException();
  }


}