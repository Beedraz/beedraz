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

package org.beedra_II.property.decimal;


import static org.ppeew.smallfries.MultiLineToStringUtil.indent;

import org.beedra_II.edit.Edit;
import org.beedra_II.property.simple.ActualOldNewEvent;
import org.ppeew.annotations.vcs.CvsInfo;


/**
 * This is the event that is actually send by actual Double beeds.
 * Users should probably use the interface {@link DoubleBeed}.
 *
 * @author  Jan Dockx
 * @author  Nele Smeets
 * @author  PeopleWare n.v.
 *
 *
 * @invar getSource() instanceof DoubleBeed;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
final class ActualDoubleEvent
    extends ActualOldNewEvent<Double>
    implements DoubleEvent {

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
  public ActualDoubleEvent(DoubleBeed<?> source, Double oldValue, Double newValue, Edit<?> edit) {
    super(source, oldValue, newValue, edit);
    $delta = ((oldValue == null) || (newValue == null)) ? null : newValue - oldValue; // MUDO overflow
  }


  public final Double getNewDouble() {
    return getNewValue();
  }

  public final Double getOldDouble() {
    return getOldValue();
  }

  /**
   * @return (getOldValue() == null) || (getNewValue() == null)
   *             ? null
   *             : getNewValue() - getOldValue();
   */
  public final Double getDoubleDelta() {
    return $delta;
  }

  private final Double $delta;

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", delta: " + getDoubleDelta();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "delta:" + getDoubleDelta() + "\n");
  }

}

