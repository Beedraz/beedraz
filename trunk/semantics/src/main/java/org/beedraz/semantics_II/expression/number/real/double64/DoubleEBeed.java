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

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>A beed that is the natural power of an {@link #getOperand() operand}
 *   {@link DoubleBeed} (<code>e<sup>x</sup></code>).</p>
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoubleEBeed
    extends AbstractRealArgDoubleUnaryExpressionBeed {

  /**
   * @post  getDouble() == null;
   * @post  getOperand() == null;
   */
  public DoubleEBeed() {
    this(null);
  }

  /**
   * @post  getDouble() == null;
   * @post  getOperand() == null;
   * @post  owner != nul ? owner.registerAggregateElement(this);
   */
  public DoubleEBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @pre operandValue != null;
   */
  @Override
  protected final double calculateValue(double operandValue) {
    return Math.exp(operandValue);
  }

  @Override
  public final String getOperatorString() {
    return "exp";
  }

}

