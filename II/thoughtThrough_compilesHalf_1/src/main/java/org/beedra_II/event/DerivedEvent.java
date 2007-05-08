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
 * <p>Events that reports an indirect change, triggered by an event on
 *   another source. Changes of a beed might propagate to other beeds,
 *   e.g., if a a component changes, also the aggregate is considered
 *   changed.</p>
 *
 * @author Jan Dockx
 *
 * @invar getSource() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class DerivedEvent<_Source_ extends Beed<_Source_, _Event_>,
                                   _Event_ extends Event<_Source_, _Event_>>
    extends Event<_Source_, _Event_> {

  /**
   * @pre source != null;
   * @pre cause != null;
   * @post getSource() == source;
   * @post getCause() == cause;
   */
  public DerivedEvent(_Source_ source, Event<?, ? extends Beed> cause) {
    super(source);
    $cause = cause;
  }

  /**
   * @basic
   */
  public final Event<?, ? extends Beed> getCause() {
    return $cause;
  }

  /**
   * @invar $cause != null;
   */
  private final Event<?, ? extends Beed> $cause;

}

