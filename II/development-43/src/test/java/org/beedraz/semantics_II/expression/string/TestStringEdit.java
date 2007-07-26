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

package org.beedraz.semantics_II.expression.string;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Map;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.StubValidityListener;
import org.beedraz.semantics_II.Edit.State;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestStringEdit {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  BeanBeed $beanBeed = new StubBeanBeed();
  StubEditableStringBeed $target = new StubEditableStringBeed($beanBeed);
  private StringEdit $stringEdit = new StringEdit($target);
  StubValidityListener $listener1 = new StubValidityListener();
  StubValidityListener $listener2 = new StubValidityListener();
  StubListener<StringEvent> $listener3 = new StubListener<StringEvent>();

  @Test
  public void constructor() {
    assertEquals($stringEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $stringEdit.perform();
      $stringEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getCurrentState(), $stringEdit.getState());
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
      $stringEdit.perform();
      $stringEdit.undo();
      $stringEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getCurrentState(), $stringEdit.getState());
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
      $stringEdit.kill();
      $stringEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getCurrentState(), $stringEdit.getState());
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
      $stringEdit.perform();
      assertEquals($stringEdit.getState(), State.DONE);
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
      $stringEdit.addValidityListener($listener1);
      $stringEdit.addValidityListener($listener2);
      assertTrue($stringEdit.isValidityListener($listener1));
      assertTrue($stringEdit.isValidityListener($listener2));
      // perform
      String goal = "row";
      $stringEdit.setGoal(goal);
      $stringEdit.perform();
      // listeners should be removed
      assertFalse($stringEdit.isValidityListener($listener1));
      assertFalse($stringEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $stringEdit);
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
      $stringEdit.addValidityListener($listener1);
      $stringEdit.addValidityListener($listener2);
      assertTrue($stringEdit.isValidityListener($listener1));
      assertTrue($stringEdit.isValidityListener($listener2));
      // perform
      $stringEdit.setGoal(null);
      $stringEdit.perform();
      // listeners are removed
      assertFalse($stringEdit.isValidityListener($listener1));
      assertFalse($stringEdit.isValidityListener($listener2));
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
      StringEdit edit = new StringEdit($target);
      String goal = "row";
      edit.setGoal(goal);
      edit.perform();
      // perform
      $stringEdit.setGoal(null);
      $stringEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      StringEdit edit1 = new StringEdit($target);
      assertNull(edit1.getInitial());
      String goal1 = "row";
      edit1.setGoal(goal1);
      edit1.perform();
      assertNull(edit1.getInitial());
      // perform
      StringEdit edit2 = new StringEdit($target);
      assertNull(edit2.getInitial());
      String goal2 = "reason";
      edit2.setGoal(goal2);
      edit2.perform();
      assertEquals(edit2.getInitial(), goal1);
      // perform - no change
      StringEdit edit3 = new StringEdit($target);
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
      String goal = "row";
      $stringEdit.setGoal(goal);
      $stringEdit.perform();
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
      $stringEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getCurrentState(), $stringEdit.getState());
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
      $stringEdit.perform();
      $stringEdit.undo();
      $stringEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getCurrentState(), $stringEdit.getState());
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
      $stringEdit.kill();
      $stringEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getCurrentState(), $stringEdit.getState());
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
      String goal = "row";
      $stringEdit.setGoal(goal);
      $stringEdit.perform();
      $stringEdit.undo();
      assertEquals($stringEdit.getState(), State.UNDONE);
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
      $stringEdit.addValidityListener($listener1);
      $stringEdit.addValidityListener($listener2);
      assertTrue($stringEdit.isValidityListener($listener1));
      assertTrue($stringEdit.isValidityListener($listener2));
      // perform
      String goal = "row";
      $stringEdit.setGoal(goal);
      $stringEdit.perform();
      assertFalse($stringEdit.isValidityListener($listener1));
      assertFalse($stringEdit.isValidityListener($listener2));
      $stringEdit.undo();
      // there are no listeners
      assertFalse($stringEdit.isValidityListener($listener1));
      assertFalse($stringEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getSource(), $target);
      assertEquals($listener3.$event.getOldValue(), goal);
      assertEquals($listener3.$event.getNewValue(), null);
      assertEquals($listener3.$event.getEdit(), $stringEdit);
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
    StringEdit edit1 = null;
    try {
      // edit1
      edit1 = new StringEdit($target);
      String goal1 = "row";
      edit1.setGoal(goal1);
      edit1.perform();
      // edit2
      StringEdit edit2 = new StringEdit($target);
      String goal2 = "reason";
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
      StringEdit edit1 = new StringEdit($target);
      String goal1 = "row";
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      String goal2 = "reason";
      $stringEdit.setGoal(goal2);
      $stringEdit.perform();
      $stringEdit.undo();
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
      $stringEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getCurrentState(), $stringEdit.getState());
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
      $stringEdit.perform();
      $stringEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getCurrentState(), $stringEdit.getState());
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
      $stringEdit.kill();
      $stringEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $stringEdit);
      assertEquals(e.getCurrentState(), $stringEdit.getState());
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
      $stringEdit.perform();
      $stringEdit.undo();
      $stringEdit.redo();
      assertEquals($stringEdit.getState(), State.DONE);
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
      $stringEdit.addValidityListener($listener1);
      $stringEdit.addValidityListener($listener2);
      assertTrue($stringEdit.isValidityListener($listener1));
      assertTrue($stringEdit.isValidityListener($listener2));
      // perform
      String goal = "row";
      $stringEdit.setGoal(goal);
      $stringEdit.perform();
      assertFalse($stringEdit.isValidityListener($listener1));
      assertFalse($stringEdit.isValidityListener($listener2));
      $stringEdit.undo();
      $stringEdit.redo();
      // there are no listeners
      assertFalse($stringEdit.isValidityListener($listener1));
      assertFalse($stringEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $stringEdit);
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
    StringEdit edit1 = null;
    try {
      // edit1
      edit1 = new StringEdit($target);
      String goal1 = "row";
      edit1.setGoal(goal1);
      edit1.perform();
      edit1.undo();
      // edit2
      StringEdit edit2 = new StringEdit($target);
      String goal2 = "reason";
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
      StringEdit edit1 = new StringEdit($target);
      String goal1 = "row";
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      String goal2 = "reason";
      $stringEdit.setGoal(goal2);
      $stringEdit.perform();
      $stringEdit.undo();
      $stringEdit.redo();
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
  public void createEvents() throws EditStateException, IllegalEditException {
    // can't create event before perform
    assertEquals(State.NOT_YET_PERFORMED, $stringEdit.getState());
//    StringEvent createdEvent = $stringEdit.createEvent();
//    assertEquals(createdEvent.getEdit(), $stringEdit);
//    assertEquals(createdEvent.getOldValue(), null);
//    assertEquals(createdEvent.getNewValue(), null);
//    assertEquals(createdEvent.getSource(), $target);
    // perform
    String goal = "row";
    $stringEdit.setGoal(goal);
    $stringEdit.perform();
    // create event
    Map<AbstractBeed<?>, StringEvent> events = $stringEdit.createEvents();
    StringEvent createdEvent = events.get($stringEdit.getTarget());
    assertEquals(createdEvent.getEdit(), $stringEdit);
    assertEquals(createdEvent.getOldValue(), null);
    assertEquals(createdEvent.getNewValue(), goal);
    assertEquals(createdEvent.getSource(), $target);
    // undo
    $stringEdit.undo();
    // create event
    events = $stringEdit.createEvents();
    createdEvent = events.get($stringEdit.getTarget());
    assertEquals(createdEvent.getEdit(), $stringEdit);
    assertEquals(createdEvent.getOldValue(), goal);
    assertEquals(createdEvent.getNewValue(), null);
    assertEquals(createdEvent.getSource(), $target);
  }

}