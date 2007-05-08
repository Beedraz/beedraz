package org.beedra_II.property.decimal;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.simple.EditableSimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A editable beed containing a {@link Double} value.
 * Listeners of the beed can receive events of type
 * {@link DoubleEvent}.
 *
 * @author  Nele Smeets
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableDoubleBeed
    extends EditableSimplePropertyBeed<Double, DoubleEvent>
    implements DoubleBeed {

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   */
  public EditableDoubleBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected DoubleEvent createInitialEvent() {
    return new DoubleEvent(this, null, get(), null);
  }

//  @Override
//  protected DoubleEvent createEvent(Edit<?> edit) {
//    return new DoubleEvent(this, edit.getOldValue(), edit.getNewValue(), edit);
//  }

}

