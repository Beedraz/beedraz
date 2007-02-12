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

package org.beedra_II.property.bool;


import org.beedra_II.event.Event;
import org.beedra_II.property.simple.OldNewEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * {@link Event} that carries a simple old and new value,
 * expressing the changed that occured in {@link #getSource()}.
 * The {@link #getSource() source} must be a {@link SimplePB}.
 * <p>Although a {@code boolean} has only 2 possible values, this
 *   event still has an old an a new value: for beeds, in general,
 *   {@code null} is a possible value too.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof IntegerBeed;
 * @invar getEdit() instanceof IntegerEdit;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class BooleanEvent extends OldNewEvent<Boolean, BooleanEdit> {

  /**
   * @pre source != null;
   * @pre edit != null;
   * @post getSource() == sourcel
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post getEdit() == edit;
   */
  public BooleanEvent(BooleanBeed source,
                     Boolean oldValue,
                     Boolean newValue,
                     BooleanEdit edit) {
    super(source, oldValue, newValue, edit);
  }

}

