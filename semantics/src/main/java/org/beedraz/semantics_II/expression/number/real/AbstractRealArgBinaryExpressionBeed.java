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

package org.beedraz.semantics_II.expression.number.real;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.text.NumberFormat;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.MathUtil;


/**
 * Abstract implementation of binary expression beeds, that represent a number value derived
 * from 2 operands of type {@link RealBeed}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractRealArgBinaryExpressionBeed<
                                                   _Number_ extends Number,
                                                   _NumberEvent_ extends RealEvent,
                                                   _LeftOperandBeed_ extends RealBeed<? extends _LeftOperandEvent_>,
                                                   _LeftOperandEvent_ extends RealEvent,
                                                   _RightOperandBeed_ extends RealBeed<? extends _RightOperandEvent_>,
                                                   _RightOperandEvent_ extends RealEvent>
    extends AbstractRealArgBinaryExprBeed<_Number_,
                                          _NumberEvent_,
                                          _LeftOperandBeed_,
                                          _LeftOperandEvent_,
                                          _RightOperandBeed_,
                                          _RightOperandEvent_>
    implements RealBeed<_NumberEvent_> {

  /**
   * @post  getLeftOprnd() == null;
   * @post  getRightOprnd() == null;
   * @post  get() == null;
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractRealArgBinaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  public final Double getDouble() {
    return isEffective() ? Double.valueOf(getdouble()) : null;
  }

  @Override
  protected boolean equalValue(_Number_ n1, _Number_ n2) {
    return MathUtil.equalValue(n1, n2);
  }

  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append("(");
    if (depth == 1) {
      sb.append(numberFormat.format(getLeftOprnd().getdouble()));
    }
    else {
      getLeftOprnd().toStringDepth(sb, depth - 1, numberFormat);
    }
    sb.append(")");
    sb.append(getOperatorString());
    sb.append("(");
    if (depth == 1) {
      sb.append(numberFormat.format(getRightOprnd().getdouble()));
    }
    else {
      getRightOprnd().toStringDepth(sb, depth - 1, numberFormat);
    }
    sb.append(")");
  }

}

