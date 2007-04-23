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

package org.beedra_II.expression.number.real.double64;


import org.beedra_II.expression.number.real.RealBeed;
import org.beedra_II.path.Path;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that is the difference of a {@link #getLeftOprnd()}
 * and a {@link #getRightOprnd()}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleDifferenceBeed extends AbstractRealArgDoubleBinaryExpressionBeed {


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @post  getDouble() == null;
   * @post  getLeftOprnd() == null;
   * @post  getRightOprnd() == null;
   */
  public DoubleDifferenceBeed() {
    super();
  }

  /*</construction>*/



  /*<property name="positive term">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getPositiveTermPath() {
    return getRightOprndPath();
  }

  /**
   * @return getPositiveTermPath() == null ? null : getPositiveTermPath().get();
   */
  public final RealBeed<?> getPositiveTerm() {
    return getLeftOprnd();
  }

  /**
   * @post getPositiveTermPath() == positiveTermPath;
   */
  public final void setPositiveTermPath(Path<? extends RealBeed<?>> positiveTermPath) {
    setLeftOprndPath(positiveTermPath);
  }

  /*</property>*/



  /*<property name="positive term">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getNegativeTermPath() {
    return getRightOprndPath();
  }

  /**
   * @return getNegativeTermPath() == null ? null : getNegativeTermPath().get();
   */
  public final RealBeed<?> getNegativeTerm() {
    return getRightOprnd();
  }

  /**
   * @post getNegativeTermPath() == negativeTermPath;
   */
  public final void setNegativeTermPath(Path<? extends RealBeed<?>> negativeTermPath) {
    setRightOprndPath(negativeTermPath);
  }

  /*</property>*/



  /**
   * @pre leftOperand != null;
   * @pre rightOperand != null;
   */
  @Override
  protected final double calculateValue(double leftOperand, double rightOperand) {
    return leftOperand - rightOperand;
  }

  @Override
  public final String getOperatorString() {
    return "-";
  }

}

