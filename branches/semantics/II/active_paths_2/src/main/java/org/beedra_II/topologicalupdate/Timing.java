/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.beedra_II.event.Event;


public class Timing {

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

  public static Map<Dependent, Timing> results() {
    return Collections.unmodifiableMap(_results);
  }

  private static Map<Dependent, Timing> _results;

  public static void reset() {
    _active = true;
    _results = new HashMap<Dependent, Timing>();
  }

  static void add(Dependent dependent, long starttime, long endtime, Event event) {
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

  private Timing(Dependent dependent, long duration, Event event) {
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
