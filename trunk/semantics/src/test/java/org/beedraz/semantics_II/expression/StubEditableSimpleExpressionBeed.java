package org.beedraz.semantics_II.expression;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.StubEvent;
import org.beedraz.semantics_II.TopologicalUpdateAccess;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
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
   */
  public void publicTopologicalUpdateStart(StubEvent event) {
    TopologicalUpdateAccess.topologicalUpdate(this, event);
  }

}

