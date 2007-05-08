package org.beedra_II.property.date;


import java.util.Date;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.simple.EditableSimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * An editable beed containing a {@link Date} value.
 * Listeners of the beed can receive events of type
 * {@link DateEvent}.
 *
 * @author  Nele Smeets
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableDateBeed
    extends EditableSimplePropertyBeed<Date, DateEvent>
    implements DateBeed {

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   */
  public EditableDateBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldValue() == null;
   * @post  Comparison.equalsWithNull(result.getNewValue(), get());
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected DateEvent createInitialEvent() {
    return new DateEvent(this, null, get(), null);
  }

  @Override
  protected Date safeValueCopy(Date original) {
    return original != null ? (Date) original.clone() : null;
  }

}

