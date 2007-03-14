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


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.decimal.DoubleBeed;
import org.beedra_II.property.decimal.DoubleEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * Abstract implementation of number beeds that represent the the negative
 * of an {@link #getArgument() argument}.
 *
 * @invar getArgument() == null ? getInteger() == null;
 * @invar getArgument() != null ? getInteger() == - getArgument().getInteger();
 *
 * @mudo overflow: -MIN_VALUE == MIN_VALUE
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractNegativeBeed<_NumberType_ extends Number,
                                           _NumberBeed_ extends DoubleBeed<_NumberEvent_>,
                                           _NumberEvent_ extends DoubleEvent>
    extends AbstractPropertyBeed<_NumberEvent_>
    implements DoubleBeed<_NumberEvent_> {

  /**
   * @pre   owner != null;
   * @post  getInteger() == null;
   * @post  getArgument() == null;
   */
  public AbstractNegativeBeed(AggregateBeed owner) {
    super(owner);
  }

  /*<property name="argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final _NumberBeed_ getArgument() {
    return $argument;
  }

  /**
   * @post getArgument() == argument;
   */
  public final void setArgument(_NumberBeed_ argument) {
    _NumberType_ oldValue = $value;
    if ($argument != null) {
      $argument.removeListener($argumentListener);
    }
    $argument = argument;
    if ($argument != null) {
      $value = calculateValueInternal(valueFrom($argument));
      $argument.addListener($argumentListener);
    }
    else {
      $value = null;
    }
    fireEvent(oldValue, null);
  }

  /**
   * @pre beed != null;
   */
  protected abstract _NumberType_ valueFrom(_NumberBeed_ beed);

  /**
   * @pre event != null;
   */
  protected abstract _NumberType_ newValueFrom(_NumberEvent_ event);

  private void fireEvent(_NumberType_ oldValue, Edit<?> edit) {
    if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
      fireChangeEvent(createNewEvent(oldValue, $value, edit));
    }
  }

  protected abstract _NumberEvent_ createNewEvent(_NumberType_ oldValue, _NumberType_ newValue, Edit<?> edit);

  private _NumberBeed_ $argument;

  private Listener<_NumberEvent_> $argumentListener = new Listener<_NumberEvent_>() {

    public void beedChanged(_NumberEvent_ event) {
      _NumberType_ oldValue = $value;
      $value = calculateValueInternal(newValueFrom(event));
      fireEvent(oldValue, event.getEdit());
    }

  };

  /*</property>*/


  public final _NumberType_ get() {
    return $value;
  }

  private _NumberType_ calculateValueInternal(_NumberType_ argumentValue) {
    return argumentValue == null ? null : calculateValue(argumentValue);
  }

  /**
   * @pre argumentValue != null;
   */
  protected abstract _NumberType_ calculateValue(_NumberType_ argumentValue);

  private _NumberType_ $value;

  @Override
  protected final String otherToStringInformation() {
    return get() == null ? "null" : get().toString();
  }

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
  }

}

