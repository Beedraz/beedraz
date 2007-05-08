package org.beedra_II.property.integer;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.simple.EditableSimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableIntegerBeed
    extends EditableSimplePropertyBeed<Integer, IntegerEvent>
    implements IntegerBeed {

  /**
   * @pre owner != null;
   */
  public EditableIntegerBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldValue() == null;
   * @post  result.getNewValue() == get();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected IntegerEvent createInitialEvent() {
    return new IntegerEvent(this, null, get(), null);
  }

//  @Override
//  protected IntegerEvent createEvent(Edit<?> edit) {
//    return new IntegerEvent(this, edit.getOldValue(), edit.getNewValue(), edit);
//  }

}

