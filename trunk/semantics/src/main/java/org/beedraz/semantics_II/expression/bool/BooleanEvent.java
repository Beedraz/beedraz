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

package org.beedraz.semantics_II.expression.bool;


import org.beedraz.semantics_II.ActualOldNewEvent;
import org.beedraz.semantics_II.edit.Edit;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * {@link OldNewEvent} whose source is a {@link BooleanBeed} and
 * that carries a simple old and new value of type {@link Boolean}.
 * <p>Although a {@code boolean} has only 2 possible values, this
 *   event still has an old an a new value: for beeds, in general,
 *   {@code null} is a possible value too.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof BooleanBeed;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class BooleanEvent extends ActualOldNewEvent<Boolean> {

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
   */
  public BooleanEvent(BooleanBeed source,
                     Boolean oldValue,
                     Boolean newValue,
                     Edit<?> edit) {
    super(source, oldValue, newValue, edit);
  }

}

