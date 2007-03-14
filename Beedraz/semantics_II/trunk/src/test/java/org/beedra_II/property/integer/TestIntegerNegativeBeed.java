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
import static org.ppeew.smallfries_I.ComparisonUtil.equalsWithNull;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.StubAggregateBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.StubListener;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestIntegerNegativeBeed {

  @BeforeClass
  public static void classSetUp() {
    System.out.println("demonstration for todo");
    System.out.println(Integer.MAX_VALUE);
    System.out.println(-Integer.MAX_VALUE);
    System.out.println(Integer.MIN_VALUE);
    System.out.println(-Integer.MIN_VALUE);
    System.out.println(Integer.MIN_VALUE == -Integer.MIN_VALUE);
  }

  @Before
  public void setUp() throws Exception {
    $aggregateBeed = new StubAggregateBeed();
    $argumentIntegerBeed = new EditableIntegerBeed($aggregateBeed);
    $argumentIntegerBeed2 = new EditableIntegerBeed($aggregateBeed);
    $subject = new IntegerNegativeBeed($aggregateBeed);
    $listener = new StubListener<IntegerEvent>();
  }

  @After
  public void tearDown() throws Exception {
    $argumentIntegerBeed = null;
    $argumentIntegerBeed2 = null;
    $subject = null;
    $aggregateBeed = null;
    $listener = null;
  }

  private IntegerNegativeBeed $subject;
  private AggregateBeed $aggregateBeed;
  private EditableIntegerBeed $argumentIntegerBeed;
  private EditableIntegerBeed $argumentIntegerBeed2;
  private int $goal1 = 12;
  private int $goal2 = -33;
  private int $goalMIN = Integer.MIN_VALUE; // - MIN_VALUE == MIN_VALUE (2-bit complement)
  private int $goalMAX = Integer.MAX_VALUE;
  StubListener<IntegerEvent> $listener = new StubListener<IntegerEvent>();

  @Test
  public void testConstructor() {
    IntegerNegativeBeed inb = new IntegerNegativeBeed($aggregateBeed);
    assertEquals($aggregateBeed, inb.getOwner());
    assertNull(inb.getArgument());
    assertNull(inb.getInteger());
    assertNull(inb.getDouble());
  }

  @Test
  public void testSetArgument_IntegerBeed_1() {
    $subject.setArgument(null);
    validateSubjectFromArgument(null);
    validateEvent(false, null, null);
  }

  @Test
  public void testSetArgument_IntegerBeed_2() {
    $subject.setArgument($argumentIntegerBeed);
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(false, null, null);
  }

  @Test
  public void testSetArgument_IntegerBeed_3() throws EditStateException, IllegalEditException {
    IntegerEdit edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal($goal1);
    edit.perform();
    $subject.addListener($listener);
    $subject.setArgument($argumentIntegerBeed);
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(true, null, $goal1);
    $subject.setArgument($argumentIntegerBeed);
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(false, null, null);
    $subject.setArgument(null);
    validateSubjectFromArgument(null);
    validateEvent(true, $goal1, null);
    $subject.setArgument($argumentIntegerBeed);
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(true, null, $goal1);
    edit = new IntegerEdit($argumentIntegerBeed2);
    edit.setGoal($goal2);
    edit.perform();
    $subject.setArgument($argumentIntegerBeed2);
    validateSubjectFromArgument($argumentIntegerBeed2);
    validateEvent(true, $goal1, $goal2);
    edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal($goal2);
    edit.perform();
    $subject.setArgument($argumentIntegerBeed);
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(false, null, null);
  }

  @Test
  public void testIntegerBeed() throws EditStateException, IllegalEditException {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    IntegerEdit edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal($goal1);
    edit.perform();
    $subject.setArgument($argumentIntegerBeed);
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(true, null, $goal1);
    edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal($goal2);
    edit.perform();
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(true, $goal1, $goal2);
    edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal($goal2);
    edit.perform();
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(false, null, null);
    edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal(null);
    edit.perform();
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(true, $goal2, null);
    edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal(null);
    edit.perform();
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(false, null, null);
    edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal($goalMIN);
    edit.perform();
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(true, null, $goalMIN);
    edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal($goalMAX);
    edit.perform();
    validateSubjectFromArgument($argumentIntegerBeed);
    validateEvent(true, $goalMIN, $goalMAX);
  }

  private void validateEvent(boolean eventSent, Integer oldV, Integer newV) {
    if (eventSent) {
      assertNotNull($listener.$event);
      if (oldV == null) {
        assertNull($listener.$event.getOldInteger());
      }
      else {
        assertNotNull($listener.$event.getOldInteger());
        assertEquals(-oldV, $listener.$event.getOldInteger());
      }
      if (newV == null) {
        assertNull($listener.$event.getNewInteger());
      }
      else {
        assertNotNull($listener.$event.getNewInteger());
        assertEquals(-newV, $listener.$event.getNewInteger());
      }
    }
    else {
      assertNull($listener.$event);
    }
    $listener.$event = null;
  }

  private void validateSubjectFromArgument(IntegerBeed argument) {
//    System.out.println("argument: " + argument + "  ##  $subject: "+ $subject);
    assertEquals(argument, $subject.getArgument());
    if (argument != null) {
      assertNotNull($subject.getArgument());
      Integer argumentValue = argument.getInteger();
      if (argumentValue == null) {
        assertNull($subject.getInteger());
      }
      else {
        assertNotNull($subject.getInteger());
        assertEquals(-argumentValue, $subject.getInteger());
      }
      equalsWithNull($subject.getInteger(), $subject.getDouble());
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
    $subject.setArgument($argumentIntegerBeed);
    $subject.toString(stub, 1);
    IntegerEdit edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal($goal1);
    edit.perform();
    $subject.toString(stub, 1);
    validateSubjectFromArgument($argumentIntegerBeed);
    edit = new IntegerEdit($argumentIntegerBeed);
    edit.setGoal($goal2);
    edit.perform();
    $subject.toString(stub, 1);
  }

}
