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

package org.beedra_II.property.number.real.double64;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.edit.ValidityListener;
import org.beedra_II.edit.Edit.State;
import org.beedra_II.event.Listener;
import org.beedra_II.property.number.real.RealEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDoubleEdit {

  public class MyEditableDoubleBeed extends EditableDoubleBeed {

    public MyEditableDoubleBeed(AggregateBeed owner) {
      super(owner);
    }

    @Override
    public boolean isAcceptable(Double goal) {
      return goal != null && goal > 0;
    }

  }

  public class MyBeanBeed extends AbstractBeanBeed {
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

  public class StubDoubleListener implements Listener<RealEvent> {

    public void beedChanged(RealEvent event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public RealEvent $event;

  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  BeanBeed $beanBeed = new MyBeanBeed();
  MyEditableDoubleBeed $target = new MyEditableDoubleBeed($beanBeed);
  private DoubleEdit $doubleEdit = new DoubleEdit($target);
  StubValidityListener $listener1 = new StubValidityListener();
  StubValidityListener $listener2 = new StubValidityListener();
  StubDoubleListener $listener3 = new StubDoubleListener();

  @Test
  public void constructor() {
    assertEquals($doubleEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $doubleEdit.perform();
      $doubleEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getCurrentState(), $doubleEdit.getState());
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
      $doubleEdit.perform();
      $doubleEdit.undo();
      $doubleEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getCurrentState(), $doubleEdit.getState());
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
      $doubleEdit.kill();
      $doubleEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getCurrentState(), $doubleEdit.getState());
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
      $doubleEdit.perform();
      assertEquals($doubleEdit.getState(), State.DONE);
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
      $doubleEdit.addValidityListener($listener1);
      $doubleEdit.addValidityListener($listener2);
      assertTrue($doubleEdit.isValidityListener($listener1));
      assertTrue($doubleEdit.isValidityListener($listener2));
      // perform
      Double goal = 1.0;
      $doubleEdit.setGoal(goal);
      $doubleEdit.perform();
      // listeners should be removed
      assertFalse($doubleEdit.isValidityListener($listener1));
      assertFalse($doubleEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $doubleEdit);
      assertEquals($listener3.$event.getOldDouble(), null);
      assertEquals($listener3.$event.getNewDouble(), goal);
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
      $doubleEdit.addValidityListener($listener1);
      $doubleEdit.addValidityListener($listener2);
      assertTrue($doubleEdit.isValidityListener($listener1));
      assertTrue($doubleEdit.isValidityListener($listener2));
      // perform
      $doubleEdit.setGoal(null);
      $doubleEdit.perform();
      // listeners are removed
      assertFalse($doubleEdit.isValidityListener($listener1));
      assertFalse($doubleEdit.isValidityListener($listener2));
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
      DoubleEdit edit = new DoubleEdit($target);
      Double goal = 1.0;
      edit.setGoal(goal);
      edit.perform();
      // perform
      $doubleEdit.setGoal(null);
      $doubleEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      DoubleEdit edit1 = new DoubleEdit($target);
      assertNull(edit1.getInitial());
      Double goal1 = 1.0;
      edit1.setGoal(goal1);
      edit1.perform();
      assertNull(edit1.getInitial());
      // perform
      DoubleEdit edit2 = new DoubleEdit($target);
      assertNull(edit2.getInitial());
      Double goal2 = 2.0;
      edit2.setGoal(goal2);
      edit2.perform();
      assertEquals(edit2.getInitial(), goal1);
      // perform - no change
      DoubleEdit edit3 = new DoubleEdit($target);
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
      Double goal = 1.0;
      $doubleEdit.setGoal(goal);
      $doubleEdit.perform();
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
      $doubleEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getCurrentState(), $doubleEdit.getState());
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
      $doubleEdit.perform();
      $doubleEdit.undo();
      $doubleEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getCurrentState(), $doubleEdit.getState());
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
      $doubleEdit.kill();
      $doubleEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getCurrentState(), $doubleEdit.getState());
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
      Double goal = 1.0;
      $doubleEdit.setGoal(goal);
      $doubleEdit.perform();
      $doubleEdit.undo();
      assertEquals($doubleEdit.getState(), State.UNDONE);
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
      $doubleEdit.addValidityListener($listener1);
      $doubleEdit.addValidityListener($listener2);
      assertTrue($doubleEdit.isValidityListener($listener1));
      assertTrue($doubleEdit.isValidityListener($listener2));
      // perform
      Double goal = 1.0;
      $doubleEdit.setGoal(goal);
      $doubleEdit.perform();
      assertFalse($doubleEdit.isValidityListener($listener1));
      assertFalse($doubleEdit.isValidityListener($listener2));
      $doubleEdit.undo();
      // there are no listeners
      assertFalse($doubleEdit.isValidityListener($listener1));
      assertFalse($doubleEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getSource(), $target);
      assertEquals($listener3.$event.getOldDouble(), goal);
      assertEquals($listener3.$event.getNewDouble(), null);
      assertEquals($listener3.$event.getEdit(), $doubleEdit);
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
    DoubleEdit edit1 = null;
    try {
      // edit1
      edit1 = new DoubleEdit($target);
      Double goal1 = 1.0;
      edit1.setGoal(goal1);
      edit1.perform();
      // edit2
      DoubleEdit edit2 = new DoubleEdit($target);
      Double goal2 = 2.0;
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
      DoubleEdit edit1 = new DoubleEdit($target);
      Double goal1 = 1.0;
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Double goal2 = 2.0;
      $doubleEdit.setGoal(goal2);
      $doubleEdit.perform();
      $doubleEdit.undo();
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
      $doubleEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getCurrentState(), $doubleEdit.getState());
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
      $doubleEdit.perform();
      $doubleEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getCurrentState(), $doubleEdit.getState());
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
      $doubleEdit.kill();
      $doubleEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $doubleEdit);
      assertEquals(e.getCurrentState(), $doubleEdit.getState());
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
      $doubleEdit.perform();
      $doubleEdit.undo();
      $doubleEdit.redo();
      assertEquals($doubleEdit.getState(), State.DONE);
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
      $doubleEdit.addValidityListener($listener1);
      $doubleEdit.addValidityListener($listener2);
      assertTrue($doubleEdit.isValidityListener($listener1));
      assertTrue($doubleEdit.isValidityListener($listener2));
      // perform
      Double goal = 1.0;
      $doubleEdit.setGoal(goal);
      $doubleEdit.perform();
      assertFalse($doubleEdit.isValidityListener($listener1));
      assertFalse($doubleEdit.isValidityListener($listener2));
      $doubleEdit.undo();
      $doubleEdit.redo();
      // there are no listeners
      assertFalse($doubleEdit.isValidityListener($listener1));
      assertFalse($doubleEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $doubleEdit);
      assertEquals($listener3.$event.getOldDouble(), null);
      assertEquals($listener3.$event.getNewDouble(), goal);
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
    DoubleEdit edit1 = null;
    try {
      // edit1
      edit1 = new DoubleEdit($target);
      Double goal1 = 1.0;
      edit1.setGoal(goal1);
      edit1.perform();
      edit1.undo();
      // edit2
      DoubleEdit edit2 = new DoubleEdit($target);
      Double goal2 = 2.0;
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
      DoubleEdit edit1 = new DoubleEdit($target);
      Double goal1 = 1.0;
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Double goal2 = 2.0;
      $doubleEdit.setGoal(goal2);
      $doubleEdit.perform();
      $doubleEdit.undo();
      $doubleEdit.redo();
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
    assertEquals(State.NOT_YET_PERFORMED, $doubleEdit.getState());
//    RealEvent createdEvent = $doubleEdit.createEvent();
//    assertEquals(createdEvent.getEdit(), $doubleEdit);
//    assertEquals(createdEvent.getOldValue(), null);
//    assertEquals(createdEvent.getNewValue(), null);
//    assertEquals(createdEvent.getSource(), $target);
    // perform
    Double goal = 1.0;
    $doubleEdit.setGoal(goal);
    $doubleEdit.perform();
    // create event
    RealEvent createdEvent = $doubleEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $doubleEdit);
    assertEquals(createdEvent.getOldDouble(), null);
    assertEquals(createdEvent.getNewDouble(), goal);
    assertEquals(createdEvent.getSource(), $target);
    // undo
    $doubleEdit.undo();
    // create event
    createdEvent = $doubleEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $doubleEdit);
    assertEquals(createdEvent.getOldDouble(), goal);
    assertEquals(createdEvent.getNewDouble(), null);
    assertEquals(createdEvent.getSource(), $target);
  }

}