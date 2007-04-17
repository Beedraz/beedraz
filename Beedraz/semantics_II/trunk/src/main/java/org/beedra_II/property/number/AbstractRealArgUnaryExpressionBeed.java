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

package org.beedra_II.property.number;


import java.text.NumberFormat;

import org.beedra_II.property.AbstractRealArgUnaryExprBeed;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.RealEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * Abstract implementation of unary expression beeds, that represent a value
 * of type {@link Number} derived from one argument of type {@link RealBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractRealArgUnaryExpressionBeed<_Number_ extends Number,
                                                         _NumberEvent_ extends RealEvent,
                                                         _ArgumentBeed_ extends RealBeed<?>>
    extends AbstractRealArgUnaryExprBeed<_Number_, _NumberEvent_, _ArgumentBeed_>
    implements RealBeed<_NumberEvent_>{

  /**
   * @post  getArgument() == null;
   * @post  get() == null;
   */
  public AbstractRealArgUnaryExpressionBeed() {
    super();
  }

  public final Double getDouble() {
    return isEffective() ? Double.valueOf(getdouble()) : null;
  }

  @Override
  protected boolean equalValue(_Number_ n1, _Number_ n2) {
    return MathUtil.equalValue(n1, n2);
  }

  public void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append(getOperatorString());
    sb.append("(");
    if (depth == 1) {
      sb.append(numberFormat.format(getArgument().getdouble()));
    }
    else {
      getArgument().toStringDepth(sb, depth - 1, numberFormat);
    }
    sb.append(")");
  }

}

