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


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedra_II.Event;
import org.beedra_II.edit.Edit;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.collection_I.WeakHashSet;


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

  public final boolean isDependent(Dependent dependent) {
    return $dependents.contains(dependent);
  }

  public final boolean isTransitiveDependent(Dependent dependent) {
    if (isDependent(dependent)) {
      return true;
    }
    else {
      for (Dependent d : $dependents.strongClone()) {
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
  protected final Set<Dependent> getDependents() {
    return $dependents.strongClone();
  }

  /**
   * @pre dependent != null;
   * @pre dependent.getDependentUpdateSource() != this;
   * @pre ! getUpdateSourcesTransitiveClosure().contains(dependent.getDependentUpdateSource());
   */
  public final void addDependent(Dependent dependent) {
    assert dependent != null;
    assert dependent.getDependentUpdateSource() != this;
    /* MUDO incredible slowdown, and -da doesn't work ???
    assert ! getUpdateSourcesTransitiveClosure().contains(dependent.getDependentUpdateSource());
    */
    assert dependent.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance();
    $dependents.add(dependent);
  }

  public final void removeDependent(Dependent dependent) {
    $dependents.remove(dependent);
  }

  /**
   * The topological update method. First change this update source locally,
   * then described the change in an event, then call this method with that event.
   *
   * @pre event != null;
   *
   * @mudo This method is only here to make the method
   *       {@link TopologicalUpdate#updateDependents(AbstractUpdateSource, Event)}
   *       accessible in other packates for subtypes.
   *       The method actually needs to be accessible by edits. The correct
   *       solution is to put edits in the same package as {@link TopologicalUpdate},
   *       which would then be the beedra core top package.
   */
  protected final void updateDependents(Event event) {
    TopologicalUpdate.updateDependents(this, event);
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
   *
   * @mudo This method is only here to make the method
   *       {@link TopologicalUpdate#updateDependents(AbstractUpdateSource, Event)}
   *       accessible in other packates for subtypes.
   *       The method actually needs to be accessible by edits. The correct
   *       solution is to put edits in the same package as {@link TopologicalUpdate},
   *       which would then be the beedra core top package.
   */
  protected static void updateDependents(Map<AbstractUpdateSource, Event> sourceEvents, Edit<?> edit) {
    TopologicalUpdate.updateDependents(sourceEvents, edit);
  }

  /**
   * Fire the event to regular (non-topological) listeners.
   */
  protected abstract void fireEvent(Event event);

  /**
   * @invar $dependents != null;
   * @invar Collections.noNull($dependents);
   */
  private final WeakHashSet<Dependent> $dependents = new WeakHashSet<Dependent>();

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

}
