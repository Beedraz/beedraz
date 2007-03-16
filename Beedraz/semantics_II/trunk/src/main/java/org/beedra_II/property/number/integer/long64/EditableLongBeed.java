package org.beedra_II.property.number.integer.long64;


import static org.ppeew.smallfries_I.MathUtil.castToDouble;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.simple.EditableSimplePropertyBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableLongBeed
    extends EditableSimplePropertyBeed<Integer, LongEvent>
    implements LongBeed {

  /**
   * @pre owner != null;
   */
  public EditableLongBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldInteger() == null;
   * @post  result.getNewInteger() == get();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected LongEvent createInitialEvent() {
    return new ActualLongEvent(this, null, get(), null);
  }

  public Double getDouble() {
    return castToDouble(getInteger());
  }

  public Integer getInteger() {
    return get();
  }

}

