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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Listener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestIntegerSumBeed {

  public class MyIntegerSumBeed extends IntegerSumBeed {
    public MyIntegerSumBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(IntegerEvent event) {
      fireChangeEvent(event);
    }
  }

  public class PropagatedEventListener implements Listener<PropagatedEvent> {

    public void beedChanged(PropagatedEvent event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public PropagatedEvent $event;

  }

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner = new MyBeanBeed();
  private MyIntegerSumBeed $integerSumBeed = new MyIntegerSumBeed($owner);
  private IntegerEvent $event1 = new IntegerEvent($integerSumBeed, new Integer(0), new Integer(1), null);
      // @mudo Laatste argument mag niet null zijn??
  private PropagatedEventListener $listener1 = new PropagatedEventListener();
  private PropagatedEventListener $listener2 = new PropagatedEventListener();

  @Test
  public void constructor() {
    assertEquals($integerSumBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $integerSumBeed.fire($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertTrue($listener1.$event instanceof PropagatedEvent);
    assertTrue($listener2.$event instanceof PropagatedEvent);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
    // the value should be 0
    assertEquals($integerSumBeed.get(), 0);
    // no terms registered (cannot be tested)
  }

  @Test
  public void isTerm() {
    // NOP: basic
  }

  @Test
  public void addTerm1() throws EditStateException, IllegalEditException {
    // create terms
    EditableIntegerBeed term5 = createEditableIntegerBeed(5);
    EditableIntegerBeed termNull = createEditableIntegerBeed(null);
    EditableIntegerBeed term30 = createEditableIntegerBeed(30);
    // add term5
    $integerSumBeed.addTerm(term5);
    // check (sum = 5)
    assertTrue($integerSumBeed.isTerm(term5));
    assertEquals($integerSumBeed.get(), 5);
    // add term30
    $integerSumBeed.addTerm(term30);
    // check (sum = 5 + 30)
    assertTrue($integerSumBeed.isTerm(term5));
    assertTrue($integerSumBeed.isTerm(term30));
    assertEquals($integerSumBeed.get(), 35);
    // add termNull
    $integerSumBeed.addTerm(termNull);
    // check (sum = 5 + 30 + null)
    assertTrue($integerSumBeed.isTerm(term5));
    assertTrue($integerSumBeed.isTerm(term30));
    assertTrue($integerSumBeed.isTerm(termNull));
    assertEquals($integerSumBeed.get(), null);
    // remove terms
    $integerSumBeed.removeTerm(term5);
    $integerSumBeed.removeTerm(term30);
    $integerSumBeed.removeTerm(termNull);
    // check (sum has no terms)
    assertTrue(!$integerSumBeed.isTerm(term5));
    assertTrue(!$integerSumBeed.isTerm(term30));
    assertTrue(!$integerSumBeed.isTerm(termNull));
    assertEquals($integerSumBeed.get(), 0);
    // add termNull
    $integerSumBeed.addTerm(termNull);
    // check (sum = null)
    assertTrue($integerSumBeed.isTerm(termNull));
    assertEquals($integerSumBeed.get(), null);
    // add term
    $integerSumBeed.addTerm(term5);
    // check (sum = null + 5)
    assertTrue($integerSumBeed.isTerm(termNull));
    assertTrue($integerSumBeed.isTerm(term5));
    assertEquals($integerSumBeed.get(), null);
    // add term
    $integerSumBeed.addTerm(term30);
    // check (sum = null + 5 + 30)
    assertTrue($integerSumBeed.isTerm(termNull));
    assertTrue($integerSumBeed.isTerm(term5));
    assertTrue($integerSumBeed.isTerm(term30));
    assertEquals($integerSumBeed.get(), null);

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of IntegerSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change null to 7
    IntegerEdit editNull = new IntegerEdit(termNull);
    Integer value7 = 7;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(termNull.get(), value7);
    // check (sum = 7 + 5 + 30)
    assertTrue($integerSumBeed.isTerm(termNull));
    assertTrue($integerSumBeed.isTerm(term5));
    assertTrue($integerSumBeed.isTerm(term30));
    assertEquals($integerSumBeed.get(), 42);
    // change 30 to 3
    IntegerEdit edit30 = new IntegerEdit(term30);
    Integer value3 = 3;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 7 + 5 + 3)
    assertTrue($integerSumBeed.isTerm(termNull));
    assertTrue($integerSumBeed.isTerm(term5));
    assertTrue($integerSumBeed.isTerm(term30));
    assertEquals($integerSumBeed.get(), 15);
  }

  @Test
  public void addTerm2() throws EditStateException, IllegalEditException {
    // create terms
    EditableIntegerBeed term5 = createEditableIntegerBeed(5);
    EditableIntegerBeed termNull = createEditableIntegerBeed(null);
    EditableIntegerBeed term30 = createEditableIntegerBeed(30);
    // add term5
    $integerSumBeed.addTerm(term5);
    // check (sum = 5)
    assertTrue($integerSumBeed.isTerm(term5));
    assertEquals($integerSumBeed.get(), 5);
    // create another sum beed
    IntegerSumBeed integerSumBeed2 = new IntegerSumBeed($owner);
    // check (sum has no terms)
    assertEquals(integerSumBeed2.get(), 0);
    // add term30
    integerSumBeed2.addTerm(term30);
    // check (sum = 30)
    assertTrue(integerSumBeed2.isTerm(term30));
    assertEquals(integerSumBeed2.get(), 30);
    // add termNull
    integerSumBeed2.addTerm(termNull);
    // check (sum = 30 + null)
    assertTrue(integerSumBeed2.isTerm(term30));
    assertTrue(integerSumBeed2.isTerm(termNull));
    assertEquals(integerSumBeed2.get(), null);
    // add integerSumBeed2
    $integerSumBeed.addTerm(integerSumBeed2);
    // check (sum = 5 + (30 + null))
    assertTrue($integerSumBeed.isTerm(term5));
    assertTrue($integerSumBeed.isTerm(integerSumBeed2));
    assertEquals($integerSumBeed.get(), null);

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of IntegerSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 5 to 2
    IntegerEdit edit5 = new IntegerEdit(term5);
    Integer value2 = 2;
    edit5.setGoal(value2);
    edit5.perform();
    assertEquals(term5.get(), value2);
    // check (sum = 2 + (30 + null))
    assertTrue(integerSumBeed2.isTerm(term30));
    assertTrue(integerSumBeed2.isTerm(termNull));
    assertEquals(integerSumBeed2.get(), null);
    assertTrue($integerSumBeed.isTerm(term5));
    assertTrue($integerSumBeed.isTerm(integerSumBeed2));
    assertEquals($integerSumBeed.get(), null);
    // change null to 7
    IntegerEdit editNull = new IntegerEdit(termNull);
    Integer value7 = 7;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(termNull.get(), value7);
    // check (sum = 2 + (30 + 7))
    assertTrue(integerSumBeed2.isTerm(term30));
    assertTrue(integerSumBeed2.isTerm(termNull));
    assertEquals(integerSumBeed2.get(), 37);
    assertTrue($integerSumBeed.isTerm(term5));
    assertTrue($integerSumBeed.isTerm(integerSumBeed2));
    assertEquals($integerSumBeed.get(), 39);
    // change 30 to 3
    IntegerEdit edit30 = new IntegerEdit(term30);
    Integer value3 = 3;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 2 + (3 + 7))
    assertTrue($integerSumBeed.isTerm(term5));
    assertTrue($integerSumBeed.isTerm(integerSumBeed2));
    assertEquals($integerSumBeed.get(), 12);
  }

  private EditableIntegerBeed createEditableIntegerBeed(Integer value) {
    try {
      EditableIntegerBeed editableIntegerBeed = new EditableIntegerBeed($owner);
      IntegerEdit edit = new IntegerEdit(editableIntegerBeed);
      edit.setGoal(value);
      edit.perform();
      assertEquals(editableIntegerBeed.get(), value);
      return editableIntegerBeed;
    }
    catch (EditStateException e) {
      assertTrue(false);
      return null;
    }
    catch (IllegalEditException e) {
      assertTrue(false);
      return null;
    }
  }

}
