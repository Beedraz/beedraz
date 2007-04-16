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

package org.beedra_II.property.number.real;


import java.math.BigDecimal;
import java.text.NumberFormat;

import org.beedra_II.Beed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed representing a real value. Real values cannot
 * be represented in a computer. Both {@link DigDecimal}
 * and {@link Double} representations are supported.
 * The first has big consequences on performance.
 *
 * Listeners of the beed can receive events of type
 * {@link RealEvent}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @invar isEffective() ?? getBigDecimal() != null;
 * @invar isEffective() ?? getDouble() != null;
 * @invar equalValue(getDouble(), getBigDecimal());
 * @invar isEffective() ? getDouble().doubleValue() == getdouble();
 *
 * @mudo describe semantics if real is to big (or to small) for double
 * @mudo why don't we extend {@link SimpleExpressionBeed}?
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface RealBeed<_Event_ extends RealEvent>
    extends Beed<_Event_> {

  /**
   * @basic
   */
  BigDecimal getBigDecimal();

  /**
   * @basic
   */
  Double getDouble();

  /**
   * @basic
   */
  boolean isEffective();

  /**
   * @pre  isEffective();
   */
  double getdouble();

  void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat);

}

