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

package org.beedra_II.property.number.real.fp;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.decimal.DoubleBeed;
import org.beedra_II.property.decimal.DoubleEvent;
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
    extends AbstractDoubleBinaryExpressionBeed {

  /**
   * @pre   owner != null;
   * @post  getDouble() == null;
   * @post  getNumerator() == null;
   * @post  getDenominator() == null;
   */
  public DoubleQuotientBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @basic
   */
  public final DoubleBeed<DoubleEvent> getNumerator() {
    return getLeftArgument();
  }

  /**
   * @post getNumerator() == numerator;
   */
  public final void setNumerator(DoubleBeed<DoubleEvent> numerator) {
    setLeftArgument(numerator);
  }

  /**
   * @basic
   */
  public final DoubleBeed<DoubleEvent> getDenominator() {
    return getRightArgument();
  }

  /**
   * @post getDenominator() == denominator;
   */
  public final void setDenominator(DoubleBeed<DoubleEvent> denominator) {
    setRightArgument(denominator);
  }

  /**
   * @pre numerator != null;
   * @pre denominator != null;
   */
  @Override
  protected final Double calculateValue(Double numerator, Double denominator) {
    assert numerator != null;
    assert denominator != null;
    return numerator / denominator;
  }

}

