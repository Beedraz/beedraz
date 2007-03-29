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

import org.beedra_II.edit.Edit;
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
    // create an array of first order dependents, sorted by MRUSD
    int nrOfFirstOrderDependents = nrOfFirstOrderDependents(sourceEvents);
    QueueElement queueHead = null;
    if (nrOfFirstOrderDependents >= 1) { // otherwise, nothing to do
      queueHead = firstOrderQueue(sourceEvents, nrOfFirstOrderDependents);
      queueHead = updateQueue(sourceEvents, edit, queueHead);
      // assert: all QE's contain an event
    }
    fireInitialUpdateSourcesEvents(sourceEvents);
    fireQueueEvents(queueHead);
  }

  private static void fireQueueEvents(QueueElement qe) {
    while (qe != null) {
      qe.fireEvent();
      qe = qe.getNext();
    }
  }

  private static void fireInitialUpdateSourcesEvents(Map<AbstractUpdateSource, Event> sourceEvents) {
    for (Map.Entry<AbstractUpdateSource, Event> entry : sourceEvents.entrySet()) {
      entry.getKey().fireEvent(entry.getValue());
    }
  }

  private static QueueElement updateQueue(Map<AbstractUpdateSource, Event> sourceEvents, Edit<?> edit, QueueElement queueHead) {
    Map<UpdateSource, Event> events = new LinkedHashMap<UpdateSource, Event>(sourceEvents);
      /* most efficient structure to store the events to give them to the dependents
       * when they need to update themselves
       */
    QueueElement current;
    QueueElement previous = null;
    current = queueHead;
    while (current != null) {
      previous = current.update(previous, events, edit); // this is it
      // returns null if the first element of the structure needs to be removed
      if (previous == null) {
        assert current == queueHead;
        queueHead = current.getNext();
      }
      current = current.getNext();
    }
    return queueHead;
  }

  private static int nrOfFirstOrderDependents(Map<AbstractUpdateSource, Event> sourceEvents) {
    int nrOfFirstOrderDependents = 0;
    for (AbstractUpdateSource aus : sourceEvents.keySet()) {
      nrOfFirstOrderDependents +=  aus.$dependents.size();
    }
    return nrOfFirstOrderDependents;
  }

  private static QueueElement firstOrderQueue(Map<AbstractUpdateSource, Event> sourceEvents, int nrOfFirstOrderDependents) {
    assert nrOfFirstOrderDependents >= 1;
    QueueElement queueHead;
    Dependent<?>[] sortedFirstOrderDependents = firstOrderDepenentsToArray(sourceEvents, nrOfFirstOrderDependents);
    Arrays.sort(sortedFirstOrderDependents, MRUSD_COMPARATOR);
    // create initial structure of first order dependents
    queueHead = createQueueFromSortedArray(sortedFirstOrderDependents);
    return queueHead;
  }

  private static QueueElement createQueueFromSortedArray(Dependent<?>[] sortedFirstOrderDependents) {
    assert sortedFirstOrderDependents != null;
    assert sortedFirstOrderDependents.length > 0;
    QueueElement queueHead;
    queueHead = new QueueElement(sortedFirstOrderDependents[0]); // there is at least 1 element
    QueueElement current = queueHead;
    for (int j = 1; j < sortedFirstOrderDependents.length; j++) {
      current.setNext(new QueueElement(sortedFirstOrderDependents[j]));
      current = current.getNext();
    }
    return queueHead;
  }

  private static Dependent<?>[] firstOrderDepenentsToArray(Map<AbstractUpdateSource, Event> sourceEvents, int nrOfFirstOrderDependents) {
    assert nrOfFirstOrderDependents >= 1;
    Dependent<?>[] sortedFirstOrderDependents = new Dependent<?>[nrOfFirstOrderDependents];
    int i = 0;
    for (AbstractUpdateSource aus : sourceEvents.keySet()) {
      Set<Dependent<?>> firstOrderDependents = aus.getDependents();
      for (Dependent<?> fod : firstOrderDependents) {
        sortedFirstOrderDependents[i] = fod;
        i++;
      }
    }
    assert i == nrOfFirstOrderDependents;
    return sortedFirstOrderDependents;
  }

  /**
   * Comparator that compares maximal root update source distance of dependents.
   */
  private static Comparator<Dependent<?>> MRUSD_COMPARATOR = new Comparator<Dependent<?>>() {
    public int compare(Dependent<?> d1, Dependent<?> d2) {
      assert d1 != null;
      assert d2 != null;
      int mfsd1 = d1.getDependentUpdateSource().getMaximumRootUpdateSourceDistance();
      int mfsd2 = d2.getDependentUpdateSource().getMaximumRootUpdateSourceDistance();
      return (mfsd1 < mfsd2) ? -1 : ((mfsd1 == mfsd2) ? 0 : +1);
    }
  };

  /**
   * @invar getDependent() != null;
   * @invar getNext() != null ? getNext().getMRUSD() >= getNext().getMRUSD();
   */
  private static class QueueElement {

    /**
     * @pre dependent != null;
     * @post getDependent() == dependent;
     * @post getNext() == null;
     */
    public QueueElement(Dependent<?> dependent) {
      assert dependent != null;
      $dependent = dependent;
    }

    /**
     * @basic
     */
    public final QueueElement getNext() {
      return $next;
    }

    /**
     * @pre next != null;
     * @post getNext() == next;
     */
    public final void setNext(QueueElement next) {
      assert next != null;
      $next = next;
    }

    /**
     * @invar $next != null ? $next.getMRUSD() >= $next.getMRUSD();
     */
    private QueueElement $next;

    /**
     * @basic
     */
    public final Dependent<?> getDependent() {
      return $dependent;
    }

    /**
     * @return getDependent().getMaximumRootUpdateSourceDistance();
     */
    final int getMrusd() {
      return $dependent.getMaximumRootUpdateSourceDistance();
    }

    /**
     * Asks the dependent to update itself, and report on the change
     * with an event. Timing is done. The timing code has no
     * substantial effect on performance.
     * Return false if the dependent did not actually change, and no event
     * should be send. Return true if the dependent did actually change, and
     * we should send the event later. If false is returned, the caller
     * should remove this from the structure.
     * We return the new previous QE. Previous is null for the first element in
     * the structure.
     */
    public final QueueElement update(QueueElement previous, Map<UpdateSource, Event> events, Edit<?> edit) {
      actualUpdate(events, edit);
      if ($event != null) {
        events.put($dependent.getDependentUpdateSource(), $event);
        addDependents();
        return this;
      }
      else {
        if (previous != null) {
          previous.setNext($next);
          return previous;
        }
        else {
          // we are the head of the queue
          return null;
        }
      }
    }

    /**
     * Asks the dependent to update itself, and report on the change
     * with an event. Timing is done. The timing code has no
     * substantial effect on performance.
     */
    private void actualUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
      long starttime = 0;
      if (Timing._active) {
        starttime = System.nanoTime();
      }
      $event = $dependent.update(events, edit);
      if (Timing._active) {
        long endtime = System.nanoTime();
        Timing.add($dependent, starttime, endtime, $event);
      }
    }

    /**
     * Adds dependents to the topological update priority queue structure.
     * Dependents are added immediately after dependents
     * that are already in this structure with the samen maximal root update
     * source distance. Elements that are already in the structure are not added
     * again. If there is no element in the structure with the same maximal root update
     * source distance, the element is added after elements with smaller maximal root update
     * source distance and before elemenents with larger maximal root update
     * source distance.
     *
     * The algorithm first sorts the new dependents according to their maximal root update
     * source distance. Because of this, going through the structure from start (low
     * maximal root update source distance) to end, we can deal with elements one
     * after to other, without the need to restart going through the structure from the
     * beginning.
     * Because we only add the dependents of the current QueueElement dependent, they
     * surely are added further down in the structure.
     *
     * @invar for (Dependent<?> d : $dependent.getDependents()) {
     *          d.getMaximalRootUpdateSourceDistance() > getMRUSD()
     *        };
     */
    private void addDependents() {
      Set<Dependent<?>> dependents = $dependent.getDependents();
      assert dependents != null;
      // MRUSD of dependents is > than current MRUSD
      Dependent<?>[] sorted = new Dependent<?>[dependents.size()];
      sorted = dependents.toArray(sorted);
      Arrays.sort(sorted, MRUSD_COMPARATOR);
      int i = 0;
      QueueElement qe = this;
      /* invar: sorted[i -1] is placed in the structure before current
       * invar: qe and elements before qe have a MRUSD smaller than or equal to sorted[i]
       */
      while ((i < sorted.length ) && (qe.getNext() != null)) {
        assert qe.getMrusd() <= sorted[i].getMaximumRootUpdateSourceDistance();
        if (qe.getNext().getDependent() == sorted[i]) {
          // do not proceed in structure yet: we might need to add more sorted d's here
          i++; // next sorted
        }
        else if (qe.getNext().getMrusd() > sorted[i].getMaximumRootUpdateSourceDistance()) {
          // we need to add sorted[i] after qe
          QueueElement insert = new QueueElement(sorted[i]);
          insert.setNext(qe.getNext());
          qe.setNext(insert);
          qe = insert;
          // do not proceed in structure yet: we might need to add more sorted d's here
          i++; // next sorted
        }
        else {
          /* not already a member, and qe.getMrusd() is smaller are equal to sorted MRUSD;
           * proceed to next element in structure (keep looking)
           */
          qe = qe.getNext();
        }
      }
      if (i < sorted.length) {
        /* add all leftover elements to the end of the structure, in the order
           they appear in the array */
        assert qe.getNext() == null : "we are at the end of the structure";
        for (int j = i; j < sorted.length; j++) {
          qe.setNext(new QueueElement(sorted[j]));
          qe = qe.getNext();
        }
      }
    }

    /**
     * @invar $dependent != null;
     */
    private final Dependent<?> $dependent;

    /**
     * @basic
     */
    public final Event getEvent() {
      return $event;
    }

    public final void fireEvent() {
      assert $event != null;
      $dependent.fireEvent($event);
    }

    private Event $event;

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
