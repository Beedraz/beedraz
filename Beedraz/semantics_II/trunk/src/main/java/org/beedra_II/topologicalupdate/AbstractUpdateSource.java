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

package org.beedra_II.topologicalupdate;


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.collection_I.WeakHashSet;
import org.ppeew.smallfries_I.MultiLineToStringUtil;


/**
 * @author Jan Dockx
 *
 * @invar getMaximumRootUpdateSourceDistance() >= 0;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractUpdateSource implements UpdateSource {

  public final boolean isDependent(Dependent<?> dependent) {
    return $dependents.contains(dependent);
  }

  public final boolean isTransitiveDependent(Dependent<?> dependent) {
    if (isDependent(dependent)) {
      return true;
    }
    else {
      for (Dependent<?> d : $dependents.strongClone()) {
        if (d.getDependentUpdateSource().isTransitiveDependent(dependent)) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Don't expose the collection of dependents publicly. It's
   * a secret shared between us and the dependent. This collection
   * uses strong references.
   *
   * @result for (Dependent d) {isDependent(d) ?? result.contains(d)};
   */
  protected final Set<Dependent<?>> getDependents() {
    return $dependents.strongClone();
  }

  /**
   * @pre dependent != null;
   * @pre dependent.getDependentUpdateSource() != this;
   * @pre ! getUpdateSourcesTransitiveClosure().contains(dependent.getDependentUpdateSource());
   */
  public final void addDependent(Dependent<?> dependent) {
    assert dependent != null;
    assert dependent.getDependentUpdateSource() != this;
    /* MUDO incredible slowdown, and -da doesn't work ???
    assert ! getUpdateSourcesTransitiveClosure().contains(dependent.getDependentUpdateSource());
    */
    assert dependent.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance();
    $dependents.add(dependent);
  }

  public final void removeDependent(Dependent<?> dependent) {
    $dependents.remove(dependent);
  }

  /**
   * The topological update method. First change this update source locally,
   * then described the change in an event, then call this method with that event.
   *
   * @pre event != null;
   */
  protected final void updateDependents(Event event) {
    updateDependents(this, event);
  }

  /**
   * The topological update method. First change this update source locally,
   * then described the change in an event, then call this method with that event.
   *
   * @param edit
   *        The edit that causes this update. This may be {@code null},
   *        for structural changes.
   * @pre us != null;
   * @pre event != null;
   */
  protected static void updateDependents(AbstractUpdateSource us, Event event) {
    HashMap<AbstractUpdateSource, Event> sourceEvents = new HashMap<AbstractUpdateSource, Event>(1);
    sourceEvents.put(us, event);
    multiUpdateDependents(sourceEvents, event.getEdit());
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
  protected static void multiUpdateDependents(Map<AbstractUpdateSource, Event> sourceEvents, Edit<?> edit) {
    assert sourceEvents != null;
    assert sourceEvents.size() > 0;
    Map<UpdateSource, Event> events = new HashMap<UpdateSource, Event>(sourceEvents);
    /* most efficient structure to store the events to give them to the dependents
     * when they need to update themselves
     */
    ArrayList<HashSet<Dependent<?>>> topologicalOrder = initialTopologicalOrder(sourceEvents);
      /* invar: topologicalOrder.get(0) = null;
       * invar: for (int i : [0, topologicalOrder.size()]) {for (Depenent<?> d : topologicalOrder.get(i)) {d.getMursd() == 1}};
       */
    topologicalUpdate(topologicalOrder, events, edit);
    fireInitialUpdateSourcesEvents(sourceEvents);
    fireDependentEvents(topologicalOrder, events);
    }

  private static void putDependent(ArrayList<HashSet<Dependent<?>>> topologicalOrder, Dependent<?> d) {
    assert topologicalOrder != null;
    assert d != null;
    int dMrusd = d.getMaximumRootUpdateSourceDistance();
    topologicalOrder.ensureCapacity(dMrusd + 1);
    while (topologicalOrder.size() <= dMrusd) {
      // make list large enough; stupid, but that's what it is
      topologicalOrder.add(null);
    }
    assert topologicalOrder.size() > dMrusd;
    HashSet<Dependent<?>> dMrusdDependents = topologicalOrder.get(dMrusd);
    if (dMrusdDependents == null) {
      dMrusdDependents = new HashSet<Dependent<?>>();
      topologicalOrder.set(dMrusd, dMrusdDependents);
    }
    dMrusdDependents.add(d);
  }

  private static ArrayList<HashSet<Dependent<?>>> initialTopologicalOrder(Map<AbstractUpdateSource, Event> sourceEvents) {
    assert sourceEvents != null;
    ArrayList<HashSet<Dependent<?>>> topologicalOrder = new ArrayList<HashSet<Dependent<?>>>(TOPOLOGICAL_ORDER_ARRAY_INITIAL_SIZE);
      for (AbstractUpdateSource aus : sourceEvents.keySet()) {
      assert aus != null;
      for (Dependent<?> d : aus.getDependents()) {
        putDependent(topologicalOrder, d);
      }
    }
    return topologicalOrder;
  }

  private static void topologicalUpdate(ArrayList<HashSet<Dependent<?>>> topologicalOrder, Map<UpdateSource, Event> events, Edit<?> edit) {
    int mrusd = 1; // 0 is not used
    while (mrusd < topologicalOrder.size()) {
      HashSet<Dependent<?>> currentDependents = topologicalOrder.get(mrusd);
      if ((currentDependents != null) && (! currentDependents.isEmpty())) {
        Iterator<Dependent<?>> iter = currentDependents.iterator();
        while (iter.hasNext()) {
          Dependent<?> currentDependent = iter.next();
        long starttime = 0;
        if (Timing._active) {
          starttime = System.nanoTime();
        }
          Event event = currentDependent.update(events, edit); // this is the actual update request
        if (Timing._active) {
          long endtime = System.nanoTime();
            Timing.add(currentDependent, starttime, endtime, event);
        }
          if (event != null) {
            // remember the event, for when we ask the dependents of d to update
            events.put(currentDependent.getDependentUpdateSource(), event);
            // add dependents of d to topological order
            Set<Dependent<?>> currentDependentDependents = currentDependent.getDependents();
            for (Dependent<?> d2 : currentDependentDependents) {
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

  private static void fireDependentEvents(ArrayList<HashSet<Dependent<?>>> topologicalOrder, Map<UpdateSource, Event> events) {
    for (Set<Dependent<?>> ds : topologicalOrder) {
      if (ds != null) {
        for (Dependent<?> d: ds) {
          Event event = events.get(d.getDependentUpdateSource());
          d.fireEvent(event);
        }
      }
    }
  }

  private static void fireInitialUpdateSourcesEvents(Map<AbstractUpdateSource, Event> sourceEvents) {
    for (Map.Entry<AbstractUpdateSource, Event> entry : sourceEvents.entrySet()) {
      entry.getKey().fireEvent(entry.getValue());
    }
    }


  public static class Timing {

    public static boolean _active = false;

    public static Timing[] resultsSortedByAverageDuration() {
      return resultsSorted(new Comparator<Timing>() {

        public int compare(Timing t1, Timing t2) {
          assert t1 != null;
          assert t2 != null;
          if (t1.getAverageDuration() > t2.getAverageDuration()) {
            return -1;
          }
          else if (t1.getAverageDuration() == t2.getAverageDuration()) {
            return 0;
          }
          else {
            return +1;
          }
        }

      });
    }

    public static Timing[] resultsSortedByTotalDuration() {
      return resultsSorted(new Comparator<Timing>() {

        public int compare(Timing t1, Timing t2) {
          assert t1 != null;
          assert t2 != null;
          if (t1.getTotalDuration() > t2.getTotalDuration()) {
            return -1;
          }
          else if (t1.getTotalDuration() == t2.getTotalDuration()) {
            return 0;
          }
          else {
            return +1;
          }
        }

      });
    }

    public static Timing[] resultsSorted(Comparator<Timing> comparator) {
      Timing[] result = new Timing[_results.size()];
      result = _results.values().toArray(result);
      Arrays.sort(result, comparator);
      return result;
    }

    public static Map<Dependent<?>, Timing> results() {
      return Collections.unmodifiableMap(_results);
    }

    private static Map<Dependent<?>, Timing> _results;

    public static void reset() {
      _active = true;
      _results = new HashMap<Dependent<?>, Timing>();
    }

    static void add(Dependent<?> dependent, long starttime, long endtime, Event event) {
      long duration = endtime - starttime;
      Timing timing = _results.get(dependent);
      if (timing == null) {
        timing = new Timing(dependent, duration, event);
        _results.put(dependent, timing);
      }
      else {
        timing.succ(duration);
      }
    }

    private Timing(Dependent<?> dependent, long duration, Event event) {
      $updateSource = dependent.getDependentUpdateSource();
      $duration = duration;
      $event = event;
    }

    final void succ(long duration) {
      $count++;
      $duration += duration;
    }

    public final int getCount() {
      return $count;
    }

    private int $count = 1;

    public final UpdateSource getUpdateSource() {
      return $updateSource;
    }

    private final UpdateSource $updateSource;

    public final long getTotalDuration() {
      return $duration;
    }

    public final double getAverageDuration() {
      return ((double)$duration) / ((double)$count);
    }

    private long $duration;

    public final Event getEvent() {
      return $event;
    }

    private final Event $event;

  }

  /**
   * Fire the event to regular (non-topological) listeners.
   */
  protected abstract void fireEvent(Event event);

  /**
   * @invar $dependents != null;
   * @invar Collections.noNull($dependents);
   */
  private final WeakHashSet<Dependent<?>> $dependents = new WeakHashSet<Dependent<?>>();

  public final Set<? extends UpdateSource> getRootUpdateSources() {
    Set<? extends UpdateSource> uss = getUpdateSourcesTransitiveClosure();
    HashSet<UpdateSource> result = new HashSet<UpdateSource>();
    for (UpdateSource us : uss) {
      if (us.getMaximumRootUpdateSourceDistance() == 0) {
        result.add(us);
      }
    }
    return result;
  }

  public static void writeUpdateSourcesDotFile(Set<? extends UpdateSource> updateSources, PrintStream out) {
    assert updateSources != null;
    writeDotHeader(updateSources, out);

    int nrOfFirstOrderUpdateSources = updateSources.size();
    if (nrOfFirstOrderUpdateSources >= 1) {
      assert nrOfFirstOrderUpdateSources >= 1 : "initial size of priority queue must be >= 1";
      PriorityQueue<UpdateSource> queue =
        new PriorityQueue<UpdateSource>(nrOfFirstOrderUpdateSources,
          new Comparator<UpdateSource>() {
                public int compare(UpdateSource us1, UpdateSource us2) {
                  assert us1 != null;
                  assert us2 != null;
                  int mrusd1 = us1.getMaximumRootUpdateSourceDistance();
                  int mrusd2 = us2.getMaximumRootUpdateSourceDistance();
                  return (mrusd1 < mrusd2) ? +1 : ((mrusd1 == mrusd2) ? 0 : -1);
                }
              });
      queue.addAll(updateSources);
      UpdateSource us = queue.poll();
      while (us != null) {
        writeUpdateSourcesToDotFile(us, out);
        Set<UpdateSource> secondDependents = new HashSet<UpdateSource>(us.getUpdateSources());
        secondDependents.removeAll(queue);
        queue.addAll(secondDependents);
        us = queue.poll();
      }
    }

    writeDotFooter(updateSources, out);
  }

  private static void writeDotHeader(Set<? extends UpdateSource> updateSources, PrintStream out) {
    assert updateSources != null;
    out.println("digraph " + "\"Update Sources of " + updateSources.size() + " UpdateSource instances\" {");
  }

  private static void writeDotFooter(Set<? extends UpdateSource> updateSources, PrintStream out) {
    out.println("}");
  }

  private static void writeUpdateSourcesToDotFile(UpdateSource us, PrintStream out) {
      out.print(MultiLineToStringUtil.indent(1));
      out.print(updateSourceDotId(us));
      if (us.getMaximumRootUpdateSourceDistance() > 0) {
        out.print(" -> {");
        for (UpdateSource usus : us.getUpdateSources()) {
          out.print(updateSourceDotId(usus) + "; ");
        }
        out.print("}");
      }
      out.println(";");
  }

  private static String updateSourceDotId(UpdateSource us) {
    String className = us.getClass().getSimpleName();
    if (className.equals("")) {
      className = us.getClass().getName();
    }
    className = className.replace('$', '_');
    className = className.replace('.', '_');
    return className + "_" + Integer.toHexString(us.hashCode());
  }

}
