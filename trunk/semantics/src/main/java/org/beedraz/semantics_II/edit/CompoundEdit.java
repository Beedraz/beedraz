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


import static org.beedraz.semantics_II.edit.Edit.State.DONE;
import static org.beedraz.semantics_II.edit.Edit.State.NOT_YET_PERFORMED;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Event;
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
public final class CompoundEdit<_Target_ extends Beed<_Event_>,
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
   * Asks all component edits to execute performance,
   * in the order they were added.
   * If one of the performance executions fails, what is performed until
   * now is undone again (rollback). If something fails during this undo, something
   * is seriously wrong, and the application is in an inconsistent state.
   * After rollback, the offending exception is propagated.
   */
  @Override
  final protected void performance() throws IllegalEditException {
    ListIterator<Edit<?>> iter = $componentEdits.listIterator();
    try {
      while (iter.hasNext()) {
        Edit<?> e = iter.next();
        e.recalculateValidity();
        e.checkValidity(); // throws IllegalEditException
        e.performance(); // throws IllegalEditException; this doesn't change the state yet, and doesn't send events
      }
    }
    catch (IllegalEditException eExc) {
      // rollback
      iter.previous(); // last edit didn't succeed, and doesn't need to be rolled-back
      try {
        while (iter.hasPrevious()) {
          Edit<?> e = iter.previous();
          assert e.getState() == DONE;
          e.unperformance();  // no exceptions
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
   * MUDO validity of component edits must be evaluated in context of
   *       earlier edits; this requires a validation expression that allows
   *       for subsitution; a component edit might be invalid (beforehand)
   *       in isolation, but it might be valid by the time we reach it;
   *       so the compound might be valid even if some components are not
   *       (before performance) -- and vice versa.
   *       So, changes in validity of components are not really relevant.
   *       If a goal changes, we need to re-evaluate completely, even
   *       if the validity of the component doesn't change.
   *       We are interested in removal though, since this signals dead.
   */

  @Override
  protected final boolean isAcceptable() {
    // MUDO, for now, all component edits need to be valid, but that is not true
    for (Edit<?> e : $componentEdits) {
      if (! e.isAcceptable()) {
        return false;
      }
    }
    return true;
  }

  private ValidityListener $componentEditListener = new ValidityListener() {

    public void validityChanged(Edit<?> target, boolean newValidity) {
//      if (isValid() && (! newValidity)) {
//        // we were true, and now have become false
//        assignValid(false);
//      }
//      else if ((! isValid()) && newValidity) {
//        // we were false; maybe we are true now
//        for (Edit<?> e : $edits) {
//          if (! e.isValid()) {
//            // we were invalid, and still are; nothing to do
//            return;
//          }
//        }
//        // we were invalid, but now all components are valid: we have changed
//        assignValid(true);
//      }
//      /* else, we were invalid, and a component changed from true to false:
//       * we are even more invalid
//       * or, we were valid, and a component changed from false to true:
//       * this is impossible: if a component would change from false to true,
//       * there was a component before that was invalid, so we were invalid
//       */
      recalculateValidity();
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





  @Override
  protected _Event_ createEvent() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void updateDependents(_Event_ event) {
    // TODO Auto-generated method stub

  }

  /*<section name="undo">*/
  //-------------------------------------------------------

  /**
   * The initial state of a compound edit is current
   * if the initial state of all component edits
   * is current.
   *
   * @mudo This is not true: a previous edit can change conditions
   *       for a later edit, making the initial state current for
   *       that edit.
   */
  @Override
  protected boolean isGoalStateCurrent() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected void unperformance() throws IllegalEditException {
    // TODO Auto-generated method stub

  }

  /*</section>*/



  /*<section name="redo">*/
  //-------------------------------------------------------

  /**
   * The initial state of a compound edit is current
   * if the initial state of all component edits
   * is current.
   *
   * @mudo This is not true: a previous edit can change conditions
   *       for a later edit, making the initial state current for
   *       that edit.
   */
  @Override
  protected boolean isInitialStateCurrent() {
    for (Edit<?> edit : $componentEdits) {
      if (! edit.isInitialStateCurrent()) {
        return false;
      }
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
