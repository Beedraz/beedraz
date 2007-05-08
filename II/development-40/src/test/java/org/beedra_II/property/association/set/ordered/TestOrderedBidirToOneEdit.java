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

package org.beedra_II.property.association.set.ordered;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import org.beedra_II.Listener;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.edit.ValidityListener;
import org.beedra_II.edit.Edit.State;
import org.beedra_II.property.association.set.ordered.EditableOrderedBidirToOneBeed;
import org.beedra_II.property.association.set.ordered.OrderedBidirToManyBeed;
import org.beedra_II.property.association.set.ordered.OrderedBidirToOneEdit;
import org.beedra_II.property.association.set.ordered.OrderedBidirToOneEvent;
import org.beedra_II.property.collection.set.ordered.OrderedSetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestOrderedBidirToOneEdit {

  public class MyOrderedBidirToOneEdit
       extends OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> {

    public MyOrderedBidirToOneEdit(EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> target) {
      super(target);
    }

    /**
     * Made public for testing reasons
     *
     */
    public void notifyListenersPublic() {
      super.updateDependents();
    }

    /**
     * Made public for testing reasons
     *
     */
    public void storeInitialStatePublic() {
      super.storeInitialState();
    }
  }

  public class MyEditableOrderedBidirToOneBeed
      extends EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> {

    public MyEditableOrderedBidirToOneBeed(ManyBeanBeed bean) {
      super(bean);
    }

    @Override
    public boolean isAcceptable(OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal) {
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
      implements Listener<OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>> {

    public void beedChanged(OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> $event;

  }

  public class StubOrderedBidirToManyListener
    implements Listener<OrderedSetEvent<ManyBeanBeed>> {

    public void beedChanged(OrderedSetEvent<ManyBeanBeed> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public OrderedSetEvent<ManyBeanBeed> $event;

  }

  @Before
  public void setUp() throws Exception {
    $manyBeanBeed = new ManyBeanBeed();
    $target = new MyEditableOrderedBidirToOneBeed($manyBeanBeed);
    $orderedBidirToOneEdit = new MyOrderedBidirToOneEdit($target);
    $oneBeanBeed = new OneBeanBeed();
    $listener1 = new StubValidityListener();
    $listener2 = new StubValidityListener();
    $listener3 = new StubBidirToOneListener();
    $listener4 = new StubOrderedBidirToManyListener();
    $listener5 = new StubOrderedBidirToManyListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $manyBeanBeed;
  private MyEditableOrderedBidirToOneBeed $target;
  private MyOrderedBidirToOneEdit $orderedBidirToOneEdit;
  private OneBeanBeed $oneBeanBeed;
  private StubValidityListener $listener1;
  private StubValidityListener $listener2;
  private StubBidirToOneListener $listener3;
  private StubOrderedBidirToManyListener $listener4;
  private StubOrderedBidirToManyListener $listener5;

  @Test
  public void constructor() {
    assertEquals($orderedBidirToOneEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.undo();
      $orderedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      $orderedBidirToOneEdit.kill();
      $orderedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      $orderedBidirToOneEdit.perform();
      assertEquals($orderedBidirToOneEdit.getState(), State.DONE);
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
      $orderedBidirToOneEdit.addValidityListener($listener1);
      $orderedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener2));
      // perform
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      Integer goalPosition = 0;
      $orderedBidirToOneEdit.setGoal(goal);
      $orderedBidirToOneEdit.setGoalPosition(goalPosition);
      $orderedBidirToOneEdit.perform();
      // listeners should be removed
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $orderedBidirToOneEdit);
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
      $orderedBidirToOneEdit.addValidityListener($listener1);
      $orderedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener2));
      // perform
      $orderedBidirToOneEdit.setGoal(null);
      $orderedBidirToOneEdit.perform();
      // listeners are removed
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are not notified
      assertNull($listener3.$event);

      // another perform
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = null;
      MyOrderedBidirToOneEdit orderedBidirToOneEdit2 = new MyOrderedBidirToOneEdit($target);
      orderedBidirToOneEdit2.setGoal(goal1);
      orderedBidirToOneEdit2.perform();
      $listener3.reset();
      // repeat this perform, this is no change
      MyOrderedBidirToOneEdit orderedBidirToOneEdit3 = new MyOrderedBidirToOneEdit($target);
      // add validity listeners to edit
      orderedBidirToOneEdit3.addValidityListener($listener1);
      orderedBidirToOneEdit3.addValidityListener($listener2);
      assertTrue(orderedBidirToOneEdit3.isValidityListener($listener1));
      assertTrue(orderedBidirToOneEdit3.isValidityListener($listener2));
      orderedBidirToOneEdit3.setGoal(goal1);
      orderedBidirToOneEdit3.perform();
      // listeners are removed
      assertFalse(orderedBidirToOneEdit3.isValidityListener($listener1));
      assertFalse(orderedBidirToOneEdit3.isValidityListener($listener2));
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
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
        createUnacceptableGoal();
      Integer goalPosition = 0;
      // perform
      $orderedBidirToOneEdit.setGoal(goal);
      $orderedBidirToOneEdit.setGoalPosition(goalPosition);
      $orderedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getMessage(), null);
    }
    try {
      // create a goal position that is not acceptable
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
        createAcceptableGoal();
      Integer goalPosition = 3;
      // perform
      $orderedBidirToOneEdit.setGoal(goal);
      $orderedBidirToOneEdit.setGoalPosition(goalPosition);
      $orderedBidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  private OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> createUnacceptableGoal() throws EditStateException, IllegalEditException {
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> toMany =
      new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    addToOne(toMany);
    addToOne(toMany);
    addToOne(toMany);
    return toMany;
  }

  private OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> createAcceptableGoal() {
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    return goal;
  }

  private void addToOne(OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> toMany) throws EditStateException, IllegalEditException {
    ManyBeanBeed many = new ManyBeanBeed();
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many);
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(toOne);
    edit.setGoal(toMany);
    edit.perform();
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      MyOrderedBidirToOneEdit edit1 = new MyOrderedBidirToOneEdit($target);
      assertNull(edit1.getInitial());
      assertNull(edit1.getInitialPosition());
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      assertNull(edit1.getInitial());
      assertNull(edit1.getInitialPosition());
      // perform
      MyOrderedBidirToOneEdit edit2 = new MyOrderedBidirToOneEdit($target);
      assertNull(edit2.getInitial());
      assertNull(edit2.getInitialPosition());
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
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
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      $orderedBidirToOneEdit.setGoal(goal1);
      $orderedBidirToOneEdit.setGoalPosition(goalPosition1);
      $orderedBidirToOneEdit.perform();
      assertEquals($target.get(), goal1);
      assertTrue(goal1.get().size() == 1);
      assertEquals(goal1.get().indexOf($target.getOwner()), goalPosition1);
      // another perform
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
      Integer goalPosition2 = 0;
      MyOrderedBidirToOneEdit orderedBidirToOneEdit2 = new MyOrderedBidirToOneEdit($target);
      orderedBidirToOneEdit2.setGoal(goal2);
      orderedBidirToOneEdit2.setGoalPosition(goalPosition2);
      orderedBidirToOneEdit2.perform();
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
      $orderedBidirToOneEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.undo();
      $orderedBidirToOneEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      $orderedBidirToOneEdit.kill();
      $orderedBidirToOneEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      Integer goalPosition = 0;
      $orderedBidirToOneEdit.setGoal(goal);
      $orderedBidirToOneEdit.setGoalPosition(goalPosition);
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.undo();
      assertEquals($orderedBidirToOneEdit.getState(), State.UNDONE);
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
      $orderedBidirToOneEdit.addValidityListener($listener1);
      $orderedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener2));
      // perform
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      Integer goalPosition = 0;
      $orderedBidirToOneEdit.setGoal(goal);
      $orderedBidirToOneEdit.setGoalPosition(goalPosition);
      $orderedBidirToOneEdit.perform();
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener2));
      $orderedBidirToOneEdit.undo();
      // there are no listeners
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $orderedBidirToOneEdit);
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
    MyOrderedBidirToOneEdit edit1 = null;
    try {
      // edit1
      edit1 = new MyOrderedBidirToOneEdit($target);
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      // edit2
      MyOrderedBidirToOneEdit edit2 = new MyOrderedBidirToOneEdit($target);
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
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
      MyOrderedBidirToOneEdit edit1 = new MyOrderedBidirToOneEdit($target);
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      // $orderedBidirToOneEdit
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
      Integer goalPosition2 = 0;
      $orderedBidirToOneEdit.setGoal(goal2);
      $orderedBidirToOneEdit.setGoalPosition(goalPosition2);
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.undo();
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
      $orderedBidirToOneEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      $orderedBidirToOneEdit.kill();
      $orderedBidirToOneEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.undo();
      $orderedBidirToOneEdit.redo();
      assertEquals($orderedBidirToOneEdit.getState(), State.DONE);
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
      $orderedBidirToOneEdit.addValidityListener($listener1);
      $orderedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener2));
      // perform
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      $orderedBidirToOneEdit.setGoal(goal1);
      $orderedBidirToOneEdit.setGoalPosition(goalPosition1);
      $orderedBidirToOneEdit.perform();
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener2));
      $orderedBidirToOneEdit.undo();
      $orderedBidirToOneEdit.redo();
      // there are no listeners
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener1));
      assertFalse($orderedBidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $orderedBidirToOneEdit);
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
    MyOrderedBidirToOneEdit edit1 = null;
    try {
      // edit1
      edit1 = new MyOrderedBidirToOneEdit($target);
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      edit1.undo();
      // edit2
      MyOrderedBidirToOneEdit edit2 = new MyOrderedBidirToOneEdit($target);
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
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
      MyOrderedBidirToOneEdit edit1 = new MyOrderedBidirToOneEdit($target);
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
      Integer goalPosition1 = 0;
      edit1.setGoal(goal1);
      edit1.setGoalPosition(goalPosition1);
      edit1.perform();
      // $orderedBidirToOneEdit
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
      Integer goalPosition2 = 0;
      $orderedBidirToOneEdit.setGoal(goal2);
      $orderedBidirToOneEdit.setGoalPosition(goalPosition2);
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.undo();
      $orderedBidirToOneEdit.redo();
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
//    $orderedBidirToOneEdit.addValidityListener($listener1);
//    $orderedBidirToOneEdit.addValidityListener($listener2);
//    assertTrue($orderedBidirToOneEdit.isValidityListener($listener1));
//    assertTrue($orderedBidirToOneEdit.isValidityListener($listener2));
//    assertTrue($listener1.isEmpty());
//    assertTrue($listener2.isEmpty());
//    // check the value of the validity
//    assertTrue($orderedBidirToOneEdit.isValid());
//    // change validity
//    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
//    $orderedBidirToOneEdit.setGoal(goal1);
//    $orderedBidirToOneEdit.checkValidityPublic();
//    // validity is still the same, so validity listeners are not notified
//    assertTrue($orderedBidirToOneEdit.isValid());
//    assertTrue($orderedBidirToOneEdit.isValidityListener($listener1));
//    assertTrue($orderedBidirToOneEdit.isValidityListener($listener2));
//    assertTrue($listener1.isEmpty());
//    assertTrue($listener2.isEmpty());
//    // change validity
//    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createUnacceptableGoal();
//    $orderedBidirToOneEdit.setGoal(goal2);
//    try {
//      $orderedBidirToOneEdit.checkValidityPublic();
//      fail();
//    }
//    catch (IllegalEditException ieExc) {
//      // NOP
//    }
//    // validity has changed, so validity listeners are notified
//    assertFalse($orderedBidirToOneEdit.isValid());
//    assertTrue($orderedBidirToOneEdit.isValidityListener($listener1));
//    assertTrue($orderedBidirToOneEdit.isValidityListener($listener2));
//    assertEquals($listener1.$target, $orderedBidirToOneEdit);
//    assertEquals($listener1.$validity, $orderedBidirToOneEdit.isValid());
//    assertEquals($listener2.$target, $orderedBidirToOneEdit);
//    assertEquals($listener2.$validity, $orderedBidirToOneEdit.isValid());
//    // change validity again
//    $listener1.reset();
//    $listener2.reset();
//    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal3 = createAcceptableGoal();
//    $orderedBidirToOneEdit.setGoal(goal3);
//    $orderedBidirToOneEdit.checkValidityPublic();
//    // validity has changed, so validity listeners are notified
//    assertTrue($orderedBidirToOneEdit.isValid());
//    assertTrue($orderedBidirToOneEdit.isValidityListener($listener1));
//    assertTrue($orderedBidirToOneEdit.isValidityListener($listener2));
//    assertEquals($listener1.$target, $orderedBidirToOneEdit);
//    assertEquals($listener1.$validity, $orderedBidirToOneEdit.isValid());
//    assertEquals($listener2.$target, $orderedBidirToOneEdit);
//    assertEquals($listener2.$validity, $orderedBidirToOneEdit.isValid());
//  }

  @Test
  public void notifyListeners1() throws EditStateException, IllegalEditException {
    // add initial goal
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
    MyOrderedBidirToOneEdit orderedBidirToOneEdit = new MyOrderedBidirToOneEdit($target);
    orderedBidirToOneEdit.setGoal(goal1);
    orderedBidirToOneEdit.perform();
    assertEquals($target.get(), goal1);
    // create second goal
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
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
    $orderedBidirToOneEdit.setGoal(goal2);
    $orderedBidirToOneEdit.storeInitialStatePublic();
    $orderedBidirToOneEdit.perform();
//  $orderedBidirToOneEdit.notifyListenersPublic();
    // check whether the listener of the beed is notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getEdit(), $orderedBidirToOneEdit);
    assertEquals($listener3.$event.getOldValue(), goal1);
    assertEquals($listener3.$event.getNewValue(), goal2);
    assertEquals($listener3.$event.getSource(), $target);
    // check whether the listener of goal1 is notified
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getEdit(), $orderedBidirToOneEdit);
    assertEquals($listener4.$event.getOldValue().size(), 1);
    assertEquals($listener4.$event.getOldValue().get(0), $target.getOwner());
    assertTrue($listener4.$event.getNewValue().isEmpty());
    assertEquals($listener4.$event.getSource(), goal1);
    // check whether the listener of goal2 is notified
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getEdit(), $orderedBidirToOneEdit);
    assertTrue($listener5.$event.getOldValue().isEmpty());
    assertEquals($listener5.$event.getNewValue().size(), 1);
    assertEquals($listener5.$event.getNewValue().get(0), $target.getOwner());
    assertEquals($listener5.$event.getSource(), goal2);
  }

  @Test
  public void notifyListeners2() throws EditStateException, IllegalEditException {
    // add initial goal
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
    MyOrderedBidirToOneEdit orderedBidirToOneEdit = new MyOrderedBidirToOneEdit($target);
    orderedBidirToOneEdit.setGoal(goal1);
    orderedBidirToOneEdit.perform();
    assertEquals($target.get(), goal1);
    // create second goal
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
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
    $orderedBidirToOneEdit.setGoal(goal2);
    $orderedBidirToOneEdit.storeInitialStatePublic();
    $orderedBidirToOneEdit.perform();
    $listener3.reset();
    $listener4.reset();
    $listener5.reset();
    $orderedBidirToOneEdit.undo();
    // check whether the listener of the beed is notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getEdit(), $orderedBidirToOneEdit);
    assertEquals($listener3.$event.getOldValue(), goal2);
    assertEquals($listener3.$event.getNewValue(), goal1);
    assertEquals($listener3.$event.getSource(), $target);
    // check whether the listener of goal1 is notified
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getEdit(), $orderedBidirToOneEdit);
    assertTrue($listener4.$event.getOldValue().isEmpty());
    assertEquals($listener4.$event.getNewValue().size(), 1);
    assertEquals($listener4.$event.getNewValue().get(0), $target.getOwner());
    assertEquals($listener4.$event.getSource(), goal1);
    // check whether the listener of goal2 is notified
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getEdit(), $orderedBidirToOneEdit);
    assertEquals($listener5.$event.getOldValue().size(), 1);
    assertEquals($listener5.$event.getOldValue().get(0), $target.getOwner());
    assertTrue($listener5.$event.getNewValue().isEmpty());
    assertEquals($listener5.$event.getSource(), goal2);
  }

  @Test
  public void notifyListeners3() throws EditStateException, IllegalEditException {
    // create initial goal, with 2 children
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child1 = createChild();
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child2 = createChild();
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
      createAcceptableGoal(child1, child2);
    // add target to this initial goal
    MyOrderedBidirToOneEdit orderedBidirToOneEdit = new MyOrderedBidirToOneEdit($target);
    Integer goalPosition1 = 1;
    orderedBidirToOneEdit.setGoal(goal1);
    orderedBidirToOneEdit.setGoalPosition(goalPosition1);
    orderedBidirToOneEdit.perform();
    assertEquals($target.get(), goal1);
    assertEquals(goal1.get().size(), 3);
    assertEquals(goal1.indexOf(child1.getOwner()), 0);
    assertEquals(goal1.indexOf($target.getOwner()), 1);
    assertEquals(goal1.indexOf(child2.getOwner()), 2);
    // create second goal, with two children
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child3 = createChild();
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child4 = createChild();
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
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
    $orderedBidirToOneEdit.setGoal(goal2);
    Integer goalPosition2 = 0;
    $orderedBidirToOneEdit.setGoalPosition(goalPosition2);
    $orderedBidirToOneEdit.perform();
    // check whether the listener of the beed is notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getEdit(), $orderedBidirToOneEdit);
    assertEquals($listener3.$event.getOldValue(), goal1);
    assertEquals($listener3.$event.getNewValue(), goal2);
    assertEquals($listener3.$event.getSource(), $target);
    // check whether the listener of goal1 is notified
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getEdit(), $orderedBidirToOneEdit);
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
    assertEquals($listener5.$event.getEdit(), $orderedBidirToOneEdit);
    assertEquals($listener5.$event.getOldValue().size(), 2);
    assertEquals($listener5.$event.getOldValue().indexOf(child3.getOwner()), 0);
    assertEquals($listener5.$event.getOldValue().indexOf(child4.getOwner()), 1);
    assertEquals($listener5.$event.getNewValue().size(), 3);
    assertEquals($listener5.$event.getNewValue().indexOf($target.getOwner()), 0);
    assertEquals($listener5.$event.getNewValue().indexOf(child3.getOwner()), 1);
    assertEquals($listener5.$event.getNewValue().indexOf(child4.getOwner()), 2);
    assertEquals($listener5.$event.getSource(), goal2);
  }

  private OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> createAcceptableGoal(
      EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child1,
      EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child2)
      throws EditStateException, IllegalEditException {
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> many =
      createAcceptableGoal();
    addChild(child1, many);
    addChild(child2, many);
    return many;
  }

  private void addChild(EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> child,
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> parent)
        throws EditStateException, IllegalEditException {
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(child);
    edit.setGoal(parent);
    edit.setGoalPosition(null);
    edit.perform();
  }

  private EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> createChild() {
    return new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
  }

  @Test
  public void performance() throws EditStateException, IllegalEditException {
    // check: old = null, new = null
    assertEquals($orderedBidirToOneEdit.getInitial(), null);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($orderedBidirToOneEdit.getGoal(), null);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), null);
    $orderedBidirToOneEdit.performance();
    assertEquals($target.get(), null);
    // check: old = null, new = goal1
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
    Integer goalPosition1 = 0;
    $orderedBidirToOneEdit.setGoal(goal1);
    $orderedBidirToOneEdit.setGoalPosition(goalPosition1);
    assertEquals($orderedBidirToOneEdit.getInitial(), null);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($orderedBidirToOneEdit.getGoal(), goal1);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition1);
    assertTrue(goal1.get().isEmpty());
    $orderedBidirToOneEdit.performance();
    assertEquals($target.get(), goal1);
    assertTrue(goal1.get().size() == 1);
    assertEquals(goal1.get().indexOf($target.getOwner()), goalPosition1);
    // check: old = goal1, new = goal2
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
    Integer goalPosition2 = 0;
    $orderedBidirToOneEdit.storeInitialStatePublic();
    $orderedBidirToOneEdit.setGoal(goal2);
    $orderedBidirToOneEdit.setGoalPosition(goalPosition2);
    assertEquals($orderedBidirToOneEdit.getInitial(), goal1);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), goalPosition1);
    assertEquals($orderedBidirToOneEdit.getGoal(), goal2);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition2);
    assertTrue(goal1.get().size() == 1);
    assertEquals(goal1.get().indexOf($target.getOwner()), goalPosition1);
    assertTrue(goal2.get().isEmpty());
    $orderedBidirToOneEdit.performance();
    assertEquals($target.get(), goal2);
    assertTrue(goal1.get().isEmpty());
    assertTrue(goal2.get().size() == 1);
    assertEquals(goal2.get().indexOf($target.getOwner()),  goalPosition2);
    // add extra element to goal2
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> esbtoBeed =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> sbtoEdit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(esbtoBeed);
    sbtoEdit.setGoal(goal2);
    sbtoEdit.setGoalPosition(0);
    // MUDO because performance is used out of context, now the size beed is not in sync, and perform will have an assertion error
//    sbtoEdit.perform();
//    assertTrue(goal2.get().size() == 2);
//    assertEquals(goal2.get().indexOf($target.getOwner()), 1);
//    assertEquals(goal2.get().indexOf(esbtoBeed.getOwner()), 0);
//    // create goal3, having several children
//    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal3 =
//      createAcceptableGoal();
//    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> esbtoBeed1 =
//      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
//    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> sbtoEdit1 =
//      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(esbtoBeed1);
//    sbtoEdit1.setGoal(goal3);
//    sbtoEdit1.setGoalPosition(0);
//    sbtoEdit1.perform();
//    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> esbtoBeed2 =
//      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
//    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> sbtoEdit2 =
//      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(esbtoBeed2);
//    sbtoEdit2.setGoal(goal3);
//    sbtoEdit2.setGoalPosition(1);
//    sbtoEdit2.perform();
//    // check: old = goal2, new = goal3
//    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal4 = goal3;
//    Integer goalPosition4 = null;
//    $orderedBidirToOneEdit.storeInitialStatePublic();
//    $orderedBidirToOneEdit.setGoal(goal4);
//    $orderedBidirToOneEdit.setGoalPosition(goalPosition4);
//    assertEquals($orderedBidirToOneEdit.getInitial(), goal2);
//    assertEquals($orderedBidirToOneEdit.getInitialPosition(), 1);
//    assertEquals($orderedBidirToOneEdit.getGoal(), goal4);
//    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition4);
//    assertTrue(goal2.get().size() == 2);
//    assertEquals(goal2.get().indexOf($target.getOwner()), 1);
//    assertEquals(goal2.get().indexOf(esbtoBeed.getOwner()), 0);
//    assertTrue(goal4.get().size() == 2);
//    assertEquals(goal4.get().indexOf(esbtoBeed1.getOwner()), 0);
//    assertEquals(goal4.get().indexOf(esbtoBeed2.getOwner()), 1);
//    $orderedBidirToOneEdit.performance();
//    assertEquals($target.get(), goal4);
//    assertTrue(goal2.get().size() == 1);
//    assertEquals(goal2.get().indexOf(esbtoBeed.getOwner()), 0);
//    assertTrue(goal4.get().size() == 3);
//    assertEquals(goal4.get().indexOf(esbtoBeed1.getOwner()), 0);
//    assertEquals(goal4.get().indexOf(esbtoBeed2.getOwner()), 1);
//    assertEquals(goal4.get().indexOf($target.getOwner()), 2);
//    assertNotSame($orderedBidirToOneEdit.getGoalPosition(), goalPosition4);
//    assertEquals($orderedBidirToOneEdit.getGoalPosition(), 2);
  }

  @Test
  public void unperformance() throws EditStateException, IllegalEditException {
    // check: old = null, new = null
    assertEquals($orderedBidirToOneEdit.getInitial(), null);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($orderedBidirToOneEdit.getGoal(), null);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), null);
    $orderedBidirToOneEdit.unperformance();
    assertEquals($target.get(), null);
    // check: old = null, new = goal1
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 = createAcceptableGoal();
    Integer goalPosition1 = 0;
    $orderedBidirToOneEdit.setGoal(goal1);
    $orderedBidirToOneEdit.setGoalPosition(goalPosition1);
    $orderedBidirToOneEdit.perform();
    assertEquals($orderedBidirToOneEdit.getInitial(), null);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($orderedBidirToOneEdit.getGoal(), goal1);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition1);
    assertTrue(goal1.get().size() == 1);
    assertEquals(goal1.get().indexOf($target.getOwner()), 0);
    $orderedBidirToOneEdit.unperformance();
    assertEquals($target.get(), null);
    assertTrue(goal1.get().isEmpty());
    // check: old = goal1, new = goal2
    MyOrderedBidirToOneEdit orderedBidirToOneEdit1 = new MyOrderedBidirToOneEdit($target);
    orderedBidirToOneEdit1.setGoal(goal1);
    orderedBidirToOneEdit1.setGoalPosition(goalPosition1);
    // MUDO because unperformance was used out of context, size beed is now not in sync, and perform will get an assertion error
//    orderedBidirToOneEdit1.perform();
//    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createAcceptableGoal();
//    Integer goalPosition2 = 0;
//    MyOrderedBidirToOneEdit orderedBidirToOneEdit2 = new MyOrderedBidirToOneEdit($target);
//    orderedBidirToOneEdit2.setGoal(goal2);
//    orderedBidirToOneEdit2.setGoalPosition(goalPosition2);
//    orderedBidirToOneEdit2.perform();
//    assertEquals(orderedBidirToOneEdit2.getInitial(), goal1);
//    assertEquals(orderedBidirToOneEdit2.getInitialPosition(), goalPosition1);
//    assertEquals(orderedBidirToOneEdit2.getGoal(), goal2);
//    assertEquals(orderedBidirToOneEdit2.getGoalPosition(), goalPosition2);
//    assertTrue(goal1.get().isEmpty());
//    assertTrue(goal2.get().size() == 1);
//    assertEquals(goal2.get().indexOf($target.getOwner()), 0);
//    orderedBidirToOneEdit2.unperformance();
//    assertEquals($target.get(), goal1);
//    assertTrue(goal1.get().size() == 1);
//    assertEquals(goal1.get().indexOf($target.getOwner()), goalPosition1);
//    assertTrue(goal2.get().isEmpty());
  }

  @Test
  public void createEvent() throws EditStateException, IllegalEditException {
    // can't create event before being performed
    assertEquals(State.NOT_YET_PERFORMED, $orderedBidirToOneEdit.getState());
//    BidirToOneEvent<OneBeanBeed, ManyBeanBeed> createdEvent = $orderedBidirToOneEdit.createEvent();
//    assertEquals(createdEvent.getEdit(), $orderedBidirToOneEdit);
//    assertEquals(createdEvent.getOldValue(), null);
//    assertEquals(createdEvent.getNewValue(), null);
//    assertEquals(createdEvent.getSource(), $target);
    // perform
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
    $orderedBidirToOneEdit.setGoal(goal);
    $orderedBidirToOneEdit.perform();
    // create event
    OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> createdEvent = $orderedBidirToOneEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $orderedBidirToOneEdit);
    assertEquals(createdEvent.getOldValue(), null);
    assertEquals(createdEvent.getNewValue(), goal);
    assertEquals(createdEvent.getSource(), $target);
    // undo
    $orderedBidirToOneEdit.undo();
    // create event
    createdEvent = $orderedBidirToOneEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $orderedBidirToOneEdit);
    assertEquals(createdEvent.getOldValue(), goal);
    assertEquals(createdEvent.getNewValue(), null);
    assertEquals(createdEvent.getSource(), $target);
  }

  @Test
  // incorrect begin-state
  public void setGoalPosition1() {
    try {
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      $orderedBidirToOneEdit.setGoal(goal);
      $orderedBidirToOneEdit.setGoalPosition(0);
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.setGoalPosition(0);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      $orderedBidirToOneEdit.setGoal(goal);
      $orderedBidirToOneEdit.setGoalPosition(0);
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.undo();
      $orderedBidirToOneEdit.setGoalPosition(0);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      $orderedBidirToOneEdit.setGoal(goal);
      $orderedBidirToOneEdit.setGoalPosition(0);
      $orderedBidirToOneEdit.perform();
      $orderedBidirToOneEdit.undo();
      $orderedBidirToOneEdit.redo();
      $orderedBidirToOneEdit.setGoalPosition(0);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $orderedBidirToOneEdit);
      assertEquals(e.getCurrentState(), $orderedBidirToOneEdit.getState());
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
      assertTrue($orderedBidirToOneEdit.isValid());
      // add validity listeners
      $orderedBidirToOneEdit.addValidityListener($listener1);
      $orderedBidirToOneEdit.addValidityListener($listener2);
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener1));
      assertTrue($orderedBidirToOneEdit.isValidityListener($listener2));
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set valid goal
      OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createAcceptableGoal();
      $orderedBidirToOneEdit.setGoal(goal);
      Integer goalPosition = 0;
      $orderedBidirToOneEdit.setGoalPosition(goalPosition);
      assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
      assertTrue($orderedBidirToOneEdit.isValid());
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set invalid goal
      Integer invalidGoal = -1;
      $orderedBidirToOneEdit.setGoalPosition(invalidGoal);
      assertEquals($orderedBidirToOneEdit.getGoalPosition(), invalidGoal);
      assertFalse($orderedBidirToOneEdit.isValid());
      assertFalse($listener1.isEmpty());
      assertFalse($listener2.isEmpty());
      assertEquals($listener1.$target, $orderedBidirToOneEdit);
      assertEquals($listener1.$validity, $orderedBidirToOneEdit.isValid());
      assertEquals($listener2.$target, $orderedBidirToOneEdit);
      assertEquals($listener2.$validity, $orderedBidirToOneEdit.isValid());
      // set other invalid goal
      Integer invalidGoal2 = 5;
      $listener1.reset();
      $listener2.reset();
      $orderedBidirToOneEdit.setGoalPosition(invalidGoal2);
      assertEquals($orderedBidirToOneEdit.getGoalPosition(), invalidGoal2);
      assertFalse($orderedBidirToOneEdit.isValid());
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set valid goal
      $listener1.reset();
      $listener2.reset();
      $orderedBidirToOneEdit.setGoalPosition(goalPosition);
      assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
      assertTrue($orderedBidirToOneEdit.isValid());
      assertEquals($listener1.$target, $orderedBidirToOneEdit);
      assertEquals($listener1.$validity, $orderedBidirToOneEdit.isValid());
      assertEquals($listener2.$target, $orderedBidirToOneEdit);
      assertEquals($listener2.$validity, $orderedBidirToOneEdit.isValid());
      // set other valid goal
      Integer goal2 = 0;
      $listener1.reset();
      $listener2.reset();
      $orderedBidirToOneEdit.setGoalPosition(goal2);
      assertEquals($orderedBidirToOneEdit.getGoalPosition(), goal2);
      assertTrue($orderedBidirToOneEdit.isValid());
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
    MyOrderedBidirToOneEdit edit = new MyOrderedBidirToOneEdit($target);
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> init =
      createAcceptableGoal();
    Integer initPosition = 0;
    edit.setGoal(init);
    edit.setGoalPosition(initPosition);
    edit.perform();
    // check
    assertEquals($orderedBidirToOneEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), null);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($orderedBidirToOneEdit.getOldPosition(), null);
    // set goal
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $orderedBidirToOneEdit.setGoal(goal);
    $orderedBidirToOneEdit.setGoalPosition(goalPosition);
    assertEquals($orderedBidirToOneEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($orderedBidirToOneEdit.getOldPosition(), goalPosition);
    // perform
    $orderedBidirToOneEdit.perform();
    assertEquals($orderedBidirToOneEdit.getState(), State.DONE);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($orderedBidirToOneEdit.getOldPosition(), initPosition);
    // undo
    $orderedBidirToOneEdit.undo();
    assertEquals($orderedBidirToOneEdit.getState(), State.UNDONE);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($orderedBidirToOneEdit.getOldPosition(), goalPosition);
    // redo
    $orderedBidirToOneEdit.redo();
    assertEquals($orderedBidirToOneEdit.getState(), State.DONE);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($orderedBidirToOneEdit.getOldPosition(), initPosition);
    // kill
    $orderedBidirToOneEdit.kill();
    assertEquals($orderedBidirToOneEdit.getState(), State.DEAD);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($orderedBidirToOneEdit.getOldPosition(), goalPosition);
  }

  @Test
  public void getNewPosition() throws EditStateException, IllegalEditException {
    // set value of target
    MyOrderedBidirToOneEdit edit = new MyOrderedBidirToOneEdit($target);
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> init =
      createAcceptableGoal();
    Integer initPosition = 0;
    edit.setGoal(init);
    edit.setGoalPosition(initPosition);
    edit.perform();
    // check
    assertEquals($orderedBidirToOneEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), null);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($orderedBidirToOneEdit.getNewPosition(), null);
    // set goal
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $orderedBidirToOneEdit.setGoal(goal);
    $orderedBidirToOneEdit.setGoalPosition(goalPosition);
    assertEquals($orderedBidirToOneEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), null);
    assertEquals($orderedBidirToOneEdit.getNewPosition(), null);
    // perform
    $orderedBidirToOneEdit.perform();
    assertEquals($orderedBidirToOneEdit.getState(), State.DONE);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($orderedBidirToOneEdit.getNewPosition(), goalPosition);
    // undo
    $orderedBidirToOneEdit.undo();
    assertEquals($orderedBidirToOneEdit.getState(), State.UNDONE);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($orderedBidirToOneEdit.getNewPosition(), initPosition);
    // redo
    $orderedBidirToOneEdit.redo();
    assertEquals($orderedBidirToOneEdit.getState(), State.DONE);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($orderedBidirToOneEdit.getNewPosition(), goalPosition);
    // kill
    $orderedBidirToOneEdit.kill();
    assertEquals($orderedBidirToOneEdit.getState(), State.DEAD);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), initPosition);
    assertEquals($orderedBidirToOneEdit.getNewPosition(), initPosition);
  }

  @Test
  public void storeInitialState() throws EditStateException, IllegalEditException {
    assertNull($target.get());
    assertNull($orderedBidirToOneEdit.getInitial());
    assertNull($orderedBidirToOneEdit.getInitialPosition());
    $orderedBidirToOneEdit.storeInitialState();
    assertNull($orderedBidirToOneEdit.getInitial());
    assertNull($orderedBidirToOneEdit.getInitialPosition());
    // change value of target
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $orderedBidirToOneEdit.setGoal(goal);
    $orderedBidirToOneEdit.setGoalPosition(goalPosition);
    $orderedBidirToOneEdit.perform();
    assertEquals($target.get(), goal);
    assertEquals($target.get().indexOf($target.getOwner()), 0);
    assertNull($orderedBidirToOneEdit.getInitial());
    assertNull($orderedBidirToOneEdit.getInitialPosition());
    $orderedBidirToOneEdit.storeInitialState();
    assertEquals($orderedBidirToOneEdit.getInitial(), goal);
    assertEquals($orderedBidirToOneEdit.getInitialPosition(), 0);
  }

  @Test
  public void isChange() throws EditStateException, IllegalEditException {
    assertNull($orderedBidirToOneEdit.getInitial());
    assertNull($orderedBidirToOneEdit.getInitialPosition());
    assertNull($orderedBidirToOneEdit.getGoal());
    assertNull($orderedBidirToOneEdit.getGoalPosition());
    assertFalse($orderedBidirToOneEdit.isChange());
    // change goal
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $orderedBidirToOneEdit.setGoal(goal);
    $orderedBidirToOneEdit.setGoalPosition(goalPosition);
    assertNull($orderedBidirToOneEdit.getInitial());
    assertNull($orderedBidirToOneEdit.getInitialPosition());
    assertEquals($orderedBidirToOneEdit.getGoal(), goal);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertTrue($orderedBidirToOneEdit.isChange());
    // perform
    $orderedBidirToOneEdit.perform();
    assertNull($orderedBidirToOneEdit.getInitial());
    assertNull($orderedBidirToOneEdit.getInitialPosition());
    assertEquals($orderedBidirToOneEdit.getGoal(), goal);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition);
    assertTrue($orderedBidirToOneEdit.isChange());
    // perform
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
      createAcceptableGoal();
    Integer goalPosition2 = 0;
    MyOrderedBidirToOneEdit edit2 = new MyOrderedBidirToOneEdit($target);
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
    assertNull($orderedBidirToOneEdit.getInitial());
    assertNull($orderedBidirToOneEdit.getInitialPosition());
    assertTrue($orderedBidirToOneEdit.isInitialStateCurrent());
    // perform
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
      createAcceptableGoal();
    Integer goalPosition = 0;
    $orderedBidirToOneEdit.setGoal(goal);
    $orderedBidirToOneEdit.setGoalPosition(goalPosition);
    $orderedBidirToOneEdit.perform();
    assertNull($orderedBidirToOneEdit.getInitial());
    assertNull($orderedBidirToOneEdit.getInitialPosition());
    assertEquals($target.get(), goal);
    assertEquals($target.get().indexOf($target.getOwner()), goalPosition);
    assertFalse($orderedBidirToOneEdit.isInitialStateCurrent());
    // undo
    $orderedBidirToOneEdit.undo();
    assertNull($orderedBidirToOneEdit.getInitial());
    assertNull($orderedBidirToOneEdit.getInitialPosition());
    assertNull($target.get());
    assertTrue($orderedBidirToOneEdit.isInitialStateCurrent());
  }

  @Test
  public void isGoalStateCurrent() throws EditStateException, IllegalEditException {
    assertNull($orderedBidirToOneEdit.getGoal());
    assertNull($orderedBidirToOneEdit.getGoalPosition());
    assertNull($target.get());
    assertTrue($orderedBidirToOneEdit.isGoalStateCurrent());
    // perform
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
      createAcceptableGoal();
    Integer goalPosition1 = 0;
    $orderedBidirToOneEdit.setGoal(goal1);
    $orderedBidirToOneEdit.setGoalPosition(goalPosition1);
    $orderedBidirToOneEdit.perform();
    assertEquals($orderedBidirToOneEdit.getGoal(), goal1);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition1);
    assertEquals($target.get(), goal1);
    assertEquals($target.get().indexOf($target.getOwner()), goalPosition1);
    assertTrue($orderedBidirToOneEdit.isGoalStateCurrent());
    // undo
    $orderedBidirToOneEdit.undo();
    assertEquals($orderedBidirToOneEdit.getGoal(), goal1);
    assertEquals($orderedBidirToOneEdit.getGoalPosition(), goalPosition1);
    assertNull($target.get());
    assertFalse($orderedBidirToOneEdit.isGoalStateCurrent());
    // perform
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> esbtoBeed =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = goal1;
    Integer goalPosition2 = null;
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> sbtoEdit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(esbtoBeed);
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