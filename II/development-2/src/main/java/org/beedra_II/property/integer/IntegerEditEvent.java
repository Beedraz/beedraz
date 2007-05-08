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


import static org.beedra.util_I.MultiLineToStringUtil.indent;

import org.beedra_II.event.Event;
import org.beedra_II.property.simple.SimpleEditEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * {@link Event} that carries a simple old and new value,
 * expressing the changed that occured in {@link #getSource()}.
 * The {@link #getSource() source} must be a {@link SimplePB}.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof IntegerBeed;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class IntegerEditEvent
    extends SimpleEditEvent<Integer>
    implements IntegerEvent {

  /**
   * @pre source != null;
   * @post getSource() == sourcel
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   */
  public IntegerEditEvent(IntegerEdit edit) {
    super(edit);
//    (getNewValue() - getOldValue() < Integer.MIN_VALUE) || (getNewValue() - getOldValue() > Integer.MAX_VALUE)
//    if ((getOldValue() == null) || (getNewValue() == null)) {
//      $delta = null;
//    }
//    else if (getOldValue() > 0) { // only MIN_VALUE problem is possible
//      if (getNewValue() < Integer.MIN_VALUE + getOldValue()) {
//        $delta = getNewValue() - getOldValue(); // ok
//      }
//      else {
//        $delta = null;
//      }
//    }
//    else {
//      assert getOldValue() <= 0;
//      // only MAX_VALUE problem is possible
//      if (getNewValue() > Integer.MAX_VALUE + getOldValue()) {
//        $delta = getNewValue() - getOldValue(); // ok
//      }
//      else {
//        $delta = null;
//      }
//    }
    $delta = ((getOldValue() == null) || (getNewValue() == null) ||
              ((getOldValue() > 0) && (getNewValue() >= Integer.MIN_VALUE + getOldValue())) ||
              ((getOldValue() <= 0) && (getNewValue() <= Integer.MAX_VALUE + getOldValue()))) ?
               null :
               getNewValue() - getOldValue();
  }

  /**
   * @return ((getOldValue() == null) || (getNewValue() == null) ||
   *          ((getOldValue() > 0) && (getNewValue() >= Integer.MIN_VALUE + getOldValue())) ||
   *          ((getOldValue() <= 0) && (getNewValue() <= Integer.MAX_VALUE + getOldValue()))) ?
   *           null :
   *           new Integer(getNewValue() - getOldValue());
   */
  public final Integer getDelta() {
    return $delta;
  }

  private final Integer $delta;

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", delta: " + getDelta();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "delta: " + getDelta() + "\n");
  }

}

