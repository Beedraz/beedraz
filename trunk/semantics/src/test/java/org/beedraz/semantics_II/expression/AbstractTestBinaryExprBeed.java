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
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.aggregate.StubAggregateBeed;
import org.beedraz.semantics_II.edit.EditStateException;
import org.beedraz.semantics_II.edit.IllegalEditException;
import org.beedraz.semantics_II.expression.AbstractBinaryExprBeed;
import org.beedraz.semantics_II.path.ConstantPath;
import org.beedraz.semantics_II.path.NullPath;
import org.beedraz.semantics_II.path.Path;
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
public abstract class AbstractTestBinaryExprBeed<_Result_ extends Object,
                                                 _ResultEvent_ extends Event,
                                                 _Operand_ extends Object,
                                                 _LeftOperandBeed_ extends Beed<?>,
                                                 _RightOperandBeed_ extends Beed<?>,
                                                 _UEB_ extends AbstractBinaryExprBeed<_Result_, _ResultEvent_, _LeftOperandBeed_, ? extends Event, _RightOperandBeed_, ? extends Event>,
                                                 _LeftEdOpB_ extends _LeftOperandBeed_,
                                                 _RightEdOpB_ extends _RightOperandBeed_> {


  protected abstract _UEB_ createSubject();

  protected abstract _LeftEdOpB_ createEditableLeftOperandBeed(AggregateBeed owner);

  protected abstract _RightEdOpB_ createEditableRightOperandBeed(AggregateBeed owner);

  protected abstract StubListener<_ResultEvent_> createStubListener();

  protected abstract void initGoals();

  protected abstract void setLeftOperandPath(Path<? extends _LeftOperandBeed_> leftOperandPath);

  protected abstract void setRightOperandPath(Path<? extends _RightOperandBeed_> rightOperandPath);

  protected abstract _LeftOperandBeed_ getLeftOperand();

  protected abstract _RightOperandBeed_ getRightOperand();

  protected abstract void changeLeftOperand(_LeftEdOpB_ editableOperandBeed, _Operand_ newValue);

  protected abstract void changeRightOperand(_RightEdOpB_ editableOperandBeed, _Operand_ newValue);

  protected abstract _Result_ expectedValue(_Operand_ leftOperandValue, _Operand_ rightOperandValue);

  protected abstract _Result_ valueFromSubject(_UEB_ operandBeed);

  protected abstract _Operand_ valueFromLeft(_LeftOperandBeed_ operandBeed);

  protected abstract _Operand_ valueFromRight(_RightOperandBeed_ operandBeed);

  protected abstract _Result_ oldValueFrom(_ResultEvent_ operandBeed);

  protected abstract _Result_ newValueFrom(_ResultEvent_ operandBeed);

  @Before
  public void setUp() throws Exception {
    initGoals();
    $aggregateBeed = new StubAggregateBeed();
    $leftOperandDoubleBeed = createEditableLeftOperandBeed($aggregateBeed);
    $leftOperandDoubleBeedPath = new ConstantPath<_LeftEdOpB_>($leftOperandDoubleBeed);
    $leftOperandDoubleBeed2 = createEditableLeftOperandBeed($aggregateBeed);
    $leftOperandDoubleBeedPath2 = new ConstantPath<_LeftEdOpB_>($leftOperandDoubleBeed2);
    $rightOperandDoubleBeed = createEditableRightOperandBeed($aggregateBeed);
    $rightOperandDoubleBeedPath = new ConstantPath<_RightEdOpB_>($rightOperandDoubleBeed);
    $rightOperandDoubleBeed2 = createEditableRightOperandBeed($aggregateBeed);
    $rightOperandDoubleBeedPath2 = new ConstantPath<_RightEdOpB_>($rightOperandDoubleBeed2);
    $subject = createSubject();
    $listener = createStubListener();
  }

  @After
  public void tearDown() throws Exception {
    $leftOperandDoubleBeed = null;
    $leftOperandDoubleBeedPath = null;
    $leftOperandDoubleBeed2 = null;
    $leftOperandDoubleBeedPath2 = null;
    $rightOperandDoubleBeed = null;
    $rightOperandDoubleBeedPath = null;
    $rightOperandDoubleBeed2 = null;
    $rightOperandDoubleBeedPath2 = null;
    $subject = null;
    $aggregateBeed = null;
    $listener = null;
    $leftGoal1 = null;
    $leftGoal2 = null;
    $leftGoalMIN = null;
    $leftGoalMAX = null;
    $rightGoal1 = null;
    $rightGoal2 = null;
    $rightGoalMIN = null;
    $rightGoalMAX = null;
  }

  protected _UEB_ $subject;
  protected AggregateBeed $aggregateBeed;
  private _LeftEdOpB_ $leftOperandDoubleBeed;
  private Path<_LeftEdOpB_> $leftOperandDoubleBeedPath;
  private _LeftEdOpB_ $leftOperandDoubleBeed2;
  private Path<_LeftEdOpB_> $leftOperandDoubleBeedPath2;
  private _RightEdOpB_ $rightOperandDoubleBeed;
  private Path<_RightEdOpB_> $rightOperandDoubleBeedPath;
  private _RightEdOpB_ $rightOperandDoubleBeed2;
  private Path<_RightEdOpB_> $rightOperandDoubleBeedPath2;
  protected _Operand_ $leftGoal1;
  protected _Operand_ $leftGoal2;
  protected _Operand_ $leftGoalMIN;
  protected _Operand_ $leftGoalMAX;
  protected _Operand_ $rightGoal1;
  protected _Operand_ $rightGoal2;
  protected _Operand_ $rightGoalMIN;
  protected _Operand_ $rightGoalMAX;
  StubListener<_ResultEvent_> $listener;

  @Test
  public void testSetOperand_1L() {
    $subject.addListener($listener);
    setLeftOperandPath(new NullPath<_LeftOperandBeed_>());
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_1R() {
    $subject.addListener($listener);
    setRightOperandPath(new NullPath<_RightOperandBeed_>());
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_1LR() {
    $subject.addListener($listener);
    setLeftOperandPath(new NullPath<_LeftOperandBeed_>());
    setRightOperandPath(new NullPath<_RightOperandBeed_>());
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_2L() {
    $subject.addListener($listener);
    setLeftOperandPath($leftOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_2R() {
    $subject.addListener($listener);
    setRightOperandPath($rightOperandDoubleBeedPath);
    validateSubjectFromOperand(null, $rightOperandDoubleBeed);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_2LR() {
    $subject.addListener($listener);
    setLeftOperandPath($leftOperandDoubleBeedPath);
    setRightOperandPath($rightOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_3L() {
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal1);
    changeRightOperand($rightOperandDoubleBeed, $rightGoal1);
    $subject.addListener($listener);
    setLeftOperandPath($leftOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, null);
    validateEvent(null, $leftGoal1, null, null);
    setRightOperandPath($rightOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent($leftGoal1, $leftGoal1, null, $rightGoal1);
    setLeftOperandPath($leftOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent(null, null, $rightGoal1, $rightGoal1);
    setLeftOperandPath(new NullPath<_LeftOperandBeed_>());
    validateSubjectFromOperand(null, $rightOperandDoubleBeed);
    validateEvent($leftGoal1, null, $rightGoal1, $rightGoal1);
    setLeftOperandPath($leftOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent(null, $leftGoal1, $rightGoal1, $rightGoal1);
    changeLeftOperand($leftOperandDoubleBeed2, $leftGoal2);
    setLeftOperandPath($leftOperandDoubleBeedPath2);
    validateSubjectFromOperand($leftOperandDoubleBeed2, $rightOperandDoubleBeed);
    validateEvent($leftGoal1, $leftGoal2, $rightGoal1, $rightGoal1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal2);
    setLeftOperandPath($leftOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent(null, null, $rightGoal1, $rightGoal1);
  }

  @Test
  public void testSetOperand_3R() {
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal1);
    changeRightOperand($rightOperandDoubleBeed, $rightGoal1);
    $subject.addListener($listener);
    setRightOperandPath($rightOperandDoubleBeedPath);
    validateSubjectFromOperand(null, $rightOperandDoubleBeed);
    validateEvent(null, null, null, $rightGoal1);
    setLeftOperandPath($leftOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent(null, $leftGoal1, $rightGoal1, $rightGoal1);
    setRightOperandPath($rightOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent($leftGoal1, $leftGoal1, null, null);
    setRightOperandPath(new NullPath<_RightOperandBeed_>());
    validateSubjectFromOperand($leftOperandDoubleBeed, null);
    validateEvent($leftGoal1, $leftGoal1, $rightGoal1, null);
    setRightOperandPath($rightOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent($leftGoal1, $leftGoal1, null, $rightGoal1);
    changeRightOperand($rightOperandDoubleBeed2, $rightGoal2);
    setRightOperandPath($rightOperandDoubleBeedPath2);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed2);
    validateEvent($leftGoal1, $leftGoal1, $rightGoal1, $rightGoal2);
    changeRightOperand($rightOperandDoubleBeed, $rightGoal2);
    setRightOperandPath($rightOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent($leftGoal1, $leftGoal1, null, null);
  }


  @Test
  public void testDynamics() {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal1);
    setLeftOperandPath($leftOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, null);
    validateEvent(null, null, null, null);
    changeRightOperand($rightOperandDoubleBeed, $rightGoal1);
    setRightOperandPath($rightOperandDoubleBeedPath);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent($leftGoal1, $leftGoal1, null, $rightGoal1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal2);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent($leftGoal1, $leftGoal2, $rightGoal1, $rightGoal1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal2);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent(null, null, $rightGoal1, $rightGoal1);
    changeLeftOperand($leftOperandDoubleBeed, null);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent($leftGoal2, null, $rightGoal1, $rightGoal1);
    changeLeftOperand($leftOperandDoubleBeed, null);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent(null, null, $rightGoal1, $rightGoal1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoalMIN);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent(null, $leftGoalMIN, $rightGoal1, $rightGoal1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoalMAX);
    validateSubjectFromOperand($leftOperandDoubleBeed, $rightOperandDoubleBeed);
    validateEvent($leftGoalMIN, $leftGoalMAX, $rightGoal1, $rightGoal1);
    // MUDO more tests changing right operand
  }

  private void validateEvent(_Operand_ oldLeftV, _Operand_ newLeftV, _Operand_ oldRightV, _Operand_ newRightV) {
    _Result_ expectedOldValue = ((oldLeftV == null) || (oldRightV == null)) ? null : expectedValue(oldLeftV, oldRightV);
    _Result_ expectedNewValue = ((newLeftV == null) || (newRightV == null)) ? null : expectedValue(newLeftV, newRightV);
    if (! $subject.equalValue(expectedOldValue, expectedNewValue)) {
      assertNotNull($listener.$event);
      if ((oldLeftV == null) || (oldRightV == null)) {
        assertNull(oldValueFrom($listener.$event));
      }
      else {
        assertNotNull(oldValueFrom($listener.$event));
        assertTrue($subject.equalValue(expectedValue(oldLeftV, oldRightV), oldValueFrom($listener.$event)));
      }
      if ((newLeftV == null) || (newRightV == null)) {
        assertNull(newValueFrom($listener.$event));
      }
      else {
        assertNotNull(newValueFrom($listener.$event));
        assertTrue($subject.equalValue(expectedValue(newLeftV, newRightV), newValueFrom($listener.$event)));
      }
    }
    else {
      assertNull($listener.$event);
    }
    $listener.$event = null;
  }

  private void validateSubjectFromOperand(_LeftEdOpB_ leftOperand, _RightEdOpB_ rightOperand) {
//    System.out.println("operand: " + leftOperand + "  ##  $subject: "+ $subject);
    assertEquals(leftOperand, getLeftOperand());
    assertEquals(rightOperand, getRightOperand());
    _Operand_ leftOperandValue = null;
    _Operand_ rightOperandValue = null;
    if (leftOperand != null) {
      assertNotNull(getLeftOperand());
      leftOperandValue = valueFromLeft(leftOperand);
      if (leftOperandValue == null) {
        assertNull($subject.get());
      }
    }
    else {
      assertNull(getLeftOperand());
    }
    if (rightOperand != null) {
      assertNotNull(getRightOperand());
      rightOperandValue = valueFromRight(rightOperand);
      if (rightOperandValue == null) {
        assertNull($subject.get());
      }
    }
    else {
      assertNull(getRightOperand());
    }
    if ((leftOperand != null) && (rightOperand != null) && (leftOperandValue != null) && (rightOperandValue != null)) {
      assertNotNull($subject.get());
      assertTrue($subject.equalValue(expectedValue(leftOperandValue, rightOperandValue), valueFromSubject($subject)));
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
  public void testToString_StringBuffer_int_2L() throws EditStateException, IllegalEditException {
    StringBuffer stub = new StringBuffer();
    $subject.toString(stub, 1);
    setLeftOperandPath($leftOperandDoubleBeedPath);
    $subject.toString(stub, 1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal1);
    $subject.toString(stub, 1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal2);
    $subject.toString(stub, 1);

    setRightOperandPath($rightOperandDoubleBeedPath);
    $subject.toString(stub, 1);

    setLeftOperandPath($leftOperandDoubleBeedPath);
    $subject.toString(stub, 1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal1);
    $subject.toString(stub, 1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal2);
    $subject.toString(stub, 1);

    changeRightOperand($rightOperandDoubleBeed, $leftGoal1);
    $subject.toString(stub, 1);

    setLeftOperandPath($leftOperandDoubleBeedPath);
    $subject.toString(stub, 1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal1);
    $subject.toString(stub, 1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal2);
    $subject.toString(stub, 1);

    changeRightOperand($rightOperandDoubleBeed, $leftGoal2);
    $subject.toString(stub, 1);

    setLeftOperandPath($leftOperandDoubleBeedPath);
    $subject.toString(stub, 1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal1);
    $subject.toString(stub, 1);
    changeLeftOperand($leftOperandDoubleBeed, $leftGoal2);
    $subject.toString(stub, 1);
  }

}
