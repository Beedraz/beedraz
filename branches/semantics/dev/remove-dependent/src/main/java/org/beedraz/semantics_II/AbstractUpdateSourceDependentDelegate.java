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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.collection_I.ArraySet;
import org.ppwcode.util.smallfries_I.MultiLineToStringUtil;


/**
 * <p>More can be implemented in a general way if we know that our
 *   {@link #getDependentUpdateSource()} is an {@link AbstractBeed}
 *   (which it will be most of the time). MUDO THIS IS OBSOLETE; MERGE WITH DEPENDENT</p>
 * <p>From experience, we know that it makes no sense for this type to be generic in the types
 *   of allowed {@link Beed update sources} or {@link Event Events}.</p>
 *
 * @author Jan Dockx
 *
 * @invar getDependentUpdateSource() != null;
 * @invar for (Dependent d: getDependents() {
 *          d.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance()
 *        };
 * @invar getUpdateSource().getMaximumRootUpdateSourceDistance() > 0;
 * @invar getUpdateSource().getMaximumRootUpdateSourceDistance() == getMaximumRootUpdateSourceDistance();
 * @invar for (UpdateSource us : getUpdateSources()) {
 *          us.getMaximalRootUpdateSourceDistance() < getMaximalRootUpdateSourceDistance()
 *        };
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractUpdateSourceDependentDelegate
    implements Dependent {

  /**
   * @pre dependentUpdateSource != null;
   * @post getDependentUpdateSource() == dependentUpdateSource;
   */
  protected AbstractUpdateSourceDependentDelegate(AbstractBeed<?> dependentUpdateSource) {
    $dependentUpdateSource = dependentUpdateSource;
  }

  /*<property name="dependent update source">*/
  //-----------------------------------------------------------------

  public final AbstractBeed<?> getDependentUpdateSource() {
    return $dependentUpdateSource;
  }

  public final Set<Dependent> getDependents() {
    return $dependentUpdateSource.getDependents();
  }

  public final void fireEvent(Event event) {
    $dependentUpdateSource.fireEvent(event);
  }

  private final AbstractBeed<?> $dependentUpdateSource;

  /*</property>*/

  /*<section name="maximum root update source distance">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final int getMaximumRootUpdateSourceDistance() {
    return $maximumRootUpdateSourceDistance;
  }

  /**
   * @pre newSourceMaximumFinalSourceDistance < Integer.MAX_VALUE
   * @post getMaximumRootUpdateSourceDistance() ==
   *         max('getMaximumRootUpdateSourceDistance(), newSourceMROSD + 1);
   * @post getMaximumRootUpdateSourceDistance() > 'getMaximumRootUpdateSourceDistance() ?
   *         for (Dependents d: getDependents()) {
   *           d.updateMaximumRootUpdateSourceDistanceUp(getMaximumRootUpdateSourceDistance())
   *         }
   */
  public void updateMaximumRootUpdateSourceDistanceUp(int newSourceMROSD) {
    assert newSourceMROSD < Integer.MAX_VALUE;
    int potentialNewMaxDistance = newSourceMROSD + 1;
    if (potentialNewMaxDistance > $maximumRootUpdateSourceDistance) {
      $maximumRootUpdateSourceDistance = potentialNewMaxDistance;
      for (Dependent dependent : getDependents()) {
        dependent.updateMaximumRootUpdateSourceDistanceUp($maximumRootUpdateSourceDistance);
      }
    }
  }

  /**
   * @post 'getMaximumRootUpdateSourceDistance() > oldSourceMROSD + 1 ?
   *         getMaximumRootUpdateSourceDistance() == 'getMaximumRootUpdateSourceDistance();
   * @post 'getMaximumRootUpdateSourceDistance() == oldSourceMROSD + 1 ?
   *         getMaximumRootUpdateSourceDistance() ==
   *           max(UpdateSource us: getUpdateSources()) {us.getMaximumRootUpdateSourceDistance()} + 1;
   * @post getMaximumRootUpdateSourceDistance() < 'getMaximumRootUpdateSourceDistance() ?
   *         for (Dependents d: getDependents()) {
   *           d.updateMaximumRootUpdateSourceDistanceDown('getMaximumRootUpdateSourceDistance())
   *         }
   */
  public void updateMaximumRootUpdateSourceDistanceDown(int oldSourceMROSD) {
    if ($maximumRootUpdateSourceDistance == oldSourceMROSD + 1) {
        // no overflow problem in if
      int oldMaximumFinalSourceDistance = $maximumRootUpdateSourceDistance;
      $maximumRootUpdateSourceDistance = 0;
      for (Beed<?> otherUpdateSource : getUpdateSources()) {
        int potentialNewMaxDistance = otherUpdateSource.getMaximumRootUpdateSourceDistance() + 1;
          // no overflow problem due to invariant
        if (potentialNewMaxDistance > $maximumRootUpdateSourceDistance) {
          $maximumRootUpdateSourceDistance = potentialNewMaxDistance;
        }
      }
      if (oldMaximumFinalSourceDistance != $maximumRootUpdateSourceDistance) {
        for (Dependent dependent : getDependents()) {
          dependent.updateMaximumRootUpdateSourceDistanceDown(oldMaximumFinalSourceDistance);
        }
      }
    }
  }

  /**
   * @invar $maximumRootUpdateSourceDistance >= 0;
   */
  private int $maximumRootUpdateSourceDistance;

  /*</section>*/


  /*<section name="update">*/
  //-----------------------------------------------------------------

  /**
   * The method that updates this dependent, and reports on the change.
   * The returned event is the one that will be offered to
   * {@link #fireEvent(Event)} later on.
   *
   * @param edit
   *        The edit that causes this update. This may be {@code null},
   *        for structural changes.
   * @pre events != null;
   */
  public final Event update(Map<? extends Beed<?>, Event> events, Edit<?> edit) {
    Map<Beed<?>, Event> result = new HashMap<Beed<?>, Event>();
    for (Beed<?> us : $updateSources) {
      assert us != null;
      Event event = events.get(us);
      if (event != null) {
        result.put(us, event);
      }
    }
    return filteredUpdate(result, edit);
  }

  /**
   * The method that updates this dependent, and reports on the change.
   * The returned event is the one that will be offered to
   * {@link #fireEvent(Event)} later on. {@code events} only contains
   * the events registered by elements of {@link #getUpdateSources()}.
   *
   * @pre events != null;
   */
  protected abstract Event filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit);

  /*</section>*/

  /*<section name="update sources">*/
  //-----------------------------------------------------------------

  /**
   * @return result.equals(getUpdateSourcesOccurencesMap().keySet());
   */
  public final Set<Beed<?>> getUpdateSources() {
    return Collections.unmodifiableSet($updateSources);
  }

  public final Set<Beed<?>> getUpdateSourcesTransitiveClosure() {
        HashSet<Beed<?>> result = new HashSet<Beed<?>>($updateSources);
        for (Beed<?> us : $updateSources) {
          result.addAll(us.getUpdateSourcesTransitiveClosure());
        }
        return result;
      }

  /**
   * @pre updateSource != null;
   * @pre ! getDependentsTransitiveClosure().contains(updateSource);
   *      no loops
   * @post getUpdateSources().contains(updateSource);
   * @post updateSource.getDependents().contains(this);
   * @post updateMaximumRootUpdateSourceDistanceUp(updateSource.getMaximumRootUpdateSourceDistance());
   */
  public final void addUpdateSource(Beed<?> updateSource) {
    assert updateSource != null;
    $updateSources.add(updateSource);
    updateMaximumRootUpdateSourceDistanceUp(updateSource.getMaximumRootUpdateSourceDistance());
    if (getMaximumRootUpdateSourceDistance() <= updateSource.getMaximumRootUpdateSourceDistance()) {
      System.out.println("DEPENDENCY LOOP while adding " + updateSource + " as update source to dependency tree:");
      StringBuffer sb = new StringBuffer();
      dependentToString(this, sb, 0);
      System.out.println(sb);
      assert false : "if this fails, it means we have a dependency loop";
    }
    updateSource.addDependent(this);
  }

  private static void dependentToString(Dependent d, StringBuffer sb, int level) {
        System.out.println(MultiLineToStringUtil.indent(level) + d.getDependentUpdateSource().toString());
        int level2 = level + 1;
        for (Dependent d2 : d.getDependents()) {
          dependentToString(d2, sb, level2);
        }
      }

  /**
   * @pre updateSource != null;
   * @post ! getUpdateSources().contains(updateSource);
   * @post ! updateSource.getDependents().contains(this);
   * @post updateMaximumRootUpdateSourceDistanceDown(updateSource.getMaximumRootUpdateSourceDistance());
   */
  public final void removeUpdateSource(Beed<?> updateSource) {
    assert updateSource != null;
    updateSource.removeDependent(this);
    updateMaximumRootUpdateSourceDistanceDown(updateSource.getMaximumRootUpdateSourceDistance());
    $updateSources.remove(updateSource);
  }


  /**
   * @invar $updateSources != null;
   * @invar Collections.noNull($updateSources);
   */
  private final Set<Beed<?>> $updateSources = new ArraySet<Beed<?>>();

  /*</section>*/

}