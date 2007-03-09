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

package org.beedra_II.property.date;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.edit.StubValidityListener;
import org.beedra_II.edit.Edit.State;
import org.beedra_II.event.StubListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDateEdit {

  public class MyEditableDateBeed extends EditableDateBeed {

    public MyEditableDateBeed(AggregateBeed owner) {
      super(owner);
    }

    @Override
    public boolean isAcceptable(Date goal) {
      return goal != null && goal.before(new Date()) ;
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
  MyEditableDateBeed $target = new MyEditableDateBeed($beanBeed);
  private DateEdit $dateEdit = new DateEdit($target);
  StubValidityListener $listener1 = new StubValidityListener();
  StubValidityListener $listener2 = new StubValidityListener();
  StubListener<DateEvent> $listener3 = new StubListener<DateEvent>();

  @Test
  public void constructor() {
    assertEquals($dateEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $dateEdit.perform();
      $dateEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getCurrentState(), $dateEdit.getState());
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
      $dateEdit.perform();
      $dateEdit.undo();
      $dateEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getCurrentState(), $dateEdit.getState());
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
      $dateEdit.kill();
      $dateEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getCurrentState(), $dateEdit.getState());
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
      $dateEdit.perform();
      assertEquals($dateEdit.getState(), State.DONE);
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
      $dateEdit.addValidityListener($listener1);
      $dateEdit.addValidityListener($listener2);
      assertTrue($dateEdit.isValidityListener($listener1));
      assertTrue($dateEdit.isValidityListener($listener2));
      // perform
      Date goal = Util.createDate(1, 1, 1901);
      $dateEdit.setGoal(goal);
      $dateEdit.perform();
      // listeners should be removed
      assertFalse($dateEdit.isValidityListener($listener1));
      assertFalse($dateEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $dateEdit);
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
      $dateEdit.addValidityListener($listener1);
      $dateEdit.addValidityListener($listener2);
      assertTrue($dateEdit.isValidityListener($listener1));
      assertTrue($dateEdit.isValidityListener($listener2));
      // perform
      $dateEdit.setGoal(null);
      $dateEdit.perform();
      // listeners are removed
      assertFalse($dateEdit.isValidityListener($listener1));
      assertFalse($dateEdit.isValidityListener($listener2));
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
      DateEdit edit = new DateEdit($target);
      Date goal = Util.createDate(2, 2, 1902);
      edit.setGoal(goal);
      edit.perform();
      // perform
      $dateEdit.setGoal(null);
      $dateEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getMessage(), null);
    }
    try {
      Date goal = Util.createFutureDate();
      $dateEdit.setGoal(goal);
      $dateEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      DateEdit edit1 = new DateEdit($target);
      assertNull(edit1.getInitial());
      Date goal1 = Util.createDate(3, 3, 1903);
      edit1.setGoal(goal1);
      edit1.perform();
      assertNull(edit1.getInitial());
      // perform
      DateEdit edit2 = new DateEdit($target);
      assertNull(edit2.getInitial());
      Date goal2 = Util.createDate(4, 4, 1904);
      edit2.setGoal(goal2);
      edit2.perform();
      assertEquals(edit2.getInitial(), goal1);
      // perform - no change
      DateEdit edit3 = new DateEdit($target);
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
      Date goal = Util.createDate(5, 5, 1905);
      $dateEdit.setGoal(goal);
      $dateEdit.perform();
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
      $dateEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getCurrentState(), $dateEdit.getState());
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
      $dateEdit.perform();
      $dateEdit.undo();
      $dateEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getCurrentState(), $dateEdit.getState());
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
      $dateEdit.kill();
      $dateEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getCurrentState(), $dateEdit.getState());
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
      Date goal = Util.createDate(6, 6, 1906);
      $dateEdit.setGoal(goal);
      $dateEdit.perform();
      $dateEdit.undo();
      assertEquals($dateEdit.getState(), State.UNDONE);
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
      $dateEdit.addValidityListener($listener1);
      $dateEdit.addValidityListener($listener2);
      assertTrue($dateEdit.isValidityListener($listener1));
      assertTrue($dateEdit.isValidityListener($listener2));
      // perform
      Date goal = Util.createDate(7, 7, 1907);
      $dateEdit.setGoal(goal);
      $dateEdit.perform();
      assertFalse($dateEdit.isValidityListener($listener1));
      assertFalse($dateEdit.isValidityListener($listener2));
      $dateEdit.undo();
      // there are no listeners
      assertFalse($dateEdit.isValidityListener($listener1));
      assertFalse($dateEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getSource(), $target);
      assertEquals($listener3.$event.getOldValue(), goal);
      assertEquals($listener3.$event.getNewValue(), null);
      assertEquals($listener3.$event.getEdit(), $dateEdit);
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
    DateEdit edit1 = null;
    try {
      // edit1
      edit1 = new DateEdit($target);
      Date goal1 = Util.createDate(8, 8, 1908);
      edit1.setGoal(goal1);
      edit1.perform();
      // edit2
      DateEdit edit2 = new DateEdit($target);
      Date goal2 = Util.createDate(9, 9, 1909);
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
      DateEdit edit1 = new DateEdit($target);
      Date goal1 = Util.createDate(10, 10, 1910);
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Date goal2 = Util.createDate(11, 11, 1911);
      $dateEdit.setGoal(goal2);
      $dateEdit.perform();
      $dateEdit.undo();
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
      $dateEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getCurrentState(), $dateEdit.getState());
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
      $dateEdit.perform();
      $dateEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getCurrentState(), $dateEdit.getState());
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
      $dateEdit.kill();
      $dateEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $dateEdit);
      assertEquals(e.getCurrentState(), $dateEdit.getState());
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
      $dateEdit.perform();
      $dateEdit.undo();
      $dateEdit.redo();
      assertEquals($dateEdit.getState(), State.DONE);
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
      $dateEdit.addValidityListener($listener1);
      $dateEdit.addValidityListener($listener2);
      assertTrue($dateEdit.isValidityListener($listener1));
      assertTrue($dateEdit.isValidityListener($listener2));
      // perform
      Date goal = Util.createDate(12, 12, 1912);
      $dateEdit.setGoal(goal);
      $dateEdit.perform();
      assertFalse($dateEdit.isValidityListener($listener1));
      assertFalse($dateEdit.isValidityListener($listener2));
      $dateEdit.undo();
      $dateEdit.redo();
      // there are no listeners
      assertFalse($dateEdit.isValidityListener($listener1));
      assertFalse($dateEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $dateEdit);
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
  // when the goal state does not match the current state, an exception should be thrown
  public void redo7() {
    DateEdit edit1 = null;
    try {
      // edit1
      edit1 = new DateEdit($target);
      Date goal1 = Util.createDate(13, 1, 1913);
      edit1.setGoal(goal1);
      edit1.perform();
      edit1.undo();
      // edit2
      DateEdit edit2 = new DateEdit($target);
      Date goal2 = Util.createDate(14, 2, 1914);
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
      DateEdit edit1 = new DateEdit($target);
      Date goal1 = Util.createDate(15, 3, 1915);
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Date goal2 = Util.createDate(16, 4, 1916);
      $dateEdit.setGoal(goal2);
      $dateEdit.perform();
      $dateEdit.undo();
      $dateEdit.redo();
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
  public void createEvent() throws EditStateException, IllegalEditException {
    assertEquals(State.NOT_YET_PERFORMED, $dateEdit.getState());
//    DateEvent createdEvent = $dateEdit.createEvent();
//    assertEquals(createdEvent.getEdit(), $dateEdit);
//    assertEquals(createdEvent.getOldValue(), null);
//    assertEquals(createdEvent.getNewValue(), null);
//    assertEquals(createdEvent.getSource(), $target);
    // perform
    Date goal = Util.createDate(17, 5, 1917);
    $dateEdit.setGoal(goal);
    $dateEdit.perform();
    // create event
    DateEvent createdEvent = $dateEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $dateEdit);
    assertEquals(createdEvent.getOldValue(), null);
    assertEquals(createdEvent.getNewValue(), goal);
    assertEquals(createdEvent.getSource(), $target);
    // undo
    $dateEdit.undo();
    // create event
    createdEvent = $dateEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $dateEdit);
    assertEquals(createdEvent.getOldValue(), goal);
    assertEquals(createdEvent.getNewValue(), null);
    assertEquals(createdEvent.getSource(), $target);
  }

}