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
import static org.beedra_II.edit.Edit.State.NOT_YET_PERFORMED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.beedra_II.Beed;
import org.beedra_II.event.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Assembles little edits into great big ones.
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
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class CompoundEdit<_Target_ extends Beed<_Event_>, // MUDO must not be editable
                                _Event_ extends Event>
    extends AbstractEdit<_Target_, _Event_> {


  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @param target
   *        If the targets of the element edits differ, this target should be
   *        an object that is upstream of all child targets.
   *
   *
   * @pre target != null;
   * @post getTarget() == target;
   */
  public CompoundEdit(_Target_ target) {
    super(target);
  }

  /*</construction>*/



  /*<section name="validity">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final List<Edit<?>> getEdits() {
    return Collections.unmodifiableList($edits);
  }

  /**
   * Component edits might even be invalid when added.
   *
   * @pre componentEdit != null;
   * @pre componentEdit.getState() == NOT_YET_PERFORMED;
   */
  public final void addComponentEdit(Edit<?> componentEdit) throws EditStateException {
    assert componentEdit != null;
    assert componentEdit.getState() == NOT_YET_PERFORMED;
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    componentEdit.addValidityListener($componentEditListener); // for dead
    $edits.add(componentEdit);
    recalculateValidity();
  }

  private final List<Edit<?>> $edits = new ArrayList<Edit<?>>();

  /*</section>*/



  /*<property name="state">*/
  //-------------------------------------------------------

  @Override
  public final void kill() {
    for (Edit<?> e : $edits) {
      e.kill();
    }
    localKill();
  }

  /*</property>*/



  /*<section name="perform">*/
  //------------------------------------------------------------------

  /**
   * Ask component edits to stare initial state;
   */
  @Override
  protected final void storeInitialState() {
    for (Edit<?> e : $edits) {
      e.storeInitialState();
    }
  }

  /**
   * There is a change if there is at least one component that is a change.
   */
  @Override
  public final boolean isChange() {
    for (Edit<?> e : $edits) {
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
    ListIterator<Edit<?>> iter = $edits.listIterator();
    try {
      while (iter.hasNext()) {
        Edit<?> e = iter.next();
        e.recalculateValidity();
        e.checkValidity(); // throws IllegalEditException
        e.performance(); // throws IllegalEditException
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
    for (Edit<?> e : $edits) {
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
   *       if isolation, but it might be valid by the time we reach it;
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
    for (Edit<?> e : $edits) {
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
      for (Edit<?> e : $edits) {
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

  @Override
  protected boolean isGoalStateCurrent() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected boolean isInitialStateCurrent() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected void unperformance() throws IllegalEditException {
    // TODO Auto-generated method stub

  }



}
