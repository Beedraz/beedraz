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

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit.State;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerBeed;
import org.beedra_II.property.integer.IntegerEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractEdit {

  public class MyAbstractEdit
       extends AbstractEdit<EditableIntegerBeed, IntegerEvent> {

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
    protected void fireEvent(IntegerEvent event) {
      $event = event;
    }

    public IntegerEvent $event;

    @Override
    protected boolean isChange() {
      return $b;
    }

    public void setChange(boolean b) {
      $b = b;
    }

    private boolean $b = true;

    @Override
    protected boolean isGoalStateCurrent() {
      return true;
    }

    @Override
    protected boolean isInitialStateCurrent() {
      return false;
    }

    @Override
    protected void performance() {
      // NOP
    }

    @Override
    protected void storeInitialState() {
      // NOP
    }

    @Override
    protected void unperformance() {
    }

    protected boolean isAcceptable() {
      return $acceptable;
    }

    public void setAcceptable(boolean b) {
      $acceptable = b;
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
    $edit.addListener($listener1);
    $edit.addListener($listener2);
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
      $edit.addListener($listener1);
      $edit.addListener($listener2);
      assertTrue($edit.isValidityListener($listener1));
      assertTrue($edit.isValidityListener($listener2));
      // perform
      $edit.setChange(true);
      $edit.perform();
      // listeners should be removed
      assertFalse($edit.isValidityListener($listener1));
      assertFalse($edit.isValidityListener($listener2));
      // listeners of the beed are notified
      assertNotNull($edit.$event);
      assertEquals($edit.$event, $edit.getCreatedEvent());
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
  public void perform6() {
    try {
      $edit.addListener($listener1);
      $edit.addListener($listener2);
      assertTrue($edit.isValidityListener($listener1));
      assertTrue($edit.isValidityListener($listener2));
      // perform
      $edit.setChange(false);
      $edit.perform();
      // listeners are not removed
      assertTrue($edit.isValidityListener($listener1));
      assertTrue($edit.isValidityListener($listener2));
      // listeners of the beed are not notified
      assertNull($edit.$event);
//      assertTrue("When the edit causes no change, the validity listeners are " +
//          "not removed and the beed listeners are not notified. The latter is " +
//          "correct, but what about the first?", false);
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

}
