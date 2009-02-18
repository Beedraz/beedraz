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
 * <p>The object responsible for the dependent role in topological updates.
 *   It is implemented as a delegate. The dependent role has 2 functions:
 *   updating the dependent in the topological algorithm, and maintaining
 *   the {@link #getMaximumRootUpdateSourceDistance()} for the dependee.</p>
 * <p>The methods {@link #update(Map)}, {@link #fireEvent(Event)},
 *   {@link #getDependentUpdateSource()}, {@link #getMaximumRootUpdateSourceDistance()}
 *   and {@link #getDependent()} are needed for the topological update algorithm.</p>
 * <p>To support the type invariant</p>
 * <pre>
 * for (Dependent d: getDependents() {
 *   d.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance()
 * };
 * </pre>
 * <p>on both this type and {@link UpdateSource}, the
 *   {@link #getMaximumRootUpdateSourceDistance()} must be maintained on all structural
 *   changes. For this algorithm to work, all dependents must support the methods
 *   {@link #updateMaximumRootUpdateSourceDistanceDown(int)} and
 *   {@link #updateMaximumRootUpdateSourceDistanceDown(int)}, and
 *   {@link #getDependents()} and {@link #getUpdateSourcesCollection()}.</p>
 * <p>The type {@link UpdateSource} must be an interface, because it is a supertype
 *   for beeds, and beeds require multiple inheritance. The methods
 *   {@link UpdateSource#addDependent(Dependent)} and
 *   {@link UpdateSource#removeDependent(Dependent)} must be defined in that type, and
 *   thus must public. That is deplorable, since {@link UpdateSource#addDependent(Dependent)}
 *   has a hard precondition, and the bidirectional, many-to-many association between
 *   update sources and dependents is adequately supported by add an remove methods on the
 *   dependent side, and not on the update source side. When {@link #addUpdateSource(UpdateSource)}
 *   and {@link #removeUpdateSource(UpdateSource)} of this class are used, everything is cared
 *   for. It is ill-advised to use the methods {@link UpdateSource#addDependent(Dependent)}
 *   and {@link UpdateSource#removeDependent(Dependent)} yourself. Implementing the maintenance
 *   of the bidirectional many-to-many association yourself is ill-advised to. By making this
 *   type a class that supports all that, and not an interface, and demanding that all dependents
 *   are of this type in {@link UpdateSource}, we give a strong hint to using the framework
 *   that way.</p>
 * <p>Because the 2 roles now come together in this class, and all dependents have to be of this
 *   type, access to parts of the algorithm can be restricted.</p>
 * <p>The collection of {@link #getUpdateSourcesCollection() update sources} is a <em>set</em>.</p>
 * <p>From experience, we know that it makes no sense for this type to be generic in the types
 *   of allowed {@link UpdateSource UpdateSources} or {@link Event Events}.</p>
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
public abstract class Dependent {

  /*<property name="dependent update source">*/
  //-----------------------------------------------------------------

  /**
   * The dependent update source, for which we are the delegate.
   *
   * @basic
   *
   * @protected Implement preferably as <code>return <var>OuterClassName</var>.this</code>.
   */
  public abstract Beed<?> getDependentUpdateSource();

  /**
   * <p>This method is used by the topological update algorithm in
   *   {@link TopologicalUpdate#topologicalUpdate(Map, Edit)}.</p>
   * <p>It is the dependent update source that holds the dependents, not us.
   *   And how it holds the dependents, is unknown to use at this level
   *   ({@link UpdateSource} does not offer the collection of dependents).
   *   Therefor, this method is abstract.</p>
   *
   * @result result != null;
   * @result getUpdateSource().getDependents();
   */
  public abstract Set<Dependent> getDependents();

  /**
   * It is the dependent update source that must fire events, not us.
   * And how it does, is unknown to use at this level
   * ({@link UpdateSource} does not offer firing events).
   * Therefor, this method is abstract.
   * The given {@code event} is the event that was returned
   * by {@link #update(Map)} earlier.
   *
   * @pre event != null;
   * @pre event.getSource() == getUpdateSource();
   * @pre event instanceof _Event_
   */
  abstract void fireEvent(Event event);

  /*</property>*/


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
  protected final Event update(Map<? extends Beed<?>, Event> events, Edit<?> edit) {
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
   * @return getUpdateSourcesSet().contains(updateSource);
   */
  public final boolean isUpdateSource(Beed<?> updateSource) {
    return $updateSources.contains(updateSource);
  }

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
  final void updateMaximumRootUpdateSourceDistanceUp(int newSourceMROSD) {
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
  final void updateMaximumRootUpdateSourceDistanceDown(int oldSourceMROSD) {
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

}