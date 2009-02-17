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
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * A binary relation expression or equality expression
 * with two operands of type {@link RealBeed}.
 *
 * @mudo same code as AbstractBooleanBinaryLogicalExpressionBeed; generalize
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractBooleanBinaryRelationalExpressionBeed extends AbstractRealArgBooleanBinaryExpressionBeed {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractBooleanBinaryRelationalExpressionBeed(AggregateBeed owner) {
    super(owner);
  }


  /*<property name="leftOperand">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getLeftOperandPath() {
    return getLeftOprndPath();
  }

  /**
   * @return getLeftOperandPath() == null ? null : getLeftOperandPath().get();
   */
  public final RealBeed<?> getLeftOperand() {
    return getLeftOprnd();
  }

  /**
   * @post getLeftOperandPath() == leftOperandPath;
   */
  public final void setLeftOperandPath(Path<? extends RealBeed<?>> leftOperandPath) {
    setLeftOprndPath(leftOperandPath);
  }

  /*</property>*/


  /*<property name="rightOperand">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getRightOperandPath() {
    return getRightOprndPath();
  }

  /**
   * @return getRightOperandPath() == null ? null : getRightOperandPath().get();
   */
  public final RealBeed<?> getRightOperand() {
    return getRightOprnd();
  }

  /**
   * @post getRightOperandPath() == rightOperandPath;
   */
  public final void setRightOperandPath(Path<? extends RealBeed<?>> rightOperandPath) {
    setRightOprndPath(rightOperandPath);
  }

  /*</property>*/

}

