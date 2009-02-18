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

package org.beedraz.semantics_II.expression.association.set;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.indent;

import org.beedraz.semantics_II.ActualOldNewEvent;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.OldNewEvent;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * {@link OldNewEvent} whose source is a {@link EditableBidirToOneBeed} and
 * that carries a simple old and new value of type {@link BidirToManyBeed}.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof EditableBidirToOneBeed<_One_, _Many_>;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class BidirToOneEvent<_One_ extends BeanBeed,
                                   _Many_ extends BeanBeed>
    extends ActualOldNewEvent<BidirToManyBeed<_One_, _Many_>> {


  /**
   * @pre  source != null;
   * @pre (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
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
  public BidirToOneEvent(EditableBidirToOneBeed<_One_, _Many_> source,
                         BidirToManyBeed<_One_, _Many_> oldValue,
                         BidirToManyBeed<_One_, _Many_> newValue,
                         Edit<?> edit) {
    super(source, oldValue, newValue, edit);
  }

  /**
   * @return getOldValue().getOwner();
   */
  public final _One_ getOldOne() {
    return (getOldValue() == null) ? null : getOldValue().getOwner();
  }

  /**
   * @return getNewValue().getOwner();
   */
  public final _One_ getNewOne() {
    return (getNewValue() == null) ? null : getNewValue().getOwner();
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

