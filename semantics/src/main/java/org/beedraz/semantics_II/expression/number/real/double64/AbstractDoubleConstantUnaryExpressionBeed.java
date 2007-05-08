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

import java.text.NumberFormat;

import org.beedraz.semantics_II.expression.number.AbstractRealArgUnaryExpressionBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>General code for Double implementations of {@link AbstractRealArgUnaryExpressionBeed}
 *   that use a constant..</p>
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractDoubleConstantUnaryExpressionBeed
    extends AbstractRealArgDoubleUnaryExpressionBeed {

  /**
   * @post  getDouble() == null;
   * @post  getOperand() == null;
   * @post  getConstant() == constant;
   */
  public AbstractDoubleConstantUnaryExpressionBeed(Double constant) {
    super();
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
      sb.append(numberFormat.format(getOperand().getdouble()));
    }
    else {
      getOperand().toStringDepth(sb, depth - 1, numberFormat);
    }
    sb.append(")");
    sb.append(getOperatorString());
  }
}

