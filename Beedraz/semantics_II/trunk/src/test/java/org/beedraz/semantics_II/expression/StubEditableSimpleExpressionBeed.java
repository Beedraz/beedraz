package org.beedraz.semantics_II.expression;


import org.beedraz.semantics_II.StubEvent;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.EditableSimpleExpressionBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class StubEditableSimpleExpressionBeed
    extends EditableSimpleExpressionBeed<Object, StubEvent> {

  /**
   * @pre  owner != null;
   * @post getOwner() == owner;
   */
  public StubEditableSimpleExpressionBeed(AggregateBeed owner) {
    super(owner);
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

