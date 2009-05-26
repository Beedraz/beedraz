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

package org.beedraz.semantics_II;


import static org.beedraz.semantics_II.Edit.State.DEAD;
import static org.beedraz.semantics_II.Edit.State.DONE;
import static org.beedraz.semantics_II.Edit.State.NOT_YET_PERFORMED;
import static org.beedraz.semantics_II.Edit.State.UNDONE;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.indent;
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.objectToString;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Edits are the only way to change the state of {@link #getTarget() target}
 *   {@link Beed editable beeds}. They are <em>reified mutators</em>, that
 *   support {@link #undo() undoability} and {@link #redo() redoability}.</p>
 * <p>The {@link #getTarget()} of an edit is the subject of the mutation.
 *   The {@link #getTarget()} of an edit cannot change.</p>
 * <p>Edits feature a <em>edit target goal state</em> and an <em>edit target
 *   initial state</em>. These states are not implemented in this class,
 *   but differently in different subtypes.</p>
 * <p>Edits go through different {@link #getState() states} through their lifetime.
 *   Initially, the edit is {@link State#NOT_YET_PERFORMED}. Once
 *   {@link #perform() performed}, the edit becomes {@link State#DONE}. From this
 *   state, the edit can be used to revert the {@link #getTarget()} to the state
 *   it had before the edit was {@link #perform() performed}, by calling the
 *   method {@link #undo()}. The state of the edit then becomes {@link State#UNDONE}.
 *   From the {@link State#UNDONE}, the change the edit represents can be
 *   redone by calling {@link #redo()}, which reverts the state to
 *   {@link State#DONE}. From the state {@link State#NOT_YET_PERFORMED},
 *   {@link State#DONE} and {@link State#UNDONE}, the edit can be made
 *   inoperative by calling the {@link #kill()}, which brings the edit
 *   to state {@link State#DEAD}. An edit can never leave this state anymore.
 *   State changes can be observed with an {@link EditStateChangeListener}.</p>
 * <img src="doc-files/edit/img/EditStateMachine.png" />
 * <p>The <em>edit target goal state</em> is the state the edit will bring the
 *   target in once {@link #perform() performed} or {@link #redo() redone}. As long
 *   as the edit is {@link State#NOT_YET_PERFORMED}, the <em>edit target goal
 *   state</em> can be changed by the user. The <em>edit target goal state</em> can
 *   be {@link #isValid() valid or invalid}. When the <em>edit target goal state</em>
 *   is {@link #isValid() invalid}, {@link #perform() performing the edit} fails with
 *   an {@link IllegalEditException}. As long as the edit is
 *   {@link State#NOT_YET_PERFORMED}, the user can register {@link ValidityListener
 *   ValidityListeners} with the edit, that are warned when the validity of the
 *   <em>edit target goal state</em> changes. When the edit is
 *   {@link #perform() performed()}, validity listeners are removed, and warned
 *   of this removal by a call to {@link ValidityListener#listenerRemoved(Edit)}.</p>
 * <p>The <em>edit target initial state</em> is the state of the {@link #getTarget()}
 *   immediately before the edit is {@link #perform() performed}. During the execution
 *   of the method {@link #perform()}, the <em>edit target initial state</em> is
 *   stored by the edit. It is used by {@link #undo()} to revert the
 *   {@link #getTarget()} to that state. Once the edit is {@link #perform() performed},
 *   the <em>edit target initial state</em> can no longer change. When the edit
 *   is {@link State#NOT_YET_PERFORMED}, the values returned by inspectors for the
 *   <em>edit target initial state</em> have no semantics.</p>
 *
 * @idea consider an exception when calling inspectors for the <em>edit target initial
 *       state</em> when the edit is not yet performed
 *
 * @invar getTarget() != null;
 * @invar getTarget() == 'getTarget();
 * @invar getState() != null;
 * @invar getState() != NOT_YET_PERFORMED ?
 *          forall (ValidityListener vl) {
 *            ! isValidityListener(vl)
 *          };
 *
 * @protected
 * <p>This is a framework template class. There are many hook methods (mostly
 *   protected) for subclasses to implement to define the precise behavior.
 *   Subclasses also need to provide for a defintion of <em>edit target
 *   state</em>, to be used in the <em>edit target goal state</em> and
 *   <em>edit target initial state</em>.</p>
 * <p>Although this class offers implementations for most methods, concrete
 *   edit implementations can derive from {@link AbstractEdit} to get more support.
 *   This class and {@link AbstractEdit} were separated, because the methods in
 *   {@link AbstractEdit} need a generic parameter that is only needed
 *   by protected methods, and is of no importance to public users. By using
 *   this type, public users avoid being exposed to that second generic parameter.
 *   In general, there should be no reason for concrete edit implementations to
 *   avoid extending {@link AbstractEdit}. A separation in an {@code Edit}
 *   <em>interface</em> and an {@link AbstractEdit} abstract class, as is customary,
 *   is not possible here, because {@link CompoundEdit} needs access to package
 *   accessible methods of element edits, which can not be defined in an
 *   interface.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @mudo absorbing other edits; note: only if DONE or UNDONE (in NOT_YET_PERFORMED, goals can change)
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class Edit<_Target_ extends Beed<?>> {

  /**
   * Possible states of an {@link Edit}.
   */
  public static enum State {

    /**
     * <p>The edit is <em>not yet performed</em>, because neither
     *   {@link Edit#perform()} nor {@link Edit#kill()} was called on the edit
     *   yet. This state is the initial state of an {@link Edit}, after
     *   construction, and there is no other way to get to this state.
     * <p>The edit target goal state can be changed.</p>
     * <p>Calling {@link Edit#perform()} from this state changes the state
     *   to {@link #DONE}. Calling {@link Edit#kill()} from this state changes
     *   the state to {@link #DEAD}. {@link Edit#undo()} nor {@link Edit#redo()}
     *   can be called from this state.</p>
     */
    NOT_YET_PERFORMED,

    /**
     * <p>The edit is <em>done</em>, either because {@link Edit#perform()} or
     *   {@link Edit#redo()} was called as the last state-changing method on the
     *   edit, from state {@link #NOT_YET_PERFORMED} or {@link #UNDONE},
     *   respectively.</p>
     * <p>The edit target goal state cannot be changed.</p>
     * <p>Calling {@link Edit#undo()} from this state changes the state to
     *   {@link #UNDONE}. Calling {@link Edit#kill()} from this state changes
     *   the state to {@link #DEAD}. {@link Edit#perform()} nor {@link Edit#redo()}
     *   can be called from this state.</p>
     */
    DONE,

    /**
     * <p>The edit is <em>undone</em>, because {@link Edit#undo()} was called as the
     *   last state-changing method on the edit, from state {@link #DONE}.</p>
     * <p>The edit target goal state cannot be changed.</p>
     * <p>Calling {@link Edit#redo()} from this state changes the state to
     *   {@link #DONE}. Calling {@link Edit#kill()} from this state changes
     *   the state to {@link #DEAD}. {@link Edit#perform()} nor {@link Edit#undo()}
     *   can be called from this state.</p>
     */
    UNDONE,

    /**
     * <p>The edit is <em>dead</em>, because {@link Edit#kill()} was called as the
     *   last state-changing method on the edit, from either state
     *   {@link #NOT_YET_PERFORMED}, {@link #DONE} or {@link #UNDONE}.</p>
     * <p>The edit target goal state cannot be changed.</p>
     * <p>From this state, the edit is frozen, and the state can no longer
     *   change.</p>
     */
    DEAD

  }



  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @pre target != null;
   * @post getTarget() == target;
   * @post getState() == NOT_YET_PERFORMED;
   */
  protected Edit(_Target_ target) {
    assert target != null;
    $target = target;
  }

  /*</construction>*/



  /*<property name="target">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final _Target_ getTarget() {
    return $target;
  }

  /**
   * @invar $target != null;
   */
  private final _Target_ $target;

  /*</property>*/



  /*<property name="state">*/
  //-------------------------------------------------------

  /**
   * @basic
   * @init NOT_YET_PERFORMED;
   */
  public final State getState() {
    return $state;
  }

  /**
   * Method added only for testing reasons.
   * Remove this method when tests are more black-box.
   */
  protected final void forceDone() {
    changeState(DONE);
  }

  private void changeState(State newState) {
    assert $state != null;
    assert $state != DEAD;
    assert $state == NOT_YET_PERFORMED ? newState == DONE || newState == DEAD : true;
    assert $state == DONE ? newState == UNDONE || newState == DEAD : true;
    assert $state == UNDONE ? newState == DONE || newState == DEAD : true;
    assert newState != NOT_YET_PERFORMED;
    State oldState = $state;
    $state = newState;
    notifyStateChange(oldState);
  }

  /**
   * @invar $state != null;
   */
  private State $state = NOT_YET_PERFORMED;



  /*<section name="listeners">*/

  /**
   * @basic
   * @init foreach (EditStateChangeListener escl) { ! isStateChangeListener(escl)};
   */
  public final boolean isStateChangeListener(EditStateChangeListener escl) {
    return $stateChangeListeners.contains(escl);
  }

  /**
   * @pre  escl != null;
   * @post isStateChangeListener(escl);
   */
  public final void addStateChangeListener(EditStateChangeListener escl) {
    $stateChangeListeners.add(escl);
  }

  /**
   * @post ! isStateChangeListener(escl);
   */
  public final void removeStateChangeListener(EditStateChangeListener escl) {
    $stateChangeListeners.remove(escl);
  }

  private void notifyStateChange(State oldState) {
    assert oldState != null;
    assert oldState != DEAD;
    assert oldState == NOT_YET_PERFORMED ? $state == DONE || $state == DEAD : true;
    assert oldState == DONE ? $state == UNDONE || $state == DEAD : true;
    assert oldState == UNDONE ? $state == DONE || $state == DEAD : true;
    assert $state != NOT_YET_PERFORMED;
    for (EditStateChangeListener escl : $stateChangeListeners) {
      escl.stateChanged(this, oldState, $state);
    }
  }

  /**
   * @invar $stateChangeListeners != null;
   */
  private final Set<EditStateChangeListener> $stateChangeListeners =
    new HashSet<EditStateChangeListener>();

  /*</section>*/

  /*</property>*/



  /*<property name="change">*/
  //-------------------------------------------------------

  /**
   * The goal state is different from the stored
   * initial state, or, if none is stored yet
   * ({@link #getState()}{@code == }{@link State#NOT_YET_PERFORMED}),
   * from the current state of the target.
   */
  public abstract boolean isChange();

  /*</property>*/



  /*<section name="perform">*/
  //------------------------------------------------------------------

  /**
   * <p>Performs this edit. If the performance succeeds, notifies listeners
   *   of the {@link #getTarget() target} and all objects that got dirty
   *   (i.e., the transitive closure of {@link Beed#getDependents()})
   *   with an event.</p>
   * <p>An Edit can only be performed once. The state is changed to
   *   {@link State#DONE}.</p>
   *
   * @post getState() == DONE;
   * @throws EditStateException
   *         'getState() != NOT_YET_PERFORMED;
   * @throws IllegalEditException
   *         ! 'isValid();
   *
   * <p><em>Hook method:</em> This method calls {@link #performance()} to do
   *   the actual work. Here we take care of checking the state of the edit,
   *   setting the state of the edit object, gathering the objects that are
   *   dirty and sending events. Override {@link #storeInitialState()},
   *   {@link #performance()}, {@link #markPerformed()} and {@link #updateDependents()}
   *   to get the desired effect.</p>
   *
   * @protected-post storeInitialState();
   *                 (isChange() ?
   *                   (recalculateValidity();
   *                    checkValidity();
   *                    performance();
   *                    markPerformed();
   *                    updateDependents()) :
   *                   (markPerformed()));
   */
  public final void perform() throws EditStateException, IllegalEditException {
    // IDEA start of transaction
    if (getState() != NOT_YET_PERFORMED) {
        throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    storeInitialState();
      /* This is not undone when performance fails: there is no need.
       * Edit target initial state inspectors are undefined when we are
       * NOT_YET_PERFORMED anyway.
       */
    if (isChange()) {
      recalculateValidity();
        /* between setting of the goal and perform, the model might have changed
           (other beeds than the target) */
      checkValidity(); // throws IllegalEditException
      performance(); // throws IllegalEditException
      markPerformed();
      updateDependents();
    }
    else {
      markPerformed();
    }
  }

  /**
   * Store the initial state of the beed, the state immediately before the edit
   * was performed, in the edit. This makes undoability possible. This method is
   * called as part of the {@link #perform()} protocol.
   *
   * @pre  getState() == NOT_YET_PERFORMED;
   */
  protected abstract void storeInitialState();

  /**
   * Perform the change on the {@link #getTarget()}: the state of the
   * {@link #getTarget()} changes from the current state to the <em>edit
   * target goal state</em>. If this fails, we throw an exception.
   * This method is called as part of the {@link #perform()} protocol
   * and as part of the {@link #redo()} protocol.
   *
   * @pre  getState() == NOT_YET_PERFORMED || UNDONE;
   */
  protected abstract void performance() throws IllegalEditException;

  /**
   * Mark the edit as {@link State#DONE}, and remove
   * validity listeners. This method is called as part of the
   * {@link #perform()} protocol. {@link #localMarkPerformed()}
   * does this for this edit, and should be used in the implementation of this
   * method.
   *
   * @pre  getState() == NOT_YET_PERFORMED;
   */
  protected abstract void markPerformed();

  /**
   * Set the {@link #getState()} to {@link State#DONE} and remove validity
   * listeners for this edit. This method is to be used in the implementation
   * of {@link #markPerformed()}.
   *
   * @pre  getState() == NOT_YET_PERFORMED
   * @post getState() == DONE;
   * @post for (ValidityListener vl) {! isValidityListener(vl)};
   * @post for (ValidityListener vl) {vl.isRemoved()};
   */
  protected final void localMarkPerformed() {
    changeState(DONE);
    removeValidityListeners();
  }

  /*</section>*/



  /*<section name="undo">*/
  //------------------------------------------------------------------

  /**
   * <p>Undoes this edit. If the undoing succeeds, notifies listeners
   *   of the {@link #getTarget() target} and all objects that got dirty (i.e.,
   *   the transitive closure of {@link Beed#getDependents()}) with an
   *   event.</p>
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
   *
   * @protected
   * <p><em>Hook method:</em> This method calls {@link #unperformance()} to do
   *   the actual work. Here we take care of checking the state of the edit,
   *   setting the state of the edit object, and sending events. Override
   *   {@link #isGoalStateCurrent()}, {@link #unperformance()}, and
   *   {@link #updateDependents()} to get the desired effect.</p>
   *
   * @protected-post unperformance();
   *                 getState() == UNDONE;
   *                 updateDependents();
   */
  public final void undo() throws EditStateException, IllegalEditException {
    if (getState() != DONE) {
        throw new EditStateException(this, getState(), DONE);
    }
    // start of transaction
    if (! isGoalStateCurrent()) {
      // end of transaction
      throw new IllegalEditException(this, null);
    }
    // MUDO is this always valid? explain!
    unperformance(); // throws IllegalEditException
    markUndone();
    // end of transaction if unperformance succeeded
    updateDependents();
  }

  /**
   * Check whether the <em>edit target goal state</em> is the current
   * state of the {@link #getTarget() target}. This should be
   * so for {@link #undo()} to be meaningful.
   * This method is called as part of the {@link #undo()} protocol.
   */
  protected abstract boolean isGoalStateCurrent();

  /**
   * Perform the undo-change on the {@link #getTarget()}: the state of the
   * {@link #getTarget()} changes from the current state to the <em>edit
   * target initial state</em>. If this fails, we throw an exception.
   * This method is called as part of the {@link #undo()} protocol.
   *
   * @pre  getState() == DONE;
   */
  protected abstract void unperformance() throws IllegalEditException;

  /**
   * Mark the edit as {@link State#UNDONE}. This method is called as part
   * of the {@link #undo()} protocol. {@link #localMarkUndone()}
   * does this for this edit, and should be used in the implementation of this
   * method.
   *
   * @pre  getState() == DONE;
   */
  protected abstract void markUndone();

  /**
   * Set the {@link #getState()} to {@link State#UNDONE}. This method is to
   * be used in the implementation of {@link #markUndone()}.
   *
   * @pre  getState() == DONE;
   * @post getState() == UNDONE;
   */
  protected final void localMarkUndone() {
    changeState(UNDONE);
  }

  /*</section>*/



  /*<section name="redo">*/
  //------------------------------------------------------------------

  /**
   * <p>Redoes this edit. If the redoing succeeds, notifies listeners
   *   of the {@link #getTarget() target} and all objects that got dirty (i.e.,
   *   the transitive closure of {@link Beed#getDependents()}) with an
   *   event.</p>
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
   *
   * @protected
   * <p><em>Hook method:</em> This method calls {@link #performance()} to do
   *   the actual work. Here we take care of checking the state of the edit,
   *   setting the state of the edit object, and sending events. Override
   *   {@link #isInitialStateCurrent()}, {@link #performance()}, and
   *   {@link #updateDependents()} to get the desired effect.</p>
   *
   * @protected-post performance();
   *                 getState() == UNDONE;
   *                 updateDependents();
   */
  public final void redo() throws EditStateException, IllegalEditException {
    if (getState() != UNDONE) {
      throw new EditStateException(this, getState(), UNDONE);
    }
    // start of transaction
    if (! isInitialStateCurrent()) {
      // end of transaction
      throw new IllegalEditException(this, null);
    }
    // MUDO is this always valid? explain!
    reperformance(); // throws IllegalEditException
    markRedone();
    // end of transaction if performance succeeded
    updateDependents();
  }

  /**
   * Check whether the <em>edit target initial state</em> is the current
   * state of the {@link #getTarget() target}. This should be
   * so for {@link #redo()} to be meaningful.
   * This method is called as part of the {@link #redo()} protocol.
   */
  protected abstract boolean isInitialStateCurrent();

  /**
   * Perform the redo-change on the {@link #getTarget()}: the state of the
   * {@link #getTarget()} changes from the current state to the <em>edit
   * target goal state</em>. If this fails, we throw an exception.
   * This method is called as part of the {@link #redo()} protocol.
   *
   * @pre  getState() == UNDONE;
   */
  protected abstract void reperformance() throws IllegalEditException;

  /**
   * Mark the edit as {@link State#DONE}. This method is called as part
   * of the {@link #redo()} protocol. {@link #localMarkRedone()}
   * does this for this edit, and should be used in the implementation of this
   * method.
   *
   * @pre  getState() == UNDONE;
   */
  protected abstract void markRedone();

  /**
   * Set the {@link #getState()} to {@link State#DONE}. This method is to
   * be used in the implementation of {@link #markRedone()}.
   *
   * @pre  getState() == UNDONE;
   * @post getState() == DONE;
   */
  protected final void localMarkRedone() {
    changeState(DONE);
  }

  /*</section>*/



  /*<section name="topological update">*/
  //-------------------------------------------------------

  /**
   * <p>Start the topological update algorithm propating changes and warning
   *   listeners of these changes, as a result of this edit being
   *   {@link #perform() performed}, {@link #undo() undone} or
   *   {@link #redo() redone}.</p>
   * <p>This method is called as part of the {@link #perform()} protocol,
   *   as part of the {@link #undo()} protocol, and as part of the {@link #redo()}
   *   protocol. Before this method is called, the state of the edit is already
   *   changed, so when the method is called as part of the {@link #perform()}
   *   or {@link #redo()} protocol, {@link #getState()} returns {@link State#DONE},
   *   and when the method is called as part of the {@link #undo()} protocol,
   *   {@link #getState()} returns {@link State#UNDONE}.</p>
   */
  private void updateDependents() {
    TopologicalUpdate.topologicalUpdate(createEvents(), this);
  }

  /**
   * <p>Create fresh events describing the state change
   *   that is the result of this edit being {@link #perform() performed},
   *   {@link #undo() undone} or {@link #redo() redone}.</p>
   * <p>This method is called as part of the {@link #perform()} protocol,
   *   as part of the {@link #undo()} protocol, and as part of the {@link #redo()}
   *   protocol. Before this method is called, the state of the edit is already
   *   changed, so when the method is called as part of the {@link #perform()}
   *   or {@link #redo()} protocol, {@link #getState()} returns {@link State#DONE},
   *   and when the method is called as part of the {@link #undo()} protocol,
   *   {@link #getState()} returns {@link State#UNDONE}.</p>
   * <p>The events describe the complete change from old to new state. In creating
   *   this event, that describes the change the target has just undergone, the
   *   implementation should use only information available in the edit, since
   *   the state of the target might already have changed further, after
   *   {@link #performance()} or {@link #unperformance()} execution, before
   *   this method is called. Remember that {@link #storeInitialState()} has
   *   been called already, and that the edit should have enough information
   *   about the goal state to make this feasible.</p>
   *
   * @result result != null;
   */
  protected abstract Map<AbstractBeed<?>, ? extends Event> createEvents();

  /*</section>*/



  /*<section name="kill">*/
  //-------------------------------------------------------

  /**
   * <p>Sets {@link #getState() state} to {@link State#DEAD} and removes
   *   validity listeners. Note that this is a one way operation: dead edits
   *   cannot be resurrected. Calling {@link #perform()}, {@link #undo()} or
   *   {@link #redo()} on a a dead edit results in an exception being thrown.</p>
   *
   * @post getState() = DEAD;
   * @post for (ValidityListener vl) {! isValidityListener(vl)};
   * @post for (ValidityListener vl) {vl.isRemoved()};
   *
   * @protected
   * {@link #localKill()} does the required work for this edit, and should be
   * used in the implementation of this method.
   */
  public abstract void kill();

  /**
   * Change the {@link #getState() state} to {@link State#DEAD} and remove
   * validity listeners for this edit. This method is to be used in the
   * implementation of {@link #kill}.
   *
   * @post getState() == DEAD;
   * @post for (ValidityListener vl) {! isValidityListener(vl)};
   * @post for (ValidityListener vl) {vl.isRemoved()};
   */
  protected final void localKill() {
    if ($state != DEAD) { // optimization, especially when triggered for component edit from compound edit
      changeState(DEAD);
      /* no longer allowed to change and registered ValidityChangedListeners
          are all deregistred */
      removeValidityListeners();
    }
  }

  /*</section>*/



  /*<section name="validity">*/
  //------------------------------------------------------------------

  /**
   * The <em>edit target goal state</em> would be valid as
   * <em>target state</em>.
   *
   * @basic
   */
  public final boolean isValid() {
    return $valid;
  }

  /**
   * {@link #isValid() validity is cached}. Recalculate
   * the validity using {@link #isAcceptable()}, and warn
   * {@link #isValidityListener(ValidityListener) validity listeners} if
   * validity changes.
   *
   * @post isValid() == isAcceptable();
   */
  protected final void recalculateValidity() {
    boolean newValidity = isAcceptable();
    if ($valid != newValidity) {
      $valid = newValidity;
      fireValidityChange();
    }
  }

  /**
   * Subtypes should implement this method to check whether the
   * <em>edit target goal state</em> would be acceptable currently
   * as <em>edit target state</em>. The result need only be meaningful
   * when the state is {@link State#NOT_YET_PERFORMED}.
   */
  protected abstract boolean isAcceptable();

  /**
   * Convenience method, that throws an exception when the edit
   * is {@link #isValid() invalid}.
   *
   * Package accessible: used in {@link CompoundEdit} on element edits.
   *
   * @post isValid();
   * @throws IllegalEditException
   *         ! isValid();
   */
  final void checkValidity() throws IllegalEditException {
    if (! isValid()) {
      throw new IllegalEditException(this, null);
    }
  }

  /**
   * Warn registered {@link ValidityListener ValidityListeners} of
   * a change in <em>edit target goal state validity</em>.
   *
   * @invar foreach (ValidityListener l: isValidityListener(l)) {
   *          l.validityChanged(this, isValid())
   *        };
   *
   */
  private final void fireValidityChange() {
    for (ValidityListener listener : $validityListeners) {
      listener.validityChanged(this, $valid);
    }
  }

  private boolean $valid = true;


  /*<section name="validity listeners">*/
  //------------------------------------------------------------------

  /**
   * @basic
   * @init foreach (ValidityListener l) { ! isValidityListener(l)};
   */
  public final boolean isValidityListener(ValidityListener listener) {
    return ($validityListeners != null) && $validityListeners.contains(listener);
  }

  /**
   * @pre listener != null;
   * @post getState() != NOT_YET_PERFORMED ? isValidityListener(listener);
   */
  public final void addValidityListener(ValidityListener listener) {
    assert listener != null;
    if (($validityListeners != null) && ($state == NOT_YET_PERFORMED)) {
      $validityListeners.add(listener);
    }
  }

  /**
   * @post ! isValidityListener(listener);
   */
  public final void removeValidityListener(ValidityListener listener) {
    if ($validityListeners != null) {
      $validityListeners.remove(listener);
    }
  }

  /**
   * Removes all validity listeners, and warns them about this
   * by calling {@link ValidityListener#listenerRemoved(Edit)}.
   */
  private void removeValidityListeners() {
    if ($validityListeners != null) {
      for (ValidityListener listener : $validityListeners) {
        listener.listenerRemoved(this);
      }
      $validityListeners = null;
    }
  }

  /**
   * @invar $validityListeners != null ? Collections.noNull($validityListeners);
   */
  private Set<ValidityListener> $validityListeners = new HashSet<ValidityListener>();

  /*</section>*/

  /*</section>*/



  /*<section name="toString">*/
  //------------------------------------------------------------------

  @Override
  public final String toString() {
    return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) +
           "[" + otherToStringInformation() + "]";
  }

  protected String otherToStringInformation() {
    return "target: " + getTarget() +
           ", state: " + getState() +
           ", valid: " + isValid();
  }

  /**
   * Multiline instance information.
   * Use in debugging or logging only.
   *
   * @pre sb != null;
   * @pre level >= 0;
   */
  public void toString(StringBuffer sb, int level) {
    assert sb != null;
    objectToString(this, sb, level);
    sb.append(indent(level + 1) + "targer:\n");
    getTarget().toString(sb, level + 2);
    sb.append(indent(level + 1) + "state: " + getState() + "\n");
    sb.append(indent(level + 1) + "valid: " + isValid() + "\n");
  }

  /*</section>*/

}
