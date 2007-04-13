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

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.number.AbstractRealArgUnaryExpressionBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>General code for Double implementations of {@link AbstractRealArgUnaryExpressionBeed}
 *   that use a constant..</p>
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractDoubleConstantUnaryExpressionBeed
    extends AbstractRealArgDoubleUnaryExpressionBeed {

  /**
   * @pre   owner != null;
   * @post  getDouble() == null;
   * @post  getArgument() == null;
   * @post  getConstant() == constant;
   */
  public AbstractDoubleConstantUnaryExpressionBeed(AggregateBeed owner, Double constant) {
    super(owner);
    $constant = constant;
  }

  /**
   * @basic
   */
  public final double getConstant() {
    return $constant;
  }

  private final double $constant;

  @Override
  public void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append("(");
    if (depth == 1) {
      sb.append(numberFormat.format(getArgument().getdouble()));
    }
    else {
      getArgument().toStringDepth(sb, depth - 1, numberFormat);
    }
    sb.append(")");
    sb.append(getOperatorString());
  }
}

