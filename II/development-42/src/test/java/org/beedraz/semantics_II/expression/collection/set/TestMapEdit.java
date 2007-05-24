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

import java.util.Map;

import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.Listener;
import org.beedraz.semantics_II.ValidityListener;
import org.beedraz.semantics_II.Edit.State;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestMapEdit {

  public class MyMapEdit extends MapEdit<String, Integer> {

    public MyMapEdit(EditableMapBeed<String, Integer> target) {
      super(target);
    }

  }

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class StubValidityListener implements ValidityListener {

    //@Implements
    public void listenerRemoved(Edit<?> target) {
      $target = target;
      $listenerRemoved = true;
    }

    //@Implements
    public void validityChanged(Edit<?> target, boolean newValidity) {
      $target = target;
      $validity = newValidity;
    }

    public boolean isEmpty() {
      return $target == null &&
             $validity == null &&
             $listenerRemoved == null;
    }

    public void reset() {
      $target = null;
      $validity = null;
      $listenerRemoved = null;
    }

    public Boolean validity() {
      return $validity;
    }

    private Boolean $validity;

    public Boolean listenerRemoved() {
      return $listenerRemoved;
    }

    private Boolean $listenerRemoved;

    public Edit<?> target() {
      return $target;
    }

    private Edit<?> $target;

  }

  public class StubMapListener implements Listener<MapEvent<String, Integer>> {

    public void beedChanged(MapEvent<String, Integer> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public MapEvent<String, Integer> $event;

  }

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $target = new EditableMapBeed<String, Integer>($owner) {
      @Override
      public boolean isAcceptable(Map<String, Integer> elementsToAdd, Map<String, Integer> elementsToRemove) {
        // all keys in the map of added element are effective
        for (String s : elementsToAdd.keySet()) {
          if (s == null) {
            return false;
          }
        }
        // all values in the map of added elements are positive
        for (Integer i : elementsToAdd.values()) {
          if (i == null || i <= 0) {
            return false;
          }
        }
        return true;
      }

    };
    $mapEdit = new MyMapEdit($target);
    $listener1 = new StubValidityListener();
    $listener2 = new StubValidityListener();
    $listener3 = new StubMapListener();
  }

  @After
  public void tearDown() throws Exception {
    $owner = null;
  }

  private StubBeanBeed $owner;
  private EditableMapBeed<String, Integer> $target;
  private MyMapEdit $mapEdit;
  private StubValidityListener $listener1;
  private StubValidityListener $listener2;
  private StubMapListener $listener3;

  @Test
  public void constructor() {
    assertEquals($mapEdit.getTarget(), $target);
  }

  @Test
  // incorrect begin-state
  public void perform1() {
    try {
      $mapEdit.perform();
      $mapEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.kill();
      $mapEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.perform();
      assertEquals($mapEdit.getState(), State.DONE);
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
      $mapEdit.addValidityListener($listener1);
      $mapEdit.addValidityListener($listener2);
      assertTrue($mapEdit.isValidityListener($listener1));
      assertTrue($mapEdit.isValidityListener($listener2));
      // perform
      assertTrue($mapEdit.getElementsToAdd().isEmpty());
      assertTrue($mapEdit.getElementsToRemove().isEmpty());
      String keyA = "horse";
      Integer valueA = 3;
      $mapEdit.addElementToAdd(keyA, valueA);
      $mapEdit.perform();
      assertTrue($mapEdit.getElementsToAdd().size() == 1);
      assertTrue($mapEdit.getElementsToAdd().containsKey(keyA));
      assertEquals(valueA, $mapEdit.getElementsToAdd().get(keyA));
      assertTrue($mapEdit.getElementsToRemove().isEmpty());
      // validity listeners should be removed (and notified when removed)
      assertTrue($listener1.listenerRemoved());
      assertTrue($listener2.listenerRemoved());
      assertFalse($mapEdit.isValidityListener($listener1));
      assertFalse($mapEdit.isValidityListener($listener2));
      // validity listeners are not notified of a validity change
      assertNull($listener1.validity());
      assertNull($listener2.validity());
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $mapEdit);
      assertEquals($listener3.$event.getAddedElements(), $mapEdit.getAddedElements());
      assertEquals($listener3.$event.getRemovedElements(), $mapEdit.getRemovedElements());
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
      $mapEdit.addValidityListener($listener1);
      $mapEdit.addValidityListener($listener2);
      assertTrue($mapEdit.isValidityListener($listener1));
      assertTrue($mapEdit.isValidityListener($listener2));
      // perform
      assertTrue($mapEdit.getElementsToAdd().isEmpty());
      assertTrue($mapEdit.getElementsToRemove().isEmpty());
      $mapEdit.perform();
      assertTrue($mapEdit.getElementsToAdd().isEmpty());
      assertTrue($mapEdit.getElementsToRemove().isEmpty());
      // validity listeners should be removed (and notified when removed)
      assertTrue($listener1.listenerRemoved());
      assertTrue($listener2.listenerRemoved());
      assertFalse($mapEdit.isValidityListener($listener1));
      assertFalse($mapEdit.isValidityListener($listener2));
      // validity listeners are not notified of a validity change
      assertNull($listener1.validity());
      assertNull($listener2.validity());
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
      String keyA = "cow";
      Integer valueA = new Integer(-1);
      $mapEdit.addElementToAdd(keyA, valueA);
      $mapEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // when the edit is not valid, an exception should be thrown
  public void perform7b() {
    try {
      // perform
      String keyA = null;
      Integer valueA = new Integer(1);
      $mapEdit.addElementToAdd(keyA, valueA);
      $mapEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getMessage(), null);
    }
  }

  @Test
  // when the edit is not valid, the validity listeners are notified, the listeners of the beed are not notified
  public void perform7c() {
    try {
      // add listener to beed
      $target.addListener($listener3);
      assertTrue($target.isListener($listener3));
      assertNull($listener3.$event);
      // add validity listeners to edit
      $mapEdit.addValidityListener($listener1);
      $mapEdit.addValidityListener($listener2);
      assertTrue($mapEdit.isValidityListener($listener1));
      assertTrue($mapEdit.isValidityListener($listener2));
      // perform
      String keyA = null;
      Integer valueA = new Integer(1);
      $mapEdit.addElementToAdd(keyA, valueA);
      $mapEdit.perform();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
    catch (IllegalEditException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getMessage(), null);
      // validity listeners are not removed (or notified of removal)
      assertNull($listener1.listenerRemoved());
      assertNull($listener2.listenerRemoved());
      assertTrue($mapEdit.isValidityListener($listener1));
      assertTrue($mapEdit.isValidityListener($listener2));
      // validity listeners are notified of a validity change
      assertFalse($listener1.validity());
      assertFalse($listener2.validity());
      // listeners of the beed are not notified
      assertNull($listener3.$event);
    }
  }

  @Test
  // check whether the initial state is stored
  public void perform8() {
    try {
      // perform
      MyMapEdit edit1 = new MyMapEdit($target);
      assertTrue(edit1.getElementsToAdd().isEmpty());
      assertTrue(edit1.getElementsToRemove().isEmpty());
      String keyA1 = "cow";
      Integer valueA1 = 5;
      edit1.addElementToAdd(keyA1, valueA1);
      edit1.perform();
      assertTrue(edit1.getElementsToAdd().size() == 1);
      assertTrue(edit1.getElementsToAdd().containsKey(keyA1));
      assertEquals(valueA1, edit1.getElementsToAdd().get(keyA1));
      assertTrue(edit1.getElementsToRemove().isEmpty());
      // perform
      MyMapEdit edit2 = new MyMapEdit($target);
      assertTrue(edit2.getElementsToAdd().isEmpty());
      assertTrue(edit2.getElementsToRemove().isEmpty());
      String keyA2 = "horse";
      Integer valueA2 = 7;
      edit2.addElementToAdd(keyA2, valueA2);
      edit2.perform();
      assertTrue(edit2.getElementsToAdd().size() == 1);
      assertTrue(edit2.getElementsToAdd().containsKey(keyA2));
      assertEquals(valueA2, edit2.getElementsToAdd().get(keyA2));
      assertTrue(edit2.getElementsToRemove().isEmpty());
      // perform - no change (getElementsToAdd() and getElementsToRemove() are
      // made empty by the perform method)
      MyMapEdit edit3 = new MyMapEdit($target);
      assertTrue(edit3.getElementsToAdd().isEmpty());
      assertTrue(edit3.getElementsToRemove().isEmpty());
      edit3.addElementToAdd(keyA2, valueA2);
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
      String keyA1 = "cow";
      Integer valueA1 = 5;
      MyMapEdit edit1 = new MyMapEdit($target);
      edit1.addElementToAdd(keyA1, valueA1);
      edit1.perform();
      assertTrue($target.keySet().size() == 1);
      assertTrue($target.keySet().contains(keyA1));
      assertEquals(valueA1, $target.get(keyA1));
      // perform
      String keyA2 = "horse";
      Integer valueA2 = 7;
      MyMapEdit edit2 = new MyMapEdit($target);
      edit2.addElementToAdd(keyA2, valueA2);
      edit2.perform();
      assertTrue($target.keySet().size() == 2);
      assertTrue($target.keySet().contains(keyA1));
      assertTrue($target.keySet().contains(keyA2));
      assertEquals(valueA1, $target.get(keyA1));
      assertEquals(valueA2, $target.get(keyA2));
      // perform
      String keyA3 = "pig";
      Integer valueA3 = 11;
      MyMapEdit edit3 = new MyMapEdit($target);
      edit3.addElementToAdd(keyA1, valueA1); // no change
      edit3.addElementToAdd(keyA3, valueA3);
      edit3.addElementToRemove(keyA2, valueA2);
      edit3.perform();
      assertTrue($target.keySet().size() == 2);
      assertTrue($target.keySet().contains(keyA1));
      assertTrue($target.keySet().contains(keyA3));
      assertEquals(valueA1, $target.get(keyA1));
      assertEquals(valueA3, $target.get(keyA3));
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
      $mapEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.kill();
      $mapEdit.undo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.addElementToAdd("cow", 5);
      $mapEdit.perform();
      $mapEdit.undo();
      assertEquals($mapEdit.getState(), State.UNDONE);
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
  // correct begin-state, so validity listeners are removed, listeners of the beed are notified
  public void undo5() {
    try {
      // add listener to beed
      $target.addListener($listener3);
      assertTrue($target.isListener($listener3));
      assertNull($listener3.$event);
      // add validity listeners to edit
      $mapEdit.addValidityListener($listener1);
      $mapEdit.addValidityListener($listener2);
      assertTrue($mapEdit.isValidityListener($listener1));
      assertTrue($mapEdit.isValidityListener($listener2));
      // perform
      $mapEdit.addElementToAdd("horse", 5);
      $mapEdit.perform();
      assertFalse($mapEdit.isValidityListener($listener1));
      assertFalse($mapEdit.isValidityListener($listener2));
      $mapEdit.undo();
      // validity listeners are removed (and notified of removal)
      assertTrue($listener1.listenerRemoved());
      assertTrue($listener2.listenerRemoved());
      assertFalse($mapEdit.isValidityListener($listener1));
      assertFalse($mapEdit.isValidityListener($listener2));
      // validity listeners are not notified of validity change
      assertNull($listener1.validity());
      assertNull($listener2.validity());
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $mapEdit);
      assertEquals($listener3.$event.getAddedElements(), $mapEdit.getAddedElements());
      assertEquals($listener3.$event.getRemovedElements(), $mapEdit.getRemovedElements());
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
    MyMapEdit edit1 = null;
    try {
      // edit1
      edit1 = new MyMapEdit($target);
      String key = "sheep";
      Integer value = 5;
      edit1.addElementToAdd(key, value);
      edit1.perform();
      // edit2
      MyMapEdit edit2 = new MyMapEdit($target);
      edit2.addElementToRemove(key, value);
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
      MyMapEdit edit1 = new MyMapEdit($target);
      String keyA1 = "horse";
      Integer valueA1 = 5;
      edit1.addElementToAdd(keyA1, valueA1);
      edit1.perform();
      // $mapEdit
      String keyA2 = "cow";
      Integer valueA2 = 7;
      $mapEdit.addElementToAdd(keyA2, valueA2);
      $mapEdit.perform();
      $mapEdit.undo();
      assertTrue($target.keySet().size() == 1);
      assertTrue($target.keySet().contains(keyA1));
      assertEquals(valueA1, $target.get(keyA1));
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
      $mapEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.perform();
      $mapEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.kill();
      $mapEdit.redo();
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.redo();
      assertEquals($mapEdit.getState(), State.DONE);
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
  // correct begin-state, so validity listeners are removed, listeners of the beed are notified
  public void redo5() {
    try {
      // add listener to beed
      $target.addListener($listener3);
      assertTrue($target.isListener($listener3));
      assertNull($listener3.$event);
      // add validity listeners to edit
      $mapEdit.addValidityListener($listener1);
      $mapEdit.addValidityListener($listener2);
      assertTrue($mapEdit.isValidityListener($listener1));
      assertTrue($mapEdit.isValidityListener($listener2));
      // perform
      $mapEdit.addElementToAdd("horse", 5);
      $mapEdit.perform();
      assertFalse($mapEdit.isValidityListener($listener1));
      assertFalse($mapEdit.isValidityListener($listener2));
      $mapEdit.undo();
      $mapEdit.redo();
      // validity listeners are removed (and notified of removal)
      assertTrue($listener1.listenerRemoved());
      assertTrue($listener2.listenerRemoved());
      assertFalse($mapEdit.isValidityListener($listener1));
      assertFalse($mapEdit.isValidityListener($listener2));
      // validity listeners are not notified of validity change
      assertNull($listener1.validity());
      assertNull($listener2.validity());
      // listeners of the beed are notified
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getEdit(), $mapEdit);
      assertEquals($listener3.$event.getAddedElements(), $mapEdit.getAddedElements());
      assertEquals($listener3.$event.getRemovedElements(), $mapEdit.getRemovedElements());
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
    MyMapEdit edit1 = null;
    try {
      // edit1
      edit1 = new MyMapEdit($target);
      String keyA1 = "chicken";
      Integer valueA1 = 5;
      edit1.addElementToAdd(keyA1, valueA1);
      edit1.perform();
      edit1.undo();
      // edit2
      MyMapEdit edit2 = new MyMapEdit($target);
      edit2.addElementToAdd(keyA1, valueA1);
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
      MyMapEdit edit1 = new MyMapEdit($target);
      String keyA1 = "duck";
      Integer valueA1 = 5;
      edit1.addElementToAdd(keyA1, valueA1);
      edit1.perform();
      // $mapEdit
      String keyA2 = "goat";
      Integer valueA2 = 7;
      $mapEdit.addElementToAdd(keyA2, valueA2);
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.redo();
      assertTrue($target.keySet().size() == 2);
      assertTrue($target.keySet().contains(keyA1));
      assertTrue($target.keySet().contains(keyA2));
      assertEquals(valueA1, $target.get(keyA1));
      assertEquals(valueA2, $target.get(keyA2));
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

  /**
   * Check whether the validity listeners are notified during a perform.
   */
  @Test
  public void checkValidity1() throws EditStateException, IllegalEditException {
    // add validity listeners
    $mapEdit.addValidityListener($listener1);
    $mapEdit.addValidityListener($listener2);
    assertTrue($mapEdit.isValidityListener($listener1));
    assertTrue($mapEdit.isValidityListener($listener2));
    assertTrue($listener1.isEmpty());
    assertTrue($listener2.isEmpty());
    // check the value of the validity
    assertTrue($mapEdit.isValid());
    // change validity
    $mapEdit.addElementToAdd("pig", 5);
    $mapEdit.perform();
    // validity is still the same, so validity listeners are not notified
    assertTrue($mapEdit.isValid());
    assertNull($listener1.validity());
    assertNull($listener2.validity());
    // validity listeners are removed
    assertTrue($listener1.listenerRemoved());
    assertTrue($listener2.listenerRemoved());
    assertFalse($mapEdit.isValidityListener($listener1));
    assertFalse($mapEdit.isValidityListener($listener2));
  }

  /**
   * Check whether the validity listeners are notified during a perform.
   */
  @Test
  public void checkValidity2() throws EditStateException, IllegalEditException {
    // add validity listeners
    $mapEdit.addValidityListener($listener1);
    $mapEdit.addValidityListener($listener2);
    assertTrue($mapEdit.isValidityListener($listener1));
    assertTrue($mapEdit.isValidityListener($listener2));
    assertTrue($listener1.isEmpty());
    assertTrue($listener2.isEmpty());
    // check the value of the validity
    assertTrue($mapEdit.isValid());
    // change validity
    String key = "horse";
    Integer value = -1;
    $mapEdit.addElementToAdd(key, value);
    try {
      $mapEdit.perform();
    }
    catch(IllegalEditException exc) {
      // NOP
    }
    // validity has changed, so validity listeners are notified
    assertFalse($mapEdit.isValid());
    assertTrue($mapEdit.isValidityListener($listener1));
    assertTrue($mapEdit.isValidityListener($listener2));
    assertEquals($listener1.$target, $mapEdit);
    assertEquals($listener1.$validity, $mapEdit.isValid());
    assertNull($listener1.listenerRemoved());
    assertEquals($listener2.$target, $mapEdit);
    assertEquals($listener2.$validity, $mapEdit.isValid());
    assertNull($listener2.listenerRemoved());
    // change validity again
    $listener1.reset();
    $listener2.reset();
    $mapEdit.removeElementToAdd(key);
    $mapEdit.perform();
    // validity has changed, so validity listeners are notified
    assertTrue($mapEdit.isValid());
    assertEquals($listener1.$target, $mapEdit);
    assertEquals($listener1.$validity, $mapEdit.isValid());
    assertEquals($listener2.$target, $mapEdit);
    assertEquals($listener2.$validity, $mapEdit.isValid());
    // validity listeners are removed and notified
    assertTrue($listener1.listenerRemoved());
    assertFalse($mapEdit.isValidityListener($listener1));
    assertTrue($listener2.listenerRemoved());
    assertFalse($mapEdit.isValidityListener($listener2));
  }

  @Test
  // incorrect begin-state
  public void addElementToAdd1() {
    try {
      $mapEdit.addElementToAdd("duck1", 1);
      $mapEdit.perform();
      $mapEdit.addElementToAdd("duck2", 2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.addElementToAdd("duck1", 1);
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.addElementToAdd("duck2", 2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.addElementToAdd("duck1", 1);
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.redo();
      $mapEdit.addElementToAdd("duck2", 2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      assertTrue($mapEdit.getElementsToAdd().isEmpty());
      String keyA1 = "pig";
      Integer valueA1 = 1;
      $mapEdit.addElementToAdd(keyA1, valueA1);
      assertTrue($mapEdit.getElementsToAdd().size() == 1);
      assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
      assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
      String keyA2 = "lamb";
      Integer valueA2 = 2;
      $mapEdit.addElementToAdd(keyA2, valueA2);
      assertTrue($mapEdit.getElementsToAdd().size() == 2);
      assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
      assertTrue($mapEdit.getElementsToAdd().containsKey(keyA2));
      assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
      assertEquals(valueA2, $mapEdit.getElementsToAdd().get(keyA2));
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
      $mapEdit.addElementToAdd("dog", 1);
      $mapEdit.perform();
      $mapEdit.removeElementToAdd("cat");
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.addElementToAdd("dog", 1);
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.removeElementToAdd("cat");
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.addElementToAdd("dog", 1);
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.redo();
      $mapEdit.removeElementToAdd("cat");
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      assertTrue($mapEdit.getElementsToAdd().isEmpty());
      String keyA1 = "cow";
      Integer valueA1 = 1;
      $mapEdit.addElementToAdd(keyA1, valueA1);
      assertTrue($mapEdit.getElementsToAdd().size() == 1);
      assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
      assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
      String keyA2 = "horse";
      Integer valueA2 = 2;
      $mapEdit.addElementToAdd(keyA2, valueA2);
      assertTrue($mapEdit.getElementsToAdd().size() == 2);
      assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
      assertTrue($mapEdit.getElementsToAdd().containsKey(keyA2));
      assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
      assertEquals(valueA2, $mapEdit.getElementsToAdd().get(keyA2));
      $mapEdit.removeElementToAdd(keyA1);
      assertTrue($mapEdit.getElementsToAdd().size() == 1);
      assertTrue($mapEdit.getElementsToAdd().containsKey(keyA2));
      assertEquals(valueA2, $mapEdit.getElementsToAdd().get(keyA2));
      $mapEdit.removeElementToAdd(keyA2);
      assertTrue($mapEdit.getElementsToAdd().isEmpty());
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
      $mapEdit.addElementToAdd("cow", 1);
      $mapEdit.perform();
      $mapEdit.addElementToRemove("horse", 2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.addElementToAdd("dog", 1);
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.addElementToRemove("horse", 2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.addElementToAdd("dog", 1);
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.redo();
      $mapEdit.addElementToRemove("cat", 2);
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      assertTrue($mapEdit.getElementsToRemove().isEmpty());
      String keyR1 = "horse";
      Integer valueR1 = 1;
      $mapEdit.addElementToRemove(keyR1, valueR1);
      assertTrue($mapEdit.getElementsToRemove().size() == 1);
      assertTrue($mapEdit.getElementsToRemove().containsKey(keyR1));
      assertEquals(valueR1, $mapEdit.getElementsToRemove().get(keyR1));
      String keyR2 = "cow";
      Integer valueR2 = 2;
      $mapEdit.addElementToRemove(keyR2, valueR2);
      assertTrue($mapEdit.getElementsToRemove().size() == 2);
      assertTrue($mapEdit.getElementsToRemove().containsKey(keyR1));
      assertTrue($mapEdit.getElementsToRemove().containsKey(keyR2));
      assertEquals(valueR1, $mapEdit.getElementsToRemove().get(keyR1));
      assertEquals(valueR2, $mapEdit.getElementsToRemove().get(keyR2));
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
      $mapEdit.addElementToAdd("cow", 1);
      $mapEdit.perform();
      $mapEdit.removeElementToRemove("horse");
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.addElementToAdd("cow", 1);
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.removeElementToRemove("horse");
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      $mapEdit.addElementToAdd("cow", 1);
      $mapEdit.perform();
      $mapEdit.undo();
      $mapEdit.redo();
      $mapEdit.removeElementToRemove("horse");
      // should not be reached
      assertTrue(false);
    }
    catch (EditStateException e) {
      assertEquals(e.getEdit(), $mapEdit);
      assertEquals(e.getCurrentState(), $mapEdit.getState());
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
      assertTrue($mapEdit.getElementsToRemove().isEmpty());
      String keyR1 = "duck";
      Integer valueR1 = 1;
      $mapEdit.addElementToRemove(keyR1, valueR1);
      assertTrue($mapEdit.getElementsToRemove().size() == 1);
      assertTrue($mapEdit.getElementsToRemove().containsKey(keyR1));
      assertEquals(valueR1, $mapEdit.getElementsToRemove().get(keyR1));
      String keyR2 = "chicken";
      Integer valueR2 = 2;
      $mapEdit.addElementToRemove(keyR2, valueR2);
      assertTrue($mapEdit.getElementsToRemove().size() == 2);
      assertTrue($mapEdit.getElementsToRemove().containsKey(keyR1));
      assertTrue($mapEdit.getElementsToRemove().containsKey(keyR2));
      assertEquals(valueR1, $mapEdit.getElementsToRemove().get(keyR1));
      assertEquals(valueR2, $mapEdit.getElementsToRemove().get(keyR2));
      $mapEdit.removeElementToRemove(keyR1);
      assertTrue($mapEdit.getElementsToRemove().size() == 1);
      assertTrue($mapEdit.getElementsToRemove().containsKey(keyR2));
      assertEquals(valueR2, $mapEdit.getElementsToRemove().get(keyR2));
      $mapEdit.removeElementToRemove(keyR2);
      assertTrue($mapEdit.getElementsToRemove().isEmpty());
    }
    catch (EditStateException e) {
      // should not be reached
      assertTrue(false);
    }
  }

  @Test
  public void getAddedElements() throws EditStateException, IllegalEditException {
    // set value of target
    String initKey = "dog";
    Integer initValue = 3;
    MyMapEdit edit = new MyMapEdit($target);
    edit.addElementToAdd(initKey, initValue);
    edit.perform();
    // check
    assertEquals($mapEdit.getState(), State.NOT_YET_PERFORMED);
    assertTrue($mapEdit.getElementsToAdd().isEmpty());
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getAddedElements().isEmpty());
    // set goal
    String keyA1 = "cat";
    Integer valueA1 = 5;
    $mapEdit.addElementToAdd(keyA1, valueA1);
    assertEquals($mapEdit.getState(), State.NOT_YET_PERFORMED);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getAddedElements().isEmpty());
    // perform
    $mapEdit.perform();
    assertEquals($mapEdit.getState(), State.DONE);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getAddedElements().size() == 1);
    assertTrue($mapEdit.getAddedElements().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getAddedElements().get(keyA1));
    // undo
    $mapEdit.undo();
    assertEquals($mapEdit.getState(), State.UNDONE);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getAddedElements().isEmpty());
    // redo
    $mapEdit.redo();
    assertEquals($mapEdit.getState(), State.DONE);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getAddedElements().size() == 1);
    assertTrue($mapEdit.getAddedElements().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getAddedElements().get(keyA1));
    // kill
    $mapEdit.kill();
    assertEquals($mapEdit.getState(), State.DEAD);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getAddedElements().isEmpty());
  }

  @Test
  public void getRemovedElements() throws EditStateException, IllegalEditException {
    // set value of target
    String initKey = "frog";
    Integer initValue = 3;
    MyMapEdit edit = new MyMapEdit($target);
    edit.addElementToAdd(initKey, initValue);
    edit.perform();
    // check
    assertEquals($mapEdit.getState(), State.NOT_YET_PERFORMED);
    assertTrue($mapEdit.getElementsToAdd().isEmpty());
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getRemovedElements().isEmpty());
    // set goal
    String keyA = "fish";
    Integer valueA = 5;
    $mapEdit.addElementToAdd(keyA, valueA);
    assertEquals($mapEdit.getState(), State.NOT_YET_PERFORMED);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA));
    assertEquals(valueA, $mapEdit.getElementsToAdd().get(keyA));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getRemovedElements().size() == 1);
    assertTrue($mapEdit.getRemovedElements().containsKey(keyA));
    assertEquals(valueA, $mapEdit.getRemovedElements().get(keyA));
    // perform
    $mapEdit.perform();
    assertEquals($mapEdit.getState(), State.DONE);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA));
    assertEquals(valueA, $mapEdit.getElementsToAdd().get(keyA));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getRemovedElements().isEmpty());
    // undo
    $mapEdit.undo();
    assertEquals($mapEdit.getState(), State.UNDONE);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA));
    assertEquals(valueA, $mapEdit.getElementsToAdd().get(keyA));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getRemovedElements().size() == 1);
    assertTrue($mapEdit.getRemovedElements().containsKey(keyA));
    assertEquals(valueA, $mapEdit.getRemovedElements().get(keyA));
    // redo
    $mapEdit.redo();
    assertEquals($mapEdit.getState(), State.DONE);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA));
    assertEquals(valueA, $mapEdit.getElementsToAdd().get(keyA));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getRemovedElements().isEmpty());
    // kill
    $mapEdit.kill();
    assertEquals($mapEdit.getState(), State.DEAD);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA));
    assertEquals(valueA, $mapEdit.getElementsToAdd().get(keyA));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.getRemovedElements().size() == 1);
    assertTrue($mapEdit.getRemovedElements().containsKey(keyA));
    assertEquals(valueA, $mapEdit.getRemovedElements().get(keyA));
  }


  /**
   * When doing a perform, is the initial state stored appropriately?
   */
  @Test
  public void storeInitialState() throws EditStateException, IllegalEditException {
    assertTrue($target.keySet().isEmpty());
    assertTrue($mapEdit.getElementsToAdd().isEmpty());
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    $mapEdit.perform();
    assertTrue($target.keySet().isEmpty());
    assertTrue($mapEdit.getElementsToAdd().isEmpty());
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    // change value of target
    String key1 = "pig";
    Integer value1 = 1;
    String key2 = "frog";
    Integer value2 = 2;
    String key3 = "fly";
    Integer value3 = 3;
    String key4 = "bee";
    Integer value4 = 4;
    MyMapEdit mapEdit = new MyMapEdit($target);
    mapEdit.addElementToAdd(key1, value1);
    mapEdit.addElementToAdd(key2, value2);
    mapEdit.addElementToAdd(key3, value3);
    mapEdit.addElementToAdd(key4, value4);
    mapEdit.perform();
    assertTrue($target.keySet().size() == 4);
    assertTrue($target.keySet().contains(key1));
    assertTrue($target.keySet().contains(key2));
    assertTrue($target.keySet().contains(key3));
    assertTrue($target.keySet().contains(key4));
    assertEquals(value1, $target.get(key1));
    assertEquals(value2, $target.get(key2));
    assertEquals(value3, $target.get(key3));
    assertEquals(value4, $target.get(key4));
    // add elements to edit
    String key5 = "cow";
    String key6 = "horse";
    Integer value = 444;
    $mapEdit = new MyMapEdit($target);
    $mapEdit.addElementToAdd(key1, value);
    $mapEdit.addElementToAdd(key5, value);
    $mapEdit.addElementToRemove(key3, value);
    $mapEdit.addElementToRemove(key6, value);
    assertTrue($mapEdit.getElementsToAdd().size() == 2);
    assertTrue($mapEdit.getElementsToAdd().containsKey(key1));
    assertTrue($mapEdit.getElementsToAdd().containsKey(key5));
    assertEquals(value, $mapEdit.getElementsToAdd().get(key1));
    assertEquals(value, $mapEdit.getElementsToAdd().get(key5));
    assertTrue($mapEdit.getElementsToRemove().size() == 2);
    assertTrue($mapEdit.getElementsToRemove().containsKey(key3));
    assertTrue($mapEdit.getElementsToRemove().containsKey(key6));
    assertEquals(value, $mapEdit.getElementsToRemove().get(key3));
    assertEquals(value, $mapEdit.getElementsToRemove().get(key6));
    // store initial state
    $mapEdit.perform(); // added = {key1,key5} \ {key1,key,key3,key4},
                        // removed = {key3,key6} intersection {key1,key2,key3,key4}
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(key5));
    assertTrue($mapEdit.getElementsToRemove().size() == 1);
    assertEquals(value, $mapEdit.getElementsToAdd().get(key5));
    assertTrue($mapEdit.getElementsToRemove().containsKey(key3));
    assertEquals(value, $mapEdit.getElementsToRemove().get(key3));
  }

  @Test
  public void isChange() throws EditStateException, IllegalEditException {
    assertTrue($mapEdit.getElementsToAdd().isEmpty());
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertFalse($mapEdit.isChange());
    // change elements to add and to remove
    String keyA1 = "horse";
    Integer valueA1 = 2;
    $mapEdit.addElementToAdd(keyA1, valueA1);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.isChange());
    // change elements to add and to remove
    String keyR1 = "cow";
    Integer valueR1 = 3;
    $mapEdit.addElementToRemove(keyR1, valueR1);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().size() == 1);
    assertTrue($mapEdit.getElementsToRemove().containsKey(keyR1));
    assertEquals(valueR1, $mapEdit.getElementsToRemove().get(keyR1));
    assertTrue($mapEdit.isChange());
    // change elements to add and to remove
    $mapEdit.removeElementToRemove(keyR1);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($mapEdit.isChange());
    // change elements to add and to remove
    $mapEdit.removeElementToAdd(keyA1);
    assertTrue($mapEdit.getElementsToAdd().isEmpty());
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertFalse($mapEdit.isChange());
  }

  @Test
  public void isInitialStateCurrent() throws EditStateException, IllegalEditException {
    assertTrue($mapEdit.getElementsToAdd().isEmpty());
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($target.keySet().isEmpty());
    // edit = {}, {}
    // target = {}
    assertTrue($mapEdit.isInitialStateCurrent());
    // change elements to add and remove
    String keyA1 = "horse";
    Integer valueA1 = 2;
    String keyA2 = "cow";
    Integer valueA2 = 3;
    String keyR1 = "frog";
    Integer valueR1 = 4;
    $mapEdit.addElementToAdd(keyA1, valueA1);
    $mapEdit.addElementToRemove(keyR1, valueR1);
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToRemove().containsKey(keyR1));
    assertEquals(valueR1, $mapEdit.getElementsToRemove().get(keyR1));
    assertTrue($target.keySet().isEmpty());
    // edit = {A1}, {R1}
    // target = {}
    assertFalse($mapEdit.isInitialStateCurrent());
    // change target
    MyMapEdit mapEdit = new MyMapEdit($target);
    mapEdit.addElementToAdd(keyA1, valueA1);
    mapEdit.addElementToAdd(keyR1, valueR1);
    mapEdit.addElementToAdd(keyA2, valueA2);
    mapEdit.perform();
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToRemove().containsKey(keyR1));
    assertEquals(valueR1, $mapEdit.getElementsToRemove().get(keyR1));
    assertTrue($target.keySet().size() == 3);
    assertTrue($target.keySet().contains(keyA1));
    assertTrue($target.keySet().contains(keyA2));
    assertTrue($target.keySet().contains(keyR1));
    assertEquals(valueA1, $target.get(keyA1));
    assertEquals(valueA2, $target.get(keyA2));
    assertEquals(valueR1, $target.get(keyR1));
    // edit = {A1}, {R1}
    // target = {A1, A2, R1}
    assertFalse($mapEdit.isInitialStateCurrent());
    // change target
    MyMapEdit mapEdit2 = new MyMapEdit($target);
    mapEdit2.addElementToRemove(keyA1, valueA1);
    mapEdit2.perform();
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToRemove().containsKey(keyR1));
    assertEquals(valueR1, $mapEdit.getElementsToRemove().get(keyR1));
    assertTrue($target.keySet().size() == 2);
    assertTrue($target.keySet().contains(keyA2));
    assertTrue($target.keySet().contains(keyR1));
    assertEquals(valueA2, $target.get(keyA2));
    assertEquals(valueR1, $target.get(keyR1));
    // edit = {A1}, {R1}
    // target = {A2, R1}
    assertTrue($mapEdit.isInitialStateCurrent());
  }

  @Test
  public void isGoalStateCurrent() throws EditStateException, IllegalEditException {
    assertTrue($mapEdit.getElementsToAdd().isEmpty());
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($target.keySet().isEmpty());
    // edit = {}, {}
    // target = {}
    assertTrue($mapEdit.isGoalStateCurrent());
    // perform
    String keyA1 = "butterfly";
    Integer valueA1 = 2;
    String keyR1 = "fox";
    Integer valueR1 = 3;
    $mapEdit.addElementToAdd(keyA1, valueA1);
    $mapEdit.addElementToRemove(keyR1, valueR1);
    $mapEdit.perform();
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($target.keySet().size() == 1);
    assertTrue($target.keySet().contains(keyA1));
    assertEquals(valueA1, $target.get(keyA1));
    // edit = {A1}, {}
    // target = {A1}
    assertTrue($mapEdit.isGoalStateCurrent());
    // undo
    $mapEdit.undo();
    assertTrue($mapEdit.getElementsToAdd().size() == 1);
    assertTrue($mapEdit.getElementsToAdd().containsKey(keyA1));
    assertEquals(valueA1, $mapEdit.getElementsToAdd().get(keyA1));
    assertTrue($mapEdit.getElementsToRemove().isEmpty());
    assertTrue($target.keySet().isEmpty());
    // edit = {A1}, {}
    // target = {}
    assertFalse($mapEdit.isGoalStateCurrent());
  }

  @Test
  public void createEvent() throws EditStateException, IllegalEditException {
    assertEquals(State.NOT_YET_PERFORMED, $mapEdit.getState());
    // perform
    String keyA1 = "cow";
    Integer valueA1 = 1;
    String keyR1 = "horse";
    Integer valueR1 = 2;
    $mapEdit.addElementToAdd(keyA1, valueA1);
    $mapEdit.addElementToRemove(keyR1, valueR1);
    $mapEdit.perform();
    // create event
    MapEvent<String, Integer> createdEvent = $mapEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $mapEdit);
    assertTrue(createdEvent.getAddedElements().size() == 1);
    assertTrue(createdEvent.getAddedElements().containsKey(keyA1));
    assertEquals(valueA1, createdEvent.getAddedElements().get(keyA1));
    assertTrue(createdEvent.getRemovedElements().isEmpty());
    assertEquals(createdEvent.getSource(), $target);
    // undo
    $mapEdit.undo();
    // create event
    createdEvent = $mapEdit.createEvent();
    assertEquals(createdEvent.getEdit(), $mapEdit);
    assertTrue(createdEvent.getAddedElements().isEmpty());
    assertTrue(createdEvent.getRemovedElements().size() == 1);
    assertTrue(createdEvent.getRemovedElements().containsKey(keyA1));
    assertEquals(valueA1, createdEvent.getRemovedElements().get(keyA1));
    assertEquals(createdEvent.getSource(), $target);
  }

}