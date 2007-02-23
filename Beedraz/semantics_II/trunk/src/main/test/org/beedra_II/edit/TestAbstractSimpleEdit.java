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

package org.beedra_II.edit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit.State;
import org.beedra_II.event.Event;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerBeed;
import org.beedra_II.property.integer.IntegerEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestAbstractSimpleEdit {

  public class MyAbstractEdit
       extends AbstractSimpleEdit<EditableIntegerBeed, IntegerEvent> {

    public MyAbstractEdit(EditableIntegerBeed target) {
      super(target);
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

    @Override
    protected void fireEvent(Event<?> event) {
      $firedEvent = (IntegerEvent)event;
    }

    public IntegerEvent $firedEvent;

    @Override
    public boolean isChange() {
      return $b;
    }

    public void setChange(boolean b) {
      $b = b;
    }

    private boolean $b = true;

    @Override
    protected boolean isGoalStateCurrent() {
      return $goalStateCurrent;
    }

    public void setGoalStateCurrent(boolean b) {
      $goalStateCurrent = b;
    }

    private boolean $goalStateCurrent = true;

    @Override
    protected boolean isInitialStateCurrent() {
      return $initialStateCurrent;
    }

    public void setInitialStateCurrent(boolean b) {
      $initialStateCurrent = b;
    }

    private boolean $initialStateCurrent = true;

    @Override
    protected void performance() {
      // NOP
    }

    @Override
    protected void storeInitialState() {
      $initialStateStored = true;
    }

    public boolean isInitialStateStored() {
      return $initialStateStored;
    }

    private boolean $initialStateStored = false;

    @Override
    protected void unperformance() {
      // NOP
    }

    @Override
    protected boolean isAcceptable() {
      return $acceptable;
    }

    public void setAcceptable(boolean b) {
      $acceptable = b;
      recalculateValidity();
    }

    private boolean $acceptable = true;

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

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  BeanBeed $beanBeed = new MyBeanBeed();
  EditableIntegerBeed $target = new EditableIntegerBeed($beanBeed);
  private MyAbstractEdit $edit =
      new MyAbstractEdit($target);
  StubValidityListener $listener1 = new StubValidityListener();
  StubValidityListener $listener2 = new StubValidityListener();

  @Test
  public void constructor() {
    assertEquals($edit.getTarget(), $target);
  }

  @Test
  public void kill() {
    // add validity listeners
    $edit.addValidityListener($listener1);
    $edit.addValidityListener($listener2);
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    // kill
    $edit.kill();
    // check
    assertEquals($edit.getState(), State.DEAD);
    assertFalse($edit.isValidityListener($listener1));
    assertFalse($edit.isValidityListener($listener2));
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $edit.perform();
      $edit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getCurrentState(), $edit.getState());
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
      $edit.perform();
      $edit.undo();
      $edit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getCurrentState(), $edit.getState());
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
      $edit.kill();
      $edit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getCurrentState(), $edit.getState());
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
      $edit.perform();
      assertEquals($edit.getState(), State.DONE);
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
      $edit.addValidityListener($listener1);
      $edit.addValidityListener($listener2);
      assertTrue($edit.isValidityListener($listener1));
      assertTrue($edit.isValidityListener($listener2));
      // perform
      assertFalse($edit.isInitialStateStored());
      $edit.setChange(true);
      $edit.perform();
      assertTrue($edit.isInitialStateStored());
      // listeners should be removed
      assertFalse($edit.isValidityListener($listener1));
      assertFalse($edit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($edit.$firedEvent);
      assertEquals($edit.$firedEvent, $edit.getCreatedEvent());
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
      $edit.addValidityListener($listener1);
      $edit.addValidityListener($listener2);
      assertTrue($edit.isValidityListener($listener1));
      assertTrue($edit.isValidityListener($listener2));
      // perform
      assertFalse($edit.isInitialStateStored());
      $edit.setChange(false);
      $edit.perform();
      assertTrue($edit.isInitialStateStored());
      // listeners are removed
      assertFalse($edit.isValidityListener($listener1));
      assertFalse($edit.isValidityListener($listener2));
      // listeners of the beed are not notified
      assertNull($edit.$firedEvent);
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
      $edit.setAcceptable(false);
      $edit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // incorrect begin-state
  public void undo1() {
    try {
      $edit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getCurrentState(), $edit.getState());
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
      $edit.perform();
      $edit.undo();
      $edit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getCurrentState(), $edit.getState());
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
      $edit.kill();
      $edit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getCurrentState(), $edit.getState());
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
      $edit.perform();
      $edit.undo();
      assertEquals($edit.getState(), State.UNDONE);
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
      $edit.addValidityListener($listener1);
      $edit.addValidityListener($listener2);
      assertTrue($edit.isValidityListener($listener1));
      assertTrue($edit.isValidityListener($listener2));
      // perform
      $edit.perform();
      assertFalse($edit.isValidityListener($listener1));
      assertFalse($edit.isValidityListener($listener2));
      $edit.undo();
      // there are no listeners
      assertFalse($edit.isValidityListener($listener1));
      assertFalse($edit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($edit.$firedEvent);
      assertEquals($edit.$firedEvent, $edit.getCreatedEvent());
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
    try {
      $edit.setGoalStateCurrent(false);
      $edit.perform();
      $edit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getMessage(), null);
    }
  }


  @Test
  // incorrect begin-state
  public void redo1() {
    try {
      $edit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getCurrentState(), $edit.getState());
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
      $edit.perform();
      $edit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getCurrentState(), $edit.getState());
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
      $edit.kill();
      $edit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getCurrentState(), $edit.getState());
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
      $edit.perform();
      $edit.undo();
      $edit.redo();
      assertEquals($edit.getState(), State.DONE);
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
      $edit.addValidityListener($listener1);
      $edit.addValidityListener($listener2);
      assertTrue($edit.isValidityListener($listener1));
      assertTrue($edit.isValidityListener($listener2));
      // perform
      $edit.perform();
      assertFalse($edit.isValidityListener($listener1));
      assertFalse($edit.isValidityListener($listener2));
      $edit.undo();
      $edit.redo();
      // there are no listeners
      assertFalse($edit.isValidityListener($listener1));
      assertFalse($edit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($edit.$firedEvent);
      assertEquals($edit.$firedEvent, $edit.getCreatedEvent());
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
    try {
      $edit.setInitialStateCurrent(false);
      $edit.perform();
      $edit.undo();
      $edit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $edit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  public void checkValidity() throws IllegalEditException {
    // add validity listeners
    $edit.addValidityListener($listener1);
    $edit.addValidityListener($listener2);
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    assertTrue($listener1.isEmpty());
    assertTrue($listener2.isEmpty());
    // check the value of the validity
    assertTrue($edit.isValid());
    // change validity
    $edit.setAcceptable(true);
    $edit.checkValidity();
    // validity is still the same, so validity listeners are not notified
    assertTrue($edit.isValid());
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    assertTrue($listener1.isEmpty());
    assertTrue($listener2.isEmpty());
    // change validity
    $edit.setAcceptable(false);
    try {
      $edit.checkValidity();
      fail();
    }
    catch (IllegalEditException ieExc) {
      // NOP
    }
    // validity has changed, so validity listeners are notified
    assertFalse($edit.isValid());
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    assertEquals($listener1.$target, $edit);
    assertEquals($listener1.$validity, $edit.isValid());
    assertEquals($listener2.$target, $edit);
    assertEquals($listener2.$validity, $edit.isValid());
    // change validity again
    $listener1.reset();
    $listener2.reset();
    $edit.setAcceptable(true);
    $edit.checkValidity();
    // validity has changed, so validity listeners are notified
    assertTrue($edit.isValid());
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    assertEquals($listener1.$target, $edit);
    assertEquals($listener1.$validity, $edit.isValid());
    assertEquals($listener2.$target, $edit);
    assertEquals($listener2.$validity, $edit.isValid());
  }

  @Test
  public void addValidityListener1() {
    // the set of listeners is effective
    $edit.addValidityListener($listener1);
    assertTrue($edit.isValidityListener($listener1));
  }

  @Test
  public void addValidityListener2() {
    // the set of listeners is not effective
    $edit.kill();
    $edit.addValidityListener($listener1);
    assertFalse($edit.isValidityListener($listener1));
  }

  @Test
  public void addValidityListener3() throws EditStateException, IllegalEditException {
    // the set of listeners is not effective
    $edit.perform();
    $edit.addValidityListener($listener1);
    assertFalse($edit.isValidityListener($listener1));
  }

  @Test
  public void removeValidityListener1() {
    $edit.addValidityListener($listener1);
    $edit.addValidityListener($listener2);
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    // the set of listeners is effective
    $edit.removeValidityListener($listener1);
    assertFalse($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
  }

  @Test
  public void removeValidityListener2() {
    $edit.addValidityListener($listener1);
    $edit.addValidityListener($listener2);
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    // the set of listeners is not effective
    $edit.kill();
    $edit.removeValidityListener($listener1);
    assertFalse($edit.isValidityListener($listener1));
    assertFalse($edit.isValidityListener($listener2));
  }

  @Test
  public void removeValidityListener3() throws EditStateException, IllegalEditException {
    $edit.addValidityListener($listener1);
    $edit.addValidityListener($listener2);
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    // the set of listeners is not effective
    $edit.perform();
    $edit.removeValidityListener($listener1);
    assertFalse($edit.isValidityListener($listener1));
    assertFalse($edit.isValidityListener($listener2));
  }

  @Test
  public void fireValidityChange() {
    // register listeners
    $edit.addValidityListener($listener1);
    $edit.addValidityListener($listener2);
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    assertTrue($listener1.isEmpty());
    assertTrue($listener2.isEmpty());
    // fire
    $edit.fireValidityChange();
    // check
    assertTrue($edit.isValidityListener($listener1));
    assertTrue($edit.isValidityListener($listener2));
    assertEquals($listener1.$target, $edit);
    assertEquals($listener1.$validity, $edit.isValid());
    assertEquals($listener2.$target, $edit);
    assertEquals($listener2.$validity, $edit.isValid());
  }

  @Test
  public void notifyListeners() {
    assertNull($edit.$firedEvent);
    // notify
    $edit.notifyListeners();
    // check
    assertEquals($edit.$firedEvent, $edit.getCreatedEvent());
  }


}