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

package org.beedra_II.property.number;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.smallfries_I.MathUtil.equalValue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.StubAggregateBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.RealEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.smallfries_I.MathUtil;


public abstract class AbstractTestUnaryExpressionBeed<_Number_ extends Number,
                                                      _ArgumentBeed_ extends RealBeed<?>,
                                                      _NumberEvent_ extends RealEvent,
                                                      _UEB_ extends AbstractUnaryExpressionBeed<_Number_, _ArgumentBeed_, ?, _NumberEvent_>,
                                                      _EAB_ extends _ArgumentBeed_> {

  protected abstract _UEB_ createSubject(AggregateBeed owner);

  protected abstract _EAB_ createEditableArgumentBeed(AggregateBeed owner);

  protected abstract StubListener<_NumberEvent_> createStubListener();

  protected abstract void initGoals();

  protected abstract void changeArgument(_EAB_ editableArgumentBeed, _Number_ newValue);

  protected abstract _Number_ expectedValue(_Number_ argumentValue);

  protected abstract _Number_ valueFromSubject(_UEB_ argumentBeed);

  protected abstract _Number_ valueFrom(_ArgumentBeed_ argumentBeed);

  protected abstract _Number_ oldValueFrom(_NumberEvent_ argumentBeed);

  protected abstract _Number_ newValueFrom(_NumberEvent_ argumentBeed);

  @Before
  public void setUp() throws Exception {
    initGoals();
    $aggregateBeed = new StubAggregateBeed();
    $argumentDoubleBeed = createEditableArgumentBeed($aggregateBeed);
    $argumentDoubleBeed2 = createEditableArgumentBeed($aggregateBeed);
    $subject = createSubject($aggregateBeed);
    $listener = createStubListener();
  }

  @After
  public void tearDown() throws Exception {
    $argumentDoubleBeed = null;
    $argumentDoubleBeed2 = null;
    $subject = null;
    $aggregateBeed = null;
    $listener = null;
    $goal1 = null;
    $goal2 = null;
    $goalMIN = null;
    $goalMAX = null;
  }

  private _UEB_ $subject;
  protected AggregateBeed $aggregateBeed;
  private _EAB_ $argumentDoubleBeed;
  private _EAB_ $argumentDoubleBeed2;
  protected _Number_ $goal1;
  protected _Number_ $goal2;
  protected _Number_ $goalMIN;
  protected _Number_ $goalMAX;
  StubListener<_NumberEvent_> $listener;

  @Test
  public void testSetArgument_1() {
    $subject.setArgument(null);
    validateSubjectFromArgument(null);
    validateEvent(null, null);
  }

  @Test
  public void testSetArgument_2() {
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, null);
  }

  @Test
  public void testSetArgument_3() {
    changeArgument($argumentDoubleBeed, $goal1);
    $subject.addListener($listener);
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, $goal1);
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, null);
    $subject.setArgument(null);
    validateSubjectFromArgument(null);
    validateEvent($goal1, null);
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, $goal1);
    changeArgument($argumentDoubleBeed2, $goal2);
    $subject.setArgument($argumentDoubleBeed2);
    validateSubjectFromArgument($argumentDoubleBeed2);
    validateEvent($goal1, $goal2);
    changeArgument($argumentDoubleBeed, $goal2);
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, null);
  }

  @Test
  public void testDynamics() {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    changeArgument($argumentDoubleBeed, $goal1);
    $subject.setArgument($argumentDoubleBeed);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, $goal1);
    changeArgument($argumentDoubleBeed, $goal2);
    validateSubjectFromArgument($argumentDoubleBeed);
  //    System.out.println("$goal1: " + $goal1);
  //    System.out.println("ln($goal1): " + Math.log((Double)$goal1));
  //    System.out.println("$goal2: " + $goal2);
  //    System.out.println("ln($goal2): " + Math.log((Double)$goal2));
    validateEvent($goal1, $goal2);
    changeArgument($argumentDoubleBeed, $goal2);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, null);
    changeArgument($argumentDoubleBeed, null);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent($goal2, null);
    changeArgument($argumentDoubleBeed, null);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, null);
    changeArgument($argumentDoubleBeed, $goalMIN);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, $goalMIN);
    changeArgument($argumentDoubleBeed, $goalMAX);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent($goalMIN, $goalMAX);
  }

  private void validateEvent(_Number_ oldV, _Number_ newV) {
    _Number_ expectedOldValue = oldV == null ? null : expectedValue(oldV);
    _Number_ expectedNewValue = newV == null ? null : expectedValue(newV);
    if (! MathUtil.equalValue(expectedOldValue, expectedNewValue)) {
      assertNotNull($listener.$event);
      if (oldV == null) {
        assertNull(oldValueFrom($listener.$event));
      }
      else {
        assertNotNull($listener.$event.getOldDouble());
        assertTrue(equalValue(expectedValue(oldV), oldValueFrom($listener.$event)));
      }
      if (newV == null) {
        assertNull(newValueFrom($listener.$event));
      }
      else {
        assertNotNull(newValueFrom($listener.$event));
        assertTrue(equalValue(expectedValue(newV), newValueFrom($listener.$event)));
      }
    }
    else {
      assertNull($listener.$event);
    }
    $listener.$event = null;
  }

  private void validateSubjectFromArgument(_EAB_ argument) {
//    System.out.println("argument: " + argument + "  ##  $subject: "+ $subject);
    assertEquals(argument, $subject.getArgument());
    if (argument != null) {
      assertNotNull($subject.getArgument());
      _Number_ argumentValue = valueFrom(argument);
      if (argumentValue == null) {
        assertNull($subject.getDouble());
      }
      else {
        assertNotNull($subject.getDouble());
        assertTrue(equalValue(expectedValue(argumentValue), valueFromSubject($subject)));
      }
//      equalsWithNull($subject.getDouble(), $subject.getDouble());
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
    changeArgument($argumentDoubleBeed, $goal1);
    $subject.toString(stub, 1);
    changeArgument($argumentDoubleBeed, $goal2);
    $subject.toString(stub, 1);
  }

}
