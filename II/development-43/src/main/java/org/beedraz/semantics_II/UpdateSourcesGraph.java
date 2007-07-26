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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.MultiLineToStringUtil;


/**
 * Static methods to visualize the update source / dependency
 * graph, using dot and graphviz.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class UpdateSourcesGraph {

  private UpdateSourcesGraph() {
    // NOP
  }

  public static void writeUpdateSourcesDotFile(Set<? extends Beed<?>> updateSources, PrintStream out) {
    assert updateSources != null;
    writeDotHeader(updateSources, out);

    int nrOfFirstOrderUpdateSources = updateSources.size();
    if (nrOfFirstOrderUpdateSources >= 1) {
      assert nrOfFirstOrderUpdateSources >= 1 : "initial size of priority queue must be >= 1";
      PriorityQueue<Beed<?>> queue =
        new PriorityQueue<Beed<?>>(nrOfFirstOrderUpdateSources,
          new Comparator<Beed<?>>() {
                public int compare(Beed<?> us1, Beed<?> us2) {
                  assert us1 != null;
                  assert us2 != null;
                  int mrusd1 = us1.getMaximumRootUpdateSourceDistance();
                  int mrusd2 = us2.getMaximumRootUpdateSourceDistance();
                  return (mrusd1 < mrusd2) ? +1 : ((mrusd1 == mrusd2) ? 0 : -1);
                }
              });
      queue.addAll(updateSources);
      Beed<?> us = queue.poll();
      while (us != null) {
        writeUpdateSourcesToDotFile(us, out);
        Set<Beed<?>> secondDependents = new HashSet<Beed<?>>(us.getUpdateSources());
        secondDependents.removeAll(queue);
        queue.addAll(secondDependents);
        us = queue.poll();
      }
    }

    writeDotFooter(updateSources, out);
  }

  private static void writeDotHeader(Set<? extends Beed<?>> updateSources, PrintStream out) {
    assert updateSources != null;
    out.println("digraph " + "\"Update Sources of " + updateSources.size() + " UpdateSource instances\" {");
  }

  private static void writeDotFooter(Set<? extends Beed<?>> updateSources, PrintStream out) {
    out.println("}");
  }

  private static void writeUpdateSourcesToDotFile(Beed<?> us, PrintStream out) {
      out.print(MultiLineToStringUtil.indent(1));
      out.print(updateSourceDotId(us));
      if (us.getMaximumRootUpdateSourceDistance() > 0) {
        out.print(" -> {");
        for (Beed<?> usus : us.getUpdateSources()) {
          out.print(updateSourceDotId(usus) + "; ");
        }
        out.print("}");
      }
      out.println(";");
  }

  private static String updateSourceDotId(Beed<?> us) {
    String className = us.getClass().getSimpleName();
    if (className.equals("")) {
      className = us.getClass().getName();
    }
    className = className.replace('$', '_');
    className = className.replace('.', '_');
    return className + "_" + Integer.toHexString(us.hashCode());
  }

}
