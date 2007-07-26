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

package org.beedraz.semantics_II.expression.number.real.double64;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A beed that is the product of a {@link #getLeftOperand()} and a {@link #getRightOperand()}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoubleProductBeed extends AbstractRealArgDoubleBinaryExpressionBeed {


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @post  getDouble() == null;
   * @post  getLeftOperand() == null;
   * @post  getRightOperand() == null;
   */
  public DoubleProductBeed() {
    this(null);
  }

  /**
   * @post  getDouble() == null;
   * @post  getLeftOperand() == null;
   * @post  getRightOperand() == null;
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  public DoubleProductBeed(AggregateBeed owner) {
    super(owner);
  }

  /*</construction>*/



  /*<property name="left operand">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getLeftOperandPath() {
    return getLeftOperandPath();
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



  /*<property name="right operand">*/
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



  /**
   * @pre leftOperand != null;
   * @pre rightOperand != null;
   */
  @Override
  protected final double calculateValue(double leftOperand, double rightOperand) {
    return leftOperand * rightOperand;
  }

  @Override
  public final String getOperatorString() {
    return "*";
  }

}

