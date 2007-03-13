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


import org.beedra_II.property.simple.SimplePropertyEdit;
import org.ppeew.annotations.vcs.CvsInfo;


/**
 * An {@link Edit} for a beed of type {@link EditableDoubleBeed}.
 * This edit can change the value of the target beed, and send
 * {@link DoubleEvent events} to the listeners of that beed.
 *
 * @author  Nele Smeets
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class DoubleEdit
    extends SimplePropertyEdit<Double, EditableDoubleBeed, DoubleEvent> {

  /**
   * @pre  target != null;
   * @post getTarget() == target;
   */
  public DoubleEdit(EditableDoubleBeed target) {
    super(target);
  }

  /**
   * @post  result.getSource() == getTarget();
   * @post  result.getOldValue() == getOldValue();
   * @post  result.getNewValue() == getNewValue();
   * @post  result.getEdit() == this;
   */
  @Override
  protected DoubleEvent createEvent() {
    return new ActualDoubleEvent(getTarget(), getOldValue(), getNewValue(), this);
  }

}


