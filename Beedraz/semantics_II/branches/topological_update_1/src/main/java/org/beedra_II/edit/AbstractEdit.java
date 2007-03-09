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

package org.beedra_II.edit;


import org.beedra_II.Beed;
import org.beedra_II.event.Event;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * The top class for the edit framework.
 *
 * @author  Jan Dockx
 *
 * @invar getTarget() != null;
 * @invar getState() != null;
 * @invar getState() != NOT_YET_PERFORMED ?
 *          forall (ValidityListener vl) {
 *            ! isValidityListener(vl)
 *          };
 *
 * @mudo absorbing other edits; note: only if DONE or UNDONE (in NOT_YET_PERFORMED, goals can change)
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractEdit<_Target_ extends Beed<?>,
                                   _Event_ extends Event>
    extends Edit<_Target_> {


  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @pre target != null;
   * @post getTarget() == target;
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
  protected void notifyListeners() {
    updateDependents(createEvent());
  }

  protected abstract void updateDependents(_Event_ event);

  protected abstract _Event_ createEvent();

}
