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


import org.beedra_II.event.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Implementation support for root update sources.
 * <p>Root update sources are update source that are not dependent
 *   on other update sources. There is no separate {@code RootUpdateSource}
 *   type, since there is nothing really special about such an update source.
 *   It is not strictly necessary, e.g., that the {@link #getMaximumRootUpdateSourceDistance()}
 *   of such an update source is zero, as long as it is smaller than
 *   the {@link Dependent#getMaximumRootUpdateSourceDistance()} of all
 *   its dependents (but that, frankly, is their problem).</p>
 * <p>Due to the limitation to single inheritance, most
 *   actual root update sources
 *   will not extend this type, but will copy this implementation.
 *   This class is mainly intended to demonstrate how to do this.
 *   If the actual root update source is not limited by inheritance
 *   to extend this type, you are welcome to do so.</p>
 *
 * @author Jan Dockx
 *
 * @invar getMaximumRootUpdateSourceDistance() == 0;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractRootUpdateSource<_Event_ extends Event>
    extends AbstractUpdateSource<_Event_> {

  public final int getMaximumRootUpdateSourceDistance() {
    return 0;
  }

}
