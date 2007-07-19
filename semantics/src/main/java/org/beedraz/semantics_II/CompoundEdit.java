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


import static org.beedraz.semantics_II.Edit.State.DONE;
import static org.beedraz.semantics_II.Edit.State.NOT_YET_PERFORMED;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Assembles little edits into great big ones.</p>
 * <p>Normally, our state and the state for all our component edits is the same.
 *   This is only untrue if a component edit goes solo. In that case, we are dead.
 *   If we are dead, the state of the component edits can be different.</p>
 *
 * @author  Jan Dockx
 *
 * @invar isValid() == for (Edit e : getEdits()) {e.isValid};
 * @invar exists (Edit e : getEdits()) {e.getState() == DEAD} ? getState() == DEAD;
 *        if there is any dead component, we are dead
 * @invar getState() == NOT_YET_PERFORMED ? for (Edit e : getEdits()) {e.getState() == NOT_YET_PERFORMED};
 *        if we are not yet performed, all components are not yet performed
 * @invar getState() == DONE ? for (Edit e : getEdits()) {e.getState() == DONE};
 *        if we are done, all components are done
 * @invar getState() == UNDONE ? for (Edit e : getEdits()) {e.getState() == UNDONE};
 *        if we are undone, all components are undone
 * @invar intersection (Beed<?> componentTarget : getComponentEditTargets()) {
 *          componentTarget.getUpdateSourcesTransitiveClosure()
 *        }.contains(getTarget());
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class CompoundEdit<_Target_ extends AbstractBeed<_Event_>,
                                _Event_ extends Event>
    extends AbstractEdit<_Target_, _Event_> {


  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @param target
   *        If the targets of the element edits differ, this target should be
   *        an object that is upstream of all child targets.
   *
   * @pre target != null;
   * @post getTarget() == target;
   */
  public CompoundEdit(_Target_ target) {
    super(target);
  }

  /*</construction>*/



  /*<section name="component edits">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final List<Edit<?>> getComponentEdits() {
    return Collections.unmodifiableList($componentEdits);
  }

  /**
   * @return map (Edit<?> componentEdit : getComponentEdits()) {
   *           componentEdit.getTarget();
   *         }
   */
  public final Set<Beed<?>> getComponentEditTargets() {
    Set<Beed<?>> result = new HashSet<Beed<?>>();
    for (Edit<?> componentEdit : $componentEdits) {
      result.add(componentEdit.getTarget());
    }
    return result;
  }

  public final boolean deepContains(Edit<?> edit) {
    for (Edit<?> ce : $componentEdits) {
      if (ce == edit) {
        return true;
      }
      else if (ce instanceof CompoundEdit) {
        CompoundEdit<?, ?> compoundComponentEdit = (CompoundEdit<?, ?>)ce;
        if (compoundComponentEdit.deepContains(edit)) {
          return true;
        }
      }
      // else, no match, try next
    }
    return false;
  }

  /**
   * Component edits might even be invalid when added. When a component edit is added,
   * validity of the compound edit is recalculated.
   *
   * @pre componentEdit != null;
   * @pre componentEdit.getState() == NOT_YET_PERFORMED;
   * @pre componentEdit.getTarget().getUpdateSourcesTransitiveClosure().contains(getTarget());
   * @throws EditStateException
   *         getState() != NOT_YET_PERFORMED;
   */
  public final void addComponentEdit(Edit<?> componentEdit) throws EditStateException {
    assert componentEdit != null;
    assert componentEdit.getState() == NOT_YET_PERFORMED;
    assert componentEdit.getTarget().getUpdateSourcesTransitiveClosure().contains(getTarget());
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    componentEdit.addValidityListener($componentEditListener); // for dead
    $componentEdits.add(componentEdit);
    recalculateValidity();
  }

  // no remove method for now

  private final List<Edit<?>> $componentEdits = new ArrayList<Edit<?>>();

  /*</section>*/



  /*<section name="perform">*/
  //------------------------------------------------------------------

  /**
   * Ask component edits to stare initial state;
   *
   * @pre  getState() == NOT_YET_PERFORMED;
   * @pre  for (Edit<?> edit : getComponentEdits()) {
   *         edit.getState() == NOT_YET_PERFORMED;
   *       }
   */
  @Override
  protected final void storeInitialState() {
    for (Edit<?> e : $componentEdits) {
      e.storeInitialState();
    }
    // there is no local initial state to store, is there?
  }

  /**
   * There is a change if there is at least one component that is a change.
   * When performing the edit, only component edits that represent a change
   * are actually performed. Component edits that do not represent a change
   * are skipped.
   */
  @Override
  public final boolean isChange() {
    for (Edit<?> e : $componentEdits) {
      if (e.isChange()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Asks all component edits to execute performance, in the order they were added.
   * If one of the performance executions fails, what is performed until
   * now is undone again (rollback). If something fails during this undo, something
   * is seriously wrong, and the application is in an inconsistent state.
   * After rollback, the offending exception is propagated.
   * If, immediately before performance, a component edit is
   * {@link Edit#isChange() not a change}, it is removed from the compound edit
   * and killed.
   */
  @Override
  final protected void performance() throws IllegalEditException {
    boolean[] toBeKilled = new boolean[$componentEdits.size()];
    ListIterator<Edit<?>> iter = $componentEdits.listIterator();
    try {
      while (iter.hasNext()) {
        Edit<?> e = iter.next();
        e.recalculateValidity();
        e.checkValidity(); // throws IllegalEditException
        if (e.isChange()) {
          e.performance(); // throws IllegalEditException; this doesn't change the state yet, and doesn't send events
        }
        else {
          /* we are going to remove and kill this edit; but we can't do that yet,
           * because, to be atomic, we need to be able to return to the previous
           * state, and kill is irreversible; so we store the edit for later
           */
          toBeKilled[iter.previousIndex()] = true;
        }
      }
      // if we get here, all edits are performed, and we can kill
      for (int i = toBeKilled.length - 1; i >= 0; i--) {
        if (toBeKilled[i]) {
          Edit<?> editToKill = $componentEdits.remove(i);
          editToKill.kill();
        }
      }
    }
    catch (IllegalEditException eExc) {
      // rollback
      iter.previous(); // last edit didn't succeed, and doesn't need to be rolled-back
      try {
        while (iter.hasPrevious()) {
          Edit<?> e = iter.previous();
          if (! toBeKilled[iter.nextIndex()]) {
            assert e.getState() == DONE;
            e.unperformance();  // no exceptions
          }
          // else, just skip
        }
      }
      catch (IllegalEditException ieExc) {
        throw new Error("could not roll-back", ieExc);
        // MUDO serious; special throwable; use ppw-exception
      }
      throw eExc;
    }
  }

  @Override
  protected final void markPerformed() {
    for (Edit<?> e : $componentEdits) {
      e.markPerformed();
    }
    localMarkPerformed();
  }


  /*</section>*/



  /*<section name="validity">*/
  //------------------------------------------------------------------

  /*
   * MUDO Validity of component edits must be evaluated in context of
   *      earlier edits: a component edit might be invalid (beforehand)
   *      in isolation, but it might be valid by the time we reach it.
   *      So the compound might be valid even if some components are not
   *      (before performance) -- and vice versa.
   *      This requires a validation expression that allows for subsitution.
   *
   *      This is not implemented yet. We only look at the first component edit:
   *      if that is not acceptable, the compound edit can surely not be
   *      performed. After that, even if the component edit says it is acceptable,
   *      perform might still fail (as currently with any edit).
   *
   *      Validation expressions with substitution is something for a
   *      later version
   */

  /**
   * @return getComponentEdits().isEmpty() || getComponentEdits().get(0).isAcceptable();
   */
  @Override
  protected final boolean isAcceptable() {
    return $componentEdits.isEmpty() || $componentEdits.get(0).isAcceptable();
  }

  private ValidityListener $componentEditListener = new ValidityListener() {

    public void validityChanged(Edit<?> target, boolean newValidity) {
      /* Since currently our acceptibility only depends on the first component edit,
       * we only react to acceptibility changes of the first component edit.
       */
      assert $componentEdits.size() > 0;
      if (target == $componentEdits.get(0)) {
        recalculateValidity();
      }
    }

    public void listenerRemoved(Edit<?> target) {
      // the sneaking component edit performed without us! we are dead!
      for (Edit<?> e : $componentEdits) {
        e.removeValidityListener($componentEditListener);
      }
      kill();
    }

  };

  /*</section>*/




  /**
   * <p>Gather source events from all atomic component edits.</p>
   * <p>These are our component edits, flattened. But, it is very well
   *   possible that amongst our flattened component edits there are
   *   several edits that work on a common target. They represent
   *   sequential changes of the same semantic state. (e.g.,
   *   0 to 1, 1 to 5, 5 to 3, and then even possibly 3 to 0).</p>
   * <p>For dependent beeds, this is no problem: they will be visited once
   *   during the topological update, and generate one event that describes
   *   the overal change.</p>
   * <p>For editable beeds, the target of the edits, this is a problem.
   *   We need to devise a way to generate one overal event for
   *   every editable beed, but the question is what to use as edit there:
   *   in this case we have several edits for the a given target, and each
   *   of these edits can be asked to generate an event (or more).</p>
   * <p>For edits that follow each other directly in the compound edit,
   *   we can have the second one be <dfn>absorded</dfn> by the first one
   *   (see {@link javax.swing.undo.UndoableEdit#addEdit(javax.swing.undo.UndoableEdit)}
   *   for information and a rationale about absorbing edits),
   *   and ask the combined edit for the event. If edits are separated by
   *   other edits, this is not trivial: it is possible that a later edit
   *   for a given target can only be executed after other edits have
   *   prepared the way, so we cannot just combine separated edits.</p>
   * <p>In general, this means that we cannot ask the edits for a combined
   *   event. We can however ask each of the separated edit (groups)
   *   for an event, and then ask the events to
   *   {@link Event#combineWith(Event, Edit) combine}: the new state
   *   of the first event will be the old state of the second event.
   *   Then we have to make sure however that the edit generates events
   *   based on the edit goal state, and not on the target state, because
   *   that might have changed already. That is the rationale for that requirement
   *   on {@link Edit#createEvents()}.</p>
   * <p>In this overal event, we also need an edit as change reason.
   *   When all edits for a given target can be combined into one edit,
   *   or if there is only one edit for the given target, the reason of
   *   change can be the combined or one edit. When edits cannot be combined,
   *   the change reason in the combined event must be this compound edit.
   *   This then signals to other mechanisms that the change can also only
   *   be undone by undoing the compound edit as a whole.</p>
   * <p>With a mechanism to {@link Event#combineWith(Event, Edit) combine}
   *   events for the same target, there really is no need anymore for
   *   edits to absorb other edits first. The event combination mechanism
   *   alone suffices. In Beedraz, it is even better not to combine
   *   edits. In Beedraz, edits are more coarse than in Swing. It does not make
   *   sense, for undoability, e.g., to collapse a change of a name from Nele
   *   to Jos, immediately followed by a change of that same name from Jos to
   *   Mieke, into a single change from Nele to Mieke. When the user triggers
   *   undo, he might want to return to the Jos state first. In Swing,
   *   the absorbtion mechanism is intended to merge, e.g., in a text editor,
   *   consecutive one-letter-changes into one text-insert-change, that can
   *   then be undone as a whole. Other than in
   *   {@link javax.swing.undo.UndoableEdit#addEdit(javax.swing.undo.UndoableEdit) Swing},
   *   in Beedraz the programmer decides which sequence is to be considered
   *   as a &quot;whole&quot;, by creating a compound edit. In Swing, deciding
   *   which actions to merge is done by the undoable edits, without much context,
   *   and is therefor a less controlled and more magical feature.</p>
   * <p>In conclusion: this method is implemented to ask each component
   *   edit for its events, and then asks events for the same target,
   *   in the order of the edits that generates them, to combine.</p>
   */
  @Override
  protected Map<AbstractBeed<?>, Event> createEvents() {
    Map<AbstractBeed<?>, Event> result = new HashMap<AbstractBeed<?>, Event>();
    for (Edit<?> componentEdit : $componentEdits) {
      eventsFrom(componentEdit, result);
    }
    return result;
  }

  private void eventsFrom(Edit<?> componentEdit, Map<AbstractBeed<?>, Event> existingEvents) {
    Map<AbstractBeed<?>, ? extends Event> newEvents = componentEdit.createEvents();
    for (Map.Entry<AbstractBeed<?>, ? extends Event> entry : newEvents.entrySet()) {
      AbstractBeed<?> source = entry.getKey();
      Event newEvent = entry.getValue();
      assert newEvent.getSource() == source;
      Event existingEvent = existingEvents.get(source);
      if (existingEvent == null) {
        existingEvents.put(source, newEvent);
      }
      else {
        try {
          Event mergedEvent = existingEvent.combineWith(newEvent, this); // CannotCombineEventsException
          existingEvents.put(source, mergedEvent); // overwrite
        }
        catch (CannotCombineEventsException cmeExc) {
          assert existingEvent.getSource() == newEvent.getSource();
          /* If the reason is DIFFERENT_TYPE in this circumstance, with
           * the same source, this is clearly a programming error.
           * This should'nt happen in Beedraz.
           * The reason can't be DIFFERENT_SOURCE: see assert
           * If the reason is INCOMPATIBLE_STATES in this circumstance,
           * it is programming error in the beeds, events, or in
           * the Beedraz-user code. It is something to signal
           * to the developer, not the end user. */
          assert false : "CannotCombineEventsException shouldn't happen" + cmeExc.toString();
          // MUDO replace with error from ppw-exception
        }
      }
    }
  }


  /*<section name="undo">*/
  //-------------------------------------------------------

  /**
   * The goal state of a compound edit is current
   * if the goal state of the first component edit,
   * from back to front, for a given target, is current.
   * Earlier component edits for the same target must
   * have a current goal state after the later component
   * edit is performed, and this is (should be) so because of
   * how compound edits work.
   */
  @Override
  protected boolean isGoalStateCurrent() {
    ListIterator<Edit<?>> iter = $componentEdits.listIterator($componentEdits.size());
    Set<Beed<?>> alreadyChecked = new HashSet<Beed<?>>();
    while (iter.hasPrevious()) {
      Edit<?> current = iter.previous();
      Beed<?> target = current.getTarget();
      if (! alreadyChecked.contains(target)) {
        if (! current.isGoalStateCurrent()) {
          return false;
        }
        alreadyChecked.add(target);
      }
      // else just skip
    }
    return true;
  }

  @Override
  protected void unperformance() throws IllegalEditException {
    // TODO Auto-generated method stub

  }

  /*</section>*/



  /*<section name="redo">*/
  //-------------------------------------------------------

  /**
   * The initials state of a compound edit is current
   * if the initial sate of the first component edit,
   * from front to back, for a given target, is current.
   * Later component edits for the same target must have
   * a current initial initial state after the earlier
   * component edit is performed, and this is (should be)
   * so because of how compound edits work.
   */
  @Override
  protected boolean isInitialStateCurrent() {
    ListIterator<Edit<?>> iter = $componentEdits.listIterator();
    Set<Beed<?>> alreadyChecked = new HashSet<Beed<?>>();
    while (iter.hasNext()) {
      Edit<?> current = iter.next();
      Beed<?> target = current.getTarget();
      if (! alreadyChecked.contains(target)) {
        if (! current.isInitialStateCurrent()) {
          return false;
        }
        alreadyChecked.add(target);
      }
      // else just skip
    }
    return true;
  }

  /*</section>*/



  /*<section name="kill">*/
  //-------------------------------------------------------

  /**
   * Kill this edit and all {@link #getComponentEdits() component edits}.
   *
   * @post for (Edit<?> edit : #getComponentEdits()) {
   *         edit.getState() = DEAD;
   *       }
   * @post for (Edit<?> edit : #getComponentEdits()) {
   *         for (ValidityListener vl) {! edit.isValidityListener(vl)}
   *       };
   * @post for (Edit<?> edit : #getComponentEdits()) {
   *         for (ValidityListener vl) {vl.isRemoved()}
   *       };
   */
  @Override
  public final void kill() {
    for (Edit<?> e : $componentEdits) {
      e.kill();
    }
    localKill();
  }

  /*</section>*/

}
