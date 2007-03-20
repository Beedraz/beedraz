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


import java.util.Map;
import java.util.Set;

import org.beedra_II.event.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>More can be implemented in a general way if we know that our
 *   {@link #getDependentUpdateSource()} is an {@link AbstractUpdateSource}
 *   (which it will be most of the time).</p>
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
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractUpdateSourceDependentDelegate<_UpdateSource_ extends UpdateSource,
                                                            _Event_ extends Event>
    extends Dependent<_UpdateSource_> {

  /**
   * @pre dependentUpdateSource != null;
   * @post getDependentUpdateSource() == dependentUpdateSource;
   */
  protected AbstractUpdateSourceDependentDelegate(AbstractUpdateSource<_Event_> dependentUpdateSource) {
    $dependentUpdateSource = dependentUpdateSource;
  }

  /*<property name="dependent update source">*/
  //-----------------------------------------------------------------

  @Override
  public final AbstractUpdateSource<_Event_> getDependentUpdateSource() {
    return $dependentUpdateSource;
  }

  @Override
  final Set<Dependent<?>> getDependents() {
    return $dependentUpdateSource.getDependents();
  }

  @Override
  final void fireEvent(Event event) {
    @SuppressWarnings("unchecked")
    _Event_ typedEvent = (_Event_)event;
    $dependentUpdateSource.fireEvent(typedEvent);
  }

  private final AbstractUpdateSource<_Event_> $dependentUpdateSource;

  /*</property>*/


  /*<section name="update">*/
  //-----------------------------------------------------------------

  @Override
  protected abstract _Event_ filteredUpdate(Map<_UpdateSource_, Event> events);

  /*</section>*/

}