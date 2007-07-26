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
 * A beed that is the quotient of a {@link #getNumerator() numerator}
 * and a {@link #getDenominator() denominator}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoubleQuotientBeed
    extends AbstractRealArgDoubleBinaryExpressionBeed {

  /**
   * @post  getDouble() == null;
   * @post  getNumerator() == null;
   * @post  getDenominator() == null;
   */
  public DoubleQuotientBeed() {
    this(null);
  }

  /**
   * @post  getDouble() == null;
   * @post  getNumerator() == null;
   * @post  getDenominator() == null;
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  public DoubleQuotientBeed(AggregateBeed owner) {
    super(owner);
  }


  /*<property name="numerator">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getNumeratorPath() {
    return getLeftOprndPath();
  }

  /**
   * @return getNumeratorPath() == null ? null : getNumeratorPath().get();
   */
  public final RealBeed<?> getNumerator() {
    return getLeftOprnd();
  }

  /**
   * @post getNumeratorPath() == numeratorPath;
   */
  public final void setNumeratorPath(Path<? extends RealBeed<?>> numeratorPath) {
    setLeftOprndPath(numeratorPath);
  }

  /*</property>*/



  /*<property name="denominator">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getDenominatorPath() {
    return getRightOprndPath();
  }

  /**
   * @return getDenominatorPath() == null ? null : getDenominatorPath().get();
   */
  public final RealBeed<?> getDenominator() {
    return getRightOprnd();
  }

  /**
   * @post getDenominatorPath() == denominatorPath;
   */
  public final void setDenominatorPath(Path<? extends RealBeed<?>> denominatorPath) {
    setRightOprndPath(denominatorPath);
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

