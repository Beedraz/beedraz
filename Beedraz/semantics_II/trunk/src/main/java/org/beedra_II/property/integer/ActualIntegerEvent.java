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


import static org.beedra.util_I.MathUtil.castToDouble;
import static org.beedra.util_I.MultiLineToStringUtil.indent;

import org.beedra_II.edit.Edit;
import org.beedra_II.property.decimal.DoubleBeed;
import org.beedra_II.property.simple.OldNewEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * This is the event that is actually send by actual Double beeds.
 * Users should probably use the interface {@link DoubleBeed}.
 *
 * @author Jan Dockx
 *
 * @mudo should be package accesible; only public because it is used in a number of tests
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class ActualIntegerEvent
    extends OldNewEvent<Integer>
    implements IntegerEvent {

  /**
   * @pre  source != null;
   * @pre  (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @pre  (oldValue != null) && (newValue != null)
   *          ? ! oldValue.equals(newValue)
   *          : true;
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post oldValue == null || newValue == null
   *          ? getDelta() == null
   *          : getDelta() = newValue - oldValue;
   */
  public ActualIntegerEvent(IntegerBeed source, Integer oldValue, Integer newValue, Edit<?> edit) {
    super(source, oldValue, newValue, edit);
    $delta = ((oldValue == null) || (newValue == null)) ? null : newValue - oldValue; // MUDO overflow
  }

  public Integer getNewInteger() {
    return getNewValue();
  }

  public Double getNewDouble() {
    return castToDouble(getNewInteger());
  }

  public Integer getOldInteger() {
    return getOldValue();
  }

  public Double getOldDouble() {
    return castToDouble(getOldInteger());
  }

  public final Integer getIntegerDelta() {
    return $delta;
  }

  public Double getDoubleDelta() {
    return castToDouble(getIntegerDelta());
  }

  private final Integer $delta;

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", delta: " + getIntegerDelta();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "delta:" + getIntegerDelta() + "\n");
  }

}

