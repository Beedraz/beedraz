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
 * <p>Events that report an indirect change, triggered by an event on
 *   another source. Changes of a beed might propagate to other beeds,
 *   e.g., if a component changes, also the aggregate is considered
 *   changed.</p>
 *
 * @author Jan Dockx
 *
 * @invar getCause() != null;
 * @invar getSource() instanceof Beed<? extends DerivedEvent>;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class DerivedEvent
    extends AbstractEvent {

  /**
   * @pre source != null;
   * @pre cause != null;
   * @post getSource() == source;
   * @post getCause() == cause;
   */
  protected DerivedEvent(Beed<? extends DerivedEvent> source, Event cause) {
    super(source);
    assert cause != null;
    $cause = cause;
  }

  /**
   * @basic
   */
  public final Event getCause() {
    return $cause;
  }

  /**
   * @invar $cause != null;
   */
  private final Event $cause;

}

