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
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.MultiLineToStringUtil;


/**
 * Static methods to visualize the update source / dependency
 * graph, using dot and graphviz.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class UpdateSourcesGraph {

  private UpdateSourcesGraph() {
    // NOP
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
