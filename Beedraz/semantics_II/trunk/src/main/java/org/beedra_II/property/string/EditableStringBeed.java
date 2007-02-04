package org.beedra_II.property.string;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.simple.EditableSimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableStringBeed
    extends EditableSimplePropertyBeed<String, StringEditEvent>
    implements StringBeed<StringEditEvent> {

  /**
   * @pre owner != null;
   */
  public EditableStringBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected StringEditEvent createInitialEvent() {
//    return new FinalIntegerEvent(this, null, get());
    return new StringEditEvent(null);
    // MUDO PROBLEM!!!!
  }

}

