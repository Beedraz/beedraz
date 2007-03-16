/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.property.number.integer.long64;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.number.integer.long64.EditableLongBeed;
import org.beedra_II.property.number.integer.long64.LongEvent;


public class StubEditableLongBeed extends EditableLongBeed {

  public StubEditableLongBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * fireChangeEvent is made public for testing reasons
   */
  public void fire(LongEvent event) {
    fireChangeEvent(event);
  }

  @Override
  public boolean isAcceptable(Integer goal) {
    return goal != null && goal > 0;
  }

}

