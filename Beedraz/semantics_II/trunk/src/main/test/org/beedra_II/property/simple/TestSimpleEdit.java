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
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.edit.ValidityListener;
import org.beedra_II.edit.Edit.State;
import org.beedra_II.event.Listener;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerBeed;
import org.beedra_II.property.integer.IntegerEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSimpleEdit {

  public class MySimpleEdit
       extends SimpleEdit<Integer, MyEditableIntegerBeed, IntegerEvent> {

    public MySimpleEdit(MyEditableIntegerBeed target) {
      super(target);
    }

    /*
     * Made public for testing reasons.
     */
    public void checkValidityPublic() {
      super.checkValidity();
    }

    /*
     * Made public for testing reasons.
     */
    public void notifyListenersPublic() {
      super.notifyListeners();
    }

    @Override
    protected IntegerEvent createEvent() {
      IntegerBeed source = new EditableIntegerBeed(new MyBeanBeed());
      $createdEvent = new IntegerEvent(source, new Integer(0), new Integer(1), null);
      return $createdEvent;
    }

    public IntegerEvent getCreatedEvent() {
      return $createdEvent;
    }

    private IntegerEvent $createdEvent;

  }

  public class MyEditableIntegerBeed extends EditableIntegerBeed {

    public MyEditableIntegerBeed(AggregateBeed owner) {
      super(owner);
    }

    @Override
    public boolean isAcceptable(Integer goal) {
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

  public class StubIntegerListener implements Listener<IntegerEvent> {

    public void beedChanged(IntegerEvent event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public IntegerEvent $event;

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
  MyEditableIntegerBeed $target = new MyEditableIntegerBeed($beanBeed);
  private MySimpleEdit $simpleEdit = new MySimpleEdit($target);
  StubValidityListener $listener1 = new StubValidityListener();
  StubValidityListener $listener2 = new StubValidityListener();
  StubIntegerListener $listener3 = new StubIntegerListener();

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
      $simpleEdit.setGoal(new Integer(1));
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
  // correct begin-state, edit is no change, so validity listeners are not removed, listeners of the beed are not notified
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
      // listeners are not removed
      assertTrue($simpleEdit.isValidityListener($listener1));
      assertTrue($simpleEdit.isValidityListener($listener2));
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
      $simpleEdit.setGoal(-1);
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
      Integer goal1 = 5;
      edit1.setGoal(goal1);
      edit1.perform();
      assertNull(edit1.getInitial());
      // perform
      MySimpleEdit edit2 = new MySimpleEdit($target);
      assertNull(edit2.getInitial());
      Integer goal2 = 7;
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
      Integer goal = 5;
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
      $simpleEdit.setGoal(5);
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
      $simpleEdit.setGoal(5);
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
      Integer goal1 = 5;
      edit1.setGoal(goal1);
      edit1.perform();
      // edit2
      MySimpleEdit edit2 = new MySimpleEdit($target);
      Integer goal2 = 7;
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
      Integer goal1 = 5;
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Integer goal2 = 7;
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
      $simpleEdit.setGoal(5);
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
      Integer goal1 = 5;
      edit1.setGoal(goal1);
      edit1.perform();
      edit1.undo();
      // edit2
      MySimpleEdit edit2 = new MySimpleEdit($target);
      Integer goal2 = 7;
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
      Integer goal1 = 5;
      edit1.setGoal(goal1);
      edit1.perform();
      // $simpleEdit
      Integer goal2 = 7;
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

  @Test
  public void checkValidity() throws EditStateException {
    // add validity listeners
    $simpleEdit.addValidityListener($listener1);
    $simpleEdit.addValidityListener($listener2);
    assertTrue($simpleEdit.isValidityListener($listener1));
    assertTrue($simpleEdit.isValidityListener($listener2));
    assertTrue($listener1.isEmpty());
    assertTrue($listener2.isEmpty());
    // check the value of the validity
    assertTrue($simpleEdit.isValid());
    // change validity
    $simpleEdit.setGoal(5);
    $simpleEdit.checkValidityPublic();
    // validity is still the same, so validity listeners are not notified
    assertTrue($simpleEdit.isValid());
    assertTrue($simpleEdit.isValidityListener($listener1));
    assertTrue($simpleEdit.isValidityListener($listener2));
    assertTrue($listener1.isEmpty());
    assertTrue($listener2.isEmpty());
    // change validity
    $simpleEdit.setGoal(-1);
    $simpleEdit.checkValidityPublic();
    // validity has changed, so validity listeners are notified
    assertFalse($simpleEdit.isValid());
    assertTrue($simpleEdit.isValidityListener($listener1));
    assertTrue($simpleEdit.isValidityListener($listener2));
    assertEquals($listener1.$target, $simpleEdit);
    assertEquals($listener1.$validity, $simpleEdit.isValid());
    assertEquals($listener2.$target, $simpleEdit);
    assertEquals($listener2.$validity, $simpleEdit.isValid());
    // change validity again
    $listener1.reset();
    $listener2.reset();
    $simpleEdit.setGoal(2);
    $simpleEdit.checkValidityPublic();
    // validity has changed, so validity listeners are notified
    assertTrue($simpleEdit.isValid());
    assertTrue($simpleEdit.isValidityListener($listener1));
    assertTrue($simpleEdit.isValidityListener($listener2));
    assertEquals($listener1.$target, $simpleEdit);
    assertEquals($listener1.$validity, $simpleEdit.isValid());
    assertEquals($listener2.$target, $simpleEdit);
    assertEquals($listener2.$validity, $simpleEdit.isValid());
  }

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
      $simpleEdit.setGoal(1);
      $simpleEdit.perform();
      $simpleEdit.setGoal(2);
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
      $simpleEdit.setGoal(1);
      $simpleEdit.perform();
      $simpleEdit.undo();
      $simpleEdit.setGoal(2);
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
      $simpleEdit.setGoal(1);
      $simpleEdit.perform();
      $simpleEdit.undo();
      $simpleEdit.redo();
      $simpleEdit.setGoal(2);
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
      Integer goal = 1;
      $simpleEdit.setGoal(goal);
      assertEquals($simpleEdit.getGoal(), goal);
      assertTrue($simpleEdit.isValid());
      assertTrue($listener1.isEmpty());
      assertTrue($listener2.isEmpty());
      // set invalid goal
      Integer invalidGoal = -1;
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
      Integer invalidGoal2 = 0;
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
      Integer goal2 = 3;
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
}