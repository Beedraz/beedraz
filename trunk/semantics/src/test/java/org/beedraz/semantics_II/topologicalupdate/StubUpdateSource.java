/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedraz.semantics_II.topologicalupdate;

import java.util.Collections;
import java.util.Set;

import org.beedraz.semantics_II.topologicalupdate.UpdateSource;


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

  public final Set<? extends UpdateSource> getUpdateSources() {
    return Collections.emptySet();
  }

  public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
    return Collections.emptySet();
  }

}
