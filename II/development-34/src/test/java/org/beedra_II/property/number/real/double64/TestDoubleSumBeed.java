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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.smallfries_I.MathUtil.equalValue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Listener;
import org.beedra_II.property.number.integer.long64.EditableLongBeed;
import org.beedra_II.property.number.integer.long64.LongEdit;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.RealEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDoubleSumBeed {

  public class MyDoubleSumBeed extends DoubleSumBeed {
    public MyDoubleSumBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(ActualDoubleEvent event) {
      updateDependents(event);
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
  private MyDoubleSumBeed $doubleSumBeed = new MyDoubleSumBeed($owner);
  private ActualDoubleEvent $event1 = new ActualDoubleEvent($doubleSumBeed, new Double(0), new Double(1), null);
      // @mudo Laatste argument mag niet null zijn??
  private PropagatedEventListener $listener1 = new PropagatedEventListener();
  private PropagatedEventListener $listener2 = new PropagatedEventListener();

  @Test
  public void constructor() {
    assertEquals($doubleSumBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $doubleSumBeed.publicUpdateDependents($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
    // the value should be 0
    assertEquals($doubleSumBeed.getDouble(), 0.0);
    // no terms registered (cannot be tested)
  }

  @Test
  public void getNbOccurrences() {
    // NOP: basic
  }

  @Test
  // one-level sum
  public void addTerm1() throws EditStateException, IllegalEditException {
    // create terms
    EditableDoubleBeed term5 = createEditableDoubleBeed(5.0);
    EditableDoubleBeed termNull = createEditableDoubleBeed(null);
    EditableDoubleBeed term30 = createEditableDoubleBeed(30.0);
    addTerm1_A(term5, termNull, term30);

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of DoubleSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change null to 7
    DoubleEdit editNull = new DoubleEdit(termNull);
    Double value7 = 7.0;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(termNull.get(), value7);
    // check (sum = 7 + 5 + 30)
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($doubleSumBeed.getDouble(), 42.0);
    // change 30 to 3
    DoubleEdit edit30 = new DoubleEdit(term30);
    Double value3 = 3.0;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 7 + 5 + 3)
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($doubleSumBeed.getDouble(), 15.0);
  }

  @Test
  // one-level sum
  public void addTerm1i() throws EditStateException, IllegalEditException {
    // create terms
    EditableLongBeed term5 = createEditableIntegerBeed(5L);
    EditableLongBeed termNull = createEditableIntegerBeed(null);
    EditableLongBeed term30 = createEditableIntegerBeed(30L);
    addTerm1_A(term5, termNull, term30);

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of DoubleSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change null to 7
    LongEdit editNull = new LongEdit(termNull);
    Long value7 = 7L;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(termNull.get(), value7);
    // check (sum = 7 + 5 + 30)
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($doubleSumBeed.getDouble(), 42.0);
    // change 30 to 3
    LongEdit edit30 = new LongEdit(term30);
    Long value3 = 3L;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 7 + 5 + 3)
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($doubleSumBeed.getDouble(), 15.0);
  }

  private void addTerm1_A(RealBeed<?> term5, RealBeed<?> termNull, RealBeed<?> term30) {
    // add term5
    $doubleSumBeed.addArgument(term5);
    // check (sum = 5)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertEquals(5.0d, $doubleSumBeed.getDouble());
    // add term30
    $doubleSumBeed.addArgument(term30);
    // check (sum = 5 + 30)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($doubleSumBeed.getDouble(), 35.0);
    // add termNull
    $doubleSumBeed.addArgument(termNull);
    // check (sum = 5 + 30 + null)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 1);
    assertEquals($doubleSumBeed.getDouble(), null);
    // remove terms
    $doubleSumBeed.removeArgument(term5);
    $doubleSumBeed.removeArgument(term30);
    $doubleSumBeed.removeArgument(termNull);
    // check (sum has no terms)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 0);
    assertEquals($doubleSumBeed.getDouble(), 0.0);
    // add termNull
    $doubleSumBeed.addArgument(termNull);
    // check (sum = null)
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 1);
    assertEquals($doubleSumBeed.getDouble(), null);
    // add term
    $doubleSumBeed.addArgument(term5);
    // check (sum = null + 5)
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($doubleSumBeed.getDouble(), null);
    // add term
    $doubleSumBeed.addArgument(term30);
    // check (sum = null + 5 + 30)
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($doubleSumBeed.getDouble(), null);
  }

  @Test
  // two-level sum
  public void addTerm2() throws EditStateException, IllegalEditException {
    // create terms
    Double goal5 = 5.0;
    EditableDoubleBeed term5 = createEditableDoubleBeed(goal5);
    EditableDoubleBeed termNull = createEditableDoubleBeed(null);
    Double goal30 = 30.0;
    EditableDoubleBeed term30 = createEditableDoubleBeed(goal30);
    DoubleSumBeed doubleSumBeed2 = addTerm2_A(goal5, term5, termNull, goal30, term30);

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of DoubleSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 5 to 2
    DoubleEdit edit5 = new DoubleEdit(term5);
    Double value2 = 2.0;
    edit5.setGoal(value2);
    edit5.perform();
    assertEquals(term5.get(), value2);
    // check (sum = 2 + (30 + null))
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(doubleSumBeed2.getDouble(), null);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertEquals($doubleSumBeed.getDouble(), null);
    // change null to 7
    DoubleEdit editNull = new DoubleEdit(termNull);
    Double value7 = 7.0;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(termNull.get(), value7);
    // check (sum = 2 + (30 + 7))
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(doubleSumBeed2.getDouble(), 37.0);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertEquals($doubleSumBeed.getDouble(), 39.0);
    // change 30 to 3
    DoubleEdit edit30 = new DoubleEdit(term30);
    Double value3 = 3.0;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 2 + (3 + 7))
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertEquals($doubleSumBeed.getDouble(), 12.0);
  }

  @Test
  // two-level sum
  public void addTerm2i() throws EditStateException, IllegalEditException {
    // create terms
    Long goal5 = 5L;
    EditableLongBeed term5 = createEditableIntegerBeed(goal5);
    EditableLongBeed termNull = createEditableIntegerBeed(null);
    Long goal30 = 30L;
    EditableLongBeed term30 = createEditableIntegerBeed(goal30);
    DoubleSumBeed doubleSumBeed2 = addTerm2_A(goal5, term5, termNull, goal30, term30);

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of DoubleSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 5 to 2
    LongEdit edit5 = new LongEdit(term5);
    Long value2 = 2L;
    edit5.setGoal(value2);
    edit5.perform();
    assertEquals(term5.get(), value2);
    // check (sum = 2 + (30 + null))
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(doubleSumBeed2.getDouble(), null);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertEquals($doubleSumBeed.getDouble(), null);
    // change null to 7
    LongEdit editNull = new LongEdit(termNull);
    Long value7 = 7L;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(termNull.get(), value7);
    // check (sum = 2 + (30 + 7))
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(doubleSumBeed2.getDouble(), 37.0);
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertEquals($doubleSumBeed.getDouble(), 39.0);
    // change 30 to 3
    LongEdit edit30 = new LongEdit(term30);
    Long value3 = 3L;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 2 + (3 + 7))
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertEquals($doubleSumBeed.getDouble(), 12.0);
  }

  private DoubleSumBeed addTerm2_A(Number goal5, RealBeed<?> term5, RealBeed<?> termNull, Number goal30, RealBeed<?> term30) {
    // add term5
    $doubleSumBeed.addArgument(term5);
    // check (sum = 5)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue(equalValue($doubleSumBeed.getDouble(), goal5));
    // create another sum beed
    DoubleSumBeed doubleSumBeed2 = new DoubleSumBeed($owner);
    // check (sum has no terms)
    assertTrue(equalValue(doubleSumBeed2.getDouble(), 0));
    // add term30
    doubleSumBeed2.addArgument(term30);
    // check (sum = 30)
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(equalValue(doubleSumBeed2.getDouble(), goal30));
    // add termNull
    doubleSumBeed2.addArgument(termNull);
    // check (sum = 30 + null)
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 1);
    assertTrue(equalValue(doubleSumBeed2.getDouble(), null));
    // add doubleSumBeed2
    $doubleSumBeed.addArgument(doubleSumBeed2);
    // check (sum = 5 + (30 + null))
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertTrue(equalValue($doubleSumBeed.getDouble(), null));
    return doubleSumBeed2;
  }

  @Test
  // three-level sum
  public void addTerm3() throws EditStateException, IllegalEditException {
    // create terms
    EditableDoubleBeed term1 = createEditableDoubleBeed(1.0);
    EditableDoubleBeed term2 = createEditableDoubleBeed(2.0);
    EditableDoubleBeed term3 = createEditableDoubleBeed(3.0);
    EditableDoubleBeed term4 = createEditableDoubleBeed(4.0);
    // create another sum beed
    DoubleSumBeed doubleSumBeed2plus3 = new DoubleSumBeed($owner);
    doubleSumBeed2plus3.addArgument(term2);
    doubleSumBeed2plus3.addArgument(term3);
    // check (sum = 2 + 3)
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals(doubleSumBeed2plus3.getDouble(), 5.0);
    // create another sum beed
    DoubleSumBeed doubleSumBeed2plus3plus4 = new DoubleSumBeed($owner);
    doubleSumBeed2plus3plus4.addArgument(doubleSumBeed2plus3);
    doubleSumBeed2plus3plus4.addArgument(term4);
    // check (sum = (2 + 3) + 4)
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertEquals(doubleSumBeed2plus3plus4.getDouble(), 9.0);
    // add terms
    $doubleSumBeed.addArgument(term1);
    $doubleSumBeed.addArgument(doubleSumBeed2plus3plus4);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertEquals($doubleSumBeed.getDouble(), 10.0);

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of DoubleSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 1 to null
    DoubleEdit edit1 = new DoubleEdit(term1);
    Double valueNull = null;
    edit1.setGoal(valueNull);
    edit1.perform();
    assertEquals(term1.get(), valueNull);
    // check (sum = null + ((2 + 3) + 4))
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertEquals(doubleSumBeed2plus3.getDouble(), 5.0);
    assertEquals(doubleSumBeed2plus3plus4.getDouble(), 9.0);
    assertEquals($doubleSumBeed.getDouble(), null);
    // change null back to 1
    edit1 = new DoubleEdit(term1);
    Double value1 = 1.0;
    edit1.setGoal(value1);
    edit1.perform();
    assertEquals(term1.get(), value1);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertEquals(doubleSumBeed2plus3.getDouble(), 5.0);
    assertEquals(doubleSumBeed2plus3plus4.getDouble(), 9.0);
    assertEquals($doubleSumBeed.getDouble(), 10.0);
    // change 2 to null
    DoubleEdit edit2 = new DoubleEdit(term2);
    edit2.setGoal(valueNull);
    edit2.perform();
    assertEquals(term2.get(), valueNull);
    // check (sum = 1 + ((null + 3) + 4))
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertEquals(doubleSumBeed2plus3.getDouble(), null);
    assertEquals(doubleSumBeed2plus3plus4.getDouble(), null);
    assertEquals($doubleSumBeed.getDouble(), null);
    // change null back to 2
    edit2 = new DoubleEdit(term2);
    Double value2 = 2.0;
    edit2.setGoal(value2);
    edit2.perform();
    assertEquals(term2.get(), value2);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertEquals(doubleSumBeed2plus3.getDouble(), 5.0);
    assertEquals(doubleSumBeed2plus3plus4.getDouble(), 9.0);
    assertEquals($doubleSumBeed.getDouble(), 10.0);
    // change 4 to null
    DoubleEdit edit4 = new DoubleEdit(term4);
    edit4.setGoal(valueNull);
    edit4.perform();
    assertEquals(term4.get(), valueNull);
    // check (sum = 1 + ((2 + 3) + null))
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertEquals(doubleSumBeed2plus3.getDouble(), 5.0);
    assertEquals(doubleSumBeed2plus3plus4.getDouble(), null);
    assertEquals($doubleSumBeed.getDouble(), null);
    // change null back to 4
    edit4 = new DoubleEdit(term4);
    Double value4 = 4.0;
    edit4.setGoal(value4);
    edit4.perform();
    assertEquals(term4.get(), value4);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertEquals(doubleSumBeed2plus3.getDouble(), 5.0);
    assertEquals(doubleSumBeed2plus3plus4.getDouble(), 9.0);
    assertEquals($doubleSumBeed.getDouble(), 10.0);

    // change 2 to 11
    edit2 = new DoubleEdit(term2);
    Double value11 = 11.0;
    edit2.setGoal(value11);
    edit2.perform();
    assertEquals(term2.get(), value11);
    // check (sum = 1 + ((11 + 3) + 4))
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertEquals(doubleSumBeed2plus3.getDouble(), 14.0);
    assertEquals(doubleSumBeed2plus3plus4.getDouble(), 18.0);
    assertEquals($doubleSumBeed.getDouble(), 19.0);
  }

  @Test
  // sum containing twice the same term
  public void addTerm4() throws EditStateException, IllegalEditException {
    // create terms
    EditableDoubleBeed term5 = createEditableDoubleBeed(5.0);
    // add term5
    $doubleSumBeed.addArgument(term5);
    // check (sum = 5)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($doubleSumBeed.getDouble(), 5.0);
    // add term5
    $doubleSumBeed.addArgument(term5);
    // check (sum = 5 + 5)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($doubleSumBeed.getDouble(), 10.0);

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of DoubleSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 5 to 7
    DoubleEdit edit5 = new DoubleEdit(term5);
    Double value7 = 7.0;
    edit5.setGoal(value7);
    edit5.perform();
    assertEquals(term5.get(), value7);
    // check (sum = 7 + 7)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($doubleSumBeed.getDouble(), 14.0);
    // change 7 to 11
    edit5 = new DoubleEdit(term5);
    Double value11 = 11.0;
    edit5.setGoal(value11);
    edit5.perform();
    assertEquals(term5.get(), value11);
    // check (sum = 11 + 11)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($doubleSumBeed.getDouble(), 22.0);
    // change 11 to null
    edit5 = new DoubleEdit(term5);
    Double valueNull = null;
    edit5.setGoal(valueNull);
    edit5.perform();
    assertEquals(term5.get(), valueNull);
    // check (sum = null + null)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($doubleSumBeed.getDouble(), null);

    edit5 = new DoubleEdit(term5);
    edit5.setGoal(value7);
    edit5.perform();
    assertEquals(term5.get(), value7);
    // check (sum = 7 + 7)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($doubleSumBeed.getDouble(), 14.0);
}

  private EditableDoubleBeed createEditableDoubleBeed(Double value) {
    try {
      EditableDoubleBeed editableDoubleBeed = new EditableDoubleBeed($owner);
      DoubleEdit edit = new DoubleEdit(editableDoubleBeed);
      edit.setGoal(value);
      edit.perform();
      assertEquals(editableDoubleBeed.get(), value);
      return editableDoubleBeed;
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

  private EditableLongBeed createEditableIntegerBeed(Long value) {
    try {
      EditableLongBeed editableLongBeed = new EditableLongBeed($owner);
      LongEdit edit = new LongEdit(editableLongBeed);
      edit.setGoal(value);
      edit.perform();
      assertEquals(editableLongBeed.get(), value);
      return editableLongBeed;
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
    EditableDoubleBeed term5 = createEditableDoubleBeed(5.0);
    EditableDoubleBeed termNull = createEditableDoubleBeed(null);
    EditableDoubleBeed term30 = createEditableDoubleBeed(30.0);
    EditableDoubleBeed term11 = createEditableDoubleBeed(11.0);
    // add terms
    $doubleSumBeed.addArgument(term5);
    $doubleSumBeed.addArgument(term5);
    $doubleSumBeed.addArgument(term5);
    $doubleSumBeed.addArgument(termNull);
    $doubleSumBeed.addArgument(termNull);
    $doubleSumBeed.addArgument(term30);
    // check (sum = 5 + 5 + 5 + null + null + 30)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 3);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 2);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($doubleSumBeed.getDouble(), null);
    // remove 11 (not in the sum)
    $doubleSumBeed.removeArgument(term11);
    // check (sum = 5 + 5 + 5 + null + null + 30)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 3);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 2);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($doubleSumBeed.getDouble(), null);
    // remove 5
    $doubleSumBeed.removeArgument(term5);
    // check (sum = 5 + 5 + null + null + 30)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 2);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($doubleSumBeed.getDouble(), null);
    // remove null
    $doubleSumBeed.removeArgument(termNull);
    // check (sum = 5 + 5 + null + 30)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($doubleSumBeed.getDouble(), null);
    // remove null
    $doubleSumBeed.removeArgument(termNull);
    // check (sum = 5 + 5 + 30)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($doubleSumBeed.getDouble(), 40.0);
    // remove 30
    $doubleSumBeed.removeArgument(term30);
    // check (sum = 5 + 5)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($doubleSumBeed.getDouble(), 10.0);
    // remove 5
    $doubleSumBeed.removeArgument(term5);
    // check (sum = 5)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($doubleSumBeed.getDouble(), 5.0);
    // remove 5
    $doubleSumBeed.removeArgument(term5);
    // check (sum = no items)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($doubleSumBeed.getDouble(), 0.0);
    // remove 5 (not in sum)
    $doubleSumBeed.removeArgument(term5);
    // check (sum = no items)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($doubleSumBeed.getDouble(), 0.0);
  }

  @Test
  // two-level sum
  public void removeTerm2() throws EditStateException, IllegalEditException {
    // create terms
    EditableDoubleBeed term5 = createEditableDoubleBeed(5.0);
    EditableDoubleBeed termNull = createEditableDoubleBeed(null);
    EditableDoubleBeed term30 = createEditableDoubleBeed(30.0);
    // create sum beed: 5 + (30 + null)
    $doubleSumBeed.addArgument(term5);
    DoubleSumBeed doubleSumBeed2 = new DoubleSumBeed($owner);
    doubleSumBeed2.addArgument(term30);
    doubleSumBeed2.addArgument(termNull);
    $doubleSumBeed.addArgument(doubleSumBeed2);
    // check (sum = 5 + (30 + null))
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals($doubleSumBeed.getDouble(), null);
    // remove null
    doubleSumBeed2.removeArgument(termNull);
    // check (sum = 5 + (30))
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals($doubleSumBeed.getDouble(), 35.0);
    // remove 5
    $doubleSumBeed.removeArgument(term5);
    // check (sum = (30))
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals($doubleSumBeed.getDouble(), 30.0);
    // remove 30
    doubleSumBeed2.removeArgument(term30);
    // check (sum = (no items))
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 1);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 0);
    assertEquals($doubleSumBeed.getDouble(), 0.0);
    // remove sum beed
    $doubleSumBeed.removeArgument(doubleSumBeed2);
    // check (sum = no items)
    assertTrue($doubleSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2) == 0);
    assertTrue(doubleSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(doubleSumBeed2.getNbOccurrences(term30) == 0);
    assertEquals($doubleSumBeed.getDouble(), 0.0);
  }

  @Test
  // three-level sum
  public void removeTerm3() throws EditStateException, IllegalEditException {
    // create terms
    EditableDoubleBeed term1 = createEditableDoubleBeed(1.0);
    EditableDoubleBeed term2 = createEditableDoubleBeed(2.0);
    EditableDoubleBeed term3 = createEditableDoubleBeed(3.0);
    EditableDoubleBeed term4 = createEditableDoubleBeed(4.0);
    // create sum beed
    DoubleSumBeed doubleSumBeed2plus3 = new DoubleSumBeed($owner);
    doubleSumBeed2plus3.addArgument(term2);
    doubleSumBeed2plus3.addArgument(term3);
    DoubleSumBeed doubleSumBeed2plus3plus4 = new DoubleSumBeed($owner);
    doubleSumBeed2plus3plus4.addArgument(doubleSumBeed2plus3);
    doubleSumBeed2plus3plus4.addArgument(term4);
    $doubleSumBeed.addArgument(term1);
    $doubleSumBeed.addArgument(doubleSumBeed2plus3plus4);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals($doubleSumBeed.getDouble(), 10.0);
    // remove 2
    doubleSumBeed2plus3.removeArgument(term2);
    // check (sum = 1 + ((3) + 4))
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 0);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals($doubleSumBeed.getDouble(), 8.0);
    // remove 4
    doubleSumBeed2plus3plus4.removeArgument(term4);
    // check (sum = 1 + ((3)))
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 0);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term2) == 0);
    assertTrue(doubleSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals($doubleSumBeed.getDouble(), 4.0);
    // remove doubleSumBeed2plus3
    doubleSumBeed2plus3plus4.removeArgument(doubleSumBeed2plus3);
    // check (sum = 1 + (no terms))
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 1);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(doubleSumBeed2plus3) == 0);
    assertTrue(doubleSumBeed2plus3plus4.getNbOccurrences(term4) == 0);
    assertEquals($doubleSumBeed.getDouble(), 1.0);
    // remove doubleSumBeed2plus3plus4
    $doubleSumBeed.removeArgument(doubleSumBeed2plus3plus4);
    // check (sum = 1)
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 0);
    assertEquals($doubleSumBeed.getDouble(), 1.0);
    // remove 1
    $doubleSumBeed.removeArgument(term1);
    // check (sum = no terms)
    assertTrue($doubleSumBeed.getNbOccurrences(term1) == 0);
    assertTrue($doubleSumBeed.getNbOccurrences(doubleSumBeed2plus3plus4) == 0);
    assertEquals($doubleSumBeed.getDouble(), 0.0);
  }

  @Test
  public void recalculate() {
    // create terms
    EditableDoubleBeed term1 = createEditableDoubleBeed(1.0);
    EditableDoubleBeed term2 = createEditableDoubleBeed(2.0);
    EditableDoubleBeed term3 = createEditableDoubleBeed(3.0);
    EditableDoubleBeed term4 = createEditableDoubleBeed(4.0);
    EditableDoubleBeed termNull = createEditableDoubleBeed(null);
    // create sum beed
    $doubleSumBeed.addArgument(term1);
    $doubleSumBeed.addArgument(term2);
    // recalculate
    $doubleSumBeed.recalculate();
    assertEquals($doubleSumBeed.getDouble(), 3.0);
    // recalculate
    $doubleSumBeed.recalculate();
    assertEquals($doubleSumBeed.getDouble(), 3.0);
    // add term
    $doubleSumBeed.addArgument(term3);
    // recalculate
    $doubleSumBeed.recalculate();
    assertEquals($doubleSumBeed.getDouble(), 6.0);
    // add term
    $doubleSumBeed.addArgument(term4);
    // recalculate
    $doubleSumBeed.recalculate();
    assertEquals($doubleSumBeed.getDouble(), 10.0);
    // add term
    $doubleSumBeed.addArgument(termNull);
    // recalculate
    $doubleSumBeed.recalculate();
    assertEquals($doubleSumBeed.getDouble(), null);
    // add term
    $doubleSumBeed.addArgument(termNull);
    // recalculate
    $doubleSumBeed.recalculate();
    assertEquals($doubleSumBeed.getDouble(), null);
  }

  @Test
  public void createInitialEvent() {
    RealEvent initialEvent = $doubleSumBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $doubleSumBeed);
    assertEquals(initialEvent.getOldDouble(), null);
    assertEquals(initialEvent.getNewDouble(), $doubleSumBeed.getDouble());
    assertEquals(initialEvent.getEdit(), null);
  }

}
