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


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.ppeew.annotations_I.vcs.CvsInfo;


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

  public final Set<Dependent> getDependents() {
    return Collections.unmodifiableSet($dependents);
  }

  public final Set<Dependent> getDependentsTransitiveClosure() {
    Set<Dependent> dtc = new HashSet<Dependent>();
    dtc.addAll($dependents);
    for (Dependent dependent : $dependents) {
      dependentsTransitiveClosure(dependent, dtc);
    }
    return dtc;
  }

  private final void dependentsTransitiveClosure(Dependent dependent, Set<Dependent> acc) {
    assert acc.contains(dependent);
    for (Dependent secondDependent : dependent.getDependents()) {
      if (! acc.contains(secondDependent)) {
        acc.add(secondDependent);
        dependentsTransitiveClosure(secondDependent, acc);
      }
    }
  }

  public final void addDependent(Dependent dependent) {
    assert dependent != null;
    assert ! dependent.getDependentsTransitiveClosure().contains(this);
    $dependents.add(dependent);
    dependent.addUpdateSource(this);
  }

  public final void removeDependent(Dependent dependent) {
    assert dependent != null;
    dependent.removeUpdateSource(this);
    $dependents.remove(dependent);
  }

  /**
   * @invar $dependents != null;
   * @invar Collections.noNull($dependents);
   */
  private final Set<Dependent> $dependents = new HashSet<Dependent>();

}
