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

package org.beedraz.semantics_II.expression.number.integer.long64;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MathUtil.castToBigInteger;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * A beed that is the negative of a {@link #getOperand() operand} {@link LongBeed}.
 *
 * @invar getOperand() == null ? getInteger() == null;
 * @invar getOperand() != null ? getInteger() == - getOperand().getInteger();
 *
 * @mudo overflow: -MIN_VALUE == MIN_VALUE
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class LongNegativeBeed
    extends AbstractRealArgLongUnaryExpressionBeed {

  public LongNegativeBeed() {
    super(null);
  }

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  public LongNegativeBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @pre operandValue != null;
   */
  @Override
  protected long calculateValue(long operandValue) {
    return -operandValue;
  }

  public BigInteger getBigInteger() {
    return castToBigInteger(getLong());
  }

  public BigDecimal getBigDecimal() {
    return castToBigDecimal(getLong());
  }

  @Override
  public final String getOperatorString() {
    return "-";
  }
}

