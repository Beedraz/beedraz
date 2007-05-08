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

package org.beedraz.semantics_II.expression.number.integer.long64;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MathUtil.equalValue;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.edit.EditStateException;
import org.beedraz.semantics_II.edit.IllegalEditException;
import org.beedraz.semantics_II.expression.number.integer.long64.ActualLongEvent;
import org.beedraz.semantics_II.expression.number.integer.long64.EditableLongBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.LongEdit;
import org.beedraz.semantics_II.expression.number.integer.long64.LongSumBeed;
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
public class TestLongSumBeed {

  public class MyIntegerSumBeed extends LongSumBeed {
    public MyIntegerSumBeed() {
      super();
    }

    /**
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(ActualLongEvent event) {
      updateDependents(event);
    }

  }

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $longSumBeed = new MyIntegerSumBeed();
  }

  @After
  public void tearDown() throws Exception {
    $owner = null;
    $longSumBeed = null;
  }

  private AggregateBeed $owner;
  private MyIntegerSumBeed $longSumBeed;

  @Test
  public void constructor() {
    // the value should be 0
    assertEquals($longSumBeed.getLong(), 0L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
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
    EditableLongBeed term5 = createEditableIntegerBeed(5L);
    EditableLongBeed termNull = createEditableIntegerBeed(null);
    EditableLongBeed term30 = createEditableIntegerBeed(30L);
    // add term5
    $longSumBeed.addTerm(term5);
    // check (sum = 5)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($longSumBeed.getLong(), 5L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add term30
    $longSumBeed.addTerm(term30);
    // check (sum = 5 + 30)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($longSumBeed.getLong(), 35L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add termNull
    $longSumBeed.addTerm(termNull);
    // check (sum = 5 + 30 + null)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 1);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove terms
    $longSumBeed.removeTerm(term5);
    $longSumBeed.removeTerm(term30);
    $longSumBeed.removeTerm(termNull);
    // check (sum has no terms)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 0);
    assertEquals(0L, $longSumBeed.getLong());
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add termNull
    $longSumBeed.addTerm(termNull);
    // check (sum = null)
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 1);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add term
    $longSumBeed.addTerm(term5);
    // check (sum = null + 5)
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add term
    $longSumBeed.addTerm(term30);
    // check (sum = null + 5 + 30)
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of LongSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change null to 7
    LongEdit editNull = new LongEdit(termNull);
    Long value7 = 7L;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(termNull.get(), value7);
    // check (sum = 7 + 5 + 30)
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($longSumBeed.getLong(), 42L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change 30 to 3
    LongEdit edit30 = new LongEdit(term30);
    Long value3 = 3L;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 7 + 5 + 3)
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertEquals($longSumBeed.getLong(), 15L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
  }

  @Test
  // two-level sum
  public void addTerm2() throws EditStateException, IllegalEditException {
    // create terms
    EditableLongBeed term5 = createEditableIntegerBeed(5L);
    EditableLongBeed termNull = createEditableIntegerBeed(null);
    EditableLongBeed term30 = createEditableIntegerBeed(30L);
    // add term5
    $longSumBeed.addTerm(term5);
    // check (sum = 5)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($longSumBeed.getLong(), 5L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // create another sum beed
    LongSumBeed integerSumBeed2 = new LongSumBeed();
    // check (sum has no terms)
    assertEquals(integerSumBeed2.getLong(), 0L);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getLong()));
    // add term30
    integerSumBeed2.addTerm(term30);
    // check (sum = 30)
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals(integerSumBeed2.getLong(), 30L);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getLong()));
    // add termNull
    integerSumBeed2.addTerm(termNull);
    // check (sum = 30 + null)
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(integerSumBeed2.getLong(), null);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getLong()));
    // add integerSumBeed2
    $longSumBeed.addTerm(integerSumBeed2);
    // check (sum = 5 + (30 + null))
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of LongSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 5 to 2
    LongEdit edit5 = new LongEdit(term5);
    Long value2 = 2L;
    edit5.setGoal(value2);
    edit5.perform();
    assertEquals(term5.get(), value2);
    // check (sum = 2 + (30 + null))
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(integerSumBeed2.getLong(), null);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getLong()));
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change null to 7
    LongEdit editNull = new LongEdit(termNull);
    Long value7 = 7L;
    editNull.setGoal(value7);
    editNull.perform();
    assertEquals(termNull.get(), value7);
    // check (sum = 2 + (30 + 7))
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 1);
    assertEquals(integerSumBeed2.getLong(), 37L);
    assertTrue(equalValue(integerSumBeed2.getDouble(), integerSumBeed2.getLong()));
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertEquals($longSumBeed.getLong(), 39L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change 30 to 3
    LongEdit edit30 = new LongEdit(term30);
    Long value3 = 3L;
    edit30.setGoal(value3);
    edit30.perform();
    assertEquals(term30.get(), value3);
    // check (sum = 2 + (3 + 7))
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertEquals($longSumBeed.getLong(), 12L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
  }

  @Test
  // three-level sum
  public void addTerm3() throws EditStateException, IllegalEditException {
    // create terms
    EditableLongBeed term1 = createEditableIntegerBeed(1L);
    EditableLongBeed term2 = createEditableIntegerBeed(2L);
    EditableLongBeed term3 = createEditableIntegerBeed(3L);
    EditableLongBeed term4 = createEditableIntegerBeed(4L);
    // create another sum beed
    LongSumBeed integerSumBeed2plus3 = new LongSumBeed();
    integerSumBeed2plus3.addTerm(term2);
    integerSumBeed2plus3.addTerm(term3);
    // check (sum = 2 + 3)
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals(integerSumBeed2plus3.getLong(), 5L);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getLong()));
    // create another sum beed
    LongSumBeed integerSumBeed2plus3plus4 = new LongSumBeed();
    integerSumBeed2plus3plus4.addTerm(integerSumBeed2plus3);
    integerSumBeed2plus3plus4.addTerm(term4);
    // check (sum = (2 + 3) + 4)
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertEquals(integerSumBeed2plus3plus4.getLong(), 9L);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getLong()));
    // add terms
    $longSumBeed.addTerm(term1);
    $longSumBeed.addTerm(integerSumBeed2plus3plus4);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals($longSumBeed.getLong(), 10L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of LongSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 1 to null
    LongEdit edit1 = new LongEdit(term1);
    Long valueNull = null;
    edit1.setGoal(valueNull);
    edit1.perform();
    assertEquals(term1.get(), valueNull);
    // check (sum = null + ((2 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getLong(), 5L);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getLong()));
    assertEquals(integerSumBeed2plus3plus4.getLong(), 9L);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getLong()));
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change null back to 1
    edit1 = new LongEdit(term1);
    Long value1 = 1L;
    edit1.setGoal(value1);
    edit1.perform();
    assertEquals(term1.get(), value1);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getLong(), 5L);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getLong()));
    assertEquals(integerSumBeed2plus3plus4.getLong(), 9L);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getLong()));
    assertEquals($longSumBeed.getLong(), 10L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change 2 to null
    LongEdit edit2 = new LongEdit(term2);
    edit2.setGoal(valueNull);
    edit2.perform();
    assertEquals(term2.get(), valueNull);
    // check (sum = 1 + ((null + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getLong(), null);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getLong()));
    assertNull(integerSumBeed2plus3plus4.getLong());
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getLong()));
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change null back to 2
    edit2 = new LongEdit(term2);
    Long value2 = 2L;
    edit2.setGoal(value2);
    edit2.perform();
    assertEquals(term2.get(), value2);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getLong(), 5L);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getLong()));
    assertEquals(integerSumBeed2plus3plus4.getLong(), 9L);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getLong()));
    assertEquals($longSumBeed.getLong(), 10L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change 4 to null
    LongEdit edit4 = new LongEdit(term4);
    edit4.setGoal(valueNull);
    edit4.perform();
    assertEquals(term4.get(), valueNull);
    // check (sum = 1 + ((2 + 3) + null))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getLong(), 5L);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getLong()));
    assertEquals(integerSumBeed2plus3plus4.getLong(), null);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getLong()));
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change null back to 4
    edit4 = new LongEdit(term4);
    Long value4 = 4L;
    edit4.setGoal(value4);
    edit4.perform();
    assertEquals(term4.get(), value4);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getLong(), 5L);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getLong()));
    assertEquals(integerSumBeed2plus3plus4.getLong(), 9L);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getLong()));
    assertEquals($longSumBeed.getLong(), 10L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));

    // change 2 to 11
    edit2 = new LongEdit(term2);
    Long value11 = 11L;
    edit2.setGoal(value11);
    edit2.perform();
    assertEquals(term2.get(), value11);
    // check (sum = 1 + ((11 + 3) + 4))
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertEquals(integerSumBeed2plus3.getLong(), 14L);
    assertTrue(equalValue(integerSumBeed2plus3.getDouble(), integerSumBeed2plus3.getLong()));
    assertEquals(integerSumBeed2plus3plus4.getLong(), 18L);
    assertTrue(equalValue(integerSumBeed2plus3plus4.getDouble(), integerSumBeed2plus3plus4.getLong()));
    assertEquals($longSumBeed.getLong(), 19L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
  }

  @Test
  // sum containing twice the same term
  public void addTerm4() throws EditStateException, IllegalEditException {
    // create terms
    EditableLongBeed term5 = createEditableIntegerBeed(5L);
    // add term5
    $longSumBeed.addTerm(term5);
    // check (sum = 5)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertEquals($longSumBeed.getLong(), 5L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add term5
    $longSumBeed.addTerm(term5);
    // check (sum = 5 + 5)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($longSumBeed.getLong(), 10L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));

    // When one of the terms is changed by an edit, the listeners of that term
    // are notified (in the perform method of the edit).
    // One of those listeners is a term listener, defined as inner class of LongSumBeed,
    // associated with the term. This term listener recalculates the sum.
    // change 5 to 7
    LongEdit edit5 = new LongEdit(term5);
    Long value7 = 7L;
    edit5.setGoal(value7);
    edit5.perform();
    assertEquals(term5.get(), value7);
    // check (sum = 7 + 7)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($longSumBeed.getLong(), 14L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change 7 to 11
    edit5 = new LongEdit(term5);
    Long value11 = 11L;
    edit5.setGoal(value11);
    edit5.perform();
    assertEquals(term5.get(), value11);
    // check (sum = 11 + 11)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($longSumBeed.getLong(), 22L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // change 11 to null
    edit5 = new LongEdit(term5);
    Long valueNull = null;
    edit5.setGoal(valueNull);
    edit5.perform();
    assertEquals(term5.get(), valueNull);
    // check (sum = null + null)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));

    edit5 = new LongEdit(term5);
    edit5.setGoal(value7);
    edit5.perform();
    assertEquals(term5.get(), value7);
    // check (sum = 7 + 7)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 2);
    assertEquals($longSumBeed.getLong(), 14L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
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
    EditableLongBeed term5 = createEditableIntegerBeed(5L);
    EditableLongBeed termNull = createEditableIntegerBeed(null);
    EditableLongBeed term30 = createEditableIntegerBeed(30L);
    EditableLongBeed term11 = createEditableIntegerBeed(11L);
    // add terms
    $longSumBeed.addTerm(term5);
    $longSumBeed.addTerm(term5);
    $longSumBeed.addTerm(term5);
    $longSumBeed.addTerm(termNull);
    $longSumBeed.addTerm(termNull);
    $longSumBeed.addTerm(term30);
    // check (sum = 5 + 5 + 5 + null + null + 30)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 3);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 2);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 11 (not in the sum)
    $longSumBeed.removeTerm(term11);
    // check (sum = 5 + 5 + 5 + null + null + 30)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 3);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 2);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 5
    $longSumBeed.removeTerm(term5);
    // check (sum = 5 + 5 + null + null + 30)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 2);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove null
    $longSumBeed.removeTerm(termNull);
    // check (sum = 5 + 5 + null + 30)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove null
    $longSumBeed.removeTerm(termNull);
    // check (sum = 5 + 5 + 30)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 1);
    assertTrue($longSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($longSumBeed.getLong(), 40L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 30
    $longSumBeed.removeTerm(term30);
    // check (sum = 5 + 5)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 2);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($longSumBeed.getLong(), 10L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 5
    $longSumBeed.removeTerm(term5);
    // check (sum = 5)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($longSumBeed.getLong(), 5L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 5
    $longSumBeed.removeTerm(term5);
    // check (sum = no items)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($longSumBeed.getLong(), 0L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 5 (not in sum)
    $longSumBeed.removeTerm(term5);
    // check (sum = no items)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($longSumBeed.getNbOccurrences(termNull) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term30) == 0);
    assertTrue($longSumBeed.getNbOccurrences(term11) == 0);
    assertEquals($longSumBeed.getLong(), 0L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
  }

  @Test
  // two-level sum
  public void removeTerm2() throws EditStateException, IllegalEditException {
    // create terms
    EditableLongBeed term5 = createEditableIntegerBeed(5L);
    EditableLongBeed termNull = createEditableIntegerBeed(null);
    EditableLongBeed term30 = createEditableIntegerBeed(30L);
    // create sum beed: 5 + (30 + null)
    $longSumBeed.addTerm(term5);
    LongSumBeed integerSumBeed2 = new LongSumBeed();
    integerSumBeed2.addTerm(term30);
    integerSumBeed2.addTerm(termNull);
    $longSumBeed.addTerm(integerSumBeed2);
    // check (sum = 5 + (30 + null))
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove null
    integerSumBeed2.removeTerm(termNull);
    // check (sum = 5 + (30))
    assertTrue($longSumBeed.getNbOccurrences(term5) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals($longSumBeed.getLong(), 35L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 5
    $longSumBeed.removeTerm(term5);
    // check (sum = (30))
    assertTrue($longSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 1);
    assertEquals($longSumBeed.getLong(), 30L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 30
    integerSumBeed2.removeTerm(term30);
    // check (sum = (no items))
    assertTrue($longSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2) == 1);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 0);
    assertEquals($longSumBeed.getLong(), 0L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove sum beed
    $longSumBeed.removeTerm(integerSumBeed2);
    // check (sum = no items)
    assertTrue($longSumBeed.getNbOccurrences(term5) == 0);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(termNull) == 0);
    assertTrue(integerSumBeed2.getNbOccurrences(term30) == 0);
    assertEquals($longSumBeed.getLong(), 0L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
  }

  @Test
  // three-level sum
  public void removeTerm3() throws EditStateException, IllegalEditException {
    // create terms
    EditableLongBeed term1 = createEditableIntegerBeed(1L);
    EditableLongBeed term2 = createEditableIntegerBeed(2L);
    EditableLongBeed term3 = createEditableIntegerBeed(3L);
    EditableLongBeed term4 = createEditableIntegerBeed(4L);
    // create sum beed
    LongSumBeed integerSumBeed2plus3 = new LongSumBeed();
    integerSumBeed2plus3.addTerm(term2);
    integerSumBeed2plus3.addTerm(term3);
    LongSumBeed integerSumBeed2plus3plus4 = new LongSumBeed();
    integerSumBeed2plus3plus4.addTerm(integerSumBeed2plus3);
    integerSumBeed2plus3plus4.addTerm(term4);
    $longSumBeed.addTerm(term1);
    $longSumBeed.addTerm(integerSumBeed2plus3plus4);
    // check (sum = 1 + ((2 + 3) + 4))
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals($longSumBeed.getLong(), 10L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 2
    integerSumBeed2plus3.removeTerm(term2);
    // check (sum = 1 + ((3) + 4))
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 1);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 0);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals($longSumBeed.getLong(), 8L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 4
    integerSumBeed2plus3plus4.removeTerm(term4);
    // check (sum = 1 + ((3)))
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 0);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term2) == 0);
    assertTrue(integerSumBeed2plus3.getNbOccurrences(term3) == 1);
    assertEquals($longSumBeed.getLong(), 4L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove integerSumBeed2plus3
    integerSumBeed2plus3plus4.removeTerm(integerSumBeed2plus3);
    // check (sum = 1 + (no terms))
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 1);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(integerSumBeed2plus3) == 0);
    assertTrue(integerSumBeed2plus3plus4.getNbOccurrences(term4) == 0);
    assertEquals($longSumBeed.getLong(), 1L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove integerSumBeed2plus3plus4
    $longSumBeed.removeTerm(integerSumBeed2plus3plus4);
    // check (sum = 1)
    assertTrue($longSumBeed.getNbOccurrences(term1) == 1);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 0);
    assertEquals($longSumBeed.getLong(), 1L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // remove 1
    $longSumBeed.removeTerm(term1);
    // check (sum = no terms)
    assertTrue($longSumBeed.getNbOccurrences(term1) == 0);
    assertTrue($longSumBeed.getNbOccurrences(integerSumBeed2plus3plus4) == 0);
    assertEquals($longSumBeed.getLong(), 0L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
  }

  @Test
  public void recalculate() {
    // create terms
    EditableLongBeed term1 = createEditableIntegerBeed(1L);
    EditableLongBeed term2 = createEditableIntegerBeed(2L);
    EditableLongBeed term3 = createEditableIntegerBeed(3L);
    EditableLongBeed term4 = createEditableIntegerBeed(4L);
    EditableLongBeed termNull = createEditableIntegerBeed(null);
    // create sum beed
    $longSumBeed.addTerm(term1);
    $longSumBeed.addTerm(term2);
    // recalculate
    $longSumBeed.recalculate();
    assertEquals($longSumBeed.getLong(), 3L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // recalculate
    $longSumBeed.recalculate();
    assertEquals($longSumBeed.getLong(), 3L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add term
    $longSumBeed.addTerm(term3);
    // recalculate
    $longSumBeed.recalculate();
    assertEquals($longSumBeed.getLong(), 6L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add term
    $longSumBeed.addTerm(term4);
    // recalculate
    $longSumBeed.recalculate();
    assertEquals($longSumBeed.getLong(), 10L);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add term
    $longSumBeed.addTerm(termNull);
    // recalculate
    $longSumBeed.recalculate();
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
    // add term
    $longSumBeed.addTerm(termNull);
    // recalculate
    $longSumBeed.recalculate();
    assertEquals($longSumBeed.getLong(), null);
    assertTrue(equalValue($longSumBeed.getDouble(), $longSumBeed.getLong()));
  }

}
