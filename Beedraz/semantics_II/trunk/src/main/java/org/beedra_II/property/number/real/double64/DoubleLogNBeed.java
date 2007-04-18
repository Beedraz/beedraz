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


import java.text.NumberFormat;

import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that is the log with base {@link #getConstant()} of an
 * {@link #getOperand() operand} {@link DoubleBeed}.
 *
 * Notice that logN(x) = log(x)/log(N).
 * Java only provides the log with base 10 and the natural logarithm
 * (see {@link Math#log10(double)} and {@link Math#log(double)}).
 *
 * @author Nele Smeets
 * @author PeopleWare n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleLogNBeed
    extends AbstractDoubleConstantUnaryExpressionBeed {

  /**
   * @post  getDouble() == null;
   * @post  getOperand() == null;
   * @post  getConstant() == constant;
   */
  public DoubleLogNBeed(double constant) {
    super(constant);
  }

  @Override
  protected final double calculateValue(double operandValue) {
    double base = getConstant();
    return Math.log(operandValue) / Math.log(base);
  }

  @Override
  public final String getOperatorString() {
    return "log[" + getConstant() + "]";
  }

  @Override
  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append(getOperatorString());
    sb.append("(");
    if (depth == 1) {
      sb.append(numberFormat.format(getOperand().getdouble()));
    }
    else {
      getOperand().toStringDepth(sb, depth - 1, numberFormat);
    }
    sb.append(")");
  }

}

