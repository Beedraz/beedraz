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

package org.beedra_II.property.association.set.sorted;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.edit.ValidityListener;
import org.beedra_II.edit.Edit.State;
import org.beedra_II.event.Listener;
import org.beedra_II.property.association.set.sorted.EditableSortedBidirToOneBeed;
import org.beedra_II.property.association.set.sorted.SortedBidirToManyBeed;
import org.beedra_II.property.association.set.sorted.SortedBidirToOneEdit;
import org.beedra_II.property.association.set.sorted.SortedBidirToOneEvent;
import org.beedra_II.property.set.sorted.SortedSetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSortedBidirToOneEdit {

  public class MySortedBidirToOneEdit
       extends SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> {

    public MySortedBidirToOneEdit(EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> target) {
      super(target);
    }

    /**
     * Made public for testing reasons
     *
     */
    public void notifyListenersPublic() {
      super.notifyListeners();
    }

    /**
     * Made public for testing reasons
     *
     */
    public void storeInitialStatePublic() {
      super.storeInitialState();
    }
  }

  public class MyEditableSortedBidirToOneBeed
      extends EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> {

    public MyEditableSortedBidirToOneBeed(ManyBeanBeed bean) {
      super(bean);
    }

    @Override
    public boolean isAcceptable(SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal) {
      return goal != null && goal.get().size() < 3;
    }
  }

  public class OneBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class ManyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class StubValidityListener implements ValidityListener {

    public void listenerRemoved(Edit<?> target) {
      $target = target;
    }

    public void validityChanged(Edit<?> target, boolean newValidity) {
      $target = target;
      $validity = newValidity;
    }

    public void reset() {
      $target = null;
      $validity = null;
    }

    public boolean isEmpty() {
      return $target == null && $validity == null;
    }

    public Edit<?> $target;

    public Boolean $validity;

  }

  public class StubBidirToOneListener
      implements Listener<SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>> {

    public void beedChanged(SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> $event;

  }

  public class StubSortedBidirToManyListener
    implements Listener<SortedSetEvent<ManyBeanBeed>> {

    public void beedChanged(SortedSetEvent<ManyBeanBeed> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SortedSetEvent<ManyBeanBeed> $event;

  }

  @Before
  public void setUp() throws Exception {
    $manyBeanBeed = new ManyBeanBeed();
    $target = new MyEditableSortedBidirToOneBeed($manyBeanBeed);
    $sortedBidirToOneEdit = new MySortedBidirToOneEdit($target);
    $oneBeanBeed = new OneBeanBeed();
    $listener1 = new StubValidityListener();
    $listener2 = new StubValidityListener();
    $listener3 = new StubBidirToOneListener();
    $listener4 = new StubSortedBidirToManyListener();
    $listener5 = new StubSortedBidirToManyListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $manyBeanBeed;
  private MyEditableSortedBidirToOneBeed $target;
  private MySortedBidirToOneEdit $sortedBidirToOneEdit;
  private OneBeanBeed $oneBeanBeed;
  private StubValidityListener $listener1;
  private StubValidityListener $listener2;
  private StubBidirToOneListener $listener3;
  private StubSortedBidirToManyListener $listener4;
  private StubSortedBidirToManyListener $listener5;

  @Test
  public void constructor() {
    assertEquals($sortedBidirToOneEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void perform2() {
    try {
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.undo();
      $sortedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void perform3() {
    try {
      $sortedBidirToOneEdit.kill();
      $sortedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, check end-state
  public void perform4() {
    try {
      $sortedBidirToOneEdit.perform();
      assertEquals($sortedBidirToOneEdit.getState(), State.DONE);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, validity listeners should be removed, listeners of the beed are notified
  public void perform5() {
    try {
      // add listener to beed
      $target.addListener($listener3);
      assertTrue($target.isListener($listener3));
      assertNull($listener3.$event);
      // add validity listeners to edit
      $sortedBidirToOneEdit.addValidityListener($listener1);
      $sortedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener2));
      // perform
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      Integer goalPosition = 0;
      $sortedBidirToOneEdit.setGoal(goal);
      $sortedBidirToOneEdit.setGoalPosition(goalPosition);
      $sortedBidirToOneEdit.perform();
      // listeners should be removed
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $sortedBidirToOneEdit);
      assertEquals($listener3.$event.getOldValue(), null);
      assertEquals($listener3.$event.getNewValue(), goal);
      assertEquals($listener3.$event.getSource(), $target);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, edit is no change, so validity listeners are removed, listeners of the beed are not notified
  public void perform6() {
    try {
      // add listener to beed
      $target.addListener($listener3);
      assertTrue($target.isListener($listener3));
      assertNull($listener3.$event);
      // add validity listeners to edit
      $sortedBidirToOneEdit.addValidityListener($listener1);
      $sortedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener2));
      // perform
      $sortedBidirToOneEdit.setGoal(null);
      $sortedBidirToOneEdit.perform();
      // listeners are removed
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are not notified
      assertNull($listener3.$event);

      // another perform
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = null;
      MySortedBidirToOneEdit sortedBidirToOneEdit2 = new MySortedBidirToOneEdit($target);
      sortedBidirToOneEdit2.setGoal(goal1);
      sortedBidirToOneEdit2.perform();
      $listener3.reset();
      // repeat this perform, this is no change
      MySortedBidirToOneEdit sortedBidirToOneEdit3 = new MySortedBidirToOneEdit($target);
      // add validity listeners to edit
      sortedBidirToOneEdit3.addValidityListener($listener1);
      sortedBidirToOneEdit3.addValidityListener($listener2);
      assertTrue(sortedBidirToOneEdit3.isValidityListener($listener1));
      assertTrue(sortedBidirToOneEdit3.isValidityListener($listener2));
      sortedBidirToOneEdit3.setGoal(goal1);
      sortedBidirToOneEdit3.perform();
      // listeners are removed
      assertFalse(sortedBidirToOneEdit3.isValidityListener($listener1));
      assertFalse(sortedBidirToOneEdit3.isValidityListener($listener2));
      // listeners of the beed are not notified
      assertNull($listener3.$event);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // when the edit is not valid, an exception should be thrown
  public void perform7() {
    try {
      // create a BidirToManyBeed that is not acceptable
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
        createUnacceptableGoal();
      Integer goalPosition = 0;
      // perform
      $sortedBidirToOneEdit.setGoal(goal);
      $sortedBidirToOneEdit.setGoalPosition(goalPosition);
      $sortedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getMessage(), null);
    }
    try {
      // create a goal position that is not acceptable
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
        createAcceptableGoal();
      Integer goalPosition = 3;
      // perform
      $sortedBidirToOneEdit.setGoal(goal);
      $sortedBidirToOneEdit.setGoalPosition(goalPosition);
      $sortedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  private SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> createUnacceptableGoal() throws EditStateException, IllegalEditException {
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> toMany =
      new SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    addToOne(toMany);
    addToOne(toMany);
    addToOne(toMany);
    return toMany;
  }

  private SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> createAcceptableGoal() {
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      new SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    return goal;
  }

  private void addToOne(SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> toMany) throws EditStateException, IllegalEditException {
    ManyBeanBeed many = new ManyBeanBeed();
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many);
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(toOne);
    edit.setGoal(toMany);
    edit.perform();
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      MySortedBidirToOneEdit edit1 = new MySortedBidirToOneEdit($target);
      assertNull(edit1.getInitial());
      assertNull(edit1.getInitialPosition());
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      assertNull(edit1.getInitial());
      assertNull(edit1.getInitialPosition());
      // perform
      MySortedBidirToOneEdit edit2 = new MySortedBidirToOneEdit($target);
      assertNull(edit2.getInitial());
      assertNull(edit2.getInitialPosition());
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
      Integer goalPosition2 = 0;
      edit2.setGoal(goal2);
      edit2.setGoalPosition(goalPosition2);
      edit2.perform();
      assertEquals(edit2.getInitial(), goal1);
      assertEquals(edit2.getInitialPosition(), goalPosition1);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // check whether the goal is stored in the beed
  public void perform9() {
    try {
      // perform
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      $sortedBidirToOneEdit.setGoal(goal1);
      $sortedBidirToOneEdit.setGoalPosition(goalPosition1);
      $sortedBidirToOneEdit.perform();
      assertEquals($target.get(), goal1);
      assertTrue(goal1.get().size() == 1);
      assertEquals(goal1.get().indexOf($target.getOwner()), goalPosition1);
      // another perform
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
      Integer goalPosition2 = 0;
      MySortedBidirToOneEdit sortedBidirToOneEdit2 = new MySortedBidirToOneEdit($target);
      sortedBidirToOneEdit2.setGoal(goal2);
      sortedBidirToOneEdit2.setGoalPosition(goalPosition2);
      sortedBidirToOneEdit2.perform();
      assertEquals($target.get(), goal2);
      assertTrue(goal2.get().size() == 1);
      assertEquals(goal2.get().indexOf($target.getOwner()), goalPosition2);
      assertTrue(goal1.get().isEmpty());
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void undo1() {
    try {
      $sortedBidirToOneEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.DONE);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void undo2() {
    try {
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.undo();
      $sortedBidirToOneEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.DONE);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void undo3() {
    try {
      $sortedBidirToOneEdit.kill();
      $sortedBidirToOneEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.DONE);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, check end-state
  public void undo4() {
    try {
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      Integer goalPosition = 0;
      $sortedBidirToOneEdit.setGoal(goal);
      $sortedBidirToOneEdit.setGoalPosition(goalPosition);
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.undo();
      assertEquals($sortedBidirToOneEdit.getState(), State.UNDONE);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, so there are no validity listeners, listeners of the beed are notified
  public void undo5() {
    try {
      // add listener to beed
      $target.addListener($listener3);
      assertTrue($target.isListener($listener3));
      assertNull($listener3.$event);
      // add validity listeners to edit
      $sortedBidirToOneEdit.addValidityListener($listener1);
      $sortedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener2));
      // perform
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      Integer goalPosition = 0;
      $sortedBidirToOneEdit.setGoal(goal);
      $sortedBidirToOneEdit.setGoalPosition(goalPosition);
      $sortedBidirToOneEdit.perform();
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener2));
      $sortedBidirToOneEdit.undo();
      // there are no listeners
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $sortedBidirToOneEdit);
      assertEquals($listener3.$event.getOldValue(), goal);
      assertEquals($listener3.$event.getNewValue(), null);
      assertEquals($listener3.$event.getSource(), $target);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // when the goal state does not match the current state, an exception should be thrown
  public void undo7() {
    MySortedBidirToOneEdit edit1 = null;
    try {
      // edit1
      edit1 = new MySortedBidirToOneEdit($target);
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      // edit2
      MySortedBidirToOneEdit edit2 = new MySortedBidirToOneEdit($target);
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
      Integer goalPosition2 = 0;
      edit2.setGoal(goal2);
      edit2.setGoalPosition(goalPosition2);
      edit2.perform();
      // undo edit1
      edit1.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), edit1);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // is the value of the beed set to the original value?
  public void undo8() {
    try {
      // edit1
      MySortedBidirToOneEdit edit1 = new MySortedBidirToOneEdit($target);
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      // $sortedBidirToOneEdit
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
      Integer goalPosition2 = 0;
      $sortedBidirToOneEdit.setGoal(goal2);
      $sortedBidirToOneEdit.setGoalPosition(goalPosition2);
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.undo();
      assertEquals($target.get(), goal1);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void redo1() {
    try {
      $sortedBidirToOneEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.UNDONE);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void redo2() {
    try {
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.UNDONE);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void redo3() {
    try {
      $sortedBidirToOneEdit.kill();
      $sortedBidirToOneEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.UNDONE);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, check end-state
  public void redo4() {
    try {
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.undo();
      $sortedBidirToOneEdit.redo();
      assertEquals($sortedBidirToOneEdit.getState(), State.DONE);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, so there are no validity listeners, listeners of the beed are notified
  public void redo5() {
    try {
      // add listener to beed
      $target.addListener($listener3);
      assertTrue($target.isListener($listener3));
      assertNull($listener3.$event);
      // add validity listeners to edit
      $sortedBidirToOneEdit.addValidityListener($listener1);
      $sortedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener2));
      // perform
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      $sortedBidirToOneEdit.setGoal(goal1);
      $sortedBidirToOneEdit.setGoalPosition(goalPosition1);
      $sortedBidirToOneEdit.perform();
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener2));
      $sortedBidirToOneEdit.undo();
      $sortedBidirToOneEdit.redo();
      // there are no listeners
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($sortedBidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $sortedBidirToOneEdit);
      assertEquals($listener3.$event.getOldValue(), null);
      assertEquals($listener3.$event.getNewValue(), goal1);
      assertEquals($listener3.$event.getSource(), $target);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // when the goal state does not match the current state, an exception should be thrown
  public void redo7() {
    MySortedBidirToOneEdit edit1 = null;
    try {
      // edit1
      edit1 = new MySortedBidirToOneEdit($target);
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      edit1.undo();
      // edit2
      MySortedBidirToOneEdit edit2 = new MySortedBidirToOneEdit($target);
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
      Integer goalPosition2 = 0;
      edit2.setGoal(goal2);
      edit2.setGoalPosition(goalPosition2);
      edit2.perform();
      // redo edit1
      edit1.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), edit1);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // is the value of the beed set to the goal value?
  public void redo8() {
    try {
      // edit1
      MySortedBidirToOneEdit edit1 = new MySortedBidirToOneEdit($target);
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      // $sortedBidirToOneEdit
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
      Integer goalPosition2 = 0;
      $sortedBidirToOneEdit.setGoal(goal2);
      $sortedBidirToOneEdit.setGoalPosition(goalPosition2);
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.undo();
      $sortedBidirToOneEdit.redo();
      assertEquals($target.get(), goal2);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

//  Method no longer accessible
//  @Test
//  public void checkValidity() throws EditStateException, IllegalEditException {
//    // add validity listeners
//    $sortedBidirToOneEdit.addValidityListener($listener1);
//    $sortedBidirToOneEdit.addValidityListener($listener2);
//    assertTrue($sortedBidirToOneEdit.isValidityListener($listener1));
//    assertTrue($sortedBidirToOneEdit.isValidityListener($listener2));
//    assertTrue($listener1.isEmpty());
//    assertTrue($listener2.isEmpty());
//    // check the value of the validity
//    assertTrue($sortedBidirToOneEdit.isValid());
//    // change validity
//    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
//    $sortedBidirToOneEdit.setGoal(goal1);
//    $sortedBidirToOneEdit.checkValidityPublic();
//    // validity is still the same, so validity listeners are not notified
//    assertTrue($sortedBidirToOneEdit.isValid());
//    assertTrue($sortedBidirToOneEdit.isValidityListener($listener1));
//    assertTrue($sortedBidirToOneEdit.isValidityListener($listener2));
//    assertTrue($listener1.isEmpty());
//    assertTrue($listener2.isEmpty());
//    // change validity
//    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createUnacceptableGoal();
//    $sortedBidirToOneEdit.setGoal(goal2);
//    try {
//      $sortedBidirToOneEdit.checkValidityPublic();
//      fail();
//    }
//    catch (IllegalEditException ieExc) {
//      // NOP
//    }
//    // validity has changed, so validity listeners are notified
//    assertFalse($sortedBidirToOneEdit.isValid());
//    assertTrue($sortedBidirToOneEdit.isValidityListener($listener1));
//    assertTrue($sortedBidirToOneEdit.isValidityListener($listener2));
//    assertEquals($listener1.$target, $sortedBidirToOneEdit);
//    assertEquals($listener1.$validity, $sortedBidirToOneEdit.isValid());
//    assertEquals($listener2.$target, $sortedBidirToOneEdit);
//    assertEquals($listener2.$validity, $sortedBidirToOneEdit.isValid());
//    // change validity again
//    $listener1.reset();
//    $listener2.reset();
//    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal3 = createAcceptableGoal();
//    $sortedBidirToOneEdit.setGoal(goal3);
//    $sortedBidirToOneEdit.checkValidityPublic();
//    // validity has changed, so validity listeners are notified
//    assertTrue($sortedBidirToOneEdit.isValid());
//    assertTrue($sortedBidirToOneEdit.isValidityListener($listener1));
//    assertTrue($sortedBidirToOneEdit.isValidityListener($listener2));
//    assertEquals($listener1.$target, $sortedBidirToOneEdit);
//    assertEquals($listener1.$validity, $sortedBidirToOneEdit.isValid());
//    assertEquals($listener2.$target, $sortedBidirToOneEdit);
//    assertEquals($listener2.$validity, $sortedBidirToOneEdit.isValid());
//  }

  @Test
  public void notifyListeners1() throws EditStateException, IllegalEditException {
    // add initial goal
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
    MySortedBidirToOneEdit sortedBidirToOneEdit = new MySortedBidirToOneEdit($target);
    sortedBidirToOneEdit.setGoal(goal1);
    sortedBidirToOneEdit.perform();
    assertEquals($target.get(), goal1);
    // create second goal
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
    // add listener to beed
    $target.addListener($listener3);
    assertTrue($target.isListener($listener3));
    assertNull($listener3.$event);
    // add listener to goal1 and goal2
    goal1.addListener($listener4);
    goal2.addListener($listener5);
    assertTrue(goal1.isListener($listener4));
    assertTrue(goal2.isListener($listener5));
    assertNull($listener4.$event);
    assertNull($listener5.$event);
    // notify
    $sortedBidirToOneEdit.setGoal(goal2);
    $sortedBidirToOneEdit.storeInitialStatePublic();
    $sortedBidirToOneEdit.perform();
//  $sortedBidirToOneEdit.notifyListenersPublic();
    // check whether the listener of the beed is notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getEdit(), $sortedBidirToOneEdit);
    assertEquals($listener3.$event.getOldValue(), goal1);
    assertEquals($listener3.$event.getNewValue(), goal2);
    assertEquals($listener3.$event.getSource(), $target);
    // check whether the listener of goal1 is notified
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getEdit(), $sortedBidirToOneEdit);
    assertEquals($listener4.$event.getOldValue().size(), 1);
    assertEquals($listener4.$event.getOldValue().get(0), $target.getOwner());
    assertTrue($listener4.$event.getNewValue().isEmpty());
    assertEquals($listener4.$event.getSource(), goal1);
    // check whether the listener of goal2 is notified
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getEdit(), $sortedBidirToOneEdit);
    assertTrue($listener5.$event.getOldValue().isEmpty());
    assertEquals($listener5.$event.getNewValue().size(), 1);
    assertEquals($listener5.$event.getNewValue().get(0), $target.getOwner());
    assertEquals($listener5.$event.getSource(), goal2);
  }

  @Test
  public void notifyListeners2() throws EditStateException, IllegalEditException {
    // add initial goal
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
    MySortedBidirToOneEdit sortedBidirToOneEdit = new MySortedBidirToOneEdit($target);
    sortedBidirToOneEdit.setGoal(goal1);
    sortedBidirToOneEdit.perform();
    assertEquals($target.get(), goal1);
    // create second goal
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
    // add listener to beed
    $target.addListener($listener3);
    assertTrue($target.isListener($listener3));
    assertNull($listener3.$event);
    // add listener to goal1 and goal2
    goal1.addListener($listener4);
    goal2.addListener($listener5);
    assertTrue(goal1.isListener($listener4));
    assertTrue(goal2.isListener($listener5));
    assertNull($listener4.$event);
    assertNull($listener5.$event);
    // notify
    $sortedBidirToOneEdit.setGoal(goal2);
    $sortedBidirToOneEdit.storeInitialStatePublic();
    $sortedBidirToOneEdit.perform();
    $listener3.reset();
    $listener4.reset();
    $listener5.reset();
    $sortedBidirToOneEdit.undo();
    // check whether the listener of the beed is notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getEdit(), $sortedBidirToOneEdit);
    assertEquals($listener3.$event.getOldValue(), goal2);
    assertEquals($listener3.$event.getNewValue(), goal1);
    assertEquals($listener3.$event.getSource(), $target);
    // check whether the listener of goal1 is notified
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getEdit(), $sortedBidirToOneEdit);
    assertTrue($listener4.$event.getOldValue().isEmpty());
    assertEquals($listener4.$event.getNewValue().size(), 1);
    assertEquals($listener4.$event.getNewValue().get(0), $target.getOwner());
    assertEquals($listener4.$event.getSource(), goal1);
    // check whether the listener of goal2 is notified
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getEdit(), $sortedBidirToOneEdit);
    assertEquals($listener5.$event.getOldValue().size(), 1);
    assertEquals($listener5.$event.getOldValue().get(0), $target.getOwner());
    assertTrue($listener5.$event.getNewValue().isEmpty());
    assertEquals($listener5.$event.getSource(), goal2);
  }

  @Test
  public void notifyListeners3() throws EditStateException, IllegalEditException {
    // create initial goal, with 2 children
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child1 = createChild();
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child2 = createChild();
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
      createAcceptableGoal(child1, child2);
    // add target to this initial goal
    MySortedBidirToOneEdit sortedBidirToOneEdit = new MySortedBidirToOneEdit($target);
    Integer goalPosition1 = 1;
    sortedBidirToOneEdit.setGoal(goal1);
    sortedBidirToOneEdit.setGoalPosition(goalPosition1);
    sortedBidirToOneEdit.perform();
    assertEquals($target.get(), goal1);
    assertEquals(goal1.get().size(), 3);
    assertEquals(goal1.indexOf(child1.getOwner()), 0);
    assertEquals(goal1.indexOf($target.getOwner()), 1);
    assertEquals(goal1.indexOf(child2.getOwner()), 2);
    // create second goal, with two children
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child3 = createChild();
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child4 = createChild();
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
      createAcceptableGoal(child3, child4);
    assertEquals(goal2.get().size(), 2);
    assertEquals(goal2.indexOf(child3.getOwner()), 0);
    assertEquals(goal2.indexOf(child4.getOwner()), 1);
    // add listener to beed
    $target.addListener($listener3);
    assertTrue($target.isListener($listener3));
    assertNull($listener3.$event);
    // add listener to goal1 and goal2
    goal1.addListener($listener4);
    goal2.addListener($listener5);
    assertTrue(goal1.isListener($listener4));
    assertTrue(goal2.isListener($listener5));
    assertNull($listener4.$event);
    assertNull($listener5.$event);
    // notify
    $sortedBidirToOneEdit.setGoal(goal2);
    Integer goalPosition2 = 0;
    $sortedBidirToOneEdit.setGoalPosition(goalPosition2);
    $sortedBidirToOneEdit.perform();
    // check whether the listener of the beed is notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getEdit(), $sortedBidirToOneEdit);
    assertEquals($listener3.$event.getOldValue(), goal1);
    assertEquals($listener3.$event.getNewValue(), goal2);
    assertEquals($listener3.$event.getSource(), $target);
    // check whether the listener of goal1 is notified
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getEdit(), $sortedBidirToOneEdit);
    assertEquals($listener4.$event.getOldValue().size(), 3);
    assertEquals($listener4.$event.getOldValue().indexOf(child1.getOwner()), 0);
    assertEquals($listener4.$event.getOldValue().indexOf($target.getOwner()), 1);
    assertEquals($listener4.$event.getOldValue().indexOf(child2.getOwner()), 2);
    assertEquals($listener4.$event.getNewValue().size(), 2);
    assertEquals($listener4.$event.getNewValue().indexOf(child1.getOwner()), 0);
    assertEquals($listener4.$event.getNewValue().indexOf(child2.getOwner()), 1);
    assertEquals($listener4.$event.getSource(), goal1);
    // check whether the listener of goal2 is notified
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getEdit(), $sortedBidirToOneEdit);
    assertEquals($listener5.$event.getOldValue().size(), 2);
    assertEquals($listener5.$event.getOldValue().indexOf(child3.getOwner()), 0);
    assertEquals($listener5.$event.getOldValue().indexOf(child4.getOwner()), 1);
    assertEquals($listener5.$event.getNewValue().size(), 3);
    assertEquals($listener5.$event.getNewValue().indexOf($target.getOwner()), 0);
    assertEquals($listener5.$event.getNewValue().indexOf(child3.getOwner()), 1);
    assertEquals($listener5.$event.getNewValue().indexOf(child4.getOwner()), 2);
    assertEquals($listener5.$event.getSource(), goal2);
  }

  private SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> createAcceptableGoal(
      EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child1,
      EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child2)
      throws EditStateException, IllegalEditException {
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> many =
      createAcceptableGoal();
    addChild(child1, many);
    addChild(child2, many);
    return many;
  }

  private void addChild(EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child,
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> parent)
        throws EditStateException, IllegalEditException {
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(child);
    edit.setGoal(parent);
    edit.setGoalPosition(null);
    edit.perform();
  }

  private EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> createChild() {
    return new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
  }

  @Test
  public void performance() throws EditStateException, IllegalEditException {
    // check: old = null, new = null
    assertEquals($sortedBidirToOneEdit.getInitial(), null);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($sortedBidirToOneEdit.getGoal(), null);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), null);
    $sortedBidirToOneEdit.performance();
    assertEquals($target.get(), null);
    // check: old = null, new = goal1
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
    Integer goalPosition1 = 0;
    $sortedBidirToOneEdit.setGoal(goal1);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition1);
    assertEquals($sortedBidirToOneEdit.getInitial(), null);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($sortedBidirToOneEdit.getGoal(), goal1);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition1);
    assertTrue(goal1.get().isEmpty());
    $sortedBidirToOneEdit.performance();
    assertEquals($target.get(), goal1);
    assertTrue(goal1.get().size() == 1);
    assertEquals(goal1.get().indexOf($target.getOwner()), goalPosition1);
    // check: old = goal1, new = goal2
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
    Integer goalPosition2 = 0;
    $sortedBidirToOneEdit.storeInitialStatePublic();
    $sortedBidirToOneEdit.setGoal(goal2);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition2);
    assertEquals($sortedBidirToOneEdit.getInitial(), goal1);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), goalPosition1);
    assertEquals($sortedBidirToOneEdit.getGoal(), goal2);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition2);
    assertTrue(goal1.get().size() == 1);
    assertEquals(goal1.get().indexOf($target.getOwner()), goalPosition1);
    assertTrue(goal2.get().isEmpty());
    $sortedBidirToOneEdit.performance();
    assertEquals($target.get(), goal2);
    assertTrue(goal1.get().isEmpty());
    assertTrue(goal2.get().size() == 1);
    assertEquals(goal2.get().indexOf($target.getOwner()),  goalPosition2);
    // add extra element to goal2
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> esbtoBeed =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> sbtoEdit =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(esbtoBeed);
    sbtoEdit.setGoal(goal2);
    sbtoEdit.setGoalPosition(0);
    sbtoEdit.perform();
    assertTrue(goal2.get().size() == 2);
    assertEquals(goal2.get().indexOf($target.getOwner()), 1);
    assertEquals(goal2.get().indexOf(esbtoBeed.getOwner()), 0);
    // create goal3, having several children
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal3 =
      createAcceptableGoal();
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> esbtoBeed1 =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> sbtoEdit1 =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(esbtoBeed1);
    sbtoEdit1.setGoal(goal3);
    sbtoEdit1.setGoalPosition(0);
    sbtoEdit1.perform();
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> esbtoBeed2 =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> sbtoEdit2 =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(esbtoBeed2);
    sbtoEdit2.setGoal(goal3);
    sbtoEdit2.setGoalPosition(1);
    sbtoEdit2.perform();
    // check: old = goal2, new = goal3
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal4 = goal3;
    Integer goalPosition4 = null;
    $sortedBidirToOneEdit.storeInitialStatePublic();
    $sortedBidirToOneEdit.setGoal(goal4);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition4);
    assertEquals($sortedBidirToOneEdit.getInitial(), goal2);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), 1);
    assertEquals($sortedBidirToOneEdit.getGoal(), goal4);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition4);
    assertTrue(goal2.get().size() == 2);
    assertEquals(goal2.get().indexOf($target.getOwner()), 1);
    assertEquals(goal2.get().indexOf(esbtoBeed.getOwner()), 0);
    assertTrue(goal4.get().size() == 2);
    assertEquals(goal4.get().indexOf(esbtoBeed1.getOwner()), 0);
    assertEquals(goal4.get().indexOf(esbtoBeed2.getOwner()), 1);
    $sortedBidirToOneEdit.performance();
    assertEquals($target.get(), goal4);
    assertTrue(goal2.get().size() == 1);
    assertEquals(goal2.get().indexOf(esbtoBeed.getOwner()), 0);
    assertTrue(goal4.get().size() == 3);
    assertEquals(goal4.get().indexOf(esbtoBeed1.getOwner()), 0);
    assertEquals(goal4.get().indexOf(esbtoBeed2.getOwner()), 1);
    assertEquals(goal4.get().indexOf($target.getOwner()), 2);
    assertNotSame($sortedBidirToOneEdit.getGoalPosition(), goalPosition4);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), 2);
  }

  @Test
  public void unperformance() throws EditStateException, IllegalEditException {
    // check: old = null, new = null
    assertEquals($sortedBidirToOneEdit.getInitial(), null);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($sortedBidirToOneEdit.getGoal(), null);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), null);
    $sortedBidirToOneEdit.unperformance();
    assertEquals($target.get(), null);
    // check: old = null, new = goal1
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
    Integer goalPosition1 = 0;
    $sortedBidirToOneEdit.setGoal(goal1);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition1);
    $sortedBidirToOneEdit.perform();
    assertEquals($sortedBidirToOneEdit.getInitial(), null);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($sortedBidirToOneEdit.getGoal(), goal1);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition1);
    assertTrue(goal1.get().size() == 1);
    assertEquals(goal1.get().indexOf($target.getOwner()), 0);
    $sortedBidirToOneEdit.unperformance();
    assertEquals($target.get(), null);
    assertTrue(goal1.get().isEmpty());
    // check: old = goal1, new = goal2
    MySortedBidirToOneEdit sortedBidirToOneEdit1 = new MySortedBidirToOneEdit($target);
    sortedBidirToOneEdit1.setGoal(goal1);
    sortedBidirToOneEdit1.setGoalPosition(goalPosition1);
    sortedBidirToOneEdit1.perform();
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
    Integer goalPosition2 = 0;
    MySortedBidirToOneEdit sortedBidirToOneEdit2 = new MySortedBidirToOneEdit($target);
    sortedBidirToOneEdit2.setGoal(goal2);
    sortedBidirToOneEdit2.setGoalPosition(goalPosition2);
    sortedBidirToOneEdit2.perform();
    assertEquals(sortedBidirToOneEdit2.getInitial(), goal1);
    assertEquals(sortedBidirToOneEdit2.getInitialPosition(), goalPosition1);
    assertEquals(sortedBidirToOneEdit2.getGoal(), goal2);
    assertEquals(sortedBidirToOneEdit2.getGoalPosition(), goalPosition2);
    assertTrue(goal1.get().isEmpty());
    assertTrue(goal2.get().size() == 1);
    assertEquals(goal2.get().indexOf($target.getOwner()), 0);
    sortedBidirToOneEdit2.unperformance();
    assertEquals($target.get(), goal1);
    assertTrue(goal1.get().size() == 1);
    assertEquals(goal1.get().indexOf($target.getOwner()), goalPosition1);
    assertTrue(goal2.get().isEmpty());
  }

  @Test
  public void createEvent() throws EditStateException, IllegalEditException {
    // can't create event before being performed
    assertEquals(State.NOT_YET_PERFORMED, $sortedBidirToOneEdit.getState());
//    BidirToOneEvent<OneBeanBeed, ManyBeanBeed> createdEvent = $sortedBidirToOneEdit.createEvent();
//    assertEquals(createdEvent.getEdit(), $sortedBidirToOneEdit);
//    assertEquals(createdEvent.getOldValue(), null);
//    assertEquals(createdEvent.getNewValue(), null);
//    assertEquals(createdEvent.getSource(), $target);
    // perform
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
    $sortedBidirToOneEdit.setGoal(goal);
    $sortedBidirToOneEdit.perform();
    // create event
    SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> createdEvent = $sortedBidirToOneEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $sortedBidirToOneEdit);
    assertEquals(createdEvent.getOldValue(), null);
    assertEquals(createdEvent.getNewValue(), goal);
    assertEquals(createdEvent.getSource(), $target);
    // undo
    $sortedBidirToOneEdit.undo();
    // create event
    createdEvent = $sortedBidirToOneEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $sortedBidirToOneEdit);
    assertEquals(createdEvent.getOldValue(), goal);
    assertEquals(createdEvent.getNewValue(), null);
    assertEquals(createdEvent.getSource(), $target);
  }

  @Test
  // incorrect begin-state
  public void setGoalPosition1() {
    try {
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      $sortedBidirToOneEdit.setGoal(goal);
      $sortedBidirToOneEdit.setGoalPosition(0);
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.setGoalPosition(0);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void setGoalPosition2() {
    try {
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      $sortedBidirToOneEdit.setGoal(goal);
      $sortedBidirToOneEdit.setGoalPosition(0);
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.undo();
      $sortedBidirToOneEdit.setGoalPosition(0);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void setGoalPosition3() {
    try {
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      $sortedBidirToOneEdit.setGoal(goal);
      $sortedBidirToOneEdit.setGoalPosition(0);
      $sortedBidirToOneEdit.perform();
      $sortedBidirToOneEdit.undo();
      $sortedBidirToOneEdit.redo();
      $sortedBidirToOneEdit.setGoalPosition(0);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $sortedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $sortedBidirToOneEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, check postconditions
  public void setGoalPosition4() {
    try {
      assertTrue($sortedBidirToOneEdit.isValid());
      // add validity listeners
      $sortedBidirToOneEdit.addValidityListener($listener1);
      $sortedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($sortedBidirToOneEdit.isValidityListener($listener2));
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set valid goal
      SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      $sortedBidirToOneEdit.setGoal(goal);
      Integer goalPosition = 0;
      $sortedBidirToOneEdit.setGoalPosition(goalPosition);
      assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
      assertTrue($sortedBidirToOneEdit.isValid());
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set invalid goal
      Integer invalidGoal = -1;
      $sortedBidirToOneEdit.setGoalPosition(invalidGoal);
      assertEquals($sortedBidirToOneEdit.getGoalPosition(), invalidGoal);
      assertFalse($sortedBidirToOneEdit.isValid());
      assertFalse($listener1.isEmpty());
      assertFalse($listener2.isEmpty());
      assertEquals($listener1.$target, $sortedBidirToOneEdit);
      assertEquals($listener1.$validity, $sortedBidirToOneEdit.isValid());
      assertEquals($listener2.$target, $sortedBidirToOneEdit);
      assertEquals($listener2.$validity, $sortedBidirToOneEdit.isValid());
      // set other invalid goal
      Integer invalidGoal2 = 5;
      $listener1.reset();
      $listener2.reset();
      $sortedBidirToOneEdit.setGoalPosition(invalidGoal2);
      assertEquals($sortedBidirToOneEdit.getGoalPosition(), invalidGoal2);
      assertFalse($sortedBidirToOneEdit.isValid());
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set valid goal
      $listener1.reset();
      $listener2.reset();
      $sortedBidirToOneEdit.setGoalPosition(goalPosition);
      assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
      assertTrue($sortedBidirToOneEdit.isValid());
      assertEquals($listener1.$target, $sortedBidirToOneEdit);
      assertEquals($listener1.$validity, $sortedBidirToOneEdit.isValid());
      assertEquals($listener2.$target, $sortedBidirToOneEdit);
      assertEquals($listener2.$validity, $sortedBidirToOneEdit.isValid());
      // set other valid goal
      Integer goal2 = 0;
      $listener1.reset();
      $listener2.reset();
      $sortedBidirToOneEdit.setGoalPosition(goal2);
      assertEquals($sortedBidirToOneEdit.getGoalPosition(), goal2);
      assertTrue($sortedBidirToOneEdit.isValid());
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  public void getOldPosition() throws EditStateException, IllegalEditException {
    // set value of target
    MySortedBidirToOneEdit edit = new MySortedBidirToOneEdit($target);
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> init =
      createAcceptableGoal();
    Integer initPosition = 0;
    edit.setGoal(init);
    edit.setGoalPosition(initPosition);
    edit.perform();
    // check
    assertEquals($sortedBidirToOneEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), null);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($sortedBidirToOneEdit.getOldPosition(), null);
    // set goal
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $sortedBidirToOneEdit.setGoal(goal);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition);
    assertEquals($sortedBidirToOneEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($sortedBidirToOneEdit.getOldPosition(), goalPosition);
    // perform
    $sortedBidirToOneEdit.perform();
    assertEquals($sortedBidirToOneEdit.getState(), State.DONE);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($sortedBidirToOneEdit.getOldPosition(), initPosition);
    // undo
    $sortedBidirToOneEdit.undo();
    assertEquals($sortedBidirToOneEdit.getState(), State.UNDONE);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($sortedBidirToOneEdit.getOldPosition(), goalPosition);
    // redo
    $sortedBidirToOneEdit.redo();
    assertEquals($sortedBidirToOneEdit.getState(), State.DONE);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($sortedBidirToOneEdit.getOldPosition(), initPosition);
    // kill
    $sortedBidirToOneEdit.kill();
    assertEquals($sortedBidirToOneEdit.getState(), State.DEAD);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($sortedBidirToOneEdit.getOldPosition(), goalPosition);
  }

  @Test
  public void getNewPosition() throws EditStateException, IllegalEditException {
    // set value of target
    MySortedBidirToOneEdit edit = new MySortedBidirToOneEdit($target);
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> init =
      createAcceptableGoal();
    Integer initPosition = 0;
    edit.setGoal(init);
    edit.setGoalPosition(initPosition);
    edit.perform();
    // check
    assertEquals($sortedBidirToOneEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), null);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($sortedBidirToOneEdit.getNewPosition(), null);
    // set goal
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $sortedBidirToOneEdit.setGoal(goal);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition);
    assertEquals($sortedBidirToOneEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($sortedBidirToOneEdit.getNewPosition(), null);
    // perform
    $sortedBidirToOneEdit.perform();
    assertEquals($sortedBidirToOneEdit.getState(), State.DONE);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($sortedBidirToOneEdit.getNewPosition(), goalPosition);
    // undo
    $sortedBidirToOneEdit.undo();
    assertEquals($sortedBidirToOneEdit.getState(), State.UNDONE);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($sortedBidirToOneEdit.getNewPosition(), initPosition);
    // redo
    $sortedBidirToOneEdit.redo();
    assertEquals($sortedBidirToOneEdit.getState(), State.DONE);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($sortedBidirToOneEdit.getNewPosition(), goalPosition);
    // kill
    $sortedBidirToOneEdit.kill();
    assertEquals($sortedBidirToOneEdit.getState(), State.DEAD);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($sortedBidirToOneEdit.getNewPosition(), initPosition);
  }

  @Test
  public void storeInitialState() throws EditStateException, IllegalEditException {
    assertNull($target.get());
    assertNull($sortedBidirToOneEdit.getInitial());
    assertNull($sortedBidirToOneEdit.getInitialPosition());
    $sortedBidirToOneEdit.storeInitialState();
    assertNull($sortedBidirToOneEdit.getInitial());
    assertNull($sortedBidirToOneEdit.getInitialPosition());
    // change value of target
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $sortedBidirToOneEdit.setGoal(goal);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition);
    $sortedBidirToOneEdit.perform();
    assertEquals($target.get(), goal);
    assertEquals($target.get().indexOf($target.getOwner()), 0);
    assertNull($sortedBidirToOneEdit.getInitial());
    assertNull($sortedBidirToOneEdit.getInitialPosition());
    $sortedBidirToOneEdit.storeInitialState();
    assertEquals($sortedBidirToOneEdit.getInitial(), goal);
    assertEquals($sortedBidirToOneEdit.getInitialPosition(), 0);
  }

  @Test
  public void isChange() throws EditStateException, IllegalEditException {
    assertNull($sortedBidirToOneEdit.getInitial());
    assertNull($sortedBidirToOneEdit.getInitialPosition());
    assertNull($sortedBidirToOneEdit.getGoal());
    assertNull($sortedBidirToOneEdit.getGoalPosition());
    assertFalse($sortedBidirToOneEdit.isChange());
    // change goal
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $sortedBidirToOneEdit.setGoal(goal);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition);
    assertNull($sortedBidirToOneEdit.getInitial());
    assertNull($sortedBidirToOneEdit.getInitialPosition());
    assertEquals($sortedBidirToOneEdit.getGoal(), goal);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertTrue($sortedBidirToOneEdit.isChange());
    // perform
    $sortedBidirToOneEdit.perform();
    assertNull($sortedBidirToOneEdit.getInitial());
    assertNull($sortedBidirToOneEdit.getInitialPosition());
    assertEquals($sortedBidirToOneEdit.getGoal(), goal);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertTrue($sortedBidirToOneEdit.isChange());
    // perform
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
      createAcceptableGoal();
    Integer goalPosition2 = 0;
    MySortedBidirToOneEdit edit2 = new MySortedBidirToOneEdit($target);
    edit2.setGoal(goal2);
    edit2.setGoalPosition(goalPosition2);
    edit2.perform();
    assertEquals(edit2.getInitial(), goal);
    assertEquals(edit2.getInitialPosition(), goalPosition);
    assertEquals(edit2.getGoal(), goal2);
    assertEquals(edit2.getGoalPosition(), goalPosition2);
    assertTrue(edit2.isChange());
    // @mudo een verandering van positie, terwijl de goal dezelfde blijft wordt tegengehouden door isAcceptable. Wat doen we?
  }

  @Test
  public void isInitialStateCurrent() throws EditStateException, IllegalEditException {
    assertNull($target.get());
    assertNull($sortedBidirToOneEdit.getInitial());
    assertNull($sortedBidirToOneEdit.getInitialPosition());
    assertTrue($sortedBidirToOneEdit.isInitialStateCurrent());
    // perform
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $sortedBidirToOneEdit.setGoal(goal);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition);
    $sortedBidirToOneEdit.perform();
    assertNull($sortedBidirToOneEdit.getInitial());
    assertNull($sortedBidirToOneEdit.getInitialPosition());
    assertEquals($target.get(), goal);
    assertEquals($target.get().indexOf($target.getOwner()), goalPosition);
    assertFalse($sortedBidirToOneEdit.isInitialStateCurrent());
    // undo
    $sortedBidirToOneEdit.undo();
    assertNull($sortedBidirToOneEdit.getInitial());
    assertNull($sortedBidirToOneEdit.getInitialPosition());
    assertNull($target.get());
    assertTrue($sortedBidirToOneEdit.isInitialStateCurrent());
  }

  @Test
  public void isGoalStateCurrent() throws EditStateException, IllegalEditException {
    assertNull($sortedBidirToOneEdit.getGoal());
    assertNull($sortedBidirToOneEdit.getGoalPosition());
    assertNull($target.get());
    assertTrue($sortedBidirToOneEdit.isGoalStateCurrent());
    // perform
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
      createAcceptableGoal();
    Integer goalPosition1 = 0;
    $sortedBidirToOneEdit.setGoal(goal1);
    $sortedBidirToOneEdit.setGoalPosition(goalPosition1);
    $sortedBidirToOneEdit.perform();
    assertEquals($sortedBidirToOneEdit.getGoal(), goal1);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition1);
    assertEquals($target.get(), goal1);
    assertEquals($target.get().indexOf($target.getOwner()), goalPosition1);
    assertTrue($sortedBidirToOneEdit.isGoalStateCurrent());
    // undo
    $sortedBidirToOneEdit.undo();
    assertEquals($sortedBidirToOneEdit.getGoal(), goal1);
    assertEquals($sortedBidirToOneEdit.getGoalPosition(), goalPosition1);
    assertNull($target.get());
    assertFalse($sortedBidirToOneEdit.isGoalStateCurrent());
    // perform
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> esbtoBeed =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = goal1;
    Integer goalPosition2 = null;
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> sbtoEdit =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(esbtoBeed);
    sbtoEdit.setGoal(goal2);
    sbtoEdit.setGoalPosition(goalPosition2);
    int goalSize = goal2.get().size();
    assertEquals(sbtoEdit.getGoal(), goal2);
    assertEquals(sbtoEdit.getGoalPosition(), null);
    sbtoEdit.perform();
    assertEquals(sbtoEdit.getGoal(), goal2);
    assertEquals(sbtoEdit.getGoalPosition(), goalSize); // perform changes null to the size of the goal (before the perform)
    assertEquals(esbtoBeed.get(), goal2);
    assertNotSame(esbtoBeed.get().indexOf(esbtoBeed.getOwner()), null);
    assertEquals(esbtoBeed.get().indexOf(esbtoBeed.getOwner()), goalSize);
    assertTrue(sbtoEdit.isGoalStateCurrent());
  }
}