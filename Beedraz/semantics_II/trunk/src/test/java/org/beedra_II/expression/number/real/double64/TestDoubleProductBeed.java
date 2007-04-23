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

package org.beedra_II.expression.number.real.double64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppeew.smallfries_I.MathUtil.equalValue;

import org.beedra_II.Listener;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.AggregateEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.expression.number.integer.long64.EditableLongBeed;
import org.beedra_II.expression.number.integer.long64.LongEdit;
import org.beedra_II.expression.number.real.RealBeed;
import org.beedra_II.expression.number.real.double64.ActualDoubleEvent;
import org.beedra_II.expression.number.real.double64.DoubleEdit;
import org.beedra_II.expression.number.real.double64.DoubleProductBeed;
import org.beedra_II.expression.number.real.double64.EditableDoubleBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDoubleProductBeed {

  public class MyDoubleProductBeed extends DoubleProductBeed {
    public MyDoubleProductBeed() {
      super();
    }

    /**
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(ActualDoubleEvent event) {
      updateDependents(event);
    }

  }

  public class AggregateEventListener implements Listener<AggregateEvent> {

    public void beedChanged(AggregateEvent event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public AggregateEvent $event;

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
  private MyDoubleProductBeed $doubleProductBeed = new MyDoubleProductBeed();

  @Test
  public void constructor() {
    // the value should be 1
    assertEquals($doubleProductBeed.getDouble(), 1.0);
    // no factors registered (cannot be tested)
  }

  @Test
  public void getNbOccurrences() {
    // NOP: basic
  }

  @Test
  // one-level product
  public void addProduct1() throws EditStateException, IllegalEditException {
    // create factors
    EditableDoubleBeed factor5 = createEditableDoubleBeed(5.0);
    EditableDoubleBeed factorNull = createEditableDoubleBeed(null);
    EditableDoubleBeed factor30 = createEditableDoubleBeed(30.0);
    addProduct1_A(factor5, factorNull, factor30);

    // When one of the factors is changed by an edit, the listeners of that factor
    // are notified (in the perform method of the edit).
    // One of those listeners is a factor listener, defined as inner class of DoubleProductBeed,
    // associated with the factor. This factor listener recalculates the product.
    // change null to 7
    DoubleEdit editNull = new DoubleEdit(factorNull);
    Double value7 = 7.0;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(factorNull.get(), value7);
    // check (product = 7 * 5 * 30)
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertEquals($doubleProductBeed.getDouble(), 1050.0);
    // change 30 to 3
    DoubleEdit edit30 = new DoubleEdit(factor30);
    Double value3 = 3.0;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(factor30.get(), value3);
    // check (product = 7 * 5 * 3)
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertEquals($doubleProductBeed.getDouble(), 105.0);
  }

  @Test
  // one-level product
  public void addProduct1i() throws EditStateException, IllegalEditException {
    // create factors
    EditableLongBeed factor5 = createEditableLongBeed(5L);
    EditableLongBeed factorNull = createEditableLongBeed(null);
    EditableLongBeed factor30 = createEditableLongBeed(30L);
    addProduct1_A(factor5, factorNull, factor30);

    // When one of the factors is changed by an edit, the listeners of that factor
    // are notified (in the perform method of the edit).
    // One of those listeners is a factor listener, defined as inner class of DoubleProductBeed,
    // associated with the factor. This factor listener recalculates the product.
    // change null to 7
    LongEdit editNull = new LongEdit(factorNull);
    Long value7 = 7L;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(factorNull.get(), value7);
    // check (product = 7 * 5 * 30)
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertEquals($doubleProductBeed.getDouble(), 1050.0);
    // change 30 to 3
    LongEdit edit30 = new LongEdit(factor30);
    Long value3 = 3L;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(factor30.get(), value3);
    // check (product = 7 * 5 * 3)
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertEquals($doubleProductBeed.getDouble(), 105.0);
  }

  private void addProduct1_A(RealBeed<?> factor5, RealBeed<?> factorNull, RealBeed<?> factor30) {
    // add factor5
    $doubleProductBeed.addArgument(factor5);
    // check (product = 5)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertEquals($doubleProductBeed.getDouble(), 5.0);
    // add factor30
    $doubleProductBeed.addArgument(factor30);
    // check (product = 5 * 30)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertEquals($doubleProductBeed.getDouble(), 150.0);
    // add factorNull
    $doubleProductBeed.addArgument(factorNull);
    // check (product = 5 * 30 * null)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 1);
    assertEquals($doubleProductBeed.getDouble(), null);
    // remove factors
    $doubleProductBeed.removeArgument(factor5);
    $doubleProductBeed.removeArgument(factor30);
    $doubleProductBeed.removeArgument(factorNull);
    // check (product has no factors)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 0);
    assertEquals($doubleProductBeed.getDouble(), 1.0);
    // add factorNull
    $doubleProductBeed.addArgument(factorNull);
    // check (product = null)
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 1);
    assertEquals($doubleProductBeed.getDouble(), null);
    // add factor
    $doubleProductBeed.addArgument(factor5);
    // check (product = null * 5)
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertEquals($doubleProductBeed.getDouble(), null);
    // add factor
    $doubleProductBeed.addArgument(factor30);
    // check (product = null * 5 * 30)
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertEquals($doubleProductBeed.getDouble(), null);
  }

  @Test
  // two-level product
  public void addProduct2() throws EditStateException, IllegalEditException {
    // create factors
    Double goal5 = 5.0;
    EditableDoubleBeed factor5 = createEditableDoubleBeed(goal5);
    EditableDoubleBeed factorNull = createEditableDoubleBeed(null);
    Double goal30 = 30.0;
    EditableDoubleBeed factor30 = createEditableDoubleBeed(goal30);
    DoubleProductBeed doubleProductBeed2 = addProduct2_A(goal5, factor5, factorNull, goal30, factor30);

    // When one of the factors is changed by an edit, the listeners of that factor
    // are notified (in the perform method of the edit).
    // One of those listeners is a factor listener, defined as inner class of DoubleProductBeed,
    // associated with the factor. This factor listener recalculates the product.
    // change 5 to 2
    DoubleEdit edit5 = new DoubleEdit(factor5);
    Double value2 = 2.0;
    edit5.setGoal(value2);
    edit5.perform();
    assertEquals(factor5.get(), value2);
    // check (product = 2 * (30 * null))
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 1);
    assertEquals(doubleProductBeed2.getDouble(), null);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertEquals($doubleProductBeed.getDouble(), null);
    // change null to 7
    DoubleEdit editNull = new DoubleEdit(factorNull);
    Double value7 = 7.0;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(factorNull.get(), value7);
    // check (product = 2 * (30 * 7))
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 1);
    assertEquals(doubleProductBeed2.getDouble(), 210.0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertEquals($doubleProductBeed.getDouble(), 420.0);
    // change 30 to 3
    DoubleEdit edit30 = new DoubleEdit(factor30);
    Double value3 = 3.0;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(factor30.get(), value3);
    // check (product = 2 * (3 * 7))
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertEquals($doubleProductBeed.getDouble(), 42.0);
  }

  @Test
  // two-level product
  public void addProduct2i() throws EditStateException, IllegalEditException {
    // create factors
    Long goal5 = 5L;
    EditableLongBeed factor5 = createEditableLongBeed(goal5);
    EditableLongBeed factorNull = createEditableLongBeed(null);
    Long goal30 = 30L;
    EditableLongBeed factor30 = createEditableLongBeed(goal30);
    DoubleProductBeed doubleProductBeed2 = addProduct2_A(goal5, factor5, factorNull, goal30, factor30);

    // When one of the factors is changed by an edit, the listeners of that factor
    // are notified (in the perform method of the edit).
    // One of those listeners is a factor listener, defined as inner class of DoubleProductBeed,
    // associated with the factor. This factor listener recalculates the product.
    // change 5 to 2
    LongEdit edit5 = new LongEdit(factor5);
    Long value2 = 2L;
    edit5.setGoal(value2);
    edit5.perform();
    assertEquals(factor5.get(), value2);
    // check (product = 2 * (30 * null))
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 1);
    assertEquals(doubleProductBeed2.getDouble(), null);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertEquals($doubleProductBeed.getDouble(), null);
    // change null to 7
    LongEdit editNull = new LongEdit(factorNull);
    Long value7 = 7L;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(factorNull.get(), value7);
    // check (product = 2 * (30 * 7))
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 1);
    assertEquals(doubleProductBeed2.getDouble(), 210.0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertEquals($doubleProductBeed.getDouble(), 420.0);
    // change 30 to 3
    LongEdit edit30 = new LongEdit(factor30);
    Long value3 = 3L;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(factor30.get(), value3);
    // check (product = 2 * (3 * 7))
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertEquals($doubleProductBeed.getDouble(), 42.0);
  }

  private DoubleProductBeed addProduct2_A(Number goal5, RealBeed<?> factor5, RealBeed<?> factorNull, Number goal30, RealBeed<?> factor30) {
    // add factor5
    $doubleProductBeed.addArgument(factor5);
    // check (product = 5)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue(equalValue($doubleProductBeed.getDouble(), goal5));
    // create another product beed
    DoubleProductBeed doubleProductBeed2 = new DoubleProductBeed();
    // check (product has no factors)
    assertTrue(equalValue(doubleProductBeed2.getDouble(), 1.0));
    // add factor30
    doubleProductBeed2.addArgument(factor30);
    // check (product = 30)
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 1);
    assertTrue(equalValue(doubleProductBeed2.getDouble(), goal30));
    // add factorNull
    doubleProductBeed2.addArgument(factorNull);
    // check (product = 30 * null)
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 1);
    assertTrue(equalValue(doubleProductBeed2.getDouble(), null));
    // add doubleProductBeed2
    $doubleProductBeed.addArgument(doubleProductBeed2);
    // check (product = 5 * (30 * null))
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertTrue(equalValue($doubleProductBeed.getDouble(), null));
    return doubleProductBeed2;
  }

  @Test
  // three-level product
  public void addProduct3() throws EditStateException, IllegalEditException {
    // create factors
    EditableDoubleBeed factor1 = createEditableDoubleBeed(1.0);
    EditableDoubleBeed factor2 = createEditableDoubleBeed(2.0);
    EditableDoubleBeed factor3 = createEditableDoubleBeed(3.0);
    EditableDoubleBeed factor4 = createEditableDoubleBeed(4.0);
    // create another product beed
    DoubleProductBeed doubleProductBeed2times3 = new DoubleProductBeed();
    doubleProductBeed2times3.addArgument(factor2);
    doubleProductBeed2times3.addArgument(factor3);
    // check (product = 2 * 3)
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertEquals(doubleProductBeed2times3.getDouble(), 6.0);
    // create another product beed
    DoubleProductBeed doubleProductBeed2times3times4 = new DoubleProductBeed();
    doubleProductBeed2times3times4.addArgument(doubleProductBeed2times3);
    doubleProductBeed2times3times4.addArgument(factor4);
    // check (product = (2 * 3) * 4)
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertEquals(doubleProductBeed2times3times4.getDouble(), 24.0);
    // add factors
    $doubleProductBeed.addArgument(factor1);
    $doubleProductBeed.addArgument(doubleProductBeed2times3times4);
    // check (product = 1 * ((2 * 3) * 4))
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertEquals($doubleProductBeed.getDouble(), 24.0);

    // When one of the factors is changed by an edit, the listeners of that factor
    // are notified (in the perform method of the edit).
    // One of those listeners is a factor listener, defined as inner class of DoubleProductBeed,
    // associated with the factor. This factor listener recalculates the product.
    // change 1 to null
    DoubleEdit edit1 = new DoubleEdit(factor1);
    Double valueNull = null;
    edit1.setGoal(valueNull);
    edit1.perform();
    assertEquals(factor1.get(), valueNull);
    // check (product = null * ((2 * 3) * 4))
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertEquals(doubleProductBeed2times3.getDouble(), 6.0);
    assertEquals(doubleProductBeed2times3times4.getDouble(), 24.0);
    assertEquals($doubleProductBeed.getDouble(), null);
    // change null back to 1
    edit1 = new DoubleEdit(factor1);
    Double value1 = 1.0;
    edit1.setGoal(value1);
    edit1.perform();
    assertEquals(factor1.get(), value1);
    // check (product = 1 * ((2 * 3) * 4))
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertEquals(doubleProductBeed2times3.getDouble(), 6.0);
    assertEquals(doubleProductBeed2times3times4.getDouble(), 24.0);
    assertEquals($doubleProductBeed.getDouble(), 24.0);
    // change 2 to null
    DoubleEdit edit2 = new DoubleEdit(factor2);
    edit2.setGoal(valueNull);
    edit2.perform();
    assertEquals(factor2.get(), valueNull);
    // check (product = 1 * ((null * 3) * 4))
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertEquals(doubleProductBeed2times3.getDouble(), null);
    assertEquals(doubleProductBeed2times3times4.getDouble(), null);
    assertEquals($doubleProductBeed.getDouble(), null);
    // change null back to 2
    edit2 = new DoubleEdit(factor2);
    Double value2 = 2.0;
    edit2.setGoal(value2);
    edit2.perform();
    assertEquals(factor2.get(), value2);
    // check (product = 1 * ((2 * 3) * 4))
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertEquals(doubleProductBeed2times3.getDouble(), 6.0);
    assertEquals(doubleProductBeed2times3times4.getDouble(), 24.0);
    assertEquals($doubleProductBeed.getDouble(), 24.0);
    // change 4 to null
    DoubleEdit edit4 = new DoubleEdit(factor4);
    edit4.setGoal(valueNull);
    edit4.perform();
    assertEquals(factor4.get(), valueNull);
    // check (product = 1 * ((2 * 3) * null))
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertEquals(doubleProductBeed2times3.getDouble(), 6.0);
    assertEquals(doubleProductBeed2times3times4.getDouble(), null);
    assertEquals($doubleProductBeed.getDouble(), null);
    // change null back to 4
    edit4 = new DoubleEdit(factor4);
    Double value4 = 4.0;
    edit4.setGoal(value4);
    edit4.perform();
    assertEquals(factor4.get(), value4);
    // check (product = 1 * ((2 * 3) * 4))
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertEquals(doubleProductBeed2times3.getDouble(), 6.0);
    assertEquals(doubleProductBeed2times3times4.getDouble(), 24.0);
    assertEquals($doubleProductBeed.getDouble(), 24.0);

    // change 2 to 11
    edit2 = new DoubleEdit(factor2);
    Double value11 = 11.0;
    edit2.setGoal(value11);
    edit2.perform();
    assertEquals(factor2.get(), value11);
    // check (product = 1 * ((11 * 3) * 4))
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertEquals(doubleProductBeed2times3.getDouble(), 33.0);
    assertEquals(doubleProductBeed2times3times4.getDouble(), 132.0);
    assertEquals($doubleProductBeed.getDouble(), 132.0);
  }

  @Test
  // product containing several times the same factor
  public void addProduct4() throws EditStateException, IllegalEditException {
    // create factors
    EditableDoubleBeed factor5 = createEditableDoubleBeed(5.0);
    // add factor5
    $doubleProductBeed.addArgument(factor5);
    // check (product = 5)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertEquals($doubleProductBeed.getDouble(), 5.0);
    // add factor5
    $doubleProductBeed.addArgument(factor5);
    // check (product = 5 * 5)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 2);
    assertEquals($doubleProductBeed.getDouble(), 25.0);
    // add factor5
    $doubleProductBeed.addArgument(factor5);
    // check (product = 5 * 5)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 3);
    assertEquals($doubleProductBeed.getDouble(), 125.0);

    // When one of the factors is changed by an edit, the listeners of that factor
    // are notified (in the perform method of the edit).
    // One of those listeners is a factor listener, defined as inner class of DoubleProductBeed,
    // associated with the factor. This factor listener recalculates the product.
    // change 5 to 7
    DoubleEdit edit5 = new DoubleEdit(factor5);
    Double value7 = 7.0;
    edit5.setGoal(value7);
    edit5.perform();
    assertEquals(factor5.get(), value7);
    // check (product = 7 * 7 * 7)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 3);
    assertTrue(equalValue($doubleProductBeed.getDouble(), 343.0));
    // change 7 to 11
    edit5 = new DoubleEdit(factor5);
    Double value11 = 11.0;
    edit5.setGoal(value11);
    edit5.perform();
    assertEquals(factor5.get(), value11);
    // check (product = 11 * 11 * 11)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 3);
    assertTrue(equalValue($doubleProductBeed.getDouble(), 1331.0));
    // change 11 to null
    edit5 = new DoubleEdit(factor5);
    Double valueNull = null;
    edit5.setGoal(valueNull);
    edit5.perform();
    assertEquals(factor5.get(), valueNull);
    // check (product = null * null * null)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 3);
    assertEquals($doubleProductBeed.getDouble(), null);

    edit5 = new DoubleEdit(factor5);
    edit5.setGoal(value7);
    edit5.perform();
    assertEquals(factor5.get(), value7);
    // check (product = 7 * 7 * 7)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 3);
    assertEquals($doubleProductBeed.getDouble(), 343.0);
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

  private EditableLongBeed createEditableLongBeed(Long value) {
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
  // one-level product
  public void removeProduct1() throws EditStateException, IllegalEditException {
    // create factors
    EditableDoubleBeed factor5 = createEditableDoubleBeed(5.0);
    EditableDoubleBeed factorNull = createEditableDoubleBeed(null);
    EditableDoubleBeed factor30 = createEditableDoubleBeed(30.0);
    EditableDoubleBeed factor11 = createEditableDoubleBeed(11.0);
    // add factors
    $doubleProductBeed.addArgument(factor5);
    $doubleProductBeed.addArgument(factor5);
    $doubleProductBeed.addArgument(factor5);
    $doubleProductBeed.addArgument(factorNull);
    $doubleProductBeed.addArgument(factorNull);
    $doubleProductBeed.addArgument(factor30);
    // check (product = 5 * 5 * 5 * null * null * 30)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 3);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 2);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor11) == 0);
    assertEquals($doubleProductBeed.getDouble(), null);
    // remove 11 (not in the product)
    $doubleProductBeed.removeArgument(factor11);
    // check (product = 5 * 5 * 5 * null * null * 30)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 3);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 2);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor11) == 0);
    assertEquals($doubleProductBeed.getDouble(), null);
    // remove 5
    $doubleProductBeed.removeArgument(factor5);
    // check (product = 5 * 5 * null * null * 30)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 2);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 2);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor11) == 0);
    assertEquals($doubleProductBeed.getDouble(), null);
    // remove null
    $doubleProductBeed.removeArgument(factorNull);
    // check (product = 5 * 5 * null * 30)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 2);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor11) == 0);
    assertEquals($doubleProductBeed.getDouble(), null);
    // remove null
    $doubleProductBeed.removeArgument(factorNull);
    // check (product = 5 * 5 * 30)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 2);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factor11) == 0);
    assertEquals($doubleProductBeed.getDouble(), 750.0);
    // remove 30
    $doubleProductBeed.removeArgument(factor30);
    // check (product = 5 * 5)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 2);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor11) == 0);
    assertEquals($doubleProductBeed.getDouble(), 25.0);
    // remove 5
    $doubleProductBeed.removeArgument(factor5);
    // check (product = 5)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor11) == 0);
    assertEquals($doubleProductBeed.getDouble(), 5.0);
    // remove 5
    $doubleProductBeed.removeArgument(factor5);
    // check (product = no items)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor11) == 0);
    assertEquals($doubleProductBeed.getDouble(), 1.0);
    // remove 5 (not in product)
    $doubleProductBeed.removeArgument(factor5);
    // check (product = no items)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factorNull) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor30) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(factor11) == 0);
    assertEquals($doubleProductBeed.getDouble(), 1.0);
  }

  @Test
  // two-level product
  public void removeProduct2() throws EditStateException, IllegalEditException {
    // create factors
    EditableDoubleBeed factor5 = createEditableDoubleBeed(5.0);
    EditableDoubleBeed factorNull = createEditableDoubleBeed(null);
    EditableDoubleBeed factor30 = createEditableDoubleBeed(30.0);
    // create product beed: 5 * (30 * null)
    $doubleProductBeed.addArgument(factor5);
    DoubleProductBeed doubleProductBeed2 = new DoubleProductBeed();
    doubleProductBeed2.addArgument(factor30);
    doubleProductBeed2.addArgument(factorNull);
    $doubleProductBeed.addArgument(doubleProductBeed2);
    // check (product = 5 * (30 * null))
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 1);
    assertEquals($doubleProductBeed.getDouble(), null);
    // remove null
    doubleProductBeed2.removeArgument(factorNull);
    // check (product = 5 * (30))
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 0);
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 1);
    assertEquals($doubleProductBeed.getDouble(), 150.0);
    // remove 5
    $doubleProductBeed.removeArgument(factor5);
    // check (product = (30))
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 0);
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 1);
    assertEquals($doubleProductBeed.getDouble(), 30.0);
    // remove 30
    doubleProductBeed2.removeArgument(factor30);
    // check (product = (no items))
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 1);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 0);
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 0);
    assertEquals($doubleProductBeed.getDouble(), 1.0);
    // remove product beed
    $doubleProductBeed.removeArgument(doubleProductBeed2);
    // check (product = no items)
    assertTrue($doubleProductBeed.getNbOccurrences(factor5) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2) == 0);
    assertTrue(doubleProductBeed2.getNbOccurrences(factorNull) == 0);
    assertTrue(doubleProductBeed2.getNbOccurrences(factor30) == 0);
    assertEquals($doubleProductBeed.getDouble(), 1.0);
  }

  @Test
  // three-level product
  public void removeProduct3() throws EditStateException, IllegalEditException {
    // create factors
    EditableDoubleBeed factor1 = createEditableDoubleBeed(1.0);
    EditableDoubleBeed factor2 = createEditableDoubleBeed(2.0);
    EditableDoubleBeed factor3 = createEditableDoubleBeed(3.0);
    EditableDoubleBeed factor4 = createEditableDoubleBeed(4.0);
    // create product beed
    DoubleProductBeed doubleProductBeed2times3 = new DoubleProductBeed();
    doubleProductBeed2times3.addArgument(factor2);
    doubleProductBeed2times3.addArgument(factor3);
    DoubleProductBeed doubleProductBeed2times3times4 = new DoubleProductBeed();
    doubleProductBeed2times3times4.addArgument(doubleProductBeed2times3);
    doubleProductBeed2times3times4.addArgument(factor4);
    $doubleProductBeed.addArgument(factor1);
    $doubleProductBeed.addArgument(doubleProductBeed2times3times4);
    // check (product = 1 * ((2 * 3) * 4))
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertEquals($doubleProductBeed.getDouble(), 24.0);
    // remove 2
    doubleProductBeed2times3.removeArgument(factor2);
    // check (product = 1 * ((3) * 4))
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 1);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 0);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertEquals($doubleProductBeed.getDouble(), 12.0);
    // remove 4
    doubleProductBeed2times3times4.removeArgument(factor4);
    // check (product = 1 * ((3)))
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 0);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor2) == 0);
    assertTrue(doubleProductBeed2times3.getNbOccurrences(factor3) == 1);
    assertEquals($doubleProductBeed.getDouble(), 3.0);
    // remove doubleProductBeed2times3
    doubleProductBeed2times3times4.removeArgument(doubleProductBeed2times3);
    // check (product = 1 * (no factors))
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 1);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(doubleProductBeed2times3) == 0);
    assertTrue(doubleProductBeed2times3times4.getNbOccurrences(factor4) == 0);
    assertEquals($doubleProductBeed.getDouble(), 1.0);
    // remove doubleProductBeed2times3times4
    $doubleProductBeed.removeArgument(doubleProductBeed2times3times4);
    // check (product = 1)
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 1);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 0);
    assertEquals($doubleProductBeed.getDouble(), 1.0);
    // remove 1
    $doubleProductBeed.removeArgument(factor1);
    // check (product = no factors)
    assertTrue($doubleProductBeed.getNbOccurrences(factor1) == 0);
    assertTrue($doubleProductBeed.getNbOccurrences(doubleProductBeed2times3times4) == 0);
    assertEquals($doubleProductBeed.getDouble(), 1.0);
  }

}
