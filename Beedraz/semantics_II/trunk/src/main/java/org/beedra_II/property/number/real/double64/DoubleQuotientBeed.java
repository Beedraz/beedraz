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

package org.beedra_II.property.number.real.double64;


import org.beedra_II.path.Path;
import org.beedra_II.property.number.real.RealBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that is the quotient of a {@link #getNumerator() numerator}
 * and a {@link #getDenominator() denominator}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleQuotientBeed
    extends AbstractRealArgDoubleBinaryExpressionBeed {

  /**
   * @post  getDouble() == null;
   * @post  getNumerator() == null;
   * @post  getDenominator() == null;
   */
  public DoubleQuotientBeed() {
    super();
  }



  /*<property name="numerator">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getNumeratorPath() {
    return getLeftArgPath();
  }

  /**
   * @return getNumeratorPath() == null ? null : getNumeratorPath().get();
   */
  public final RealBeed<?> getNumerator() {
    return getLeftArg();
  }

  /**
   * @post getNumeratorPath() == numeratorPath;
   */
  public final void setNumeratorPath(Path<? extends RealBeed<?>> numeratorPath) {
    setLeftArgPath(numeratorPath);
  }

  /*</property>*/



  /*<property name="denominator">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getDenominatorPath() {
    return getRightArgPath();
  }

  /**
   * @return getDenominatorPath() == null ? null : getDenominatorPath().get();
   */
  public final RealBeed<?> getDenominator() {
    return getRightArg();
  }

  /**
   * @post getDenominatorPath() == denominatorPath;
   */
  public final void setDenominatorPath(Path<? extends RealBeed<?>> denominatorPath) {
    setRightArgPath(denominatorPath);
  }

  /*</property>*/




  /**
   * @pre numerator != null;
   * @pre denominator != null;
   */
  @Override
  protected final double calculateValue(double numerator, double denominator) {
    return numerator / denominator;
  }

  @Override
  public final String getOperatorString() {
    return "/";
  }

}

