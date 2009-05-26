/*<license>
Copyright 2007 - $Date: 2007-05-24 17:33:02 +0200 (do, 24 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.enumeration;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEvent;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.MathUtil;


/**
 * Abstract implementation of binary expression beeds, that represent a boolean value derived
 * from 2 operands of type {@link EnumBeed}.
 *
 * @author  Nele Smeets
 * @author  Jan Dockx
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date: 2007-05-24 17:33:02 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 920 $",
         date     = "$Date: 2007-05-24 17:33:02 +0200 (do, 24 mei 2007) $")
public abstract class AbstractEnumArgBooleanBinaryExpressionBeed<_Enum_ extends Enum<_Enum_>>
    extends AbstractEnumArgBinaryExprBeed<_Enum_, Boolean, BooleanEvent>
    implements BooleanBeed {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractEnumArgBooleanBinaryExpressionBeed(AggregateBeed owner) {
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
  protected final void recalculateFrom(EnumBeed<_Enum_> leftOperand, EnumBeed<_Enum_> rightOperand) {
    $value = calculateValue(leftOperand.get(), rightOperand.get());
  }

  protected abstract boolean calculateValue(_Enum_ leftOperand, _Enum_ rightOperand);

}

