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

package org.beedraz.semantics_II.expression.bool;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.number.real.AbstractRealArgBinaryExprBeed;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.expression.number.real.RealEvent;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * Abstract implementation of binary expression beeds, that represent a boolean value derived
 * from 2 operands of type {@link RealBeed}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractRealArgBooleanBinaryExpressionBeed
    extends AbstractRealArgBinaryExprBeed<
                                   Boolean,
                                   BooleanEvent,
                                   RealBeed<?>,
                                   RealEvent,
                                   RealBeed<?>,
                                   RealEvent>
    implements BooleanBeed {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractRealArgBooleanBinaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  public final Boolean getBoolean() {
    return isEffective() ? Boolean.valueOf(getboolean()) : null;
  }

  @Override
  protected boolean equalValue(Boolean b1, Boolean b2) {
    return MathUtil.equalValue(b1, b2);
  }

  public final boolean getboolean() {
    return $value;
  }

  private boolean $value;

  @Override
  public final Boolean get() {
    return getBoolean();
  }

  @Override
  protected final BooleanEvent createNewEvent(Boolean oldValue, Boolean newValue, Edit<?> edit) {
    return new BooleanEvent(this, oldValue, newValue, edit);
  }

  @Override
  protected final void recalculateFrom(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    $value = calculateValue(leftOperand.getdouble(), rightOperand.getdouble());
  }

  protected abstract boolean calculateValue(double leftOperand, double rightOperand);

}

