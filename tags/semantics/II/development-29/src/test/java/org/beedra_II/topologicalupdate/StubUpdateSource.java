/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.topologicalupdate;


public final class StubUpdateSource extends AbstractStubUpdateSource {

  public final static int MRUSD = 0;

  public StubUpdateSource() {
    $mrusd = MRUSD;
  }

  public StubUpdateSource(int mrosd) {
    $mrusd = mrosd;
  }

  public int $mrusd;

  public int getMaximumRootUpdateSourceDistance() {
    return $mrusd;
  }

}
