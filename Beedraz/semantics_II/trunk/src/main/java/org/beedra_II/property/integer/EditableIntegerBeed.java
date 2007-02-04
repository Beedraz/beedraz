package org.beedra_II.property.integer;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.simple.EditableSimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableIntegerBeed
    extends EditableSimplePropertyBeed<Integer, IntegerEditEvent>
    implements IntegerBeed<IntegerEditEvent> {

  /**
   * @pre owner != null;
   */
  public EditableIntegerBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected IntegerEditEvent createInitialEvent() {
//    return new FinalIntegerEvent(this, null, get());
    return new IntegerEditEvent(null);
    // MUDO PROBLEM!!!!
  }

//  protected void fireEdit(IntegerEdit edit) {
//    fireChangeEvent(new IntegerEditEvent(edit));
//  }
//
//  @Override
//  protected void fireEdit(Edit<?> edit) {
//    fireChangeEvent(new IntegerEditEvent(edit));
//  }
//

}

