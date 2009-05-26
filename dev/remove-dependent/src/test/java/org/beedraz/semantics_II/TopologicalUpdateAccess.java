/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedraz.semantics_II;

import java.util.HashMap;
import java.util.Map;


/**
 * Access for tests to package accessible methods.
 */
public class TopologicalUpdateAccess {

  public static void topologicalUpdate(AbstractBeed<?> us, Event event) {
    assert event != null;
    HashMap<AbstractBeed<?>, Event> sourceEvents = new HashMap<AbstractBeed<?>, Event>(1);
    sourceEvents.put(us, event);
    TopologicalUpdate.topologicalUpdate(sourceEvents, null);
  }

  public static void topologicalUpdate(Map<AbstractBeed<?>, ? extends Event> sourceEvents, Edit<?> edit) {
    assert sourceEvents != null;
    TopologicalUpdate.topologicalUpdate(sourceEvents, edit);
  }

}

