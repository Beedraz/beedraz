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
 * A beed that is the {@link #getConstant()}-root of an
 * {@link #getOperand() operand} {@link DoubleBeed}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoubleRootBeed
    extends AbstractDoubleConstantUnaryExpressionBeed {

  /**
   * @post  getDouble() == null;
   * @post  getOperand() == null;
   * @post  getConstant() == constant;
   */
  public DoubleRootBeed(double constant) {
    this(constant, null);
  }

  /**
   * @post  getDouble() == null;
   * @post  getOperand() == null;
   * @post  getConstant() == constant;
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  public DoubleRootBeed(double constant, AggregateBeed owner) {
    super(constant, owner);
  }

  /**
   * @pre operandValue != null;
   */
  @Override
  protected final double calculateValue(double operandValue) {
    double root = getConstant();
    if (root == 1) {
      return operandValue;
    }
    else if (root == 2) {
      return Math.sqrt(operandValue);
    }
    else if (root == 3) {
      return Math.cbrt(operandValue);
    }
    else {
      return Math.pow(operandValue, 1 / getConstant());
    }
  }

  @Override
  public final String getOperatorString() {
    return "^(1/" + getConstant() + ")";
  }

}

