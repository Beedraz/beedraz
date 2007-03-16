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
import org.beedra_II.event.Listener;
import org.beedra_II.property.decimal.DoubleBeed;
import org.beedra_II.property.decimal.DoubleEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of unary expression number beeds, represent the a value derived
 * from an {@link #getArgument() argument}.
 *
 * @invar getArgument() == null ? get() == null;
 *
 * @protected
 * @invar getArgument() != null && valueFrom(getArgument()) == null ? get() == null;
 * @invar getArgument() != null && ! valueFrom(getArgument()) != null ?
 *              get().equals(calculateValue(valueFrom(getArgument())));
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractUnaryExpressionBeed<_Number_ extends Number,
                                                  _ArgumentBeed_ extends DoubleBeed<_NumberEvent_>,
                                                  _NumberEvent_ extends DoubleEvent>
    extends AbstractExpressionBeed<_Number_, _NumberEvent_>  {

  /**
   * @pre   owner != null;
   * @post  getArgument() == null;
   * @post  get() == null;
   */
  public AbstractUnaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  /*<property name="argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final _ArgumentBeed_ getArgument() {
    return $argument;
  }

  /**
   * @post getArgument() == argument;
   */
  public final void setArgument(_ArgumentBeed_ argument) {
    _Number_ oldValue = get();
    if ($argument != null) {
      $argument.removeListener($argumentListener);
    }
    $argument = argument;
    if ($argument != null) {
      assignValue(calculateValueInternal(valueFrom($argument)));
      $argument.addListener($argumentListener);
    }
    else {
      assignValue(null);
    }
    fireEvent(oldValue, null);
  }

  /**
   * @pre beed != null;
   */
  protected abstract _Number_ valueFrom(_ArgumentBeed_ beed);

  private _ArgumentBeed_ $argument;

  private Listener<_NumberEvent_> $argumentListener = new Listener<_NumberEvent_>() {

    public void beedChanged(_NumberEvent_ event) {
      _Number_ oldValue = get();
      assignValue(calculateValueInternal(newValueFrom(event)));
      fireEvent(oldValue, event.getEdit());
    }

  };

  /*</property>*/


  private _Number_ calculateValueInternal(_Number_ argumentValue) {
    return argumentValue == null ? null : calculateValue(argumentValue);
  }

  /**
   * @pre argumentValue != null;
   */
  protected abstract _Number_ calculateValue(_Number_ argumentValue);

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
    getArgument().toString(sb, level + 2);
  }

}

