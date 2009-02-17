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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;

import java.math.BigDecimal;

import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.number.real.AbstractRealArgBinaryExpressionBeed;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.expression.number.real.RealEvent;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * General code for Double implementations of {@link AbstractRealArgBinaryExpressionBeed}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractRealArgDoubleBinaryExpressionBeed
    extends AbstractRealArgBinaryExpressionBeed<
                                         Double,
                                         ActualDoubleEvent,
                                         RealBeed<?>,
                                         RealEvent,
                                         RealBeed<?>,
                                         RealEvent>
    implements DoubleBeed {

  /**
   * @post  getLeftOprnd() == null;
   * @post  getRightOprnd() == null;
   * @post  get() == null;
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractRealArgDoubleBinaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  public final double getdouble() {
    return $value;
  }

  private double $value;

  @Override
  public final Double get() {
    return getDouble();
  }

  public final BigDecimal getBigDecimal() {
    return castToBigDecimal(getDouble());
  }

  @Override
  protected final ActualDoubleEvent createNewEvent(Double oldValue, Double newValue, Edit<?> edit) {
    return new ActualDoubleEvent(this, oldValue, newValue, edit);
  }

  @Override
  protected final void recalculateFrom(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    $value = calculateValue(leftOperand.getdouble(), rightOperand.getdouble());
  }

  protected abstract double calculateValue(double leftOperand, double rightOperand);

}

