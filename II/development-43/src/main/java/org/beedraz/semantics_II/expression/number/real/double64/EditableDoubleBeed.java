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
import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.EditableSimpleExpressionBeed;
import org.beedraz.semantics_II.expression.number.real.RealEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A editable beed containing a {@link Double} value.
 * Listeners of the beed can receive events of type
 * {@link RealEvent}.
 *
 * @author  Nele Smeets
 *
 * @mudo break link with editable simple property beed and use double instead of Double internally.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class EditableDoubleBeed
    extends EditableSimpleExpressionBeed<Double, ActualDoubleEvent>
    implements DoubleBeed {

  /**
   * @pre   owner != null;
   * @post  owner.registerAggregateElement(this);
   */
  public EditableDoubleBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @return get();
   */
  public final Double getDouble() {
    return get();
  }

  public final BigDecimal getBigDecimal() {
    return castToBigDecimal(getDouble());
  }

  public double getdouble() {
    return get().doubleValue();
  }

  public boolean isEffective() {
    return get() != null;
  }

  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append(numberFormat.format(get()));
  }

}

