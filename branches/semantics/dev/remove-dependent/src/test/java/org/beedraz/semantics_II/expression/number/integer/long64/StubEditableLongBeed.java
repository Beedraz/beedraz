/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedraz.semantics_II.expression.number.integer.long64;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.TopologicalUpdateAccess;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class StubEditableLongBeed extends EditableLongBeed {

  public StubEditableLongBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * Makes the updateDependents method public for testing reasons.
   */
  public void publicTopologicalUpdateStart(ActualLongEvent event) {
    TopologicalUpdateAccess.topologicalUpdate(this, event);
  }

  @Override
  public boolean isAcceptable(Long goal) {
    return goal != null && goal > 0;
  }

}

