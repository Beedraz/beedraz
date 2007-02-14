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


import static org.beedra.util_I.MultiLineToStringUtil.objectToString;
import static org.beedra.util_I.MultiLineToStringUtil.indent;
import static org.beedra_II.edit.Edit.State.DONE;
import static org.beedra_II.edit.Edit.State.NOT_YET_PERFORMED;
import static org.beedra_II.edit.Edit.State.UNDONE;

import java.util.HashSet;
import java.util.Set;

import org.beedra_II.Beed;
import org.beedra_II.EditableBeed;
import org.beedra_II.event.Event;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * The top interface for the edit framework. We advise the use of the actual
 * classes in this package. If the do not provide the functionality you need,
 * you can implement your own version implementing this interface.
 *
 * @author  Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractEdit<_Target_ extends EditableBeed<_Event_>,
                                   _Event_ extends Event<?>>
    implements Edit<_Target_> {


  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @pre target != null;
   * @post getTarget() == target;
   */
  protected AbstractEdit(_Target_ target) {
    $target = target;
  }

  /*</construction>*/



  /*<property name="target">*/
  //-------------------------------------------------------

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

  public final State getState() {
    return $state;
  }

  public final void kill() {
    $state = State.DEAD;
    /* allowed to change and registered ValidityChangedListeners
        are all deregistred */
    removeValidityListeners();
  }

  /**
   * @invar $state != null;
   */
  private State $state = NOT_YET_PERFORMED;

  /*</property>*/



  /*<section name="perform">*/
  //------------------------------------------------------------------

  /**
   * {@inheritDoc}
   *
   * <p><em>Hook method:</em> This method calls {@link #performance()} to do
   *   the actual work. Here we take care of checking the state of the edit,
   *   setting the state of the edit object, gathering the objects that are
   *   dirty and sending events. Override {@link #storeInitialState()},
   *   {@link #performance()}, and {@link Beed#propagateChangesTo()} to get the
   *   desired effect.</p>
   */
  public final void perform() throws EditStateException, IllegalEditException {
    // IDEA start of transaction
    if ($state != NOT_YET_PERFORMED) {
        throw new EditStateException(this, $state, NOT_YET_PERFORMED);
    }
    storeInitialState();
    if (isChange()) {
      checkValidity(); // between setting of the goal and perform, the model might have changed
  //    updateValidityWithGoal(); // MUDO OLD COMMENT this is not exactly what we want; it doesn't recalulate enough, but potentially sends validity changed events JDJDJD
      // MUDO why is this update here? a! the other beed might have changed
      if (! isValid()) {
        throw new IllegalEditException(this, null);
      }
      performance(); // throws IllegalEditException
      buildChangedObjects(this);
          /* Building the list of changed objects is done here and not when
             the edit object is constructed, because the object structure the
             edit has to work on could have changed in the mean time. The
             structure will have changed by the time undo (or redo) would be
             called too, of course, but that is no problem since being changed
             is symmetric for all edits, and the state of the entire object
             structure will be the same for redo, and for undo, apart from
             the edit itself. */
      $state = DONE;
      // end of transaction if performance succeeded
      removeValidityListeners();
      notifyListeners();
    }
    else {
      $state = DONE;
    }
  }

  /**
   * Store the initial state of the beed, the state immediately before the edit
   * was performed, in the edit. This makes undoability possible.
   */
  protected abstract void storeInitialState();

  /**
   * The goal state is different from the stored
   * initial state.
   */
  protected abstract boolean isChange();

  /*</section>*/



  /*<section name="undo">*/
  //------------------------------------------------------------------

  /**
   * {@inheritDoc}
   *
   * @protected
   * <p><em>Hook method:</em> This method calls {@link #unperformance()} to do
   *   the actual work. Here we take care of checking the state of the edit,
   *   setting the state of the edit object, gathering the objects that are
   *   dirty and sending events. Override {@link #isGoalStateCurrent()},
   *   {@link #unperformance()}, and {@link Beed#propagateChangesTo()} to get the
   *   desired effect.</p>
   */
  public final void undo() throws EditStateException, IllegalEditException {
    if ($state != DONE) {
        throw new EditStateException(this, DONE, $state);
    }
    // start of transaction
    if (! isGoalStateCurrent()) {
      // end of transaction
      throw new IllegalEditException(this, null);
    }
    // MUDO is this always valid? explain!
    unperformance(); // throws IllegalEditException
    buildChangedObjects(this);
    /* Building the list of changed objects is done here and not when
       the edit object is constructed, because the object structure the
       edit has to work on could have changed in the mean time. The
       structure will have changed by the time undo (or redo) would be
       called too, of course, but that is no problem since being changed
       is symmetric for all edits, and the state of the entire object
       structure will be the same for redo, and for undo, apart from
       the edit itself. */
    $state = UNDONE;
    // end of transaction if unperformance succeeded
    notifyListeners();
  }

  /**
   * Check whether the goal state of this edit is the current
   * state of the {@link #getTarget() target}. This should be
   * so for undo to be meaningful.
   */
  protected abstract boolean isGoalStateCurrent();

  /*</section>*/



  /*<section name="redo">*/
  //------------------------------------------------------------------

  /**
   * {@inheritDoc}
   *
   * @protected
   * <p><em>Hook method:</em> This method calls {@link #performance()} to do
   *   the actual work. Here we take care of checking the state of the edit,
   *   setting the state of the edit object, gathering the objects that are
   *   dirty and sending events. Override {@link #isInitialStateCurrent()},
   *   {@link #performance()}, and {@link Beed#propagateChangesTo()} to get the
   *   desired effect.</p>
   */
  final public void redo() throws EditStateException, IllegalEditException {
    if ($state != UNDONE) {
      throw new EditStateException(this, UNDONE, $state);
    }
    // start of transaction
    if (! isInitialStateCurrent()) {
      // end of transaction
      throw new IllegalEditException(this, null);
    }
    // MUDO is this always valid? explain!
    performance(); // throws IllegalEditException
    buildChangedObjects(this);
    /* Building the list of changed objects is done here and not when
       the edit object is constructed, because the object structure the
       edit has to work on could have changed in the mean time. The
       structure will have changed by the time undo (or redo) would be
       called too, of course, but that is no problem since being changed
       is symmetric for all edits, and the state of the entire object
       structure will be the same for redo, and for undo, apart from
       the edit itself. */
    $state = DONE;
    // end of transaction if performance succeeded
    notifyListeners();
  }

  /**
   * Check whether the initial state of this edit is the current
   * state of the {@link #getTarget() target}. This should be
   * so for redo to be meaningful.
   */
  protected abstract boolean isInitialStateCurrent();

  /*</section>*/



  /*<section name="un-performance">*/
  //------------------------------------------------------------------

  // MUDO docs
  protected abstract void performance();

  // MUDO docs
  protected abstract void unperformance();

  /*</section>*/



  /*<section name="validity">*/
  //------------------------------------------------------------------

  public final boolean isValid() {
    return $valid;
  }

  protected final void checkValidity() {
    boolean oldValidity = $valid;
    $valid = isAcceptable();
    if ($valid != oldValidity) {
      fireValidityChange();
    }
  }

  protected boolean isAcceptable() {
    return false;
  }

  private boolean $valid = true;

  public final boolean isValidityListener(ValidityListener listener) {
    return ($validityListeners != null) && $validityListeners.contains(listener);
  }

  public final void addListener(ValidityListener listener) {
    assert listener != null;
    if ($validityListeners != null) {
      $validityListeners.add(listener);
    }
  }

  public final void removeValidityListener(ValidityListener listener) {
    if ($validityListeners != null) {
      $validityListeners.remove(listener);
    }
  }

  /**
   * @invar foreach (ValidityListener l: isValidityListener(l)) {
   *          l.validityChanged(this, isValid())
   *        };
   *
   */
  protected final void fireValidityChange() {
    for (ValidityListener listener : $validityListeners) {
      listener.validityChanged(this, $valid);
    }
  }

  // MUDO docs
  private void removeValidityListeners() {
    for (ValidityListener listener : $validityListeners) {
      listener.listenerRemoved(this);
    }
    $validityListeners = null;
  }

  /**
   * @invar $validityListeners != null ? Collections.noNull($validityListeners);
   */
  private Set<ValidityListener> $validityListeners = new HashSet<ValidityListener>();


  /*</section>*/



  private void buildChangedObjects(AbstractEdit<_Target_, _Event_> name) {
    // NIY
  }

  /**
   * Should be implemented as
   * {@code getTarget().fireChangeEvent(<var>an event</var>);}.
   */
  protected void notifyListeners() {
    fireEvent(createEvent());
  }

  protected abstract void fireEvent(_Event_ event);

  protected abstract _Event_ createEvent();


    //  /**
//   * @H1 Absorbing other edits
//   */
//  //------------------------------------------------------------------
//  //------------------------------------------------------------------
//
//  /**
//   * Inherited from ...swing.undo.UndoableEdit
//  -**
//   * Tries to absorb the edit given as parameter. If this succeeds,
//   * anEdit is actually absorbed and should no longer be performed,
//   * undone or redone on its own. The effect of the resulting this
//   * should be the same as the effect of the old this <EM>followed
//   * by</EM> the effect of anEdit. replaceEdit() achieves the
//   * reverse order.
//    * If true is returned, from now on anEdit must return false from
//    * canUndo() and canRedo(), and must throw the appropriate exception
//    * on undo() or redo(). This can most easily be achieved by telling
//    * anEdit to die.
//   *
//   * @param	anEdit	the edit this will try to absorb
//   * @return	true if this absorbed anEdit, false it it did not; this
//   *						default implementation returns false
//   * @see	#die()
//   * @see #replaceEdit()
//   *-
//  public boolean addEdit(UndoableEdit anEdit);
//
//  -**
//   * Tries to absorb the edit given as parameter. If this succeeds,
//   * anEdit is actually absorbed and should no longer be performed,
//   * undone or redone on its own. The effect of the resulting this
//   * should be the same as the effect of the anEdit <EM>followed
//   * by</EM> the effect of the old this. addEdit() achieves the
//   * reverse order.
//    * If true is returned, from now on anEdit must return false from
//    * canUndo() and canRedo(), and must throw the appropriate exception
//    * on undo() or redo(). This can most easily be achieved by telling
//    * anEdit to die.
//   *
//   * @param	anEdit	the edit this will try to absorb
//   * @return	true if this absorbed anEdit, false it it did not; this
//   *						default implementation returns false
//   * @see	#die()
//   * @see #addEdit()
//   *-
//  public boolean replaceEdit(UndoableEdit anEdit);
//   */


  @Override
  public final String toString() {
    return getClass().getSimpleName() + //"@" + hashCode() +
           "[" + otherToStringInformation() + "]";
  }

  protected String otherToStringInformation() {
    return "target: " + getTarget() +
           ", state: " + getState() +
           ", valid: " + isValid();
  }

  public void toString(StringBuffer sb, int level) {
    assert sb != null;
    objectToString(this, sb, level);
    sb.append(indent(level + 1) + "targer:\n");
    getTarget().toString(sb, level + 2);
    sb.append(indent(level + 1) + "state: " + getState() + "\n");
    sb.append(indent(level + 1) + "valid: " + isValid() + "\n");
  }

}
