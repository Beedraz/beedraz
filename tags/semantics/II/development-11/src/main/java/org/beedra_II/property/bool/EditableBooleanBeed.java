package org.beedra_II.property.bool;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.simple.EditableSimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableBooleanBeed
    extends EditableSimplePropertyBeed<Boolean, BooleanEvent>
    implements BooleanBeed {

  /**
   * @pre owner != null;
   */
  public EditableBooleanBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected BooleanEvent createInitialEvent() {
    return new BooleanEvent(this, null, get(), null);
  }

}

