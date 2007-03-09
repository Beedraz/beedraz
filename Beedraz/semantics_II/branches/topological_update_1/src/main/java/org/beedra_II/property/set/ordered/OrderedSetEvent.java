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

package org.beedra_II.property.set.ordered;


import java.util.List;

import org.beedra_II.edit.Edit;
import org.beedra_II.property.simple.OldNewEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * {@link OldNewEvent} whose source is a {@link OrderedSetBeed} and
 * that carries a simple old and new value of type {@link List}.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar getSource() instanceof OrderedSetBeed<_Element_>;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class OrderedSetEvent<_Element_> extends OldNewEvent<List<_Element_>> {

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
  public OrderedSetEvent(OrderedSetBeed<_Element_> source,
                   List<_Element_> oldValue,
                   List<_Element_> newValue,
                   Edit<?> edit) {
    super(source, oldValue, newValue, edit);
  }

}

