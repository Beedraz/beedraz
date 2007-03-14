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

package org.beedra_II.property.number.decimal;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.smallfries_I.ComparisonUtil.equalsWithNull;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.StubAggregateBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.decimal.DoubleBeed;
import org.beedra_II.property.decimal.DoubleEdit;
import org.beedra_II.property.decimal.DoubleEvent;
import org.beedra_II.property.decimal.EditableDoubleBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestDoubleNegativeBeed {

  @Before
  public void setUp() throws Exception {
    $aggregateBeed = new StubAggregateBeed();
    $argumentDoubleBeed = new EditableDoubleBeed($aggregateBeed);
    $argumentDoubleBeed2 = new EditableDoubleBeed($aggregateBeed);
    $subject = new DoubleNegativeBeed($aggregateBeed);
    $listener = new StubListener<DoubleEvent>();
  }

  @After
  public void tearDown() throws Exception {
    $argumentDoubleBeed = null;
    $argumentDoubleBeed2 = null;
    $subject = null;
    $aggregateBeed = null;
    $listener = null;
  }

  private DoubleNegativeBeed $subject;
  private AggregateBeed $aggregateBeed;
  private EditableDoubleBeed $argumentDoubleBeed;
  private EditableDoubleBeed $argumentDoubleBeed2;
  private double $goal1 = -Math.E;
  private double $goal2 = Double.NEGATIVE_INFINITY;
  private double $goalMIN = Double.MIN_VALUE; // - MIN_VALUE == MIN_VALUE (2-bit complement)
  private double $goalMAX = Double.MAX_VALUE;
  StubListener<DoubleEvent> $listener = new StubListener<DoubleEvent>();

  @Test
  public void testConstructor() {
    DoubleNegativeBeed inb = new DoubleNegativeBeed($aggregateBeed);
    assertEquals($aggregateBeed, inb.getOwner());
    assertNull(inb.getArgument());
    assertNull(inb.getDouble());
    assertNull(inb.getDouble());
  }

  @Test
  public void testSetArgument_DoubleBeed_1() {
    $subject.setArgument(null);
    validateSubjectFromArgument(null);
    validateEvent(false, null, null);
  }

  @Test
  public void testSetArgument_DoubleBeed_2() {
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(false, null, null);
  }

  @Test
  public void testSetArgument_DoubleBeed_3() throws EditStateException, IllegalEditException {
    DoubleEdit edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal($goal1);
    edit.perform();
    $subject.addListener($listener);
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(true, null, $goal1);
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(false, null, null);
    $subject.setArgument(null);
    validateSubjectFromArgument(null);
    validateEvent(true, $goal1, null);
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(true, null, $goal1);
    edit = new DoubleEdit($argumentDoubleBeed2);
    edit.setGoal($goal2);
    edit.perform();
    $subject.setArgument($argumentDoubleBeed2);
    validateSubjectFromArgument($argumentDoubleBeed2);
    validateEvent(true, $goal1, $goal2);
    edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal($goal2);
    edit.perform();
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(false, null, null);
  }

  @Test
  public void testDoubleBeed() throws EditStateException, IllegalEditException {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    DoubleEdit edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal($goal1);
    edit.perform();
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(true, null, $goal1);
    edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal($goal2);
    edit.perform();
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(true, $goal1, $goal2);
    edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal($goal2);
    edit.perform();
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(false, null, null);
    edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal(null);
    edit.perform();
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(true, $goal2, null);
    edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal(null);
    edit.perform();
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(false, null, null);
    edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal($goalMIN);
    edit.perform();
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(true, null, $goalMIN);
    edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal($goalMAX);
    edit.perform();
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(true, $goalMIN, $goalMAX);
  }

  private void validateEvent(boolean eventSent, Double oldV, Double newV) {
    if (eventSent) {
      assertNotNull($listener.$event);
      if (oldV == null) {
        assertNull($listener.$event.getOldDouble());
      }
      else {
        assertNotNull($listener.$event.getOldDouble());
        assertEquals(-oldV, $listener.$event.getOldDouble());
      }
      if (newV == null) {
        assertNull($listener.$event.getNewDouble());
      }
      else {
        assertNotNull($listener.$event.getNewDouble());
        assertEquals(-newV, $listener.$event.getNewDouble());
      }
    }
    else {
      assertNull($listener.$event);
    }
    $listener.$event = null;
  }

  private void validateSubjectFromArgument(DoubleBeed<?> argument) {
    System.out.println("argument: " + argument + "  ##  $subject: "+ $subject);
    assertEquals(argument, $subject.getArgument());
    if (argument != null) {
      assertNotNull($subject.getArgument());
      Double argumentValue = argument.getDouble();
      if (argumentValue == null) {
        assertNull($subject.getDouble());
      }
      else {
        assertNotNull($subject.getDouble());
        assertEquals(-argumentValue, $subject.getDouble());
      }
      equalsWithNull($subject.getDouble(), $subject.getDouble());
    }
    else {
      assertNull($subject.getArgument());
    }
  }

  @Test
  public void testToString() {
    String result = $subject.toString();
    assertNotNull(result);
  }

  @Test
  public void testToString_StringBuffer_int_1() {
    StringBuffer stub = new StringBuffer();
    $subject.toString(stub, 1);
  }

  @Test
  public void testToString_StringBuffer_int_2() throws EditStateException, IllegalEditException {
    StringBuffer stub = new StringBuffer();
    $subject.toString(stub, 1);
    $subject.setArgument($argumentDoubleBeed);
    $subject.toString(stub, 1);
    DoubleEdit edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal($goal1);
    edit.perform();
    $subject.toString(stub, 1);
    validateSubjectFromArgument($argumentDoubleBeed);
    edit = new DoubleEdit($argumentDoubleBeed);
    edit.setGoal($goal2);
    edit.perform();
    $subject.toString(stub, 1);
  }

}
