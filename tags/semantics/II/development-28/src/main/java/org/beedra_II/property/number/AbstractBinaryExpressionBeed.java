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
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.RealEvent;
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
                                                   _LeftArgumentBeed_ extends RealBeed<? extends _LeftArgumentEvent_>,
                                                   _LeftArgumentEvent_ extends RealEvent,
                                                   _RightArgumentBeed_ extends RealBeed<? extends _RightArgumentEvent_>,
                                                   _RightArgumentEvent_ extends RealEvent,
                                                   _NumberEvent_ extends RealEvent>
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
  protected final _LeftArgumentBeed_ getLeftArgument() {
    return $leftArgument;
  }

  /**
   * @post getLeftArgument() == leftArgument;
   */
  protected final void setLeftArgument(_LeftArgumentBeed_ leftArgument) {
    _Number_ oldValue = get();
    if ($leftArgument != null) {
      $leftArgument.removeListener($leftArgumentListener);
    }
    $leftArgument = leftArgument;
    if (($leftArgument != null) && ($rightArgument != null)) {
      assignValue(calculateValueInternal(valueFromLeft($leftArgument), valueFromRight($rightArgument)));
    }
    else {
      assignValue(null);
    }
    if ($leftArgument != null) {
      $leftArgument.addListener($leftArgumentListener);
    }
    fireEvent(oldValue, null);
  }

  /**
   * @pre beed != null;
   */
  protected abstract _Number_ valueFromLeft(_LeftArgumentBeed_ beed);

  private _LeftArgumentBeed_ $leftArgument;

  private Listener<_LeftArgumentEvent_> $leftArgumentListener = new Listener<_LeftArgumentEvent_>() {

    public void beedChanged(_LeftArgumentEvent_ event) {
      if ($rightArgument != null) {
        _Number_ oldValue = get();
        assignValue(calculateValueInternal(newValueFromLeft(event), valueFromRight($rightArgument)));
        fireEvent(oldValue, event.getEdit());
      }
    }

  };

  protected abstract _Number_ newValueFromLeft(_LeftArgumentEvent_ event);

  /*</property>*/


  /*<property name="right argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  protected final _RightArgumentBeed_ getRightArgument() {
    return $rightArgument;
  }

  /**
   * @post getRightArgument() == rightArgument;
   */
  protected final void setRightArgument(_RightArgumentBeed_ rightArgument) {
    _Number_ oldValue = get();
    if ($rightArgument != null) {
      $rightArgument.removeListener($rightArgumentListener);
    }
    $rightArgument = rightArgument;
    if (($leftArgument != null) && ($rightArgument != null)) {
      assignValue(calculateValueInternal(valueFromLeft($leftArgument), valueFromRight($rightArgument)));
    }
    else {
      assignValue(null);
    }
    if ($rightArgument != null) {
      $rightArgument.addListener($rightArgumentListener);
    }
    fireEvent(oldValue, null);
  }

  /**
   * @pre beed != null;
   */
  protected abstract _Number_ valueFromRight(_RightArgumentBeed_ beed);

  private _RightArgumentBeed_ $rightArgument;

  private Listener<_RightArgumentEvent_> $rightArgumentListener = new Listener<_RightArgumentEvent_>() {

    public void beedChanged(_RightArgumentEvent_ event) {
      if ($leftArgument != null) {
        _Number_ oldValue = get();
        assignValue(calculateValueInternal(valueFromLeft($leftArgument), newValueFromRight(event)));
        fireEvent(oldValue, event.getEdit());
      }
    }

  };

  protected abstract _Number_ newValueFromRight(_RightArgumentEvent_ event);

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

