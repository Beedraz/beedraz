/*<license>
Copyright 2007 - $Date$ by the authors mentioned below.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.beedraz.semantics_II;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Static methods in this class incorporate the topological update algorithm.
 *
 * @author Jan Dockx
 *
 * @invar getMaximumRootUpdateSourceDistance() >= 0;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
class TopologicalUpdate {

  private TopologicalUpdate() {
    // NOP
  }

  private final static int TOPOLOGICAL_ORDER_ARRAY_INITIAL_SIZE = 15;

  /**
   * The topological update method. First change this update source locally,
   * then described the change in an event, then call this method with that event.
   *
   * @param edit
   *        The edit that causes this update. This may be {@code null},
   *        for structural changes.
   * @pre sourceEvents != null;
   * @pre sourceEvents.size() > 0;
   */
  static void topologicalUpdate(Map<AbstractBeed<?>, ? extends Event> sourceEvents, Edit<?> edit) {
    assert sourceEvents != null;
    assert sourceEvents.size() > 0;
    Map<Beed<?>, Event> events = new HashMap<Beed<?>, Event>(sourceEvents);
    /* most efficient structure to store the events to give them to the dependents
     * when they need to update themselves
     */
    ArrayList<HashSet<Dependent>> topologicalOrder = initialTopologicalOrder(sourceEvents);
      /* invar: topologicalOrder.get(0) = null;
       * invar: for (int i : [0, topologicalOrder.size()]) {for (Depenent<?> d : topologicalOrder.get(i)) {d.getMursd() == 1}};
       */
    topologicalUpdate(topologicalOrder, events, edit);
    fireInitialUpdateSourcesEvents(sourceEvents);
    fireDependentEvents(topologicalOrder, events);
  }

  private static ArrayList<HashSet<Dependent>> initialTopologicalOrder(Map<? extends Beed<?>, ? extends Event> sourceEvents) {
    assert sourceEvents != null;
    ArrayList<HashSet<Dependent>> topologicalOrder = new ArrayList<HashSet<Dependent>>(TOPOLOGICAL_ORDER_ARRAY_INITIAL_SIZE);
    for (Beed<?> aus : sourceEvents.keySet()) {
      assert aus != null;
      for (Dependent d : aus.getDependents()) {
        putDependent(topologicalOrder, d);
      }
    }
    return topologicalOrder;
  }

  private static void putDependent(ArrayList<HashSet<Dependent>> topologicalOrder, Dependent d) {
    assert topologicalOrder != null;
    assert d != null;
    int dMrusd = d.getMaximumRootUpdateSourceDistance();
    topologicalOrder.ensureCapacity(dMrusd + 1);
    while (topologicalOrder.size() <= dMrusd) {
      // make list large enough; stupid, but that's what it is
      topologicalOrder.add(null);
    }
    assert topologicalOrder.size() > dMrusd;
    HashSet<Dependent> dMrusdDependents = topologicalOrder.get(dMrusd);
    if (dMrusdDependents == null) {
      dMrusdDependents = new HashSet<Dependent>();
      topologicalOrder.set(dMrusd, dMrusdDependents);
    }
    dMrusdDependents.add(d);
  }

  private static void topologicalUpdate(ArrayList<HashSet<Dependent>> topologicalOrder, Map<Beed<?>, Event> events, Edit<?> edit) {
    int mrusd = 1; // 0 is not used
    while (mrusd < topologicalOrder.size()) {
      HashSet<Dependent> currentDependents = topologicalOrder.get(mrusd);
      if ((currentDependents != null) && (! currentDependents.isEmpty())) {
        Iterator<Dependent> iter = currentDependents.iterator();
        while (iter.hasNext()) {
          Dependent currentDependent = iter.next();
          long starttime = 0;
          if (TopologicalUpdateTiming._active) {
            starttime = System.nanoTime();
          }
          Event event = currentDependent.update(events, edit); // this is the actual update request
          if (TopologicalUpdateTiming._active) {
            long endtime = System.nanoTime();
            TopologicalUpdateTiming.add(currentDependent, starttime, endtime, event);
          }
          if (event != null) {
            // remember the event, for when we ask the dependents of d to update
            events.put(currentDependent.getDependentUpdateSource(), event);
            // add dependents of d to topological order
            Set<Dependent> currentDependentDependents = currentDependent.getDependents();
            for (Dependent d2 : currentDependentDependents) {
              putDependent(topologicalOrder, d2);
            }
          }
          else {
            /* We will not ask d to send events; remove it from the set.
               the set may stay. */
            iter.remove();
          }
        }
      }
      mrusd++;
    }
  }

  private static void fireInitialUpdateSourcesEvents(Map<AbstractBeed<?>, ? extends Event> sourceEvents) {
    for (Map.Entry<AbstractBeed<?>, ? extends Event> entry : sourceEvents.entrySet()) {
      entry.getKey().fireEvent(entry.getValue());
    }
  }

  private static void fireDependentEvents(ArrayList<HashSet<Dependent>> topologicalOrder, Map<Beed<?>, Event> events) {
    for (Set<Dependent> ds : topologicalOrder) {
      if (ds != null) {
        for (Dependent d: ds) {
          Event event = events.get(d.getDependentUpdateSource());
          d.fireEvent(event);
        }
      }
    }
  }

}
