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

package org.beedra_II.property.number.decimal;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.decimal.DoubleBeed;
import org.beedra_II.property.decimal.DoubleEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that is the {@link #getExponent() exponent-power}
 * of a {@link #getBase() base} (<code>base<sup>exponent</sup></code>).
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleExponentBeed
    extends AbstractDoubleBinaryExpressionBeed {

  /**
   * @pre   owner != null;
   * @post  getDouble() == null;
   * @post  getBase() == null;
   * @post  getExponent() == null;
   */
  public DoubleExponentBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @basic
   */
  public final DoubleBeed<DoubleEvent> getBase() {
    return getLeftArgument();
  }

  /**
   * @post getBase() == base;
   */
  public final void setBase(DoubleBeed<DoubleEvent> base) {
    setLeftArgument(base);
  }

  /**
   * @basic
   */
  public final DoubleBeed<DoubleEvent> getExponent() {
    return getRightArgument();
  }

  /**
   * @post getExponent() == exponent;
   */
  public final void setExponent(DoubleBeed<DoubleEvent> exponent) {
    setRightArgument(exponent);
  }

  /**
   * @pre base != null;
   * @pre exponent != null;
   */
  @Override
  protected final Double calculateValue(Double base, Double exponent) {
    assert base != null;
    assert exponent != null;
    return Math.pow(base, exponent);
  }

}

