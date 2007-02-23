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


import static org.beedra_II.edit.Edit.State.DONE;
import static org.beedra_II.edit.Edit.State.UNDONE;

import org.beedra_II.Beed;
import org.beedra_II.EditableBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * The top interface for the edit framework. Implementations can derive
 * from {@link AbstractEdit} to get implementations for most public methods.
 * The main reason for the existence of this interface is that the second
 * generic parameter of the {@link AbstractEdit} class is only needed
 * on protected methods, and is of no importance to public users. By using
 * this interface, they can avoid being exposed to that second generic
 * parameter.
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
public interface Edit<_Target_ extends EditableBeed<?>> {


  // MUDO docs
  public static enum State {
    NOT_YET_PERFORMED,
    @SuppressWarnings("hiding") DONE, // warning is bogus, new compiler should make suppress unnecessary
    @SuppressWarnings("hiding") UNDONE,  // warning is bogus, new compiler should make suppress unnecessary
    DEAD
  }



  /*<property name="target">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  _Target_ getTarget();

  /*</property>*/



  /*<property name="state">*/
  //-------------------------------------------------------

  /**
   * @basic
   * @init NOT_YET_PERFORMED;
   */
  State getState();

  /**
   * <p>Sets {@link #getState() state} to {@link State#DEAD}. Note that this
   *   is a one way operation: dead edits cannot be resurrected. Calling
   *   {@link #perform()}, {@link #undo()} or {@link #redo()} on a a dead edit
   *   results in an exception being thrown.</p>
   * <p>Typically an edit is killed when it is consolidated by another
   *   edit's {@link #addEdit(Edit)} or {@link #replaceEdit(Edit)} method.</p>
   *
   * @post getState() = DEAD;
   */
  void kill();

  /*</property>*/



  /*<section name="perform">*/
  //------------------------------------------------------------------

  /**
   * <p>Performs this edit. If the performance succeeds, notifies listeners
   *   of the {@link #getTarget() target} and all objects that got dirty (i.e.,
   *   the transitive closure of {@link Beed#getPropagatesChangesTo()}) with an
   *   event. The set of dirty objects is calculated within the transaction.
   *   Notification happens outside the transaction.</p>
   * <p>An Edit can only be performed once. The state is changed to
   *   {@link State#DONE}.</p>
   *
   * @post getState() == DONE;
   * @throws EditStateException
   *         'getState() != NOT_YET_PERFORMED;
   * @throws IllegalEditException
   *         ! 'isValid();
   */
  void perform() throws EditStateException, IllegalEditException;

  /*</section>*/



  /*<section name="undo">*/
  //------------------------------------------------------------------

  /**
   * <p>Undoes this edit. If the undoing succeeds, notifies listeners
   *   of the {@link #getTarget() target} and all objects that got dirty (i.e.,
   *   the transitive closure of {@link Beed#getPropagatesChangesTo()}) with an
   *   event. The set of dirty objects is calculated within the transaction.
   *   Notification happens outside the transaction.</p>
   * <p>An Edit can be undone when the edit is in state {@link #DONE}. The state
   *   is changed to {@link State#UNDONE}.</p>
   * @throws EditStateException
   *
   * @post getState() == UNDONE;
   * @throws EditStateException
   *         'getState() != DONE;
   * @throws IllegalEditException;
   *         The current state of the {@link #getTarget() target} does not
   *         correspond to the goal of this edit.
   */
  void undo() throws EditStateException, IllegalEditException;

  /*</section>*/



  /*<section name="redo">*/
  //------------------------------------------------------------------

  /**
   * <p>Redoes this edit. If the redoing succeeds, notifies listeners
   *   of the {@link #getTarget() target} and all objects that got dirty (i.e.,
   *   the transitive closure of {@link Beed#getPropagatesChangesTo()}) with an
   *   event. The set of dirty objects is calculated within the transaction.
   *   Notification happens outside the transaction.</p>
   * <p>An Edit can be redone when the edit is in state {@link #UNDONE}. The state
   *   is changed to {@link State#DONE}.</p>
   * @throws EditStateException
   *
   * @post getState() == DONE;
   * @throws EditStateException
   *         'getState() != UNDONE;
   * @throws IllegalEditException;
   *         The current state of the {@link #getTarget() target} does not
   *         correspond to the initial values of this edit.
   */
  void redo() throws EditStateException, IllegalEditException;

  /*</section>*/



  /*<section name="validity">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  boolean isValid();

  /**
   * @basic
   * @init foreach (ValidityListener l) { ! isValidityListener(l)};
   */
  boolean isValidityListener(ValidityListener listener);

  /**
   * @pre listener != null;
   * @post getState() != NOT_YET_PERFORMED ? isValidityListener(listener);
   */
  void addValidityListener(ValidityListener listener);

  /**
   * @post ! isValidityListener(listener);
   */
  void removeValidityListener(ValidityListener listener);

  /*</section>*/


  /**
   * Multiline instance information.
   * Use in debugging or logging only.
   *
   * @pre sb != null;
   * @pre level >= 0;
   */
  void toString(StringBuffer sb, int level);

}
