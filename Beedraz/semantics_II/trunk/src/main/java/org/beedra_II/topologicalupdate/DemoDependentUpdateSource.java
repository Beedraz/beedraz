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
 * Due to the limitation to single inheritance, most
 * actual dependent update sources
 * will not extend this type, but will delegate to {@link DependentDelegate}.
 * The important algorithms are then reused through delegation
 * instead of inheritance. This class is mainly intended
 * to demonstrate how to do this. If the actual
 * dependent update source is not limited by inheritance to extend
 * this type, please do so.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class DemoDependentUpdateSource<_Event_ extends Event, _UpdateSource_ extends UpdateSource>
    extends AbstractUpdateSource {

  /**
   * @basic
   */
  public int getMaximumRootUpdateSourceDistance() {
    return $dependentDelegate.getMaximumRootUpdateSourceDistance();
  }

  private final Dependent $dependentDelegate
    = new Dependent() {

      @Override
      public UpdateSource getDependentUpdateSource() {
        return DemoDependentUpdateSource.this;
      }

      @Override
      public _Event_ update(Map<UpdateSource, Event> events) {
        return DemoDependentUpdateSource.this.update(events);
      }

    };

  protected abstract _Event_ update(Map<UpdateSource, Event> events);

  /**
   * @note There is no way that the author can see to avoid the downcast
   *       that is used here. This method is unchecked.
   *
   * @basic
   */
  @SuppressWarnings("unchecked")
  protected final Set<_UpdateSource_> getUpdateSources() {
    return (Set<_UpdateSource_>)$dependentDelegate.getUpdateSources();
  }

  /**
   * @pre updateSource != null;
   * @post getUpdateSources().contains(updateSource);
   * @post updateSource.getDependents().contains(this);
   */
  protected final void addUpdateSource(_UpdateSource_ updateSource) {
    updateSource.addDependent($dependentDelegate);
  }

  /**
   * @pre updateSource != null;
   * @post ! getUpdateSources().contains(updateSource);
   * @post ! updateSource.getDependents().contains(this);
   */
  protected final void removeUpdateSource(_UpdateSource_ updateSource) {
    updateSource.removeDependent($dependentDelegate);
  }

}
