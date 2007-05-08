/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.property.integer;


import org.beedra_II.aggregate.AggregateBeed;


public class StubEditableIntegerBeed extends EditableIntegerBeed {

  public StubEditableIntegerBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * fireChangeEvent is made public for testing reasons
   */
  public void fire(IntegerEvent event) {
    fireChangeEvent(event);
  }

  @Override
  public boolean isAcceptable(Integer goal) {
    return goal != null && goal > 0;
  }

}

