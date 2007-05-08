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


import static org.beedra.util_I.MultiLineToStringUtil.indent;

import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
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
 * @invar getEdit() != null;
 * @invar getEdit() == getCause().getEdit();
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class PropagatedEvent
    extends Event<Edit<?>> {

  /**
   * @pre source != null;
   * @pre cause != null;
   * @pre cause.getEdit() != null;
   * @pre (cause.getEdit().getState() == DONE) || (cause.getEdit().getState() == UNDONE)
   * @post getSource() == source;
   * @post getCause() == cause;
   * @post getEdit() != cause.getEdit();
   * @post getEditState() == cause.getEdit().getState();
   */
  public PropagatedEvent(AggregateBeed source, Event<?> cause) {
    super(source, cause.getEdit());
    $cause = cause;
  }

  /**
   * @basic
   */
  public final Event<?> getCause() {
    return $cause;
  }

  /**
   * @invar $cause != null;
   */
  private final Event<?> $cause;

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", cause: " + getCause();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "cause:\n");
    getCause().toString(sb, level + 2);
  }

}

