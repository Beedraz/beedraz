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
import static org.ppeew.smallfries.MathUtil.equalValue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.StubListener;
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

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner = new StubBeanBeed();
  private MyIntegerSumBeed $integerSumBeed = new MyIntegerSumBeed($owner);
  private IntegerEvent $event1 = new ActualIntegerEvent($integerSumBeed, new Integer(0), new Integer(1), null);
      // @mudo Laatste argument mag niet null zijn??
  private StubListener<PropagatedEvent> $listener1 = new StubListener<PropagatedEvent>();
  private StubListener<PropagatedEvent> $listener2 = new StubListener<PropagatedEvent>();

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
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
    // the value should be 0
    assertEquals($integerSumBeed.getInteger(), 0);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // no terms registered (cannot be tested)
  }

  @Test
  public void getNbOccurrences() {
    // NOP: basic
  }

  //@Test
  // one-level sum
  public void addTerm1() throws EditStateException, IllegalEditException {
    // create terms
    EditableIntegerBeed term5 = createEditableIntegerBeed(5);
    EditableIntegerBeed termNull = createEditableIntegerBeed(null);
    EditableIntegerBeed term30 = createEditableIntegerBeed(30);
    // add term5
    $integerSumBeed.addTerm(term5);
    // check (sum = 5)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($integerSumBeed.getInteger(), 5);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add term30
    $integerSumBeed.addTerm(term30);
    // check (sum = 5 + 30)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($integerSumBeed.getInteger(), 35);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add termNull
    $integerSumBeed.addTerm(termNull);
    // check (sum = 5 + 30 + null)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 1);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove terms
    $integerSumBeed.removeTerm(term5);
    $integerSumBeed.removeTerm(term30);
    $integerSumBeed.removeTerm(termNull);
    // check (sum has no terms)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 0);
    assertEquals($integerSumBeed.getInteger(), 0);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add termNull
    $integerSumBeed.addTerm(termNull);
    // check (sum = null)
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 1);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add term
    $integerSumBeed.addTerm(term5);
    // check (sum = null + 5)
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add term
    $integerSumBeed.addTerm(term30);
    // check (sum = null + 5 + 30)
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));

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
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($integerSumBeed.getInteger(), 42);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change 30 to 3
    IntegerEdit edit30 = new IntegerEdit(term30);
    Integer value3 = 3;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 7 + 5 + 3)
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($integerSumBeed.getInteger(), 15);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
  }

  @Test
  // two-level sum
  public void addTerm2() throws EditStateException, IllegalEditException {
    // create terms
    EditableIntegerBeed term5 = createEditableIntegerBeed(5);
    EditableIntegerBeed termNull = createEditableIntegerBeed(null);
    EditableIntegerBeed term30 = createEditableIntegerBeed(30);
    // add term5
    $integerSumBeed.addTerm(term5);
    // check (sum = 5)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($integerSumBeed.getInteger(), 5);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // create another sum beed
    IntegerSumBeed integerSumBeed2 = new IntegerSumBeed($owner);
    // check (sum has no terms)
    assertEquals(integerSumBeed2.getInteger(), 0);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getInteger()));
    // add term30
    integerSumBeed2.addTerm(term30);
    // check (sum = 30)
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals(integerSumBeed2.getInteger(), 30);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getInteger()));
    // add termNull
    integerSumBeed2.addTerm(termNull);
    // check (sum = 30 + null)
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(integerSumBeed2.getInteger(), null);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getInteger()));
    // add integerSumBeed2
    $integerSumBeed.addTerm(integerSumBeed2);
    // check (sum = 5 + (30 + null))
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));

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
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(integerSumBeed2.getInteger(), null);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getInteger()));
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change null to 7
    IntegerEdit editNull = new IntegerEdit(termNull);
    Integer value7 = 7;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(termNull.get(), value7);
    // check (sum = 2 + (30 + 7))
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(integerSumBeed2.getInteger(), 37);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getInteger()));
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertEquals($integerSumBeed.getInteger(), 39);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change 30 to 3
    IntegerEdit edit30 = new IntegerEdit(term30);
    Integer value3 = 3;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 2 + (3 + 7))
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertEquals($integerSumBeed.getInteger(), 12);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
  }

  //@Test
  // three-level sum
  public void addTerm3() throws EditStateException, IllegalEditException {
    // create terms
    EditableIntegerBeed term1 = createEditableIntegerBeed(1);
    EditableIntegerBeed term2 = createEditableIntegerBeed(2);
    EditableIntegerBeed term3 = createEditableIntegerBeed(3);
    EditableIntegerBeed term4 = createEditableIntegerBeed(4);
    // create another sum beed
    IntegerSumBeed integerSumBeed2plus3 = new IntegerSumBeed($owner);
    integerSumBeed2plus3.addTerm(term2);
    integerSumBeed2plus3.addTerm(term3);
    // check (sum = 2 + 3)
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals(integerSumBeed2plus3.getInteger(), 5);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getInteger()));
    // create another sum beed
    IntegerSumBeed integerSumBeed2plus3plus4 = new IntegerSumBeed($owner);
    integerSumBeed2plus3plus4.addTerm(integerSumBeed2plus3);
    integerSumBeed2plus3plus4.addTerm(term4);
    // check (sum = (2 + 3) + 4)
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertEquals(integerSumBeed2plus3plus4.getInteger(), 9);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getInteger()));
    // add terms
    $integerSumBeed.addTerm(term1);
    $integerSumBeed.addTerm(integerSumBeed2plus3plus4);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals($integerSumBeed.getInteger(), 10);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of IntegerSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 1 to null
    IntegerEdit edit1 = new IntegerEdit(term1);
    Integer valueNull = null;
    edit1.setGoal(valueNull);
    edit1.perform();
    assertEquals(term1.get(), valueNull);
    // check (sum = null + ((2 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getInteger(), 5);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getInteger()));
    assertEquals(integerSumBeed2plus3plus4.getInteger(), 9);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getInteger()));
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change null back to 1
    edit1 = new IntegerEdit(term1);
    Integer value1 = 1;
    edit1.setGoal(value1);
    edit1.perform();
    assertEquals(term1.get(), value1);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getInteger(), 5);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getInteger()));
    assertEquals(integerSumBeed2plus3plus4.getInteger(), 9);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getInteger()));
    assertEquals($integerSumBeed.getInteger(), 10);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change 2 to null
    IntegerEdit edit2 = new IntegerEdit(term2);
    edit2.setGoal(valueNull);
    edit2.perform();
    assertEquals(term2.get(), valueNull);
    // check (sum = 1 + ((null + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getInteger(), null);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getInteger()));
    assertEquals(integerSumBeed2plus3plus4.getInteger(), null);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getInteger()));
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change null back to 2
    edit2 = new IntegerEdit(term2);
    Integer value2 = 2;
    edit2.setGoal(value2);
    edit2.perform();
    assertEquals(term2.get(), value2);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getInteger(), 5);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getInteger()));
    assertEquals(integerSumBeed2plus3plus4.getInteger(), 9);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getInteger()));
    assertEquals($integerSumBeed.getInteger(), 10);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change 4 to null
    IntegerEdit edit4 = new IntegerEdit(term4);
    edit4.setGoal(valueNull);
    edit4.perform();
    assertEquals(term4.get(), valueNull);
    // check (sum = 1 + ((2 + 3) + null))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getInteger(), 5);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getInteger()));
    assertEquals(integerSumBeed2plus3plus4.getInteger(), null);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getInteger()));
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change null back to 4
    edit4 = new IntegerEdit(term4);
    Integer value4 = 4;
    edit4.setGoal(value4);
    edit4.perform();
    assertEquals(term4.get(), value4);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getInteger(), 5);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getInteger()));
    assertEquals(integerSumBeed2plus3plus4.getInteger(), 9);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getInteger()));
    assertEquals($integerSumBeed.getInteger(), 10);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));

    // change 2 to 11
    edit2 = new IntegerEdit(term2);
    Integer value11 = 11;
    edit2.setGoal(value11);
    edit2.perform();
    assertEquals(term2.get(), value11);
    // check (sum = 1 + ((11 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getInteger(), 14);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getInteger()));
    assertEquals(integerSumBeed2plus3plus4.getInteger(), 18);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getInteger()));
    assertEquals($integerSumBeed.getInteger(), 19);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
  }

  @Test
  // sum containing twice the same term
  public void addTerm4() throws EditStateException, IllegalEditException {
    // create terms
    EditableIntegerBeed term5 = createEditableIntegerBeed(5);
    // add term5
    $integerSumBeed.addTerm(term5);
    // check (sum = 5)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($integerSumBeed.getInteger(), 5);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add term5
    $integerSumBeed.addTerm(term5);
    // check (sum = 5 + 5)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($integerSumBeed.getInteger(), 10);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of IntegerSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 5 to 7
    IntegerEdit edit5 = new IntegerEdit(term5);
    Integer value7 = 7;
    edit5.setGoal(value7);
    edit5.perform();
    assertEquals(term5.get(), value7);
    // check (sum = 7 + 7)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($integerSumBeed.getInteger(), 14);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change 7 to 11
    edit5 = new IntegerEdit(term5);
    Integer value11 = 11;
    edit5.setGoal(value11);
    edit5.perform();
    assertEquals(term5.get(), value11);
    // check (sum = 11 + 11)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($integerSumBeed.getInteger(), 22);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // change 11 to null
    edit5 = new IntegerEdit(term5);
    Integer valueNull = null;
    edit5.setGoal(valueNull);
    edit5.perform();
    assertEquals(term5.get(), valueNull);
    // check (sum = null + null)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));

    edit5 = new IntegerEdit(term5);
    edit5.setGoal(value7);
    edit5.perform();
    assertEquals(term5.get(), value7);
    // check (sum = 7 + 7)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($integerSumBeed.getInteger(), 14);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
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

  @Test
  // one-level sum
  public void removeTerm1() throws EditStateException, IllegalEditException {
    // create terms
    EditableIntegerBeed term5 = createEditableIntegerBeed(5);
    EditableIntegerBeed termNull = createEditableIntegerBeed(null);
    EditableIntegerBeed term30 = createEditableIntegerBeed(30);
    EditableIntegerBeed term11 = createEditableIntegerBeed(11);
    // add terms
    $integerSumBeed.addTerm(term5);
    $integerSumBeed.addTerm(term5);
    $integerSumBeed.addTerm(term5);
    $integerSumBeed.addTerm(termNull);
    $integerSumBeed.addTerm(termNull);
    $integerSumBeed.addTerm(term30);
    // check (sum = 5 + 5 + 5 + null + null + 30)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 3);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 2);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 11 (not in the sum)
    $integerSumBeed.removeTerm(term11);
    // check (sum = 5 + 5 + 5 + null + null + 30)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 3);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 2);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 5
    $integerSumBeed.removeTerm(term5);
    // check (sum = 5 + 5 + null + null + 30)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 2);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove null
    $integerSumBeed.removeTerm(termNull);
    // check (sum = 5 + 5 + null + 30)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove null
    $integerSumBeed.removeTerm(termNull);
    // check (sum = 5 + 5 + 30)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($integerSumBeed.getInteger(), 40);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 30
    $integerSumBeed.removeTerm(term30);
    // check (sum = 5 + 5)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($integerSumBeed.getInteger(), 10);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 5
    $integerSumBeed.removeTerm(term5);
    // check (sum = 5)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($integerSumBeed.getInteger(), 5);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 5
    $integerSumBeed.removeTerm(term5);
    // check (sum = no items)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($integerSumBeed.getInteger(), 0);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 5 (not in sum)
    $integerSumBeed.removeTerm(term5);
    // check (sum = no items)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($integerSumBeed.getInteger(), 0);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
  }

  @Test
  // two-level sum
  public void removeTerm2() throws EditStateException, IllegalEditException {
    // create terms
    EditableIntegerBeed term5 = createEditableIntegerBeed(5);
    EditableIntegerBeed termNull = createEditableIntegerBeed(null);
    EditableIntegerBeed term30 = createEditableIntegerBeed(30);
    // create sum beed: 5 + (30 + null)
    $integerSumBeed.addTerm(term5);
    IntegerSumBeed integerSumBeed2 = new IntegerSumBeed($owner);
    integerSumBeed2.addTerm(term30);
    integerSumBeed2.addTerm(termNull);
    $integerSumBeed.addTerm(integerSumBeed2);
    // check (sum = 5 + (30 + null))
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove null
    integerSumBeed2.removeTerm(termNull);
    // check (sum = 5 + (30))
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals($integerSumBeed.getInteger(), 35);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 5
    $integerSumBeed.removeTerm(term5);
    // check (sum = (30))
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals($integerSumBeed.getInteger(), 30);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 30
    integerSumBeed2.removeTerm(term30);
    // check (sum = (no items))
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 0);
    assertEquals($integerSumBeed.getInteger(), 0);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove sum beed
    $integerSumBeed.removeTerm(integerSumBeed2);
    // check (sum = no items)
    assertTrue($integerSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 0);
    assertEquals($integerSumBeed.getInteger(), 0);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
  }

  //@Test
  // three-level sum
  public void removeTerm3() throws EditStateException, IllegalEditException {
    // create terms
    EditableIntegerBeed term1 = createEditableIntegerBeed(1);
    EditableIntegerBeed term2 = createEditableIntegerBeed(2);
    EditableIntegerBeed term3 = createEditableIntegerBeed(3);
    EditableIntegerBeed term4 = createEditableIntegerBeed(4);
    // create sum beed
    IntegerSumBeed integerSumBeed2plus3 = new IntegerSumBeed($owner);
    integerSumBeed2plus3.addTerm(term2);
    integerSumBeed2plus3.addTerm(term3);
    IntegerSumBeed integerSumBeed2plus3plus4 = new IntegerSumBeed($owner);
    integerSumBeed2plus3plus4.addTerm(integerSumBeed2plus3);
    integerSumBeed2plus3plus4.addTerm(term4);
    $integerSumBeed.addTerm(term1);
    $integerSumBeed.addTerm(integerSumBeed2plus3plus4);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals($integerSumBeed.getInteger(), 10);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 2
    integerSumBeed2plus3.removeTerm(term2);
    // check (sum = 1 + ((3) + 4))
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 0);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals($integerSumBeed.getInteger(), 8);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 4
    integerSumBeed2plus3plus4.removeTerm(term4);
    // check (sum = 1 + ((3)))
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 0);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 0);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals($integerSumBeed.getInteger(), 4);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove integerSumBeed2plus3
    integerSumBeed2plus3plus4.removeTerm(integerSumBeed2plus3);
    // check (sum = 1 + (no terms))
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 0);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 0);
    assertEquals($integerSumBeed.getInteger(), 1);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove integerSumBeed2plus3plus4
    $integerSumBeed.removeTerm(integerSumBeed2plus3plus4);
    // check (sum = 1)
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 0);
    assertEquals($integerSumBeed.getInteger(), 1);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // remove 1
    $integerSumBeed.removeTerm(term1);
    // check (sum = no terms)
    assertTrue($integerSumBeed.getNbOccurrences(term1) == 0);
    assertTrue($integerSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 0);
    assertEquals($integerSumBeed.getInteger(), 0);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
  }

  @Test
  public void recalculate() {
    // create terms
    EditableIntegerBeed term1 = createEditableIntegerBeed(1);
    EditableIntegerBeed term2 = createEditableIntegerBeed(2);
    EditableIntegerBeed term3 = createEditableIntegerBeed(3);
    EditableIntegerBeed term4 = createEditableIntegerBeed(4);
    EditableIntegerBeed termNull = createEditableIntegerBeed(null);
    // create sum beed
    $integerSumBeed.addTerm(term1);
    $integerSumBeed.addTerm(term2);
    // recalculate
    $integerSumBeed.recalculate();
    assertEquals($integerSumBeed.getInteger(), 3);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // recalculate
    $integerSumBeed.recalculate();
    assertEquals($integerSumBeed.getInteger(), 3);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add term
    $integerSumBeed.addTerm(term3);
    // recalculate
    $integerSumBeed.recalculate();
    assertEquals($integerSumBeed.getInteger(), 6);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add term
    $integerSumBeed.addTerm(term4);
    // recalculate
    $integerSumBeed.recalculate();
    assertEquals($integerSumBeed.getInteger(), 10);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add term
    $integerSumBeed.addTerm(termNull);
    // recalculate
    $integerSumBeed.recalculate();
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
    // add term
    $integerSumBeed.addTerm(termNull);
    // recalculate
    $integerSumBeed.recalculate();
    assertEquals($integerSumBeed.getInteger(), null);
    assertTrue(equalValue($integerSumBeed.getDouble(), $integerSumBeed.getInteger()));
  }

  @Test
  public void createInitialEvent() {
    IntegerEvent initialEvent = $integerSumBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $integerSumBeed);
    assertEquals(initialEvent.getOldInteger(), null);
    assertEquals(initialEvent.getNewInteger(), $integerSumBeed.getInteger());
    assertEquals(initialEvent.getEdit(), null);
  }

}
