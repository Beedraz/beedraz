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

package org.beedra_II.property.number.integer.long64;


import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MathUtil.castToBigInteger;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that is the negative of a {@link #getArgument() argument} {@link LongBeed}.
 *
 * @invar getArgument() == null ? getInteger() == null;
 * @invar getArgument() != null ? getInteger() == - getArgument().getInteger();
 *
 * @mudo overflow: -MIN_VALUE == MIN_VALUE
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class LongNegativeBeed
    extends AbstractRealArgLongUnaryExpressionBeed {

  /**
   * @post  getInteger() == null;
   * @post  getArgument() == null;
   */
  public LongNegativeBeed() {
    super(null);
  }

  /**
   * @pre argumentValue != null;
   */
  @Override
  protected long calculateValue(long argumentValue) {
    return -argumentValue;
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

