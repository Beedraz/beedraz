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

package org.beedraz.semantics_II.expression.collection.set;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.Listener;
import org.beedraz.semantics_II.TopologicalUpdateAccess;
import org.beedraz.semantics_II.ValidityListener;
import org.beedraz.semantics_II.Edit.State;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
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
public class TestSetEdit {

  public class MySetEdit extends SetEdit<Integer> {

    public MySetEdit(EditableSetBeed<Integer> target) {
      super(target);
    }

    /**
     * Makes the updateDependents method public for testing reasons.
     */
    public void publicTopologicalUpdateStart(SetEvent<Integer> event) {
      TopologicalUpdateAccess.topologicalUpdate($target, event);
    }

    public void publicTopologicalUpdateStart() {
      TopologicalUpdateAccess.topologicalUpdate(createEvents(), this);
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

  public class StubSetListener implements Listener<SetEvent<Integer>> {

    public void beedChanged(SetEvent<Integer> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SetEvent<Integer> $event;

  }

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $target = new EditableSetBeed<Integer>($owner) {
      @Override
      public boolean isAcceptable(Set<Integer> elementsToAdd, Set<Integer> elementsToRemove) {
        // all integers in the set of added elements are positive
        for (Integer i : elementsToAdd) {
          if (i == null || i <= 0) {
            return false;
          }
        }
        return true;
      }

    };
    $setEdit = new MySetEdit($target);
    $listener1 = new StubValidityListener();
    $listener2 = new StubValidityListener();
    $listener3 = new StubSetListener();
  }

  @After
  public void tearDown() throws Exception {
    $owner = null;
  }

  private StubBeanBeed $owner;
  private EditableSetBeed<Integer> $target;
  private MySetEdit $setEdit;
  private StubValidityListener $listener1;
  private StubValidityListener $listener2;
  private StubSetListener $listener3;

  @Test
  public void constructor() {
    assertEquals($setEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $setEdit.perform();
      $setEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
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
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
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
      $setEdit.kill();
      $setEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
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
      $setEdit.perform();
      assertEquals($setEdit.getState(), State.DONE);
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
      $setEdit.addValidityListener($listener1);
      $setEdit.addValidityListener($listener2);
      assertTrue($setEdit.isValidityListener($listener1));
      assertTrue($setEdit.isValidityListener($listener2));
      // perform
      assertTrue($setEdit.getElementsToAdd().isEmpty());
      assertTrue($setEdit.getElementsToRemove().isEmpty());
      Integer toAdd = 3;
      $setEdit.addElementToAdd(toAdd);
      $setEdit.perform();
      assertTrue($setEdit.getElementsToAdd().size() == 1);
      assertTrue($setEdit.getElementsToAdd().contains(toAdd));
      assertTrue($setEdit.getElementsToRemove().isEmpty());
      // listeners should be removed
      assertFalse($setEdit.isValidityListener($listener1));
      assertFalse($setEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $setEdit);
      assertEquals($listener3.$event.getAddedElements(), $setEdit.getAddedElements());
      assertEquals($listener3.$event.getRemovedElements(), $setEdit.getRemovedElements());
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
      $setEdit.addValidityListener($listener1);
      $setEdit.addValidityListener($listener2);
      assertTrue($setEdit.isValidityListener($listener1));
      assertTrue($setEdit.isValidityListener($listener2));
      // perform
      assertTrue($setEdit.getElementsToAdd().isEmpty());
      assertTrue($setEdit.getElementsToRemove().isEmpty());
      $setEdit.perform();
      assertTrue($setEdit.getElementsToAdd().isEmpty());
      assertTrue($setEdit.getElementsToRemove().isEmpty());
      // listeners are removed
      assertFalse($setEdit.isValidityListener($listener1));
      assertFalse($setEdit.isValidityListener($listener2));
      // listeners of the beed are not notified
      assertNull($listener3.$event);
//    assertTrue("When the edit causes no change, the validity listeners are " +
//    "not removed and the beed listeners are not notified. The latter is " +
//    "correct, but what about the first?", false);
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
      Integer toAdd = new Integer(-1);
      $setEdit.addElementToAdd(toAdd);
      $setEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      MySetEdit edit1 = new MySetEdit($target);
      assertTrue(edit1.getElementsToAdd().isEmpty());
      assertTrue(edit1.getElementsToRemove().isEmpty());
      Integer toAdd1 = 5;
      edit1.addElementToAdd(toAdd1);
      edit1.perform();
      assertTrue(edit1.getElementsToAdd().size() == 1);
      assertTrue(edit1.getElementsToAdd().contains(toAdd1));
      assertTrue(edit1.getElementsToRemove().isEmpty());
      // perform
      MySetEdit edit2 = new MySetEdit($target);
      assertTrue(edit2.getElementsToAdd().isEmpty());
      assertTrue(edit2.getElementsToRemove().isEmpty());
      Integer toAdd2 = 7;
      edit2.addElementToAdd(toAdd2);
      edit2.perform();
      assertTrue(edit2.getElementsToAdd().size() == 1);
      assertTrue(edit2.getElementsToAdd().contains(toAdd2));
      assertTrue(edit2.getElementsToRemove().isEmpty());
      // perform - no change
      MySetEdit edit3 = new MySetEdit($target);
      assertTrue(edit3.getElementsToAdd().isEmpty());
      assertTrue(edit3.getElementsToRemove().isEmpty());
      edit3.addElementToAdd(toAdd2);
      edit3.perform();
      assertTrue(edit3.getElementsToAdd().isEmpty());
      assertTrue(edit3.getElementsToRemove().isEmpty());
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
      Integer toAdd = 5;
      $setEdit.addElementToAdd(toAdd);
      $setEdit.perform();
      assertTrue($target.get().size() == 1);
      assertTrue($target.get().contains(toAdd));
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
      $setEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
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
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
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
      $setEdit.kill();
      $setEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
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
      $setEdit.addElementToAdd(5);
      $setEdit.perform();
      $setEdit.undo();
      assertEquals($setEdit.getState(), State.UNDONE);
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
      $setEdit.addValidityListener($listener1);
      $setEdit.addValidityListener($listener2);
      assertTrue($setEdit.isValidityListener($listener1));
      assertTrue($setEdit.isValidityListener($listener2));
      // perform
      $setEdit.addElementToAdd(5);
      $setEdit.perform();
      assertFalse($setEdit.isValidityListener($listener1));
      assertFalse($setEdit.isValidityListener($listener2));
      $setEdit.undo();
      // there are no listeners
      assertFalse($setEdit.isValidityListener($listener1));
      assertFalse($setEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $setEdit);
      assertEquals($listener3.$event.getAddedElements(), $setEdit.getAddedElements());
      assertEquals($listener3.$event.getRemovedElements(), $setEdit.getRemovedElements());
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
    MySetEdit edit1 = null;
    try {
      // edit1
      edit1 = new MySetEdit($target);
      Integer toAdd1 = 5;
      edit1.addElementToAdd(toAdd1);
      edit1.perform();
      // edit2
      MySetEdit edit2 = new MySetEdit($target);
      edit2.addElementToRemove(toAdd1);
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
      MySetEdit edit1 = new MySetEdit($target);
      Integer toAdd1 = 5;
      edit1.addElementToAdd(toAdd1);
      edit1.perform();
      // $setEdit
      Integer toAdd2 = 7;
      $setEdit.addElementToAdd(toAdd2);
      $setEdit.perform();
      $setEdit.undo();
      assertTrue($target.get().size() == 1);
      assertTrue($target.get().contains(toAdd1));
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
      $setEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
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
      $setEdit.perform();
      $setEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
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
      $setEdit.kill();
      $setEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
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
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.redo();
      assertEquals($setEdit.getState(), State.DONE);
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
      $setEdit.addValidityListener($listener1);
      $setEdit.addValidityListener($listener2);
      assertTrue($setEdit.isValidityListener($listener1));
      assertTrue($setEdit.isValidityListener($listener2));
      // perform
      $setEdit.addElementToAdd(5);
      $setEdit.perform();
      assertFalse($setEdit.isValidityListener($listener1));
      assertFalse($setEdit.isValidityListener($listener2));
      $setEdit.undo();
      $setEdit.redo();
      // there are no listeners
      assertFalse($setEdit.isValidityListener($listener1));
      assertFalse($setEdit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $setEdit);
      assertEquals($listener3.$event.getAddedElements(), $setEdit.getAddedElements());
      assertEquals($listener3.$event.getRemovedElements(), $setEdit.getRemovedElements());
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
    MySetEdit edit1 = null;
    try {
      // edit1
      edit1 = new MySetEdit($target);
      Integer toAdd1 = 5;
      edit1.addElementToAdd(toAdd1);
      edit1.perform();
      edit1.undo();
      // edit2
      MySetEdit edit2 = new MySetEdit($target);
      edit2.addElementToAdd(toAdd1);
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
      MySetEdit edit1 = new MySetEdit($target);
      Integer toAdd1 = 5;
      edit1.addElementToAdd(toAdd1);
      edit1.perform();
      // $setEdit
      Integer toAdd2 = 7;
      $setEdit.addElementToAdd(toAdd2);
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.redo();
      assertTrue($target.get().size() == 2);
      assertTrue($target.get().contains(toAdd1));
      assertTrue($target.get().contains(toAdd2));
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
//    $setEdit.addValidityListener($listener1);
//    $setEdit.addValidityListener($listener2);
//    assertTrue($setEdit.isValidityListener($listener1));
//    assertTrue($setEdit.isValidityListener($listener2));
//    assertTrue($listener1.isEmpty());
//    assertTrue($listener2.isEmpty());
//    // check the value of the validity
//    assertTrue($setEdit.isValid());
//    // change validity
//    $setEdit.addElementToAdd(5);
//    $setEdit.checkValidityPublic();
//    // validity is still the same, so validity listeners are not notified
//    assertTrue($setEdit.isValid());
//    assertTrue($setEdit.isValidityListener($listener1));
//    assertTrue($setEdit.isValidityListener($listener2));
//    assertTrue($listener1.isEmpty());
//    assertTrue($listener2.isEmpty());
//    // change validity
//    $setEdit.addElementToAdd(-1);
//    try {
//      $setEdit.checkValidityPublic();
//      fail();
//    }
//    catch (IllegalEditException ieExc) {
//      // NOP
//    }
//    // validity has changed, so validity listeners are notified
//    assertFalse($setEdit.isValid());
//    assertTrue($setEdit.isValidityListener($listener1));
//    assertTrue($setEdit.isValidityListener($listener2));
//    assertEquals($listener1.$target, $setEdit);
//    assertEquals($listener1.$validity, $setEdit.isValid());
//    assertEquals($listener2.$target, $setEdit);
//    assertEquals($listener2.$validity, $setEdit.isValid());
//    // change validity again
//    $listener1.reset();
//    $listener2.reset();
//    $setEdit.removeElementToAdd(-1);
//    $setEdit.checkValidityPublic();
//    // validity has changed, so validity listeners are notified
//    assertTrue($setEdit.isValid());
//    assertTrue($setEdit.isValidityListener($listener1));
//    assertTrue($setEdit.isValidityListener($listener2));
//    assertEquals($listener1.$target, $setEdit);
//    assertEquals($listener1.$validity, $setEdit.isValid());
//    assertEquals($listener2.$target, $setEdit);
//    assertEquals($listener2.$validity, $setEdit.isValid());
//  }

  @Test
  public void notifyListeners() throws EditStateException, IllegalEditException {
    // add listener to beed
    $target.addListener($listener3);
    assertTrue($target.isListener($listener3));
    assertNull($listener3.$event);
    // notify
    $setEdit.perform();
    $setEdit.publicTopologicalUpdateStart();
    // check
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getEdit(), $setEdit);
    assertEquals($listener3.$event.getAddedElements(), $setEdit.getAddedElements());
    assertEquals($listener3.$event.getRemovedElements(), $setEdit.getRemovedElements());
    assertEquals($listener3.$event.getSource(), $target);
  }

  @Test
  // incorrect begin-state
  public void addElementToAdd1() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.addElementToAdd(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void addElementToAdd2() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.addElementToAdd(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void addElementToAdd3() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.redo();
      $setEdit.addElementToAdd(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, check postconditions
  public void addElementToAdd4() {
    try {
      assertTrue($setEdit.getElementsToAdd().isEmpty());
      Integer toAdd1 = 1;
      $setEdit.addElementToAdd(toAdd1);
      assertTrue($setEdit.getElementsToAdd().size() == 1);
      assertTrue($setEdit.getElementsToAdd().contains(toAdd1));
      Integer toAdd2 = 2;
      $setEdit.addElementToAdd(toAdd2);
      assertTrue($setEdit.getElementsToAdd().size() == 2);
      assertTrue($setEdit.getElementsToAdd().contains(toAdd1));
      assertTrue($setEdit.getElementsToAdd().contains(toAdd2));
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void removeElementToAdd1() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.removeElementToAdd(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void removeElementToAdd2() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.removeElementToAdd(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void removeElementToAdd3() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.redo();
      $setEdit.removeElementToAdd(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, check postconditions
  public void removeElementToAdd4() {
    try {
      assertTrue($setEdit.getElementsToAdd().isEmpty());
      Integer toAdd1 = 1;
      $setEdit.addElementToAdd(toAdd1);
      assertTrue($setEdit.getElementsToAdd().size() == 1);
      assertTrue($setEdit.getElementsToAdd().contains(toAdd1));
      Integer toAdd2 = 2;
      $setEdit.addElementToAdd(toAdd2);
      assertTrue($setEdit.getElementsToAdd().size() == 2);
      assertTrue($setEdit.getElementsToAdd().contains(toAdd1));
      assertTrue($setEdit.getElementsToAdd().contains(toAdd2));
      $setEdit.removeElementToAdd(toAdd1);
      assertTrue($setEdit.getElementsToAdd().size() == 1);
      assertTrue($setEdit.getElementsToAdd().contains(toAdd2));
      $setEdit.removeElementToAdd(toAdd2);
      assertTrue($setEdit.getElementsToAdd().isEmpty());
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void addElementToRemove1() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.addElementToRemove(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void addElementToRemove2() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.addElementToRemove(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void addElementToRemove3() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.redo();
      $setEdit.addElementToRemove(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, check postconditions
  public void addElementToRemove4() {
    try {
      assertTrue($setEdit.getElementsToRemove().isEmpty());
      Integer toAdd1 = 1;
      $setEdit.addElementToRemove(toAdd1);
      assertTrue($setEdit.getElementsToRemove().size() == 1);
      assertTrue($setEdit.getElementsToRemove().contains(toAdd1));
      Integer toAdd2 = 2;
      $setEdit.addElementToRemove(toAdd2);
      assertTrue($setEdit.getElementsToRemove().size() == 2);
      assertTrue($setEdit.getElementsToRemove().contains(toAdd1));
      assertTrue($setEdit.getElementsToRemove().contains(toAdd2));
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void removeElementToRemove1() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.removeElementToRemove(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void removeElementToRemove2() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.removeElementToRemove(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // incorrect begin-state
  public void removeElementToRemove3() {
    try {
      $setEdit.addElementToAdd(1);
      $setEdit.perform();
      $setEdit.undo();
      $setEdit.redo();
      $setEdit.removeElementToRemove(2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $setEdit);
      assertEquals(e.getCurrentState(), $setEdit.getState());
      assertEquals(e.getExpectedState(), State.NOT_YET_PERFORMED);
    }
    catch (IllegalEditException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  // correct begin-state, check postconditions
  public void removeElementToRemove4() {
    try {
      assertTrue($setEdit.getElementsToRemove().isEmpty());
      Integer toAdd1 = 1;
      $setEdit.addElementToRemove(toAdd1);
      assertTrue($setEdit.getElementsToRemove().size() == 1);
      assertTrue($setEdit.getElementsToRemove().contains(toAdd1));
      Integer toAdd2 = 2;
      $setEdit.addElementToRemove(toAdd2);
      assertTrue($setEdit.getElementsToRemove().size() == 2);
      assertTrue($setEdit.getElementsToRemove().contains(toAdd1));
      assertTrue($setEdit.getElementsToRemove().contains(toAdd2));
      $setEdit.removeElementToRemove(toAdd1);
      assertTrue($setEdit.getElementsToRemove().size() == 1);
      assertTrue($setEdit.getElementsToRemove().contains(toAdd2));
      $setEdit.removeElementToRemove(toAdd2);
      assertTrue($setEdit.getElementsToRemove().isEmpty());
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  public void getAddedElements() throws EditStateException, IllegalEditException {
    // set value of target
    Integer init = 3;
    MySetEdit edit = new MySetEdit($target);
    edit.addElementToAdd(init);
    edit.perform();
    // check
    assertEquals($setEdit.getState(), State.NOT_YET_PERFORMED);
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getAddedElements().isEmpty());
    // set goal
    Integer toAdd = 5;
    $setEdit.addElementToAdd(toAdd);
    assertEquals($setEdit.getState(), State.NOT_YET_PERFORMED);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getAddedElements().isEmpty());
    // perform
    $setEdit.perform();
    assertEquals($setEdit.getState(), State.DONE);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getAddedElements().size() == 1);
    assertTrue($setEdit.getAddedElements().contains(toAdd));
    // undo
    $setEdit.undo();
    assertEquals($setEdit.getState(), State.UNDONE);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getAddedElements().isEmpty());
    // redo
    $setEdit.redo();
    assertEquals($setEdit.getState(), State.DONE);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getAddedElements().size() == 1);
    assertTrue($setEdit.getAddedElements().contains(toAdd));
    // kill
    $setEdit.kill();
    assertEquals($setEdit.getState(), State.DEAD);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getAddedElements().isEmpty());
  }

  @Test
  public void getRemovedElements() throws EditStateException, IllegalEditException {
    // set value of target
    Integer init = 3;
    MySetEdit edit = new MySetEdit($target);
    edit.addElementToAdd(init);
    edit.perform();
    // check
    assertEquals($setEdit.getState(), State.NOT_YET_PERFORMED);
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getRemovedElements().isEmpty());
    // set goal
    Integer toAdd = 5;
    $setEdit.addElementToAdd(toAdd);
    assertEquals($setEdit.getState(), State.NOT_YET_PERFORMED);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getRemovedElements().size() == 1);
    assertTrue($setEdit.getRemovedElements().contains(toAdd));
    // perform
    $setEdit.perform();
    assertEquals($setEdit.getState(), State.DONE);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getRemovedElements().isEmpty());
    // undo
    $setEdit.undo();
    assertEquals($setEdit.getState(), State.UNDONE);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getRemovedElements().size() == 1);
    assertTrue($setEdit.getRemovedElements().contains(toAdd));
    // redo
    $setEdit.redo();
    assertEquals($setEdit.getState(), State.DONE);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getRemovedElements().isEmpty());
    // kill
    $setEdit.kill();
    assertEquals($setEdit.getState(), State.DEAD);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.getRemovedElements().size() == 1);
    assertTrue($setEdit.getRemovedElements().contains(toAdd));
  }


  @Test
  public void storeInitialState() throws EditStateException, IllegalEditException {
    assertTrue($target.get().isEmpty());
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    $setEdit.storeInitialState();
    assertTrue($target.get().isEmpty());
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    // change value of target
    Integer toAdd1 = 1;
    Integer toAdd2 = 2;
    Integer toAdd3 = 3;
    Integer toAdd4 = 4;
    MySetEdit setEdit = new MySetEdit($target);
    setEdit.addElementToAdd(toAdd1);
    setEdit.addElementToAdd(toAdd2);
    setEdit.addElementToAdd(toAdd3);
    setEdit.addElementToAdd(toAdd4);
    setEdit.perform();
    assertTrue($target.get().size() == 4);
    assertTrue($target.get().contains(toAdd1));
    assertTrue($target.get().contains(toAdd2));
    assertTrue($target.get().contains(toAdd3));
    assertTrue($target.get().contains(toAdd4));
    // add elements to edit
    Integer toAdd6 = 6;
    Integer toRemove7 = 7;
    $setEdit.addElementToAdd(toAdd1);
    $setEdit.addElementToAdd(toAdd6);
    $setEdit.addElementToRemove(toAdd3);
    $setEdit.addElementToRemove(toRemove7);
    assertTrue($setEdit.getElementsToAdd().size() == 2);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd1));
    assertTrue($setEdit.getElementsToAdd().contains(toAdd6));
    assertTrue($setEdit.getElementsToRemove().size() == 2);
    assertTrue($setEdit.getElementsToRemove().contains(toAdd3));
    assertTrue($setEdit.getElementsToRemove().contains(toRemove7));
    // store initial state
    $setEdit.storeInitialState(); // added = {1,6} \ {1,2,3,4}, removed = {3,7} intersection {1,2,3,4}
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd6));
    assertTrue($setEdit.getElementsToRemove().size() == 1);
    assertTrue($setEdit.getElementsToRemove().contains(toAdd3));
  }

  @Test
  public void isChange() throws EditStateException, IllegalEditException {
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertFalse($setEdit.isChange());
    // change elements to add and to remove
    Integer toAdd = 2;
    $setEdit.addElementToAdd(toAdd);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.isChange());
    // change elements to add and to remove
    Integer toRemove = 3;
    $setEdit.addElementToRemove(toRemove);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().size() == 1);
    assertTrue($setEdit.getElementsToRemove().contains(toRemove));
    assertTrue($setEdit.isChange());
    // change elements to add and to remove
    $setEdit.removeElementToRemove(toRemove);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($setEdit.isChange());
    // change elements to add and to remove
    $setEdit.removeElementToAdd(toAdd);
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertFalse($setEdit.isChange());
  }

  @Test
  public void isInitialStateCurrent() throws EditStateException, IllegalEditException {
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($target.get().isEmpty());
    assertTrue($setEdit.isInitialStateCurrent());
    // change elements to add and remove
    Integer toAdd1 = 2;
    Integer toAdd2 = 3;
    Integer toRemove = 4;
    $setEdit.addElementToAdd(toAdd1);
    $setEdit.addElementToRemove(toRemove);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd1));
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToRemove().contains(toRemove));
    assertTrue($target.get().isEmpty());
    assertFalse($setEdit.isInitialStateCurrent());
    // change target
    MySetEdit setEdit = new MySetEdit($target);
    setEdit.addElementToAdd(toAdd1);
    setEdit.addElementToAdd(toRemove);
    setEdit.addElementToAdd(toAdd2);
    setEdit.perform();
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd1));
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToRemove().contains(toRemove));
    assertTrue($target.get().size() == 3);
    assertTrue($target.get().contains(toAdd1));
    assertTrue($target.get().contains(toAdd2));
    assertTrue($target.get().contains(toRemove));
    assertFalse($setEdit.isInitialStateCurrent());
    // change target
    MySetEdit setEdit2 = new MySetEdit($target);
    setEdit2.addElementToRemove(toAdd1);
    setEdit2.perform();
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd1));
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToRemove().contains(toRemove));
    assertTrue($target.get().size() == 2);
    assertTrue($target.get().contains(toAdd2));
    assertTrue($target.get().contains(toRemove));
    assertTrue($setEdit.isInitialStateCurrent());
  }

  @Test
  public void performance() throws EditStateException, IllegalEditException {
    Integer toAdd = 2;
    Integer toRemove = 3;
    HashSet<Integer> initialElements = new HashSet<Integer>();
    initialElements.add(toRemove);
    $target.addElements(initialElements);
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertEquals(1, $target.get().size());
    assertTrue($target.get().contains(toRemove));
    $setEdit.performance();
    assertEquals(1, $target.get().size());
    assertTrue($target.get().contains(toRemove));
    // change elements to add and remove
    $setEdit.addElementToAdd(toAdd);
    $setEdit.addElementToRemove(toRemove);
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().size() == 1);
    assertTrue($setEdit.getElementsToRemove().contains(toRemove));
    assertEquals(1, $target.get().size());
    assertTrue($target.get().contains(toRemove));
    $setEdit.performance();
    assertTrue($target.get().size() == 1);
    assertTrue($target.get().contains(toAdd));
    // change elements to add and remove
    Integer toAdd2 = 3;
    MySetEdit setEdit = new MySetEdit($target);
    setEdit.addElementToAdd(toAdd2);
    setEdit.addElementToRemove(toAdd);
    assertTrue(setEdit.getElementsToAdd().size() == 1);
    assertTrue(setEdit.getElementsToAdd().contains(toAdd2));
    assertTrue(setEdit.getElementsToRemove().size() == 1);
    assertTrue(setEdit.getElementsToRemove().contains(toAdd));
    assertTrue($target.get().size() == 1);
    assertTrue($target.get().contains(toAdd));
    setEdit.performance();
    assertTrue($target.get().size() == 1);
    assertTrue($target.get().contains(toAdd2));
  }

  @Test
  public void isGoalStateCurrent() throws EditStateException, IllegalEditException {
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($target.get().isEmpty());
    assertTrue($setEdit.isGoalStateCurrent());
    // perform
    Integer toAdd = 2;
    Integer toRemove = 3;
    $setEdit.addElementToAdd(toAdd);
    $setEdit.addElementToRemove(toRemove);
    $setEdit.perform();
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($target.get().size() == 1);
    assertTrue($target.get().contains(toAdd));
    assertTrue($setEdit.isGoalStateCurrent());
    // undo
    $setEdit.undo();
    assertTrue($setEdit.getElementsToAdd().size() == 1);
    assertTrue($setEdit.getElementsToAdd().contains(toAdd));
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($target.get().isEmpty());
    assertFalse($setEdit.isGoalStateCurrent());
  }

  @Test
  public void unperformance() throws EditStateException, IllegalEditException {
    assertTrue($setEdit.getElementsToAdd().isEmpty());
    assertTrue($setEdit.getElementsToRemove().isEmpty());
    assertTrue($target.get().isEmpty());
    $setEdit.unperformance();
    assertTrue($target.get().isEmpty());
    // perform
    Integer toAdd = 2;
    Integer toRemove = 3;
    $setEdit.addElementToAdd(toAdd);
    $setEdit.addElementToRemove(toRemove);
    $setEdit.perform();
    assertTrue($target.get().size() == 1);
    assertTrue($target.get().contains(toAdd));
    // change initial
    MySetEdit edit = new MySetEdit($target);
    edit.addElementToAdd(toAdd);
    edit.addElementToRemove(toRemove);
    assertTrue(edit.getElementsToAdd().size() == 1);
    assertTrue(edit.getElementsToAdd().contains(toAdd));
    assertTrue(edit.getElementsToRemove().contains(toRemove));
    edit.unperformance();
    assertTrue($target.get().size() == 1);
    assertTrue($target.get().contains(toRemove));
  }

  @Test
  public void fireEvent() {
    // add listener to target
    $target.addListener($listener3);
    assertTrue($target.isListener($listener3));
    assertNull($listener3.$event);
    // fire
    Set<Integer> addedElements = new HashSet<Integer>();
    addedElements.add(1);
    Set<Integer> removedElements = new HashSet<Integer>();
    removedElements.add(3);
    SetEvent<Integer> event =
      new ActualSetEvent<Integer>($target, addedElements, removedElements, null);
    $setEdit.publicTopologicalUpdateStart(event);
    // check listener
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event, event);
  }

  @Test
  public void createEvent() throws EditStateException, IllegalEditException {
    assertEquals(State.NOT_YET_PERFORMED, $setEdit.getState());
//    SetEvent<Integer> createdEvent = $setEdit.createEvent();
//    assertEquals(createdEvent.getEdit(), $setEdit);
//    assertTrue(createdEvent.getAddedElements().isEmpty());
//    assertTrue(createdEvent.getRemovedElements().isEmpty());
//    assertEquals(createdEvent.getSource(), $target);
    // perform
    Integer toAdd = 1;
    Integer toRemove = 2;
    $setEdit.addElementToAdd(toAdd);
    $setEdit.addElementToRemove(toRemove);
    $setEdit.perform();
    // create event
    Map<AbstractBeed<?>, ? extends SetEvent<Integer>> events = $setEdit.createEvents();
    SetEvent<Integer> createdEvent = events.get($setEdit.getTarget());
    assertEquals(createdEvent.getEdit(), $setEdit);
    assertTrue(createdEvent.getAddedElements().size() == 1);
    assertTrue(createdEvent.getAddedElements().contains(toAdd));
    assertTrue(createdEvent.getRemovedElements().isEmpty());
    assertEquals(createdEvent.getSource(), $target);
    // undo
    $setEdit.undo();
    // create event
    events = $setEdit.createEvents();
    createdEvent = events.get($setEdit.getTarget());
    assertEquals(createdEvent.getEdit(), $setEdit);
    assertTrue(createdEvent.getAddedElements().isEmpty());
    assertTrue(createdEvent.getRemovedElements().size() == 1);
    assertTrue(createdEvent.getRemovedElements().contains(toAdd));
    assertEquals(createdEvent.getSource(), $target);
  }

}