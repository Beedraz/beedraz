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

package org.beedra_II.property.association;


import static org.beedra.util_I.MultiLineToStringUtil.indent;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.event.Event;
import org.beedra_II.property.simple.OldNewEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * {@link Event} that carries a simple old and new value,
 * expressing the changed that occured in {@link #getSource()}.
 * The {@link #getSource() source} must be a {@link SimplePB}.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof EditableBidirToOne<_One_, _Many_>;
 * @invar getEdit() instanceof BidirToOneEdit;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class BidirToOneEvent<_One_ extends BeanBeed>
    extends OldNewEvent<_One_> {

  /**
   * @pre source != null;
   * @pre (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @post getSource() == source;
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   */
  public BidirToOneEvent(EditableBidirToOneBeed<_One_, ?> source,
                         _One_ oldValue,
                         _One_ newValue,
                         BidirToOneEdit<_One_, ?> edit) {
    super(source, oldValue, newValue, edit);
  }

  @Override
  protected void toStringOldNew(StringBuffer sb, int level) {
    sb.append(indent(level) + "old value:");
    if (getOldValue() == null) {
      sb.append(" null\n");
    }
    else {
      sb.append("\n");
      getOldValue().toString(sb, level + 1);
    }
    sb.append(indent(level) + "new value:");
    if (getNewValue() == null) {
      sb.append(" null\n");
    }
    else {
      sb.append("\n");
      getNewValue().toString(sb, level + 1);
    }
  }

}

