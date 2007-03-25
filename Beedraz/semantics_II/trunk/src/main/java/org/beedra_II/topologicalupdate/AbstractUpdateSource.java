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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.beedra_II.event.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;
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
      for (Dependent<?> d : $dependents) {
        if (d.getDependentUpdateSource().isTransitiveDependent(dependent)) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Don't expose the collection of dependents publicly. It's
   * a secret shared between us and the dependent.
   *
   * @result for (Dependent d) {isDependent(d) ?? result.contains(d)};
   */
  protected final Set<Dependent<?>> getDependents() {
    return Collections.unmodifiableSet($dependents);
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
   * @pre us != null;
   * @pre event != null;
   */
  protected static void updateDependents(AbstractUpdateSource us, Event event) {
    HashMap<AbstractUpdateSource, Event> sourceEvents = new HashMap<AbstractUpdateSource, Event>(1);
    sourceEvents.put(us, event);
    multiUpdateDependents(sourceEvents);
  }

  /**
   * The topological update method. First change this update source locally,
   * then described the change in an event, then call this method with that event.
   *
   * @pre sourceEvents != null;
   * @pre sourceEvents.size() > 0;
   */
  protected static void multiUpdateDependents(Map<AbstractUpdateSource, Event> sourceEvents) {
    Map<UpdateSource, Event> events = new LinkedHashMap<UpdateSource, Event>(sourceEvents);
    Map<Dependent<?>, Event> dependentEvents = new LinkedHashMap<Dependent<?>, Event>();
    // LinkedHashMap to remember topol order for event listener notification
//    events.put(us, event);
    int nrOfFirstOrderDependents = 0;
    for (AbstractUpdateSource aus : sourceEvents.keySet()) {
      nrOfFirstOrderDependents +=  aus.$dependents.size();
    }
    if (nrOfFirstOrderDependents >= 1) {
      assert nrOfFirstOrderDependents >= 1 : "initial size of priority queue must be >= 1";
      PriorityQueue<Dependent<?>> queue =
        new PriorityQueue<Dependent<?>>(nrOfFirstOrderDependents,
          new Comparator<Dependent<?>>() {
                public int compare(Dependent<?> d1, Dependent<?> d2) {
                  assert d1 != null;
                  assert d2 != null;
                  int mfsd1 = d1.getDependentUpdateSource().getMaximumRootUpdateSourceDistance();
                  int mfsd2 = d2.getDependentUpdateSource().getMaximumRootUpdateSourceDistance();
                  return (mfsd1 < mfsd2) ? -1 : ((mfsd1 == mfsd2) ? 0 : +1);
                }
              });
      for (AbstractUpdateSource aus : sourceEvents.keySet()) {
        queue.addAll(aus.$dependents);
      }
      Dependent<?> dependent = queue.poll();
      while (dependent != null) {
        // TODO start timing measurement START
        long starttime = 0;
        if (Timing._active) {
          starttime = System.nanoTime();
        }
        // TODO start timing measurement END
        Event dependentEvent = dependent.update(Collections.unmodifiableMap(events));
        // TODO stop timing measurement START
        if (Timing._active) {
          long endtime = System.nanoTime();
          Timing.add(dependent, starttime, endtime, dependentEvent);
        }
        // TODO stop timing measurement END
        if (dependentEvent != null) {
          events.put(dependent.getDependentUpdateSource(), dependentEvent);
          dependentEvents.put(dependent, dependentEvent);
          Set<Dependent<?>> secondDependents =
            new HashSet<Dependent<?>>(dependent.getDependents());
          secondDependents.removeAll(queue);
          queue.addAll(secondDependents);
        }
        dependent = queue.poll();
      }
    }
    for (Map.Entry<AbstractUpdateSource, Event> entry : sourceEvents.entrySet()) {
      entry.getKey().fireEvent(entry.getValue());
    }
    for (Map.Entry<Dependent<?>, Event> entry : dependentEvents.entrySet()) {
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
  private final Set<Dependent<?>> $dependents = new HashSet<Dependent<?>>();

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
