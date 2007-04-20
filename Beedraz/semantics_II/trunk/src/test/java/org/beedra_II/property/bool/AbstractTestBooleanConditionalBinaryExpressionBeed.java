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

package org.beedra_II.property.bool;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.smallfries_I.MathUtil.equalValue;

import org.beedra_II.StubListener;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.StubAggregateBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.path.ConstantPath;
import org.beedra_II.path.NullPath;
import org.beedra_II.path.Path;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public abstract class AbstractTestBooleanConditionalBinaryExpressionBeed<
                _UEB_ extends AbstractBooleanConditionalBinaryExpressionBeed> {

  protected abstract _UEB_ createSubject();
  protected abstract boolean isDecisive(boolean leftOperand);
  /**
   * @pre  leftValue != null;
   * @pre  rightValue != null;
   */
  protected abstract boolean expectedEffectiveValue(boolean leftValue, boolean rightValue);


  @Before
  public void setUp() throws Exception {
    initGoals();
    $aggregateBeed = new StubAggregateBeed();
    $leftOperandBeed = createEditableLeftOperandBeed($aggregateBeed);
    $leftOperandBeedPath = new ConstantPath<BooleanBeed>($leftOperandBeed);
    $leftOperandBeed2 = createEditableLeftOperandBeed($aggregateBeed);
    $leftOperandBeedPath2 = new ConstantPath<BooleanBeed>($leftOperandBeed2);
    $rightOperandBeed = createEditableRightOperandBeed($aggregateBeed);
    $rightOperandBeedPath = new ConstantPath<BooleanBeed>($rightOperandBeed);
    $rightOperandBeed2 = createEditableRightOperandBeed($aggregateBeed);
    $rightOperandBeedPath2 = new ConstantPath<BooleanBeed>($rightOperandBeed2);
    $subject = createSubject();
    $listener = new StubListener<BooleanEvent>();
  }

  @After
  public void tearDown() throws Exception {
    $leftOperandBeed = null;
    $leftOperandBeedPath = null;
    $leftOperandBeed2 = null;
    $leftOperandBeedPath2 = null;
    $rightOperandBeed = null;
    $rightOperandBeedPath = null;
    $rightOperandBeed2 = null;
    $rightOperandBeedPath2 = null;
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
  private EditableBooleanBeed $leftOperandBeed;
  private Path<BooleanBeed> $leftOperandBeedPath;
  private EditableBooleanBeed $leftOperandBeed2;
  private Path<BooleanBeed> $leftOperandBeedPath2;
  private EditableBooleanBeed $rightOperandBeed;
  private Path<BooleanBeed> $rightOperandBeedPath;
  private EditableBooleanBeed $rightOperandBeed2;
  private Path<BooleanBeed> $rightOperandBeedPath2;
  protected Boolean $leftGoal1;
  protected Boolean $leftGoal2;
  protected Boolean $leftGoalMIN;
  protected Boolean $leftGoalMAX;
  protected Boolean $rightGoal1;
  protected Boolean $rightGoal2;
  protected Boolean $rightGoalMIN;
  protected Boolean $rightGoalMAX;
  StubListener<BooleanEvent> $listener;

  @Test
  public void testSetOperand_1L() {
    $subject.addListener($listener);
    $subject.setLeftOperandPath(new NullPath<BooleanBeed>());
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null, null, null, null, null);
  }

  @Test
  public void testSetOperand_1R() {
    $subject.addListener($listener);
    $subject.setRightOperandPath(new NullPath<BooleanBeed>());
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null, null, null, null, null);
  }

  @Test
  public void testSetOperand_1LR() {
    $subject.addListener($listener);
    $subject.setLeftOperandPath(new NullPath<BooleanBeed>());
    $subject.setRightOperandPath(new NullPath<BooleanBeed>());
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null, null, null, null, null);
  }

  @Test
  public void testSetOperand_2L() {
    $subject.addListener($listener);
    $subject.setLeftOperandPath($leftOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, null);
    validateEvent(null, null, $leftOperandBeed, null,
                  null, null, null, null);
  }

  @Test
  public void testSetOperand_2R() {
    $subject.addListener($listener);
    $subject.setRightOperandPath($rightOperandBeedPath);
    validateSubjectFromOperand(null, $rightOperandBeed);
    validateEvent(null, null, null, null,
                  null, null, $rightOperandBeed, null);
  }

  @Test
  public void testSetOperand_2LR() {
    $subject.addListener($listener);
    $subject.setLeftOperandPath($leftOperandBeedPath);
    $subject.setRightOperandPath($rightOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent(null, null, $leftOperandBeed, null,
                  null, null, $rightOperandBeed, null);
  }

  @Test
  public void testSetOperand_3L() {
    changeLeftOperand($leftOperandBeed, $leftGoal1);
    changeRightOperand($rightOperandBeed, $rightGoal1);
    $subject.addListener($listener);
    $subject.setLeftOperandPath($leftOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, null);
    validateEvent(null, null, $leftOperandBeed, $leftGoal1,
                  null, null, null, null);
    $subject.setRightOperandPath($rightOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed, $leftGoal1,
                  null, null, $rightOperandBeed, $rightGoal1);
    $subject.setLeftOperandPath($leftOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed, $leftGoal1,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    $subject.setLeftOperandPath(new NullPath<BooleanBeed>());
    validateSubjectFromOperand(null, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal1, null, null,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    $subject.setLeftOperandPath($leftOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent(null, null, $leftOperandBeed, $leftGoal1,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    changeLeftOperand($leftOperandBeed2, $leftGoal2);
    $subject.setLeftOperandPath($leftOperandBeedPath2);
    validateSubjectFromOperand($leftOperandBeed2, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed2, $leftGoal2,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    changeLeftOperand($leftOperandBeed, $leftGoal2);
    $subject.setLeftOperandPath($leftOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal2, $leftOperandBeed, $leftGoal2,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
  }

  @Test
  public void testSetOperand_3R() {
    changeLeftOperand($leftOperandBeed, $leftGoal1);
    changeRightOperand($rightOperandBeed, $rightGoal1);
    $subject.addListener($listener);
    $subject.setRightOperandPath($rightOperandBeedPath);
    validateSubjectFromOperand(null, $rightOperandBeed);
    validateEvent(null, null, null, null,
                  null, null, $rightOperandBeed, $rightGoal1);
    $subject.setLeftOperandPath($leftOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent(null, null, $leftOperandBeed, $leftGoal1,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    $subject.setRightOperandPath($rightOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed, $leftGoal1,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    $subject.setRightOperandPath(new NullPath<BooleanBeed>());
    validateSubjectFromOperand($leftOperandBeed, null);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed, $leftGoal1,
                   $rightOperandBeed, $rightGoal1, null, null);
    $subject.setRightOperandPath($rightOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed, $leftGoal1,
                  null, null, $rightOperandBeed, $rightGoal1);
    changeRightOperand($rightOperandBeed2, $rightGoal2);
    $subject.setRightOperandPath($rightOperandBeedPath2);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed2);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed, $leftGoal1,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed2, $rightGoal2);
    changeRightOperand($rightOperandBeed, $rightGoal2);
    $subject.setRightOperandPath($rightOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed, $leftGoal1,
                  $rightOperandBeed2, $rightGoal2, $rightOperandBeed2, $rightGoal2);
  }


  @Test
  public void testDynamics() {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    changeLeftOperand($leftOperandBeed, $leftGoal1);
    $subject.setLeftOperandPath($leftOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, null);
    validateEvent(null, null, $leftOperandBeed, $leftGoal1,
                  null, null, null, null);
    changeRightOperand($rightOperandBeed, $rightGoal1);
    $subject.setRightOperandPath($rightOperandBeedPath);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed, $leftGoal1,
                  null, null, $rightOperandBeed, $rightGoal1);
    // change left operand
    changeLeftOperand($leftOperandBeed, $leftGoal2);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal1, $leftOperandBeed, $leftGoal2,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    changeLeftOperand($leftOperandBeed, $leftGoal2);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal2, $leftOperandBeed, $leftGoal2,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    changeLeftOperand($leftOperandBeed, null);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoal2, $leftOperandBeed, null,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    changeLeftOperand($leftOperandBeed, null);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, null, $leftOperandBeed, null,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    changeLeftOperand($leftOperandBeed, $leftGoalMIN);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, null, $leftOperandBeed, $leftGoalMIN,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    changeLeftOperand($leftOperandBeed, $leftGoalMAX);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoalMIN, $leftOperandBeed, $leftGoalMAX,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal1);
    // change right operand
    changeRightOperand($rightOperandBeed, $rightGoal2);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoalMAX, $leftOperandBeed, $leftGoalMAX,
                  $rightOperandBeed, $rightGoal1, $rightOperandBeed, $rightGoal2);
    changeRightOperand($rightOperandBeed, $rightGoal2);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoalMAX, $leftOperandBeed, $leftGoalMAX,
                  $rightOperandBeed, $rightGoal2, $rightOperandBeed, $rightGoal2);
    changeRightOperand($rightOperandBeed, null);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoalMAX, $leftOperandBeed, $leftGoalMAX,
                  $rightOperandBeed, $rightGoal2, $rightOperandBeed, null);
    changeRightOperand($rightOperandBeed, null);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoalMAX, $leftOperandBeed, $leftGoalMAX,
                  $rightOperandBeed, null, $rightOperandBeed, null);
    changeRightOperand($rightOperandBeed, $rightGoalMIN);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoalMAX, $leftOperandBeed, $leftGoalMAX,
                  $rightOperandBeed, null, $rightOperandBeed, $rightGoalMIN);
    changeRightOperand($rightOperandBeed, $rightGoalMAX);
    validateSubjectFromOperand($leftOperandBeed, $rightOperandBeed);
    validateEvent($leftOperandBeed, $leftGoalMAX, $leftOperandBeed, $leftGoalMAX,
                  $rightOperandBeed, $rightGoalMIN, $rightOperandBeed, $rightGoalMAX);
  }

  protected void validateEvent(
      BooleanBeed oldLeftB, Boolean oldLeftV,
      BooleanBeed newLeftB, Boolean newLeftV,
      BooleanBeed oldRightB, Boolean oldRightV,
      BooleanBeed newRightB, Boolean newRightV) {
    Boolean expectedOldValue = expectedValue(oldLeftB, oldLeftV, oldRightB, oldRightV);
    Boolean expectedNewValue = expectedValue(newLeftB, newLeftV, newRightB, newRightV);
    if (! $subject.equalValue(expectedOldValue, expectedNewValue)) {
      assertNotNull($listener.$event);
      assertTrue(equalValue(expectedValue(oldLeftB, oldLeftV, oldRightB, oldRightV),
                            $listener.$event.getOldValue()));
      assertTrue(equalValue(expectedValue(newLeftB, newLeftV, newRightB, newRightV),
                            $listener.$event.getNewValue()));
    }
    else {
      assertNull($listener.$event);
    }
    $listener.$event = null;
  }

  protected void validateSubjectFromOperand(BooleanBeed leftOperand, BooleanBeed rightOperand) {
    assertEquals(leftOperand, $subject.getLeftOperand());
    assertEquals(rightOperand, $subject.getRightOperand());
    assertTrue(equalValue(expectedValue(leftOperand, rightOperand), $subject.get()));
  }


  protected Boolean expectedValue(BooleanBeed leftOperand, BooleanBeed rightOperand) {
    return expectedValue(leftOperand, leftOperand != null ? leftOperand.get() : null,
                         rightOperand, rightOperand != null ? rightOperand.get() : null);
  }

  protected Boolean expectedValue(
      BooleanBeed leftOperand, Boolean leftValue,
      BooleanBeed rightOperand, Boolean rightValue) {
    if ((leftOperand != null) && leftValue != null) {
      if (isDecisive(leftValue)) {
        return expectedEffectiveValue(leftValue, true);
      }
      else if ((rightOperand != null) && (rightValue != null)) {
        return expectedEffectiveValue(leftValue, rightValue);
      }
      else {
        return null;
      }
    }
    else {
      return null;
    }
  }

  protected void initGoals() {
    $leftGoal1 = Boolean.FALSE;
    $leftGoal2 = Boolean.TRUE;
    $leftGoalMIN = Boolean.FALSE;
    $leftGoalMAX = Boolean.TRUE;
    $rightGoal1 = Boolean.TRUE;
    $rightGoal2 = Boolean.FALSE;
    $rightGoalMIN = Boolean.FALSE;
    $rightGoalMAX = Boolean.TRUE;
  }

  protected EditableBooleanBeed createEditableLeftOperandBeed(AggregateBeed owner) {
    return new EditableBooleanBeed(owner);
  }

  protected EditableBooleanBeed createEditableRightOperandBeed(AggregateBeed owner) {
    return createEditableLeftOperandBeed(owner);
  }

  protected void changeLeftOperand(EditableBooleanBeed editableOperandBeed, Boolean newValue) {
    try {
      BooleanEdit edit = new BooleanEdit(editableOperandBeed);
      edit.setGoal(newValue);
      edit.perform();
    }
    catch (EditStateException exc) {
      Assert.fail();
    }
    catch (IllegalEditException exc) {
      Assert.fail();
    }
  }

  protected void changeRightOperand(EditableBooleanBeed editableOperandBeed, Boolean newValue) {
    changeLeftOperand(editableOperandBeed, newValue);
  }

}
