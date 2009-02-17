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

package org.beedraz.semantics_II.expression;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.aggregate.StubAggregateBeed;
import org.beedraz.semantics_II.path.ConstantPath;
import org.beedraz.semantics_II.path.NullPath;
import org.beedraz.semantics_II.path.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractTestUnaryExprBeed<_Result_ extends Object,
                                                _ResultEvent_ extends Event,
                                                _Operand_ extends Object,
                                                _OperandBeed_ extends Beed<?>,
                                                _UEB_ extends AbstractUnaryExprBeed<_Result_, _ResultEvent_, _OperandBeed_>,
                                                _EdOpB_ extends _OperandBeed_> {

  protected abstract _UEB_ createSubject();

  protected abstract _EdOpB_ createEditableOperandBeed(AggregateBeed owner);

  protected abstract StubListener<_ResultEvent_> createStubListener();

  protected abstract void initGoals();

  protected abstract void changeOperand(_EdOpB_ editableOperandBeed, _Operand_ newValue);

  protected final _Result_ expectedValue(_Operand_ operandValue) {
    if (operandValue == null) {
      return expectedValueNull();
    }
    else {
      return expectedValueNotNull(operandValue);
    }
  }

  /**
   * @pre  operandValue != null;
   */
  protected abstract _Result_ expectedValueNotNull(_Operand_ operandValue);

  /**
   * @default-return null;
   */
  protected _Result_ expectedValueNull() {
    return null;
  }

  protected abstract _Result_ valueFromSubject(_UEB_ operandBeed);

  protected abstract _Operand_ valueFrom(_OperandBeed_ operandBeed);

  protected abstract _Result_ oldValueFrom(_ResultEvent_ operandBeed);

  protected abstract _Result_ newValueFrom(_ResultEvent_ operandBeed);

  @Before
  public void setUp() throws Exception {
    initGoals();
    $aggregateBeed = new StubAggregateBeed();
    $operandDoubleBeed = createEditableOperandBeed($aggregateBeed);
    $operandDoubleBeedPath = new ConstantPath<_OperandBeed_>($operandDoubleBeed);
    $operandDoubleBeed2 = createEditableOperandBeed($aggregateBeed);
    $operandDoubleBeedPath2 = new ConstantPath<_OperandBeed_>($operandDoubleBeed2);
    $subject = createSubject();
    $listener = createStubListener();
  }

  @After
  public void tearDown() throws Exception {
    $operandDoubleBeed = null;
    $operandDoubleBeedPath = null;
    $operandDoubleBeed2 = null;
    $operandDoubleBeedPath2 = null;
    $subject = null;
    $aggregateBeed = null;
    $listener = null;
    $goal1 = null;
    $goal2 = null;
    $goalMIN = null;
    $goalMAX = null;
    $goalNaN = null;
  }

  protected _UEB_ $subject;
  protected AggregateBeed $aggregateBeed;
  private _EdOpB_ $operandDoubleBeed;
  private Path<_OperandBeed_> $operandDoubleBeedPath;
  private _EdOpB_ $operandDoubleBeed2;
  private Path<_OperandBeed_> $operandDoubleBeedPath2;
  protected _Operand_ $goal1;
  protected _Operand_ $goal2;
  protected _Operand_ $goalMIN;
  protected _Operand_ $goalMAX;
  protected _Operand_ $goalNaN;
  StubListener<_ResultEvent_> $listener;

  @Test
  public void testSetOperand_1() {
    $subject.addListener($listener);
    $subject.setOperandPath(new NullPath<_OperandBeed_>());
    validateSubjectFromOperand(null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_2() {
    $subject.addListener($listener);
    $subject.setOperandPath($operandDoubleBeedPath);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent(null, null, $operandDoubleBeed, null);
  }

  @Test
  public void testSetOperand_3() {
    changeOperand($operandDoubleBeed, $goal1);
    $subject.addListener($listener);
    $subject.setOperandPath($operandDoubleBeedPath);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent(null, null, $operandDoubleBeed, $goal1);
    $subject.setOperandPath($operandDoubleBeedPath);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent($operandDoubleBeed, $goal1, $operandDoubleBeed, $goal1);
    $subject.setOperandPath(null);
    validateSubjectFromOperand(null);
    validateEvent($operandDoubleBeed, $goal1, null, null);
    $subject.setOperandPath($operandDoubleBeedPath);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent(null, null, $operandDoubleBeed, $goal1);
    changeOperand($operandDoubleBeed2, $goal2);
    $subject.setOperandPath($operandDoubleBeedPath2);
    validateSubjectFromOperand($operandDoubleBeed2);
    validateEvent($operandDoubleBeed, $goal1, $operandDoubleBeed2, $goal2);
    changeOperand($operandDoubleBeed, $goal2);
    $subject.setOperandPath($operandDoubleBeedPath);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent($operandDoubleBeed2, $goal2, $operandDoubleBeed, $goal2);
  }

  @Test
  public void testDynamics() {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    changeOperand($operandDoubleBeed, $goal1);
    $subject.setOperandPath($operandDoubleBeedPath);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent(null, null, $operandDoubleBeed, $goal1);
    changeOperand($operandDoubleBeed, $goal2);
    validateSubjectFromOperand($operandDoubleBeed);
  //    System.out.println("$goal1: " + $goal1);
  //    System.out.println("ln($goal1): " + Math.log((Double)$goal1));
  //    System.out.println("$goal2: " + $goal2);
  //    System.out.println("ln($goal2): " + Math.log((Double)$goal2));
    validateEvent($operandDoubleBeed, $goal1, $operandDoubleBeed, $goal2);
    changeOperand($operandDoubleBeed, $goal2);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent($operandDoubleBeed, $goal2, $operandDoubleBeed, $goal2);
    changeOperand($operandDoubleBeed, null);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent($operandDoubleBeed, $goal2, $operandDoubleBeed, null);
    changeOperand($operandDoubleBeed, null);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent($operandDoubleBeed, null, $operandDoubleBeed, null);
    changeOperand($operandDoubleBeed, $goalMIN);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent($operandDoubleBeed, null, $operandDoubleBeed, $goalMIN);
    changeOperand($operandDoubleBeed, $goalMAX);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent($operandDoubleBeed, $goalMIN, $operandDoubleBeed, $goalMAX);
    changeOperand($operandDoubleBeed, $goalNaN);
    validateSubjectFromOperand($operandDoubleBeed);
    validateEvent($operandDoubleBeed, $goalMAX, $operandDoubleBeed, $goalNaN);
  }

  private void validateEvent(_OperandBeed_ oldBeed, _Operand_ oldV,
                             _OperandBeed_ newBeed, _Operand_ newV) {
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

  protected void checkOldValue(_OperandBeed_ oldBeed, _Operand_ oldV, _ResultEvent_ event) {
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

  protected void checkNewValue(_OperandBeed_ newBeed, _Operand_ newV, _ResultEvent_ event) {
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

  private void validateSubjectFromOperand(_EdOpB_ operand) {
//    System.out.println("operand: " + operand + "  ##  $subject: "+ $subject);
    assertEquals(operand, $subject.getOperand());
    if (operand != null) {
      assertNotNull($subject.getOperand());
      _Operand_ operandValue = valueFrom(operand);
      checkOperandValue(operand, operandValue);
//      equalsWithNull($subject.getDouble(), $subject.getDouble());
    }
    else {
      assertNull($subject.getOperand());
    }
  }

  protected void checkOperandValue(_OperandBeed_ operandBeed, _Operand_ operandValue) {
    if (operandBeed == null) {
      assertNull($subject.get());
    }
    else if (operandValue == null) {
      checkValueNull($subject.get());
    }
    else {
      assertNotNull($subject.get());
      assertTrue($subject.equalValue(expectedValue(operandValue), valueFromSubject($subject)));
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
    $subject.setOperandPath($operandDoubleBeedPath);
    $subject.toString(stub, 1);
    changeOperand($operandDoubleBeed, $goal1);
    $subject.toString(stub, 1);
    changeOperand($operandDoubleBeed, $goal2);
    $subject.toString(stub, 1);
  }

}
