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

package org.beedra_II.property;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.Beed;
import org.beedra_II.Event;
import org.beedra_II.StubListener;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.StubAggregateBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.path.ConstantPath;
import org.beedra_II.path.NullPath;
import org.beedra_II.path.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public abstract class AbstractTestUnaryExprBeed<_Result_ extends Object,
                                                _ResultEvent_ extends Event,
                                                _Argument_ extends Object,
                                                _ArgumentBeed_ extends Beed<?>,
                                                _UEB_ extends AbstractUnaryExprBeed<_Result_, _ResultEvent_, _ArgumentBeed_, ? extends Event>,
                                                _EAB_ extends _ArgumentBeed_> {

  protected abstract _UEB_ createSubject(AggregateBeed owner);

  protected abstract _EAB_ createEditableArgumentBeed(AggregateBeed owner);

  protected abstract StubListener<_ResultEvent_> createStubListener();

  protected abstract void initGoals();

  protected abstract void changeArgument(_EAB_ editableArgumentBeed, _Argument_ newValue);

  protected final _Result_ expectedValue(_Argument_ argumentValue) {
    if (argumentValue == null) {
      return expectedValueNull();
    }
    else {
      return expectedValueNotNull(argumentValue);
    }
  }

  /**
   * @pre  argumentValue != null;
   */
  protected abstract _Result_ expectedValueNotNull(_Argument_ argumentValue);

  /**
   * @default-return null;
   */
  protected _Result_ expectedValueNull() {
    return null;
  }

  protected abstract _Result_ valueFromSubject(_UEB_ argumentBeed);

  protected abstract _Argument_ valueFrom(_ArgumentBeed_ argumentBeed);

  protected abstract _Result_ oldValueFrom(_ResultEvent_ argumentBeed);

  protected abstract _Result_ newValueFrom(_ResultEvent_ argumentBeed);

  @Before
  public void setUp() throws Exception {
    initGoals();
    $aggregateBeed = new StubAggregateBeed();
    $argumentDoubleBeed = createEditableArgumentBeed($aggregateBeed);
    $argumentDoubleBeedPath = new ConstantPath<_ArgumentBeed_>($argumentDoubleBeed);
    $argumentDoubleBeed2 = createEditableArgumentBeed($aggregateBeed);
    $argumentDoubleBeedPath2 = new ConstantPath<_ArgumentBeed_>($argumentDoubleBeed2);
    $subject = createSubject($aggregateBeed);
    $listener = createStubListener();
  }

  @After
  public void tearDown() throws Exception {
    $argumentDoubleBeed = null;
    $argumentDoubleBeedPath = null;
    $argumentDoubleBeed2 = null;
    $argumentDoubleBeedPath2 = null;
    $subject = null;
    $aggregateBeed = null;
    $listener = null;
    $goal1 = null;
    $goal2 = null;
    $goalMIN = null;
    $goalMAX = null;
  }

  protected _UEB_ $subject;
  protected AggregateBeed $aggregateBeed;
  private _EAB_ $argumentDoubleBeed;
  private Path<_ArgumentBeed_> $argumentDoubleBeedPath;
  private _EAB_ $argumentDoubleBeed2;
  private Path<_ArgumentBeed_> $argumentDoubleBeedPath2;
  protected _Argument_ $goal1;
  protected _Argument_ $goal2;
  protected _Argument_ $goalMIN;
  protected _Argument_ $goalMAX;
  StubListener<_ResultEvent_> $listener;

  @Test
  public void testSetArgument_1() {
    $subject.addListener($listener);
    $subject.setArgumentPath(new NullPath<_ArgumentBeed_>());
    validateSubjectFromArgument(null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetArgument_2() {
    $subject.addListener($listener);
    $subject.setArgumentPath($argumentDoubleBeedPath);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, null, $argumentDoubleBeed, null);
  }

  @Test
  public void testSetArgument_3() {
    changeArgument($argumentDoubleBeed, $goal1);
    $subject.addListener($listener);
    $subject.setArgumentPath($argumentDoubleBeedPath);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, null, $argumentDoubleBeed, $goal1);
    $subject.setArgumentPath($argumentDoubleBeedPath);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent($argumentDoubleBeed, $goal1, $argumentDoubleBeed, $goal1);
    $subject.setArgumentPath(null);
    validateSubjectFromArgument(null);
    validateEvent($argumentDoubleBeed, $goal1, null, null);
    $subject.setArgumentPath($argumentDoubleBeedPath);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, null, $argumentDoubleBeed, $goal1);
    changeArgument($argumentDoubleBeed2, $goal2);
    $subject.setArgumentPath($argumentDoubleBeedPath2);
    validateSubjectFromArgument($argumentDoubleBeed2);
    validateEvent($argumentDoubleBeed, $goal1, $argumentDoubleBeed2, $goal2);
    changeArgument($argumentDoubleBeed, $goal2);
    $subject.setArgumentPath($argumentDoubleBeedPath);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent($argumentDoubleBeed2, $goal2, $argumentDoubleBeed, $goal2);
  }

  @Test
  public void testDynamics() {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    changeArgument($argumentDoubleBeed, $goal1);
    $subject.setArgumentPath($argumentDoubleBeedPath);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent(null, null, $argumentDoubleBeed, $goal1);
    changeArgument($argumentDoubleBeed, $goal2);
    validateSubjectFromArgument($argumentDoubleBeed);
  //    System.out.println("$goal1: " + $goal1);
  //    System.out.println("ln($goal1): " + Math.log((Double)$goal1));
  //    System.out.println("$goal2: " + $goal2);
  //    System.out.println("ln($goal2): " + Math.log((Double)$goal2));
    validateEvent($argumentDoubleBeed, $goal1, $argumentDoubleBeed, $goal2);
    changeArgument($argumentDoubleBeed, $goal2);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent($argumentDoubleBeed, $goal2, $argumentDoubleBeed, $goal2);
    changeArgument($argumentDoubleBeed, null);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent($argumentDoubleBeed, $goal2, $argumentDoubleBeed, null);
    changeArgument($argumentDoubleBeed, null);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent($argumentDoubleBeed, null, $argumentDoubleBeed, null);
    changeArgument($argumentDoubleBeed, $goalMIN);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent($argumentDoubleBeed, null, $argumentDoubleBeed, $goalMIN);
    changeArgument($argumentDoubleBeed, $goalMAX);
    validateSubjectFromArgument($argumentDoubleBeed);
    validateEvent($argumentDoubleBeed, $goalMIN, $argumentDoubleBeed, $goalMAX);
  }

  private void validateEvent(_ArgumentBeed_ oldBeed, _Argument_ oldV,
                             _ArgumentBeed_ newBeed, _Argument_ newV) {
    _Result_ expectedOldValue =
      oldBeed == null
        ? null
        : expectedValue(oldV);
    _Result_ expectedNewValue =
      newBeed == null
        ? null
        : expectedValue(newV);
    if (! $subject.equalValue(expectedOldValue, expectedNewValue)) {
      assertNotNull($listener.$event);
      checkOldValue(oldBeed, oldV, $listener.$event);
      checkNewValue(newBeed, newV, $listener.$event);
    }
    else {
      assertNull($listener.$event);
    }
    $listener.$event = null;
  }

  protected void checkOldValue(_ArgumentBeed_ oldBeed, _Argument_ oldV, _ResultEvent_ event) {
    if (oldBeed == null) {
      assertNull(oldValueFrom(event));
    }
    else if (oldV == null) {
      checkValueNull(oldValueFrom(event));
    }
    else {
      assertNotNull(oldValueFrom(event));
      assertTrue($subject.equalValue(expectedValue(oldV), oldValueFrom(event)));
    }
  }

  private void checkValueNull(_Result_ value) {
    assertEquals(expectedValueNull(), value);
  }

  protected void checkNewValue(_ArgumentBeed_ newBeed, _Argument_ newV, _ResultEvent_ event) {
    if (newBeed == null) {
      assertNull(newValueFrom(event));
    }
    else if (newV == null) {
      checkValueNull(newValueFrom(event));
    }
    else {
      assertNotNull(newValueFrom(event));
      assertTrue($subject.equalValue(expectedValue(newV), newValueFrom(event)));
    }
  }

  private void validateSubjectFromArgument(_EAB_ argument) {
//    System.out.println("argument: " + argument + "  ##  $subject: "+ $subject);
    assertEquals(argument, $subject.getArgument());
    if (argument != null) {
      assertNotNull($subject.getArgument());
      _Argument_ argumentValue = valueFrom(argument);
      checkArgumentValue(argument, argumentValue);
//      equalsWithNull($subject.getDouble(), $subject.getDouble());
    }
    else {
      assertNull($subject.getArgument());
    }
  }

  protected void checkArgumentValue(_ArgumentBeed_ argumentBeed, _Argument_ argumentValue) {
    if (argumentBeed == null) {
      assertNull($subject.get());
    }
    else if (argumentValue == null) {
      checkValueNull($subject.get());
    }
    else {
      assertNotNull($subject.get());
      assertTrue($subject.equalValue(expectedValue(argumentValue), valueFromSubject($subject)));
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
    $subject.setArgumentPath($argumentDoubleBeedPath);
    $subject.toString(stub, 1);
    changeArgument($argumentDoubleBeed, $goal1);
    $subject.toString(stub, 1);
    changeArgument($argumentDoubleBeed, $goal2);
    $subject.toString(stub, 1);
  }

}
