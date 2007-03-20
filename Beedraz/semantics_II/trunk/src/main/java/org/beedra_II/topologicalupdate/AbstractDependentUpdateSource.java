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
 * will not extend this type, but will copy this implementation.
 * This class is mainly intended
 * to demonstrate how to do this. If the actual
 * dependent update source is not limited by inheritance to extend
 * this type, you are welcome to do so.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractDependentUpdateSource<_Event_ extends Event,
                                                    _UpdateSource_ extends UpdateSource>
    extends AbstractUpdateSource<_Event_> {


  private final Dependent<?> $dependent = new Dependent<UpdateSource>() {

    @Override
    public UpdateSource getDependentUpdateSource() {
      return AbstractDependentUpdateSource.this;
    }

    @Override
    void fireEvent(Event event) {
      @SuppressWarnings("unchecked")
      _Event_ fireEvent = (_Event_)event;
      AbstractDependentUpdateSource.this.fireEvent(fireEvent);
    }

    @Override
    Set<Dependent<?>> getDependents() {
      return AbstractDependentUpdateSource.this.getDependents();
    }

    @Override
    Event update(Map<UpdateSource, Event> events) {
      return AbstractDependentUpdateSource.this.update(events);
    }


  };

  public final int getMaximumRootUpdateSourceDistance() {
    return $dependent.getMaximumRootUpdateSourceDistance();
  }

  protected abstract _Event_ update(Map<UpdateSource, Event> events);

}
