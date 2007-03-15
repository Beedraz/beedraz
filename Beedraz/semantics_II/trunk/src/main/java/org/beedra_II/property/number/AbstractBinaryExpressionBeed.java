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


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.decimal.DoubleBeed;
import org.beedra_II.property.decimal.DoubleEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of binary expression number beeds, that represent a value derived
 * from 2 arguments.
 *
 * @invar getLeftArgument() == null || getRightArgument() == null ? get() == null;
 *
 * @protected
 * Accessor methods for the {@link #getLeftArgument() left argument} and the {@link #getRightArgument()
 * right argument} are kept protected, to force subclasses to provide meaningful public names for the
 * arguments.
 *
 * @invar getArgument() != null && valueFrom(getArgument()) == null ? get() == null;
 * @invar getArgument() != null && ! valueFrom(getArgument()) != null ?
 *              get().equals(calculateValue(valueFrom(getArgument())));
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBinaryExpressionBeed<_Number_ extends Number,
                                                   _LeftArgumentBeed_ extends DoubleBeed<_NumberEvent_>,
                                                   _RightArgumentBeed_ extends DoubleBeed<_NumberEvent_>,
                                                   _NumberEvent_ extends DoubleEvent>
    extends AbstractExpressionBeed<_Number_, _NumberEvent_>  {

  /**
   * @pre   owner != null;
   * @post  getArgument() == null;
   * @post  get() == null;
   */
  public AbstractBinaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  /*<property name="left argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final _LeftArgumentBeed_ getLeftArgument() {
    return $leftArgument;
  }

  /**
   * @post getLeftArgument() == leftArgument;
   */
  public final void setLeftArgument(_LeftArgumentBeed_ leftArgument) {
    _Number_ oldValue = get();
    if ($leftArgument != null) {
      $leftArgument.removeListener($leftArgumentListener);
    }
    $leftArgument = leftArgument;
    if ($leftArgument != null) {
      assignValue(calculateValueInternal(valueFromLeft($leftArgument), valueFromRight($rightArgument)));
      $leftArgument.addListener($leftArgumentListener);
    }
    else {
      assignValue(null);
    }
    fireEvent(oldValue, null);
  }

  /**
   * @pre beed != null;
   */
  protected abstract _Number_ valueFromLeft(_LeftArgumentBeed_ beed);

  private _LeftArgumentBeed_ $leftArgument;

  private Listener<_NumberEvent_> $leftArgumentListener = new Listener<_NumberEvent_>() {

    public void beedChanged(_NumberEvent_ event) {
      _Number_ oldValue = get();
      assignValue(calculateValueInternal(newValueFrom(event), valueFromRight($rightArgument)));
      fireEvent(oldValue, event.getEdit());
    }

  };

  /*</property>*/


  /*<property name="right argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final _RightArgumentBeed_ getRightArgument() {
    return $rightArgument;
  }

  /**
   * @post getRightArgument() == rightArgument;
   */
  public final void setRightArgument(_RightArgumentBeed_ rightArgument) {
    _Number_ oldValue = get();
    if ($rightArgument != null) {
      $rightArgument.removeListener($rightArgumentListener);
    }
    $rightArgument = rightArgument;
    if ($rightArgument != null) {
      assignValue(calculateValueInternal(valueFromLeft($leftArgument), valueFromRight($rightArgument)));
      $rightArgument.addListener($rightArgumentListener);
    }
    else {
      assignValue(null);
    }
    fireEvent(oldValue, null);
  }

  /**
   * @pre beed != null;
   */
  protected abstract _Number_ valueFromRight(_RightArgumentBeed_ beed);

  private _RightArgumentBeed_ $rightArgument;

  private Listener<_NumberEvent_> $rightArgumentListener = new Listener<_NumberEvent_>() {

    public void beedChanged(_NumberEvent_ event) {
      _Number_ oldValue = get();
      assignValue(calculateValueInternal(valueFromLeft($leftArgument), newValueFrom(event)));
      fireEvent(oldValue, event.getEdit());
    }

  };

  /*</property>*/


  private _Number_ calculateValueInternal(_Number_ leftArgumentValue, _Number_ rightArgumentValue) {
    return ((leftArgumentValue == null) || (rightArgumentValue == null)) ?
             null :
             calculateValue(leftArgumentValue, rightArgumentValue);
  }

  /**
   * @pre leftArgumentValue != null;
   * @pre rightArgumentValue != null;
   */
  protected abstract _Number_ calculateValue(_Number_ leftArgumentValue, _Number_ rightArgumentValue);

}

