package org.beedra_II.property.simple;


import org.beedra_II.StubEvent;
import org.beedra_II.property.EditableSimpleExpressionBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class StubEditableSimplePropertyBeed
    extends EditableSimpleExpressionBeed<Object, StubEvent> {

  /**
   * @pre owner != null;
   */
  public StubEditableSimplePropertyBeed() {
    super(null);
  }

  public Object publicSafeValueCopy(Object original) {
    return safeValueCopy(original);
  }

  public StubEvent $initialEvent = new StubEvent(this);

  /**
   * Makes the updateDependents method public for testing reasons.
   * @param event
   */
  public void publicUpdateDependents(StubEvent event) {
    updateDependents(event);
  }

}

