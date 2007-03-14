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

package org.beedra_II.property.integer;


import static org.ppeew.smallfries_I.MathUtil.castToDouble;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * A beed that is the negative of a {@link #getArgument() argument} {@link IntegerBeed}.
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
public class IntegerNegativeBeed
    extends AbstractPropertyBeed<IntegerEvent>
    implements IntegerBeed {

  /**
   * @pre   owner != null;
   * @post  getInteger() == null;
   * @post  getArgument() == null;
   */
  public IntegerNegativeBeed(AggregateBeed owner) {
    super(owner);
  }

  /*<property name="argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final IntegerBeed getArgument() {
    return $argument;
  }

  /**
   * @post getArgument() == argument;
   */
  public final void setArgument(IntegerBeed argument) {
    Integer oldValue = $value;
    if ($argument != null) {
      $argument.removeListener($argumentListener);
    }
    $argument = argument;
    if ($argument != null) {
      $value = calculateValueInternal($argument.getInteger());
      $argument.addListener($argumentListener);
    }
    else {
      $value = null;
    }
    fireEvent(oldValue);
  }

  private void fireEvent(Integer oldValue) {
    if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
      fireChangeEvent(new ActualIntegerEvent(this, oldValue, $value, null));
    }
  }

  private IntegerBeed $argument;

  private Listener<IntegerEvent> $argumentListener = new Listener<IntegerEvent>() {

    public void beedChanged(IntegerEvent event) {
      Integer oldValue = $value;
      $value = calculateValueInternal(event.getNewInteger());
      fireEvent(oldValue);
    }

  };

  /*</property>*/



  public final Double getDouble() {
    return castToDouble(getInteger());
  }

  public final Integer getInteger() {
    return $value;
  }

  private Integer calculateValueInternal(Integer argumentValue) {
    return argumentValue == null ? null : calculateValue(argumentValue);
  }

  /**
   * @pre argumentValue != null;
   */
  protected Integer calculateValue(Integer argumentValue) {
    assert argumentValue != null;
    return -argumentValue;
  }

  private Integer $value;

  /**
   * @post  result != null;
   * @post  result.getArgument() == this;
   * @post  result.getOldInteger() == null;
   * @post  result.getNewInteger() == getInteger();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final IntegerEvent createInitialEvent() {
    return new ActualIntegerEvent(this, null, getInteger(), null);
  }


  @Override
  protected String otherToStringInformation() {
    return getInteger() == null ? "null" : getInteger().toString();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + getInteger() + "\n");
  }

}

