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
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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
   * If this compound edit has only 1 component edit, the single edit
   * of that component edit. If this compound edit has more than 1 component
   * edit, this compound edit itself. If this compound edit has 0 component
   * edits, {@code null}.
   *
   * @result getComponentEdits().size() < 1 ? null;
   * @result getComponentEdits().size() == 1 ? getComponentEdits().get(0).getSingleEdit();
   * @result getComponentEdits().size() > 1 ? this;
   */
  public final Edit<?> getSingleEdit() {
    switch ($componentEdits.size()) {
      case 0:
        return null;
      case 1:
        Edit<?> single = getComponentEdits().get(0);
        if (single instanceof CompoundEdit) {
          @SuppressWarnings("unchecked")
          CompoundEdit compoundEdit = ((CompoundEdit)single);
          single = compoundEdit.getSingleEdit();
          // alternatively, add "getSingleEdit()" method to all edits
        }
        return single;
      default:
        return this;
    }
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
