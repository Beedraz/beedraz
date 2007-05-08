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

package org.beedra_II.property.integer;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

public class TestIntegerEdit {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  BeanBeed $beanBeed = new StubBeanBeed();
  StubEditableIntegerBeed $target = new StubEditableIntegerBeed($beanBeed);
  private IntegerEdit $integerEdit = new IntegerEdit($target);
  StubValidityListener $listener1 = new StubValidityListener();
  StubValidityListener $listener2 = new StubValidityListener();
  StubListener<IntegerEvent> $listener3 = new StubListener<IntegerEvent>();

  @Test
  public void constructor() {
    assertEquals($integerEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $integerEdit.perform();
      $integerEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getCurrentState(), $integerEdit.getState());
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
      $integerEdit.perform();
      $integerEdit.undo();
      $integerEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getCurrentState(), $integerEdit.getState());
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
      $integerEdit.kill();
      $integerEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getCurrentState(), $integerEdit.getState());
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
      $integerEdit.perform();
      assertEquals($integerEdit.getState(), State.DONE);
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
      $integerEdit.addValidityListener($listener1);
      $integerEdit.addValidityListener($listener2);
      assertTrue($integerEdit.isValidityListener($listener1));
      assertTrue($integerEdit.isValidityListener($listener2));
      // perform
      Integer goal = 1;
      $integerEdit.setGoal(goal);
      $integerEdit.perform();
      // listeners should be removed
      assertFalse($integerEdit.isValidityListener($listener1));
      assertFalse($integerEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $integerEdit);
      assertEquals($listener3.$event.getOldInteger(), null);
      assertEquals($listener3.$event.getNewInteger(), goal);
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
      $integerEdit.addValidityListener($listener1);
      $integerEdit.addValidityListener($listener2);
      assertTrue($integerEdit.isValidityListener($listener1));
      assertTrue($integerEdit.isValidityListener($listener2));
      // perform
      $integerEdit.setGoal(null);
      $integerEdit.perform();
      // listeners are removed
      assertFalse($integerEdit.isValidityListener($listener1));
      assertFalse($integerEdit.isValidityListener($listener2));
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
      IntegerEdit edit = new IntegerEdit($target);
      Integer goal = 1;
      edit.setGoal(goal);
      edit.perform();
      // perform
      $integerEdit.setGoal(null);
      $integerEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      IntegerEdit edit1 = new IntegerEdit($target);
      assertNull(edit1.getInitial());
      Integer goal1 = 1;
      edit1.setGoal(goal1);
      edit1.perform();
      assertNull(edit1.getInitial());
      // perform
      IntegerEdit edit2 = new IntegerEdit($target);
      assertNull(edit2.getInitial());
      Integer goal2 = 2;
      edit2.setGoal(goal2);
      edit2.perform();
      assertEquals(edit2.getInitial(), goal1);
      // perform - no change
      IntegerEdit edit3 = new IntegerEdit($target);
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
      Integer goal = 1;
      $integerEdit.setGoal(goal);
      $integerEdit.perform();
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
      $integerEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getCurrentState(), $integerEdit.getState());
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
      $integerEdit.perform();
      $integerEdit.undo();
      $integerEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getCurrentState(), $integerEdit.getState());
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
      $integerEdit.kill();
      $integerEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getCurrentState(), $integerEdit.getState());
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
      Integer goal = 1;
      $integerEdit.setGoal(goal);
      $integerEdit.perform();
      $integerEdit.undo();
      assertEquals($integerEdit.getState(), State.UNDONE);
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
      $integerEdit.addValidityListener($listener1);
      $integerEdit.addValidityListener($listener2);
      assertTrue($integerEdit.isValidityListener($listener1));
      assertTrue($integerEdit.isValidityListener($listener2));
      // perform
      Integer goal = 1;
      $integerEdit.setGoal(goal);
      $integerEdit.perform();
      assertFalse($integerEdit.isValidityListener($listener1));
      assertFalse($integerEdit.isValidityListener($listener2));
      $integerEdit.undo();
      // there are no listeners
      assertFalse($integerEdit.isValidityListener($listener1));
      assertFalse($integerEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getSource(), $target);
      assertEquals($listener3.$event.getOldInteger(), goal);
      assertEquals($listener3.$event.getNewInteger(), null);
      assertEquals($listener3.$event.getEdit(), $integerEdit);
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
    IntegerEdit edit1 = null;
    try {
      // edit1
      edit1 = new IntegerEdit($target);
      Integer goal1 = 1;
      edit1.setGoal(goal1);
      edit1.perform();
      // edit2
      IntegerEdit edit2 = new IntegerEdit($target);
      Integer goal2 = 2;
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
      IntegerEdit edit1 = new IntegerEdit($target);
      Integer goal1 = 1;
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Integer goal2 = 2;
      $integerEdit.setGoal(goal2);
      $integerEdit.perform();
      $integerEdit.undo();
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
      $integerEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getCurrentState(), $integerEdit.getState());
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
      $integerEdit.perform();
      $integerEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getCurrentState(), $integerEdit.getState());
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
      $integerEdit.kill();
      $integerEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $integerEdit);
      assertEquals(e.getCurrentState(), $integerEdit.getState());
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
      $integerEdit.perform();
      $integerEdit.undo();
      $integerEdit.redo();
      assertEquals($integerEdit.getState(), State.DONE);
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
      $integerEdit.addValidityListener($listener1);
      $integerEdit.addValidityListener($listener2);
      assertTrue($integerEdit.isValidityListener($listener1));
      assertTrue($integerEdit.isValidityListener($listener2));
      // perform
      Integer goal = 1;
      $integerEdit.setGoal(goal);
      $integerEdit.perform();
      assertFalse($integerEdit.isValidityListener($listener1));
      assertFalse($integerEdit.isValidityListener($listener2));
      $integerEdit.undo();
      $integerEdit.redo();
      // there are no listeners
      assertFalse($integerEdit.isValidityListener($listener1));
      assertFalse($integerEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $integerEdit);
      assertEquals($listener3.$event.getOldInteger(), null);
      assertEquals($listener3.$event.getNewInteger(), goal);
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
    IntegerEdit edit1 = null;
    try {
      // edit1
      edit1 = new IntegerEdit($target);
      Integer goal1 = 1;
      edit1.setGoal(goal1);
      edit1.perform();
      edit1.undo();
      // edit2
      IntegerEdit edit2 = new IntegerEdit($target);
      Integer goal2 = 2;
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
      IntegerEdit edit1 = new IntegerEdit($target);
      Integer goal1 = 1;
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Integer goal2 = 2;
      $integerEdit.setGoal(goal2);
      $integerEdit.perform();
      $integerEdit.undo();
      $integerEdit.redo();
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
    assertEquals(State.NOT_YET_PERFORMED, $integerEdit.getState());
//    IntegerEvent createdEvent = $integerEdit.createEvent();
//    assertEquals(createdEvent.getEdit(), $integerEdit);
//    assertEquals(createdEvent.getOldValue(), null);
//    assertEquals(createdEvent.getNewValue(), null);
//    assertEquals(createdEvent.getSource(), $target);
    // perform
    Integer goal = 1;
    $integerEdit.setGoal(goal);
    $integerEdit.perform();
    // create event
    IntegerEvent createdEvent = $integerEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $integerEdit);
    assertEquals(createdEvent.getOldInteger(), null);
    assertEquals(createdEvent.getNewInteger(), goal);
    assertEquals(createdEvent.getSource(), $target);
    // undo
    $integerEdit.undo();
    // create event
    createdEvent = $integerEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $integerEdit);
    assertEquals(createdEvent.getOldInteger(), goal);
    assertEquals(createdEvent.getNewInteger(), null);
    assertEquals(createdEvent.getSource(), $target);
  }

}