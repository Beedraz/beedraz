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


import java.util.Set;

import org.ppeew.annotations.vcs.CvsInfo;


/**
 * @author Jan Dockx
 *
 * @invar getMaximumRootUpdateSourceDistance() >= 0;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface UpdateSource {

  /**
   * @basic
   */
  Set<Dependent> getDependents();

  Set<Dependent> getDependentsTransitiveClosure();

  /**
   * @pre dependent != null;
   * @pre ! dependent.getDependentsTransitiveClosure().contains(this);
   *      no loops
   * @post getDependents().contains(dependent);
   * @post dependent.getUpdateSources().contains(this);
   */
  void addDependent(Dependent dependent);

  /**
   * @pre dependent != null;
   * @post ! getDependents().contains(dependent);
   * @post ! dependent.getUpdateSources().contains(this);
   */
  void removeDependent(Dependent dependent);

  int getMaximumRootUpdateSourceDistance();

}
