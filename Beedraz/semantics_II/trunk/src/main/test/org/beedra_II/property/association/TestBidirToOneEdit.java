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

package org.beedra_II.property.association;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.edit.ValidityListener;
import org.beedra_II.edit.Edit.State;
import org.beedra_II.event.Listener;
import org.beedra_II.property.set.SetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBidirToOneEdit {

  public class MyBidirToOneEdit
       extends BidirToOneEdit<OneBeanBeed, ManyBeanBeed> {

    public MyBidirToOneEdit(EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> target) {
      super(target);
    }

    /**
     * Made public for testing reasons
     *
     */
    public void checkValidityPublic() {
      super.checkValidity();
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

  public class MyEditableBidirToOneBeed extends EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> {

    public MyEditableBidirToOneBeed(ManyBeanBeed bean) {
      super(bean);
    }

    @Override
    public boolean isAcceptable(BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal) {
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

  public class StubBidirToOneListener implements Listener<BidirToOneEvent<OneBeanBeed, ManyBeanBeed>> {

    public void beedChanged(BidirToOneEvent<OneBeanBeed, ManyBeanBeed> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public BidirToOneEvent<OneBeanBeed, ManyBeanBeed> $event;

  }

  public class StubBidirToManyListener implements Listener<SetEvent<ManyBeanBeed, BidirToOneEdit<OneBeanBeed, ManyBeanBeed>>> {

    public void beedChanged(SetEvent<ManyBeanBeed, BidirToOneEdit<OneBeanBeed, ManyBeanBeed>> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SetEvent<ManyBeanBeed, BidirToOneEdit<OneBeanBeed, ManyBeanBeed>> $event;

  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $manyBeanBeed = new ManyBeanBeed();
  private MyEditableBidirToOneBeed $target
    = new MyEditableBidirToOneBeed($manyBeanBeed);
  private MyBidirToOneEdit $bidirToOneEdit = new MyBidirToOneEdit($target);
  private OneBeanBeed $oneBeanBeed = new OneBeanBeed();
  StubValidityListener $listener1 = new StubValidityListener();
  StubValidityListener $listener2 = new StubValidityListener();
  StubBidirToOneListener $listener3 = new StubBidirToOneListener();
  StubBidirToManyListener $listener4 = new StubBidirToManyListener();
  StubBidirToManyListener $listener5 = new StubBidirToManyListener();

  @Test
  public void constructor() {
    assertEquals($bidirToOneEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $bidirToOneEdit.perform();
      $bidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getCurrentState(), $bidirToOneEdit.getState());
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
      $bidirToOneEdit.perform();
      $bidirToOneEdit.undo();
      $bidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getCurrentState(), $bidirToOneEdit.getState());
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
      $bidirToOneEdit.kill();
      $bidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getCurrentState(), $bidirToOneEdit.getState());
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
      $bidirToOneEdit.perform();
      assertEquals($bidirToOneEdit.getState(), State.DONE);
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
      $bidirToOneEdit.addValidityListener($listener1);
      $bidirToOneEdit.addValidityListener($listener2);
      assertTrue($bidirToOneEdit.isValidityListener($listener1));
      assertTrue($bidirToOneEdit.isValidityListener($listener2));
      // perform
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      $bidirToOneEdit.setGoal(goal);
      $bidirToOneEdit.perform();
      // listeners should be removed
      assertFalse($bidirToOneEdit.isValidityListener($listener1));
      assertFalse($bidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $bidirToOneEdit);
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
  // correct begin-state, edit is no change, so validity listeners are not removed, listeners of the beed are not notified
  public void perform6() {
    try {
      // add listener to beed
      $target.addListener($listener3);
      assertTrue($target.isListener($listener3));
      assertNull($listener3.$event);
      // add validity listeners to edit
      $bidirToOneEdit.addValidityListener($listener1);
      $bidirToOneEdit.addValidityListener($listener2);
      assertTrue($bidirToOneEdit.isValidityListener($listener1));
      assertTrue($bidirToOneEdit.isValidityListener($listener2));
      // perform
      $bidirToOneEdit.setGoal(null);
      $bidirToOneEdit.perform();
      // listeners are not removed
      assertTrue($bidirToOneEdit.isValidityListener($listener1));
      assertTrue($bidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are not notified
      assertNull($listener3.$event);

      // another perform
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      MyBidirToOneEdit bidirToOneEdit2 = new MyBidirToOneEdit($target);
      bidirToOneEdit2.setGoal(goal1);
      bidirToOneEdit2.perform();
      $listener3.reset();
      // repeat this perform, this is no change
      MyBidirToOneEdit bidirToOneEdit3 = new MyBidirToOneEdit($target);
      // add validity listeners to edit
      bidirToOneEdit3.addValidityListener($listener1);
      bidirToOneEdit3.addValidityListener($listener2);
      assertTrue(bidirToOneEdit3.isValidityListener($listener1));
      assertTrue(bidirToOneEdit3.isValidityListener($listener2));
      bidirToOneEdit3.setGoal(goal1);
      bidirToOneEdit3.perform();
      // listeners are not removed
      assertTrue(bidirToOneEdit3.isValidityListener($listener1));
      assertTrue(bidirToOneEdit3.isValidityListener($listener2));
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
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal = createUnacceptableGoal();
      // perform
      $bidirToOneEdit.setGoal(goal);
      $bidirToOneEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  private BidirToManyBeed<OneBeanBeed, ManyBeanBeed> createUnacceptableGoal() throws EditStateException, IllegalEditException {
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> toMany =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    addToOne(toMany);
    addToOne(toMany);
    addToOne(toMany);
    return toMany;
  }

  private void addToOne(BidirToManyBeed<OneBeanBeed, ManyBeanBeed> toMany) throws EditStateException, IllegalEditException {
    ManyBeanBeed many = new ManyBeanBeed();
    EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne =
      new EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many);
    BidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>(toOne);
    edit.setGoal(toMany);
    edit.perform();
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      MyBidirToOneEdit edit1 = new MyBidirToOneEdit($target);
      assertNull(edit1.getInitial());
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      edit1.setGoal(goal1);
      edit1.perform();
      assertNull(edit1.getInitial());
      // perform
      MyBidirToOneEdit edit2 = new MyBidirToOneEdit($target);
      assertNull(edit2.getInitial());
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      edit2.setGoal(goal2);
      edit2.perform();
      assertEquals(edit2.getInitial(), goal1);
      // perform - no change
      MyBidirToOneEdit edit3 = new MyBidirToOneEdit($target);
      assertNull(edit3.getInitial());
      edit3.setGoal(goal2);
      edit3.perform();
      assertEquals(edit3.getInitial(), goal2);
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
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      $bidirToOneEdit.setGoal(goal1);
      $bidirToOneEdit.perform();
      assertEquals($target.get(), goal1);
      assertTrue(goal1.get().size() == 1);
      assertTrue(goal1.get().contains($target.getOwner()));
      // another perform
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      MyBidirToOneEdit bidirToOneEdit2 = new MyBidirToOneEdit($target);
      bidirToOneEdit2.setGoal(goal2);
      bidirToOneEdit2.perform();
      assertEquals($target.get(), goal2);
      assertTrue(goal2.get().size() == 1);
      assertTrue(goal2.get().contains($target.getOwner()));
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
      $bidirToOneEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getCurrentState(), $bidirToOneEdit.getState());
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
      $bidirToOneEdit.perform();
      $bidirToOneEdit.undo();
      $bidirToOneEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getCurrentState(), $bidirToOneEdit.getState());
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
      $bidirToOneEdit.kill();
      $bidirToOneEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getCurrentState(), $bidirToOneEdit.getState());
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
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      $bidirToOneEdit.setGoal(goal1);
      $bidirToOneEdit.perform();
      $bidirToOneEdit.undo();
      assertEquals($bidirToOneEdit.getState(), State.UNDONE);
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
      $bidirToOneEdit.addValidityListener($listener1);
      $bidirToOneEdit.addValidityListener($listener2);
      assertTrue($bidirToOneEdit.isValidityListener($listener1));
      assertTrue($bidirToOneEdit.isValidityListener($listener2));
      // perform
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      $bidirToOneEdit.setGoal(goal1);
      $bidirToOneEdit.perform();
      assertFalse($bidirToOneEdit.isValidityListener($listener1));
      assertFalse($bidirToOneEdit.isValidityListener($listener2));
      $bidirToOneEdit.undo();
      // there are no listeners
      assertFalse($bidirToOneEdit.isValidityListener($listener1));
      assertFalse($bidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $bidirToOneEdit);
      assertEquals($listener3.$event.getOldValue(), goal1);
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
    MyBidirToOneEdit edit1 = null;
    try {
      // edit1
      edit1 = new MyBidirToOneEdit($target);
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      edit1.setGoal(goal1);
      edit1.perform();
      // edit2
      MyBidirToOneEdit edit2 = new MyBidirToOneEdit($target);
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      edit2.setGoal(goal2);
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
      MyBidirToOneEdit edit1 = new MyBidirToOneEdit($target);
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      $bidirToOneEdit.setGoal(goal2);
      $bidirToOneEdit.perform();
      $bidirToOneEdit.undo();
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
      $bidirToOneEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getCurrentState(), $bidirToOneEdit.getState());
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
      $bidirToOneEdit.perform();
      $bidirToOneEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getCurrentState(), $bidirToOneEdit.getState());
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
      $bidirToOneEdit.kill();
      $bidirToOneEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $bidirToOneEdit);
      assertEquals(e.getCurrentState(), $bidirToOneEdit.getState());
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
      $bidirToOneEdit.perform();
      $bidirToOneEdit.undo();
      $bidirToOneEdit.redo();
      assertEquals($bidirToOneEdit.getState(), State.DONE);
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
      $bidirToOneEdit.addValidityListener($listener1);
      $bidirToOneEdit.addValidityListener($listener2);
      assertTrue($bidirToOneEdit.isValidityListener($listener1));
      assertTrue($bidirToOneEdit.isValidityListener($listener2));
      // perform
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      $bidirToOneEdit.setGoal(goal1);
      $bidirToOneEdit.perform();
      assertFalse($bidirToOneEdit.isValidityListener($listener1));
      assertFalse($bidirToOneEdit.isValidityListener($listener2));
      $bidirToOneEdit.undo();
      $bidirToOneEdit.redo();
      // there are no listeners
      assertFalse($bidirToOneEdit.isValidityListener($listener1));
      assertFalse($bidirToOneEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $bidirToOneEdit);
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
    MyBidirToOneEdit edit1 = null;
    try {
      // edit1
      edit1 = new MyBidirToOneEdit($target);
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      edit1.setGoal(goal1);
      edit1.perform();
      edit1.undo();
      // edit2
      MyBidirToOneEdit edit2 = new MyBidirToOneEdit($target);
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      edit2.setGoal(goal2);
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
      MyBidirToOneEdit edit1 = new MyBidirToOneEdit($target);
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
        new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
      $bidirToOneEdit.setGoal(goal2);
      $bidirToOneEdit.perform();
      $bidirToOneEdit.undo();
      $bidirToOneEdit.redo();
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

  @Test
  public void checkValidity() throws EditStateException, IllegalEditException {
    // add validity listeners
    $bidirToOneEdit.addValidityListener($listener1);
    $bidirToOneEdit.addValidityListener($listener2);
    assertTrue($bidirToOneEdit.isValidityListener($listener1));
    assertTrue($bidirToOneEdit.isValidityListener($listener2));
    assertTrue($listener1.isEmpty());
    assertTrue($listener2.isEmpty());
    // check the value of the validity
    assertTrue($bidirToOneEdit.isValid());
    // change validity
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    $bidirToOneEdit.setGoal(goal1);
    $bidirToOneEdit.checkValidityPublic();
    // validity is still the same, so validity listeners are not notified
    assertTrue($bidirToOneEdit.isValid());
    assertTrue($bidirToOneEdit.isValidityListener($listener1));
    assertTrue($bidirToOneEdit.isValidityListener($listener2));
    assertTrue($listener1.isEmpty());
    assertTrue($listener2.isEmpty());
    // change validity
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 = createUnacceptableGoal();
    $bidirToOneEdit.setGoal(goal2);
    $bidirToOneEdit.checkValidityPublic();
    // validity has changed, so validity listeners are notified
    assertFalse($bidirToOneEdit.isValid());
    assertTrue($bidirToOneEdit.isValidityListener($listener1));
    assertTrue($bidirToOneEdit.isValidityListener($listener2));
    assertEquals($listener1.$target, $bidirToOneEdit);
    assertEquals($listener1.$validity, $bidirToOneEdit.isValid());
    assertEquals($listener2.$target, $bidirToOneEdit);
    assertEquals($listener2.$validity, $bidirToOneEdit.isValid());
    // change validity again
    $listener1.reset();
    $listener2.reset();
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal3 =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    $bidirToOneEdit.setGoal(goal3);
    $bidirToOneEdit.checkValidityPublic();
    // validity has changed, so validity listeners are notified
    assertTrue($bidirToOneEdit.isValid());
    assertTrue($bidirToOneEdit.isValidityListener($listener1));
    assertTrue($bidirToOneEdit.isValidityListener($listener2));
    assertEquals($listener1.$target, $bidirToOneEdit);
    assertEquals($listener1.$validity, $bidirToOneEdit.isValid());
    assertEquals($listener2.$target, $bidirToOneEdit);
    assertEquals($listener2.$validity, $bidirToOneEdit.isValid());
  }

  @Test
  public void notifyListeners() throws EditStateException, IllegalEditException {
    // add initial goal
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    MyBidirToOneEdit bidirToOneEdit = new MyBidirToOneEdit($target);
    bidirToOneEdit.setGoal(goal1);
    bidirToOneEdit.perform();
    assertEquals($target.get(), goal1);
    // create second goal
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
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
    $bidirToOneEdit.setGoal(goal2);
    $bidirToOneEdit.storeInitialStatePublic();
    $bidirToOneEdit.notifyListenersPublic();
    // check whether the listener of the beed is notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getEdit(), $bidirToOneEdit);
    assertEquals($listener3.$event.getOldValue(), goal2);
    assertEquals($listener3.$event.getNewValue(), goal1);
    assertEquals($listener3.$event.getSource(), $target);
    // check whether the listener of goal1 is notified
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getEdit(), $bidirToOneEdit);
    assertTrue($listener4.$event.getAddedElements().size() == 1);
    assertTrue($listener4.$event.getAddedElements().contains($target.getOwner()));
    assertTrue($listener4.$event.getRemovedElements().isEmpty());
    assertEquals($listener4.$event.getSource(), goal1);
    // check whether the listener of goal2 is notified
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getEdit(), $bidirToOneEdit);
    assertTrue($listener5.$event.getAddedElements().isEmpty());
    assertTrue($listener5.$event.getRemovedElements().size() == 1);
    assertTrue($listener5.$event.getRemovedElements().contains($target.getOwner()));
    assertEquals($listener5.$event.getSource(), goal2);
  }

  @Test
  public void performance() throws EditStateException {
    // check: old = null, new = null
    assertEquals($bidirToOneEdit.getInitial(), null);
    assertEquals($bidirToOneEdit.getGoal(), null);
    $bidirToOneEdit.performance();
    assertEquals($target.get(), null);
    // check: old = null, new = goal1
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    $bidirToOneEdit.setGoal(goal1);
    assertEquals($bidirToOneEdit.getInitial(), null);
    assertEquals($bidirToOneEdit.getGoal(), goal1);
    assertTrue(goal1.get().isEmpty());
    $bidirToOneEdit.performance();
    assertEquals($target.get(), goal1);
    assertTrue(goal1.get().size() == 1);
    assertTrue(goal1.get().contains($target.getOwner()));
    // check: old = goal1, new = goal2
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    $bidirToOneEdit.storeInitialStatePublic();
    $bidirToOneEdit.setGoal(goal2);
    assertEquals($bidirToOneEdit.getInitial(), goal1);
    assertEquals($bidirToOneEdit.getGoal(), goal2);
    assertTrue(goal1.get().size() == 1);
    assertTrue(goal1.get().contains($target.getOwner()));
    assertTrue(goal2.get().isEmpty());
    $bidirToOneEdit.performance();
    assertEquals($target.get(), goal2);
    assertTrue(goal1.get().isEmpty());
    assertTrue(goal2.get().size() == 1);
    assertTrue(goal2.get().contains($target.getOwner()));
  }

  @Test
  public void unperformance() throws EditStateException, IllegalEditException {
    // check: old = null, new = null
    assertEquals($bidirToOneEdit.getInitial(), null);
    assertEquals($bidirToOneEdit.getGoal(), null);
    $bidirToOneEdit.unperformance();
    assertEquals($target.get(), null);
    // check: old = null, new = goal1
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal1 =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    $bidirToOneEdit.setGoal(goal1);
    $bidirToOneEdit.perform();
    assertEquals($bidirToOneEdit.getInitial(), null);
    assertEquals($bidirToOneEdit.getGoal(), goal1);
    assertTrue(goal1.get().size() == 1);
    assertTrue(goal1.get().contains($target.getOwner()));
    $bidirToOneEdit.unperformance();
    assertEquals($target.get(), null);
    assertTrue(goal1.get().isEmpty());
    // check: old = goal1, new = goal2
    MyBidirToOneEdit bidirToOneEdit1 = new MyBidirToOneEdit($target);
    bidirToOneEdit1.setGoal(goal1);
    bidirToOneEdit1.perform();
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> goal2 =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($oneBeanBeed);
    MyBidirToOneEdit bidirToOneEdit2 = new MyBidirToOneEdit($target);
    bidirToOneEdit2.setGoal(goal2);
    bidirToOneEdit2.perform();
    assertEquals(bidirToOneEdit2.getInitial(), goal1);
    assertEquals(bidirToOneEdit2.getGoal(), goal2);
    assertTrue(goal1.get().isEmpty());
    assertTrue(goal2.get().size() == 1);
    assertTrue(goal2.get().contains($target.getOwner()));
    bidirToOneEdit2.unperformance();
    assertEquals($target.get(), goal1);
    assertTrue(goal1.get().size() == 1);
    assertTrue(goal1.get().contains($target.getOwner()));
    assertTrue(goal2.get().isEmpty());
  }
}