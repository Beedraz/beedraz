/*<license>
Copyright 2007 - $Date: 2007-05-16 17:43:13 +0200 (wo, 16 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.aggregate;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>{@link Path} that selects a component or property {@link Path} from an
 *   {@link AggregateBeed}. The {@link #getAggregateBeed() aggregate beed} is the result of
 *   a {@link #getAggregateBeedPath() aggregate beed path}. When the result of the
 *   {@link #getAggregateBeedPath() aggregate beed path} changes, the result of this
 *   {@code AggregateComponentPath} might change too. When this happens, this
 *   will update dependents and send events to listeners.</p>
 *
 * @protected
 * <p>Subclasses must implement {@link #selectComponentPathFromAggregate(AggregateBeed)},
 *   to select the appropriate property path from the aggregate beed given by the
 *   {link {@link #getAggregateBeedPath()} (i.e., {@link #getAggregateBeed()}).</p>
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getAggregateBeedPath() != null;
 * @invar  getAggregateBeed() == getAggregateBeedPath().get();
 *
 * @mudo   This class is very similar to AggregateComponentPath.
 * @mudo   Better name?
 */
@Copyright("2007 - $Date: 2007-05-16 17:43:13 +0200 (wo, 16 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 905 $",
         date     = "$Date: 2007-05-16 17:43:13 +0200 (wo, 16 mei 2007) $")
public abstract class VariableComponentPath<_AggregateBeed_ extends AggregateBeed, _ComponentBeed_ extends Beed<?>>
    extends AbstractDependentPath<_ComponentBeed_> {

  /*<construction>*/
  //-----------------------------------------------------------------

  /**
   * @pre  aggregateBeedPath != null;
   * @post getAggregateBeedPath() == aggregateBeedPath;
   * @post getAggregateBeed() == aggregateBeedPath.get();
   * @post getComponentBeedPath() ==
   *         aggregateBeedPath.get() == null
   *           ? null
   *           : selectComponentPathFromAggregate(aggregateBeedPath.get());
   */
  public VariableComponentPath(Path<? extends _AggregateBeed_> aggregateBeedPath) {
    assert aggregateBeedPath != null;
    $aggregateBeedPath = aggregateBeedPath;
    _AggregateBeed_ aggregateBeed = aggregateBeedPath.get();
    addUpdateSource(aggregateBeedPath);
    setComponentBeedPath(aggregateBeed == null ? null : selectComponentPathFromAggregate(aggregateBeed));
  }

  /*</construction>*/



  /*<section name="dependent">*/
  //-----------------------------------------------------------------

  @Override
  protected PathEvent<_ComponentBeed_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    _ComponentBeed_ oldComponentBeed = $componentBeed;
    // events can come from:
    //   - $aggregateBeedPath
    //   - $componentBeedPath
    PathEvent<_AggregateBeed_> aggregateBeedPathEvent = (PathEvent<_AggregateBeed_>)events.get($aggregateBeedPath);
    if (aggregateBeedPathEvent != null) {
      _AggregateBeed_ newAggregateBeed = aggregateBeedPathEvent.getNewValue();
      assert newAggregateBeed == $aggregateBeedPath.get();
      setComponentBeedPath(newAggregateBeed == null ? null : selectComponentPathFromAggregate(newAggregateBeed));
    }
    else {
      PathEvent<_ComponentBeed_> componentBeedPathEvent =
        (PathEvent<_ComponentBeed_>) events.get($componentBeedPath);
      assert componentBeedPathEvent != null;
      $componentBeed = componentBeedPathEvent.getNewValue();
    }
    if (oldComponentBeed != $componentBeed) {
      return new PathEvent<_ComponentBeed_>(this, oldComponentBeed, $componentBeed, edit);
    }
    else {
      return null;
    }
  }

  /*</section>*/



  /*<property name="bean beed path">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends _AggregateBeed_> getAggregateBeedPath() {
    return $aggregateBeedPath;
  }

  private final Path<? extends _AggregateBeed_> $aggregateBeedPath;

  /*</property>*/



  /*<property name="bean beed">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final _AggregateBeed_ getAggregateBeed() {
    return $aggregateBeedPath.get();
  }

  /*</property>*/



  /*<property name="selected">*/
  //-----------------------------------------------------------------

  public final Path<_ComponentBeed_> getComponentBeedPath() {
    return $componentBeedPath;
  }

  private final void setComponentBeedPath(Path<_ComponentBeed_> componentBeedPath) {
    if ($componentBeedPath instanceof AbstractDependentPath) {
      removeUpdateSource($componentBeedPath);
    }
    $componentBeedPath = componentBeedPath;
    if ($componentBeedPath != null) {
      $componentBeed = $componentBeedPath.get();
      if ($componentBeedPath instanceof AbstractDependentPath) {
        addUpdateSource($componentBeedPath);
      }
    }
    else {
      $componentBeed = null;
    }
  }

  private Path<_ComponentBeed_> $componentBeedPath;

  public final _ComponentBeed_ get() {
    return $componentBeed;
  }

  /**
   * @pre  aggregateBeed != null;
   */
  protected abstract Path<_ComponentBeed_> selectComponentPathFromAggregate(_AggregateBeed_ aggregateBeed);

  private _ComponentBeed_ $componentBeed;

  /*</property>*/

}

