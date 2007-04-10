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

package org.beedra_II.aggregate;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.beedra_II.AbstractEvent;
import org.beedra_II.Event;
import org.beedra_II.edit.Edit;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Events that report an indirect change, triggered by one or more
 *   events on other sources. Changes of a beed might propagate to other beeds,
 *   e.g., if a component changes, also the aggregate is considered
 *   changed. These changes might come together via the propagation
 *   graph, so that 1 aggregate beed has to report on several changes
 *   upstream.</p>
 * <p>The {@link #getEdit()} of an aggregate event and all its component
 *   events are the same.</p>
 * <p>Aggregate events should not be exposed before they are
 *   {@link #isClosed() closed}.</p>
 *
 * @author Jan Dockx
 *
 * @invar getComponentEvents() != null;
 * @invar Collections.noNull(getComponentEvents());
 * @invar for (Event event : getComponentEvents()) {event.getEdit() == getEdit()}.
 * @invar for (Event event : getComponentEvents()) {event.getEditState() == getEditState()}.
 * @invar getSource() instanceof AggregateBeed;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class AggregateEvent
    extends AbstractEvent {

  /**
   * @pre source != null;
   * @pre getEdit() != edit;
   * @pre (edit.getState() == DONE) || (edit.getState() == UNDONE)
   * @post getSource() == source;
   * @post getEdit() == getEdit();
   * @post getEditState() == edit.getState();
   * @post ! isClosed();
   */
  public AggregateEvent(AggregateBeed source, Edit<?> edit) {
    super(source, edit);
  }

  /**
   * @basic
   */
  public final boolean isClosed() {
    return $closed;
  }

  /**
   * @post ! isClosed();
   */
  public final void close() {
    $closed = true;
  }

  private boolean $closed;

  /**
   * @basic
   */
  public final Set<Event> getComponentevents() {
    return Collections.unmodifiableSet($componentEvents);
  }

  /**
   * @pre event != null;
   * @post getComponentEvents().contains(event);
   * @post 'isClosed() ? false;
   * @throws AggregateEventStateException
   *         isClosed();
   */
  public final void addComponentEvent(Event event) throws AggregateEventStateException {
    if (isClosed()) {
      throw new AggregateEventStateException(this, "cannot add component events when aggregate event is closed");
    }
    $componentEvents.add(event);
  }

  private final Set<Event> $componentEvents = new HashSet<Event>();

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", " + $componentEvents.size() + " component events";
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "component events ("+ $componentEvents.size() + "):\n");
    for (Event event : $componentEvents) {
      event.toString(sb, level + 1);
    }
  }

}

