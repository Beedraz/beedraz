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

package org.beedra_II.property.association.set.sorted;


import static org.beedra.util_I.MultiLineToStringUtil.indent;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.property.simple.OldNewEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * {@link OldNewEvent} whose source is a {@link EditableSortedBidirToOneBeed<_One_, _Many_>} and
 * that carries a simple old and new value of type {@link SortedBidirToManyBeed<_One_, _Many_>}.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar getSource() instanceof EditableSortedBidirToOneBeed<_One_, _Many_>;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class SortedBidirToOneEvent<_One_ extends BeanBeed,
                                         _Many_ extends BeanBeed>
    extends OldNewEvent<SortedBidirToManyBeed<_One_, _Many_>> {


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
  public SortedBidirToOneEvent(EditableSortedBidirToOneBeed<_One_, _Many_> source,
                               SortedBidirToManyBeed<_One_, _Many_> oldValue,
                               SortedBidirToManyBeed<_One_, _Many_> newValue,
                               Edit<?> edit) {
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
      sb.append(indent(level + 1) + "owner:\n");
      getOldValue().getOwner().toString(sb, level + 2);
    }
    sb.append(indent(level) + "new value:");
    if (getNewValue() == null) {
      sb.append(" null\n");
    }
    else {
      sb.append("\n");
      getNewValue().toString(sb, level + 1);
      sb.append(indent(level + 1) + "owner:\n");
      getNewValue().getOwner().toString(sb, level + 2);
    }
  }

}

