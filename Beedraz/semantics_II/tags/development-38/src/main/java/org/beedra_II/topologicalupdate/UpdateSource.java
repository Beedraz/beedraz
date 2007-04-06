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

import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * @author Jan Dockx
 *
 * @invar getMaximumRootUpdateSourceDistance() >= 0;
 * @invar for (Dependent d: getDependents() {
 *          d.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance()
 *        };
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface UpdateSource {

  /**
   * @basic
   */
  boolean isDependent(Dependent<?> dependent);

  /**
   * @result isDependent(dependent) ||
   *           exists (Dependent d) {isDependent(d) && d.isTransitiveDependent(dependent)};
   */
  boolean isTransitiveDependent(Dependent<?> dependent);

  /**
   * @pre dependent != null;
   * @pre ! isTransitiveDependent(dependent);
   *      no loops
   * @pre dependent.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance();
   * @post isDependent(dependent);
   *
   * @note {@code dependent.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance();}
   *       is obviously a weird and hard precondition for a public method.
   *       It is however crucial for the topological update algorithm. We would have liked
   *       it better if we could limit access to this method for that reason, but the method
   *       has to be declared in this type, and this type has to be an interface, because
   *       beeds need multiple inheritance. Thus, the method must be public.
   *       It is ill-advised to use the method yourself, though. If you make structural
   *       changes through {@link Dependent#addUpdateSource()} and {@link Dependent#removeUpdateSource()}
   *       only, everything will be cared for.
   */
  void addDependent(Dependent<?> dependent);

  /**
   * @post ! isDependent(dependent);
   *
   * @note It is ill-advised to use the method yourself, though. If you make structural
   *       changes through {@link Dependent#addUpdateSource()} and {@link Dependent#removeUpdateSource()}
   *       only, everything will be cared for.
   */
  void removeDependent(Dependent<?> dependent);

  int getMaximumRootUpdateSourceDistance();

  Set<? extends UpdateSource> getUpdateSources();

  Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure();

  Set<? extends UpdateSource> getRootUpdateSources();

}
