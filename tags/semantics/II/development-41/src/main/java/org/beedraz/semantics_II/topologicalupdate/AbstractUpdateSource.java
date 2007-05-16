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

package org.beedraz.semantics_II.topologicalupdate;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.collection_I.ArraySet;


/**
 * @author Jan Dockx
 *
 * @invar getMaximumRootUpdateSourceDistance() >= 0;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractUpdateSource implements UpdateSource {

  public final boolean isDependent(Dependent dependent) {
    return ($dependents != null) && $dependents.contains(dependent);
  }

  public final boolean isTransitiveDependent(Dependent dependent) {
    if ($dependents == null) {
      return false;
    }
    if (isDependent(dependent)) {
      return true;
    }
    else {
      for (Dependent d : $dependents) {
        if (d.getDependentUpdateSource().isTransitiveDependent(dependent)) {
          return true;
        }
      }
      return false;
    }
  }

  private final static Set<Dependent> EMPTY_DEPENDENTS = Collections.emptySet();

  /**
   * Don't expose the collection of dependents publicly. It's
   * a secret shared between us and the dependent. This collection
   * uses strong references.
   *
   * @result for (Dependent d) {isDependent(d) ?? result.contains(d)};
   */
  protected final Set<Dependent> getDependents() {
    return ($dependents == null) ? EMPTY_DEPENDENTS : $dependents.clone();
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
    if ($dependents == null) {
      $dependents = new ArraySet<Dependent>();
    }
    $dependents.add(dependent);
  }

  public final void removeDependent(Dependent dependent) {
    if ($dependents != null) {
      $dependents.remove(dependent);
      if ($dependents.isEmpty()) {
        $dependents = null;
      }
    }
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
   * If the set is empty, it is set to null, to save memory.
   */
  private ArraySet<Dependent> $dependents = null;

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
