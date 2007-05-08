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

package org.beedraz.semantics_II.expression.date;


import java.util.Date;

import org.beedraz.semantics_II.edit.Edit;
import org.beedraz.semantics_II.expression.SimpleExpressionEdit;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * An {@link Edit} for a beed of type {@link EditableDateBeed}.
 * This edit can change the value of the target beed, and send
 * {@link DateEvent events} to the listeners of that beed.
 *
 * @author  Nele Smeets
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class DateEdit
    extends SimpleExpressionEdit<Date, EditableDateBeed, DateEvent> {

  /**
   * @pre  target != null;
   * @post getTarget() == target;
   */
  public DateEdit(EditableDateBeed target) {
    super(target);
  }

  /**
   * @post  result.getSource() == getTarget();
   * @post  ComparisonUtil.equalsWithNull(result.getOldValue(), getOldValue());
   * @post  ComparisonUtil.equalsWithNull(result.getNewValue(), getNewValue());
   * @post  result.getEdit() == this;
   */
  @Override
  protected DateEvent createEvent() {
    return new DateEvent(getTarget(), getOldValue(), getNewValue(), this);
  }

}


