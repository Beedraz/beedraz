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

package org.beedraz.semantics_II;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Edit.State;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.expression.StubEditableSimpleExpressionBeed;
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
public class TestAbstractSimpleEdit {



  @Before
  public void setUp() throws Exception {
    $beanBeed = new StubBeanBeed();
    $target = new StubEditableSimpleExpressionBeed($beanBeed);
    $targetListener = new StubListener<StubEvent>();
    $target.addListener($targetListener);
    $edit = new StubSimpleEdit($target);
    $validityListener1 = new StubValidityListener();
    $validityListener2 = new StubValidityListener();
  }

  @After
  public void tearDown() throws Exception {
    $beanBeed = null;
    $target = null;
    $edit = null;
    $validityListener1 = null;
    $validityListener2 = null;
    $targetListener = null;
  }

  private BeanBeed $beanBeed = new StubBeanBeed();
  private StubEditableSimpleExpressionBeed $target = new StubEditableSimpleExpressionBeed($beanBeed);
  private StubSimpleEdit $edit = new StubSimpleEdit($target);
  private StubValidityListener $validityListener1 = new StubValidityListener();
  private StubValidityListener $validityListener2 = new StubValidityListener();
  private StubListener<StubEvent> $targetListener = new StubListener<StubEvent>();

  @Test
  public void constructor() {
    assertEquals($edit.getTarget(), $target);
  }

  @Test
  public void kill() {
    // add validity listeners
    $edit.addValidityListener($validityListener1);
    $edit.addValidityListener($validityListener2);
    assertTrue($edit.isValidityListener($validityListener1));
    assertTrue($edit.isValidityListener($validityListener2));
    // kill
    $edit.kill();
    // check
    assertEquals($edit.getState(), State.DEAD);
    assertFalse($edit.isValidityListener($validityListener1));
    assertFalse($edit.isValidityListener($validityListener2));
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
      $edit.addValidityListener($validityListener1);
      $edit.addValidityListener($validityListener2);
      assertTrue($edit.isValidityListener($validityListener1));
      assertTrue($edit.isValidityListener($validityListener2));
      // perform
      assertFalse($edit.isInitialStateStored());
      $edit.setChange(true);
      $edit.perform();
      assertTrue($edit.isInitialStateStored());
      // listeners should be removed
      assertFalse($edit.isValidityListener($validityListener1));
      assertFalse($edit.isValidityListener($validityListener2));
      // listeners of the beed are notified
      assertNotNull($targetListener.$event);
      assertEquals($targetListener.$event, $edit.getCreatedEvent());
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
      $edit.addValidityListener($validityListener1);
      $edit.addValidityListener($validityListener2);
      assertTrue($edit.isValidityListener($validityListener1));
      assertTrue($edit.isValidityListener($validityListener2));
      // perform
      assertFalse($edit.isInitialStateStored());
      $edit.setChange(false);
      $edit.perform();
      assertTrue($edit.isInitialStateStored());
      // listeners are removed
      assertFalse($edit.isValidityListener($validityListener1));
      assertFalse($edit.isValidityListener($validityListener2));
      // listeners of the beed are not notified
      // listeners of the beed are notified
      assertNull($targetListener.$event);
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
      $edit.addValidityListener($validityListener1);
      $edit.addValidityListener($validityListener2);
      assertTrue($edit.isValidityListener($validityListener1));
      assertTrue($edit.isValidityListener($validityListener2));
      // perform
      $edit.perform();
      assertFalse($edit.isValidityListener($validityListener1));
      assertFalse($edit.isValidityListener($validityListener2));
      $targetListener.reset();
      $edit.undo();
      // there are no listeners
      assertFalse($edit.isValidityListener($validityListener1));
      assertFalse($edit.isValidityListener($validityListener2));
      // listeners of the beed are notified
      assertNotNull($targetListener.$event);
      assertEquals($targetListener.$event, $edit.getCreatedEvent());
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
      $edit.addValidityListener($validityListener1);
      $edit.addValidityListener($validityListener2);
      assertTrue($edit.isValidityListener($validityListener1));
      assertTrue($edit.isValidityListener($validityListener2));
      // perform
      $edit.perform();
      assertFalse($edit.isValidityListener($validityListener1));
      assertFalse($edit.isValidityListener($validityListener2));
      $edit.undo();
      $targetListener.reset();
      $edit.redo();
      // there are no listeners
      assertFalse($edit.isValidityListener($validityListener1));
      assertFalse($edit.isValidityListener($validityListener2));
      // listeners of the beed are notified
      assertNotNull($targetListener.$event);
      assertEquals($targetListener.$event, $edit.getCreatedEvent());
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
    $edit.addValidityListener($validityListener1);
    $edit.addValidityListener($validityListener2);
    assertTrue($edit.isValidityListener($validityListener1));
    assertTrue($edit.isValidityListener($validityListener2));
    assertTrue($validityListener1.isEmpty());
    assertTrue($validityListener2.isEmpty());
    // check the value of the validity
    assertTrue($edit.isValid());
    // change validity
    $edit.setAcceptable(true);
    $edit.checkValidity();
    // validity is still the same, so validity listeners are not notified
    assertTrue($edit.isValid());
    assertTrue($edit.isValidityListener($validityListener1));
    assertTrue($edit.isValidityListener($validityListener2));
    assertTrue($validityListener1.isEmpty());
    assertTrue($validityListener2.isEmpty());
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
    assertTrue($edit.isValidityListener($validityListener1));
    assertTrue($edit.isValidityListener($validityListener2));
    assertEquals($validityListener1.$target, $edit);
    assertEquals($validityListener1.$validity, $edit.isValid());
    assertEquals($validityListener2.$target, $edit);
    assertEquals($validityListener2.$validity, $edit.isValid());
    // change validity again
    $validityListener1.reset();
    $validityListener2.reset();
    $edit.setAcceptable(true);
    $edit.checkValidity();
    // validity has changed, so validity listeners are notified
    assertTrue($edit.isValid());
    assertTrue($edit.isValidityListener($validityListener1));
    assertTrue($edit.isValidityListener($validityListener2));
    assertEquals($validityListener1.$target, $edit);
    assertEquals($validityListener1.$validity, $edit.isValid());
    assertEquals($validityListener2.$target, $edit);
    assertEquals($validityListener2.$validity, $edit.isValid());
  }

  @Test
  public void addValidityListener1() {
    // the set of listeners is effective
    $edit.addValidityListener($validityListener1);
    assertTrue($edit.isValidityListener($validityListener1));
  }

  @Test
  public void addValidityListener2() {
    // the set of listeners is not effective
    $edit.kill();
    $edit.addValidityListener($validityListener1);
    assertFalse($edit.isValidityListener($validityListener1));
  }

  @Test
  public void addValidityListener3() throws EditStateException, IllegalEditException {
    // the set of listeners is not effective
    $edit.perform();
    $edit.addValidityListener($validityListener1);
    assertFalse($edit.isValidityListener($validityListener1));
  }

  @Test
  public void removeValidityListener1() {
    $edit.addValidityListener($validityListener1);
    $edit.addValidityListener($validityListener2);
    assertTrue($edit.isValidityListener($validityListener1));
    assertTrue($edit.isValidityListener($validityListener2));
    // the set of listeners is effective
    $edit.removeValidityListener($validityListener1);
    assertFalse($edit.isValidityListener($validityListener1));
    assertTrue($edit.isValidityListener($validityListener2));
  }

  @Test
  public void removeValidityListener2() {
    $edit.addValidityListener($validityListener1);
    $edit.addValidityListener($validityListener2);
    assertTrue($edit.isValidityListener($validityListener1));
    assertTrue($edit.isValidityListener($validityListener2));
    // the set of listeners is not effective
    $edit.kill();
    $edit.removeValidityListener($validityListener1);
    assertFalse($edit.isValidityListener($validityListener1));
    assertFalse($edit.isValidityListener($validityListener2));
  }

  @Test
  public void removeValidityListener3() throws EditStateException, IllegalEditException {
    $edit.addValidityListener($validityListener1);
    $edit.addValidityListener($validityListener2);
    assertTrue($edit.isValidityListener($validityListener1));
    assertTrue($edit.isValidityListener($validityListener2));
    // the set of listeners is not effective
    $edit.perform();
    $edit.removeValidityListener($validityListener1);
    assertFalse($edit.isValidityListener($validityListener1));
    assertFalse($edit.isValidityListener($validityListener2));
  }

// method is no longer public or protected
//  @Test
//  public void fireValidityChange() {
//    // register listeners
//    $edit.addValidityListener($validityListener1);
//    $edit.addValidityListener($validityListener2);
//    assertTrue($edit.isValidityListener($validityListener1));
//    assertTrue($edit.isValidityListener($validityListener2));
//    assertTrue($validityListener1.isEmpty());
//    assertTrue($validityListener2.isEmpty());
//    // fire
//    $edit.fireValidityChange();
//    // check
//    assertTrue($edit.isValidityListener($validityListener1));
//    assertTrue($edit.isValidityListener($validityListener2));
//    assertEquals($validityListener1.$target, $edit);
//    assertEquals($validityListener1.$validity, $edit.isValid());
//    assertEquals($validityListener2.$target, $edit);
//    assertEquals($validityListener2.$validity, $edit.isValid());
//  }

  @Test
  public void notifyListeners() {
    $edit.createEvents();
    assertNull($targetListener.$event);
    // notify
    $edit.publicTopologicalUpdateStart();
    // check
    assertNotNull($targetListener.$event);
    assertEquals($targetListener.$event, $edit.getCreatedEvent());
  }

}