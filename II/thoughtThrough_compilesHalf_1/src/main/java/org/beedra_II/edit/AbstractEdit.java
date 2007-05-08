package org.beedra_II.edit;


import beedra.edit.*;
import beedra.validation.ValidityListener;


/**
 * AbstractEdit is the top of the edit hierarchy. Each edit should extend this
 * general edit class, unless you want to make your own implementation of the
 * framework.
 * This class offers support for performing, undoing and redoing edits, each of
 * which sends specific events and general Changed events after succesful
 * completion. A specific edit should implement a number of methods abstract in
 * this class to achieve that. The overall protocol cannot be overridden.
 * People implementing a specific edit class should implement:
 * <UL>
 * 		<LI>a constructor supplying the target (we think it best to use inner
 *							classes and use a default constructor)
 *		<LI>getPresentationName()
 *		<LI>storeInitialState()
 *		<LI>performance()
 *		<LI>unperformance()
 *		<LI>notifyPerformanceListeners()
 *		<LI>getPropagatesChangesTo()
 *		<LI>toStringInitialData()
 *		<LI>toStringGoalData()
 * </UL>
 * You will need to add data members, set-methods and get-methods for the
 * initial state and the goal state. Methods should be called
 * <CODE>(get|set)Initial[SpecificData]</CODE> and
 * <CODE>(get|set)Goal[SpecificData]</CODE>.
 * Also add edit-state-dependent get-methods called
 * <CODE>getCurrent[SpecificData]</CODE> and
 * <CODE>getPrevious[SpecificData]</CODE>, with implementation
 * <PRE>
 *	public [SpecificType] getCurrent[SpecificData]() {
 *		return (hasBeenDone() ? getGoal[SpecificData] : getInitial[SpecificData]());
 *  }
 *
 *	public [SpecificType] getPrevious[SpecificData]() {
 *		return (hasBeenDone() ? getInitial[SpecificData]() : getGoal[SpecificData]);
 *  }
 * </PRE>
 * The get methods should only be used if isPerformed(), the set methods only
 * if not isPerformed().
 * You should also provide type-safe implementation of getTarget():
 * <PRE>
 *	public [TargetType] get[SpecificTarget]() {
 *		return ([TargetType])getTarget();
 *	}
 * </PRE>
 *
 * The goal parameters set for the edit with the above set-methods can be
 * invalid for the semantics of the operation on the specific target. There
 * are 2 mechanisms for dealing with this.
 * When the goal parameters are unacceptable, calling perform() results in a
 * IllegalEditException. This is either thrown by
 * the implementation of performance(), or by the framework code in perform().
 * perform() calls isValid() before actually performing the change to the goal.
 * If isValid() is false, createIllegalEditException() generates the exception
 * that will be thrown. isValid() in this class is default true, and thus the default
 * for createIllegalEditException() is irrelevant, but both methods should be
 * overridden by subclasses to test for complex preconditions.
 * The other mechanism gives early warning of unacceptable goal parameters through
 * an event mechanism. Interested parties can register as ValidityChangedListeners
 * with the edit (while it is not isPerformed()). When setting a goal parameter
 * or when the context of the edit changes, validity should be reconsidered, and
 * if it is different from before, you should call notifyValidityListeners().
 * When perform() completes successfully, all registered ValidityListeners are
 * removed and notified of the removal.
 *
 * Consider wether the default implementation suffices or whether you need your own
 * implementation for:
 * <UL>
 *		<LI>isSignificant
 *		<LI>addEdit
 *		<LI>replaceEdit
 * </UL>
 * If you needed to add extra edit state, you should also override
 * <UL>
 *		<LI>canPerform
 *		<LI>canUndo
 *		<LI>canRedo
 * </UL>
 * We could not extend the standard swing AbstractUndoableEdit, because some things
 * we needed where declared private there.
 *
 * @invar getTarget() <> null
 * @invar for all cs in getChangedObjects: cs is ChangedSource
 *
 * @author  Jan Dockx
 */
public abstract class AbstractEdit
    implements beedra.edit.Edit, beedra.validation.Validatable,
                beedra.event.ChangePropagator {

  /**
   * Constructs an Edit with a given target.
   * Classes extending this class should call the super constructor.
   *
   * @param target	the object that is to be edited
   */
  public AbstractEdit(Object target) {
    this.target = target;
    alive = true;
    performed = false;
    hasBeenDone = false;
/* JDJDJD updateValidityWithGoal();
    This is impossible. During execution of this code, the edit does not
    yet have an outer object. The method almost always will eventually
    use the outer object to check validity.
    You must set validity the first time, the first time isValid is
    called or when the first listener registers -- as in the
    framework written for validation so far. */
    logAction(ActionData.CREATED);
  }

  /**
   * @H2 Initial State
   */
  //------------------------------------------------------------------

  /**
   * <EM>Template method</EM> Actual edits need to specify here how the
   * initial state information, needed to be able to execute undo, will
   * be saved.
   * This method is called in the transaction by perform().
   *
   * @pre	the edit is in a state that allows performing
   *			canPerform()
   * @exception IllegalEditException the target did not allow the edit
   * 								for semantical reasons
   * @see	#perform
   */
  abstract protected void storeInitialState();



  /**
   * @H2 Performance and Unperformance
   */
  //------------------------------------------------------------------

  /**
   * <EM>Template method</EM> Actual edits need to specify here what
   * the edit does in perform and redo.
   * This method is called in the transaction by perform() and redo().
   *
   * @pre	the edit is in a state that allows performing or redoing
   *			canPerform() or canRedo()
   * @exception IllegalEditException the target did not allow the edit
   * 								for semantical reasons
   * @see	#perform
   * @see #redo
   */
  abstract protected void performance()
      throws IllegalEditException;

  /**
   * <EM>Template method</EM> Actual edits need to specify here what
   * the reverse edit does in undo.
   * This method is called in the transaction by undo().
   *
   * @pre	the edit is in a state that allows undoing
   *			canUndo()
   * @exception IllegalEditException the target did not allow the edit
   * 								for semantical reasons
   * @see	#undo
   */
  abstract protected void unperformance()
      throws IllegalEditException;



  /**
   * @H2 Specific Listener Notification
   */
  //------------------------------------------------------------------
  //------------------------------------------------------------------

  /**
   * <EM>Template method</EM> Actual edits need to specify here what
   * to do to notify specific listeners interested in the actual edit
   * with specific events.
   * This method is called after the transaction by perform() and redo().
   *
   * @pre	the edit is in a state that allows performing or redoing
   *			canPerform() or canRedo()
   * @see	#perform
   * @see #redo
   */
  abstract protected void notifyPerformanceListeners();

  /**
   * <EM>Template method</EM> Actual edits need to specify here what
   * to do to notify specific listeners interested in the actual edit
   * with specific events.
   * This method is called after the transaction by undo().
   *
   * @pre	the edit is in a state that allows performing or redoing
   *			canUndo()
   * @see	#undo
   */
  abstract protected void notifyUnperformanceListeners();



  /**
   * @H2 Changed Objects Notification
   */
  //------------------------------------------------------------------
  //------------------------------------------------------------------

  /**
   * The objects that are changed by this edit, directly (the target)
   * or implicitly.
   * Mostly, these are the ancestors in an aggregate
   * structure, sometimes some peers. Changes are propageted upstream.
   * The structure is built when performing the edit. This method
   * will return null before that time.
   *
   * @return null before perform() is executed succesfully, a clone of
   *					the set
   *					of objects this edit changes directly or indirectly
   *					afterwards
   *
   * @see buildChangedObjects
   */
  final public com.objectspace.jgl.HashSet getChangedObjects() {
    return (changedObjects == null) ? null : (com.objectspace.jgl.HashSet)(changedObjects.clone());
  }

  /**
   * Returns objects that are changed implicitly when this
   * edit is performed, undone or redone. This default implementation
   * enters getTarget() of this edit in the set it returns.
   * Actual edits should call the super method, and add other
   * first level objects to start propagation. These most often are the
   * before-and-after data also kept in the edit for enabling undo
   * and redo.
   * Nulls are not allowed in the set. Remember to add also the old
   * things this edit changes.
   *
   * @return	a set containing getTarget()
   */
  public com.objectspace.jgl.HashSet getPropagatesChangesTo() {
    com.objectspace.jgl.HashSet set = new com.objectspace.jgl.HashSet();
    set.add(getTarget());
    return set;
  }

  /**
   * Builds the set of all objects interested as ChangedListeners
   * in this edit. This is done by taking the transitive closure
   * of getPropagatesChangesTo() of the edit.
   *
   * @param	propagator an object that propagates, and possibly sends,
   *				Changed events; the algorithm should start out with this,
   *				which is never added to changedObjects
   * @pre (propagator is ChangedSource) ==> (propagator in changedObjects)
   */
  private void buildChangedObjects(beedra.event.ChangePropagator propagator) {
    if (propagator != null) {
      /* get objects changes are propagated to
        that are not yet known, add them to changedObjects if they are
        changedSources, and go deep if they are ChangedPropagators */
      com.objectspace.jgl.HashSet nextSources =
          propagator.getPropagatesChangesTo();
      if (nextSources != null) {
        com.objectspace.jgl.HashSet newSources =
            nextSources.difference(changedObjects);
        /* first add newSources to changedObjects,
            if they are ChangedSources */
        com.objectspace.jgl.algorithms.Applying.forEach(
            newSources,
            new com.objectspace.jgl.UnaryFunction() {
              public Object execute(Object object) {
                try {
                  changedObjects.add((beedra.event.ChangeSource)object);
                    // throws ClassCastException
                }
                catch (ClassCastException cCExc) { /* NOP */
                }
                return null; //anything will do
              }
            }
            );
        /* go deep for each element in newSources; this element is already in
            changedSources if it is a ChangedSource */
        com.objectspace.jgl.algorithms.Applying.forEach(
            newSources,
            new com.objectspace.jgl.UnaryFunction() {
              public Object execute(Object object) {
                try {
                  buildChangedObjects((beedra.event.ChangePropagator)object);
                    // throws ClassCastException
                }
                catch (ClassCastException cCExc) { /* NOP */
                }
                return null; //anything will do
              }
            }
            );
      } // nextSources != null
    } // propagator != null
  }

  /**
   * Tells all collected ChangedSources to notify their ChangedListeners
   *
   * @pre performed (buildChangedObjects has been executed)
   */
  private void notifyChangedListeners() {
    com.objectspace.jgl.algorithms.Applying.forEach(
        changedObjects,
        new com.objectspace.jgl.UnaryFunction() {
          public Object execute(Object object) {
            ((beedra.event.ChangeSource)object).notifyChanged(AbstractEdit.this);
              /* throws ClassCastException, but should not happen if class
                   invariant is respected */
            return object; //anything will do
          }
        }
        );
  }

  /**
   * The objects changed by this edits. This is the target and all
   * upstream objects, and maybe some peer objects.
   * The set is built by taking the transitive closure of
   * getChangePropagetedTo of the target.
   *
   * @see ChangePropagator
   */
  private com.objectspace.jgl.HashSet changedObjects = new com.objectspace.jgl.HashSet();





  /**
   * @H1 Validation
   */
  //------------------------------------------------------------------
  //------------------------------------------------------------------

  /**
   * At this time, this is valid.
   * Overwrite this method to return current validity of the goal
   * parameters of this edit. When this isPerformed(), it should
   * always return true.
   *
   * @return	isPerformed() ==> true; not isPerformed() ==>
   */
  final public boolean isValid() {
    if (isPerformed()) return true;
    return $validity;
  }

  /**
   * The ValidityListener parameter is added to the list of objects to be
   * notified of validity changes in this Validatable. During registration,
   * the listener is notified the first time with the current validity.
   * Because goal parameters can only change if this
   * not isPerformed(), this method does nothing if this isPerformed().
   * When perform() is called succesfully, all registred listeners are removed.
   *
   * @param listener The ValidityListener that has to be added
   * @param  requestInitialNotification
   *       | If this is true, and an initial notification was setup during construction
   *         of this instance, an initial notification will be send to the listener.
   */
  public void addValidityListener(ValidityListener listener, boolean requestInitialNotification) {
    /* JDJDJD BIG PROBLEM: validityListeners can be null (see setPerformDoneState)
        Yet a simple if-no-null around the code below, like works with the remove,
        doesn't work here. This method can still be called, and then notifications
        are in order! */
    if (validityListeners != null) {
      // if validityListeners == null, this means no validity events will happen anymore anyway
      validityListeners.add(listener, requestInitialNotification);
    }
  }

  /**
   * The ValidityListener parameter is removed from the list of objects to be
   * notified of validity changes in this Validatable.
   * Because goal parameters can only change if this
   * not isPerformed(), this method does nothing if this isPerformed().
   * When perform() succeeds, all registered ValidityListeners are deregistered
   * anyway.
   *
   * @param listener The ValidityListener that has to be removed
   */
  final public void removeValidityListener(beedra.validation.ValidityListener listener) {
    if (validityListeners != null) {
      validityListeners.remove(listener);
    }
  }

  /**
   * This method needs to be called by subclasses when the goal values of the
   * edit have changed. It check validity of the goal parameters, caches
   * validity and notifies ValidityListeners if validity has changed.
   *
   * Subclasses need to implement the method isAcceptableGoal.
   */
  final protected void updateValidityWithGoal() {
    if (isValid() != isAcceptableGoal()) {
              /* JDJDJD needs to be replaced with a more component-wise
                  validation architecture */
      toggleValidity();
    }
  }

  /**
   * Returns an exception to be thrown when perform() is tried with unacceptable
   * goal parameters (not isValid()).
   *
   * @return	generic IllegalEditException; should be overridden to return a
   *						more informative exception in specific subclasses.
   */
  protected IllegalEditException createIllegalEditException() {
    return new
        IllegalEditException("Goal parameters are not valid in the current state.", this);
  }

  /**
   * The goal currently recorded is acceptable for this edit to be performed.
   */
  abstract protected boolean isAcceptableGoal();

  /**
   * Toggles the cached validity and notifies registred ValidityListeners of the
   * change.
   *
   * @effect	isPerformed() ==> NOP
   * @effect not isPerformed() ==> isValid() = not old isValid() and notify listeners
   */
  private void toggleValidity() {
    $validity = !$validity;
    notifyValidityListeners();
  }

  /**
   * Notifies registered ValidatyListeners of a change in validity in the goal
   * parameters of this edit. Because goal parameters can only change if this
   * not isPerformed(), this method does nothing if this isPerformed().
   */
  final private void notifyValidityListeners() {
    validityListeners.notifyListeners(new beedra.validation.ValidityChanged(this, isValid()));
  }

  /**
   * Cache for validity of goal string. Only applicable when
   * not isPerformed().
   */
  // JDJDJD default true is needed for compound edits with this quickfix validation mechanism
  private boolean $validity = true;

  /**
   * The source support for validity listeners.
   *
   * @invar	never null
   */
  private org.jutil.event.SourceSupport validityListeners =
      new org.jutil.event.SourceSupport(/*@ beedra.validation.ValidityListener.class @*/) {
            public void notifyListener(java.util.EventListener listener, java.util.EventObject event) {
              if (event instanceof beedra.validation.ValidityChanged) {
                ((beedra.validation.ValidityListener)listener).validityChanged((beedra.validation.ValidityChanged)event);
              }
              else { // instanceof ValidityListenerRemoved
                ((beedra.validation.ValidityListener)listener).
                    validityListenerRemoved((beedra.validation.ValidityListenerRemoved)event);
              }
            }
            public void initialNotifyListeners(java.util.EventListener listener) {
              ((beedra.validation.ValidityListener)listener).validityChanged(
                new beedra.validation.ValidityChanged(AbstractEdit.this, isValid()));
            }
          };





  /**
   * @H1 Absorbing other edits
   */
  //------------------------------------------------------------------
  //------------------------------------------------------------------

  /**
   * Tries to absorb the edit given as parameter. If this succeeds,
   * anEdit is actually absorbed and should no longer be performed,
   * undone or redone on its own.
   * This default implementation does nothing.
   *
   * @param	anEdit	the edit this will try to absorb
   * @return	true if this absorbed anEdit, false it it did not; this
   *						default implementation returns false
   * @see UndoableEdit#addEdit
   */
  public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
    // JDJDJD shouldn't parameter be an Edit?
    return false;
  }

  /**
   * Tries to absorb the edit given as parameter. If this succeeds,
   * anEdit is actually absorbed and should no longer be performed,
   * undone or redone on its own. The effect of the resulting this
   * should be the same as the effect of the old this <EM>followed
   * by</EM> the effect of anEdit. replaceEdit() achieves the
   * reverse order.
    * If true is returned, from now on anEdit must return false from
    * canUndo() and canRedo(), and must throw the appropriate exception
    * on undo() or redo(). This can most easily be achieved by telling
    * anEdit to die.
   * This default implementation does nothing.
   *
   * @param	anEdit	the edit this will try to absorb
   * @return	true if this absorbed anEdit, false it it did not; this
   *						default implementation returns false
   * @see	#die()
   * @see #replaceEdit()
   */
    // JDJDJD shouldn't parameter be an Edit?
  public boolean replaceEdit(javax.swing.undo.UndoableEdit anEdit) {
    return false;
  }





  /**
   * @H1 History Data
   */
  //------------------------------------------------------------------
  //------------------------------------------------------------------

  /**
   * Everything you ever wanted to know about the history of this edit.
   */

  /**
   * A history of what happened with the edit.
   *
   * @return	an array of ActionData, with all entries not null,
   *					in chronological order
   */
  final public ActionData[] getHistory() {
    ActionData[] copy = new ActionData[history.size()];
    history.copyInto(copy);
    return copy;
  }

  /**
   * A String representation of the history of this with line breaks and
   * an indent.
   *
   * @param	spaces	the number of space to indent the listing;
   *								values smaller then 0 are considered 0
   * @return	a String representation of the history of this
   */
  public String getHistoryStringLines(int spaces) {
    ActionData[] h = getHistory();
    String sr = "";
    for (int i = 0; i < h.length; i++) {
      sr = sr + h[i].toStringLines(spaces) +
            ((i == h.length) ? "" : "\n");
    }
    return sr;
  }

  /**
   * Log the action in the history.
   *
   * @param actionKind	an element of ActionData.ACTION_KIND
   * @pre actionKind is ACTION_KIND
   * @see #ActionData.getAction()
   * @see ActionData.ACTION_KIND
   */
  // JDJDJD should be final, but overridden in CompoundEdit
  protected void logAction(int actionKind) {
    history.addElement(new ActionData(actionKind));
  }

  /**
   * Contains ActionData about everything that happened with this edit.
   * The order is chronological.
   */
  private java.util.Vector history = new java.util.Vector(4);





  /**
   * @H1 Miscellaneous
   */
  //------------------------------------------------------------------
  //------------------------------------------------------------------

  /**
   * A String representation of this, listing the target and state of this.
   *
   * @return	the presentation name, the state of the edit, the goal data
   *					and a string representation of the target of the edit; the initial
   *					data is listed if this.isPerformed().
   *
   * @protected
   * <EM>Hook method</EM> This method calls toStringInitialData and
   *	toStringGoalData to get a representation of initial and goal data.
   */
  public String toString() {
    return "Edit \"" + getPresentationName() +
             "\"(target: " + getTarget().getClass().getName() + " [" + getTarget() + "]" +
             ", " + (isAlive() ? "alive" : "not alive") +
             ", " + (isPerformed() ? "performed" : "not performed") +
             ", " + (hasBeenDone() ? "done" : "undone") +
             ", initial: " + (isPerformed() ? toStringInitialData() : "[not yet performed]") +
             ", goal: " + toStringGoalData() + ")";
  }

  /**
   * A String representation of this, listing the target, the state of this
   * and the data on separate lines, followed by the history of the
   * edit.
   *
   * @return	the presentation name, the state of the edit, the goal data
   *					and a string representation of the target of the edit; the initial
   *					data is listed if this.isPerformed().
   */
  public String toStringLines() {
    return toStringLines(0);
  }

  /**
   * A String representation of this, listing the target, the state of this
   * and the data on separate lines, followed by the history of the
   * edit. This method lists the data with an indent.
   *
   * @param		indent	the number of spaces the data is indent from the left margin;
   *								values smaller then 0 are considered 0
   * @return	the presentation name, the state of the edit, the goal data
   *					and a string representation of the target of the edit; the initial
   *					data is listed if this.isPerformed().
   *
   * @protected
   * The indent can be realised by using spaces(int howMany) and
   * indentLine(int howMany, String line)
   */
  public String toStringLines(int indent) {
    int i2 = indent + 2;
    return indentLine(indent, "Edit \"" + getPresentationName() + "\"") +
           indentLine(i2, "target  : " + getTarget().getClass().getName() + " [" + getTarget() + "]") +
           indentLine(i2, "state   : " + (isAlive() ? "alive" : "not alive") +
                  ", " + (isPerformed() ? "performed" : "not performed") +
                   ", " + (hasBeenDone() ? "done" : "undone")) +
            indentLine(i2, "initial :" + (isPerformed() ?
                                " " + toStringInitialData() : "[not yet performed]")) +
            indentLine(i2, "goal    :" + " " + toStringGoalData()) +
            indentLine(i2, "history :") +
                            getHistoryStringLines(i2 + 2);
  }

  /**
   * <EM>Template method</EM> Actual edits need to return a string
   * representation of the goal data.
   * This method is called by toString and toStringLines. The returned
   * string is listed in between commas in toString, and on 1 line in
   * toStringLines.
   *
   * @pre	isPerformed()
   * @return	a string representation of the goal data of the edit;
   *						not null
   * @see	#toString
   * @see	#toStringLines
   */
  abstract protected String toStringInitialData();

  /**
   * <EM>Template method</EM> Actual edits need to return a string
   * representation of the goal data.
   * This method is called by toString and toStringLines. The returned
   * string is listed in between commas in toString, and on 1 line in
   * toStringLines.
   *
   * @return	a string representation of the goal data of the edit;
   *						not null
   * @see	#toString
   * @see	#toStringLines
   */
  abstract protected String toStringGoalData();

  /**
   * Utility method to build a String of spaces.
   *
   * @param		howMany		how many spaces to return;
   *								values smaller then 0 are considered 0
   * @return	String with howMany spaces
   */
  final static protected String spaces(int howMany) {
    String result = "";
    char space = ' ';
    for (int i = 0; i < howMany; i++) {
      result = result + space;
    }
    return result;
  }

  /**
   * Utility method to build an indent line
   *
   * @param		howMany		how many spaces to indent line;
   *								values smaller then 0 are considered 0
   * @param		line			the text to indent
   * @return	String with howMany spaces, the line, and an end-of-line
   */
  final static protected String indentLine(int howMany, String line) {
    return spaces(howMany) + line + "\n";
  }

}

