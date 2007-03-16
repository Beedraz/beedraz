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

package org.beedra_II.property.simple;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.edit.StubValidityListener;
import org.beedra_II.edit.Edit.State;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.number.integer.IntegerEvent;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
import org.beedra_II.property.number.integer.long64.EditableLongBeed;
import org.beedra_II.property.number.integer.long64.LongBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSimpleEdit {

  public class MySimpleEdit
       extends SimplePropertyEdit<Long, MyEditableIntegerBeed, ActualLongEvent> {

    public MySimpleEdit(MyEditableIntegerBeed target) {
      super(target);
    }

//    /*
//     * Made public for testing reasons.
//     */
//    public void checkValidityPublic() throws IllegalEditException {
//      super.checkValidity();
//    }

    /*
     * Made public for testing reasons.
     */
    public void notifyListenersPublic() {
      super.notifyListeners();
    }

    @Override
    protected ActualLongEvent createEvent() {
      LongBeed source = new EditableLongBeed(new StubBeanBeed());
      $createdEvent = new ActualLongEvent(source, new Long(0), new Long(1), null);
      return $createdEvent;
    }

    public ActualLongEvent getCreatedEvent() {
      return $createdEvent;
    }

    private ActualLongEvent $createdEvent;

  }

  public class MyEditableIntegerBeed extends EditableLongBeed {

    public MyEditableIntegerBeed(AggregateBeed owner) {
      super(owner);
    }

    @Override
    public boolean isAcceptable(Long goal) {
      return goal != null && goal > 0;
    }

  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  BeanBeed $beanBeed = new StubBeanBeed();
  MyEditableIntegerBeed $target = new MyEditableIntegerBeed($beanBeed);
  private MySimpleEdit $simpleEdit = new MySimpleEdit($target);
  StubValidityListener $listener1 = new StubValidityListener();
  StubValidityListener $listener2 = new StubValidityListener();
  StubListener<IntegerEvent> $listener3 = new StubListener<IntegerEvent>();

  @Test
  public void constructor() {
    assertEquals($simpleEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $simpleEdit.perform();
      $simpleEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
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
      $simpleEdit.perform();
      $simpleEdit.undo();
      $simpleEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
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
      $simpleEdit.kill();
      $simpleEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
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
      $simpleEdit.perform();
      assertEquals($simpleEdit.getState(), State.DONE);
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
      $simpleEdit.addValidityListener($listener1);
      $simpleEdit.addValidityListener($listener2);
      assertTrue($simpleEdit.isValidityListener($listener1));
      assertTrue($simpleEdit.isValidityListener($listener2));
      // perform
      $simpleEdit.setGoal(new Long(1));
      $simpleEdit.perform();
      // listeners should be removed
      assertFalse($simpleEdit.isValidityListener($listener1));
      assertFalse($simpleEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event, $simpleEdit.$createdEvent);
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
      $simpleEdit.addValidityListener($listener1);
      $simpleEdit.addValidityListener($listener2);
      assertTrue($simpleEdit.isValidityListener($listener1));
      assertTrue($simpleEdit.isValidityListener($listener2));
      // perform
      $simpleEdit.setGoal(null);
      $simpleEdit.perform();
      // listeners are removed
      assertFalse($simpleEdit.isValidityListener($listener1));
      assertFalse($simpleEdit.isValidityListener($listener2));
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
      // perform
      $simpleEdit.setGoal(-1L);
      $simpleEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      MySimpleEdit edit1 = new MySimpleEdit($target);
      assertNull(edit1.getInitial());
      Long goal1 = 5L;
      edit1.setGoal(goal1);
      edit1.perform();
      assertNull(edit1.getInitial());
      // perform
      MySimpleEdit edit2 = new MySimpleEdit($target);
      assertNull(edit2.getInitial());
      Long goal2 = 7L;
      edit2.setGoal(goal2);
      edit2.perform();
      assertEquals(edit2.getInitial(), goal1);
      // perform - no change
      MySimpleEdit edit3 = new MySimpleEdit($target);
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
      Long goal = 5L;
      $simpleEdit.setGoal(goal);
      $simpleEdit.perform();
      assertEquals($target.get(), goal);
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
      $simpleEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
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
      $simpleEdit.perform();
      $simpleEdit.undo();
      $simpleEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
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
      $simpleEdit.kill();
      $simpleEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
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
      $simpleEdit.setGoal(5L);
      $simpleEdit.perform();
      $simpleEdit.undo();
      assertEquals($simpleEdit.getState(), State.UNDONE);
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
      $simpleEdit.addValidityListener($listener1);
      $simpleEdit.addValidityListener($listener2);
      assertTrue($simpleEdit.isValidityListener($listener1));
      assertTrue($simpleEdit.isValidityListener($listener2));
      // perform
      $simpleEdit.setGoal(5L);
      $simpleEdit.perform();
      assertFalse($simpleEdit.isValidityListener($listener1));
      assertFalse($simpleEdit.isValidityListener($listener2));
      $simpleEdit.undo();
      // there are no listeners
      assertFalse($simpleEdit.isValidityListener($listener1));
      assertFalse($simpleEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event, $simpleEdit.$createdEvent);
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
    MySimpleEdit edit1 = null;
    try {
      // edit1
      edit1 = new MySimpleEdit($target);
      Long goal1 = 5L;
      edit1.setGoal(goal1);
      edit1.perform();
      // edit2
      MySimpleEdit edit2 = new MySimpleEdit($target);
      Long goal2 = 7L;
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
      MySimpleEdit edit1 = new MySimpleEdit($target);
      Long goal1 = 5L;
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Long goal2 = 7L;
      $simpleEdit.setGoal(goal2);
      $simpleEdit.perform();
      $simpleEdit.undo();
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
      $simpleEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
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
      $simpleEdit.perform();
      $simpleEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
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
      $simpleEdit.kill();
      $simpleEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
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
      $simpleEdit.perform();
      $simpleEdit.undo();
      $simpleEdit.redo();
      assertEquals($simpleEdit.getState(), State.DONE);
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
      $simpleEdit.addValidityListener($listener1);
      $simpleEdit.addValidityListener($listener2);
      assertTrue($simpleEdit.isValidityListener($listener1));
      assertTrue($simpleEdit.isValidityListener($listener2));
      // perform
      $simpleEdit.setGoal(5L);
      $simpleEdit.perform();
      assertFalse($simpleEdit.isValidityListener($listener1));
      assertFalse($simpleEdit.isValidityListener($listener2));
      $simpleEdit.undo();
      $simpleEdit.redo();
      // there are no listeners
      assertFalse($simpleEdit.isValidityListener($listener1));
      assertFalse($simpleEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event, $simpleEdit.$createdEvent);
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
    MySimpleEdit edit1 = null;
    try {
      // edit1
      edit1 = new MySimpleEdit($target);
      Long goal1 = 5L;
      edit1.setGoal(goal1);
      edit1.perform();
      edit1.undo();
      // edit2
      MySimpleEdit edit2 = new MySimpleEdit($target);
      Long goal2 = 7L;
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
      MySimpleEdit edit1 = new MySimpleEdit($target);
      Long goal1 = 5L;
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Long goal2 = 7L;
      $simpleEdit.setGoal(goal2);
      $simpleEdit.perform();
      $simpleEdit.undo();
      $simpleEdit.redo();
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

// Method no longer accessible
//  @Test
//  public void checkValidity() throws EditStateException, IllegalEditException {
//    // add validity listeners
//    $simpleEdit.addValidityListener($listener1);
//    $simpleEdit.addValidityListener($listener2);
//    assertTrue($simpleEdit.isValidityListener($listener1));
//    assertTrue($simpleEdit.isValidityListener($listener2));
//    assertTrue($listener1.isEmpty());
//    assertTrue($listener2.isEmpty());
//    // check the value of the validity
//    assertTrue($simpleEdit.isValid());
//    // change validity
//    $simpleEdit.setGoal(5L);
//    $simpleEdit.checkValidityPublic();
//    // validity is still the same, so validity listeners are not notified
//    assertTrue($simpleEdit.isValid());
//    assertTrue($simpleEdit.isValidityListener($listener1));
//    assertTrue($simpleEdit.isValidityListener($listener2));
//    assertTrue($listener1.isEmpty());
//    assertTrue($listener2.isEmpty());
//    // change validity
//    $simpleEdit.setGoal(-1L);
//    try {
//      $simpleEdit.checkValidityPublic();
//      fail();
//    }
//    catch (IllegalEditException ieExc) {
//      // NOP;
//    }
//    // validity has changed, so validity listeners are notified
//    assertFalse($simpleEdit.isValid());
//    assertTrue($simpleEdit.isValidityListener($listener1));
//    assertTrue($simpleEdit.isValidityListener($listener2));
//    assertEquals($listener1.$target, $simpleEdit);
//    assertEquals($listener1.$validity, $simpleEdit.isValid());
//    assertEquals($listener2.$target, $simpleEdit);
//    assertEquals($listener2.$validity, $simpleEdit.isValid());
//    // change validity again
//    $listener1.reset();
//    $listener2.reset();
//    $simpleEdit.setGoal(2L);
//    $simpleEdit.checkValidityPublic();
//    // validity has changed, so validity listeners are notified
//    assertTrue($simpleEdit.isValid());
//    assertTrue($simpleEdit.isValidityListener($listener1));
//    assertTrue($simpleEdit.isValidityListener($listener2));
//    assertEquals($listener1.$target, $simpleEdit);
//    assertEquals($listener1.$validity, $simpleEdit.isValid());
//    assertEquals($listener2.$target, $simpleEdit);
//    assertEquals($listener2.$validity, $simpleEdit.isValid());
//  }
//
  @Test
  public void notifyListeners() {
    // add listener to beed
    $target.addListener($listener3);
    assertTrue($target.isListener($listener3));
    assertNull($listener3.$event);
    // notify
    $simpleEdit.notifyListenersPublic();
    // check
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event, $simpleEdit.getCreatedEvent());
  }

  @Test
  // incorrect begin-state
  public void setGoal1() {
    try {
      $simpleEdit.setGoal(1L);
      $simpleEdit.perform();
      $simpleEdit.setGoal(2L);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void setGoal2() {
    try {
      $simpleEdit.setGoal(1L);
      $simpleEdit.perform();
      $simpleEdit.undo();
      $simpleEdit.setGoal(2L);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void setGoal3() {
    try {
      $simpleEdit.setGoal(1L);
      $simpleEdit.perform();
      $simpleEdit.undo();
      $simpleEdit.redo();
      $simpleEdit.setGoal(2L);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $simpleEdit);
      assertEquals(e.getCurrentState(), $simpleEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, check postconditions
  public void setGoal4() {
    try {
      assertTrue($simpleEdit.isValid());
      // add validity listeners
      $simpleEdit.addValidityListener($listener1);
      $simpleEdit.addValidityListener($listener2);
      assertTrue($simpleEdit.isValidityListener($listener1));
      assertTrue($simpleEdit.isValidityListener($listener2));
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set valid goal
      Long goal = 1L;
      $simpleEdit.setGoal(goal);
      assertEquals($simpleEdit.getGoal(), goal);
      assertTrue($simpleEdit.isValid());
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set invalid goal
      Long invalidGoal = -1L;
      $simpleEdit.setGoal(invalidGoal);
      assertEquals($simpleEdit.getGoal(), invalidGoal);
      assertFalse($simpleEdit.isValid());
      assertFalse($listener1.isEmpty());
      assertFalse($listener2.isEmpty());
      assertEquals($listener1.$target, $simpleEdit);
      assertEquals($listener1.$validity, $simpleEdit.isValid());
      assertEquals($listener2.$target, $simpleEdit);
      assertEquals($listener2.$validity, $simpleEdit.isValid());
      // set other invalid goal
      Long invalidGoal2 = 0L;
      $listener1.reset();
      $listener2.reset();
      $simpleEdit.setGoal(invalidGoal2);
      assertEquals($simpleEdit.getGoal(), invalidGoal2);
      assertFalse($simpleEdit.isValid());
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set valid goal
      $listener1.reset();
      $listener2.reset();
      $simpleEdit.setGoal(goal);
      assertEquals($simpleEdit.getGoal(), goal);
      assertTrue($simpleEdit.isValid());
      assertEquals($listener1.$target, $simpleEdit);
      assertEquals($listener1.$validity, $simpleEdit.isValid());
      assertEquals($listener2.$target, $simpleEdit);
      assertEquals($listener2.$validity, $simpleEdit.isValid());
      // set other valid goal
      Long goal2 = 3L;
      $listener1.reset();
      $listener2.reset();
      $simpleEdit.setGoal(goal2);
      assertEquals($simpleEdit.getGoal(), goal2);
      assertTrue($simpleEdit.isValid());
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  public void getOldValue() throws EditStateException, IllegalEditException {
    // set value of target
    Long init = 3L;
    MySimpleEdit edit = new MySimpleEdit($target);
    edit.setGoal(init);
    edit.perform();
    // check
    assertEquals($simpleEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($simpleEdit.getGoal(), null);
    assertEquals($simpleEdit.getInitial(), null);
    assertEquals($simpleEdit.getOldValue(), null);
    // set goal
    Long goal = 5L;
    $simpleEdit.setGoal(goal);
    assertEquals($simpleEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), null);
    assertEquals($simpleEdit.getOldValue(), goal);
    // perform
    $simpleEdit.perform();
    assertEquals($simpleEdit.getState(), State.DONE);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), init);
    assertEquals($simpleEdit.getOldValue(), init);
    // undo
    $simpleEdit.undo();
    assertEquals($simpleEdit.getState(), State.UNDONE);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), init);
    assertEquals($simpleEdit.getOldValue(), goal);
    // redo
    $simpleEdit.redo();
    assertEquals($simpleEdit.getState(), State.DONE);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), init);
    assertEquals($simpleEdit.getOldValue(), init);
    // kill
    $simpleEdit.kill();
    assertEquals($simpleEdit.getState(), State.DEAD);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), init);
    assertEquals($simpleEdit.getOldValue(), goal);
  }

  @Test
  public void getNewValue() throws EditStateException, IllegalEditException {
    // set value of target
    Long init = 3L;
    MySimpleEdit edit = new MySimpleEdit($target);
    edit.setGoal(init);
    edit.perform();
    // check
    assertEquals($simpleEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($simpleEdit.getGoal(), null);
    assertEquals($simpleEdit.getInitial(), null);
    assertEquals($simpleEdit.getNewValue(), null);
    // set goal
    Long goal = 5L;
    $simpleEdit.setGoal(goal);
    assertEquals($simpleEdit.getState(), State.NOT_YET_PERFORMED);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), null);
    assertEquals($simpleEdit.getNewValue(), null);
    // perform
    $simpleEdit.perform();
    assertEquals($simpleEdit.getState(), State.DONE);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), init);
    assertEquals($simpleEdit.getNewValue(), goal);
    // undo
    $simpleEdit.undo();
    assertEquals($simpleEdit.getState(), State.UNDONE);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), init);
    assertEquals($simpleEdit.getNewValue(), init);
    // redo
    $simpleEdit.redo();
    assertEquals($simpleEdit.getState(), State.DONE);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), init);
    assertEquals($simpleEdit.getNewValue(), goal);
    // kill
    $simpleEdit.kill();
    assertEquals($simpleEdit.getState(), State.DEAD);
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($simpleEdit.getInitial(), init);
    assertEquals($simpleEdit.getNewValue(), init);
  }

  @Test
  public void isAcceptable() throws EditStateException {
    assertFalse($simpleEdit.isAcceptable());
    $simpleEdit.setGoal(0L);
    assertFalse($simpleEdit.isAcceptable());
    $simpleEdit.setGoal(-1L);
    assertFalse($simpleEdit.isAcceptable());
    $simpleEdit.setGoal(-2L);
    assertFalse($simpleEdit.isAcceptable());
    $simpleEdit.setGoal(1L);
    assertTrue($simpleEdit.isAcceptable());
    $simpleEdit.setGoal(2L);
    assertTrue($simpleEdit.isAcceptable());
  }

  @Test
  public void storeInitialState() throws EditStateException, IllegalEditException {
    assertNull($target.get());
    assertNull($simpleEdit.getInitial());
    $simpleEdit.storeInitialState();
    assertNull($simpleEdit.getInitial());
    // change value of target
    Long goal = 5L;
    $simpleEdit.setGoal(goal);
    $simpleEdit.perform();
    assertEquals($target.get(), goal);
    assertNull($simpleEdit.getInitial());
    $simpleEdit.storeInitialState();
    assertEquals($simpleEdit.getInitial(), goal);
  }

  @Test
  public void isChange() throws EditStateException, IllegalEditException {
    assertNull($simpleEdit.getInitial());
    assertNull($simpleEdit.getGoal());
    assertFalse($simpleEdit.isChange());
    // change goal
    Long goal = 2L;
    $simpleEdit.setGoal(goal);
    assertNull($simpleEdit.getInitial());
    assertEquals($simpleEdit.getGoal(), goal);
    assertTrue($simpleEdit.isChange());
    // perform
    $simpleEdit.perform();
    assertNull($simpleEdit.getInitial());
    assertEquals($simpleEdit.getGoal(), goal);
    assertTrue($simpleEdit.isChange());
    // perform
    Long goal2 = goal;
    MySimpleEdit edit2 = new MySimpleEdit($target);
    edit2.setGoal(goal2);
    edit2.perform();
    assertEquals(edit2.getInitial(), goal);
    assertEquals(edit2.getGoal(), goal2);
    assertFalse(edit2.isChange());
  }

  @Test
  public void isInitialStateCurrent() throws EditStateException, IllegalEditException {
    assertNull($simpleEdit.getInitial());
    assertNull($target.get());
    assertTrue($simpleEdit.isInitialStateCurrent());
    // perform
    Long goal = 2L;
    $simpleEdit.setGoal(goal);
    $simpleEdit.perform();
    assertNull($simpleEdit.getInitial());
    assertEquals($target.get(), goal);
    assertFalse($simpleEdit.isInitialStateCurrent());
    // undo
    $simpleEdit.undo();
    assertNull($simpleEdit.getInitial());
    assertNull($target.get());
    assertTrue($simpleEdit.isInitialStateCurrent());
  }

  @Test
  public void performance() throws EditStateException, IllegalEditException {
    assertNull($simpleEdit.getGoal());
    assertNull($target.get());
    $simpleEdit.performance();
    assertNull($target.get());
    // change goal
    Long goal = 2L;
    $simpleEdit.setGoal(goal);
    assertEquals($simpleEdit.getGoal(), goal);
    assertNull($target.get());
    $simpleEdit.performance();
    assertEquals($target.get(), goal);
  }

  @Test
  public void isGoalStateCurrent() throws EditStateException, IllegalEditException {
    assertNull($simpleEdit.getGoal());
    assertNull($target.get());
    assertTrue($simpleEdit.isGoalStateCurrent());
    // perform
    Long goal = 2L;
    $simpleEdit.setGoal(goal);
    $simpleEdit.perform();
    assertEquals($simpleEdit.getGoal(), goal);
    assertEquals($target.get(), goal);
    assertTrue($simpleEdit.isGoalStateCurrent());
    // undo
    $simpleEdit.undo();
    assertEquals($simpleEdit.getGoal(), goal);
    assertNull($target.get());
    assertFalse($simpleEdit.isGoalStateCurrent());
  }

  @Test
  public void unperformance() throws EditStateException, IllegalEditException {
    assertNull($simpleEdit.getInitial());
    assertNull($target.get());
    $simpleEdit.unperformance();
    assertNull($target.get());
    // perform
    Long goal = 2L;
    $simpleEdit.setGoal(goal);
    $simpleEdit.perform();
    // change initial
    MySimpleEdit edit = new MySimpleEdit($target);
    assertNull(edit.getInitial());
    assertEquals($target.get(), goal);
    edit.unperformance();
    assertNull($target.get());
  }

  @Test
  public void fireEvent() {
    // add listener to target
    $target.addListener($listener3);
    assertTrue($target.isListener($listener3));
    assertNull($listener3.$event);
    // fire
    ActualLongEvent event = new ActualLongEvent($target, new Long(0), new Long(1), null);
    $simpleEdit.fireEvent(event);
    // check listener
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event, event);
  }
}