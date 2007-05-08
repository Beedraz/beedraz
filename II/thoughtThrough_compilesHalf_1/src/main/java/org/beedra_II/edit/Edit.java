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
public abstract class Edit<_Source_ extends EditableBeed<_Source_, _Edit_>,
                           _Edit_ extends Edit<_Source_, _Edit_>>
    extends Event<_Source_, _Edit_> {

  protected Edit(_Source_ source) {
    super(source);
  }

  public abstract void perform();

//  /*<property name="alive">*/
//  //------------------------------------------------------------------
//
//  /**
//   * The edit is alive, i.e., {@link #kill()} has not yet been called on it.
//   * An edit is only operational when it is alive. Once an edit is killed,
//   * it cannot become alive again.
//   *
//   * @basic
//   * @init true;
//   */
//  final public boolean isAlive() {
//      return $alive;
//  }
//
//  /**
//   * <p>Sets alive to false. Note that this is a one way operation:
//   *   dead edits cannot be resurrected. Sending undo() or redo() to
//   *   a dead edit results in an exception being thrown.</p>
//   * <p>Typically an edit is killed when it is consolidated by another
//   *   edit's addEdit() or replaceEdit() method.</p>
//   *
//   * @post ! isAlive();
//   */
//  public final void kill() {
//    // JDJDJD should be final, but overridden in CompoundEdit
//      $alive = false;
////      /* allowed to change and registered ValidityChangedListeners
////              are all deregistred */
////      validityListeners.notifyListeners(
////          new beedra.validation.ValidityListenerRemoved(AbstractEdit.this));
////      validityListeners = null;
//  }
//
//  /**
//   * True as long as {@link #kill()} has not been called.
//   */
//  private boolean $alive;
//
//  /*</property>*/
//
//
//
//  /*<section name="perform">*/
//  //------------------------------------------------------------------
//
//  /**
//     * The edit has been "performed" succesfully. Is false after
//     * construction and becomes true by calling perform. Edits
//     * cannot get "un"-performed once they are performed.
//     *
//     * @see #perform
//     *
//     * @protected
//     * actually the data member is set by setPerformDoneState,
//     * a template method called in perform.
//     *
//     * @see #setPerformDoneState
//     */
//    /* Added to AbstractUndoableEdit, where the state was completely
//       hidden. */
//    final public boolean isPerformed() {
//        return performed;
//    }
//
//    /**
//     * Returns {@code false} if this edit is already {@link #perform() performed}
//     * or no longer {@link #isAlive()}, {@code true} if it is {@link #isAlive()}
//     * and not yet {@link #perform() performed}. Once an action is
//     * {@link #perform() performed}, it can not get un-performed.
//     *
//     * @return  ! isPerformed() && isAlive();
//     * @init true;
//     */
//    public boolean canPerform() {
//      return ! $performed && $alive;
//    }
//
//    /**
//     * Performs this edit. If the performance succeeds, this notifies listeners
//     * specifically interested in edits of a specific kind with a specific event,
//     * and all objects that got dirty (i.e., the transitive closure of
//     * getPropagatesChangesTo()) with a Changed event. The set of dirty objects
//     * is calculated within the transaction. Notification happens outside
//     * the transaction.
//     * An Edit can only be performed once. Sets hasBeenDone and
//     * isPerformed to true.
//     *
//     * @effect  hasBeenDone() & isPerformed()
//     * @exception   CannotPerformException the edit was not performed
//     *                              because the edit object was not in an state that
//     *                              allowed performing
//     *                          not canPerform()
//     * @exception IllegalEditException the target did not allow the edit
//     *                              for semantical reasons
//     * @see         #canPerform
//     *
//     * @protected
//     * <EM>Hook method</EM> This method calls performance to do the actual
//     * work. Here we take care of checking the state of the edit, logging the
//     * call to perform, setting the state of the edit object, gathering the
//     * objects that are dirty and sending events.
//     * Override storeInitialState(), performance(), notifyPerformanceListeners()
//     * and propageChangesTo() to get the desired effect.
//     * <EM>This method should not be overridden outside the framework.</EM>
//     * @see         #performance
//     * @see         #getPropagatesChangesTo()
//     */
//    /* Added to AbstractUndoableEdit. */
//    final public void perform()
//            throws CannotPerformException, IllegalEditException {
//      // start of transaction
//        if (!canPerform()) {
//            logAction(ActionData.PERFORMED_CANNOT);
//            // end of transaction if not canPerform
//            throw new CannotPerformException();
//        }
//        try {
//            storeInitialState();
//            updateValidityWithGoal(); // this is not exactly what we want; it doesn't recalulate enough, but potentially sends validity changed events JDJDJD
//            if (! isValid()) throw createIllegalEditException();
//            performance(); // throws IllegalEditException
//            buildChangedObjects(this);
//                // this could be done in a new thread
//                /* Building the list of changed objects is done here and not when
//                   the edit object is constructed, because the object structure the
//                   edit has to work on could have changed in the mean time. The
//                   structure will have changed by the time undo (or redo) would be
//                   called too, of course, but that is no problem since being changed
//                   is symmetric for all edits, and the state of the entire object
//                   structure will be the same for redo, and for undo, apart from
//                   the edit itself. */
//            setPerformDoneState();
//            logAction(ActionData.PERFORMED);
//            // end of transaction if performance succeeded
//            notifyPerformanceListeners();
//            notifyChangedListeners();
//        }
//        catch (IllegalEditException illEexc) {
//            logAction(ActionData.PERFORMED_ILLEGAL);
//            // end of transaction if performance did not succeed
//            throw illEexc; // propagate exception
//        }
//    }
//
//    /**
//     * <EM>Template method</EM>
//     * Sets boolean data members to reflect the fact that
//     * the edit was performed. The method is called in
//     * hook method perform.
//     * Implementation is default for a simple edit. It is
//     * overridden e.g. in CompoundEdit.
//     *
//     * @see #perform
//     */
//    /* Added to AbstractUndoableEdit, where the state was
//         completely hidden. Needed to give subclasses power over the
//         state of the edit. */
//    // JDJDJD should be final, but overridden in CompoundEdit
//    protected void setPerformDoneState() {
//      hasBeenDone = true;
//      performed = true;
//      /* when this isPerformed(), goal parameters are no longer
//                allowed to change and registered ValidityChangedListeners
//                are all deregistred */
//      validityListeners.notifyListeners(
//                                        new beedra.validation.ValidityListenerRemoved(AbstractEdit.this));
//      validityListeners = null;
//    }
//
//    /**
//     * The edit has been performed.
//     * Defaults to {@code false}. Becomes true if this edit is performed,
//     * and cannot change back.
//     */
//    private boolean $performed;
//
//    /*</section>*/
//
//    /**
//     * The edit has been "done".
//     * Defaults to false. Becomes true if this edit is performed or
//     * redone, false again if it is undone.
//     */
//    /* Added to AbstractUndoableEdit, where the state was completely
//       hidden. */
//    final public boolean hasBeenDone() {
//      return hasBeenDone;
//    }
//
//    /**
//     * The edit has been "done".
//     * Defaults to {@code false}. Becomes true if this edit is performed or
//     * redone, false again if it is undone.
//     */
//    private boolean $hasBeenDone;
//
//
//    /*<section name="undo">*/
//    //------------------------------------------------------------------
//
//    /**
//     * <EM>Template method</EM>
//     * Sets boolean data members to reflect the fact that
//     * the edit was undone. The method is called in hook
//     * method undo.
//     * Implementation is default for a simple edit. It is
//     * overridden e.g. in CompoundEdit.
//     *
//     * @see #undo
//     */
//    /* Added to AbstractUndoableEdit, where the state was
//         completely hidden. Needed to give subclasses power over the
//         state of the edit. */
//    // JDJDJD should be final, but overridden in CompoundEdit
//    protected void setUndoDoneState() {
//      hasBeenDone = false;
//    }
//
//    /**
//     * The edit can be undone, as far as the edit object is concerned,
//     * i.e., it is in a state that would allow undoing.
//     *
//     * @return  the edit is alive and, performed and hasBeenDone (i.e.,
//     *                  the last action was perform or redo, not undo)
//     * @see         #perform
//     * @see         #undo
//     * @see         #redo
//     * @see         #die
//     * @see     #hasBeenDone
//     * @see     #isAlive
//     */
//    /* Changed from AbstractUndoableEdit. */
//    // JDJDJD should be final, but overridden in CompoundEdit
//    public boolean canUndo() {
//        return alive && hasBeenDone && performed;
//    }
//
//    /**
//     * Actually undoing the edit on the target.
//     * If the undoing succeeds, notifies listeners specifically interested
//     * in edits of a specific kind with a specific event, and all objects
//     * that got dirty (i.e., the transitive closure of getPropagatesChangesTo())
//     * with a Changed event. The set of dirty object is calculated
//     * in perform(). Notification happens outside the transaction.
//     * Sets hasBeenDone to false.
//     *
//     * @effect  not hasBeenDone()
//     * @excep IllegalUndoException  the target did not allow the undo due to
//     *                              semantical reasons; this should never occur, because it
//     *                              should return you to a state that was previously valid
//     * @excep CannotUndoException       the undo is not possible due to the state of
//     *                              the edit
//     *                          not canUndo()
//     * @see         #perform
//     * @see         #canUndo
//     *
//     *
//     * @protected
//     * <EM>Hook method</EM> This method calls unperformance to do the actual
//     * work. Here we take care of checking the state of the edit, logging the
//     * call to undo, setting the state of the edit object and sending events.
//     * Override unperformance(), notifyUnperformanceListeners() and
//     * propageChangesTo() to get the desired effect.
//     * <EM>This method should not be overridden outside the framework.</EM>
//     * @see         #unperformance()
//     * @see         #propageChangesTo()
//     */
//     /* A possible IllegalEditException from unperformance() is translated into
//            a IllegalUndoException (subclass of CannotUndoException, because we
//            cannot throw an IllegalEditException directly. But undo giving an
//            IllegalEditException is highly suspicious anyway.
//            Changed from AbstractUndoableEdit because unperformance can throw
//            IllegalEditException; if this occurs, state should not be changed. */
//    final public void undo()
//            throws javax.swing.undo.CannotUndoException {
//      // start of transaction
//        if (!canUndo()) {
//            logAction(ActionData.UNDONE_CANNOT);
//            // end of transaction if not canUndo
//            throw new javax.swing.undo.CannotUndoException();
//        }
//        try {
//            unperformance(); // throws IllegalEditException
//            setUndoDoneState();
//            logAction(ActionData.UNDONE);
//            // end of transaction if unperformance succeeded
//            notifyUnperformanceListeners();
//            notifyChangedListeners();
//        }
//        catch (IllegalEditException illEditExc) {
//            logAction(ActionData.UNDONE_ILLEGAL);
//            // end of transaction if unperformance did not succeed
//            throw new IllegalUndoException(illEditExc);
//        }
//    }
//
//    /*</section>*/
//
//    /*<section name="redo">*/
//    //------------------------------------------------------------------
//
//    /**
//     * The edit can be redone, as far as the edit object is concerned,
//     * i.e., it is in a state that would allow redoing.
//     *
//     * @return  the edit is alive and, performed and not hasBeenDone (i.e.,
//     *                  the last action was undo, not perform or redo)
//     * @see         #perform
//     * @see         #undo
//     * @see         #redo
//     * @see         #die
//     * @see     #hasBeenDone
//     * @see     #isAlive
//     */
//    /* Changed from AbstractUndoableEdit. */
//    // JDJDJD should be final, but overridden in CompoundEdit
//    public boolean canRedo() {
//        return alive && !hasBeenDone && performed;
//    }
//
//    /**
//     * Actually redoing the edit on the target.
//     * If the redoing succeeds, notifies listeners specifically interested in
//     * edits of a specific kind with a specific event, and all objects
//     * that got dirty (i.e., the transitive closure of getPropagatesChangesTo())
//     * with a Changed event. The set of dirty object is calculated
//     * in perform(). Notification happens outside the transaction.
//     * Sets hasBeenDone to true.
//     *
//     * @effect  hasBeenDone()
//     * @excep IllegalRedoException  the target did not allow the redo due to
//     *                              semantical reasons; this should never occur, because it
//     *                              should return you to a state that was previously valid
//     * @excep CannotRedoException       the redo is not possible due to the state of
//     *                              the edit
//     *                          not canRedo()
//     * @see         #perform
//     * @see         #canRedo
//     *
//     *
//     * @protected
//     * <EM>Hook method</EM> This method calls performance to do the actual
//     * work. Here we take care of checking the state of the edit, logging the
//     * call to redo, setting the state of the edit object and sending events.
//     * Override performance(), notifyPerformanceListeners and propageChangesTo()
//     * to get the desired effect.
//     * <EM>This method should not be overridden outside the framework.</EM>
//     * @see         #performance()
//     * @see         #propageChangesTo()
//     */
//     /* A possible IllegalEditException from performance() is translated into
//            a IllegalRedoException (subclass of CannotRedoException, because we
//            cannot throw an IllegalEditException directly. But redo giving an
//            IllegalEditException is highly suspicious anyway.
//            Changed from AbstractUndoableEdit because performance can throw
//            IllegalEditException; if this occurs, state should not be changed. */
//    final public void redo()
//            throws javax.swing.undo.CannotRedoException {
//      // start of transaction
//        if (!canRedo()) {
//            logAction(ActionData.REDONE_CANNOT);
//            // end of transaction if not canRedo
//            throw new javax.swing.undo.CannotRedoException();
//        }
//        try {
//            performance(); // throws IllegalEditException
//            setRedoDoneState();
//            logAction(ActionData.REDONE);
//            // end of transaction if performance succeeded
//            notifyPerformanceListeners();
//            notifyChangedListeners();
//        }
//        catch (IllegalEditException illEditExc) {
//            logAction(ActionData.REDONE_ILLEGAL);
//            // end of transaction if performance did not succeed
//            throw new IllegalRedoException(illEditExc);
//        }
//    }
//
//    /**
//     * <EM>Template method</EM>
//     * Sets boolean data members to reflect the fact that
//     * the edit was redone. The method is called in hook
//     * method redo.
//     * Implementation is default for a simple edit. It is
//     * overridden e.g. in CompoundEdit.
//     *
//     * @see #redo
//     */
//    /* Added to AbstractUndoableEdit, where the state was
//         completely hidden. Needed to give subclasses power over the
//         state of the edit. */
//    // JDJDJD should be final, but overridden in CompoundEdit
//    protected void setRedoDoneState() {
//      hasBeenDone = true;
//    }
//
//    /*</section>*/
//
//
//
//
//
//  /*</section>*/
//
//
//
//
//
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

}
