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


import org.beedra_II.property.simple.SimplePropertyEdit;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class IntegerEdit
    extends SimplePropertyEdit<Integer, EditableIntegerBeed, IntegerEvent> {

  /**
   * @pre  target != null;
   * @post getTarget() == target;
   */
  public IntegerEdit(EditableIntegerBeed target) {
    super(target);
  }

  /**
   * @post  result.getSource() == getTarget();
   * @post  result.getOldValue() == getOldValue();
   * @post  result.getNewValue() == getNewValue();
   * @post  result.getEdit() == this;
   */
  @Override
  protected IntegerEvent createEvent() {
    return new ActualIntegerEvent(getTarget(), getOldValue(), getNewValue(), this);
  }

}


