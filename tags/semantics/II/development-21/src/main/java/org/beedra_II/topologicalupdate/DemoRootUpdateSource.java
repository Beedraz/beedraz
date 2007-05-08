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


import java.util.LinkedHashMap;

import org.beedra_II.event.Event;
import org.ppeew.annotations.vcs.CvsInfo;


/**
 * Due to the limitation to single inheritance, most
 * actual dependent update sources
 * will not extend this type, but will delegate to {@link RootUpdateSourceDelegate}.
 * The important algorithms are then reused through delegation
 * instead of inheritance. This class is mainly intended
 * to demonstrate how to do this. If the actual
 * dependent update source is not limited by inheritance to extend
 * this type, please do so.
 *
 * @author Jan Dockx
 *
 * @invar getMaximumRootUpdateSourceDistance() == 0;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class DemoRootUpdateSource
    extends AbstractUpdateSource {

  public final int getMaximumRootUpdateSourceDistance() {
    return 0;
  }

  private RootUpdateSourceDelegate $rootUpdateSourceDelegate = new RootUpdateSourceDelegate() {

    @Override
    public UpdateSource getRootUpdateSource() {
      return DemoRootUpdateSource.this;
    }

    @Override
    protected void notifyListeners(LinkedHashMap<UpdateSource, Event> events) {
      DemoRootUpdateSource.this.notifyListeners(events);
    }

  };

  protected final void updateDependents(Event event) {
    $rootUpdateSourceDelegate.updateDependents(event);
  }

  protected abstract void notifyListeners(LinkedHashMap<UpdateSource, Event> events);

//    for (Map.Entry<UpdateSource, Event> entry : events.entrySet()) {
//      UpdateSource dependentUpdateSource = entry.getKey();
//      Event dependentUpdateSourceEvent = entry.getValue();
//      dependentUpdateSource.notifyListeners(dependentUpdateSourceEvent);
//    }
//  }

}
