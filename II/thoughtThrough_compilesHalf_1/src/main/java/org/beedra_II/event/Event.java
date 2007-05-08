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

package org.beedra_II.event;


import org.beedra_II.Beed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * <p>Notifies interested {@link Listener BeedChangeListeners} of changes
 *   in {@link #getSource()}.</p>
 * <p>Events should be <em>immutable</em> during event propagation. Non-abstract
 *   subtypes should be declared {@code final} to ensure immutability. All
 *   mutating operations, if any, should throw an exception if called during
 *   event propagation.</p>
 * <p>Events are specific for the type of {@link Beed} that sends them.
 *   The event type hierarchy is <em>covariant</em> with the {@link Beed}
 *   hierarchy. The type of the events sent by a beed of type {@code B} is
 *   {@code Event&lt;B&gt;}, which should be {@code final}.</p>
 * <p>A concrete event can eventually be send by {@link Beed Beeds} of type
 *   the {@code _Source_} and subtypes.</p>
 *
 * @author Jan Dockx
 *
 * @invar getSource() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class Event<_Source_ extends Beed<_Source_, _Event_>,
                            _Event_ extends Event<_Source_, _Event_>> {

  /**
   * @pre source != null;
   * @post getSource() == source;
   */
  public Event(_Source_ source) {
    assert source != null;
    $source = source;
  }

  /**
   * @basic
   */
  public final _Source_ getSource() {
    return $source;
  }

  /**
   * @invar $source != null;
   */
  private final _Source_ $source;

}

