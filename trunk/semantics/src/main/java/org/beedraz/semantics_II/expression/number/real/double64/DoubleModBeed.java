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

import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A beed that represents the remainder operation, where dividend and divisor
 * are of type {link {@link RealBeed}}.
 * In Java: a % b.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoubleModBeed extends AbstractRealArgDoubleBinaryExpressionBeed {


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @post  getDouble() == null;
   * @post  getDividend() == null;
   * @post  getDivisor() == null;
   */
  public DoubleModBeed() {
    super();
  }

  /*</construction>*/


  /*<property name="dividend">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getDividendPath() {
    return getLeftOprndPath();
  }

  /**
   * @return getDividendPath().get();
   */
  public final RealBeed<?> getDividend() {
    return getLeftOprnd();
  }

  /**
   * @post getDividendPath() == dividendPath;
   */
  public final void setDividendPath(Path<? extends RealBeed<?>> dividendPath) {
    setLeftOprndPath(dividendPath);
  }

  /*</property>*/


  /*<property name="divisor">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getDivisorPath() {
    return getRightOprndPath();
  }

  /**
   * @return getDivisorPath().get();
   */
  public final RealBeed<?> getDivisor() {
    return getRightOprnd();
  }

  /**
   * @post getDivisorPath() == divisorPath;
   */
  public final void setDivisorPath(Path<? extends RealBeed<?>> divisorPath) {
    setRightOprndPath(divisorPath);
  }

  /*</property>*/


  /**
   * @pre dividend != null;
   * @pre divisor != null;
   */
  @Override
  protected final double calculateValue(double dividend, double divisor) {
    return dividend % divisor;
  }

  @Override
  public final String getOperatorString() {
    return "%";
  }

}

