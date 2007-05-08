/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedraz.semantics_II.expression.number.integer.long64;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.ActualLongEvent;
import org.beedraz.semantics_II.expression.number.integer.long64.EditableLongBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class StubEditableLongBeed extends EditableLongBeed {

  public StubEditableLongBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * updateDependents is made public for testing reasons
   */
  public void publicUpdateDependents(ActualLongEvent event) {
    updateDependents(event);
  }

  @Override
  public boolean isAcceptable(Long goal) {
    return goal != null && goal > 0;
  }

}

