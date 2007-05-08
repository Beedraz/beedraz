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

package org.beedraz.semantics_II.edit;


import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Implementation of some general methods for {@link #Edit Edits} that
 *   benefit from generics.</p>
 * <p>This class and {@link Edit} were separated, because the methods in
 *   this class need a generic parameter that is only needed by protected
 *   methods, and is of no importance to public users, who see {@link Edit}.
 *   In general, there should be no reason for concrete edit implementations to
 *   avoid extending this class.</p>
 *
 * @author  Jan Dockx
 * @author  PeopleWare n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State: Exp $",
         tag      = "$Name:  $")
public abstract class AbstractEdit<_Target_ extends Beed<?>,
                                   _Event_ extends Event>
    extends Edit<_Target_> {


  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @pre target != null;
   * @post getTarget() == target;
   * @post getState() == NOT_YET_PERFORMED;
   */
  protected AbstractEdit(_Target_ target) {
    super(target);
  }

  /*</construction>*/



  /**
   * Should be implemented as
   * {@code getTarget().fireChangeEvent(<var>an event</var>);}.
   *
   * @todo method should be final
   */
  @Override
  protected void updateDependents() {
    updateDependents(createEvent());
  }

  /**
   * <p>Start the topological update algorithm propating changes and warning
   *   listeners of these changes, with {@code event} expressing the final
   *   change executed by this edit (either by {@link #perform()},
   *   {@link #undo()}, or {@link #redo()}).</p>
   *
   * @pre event != null;
   */
  protected abstract void updateDependents(_Event_ event);

  /**
   * <p>Create a fresh event describing the state change of the {@link #getTarget()}
   *   that is the result of this edit being {@link #perform() performed},
   *   {@link #undo() undone} or {@link #redo() redone}.</p>
   * <p>This method is called as part of the {@link #perform()} protocol,
   *   as part of the {@link #undo()} protocol, and as part of the {@link #redo()}
   *   protocol. Before this method is called, the state of the edit is already
   *   changed, so when the method is called as part of the {@link #perform()}
   *   or {@link #redo()} protocol, {@link #getState()} returns {@link State#DONE},
   *   and when the method is called as part of the {@link #undo()} protocol,
   *   {@link #getState()} returns {@link State#UNDONE}.</p>
   *
   * @result result != null;
   */
  protected abstract _Event_ createEvent();

}
