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

package org.beedraz.semantics_II.expression.bool;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.aggregate.StubAggregateBeed;
import org.beedraz.semantics_II.bean.RunBeanBeed;
import org.beedraz.semantics_II.bean.WellBeanBeed;
import org.beedraz.semantics_II.edit.EditStateException;
import org.beedraz.semantics_II.edit.IllegalEditException;
import org.beedraz.semantics_II.expression.association.set.BidirToOneEdit;
import org.beedraz.semantics_II.expression.association.set.EditableBidirToOneBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEqualBeanBeedsBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEvent;
import org.beedraz.semantics_II.path.ConstantPath;
import org.beedraz.semantics_II.path.NullPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.Paths;
import org.beedraz.semantics_II.path.ToOneBeanPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.smallfries_I.MathUtil;


public class TestBooleanEqualBeanBeedsBeed {

  @Before
  public void setUp() throws Exception {
    $aggregateBeed = new StubAggregateBeed();
    $leftOperand1 = new RunBeanBeed();
    $leftOperandPath1 = new ConstantPath<RunBeanBeed>($leftOperand1);
    $rightOperand1 = new RunBeanBeed();
    $rightOperandPath1 = new ConstantPath<RunBeanBeed>($rightOperand1);
    $subject = new BooleanEqualBeanBeedsBeed<RunBeanBeed>();
    $listener = new StubListener<BooleanEvent>();
    // initialise runs
    $run1 = new RunBeanBeed();
    $run2 = new RunBeanBeed();
    $run3 = new RunBeanBeed();
    $run4 = new RunBeanBeed();
    $well1 = new WellBeanBeed();
    $well2 = new WellBeanBeed();
    $well3 = new WellBeanBeed();
    $well4 = new WellBeanBeed();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well1.run);
    edit.setGoal($run1.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well2.run);
    edit.setGoal($run2.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well3.run);
    edit.setGoal($run3.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well4.run);
    edit.setGoal($run4.wells);
    edit.perform();
    // initialise paths
    $leftPathToRun1 = new ToOneBeanPath<RunBeanBeed>(Paths.path($well1.run));
    $leftPathToRun2 = new ToOneBeanPath<RunBeanBeed>(Paths.path($well2.run));
    $rightPathToRun3 = new ToOneBeanPath<RunBeanBeed>(Paths.path($well3.run));
    $rightPathToRun4 = new ToOneBeanPath<RunBeanBeed>(Paths.path($well4.run));
  }

  @After
  public void tearDown() throws Exception {
    $leftOperand1 = null;
    $leftOperandPath1 = null;
    $rightOperand1 = null;
    $rightOperandPath1 = null;
    $subject = null;
    $aggregateBeed = null;
    $listener = null;
    $run1 = null;
    $run2 = null;
    $run3 = null;
    $run4 = null;
    $well1 = null;
    $well2 = null;
    $well3 = null;
    $well4 = null;
    $leftPathToRun1 = null;
    $leftPathToRun2 = null;
    $rightPathToRun3 = null;
    $rightPathToRun4 = null;
  }

  protected BooleanEqualBeanBeedsBeed<RunBeanBeed> $subject;
  protected AggregateBeed $aggregateBeed;
  private RunBeanBeed $leftOperand1;
  private Path<RunBeanBeed> $leftOperandPath1;
  private RunBeanBeed $rightOperand1;
  private Path<RunBeanBeed> $rightOperandPath1;
  StubListener<BooleanEvent> $listener;
  private RunBeanBeed $run1;
  private RunBeanBeed $run2;
  private RunBeanBeed $run3;
  private RunBeanBeed $run4;
  private WellBeanBeed $well1;
  private WellBeanBeed $well2;
  private WellBeanBeed $well3;
  private WellBeanBeed $well4;
  private ToOneBeanPath<RunBeanBeed> $leftPathToRun1;
  private ToOneBeanPath<RunBeanBeed> $leftPathToRun2;
  private ToOneBeanPath<RunBeanBeed> $rightPathToRun3;
  private ToOneBeanPath<RunBeanBeed> $rightPathToRun4;

  @Test
  public void testSetOperand_1L() {
    $subject.addListener($listener);
    $subject.setLeftOperandPath(new NullPath<RunBeanBeed>());
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_1R() {
    $subject.addListener($listener);
    $subject.setRightOperandPath(new NullPath<RunBeanBeed>());
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_1LR() {
    $subject.addListener($listener);
    $subject.setLeftOperandPath(new NullPath<RunBeanBeed>());
    $subject.setRightOperandPath(new NullPath<RunBeanBeed>());
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetOperand_2L() {
    $subject.addListener($listener);
    $subject.setLeftOperandPath($leftOperandPath1);
    validateSubjectFromOperand($leftOperand1, null);
    validateEvent(null, $leftOperand1, null, null);
  }

  @Test
  public void testSetOperand_2R() {
    $subject.addListener($listener);
    $subject.setRightOperandPath($rightOperandPath1);
    validateSubjectFromOperand(null, $rightOperand1);
    validateEvent(null, null, null, $rightOperand1);
  }

  @Test
  public void testSetOperand_2LR() {
    $subject.addListener($listener);
    $subject.setLeftOperandPath($leftOperandPath1);
    $subject.setRightOperandPath($rightOperandPath1);
    validateSubjectFromOperand($leftOperand1, $rightOperand1);
    validateEvent(null, $leftOperand1, null, $rightOperand1);
  }

  private void validateSubjectFromOperand(RunBeanBeed leftOperand, RunBeanBeed rightOperand) {
    assertEquals(leftOperand, $subject.getLeftOperand());
    assertEquals(rightOperand, $subject.getRightOperand());
    assertNotNull($subject.get());
    assertEquals(leftOperand == rightOperand, $subject.getboolean());
  }

  @Test
  public void testSetOperand_3L() {
    $subject.addListener($listener);
    changeLeftOperandPath($leftPathToRun1);
    $subject.setLeftOperandPath($leftOperandPath1);
    validateSubjectFromOperand($run1, null);
    validateEvent(null, $run1, null, null);
    changeRightOperandPath($rightPathToRun3);
    $subject.setRightOperandPath($rightOperandPath1);
    validateSubjectFromOperand($run1, $run3);
    validateEvent($run1, $run1, null, $run3);
    $subject.setLeftOperandPath($leftOperandPath1);
    validateSubjectFromOperand($run1, $run3);
    validateEvent($run1, $run1, $run3, $run3);
    $subject.setLeftOperandPath(null);
    validateSubjectFromOperand(null, $run3);
    validateEvent($run1, null, $run3, $run3);
    $subject.setLeftOperandPath($leftOperandPath1);
    validateSubjectFromOperand($run1, $run3);
    validateEvent(null, $run1, $run3, $run3);
    changeLeftOperandPath($leftPathToRun2);
    $subject.setLeftOperandPath($leftOperandPath1);
    validateSubjectFromOperand($run2, $run3);
    validateEvent($run1, $run2, $run3, $run3);
    changeLeftOperandPath($leftPathToRun2);
    $subject.setLeftOperandPath($leftOperandPath1);
    validateSubjectFromOperand($run2, $run3);
    validateEvent($run2, $run2, $run3, $run3);
  }

  @Test
  public void testSetOperand_3R() {
    $subject.addListener($listener);
    changeRightOperandPath($rightPathToRun3);
    $subject.setRightOperandPath($rightOperandPath1);
    validateSubjectFromOperand(null, $run3);
    validateEvent(null, null, null, $run3);
    changeLeftOperandPath($leftPathToRun1);
    $subject.setLeftOperandPath($leftOperandPath1);
    validateSubjectFromOperand($run1, $run3);
    validateEvent(null, $run1, $run3, $run3);
    $subject.setRightOperandPath($rightOperandPath1);
    validateSubjectFromOperand($run1, $run3);
    validateEvent($run1, $run1, $run3, $run3);
    $subject.setRightOperandPath(null);
    validateSubjectFromOperand($run1, null);
    validateEvent($run1, $run1, $run3, null);
    $subject.setRightOperandPath($rightOperandPath1);
    validateSubjectFromOperand($run1, $run3);
    validateEvent($run1, $run1, null, $run3);
    changeRightOperandPath($rightPathToRun4);
    $subject.setRightOperandPath($rightOperandPath1);
    validateSubjectFromOperand($run1, $run4);
    validateEvent($run1, $run1, $run3, $run4);
    changeRightOperandPath($rightPathToRun4);
    $subject.setRightOperandPath($rightOperandPath1);
    validateSubjectFromOperand($run1, $run4);
    validateEvent($run1, $run1, $run4, $run4);
  }


  @Test
  public void testDynamics() throws EditStateException, IllegalEditException {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    changeLeftOperandPath($leftPathToRun1);
    $subject.setLeftOperandPath($leftOperandPath1);
    validateSubjectFromOperand($run1, null);
    validateEvent(null, $run1, null, null);
    changeRightOperandPath($rightPathToRun3);
    $subject.setRightOperandPath($rightPathToRun3);
    validateSubjectFromOperand($run1, $run3);
    validateEvent($run1, $run1, null, $run3);
    // now change the value of the left operand path
    changeOperandPath($leftPathToRun1, $run2);
    validateSubjectFromOperand($run2, $run3);
    validateEvent($run1, $run2, $run3, $run3);
    changeOperandPath($leftPathToRun1, $run1);
    validateSubjectFromOperand($run1, $run3);
    validateEvent($run2, $run1, $run3, $run3);
    changeOperandPath($leftPathToRun1, $run1);
    validateSubjectFromOperand($run1, $run3);
    validateEvent($run1, $run1, $run3, $run3);
    changeOperandPath($leftPathToRun1, null);
    validateSubjectFromOperand(null, $run3);
    validateEvent($run1, null, $run3, $run3);
    changeOperandPath($leftPathToRun1, null);
    validateSubjectFromOperand(null, $run3);
    validateEvent(null, null, $run3, $run3);
    // now change the value of the right operand path
    changeOperandPath($rightPathToRun3, $run4);
    validateSubjectFromOperand(null, $run4);
    validateEvent(null, null, $run3, $run4);
    changeOperandPath($rightPathToRun3, $run3);
    validateSubjectFromOperand(null, $run3);
    validateEvent(null, null, $run4, $run3);
    changeOperandPath($rightPathToRun3, $run3);
    validateSubjectFromOperand(null, $run3);
    validateEvent(null, null, $run3, $run3);
    changeOperandPath($rightPathToRun3, null);
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, $run3, null);
    changeOperandPath($rightPathToRun3, null);
    validateSubjectFromOperand(null, null);
    validateEvent(null, null, null, null);
  }

  private void validateEvent(RunBeanBeed oldLeftBeed, RunBeanBeed newLeftBeed,
                             RunBeanBeed oldRightBeed, RunBeanBeed newRightBeed) {
    Boolean expectedOldValue = oldLeftBeed == oldRightBeed;
    Boolean expectedNewValue = newLeftBeed == newRightBeed;
    if (! MathUtil.equalValue(expectedOldValue, expectedNewValue)) {
      assertNotNull($listener.$event);
      assertNotNull($listener.$event.getOldValue());
      assertTrue(MathUtil.equalValue(expectedOldValue, $listener.$event.getOldValue()));
      assertNotNull($listener.$event.getNewValue());
      assertTrue(MathUtil.equalValue(expectedNewValue, $listener.$event.getNewValue()));
    }
    else {
      assertNull($listener.$event);
    }
    $listener.$event = null;
  }

  private void changeLeftOperandPath(Path<RunBeanBeed> path) {
    $leftOperandPath1 = path;
    $leftOperand1 = path.get();
  }

  private void changeRightOperandPath(Path<RunBeanBeed> path) {
    $rightOperandPath1 = path;
    $rightOperand1 = path.get();
  }

  /**
   * The given path is a path from a well to a run.
   * Change the run of that well to the given run
   *
   * @pre  path != null;
   * @pre  path.getToOneBeed() instanceof EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>;
   * @pre  path.getToOneBeed().getOwner() != null;
   */
  private void changeOperandPath(ToOneBeanPath<RunBeanBeed> path, RunBeanBeed run) throws EditStateException, IllegalEditException {
    @SuppressWarnings("unchecked")
    EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed> bidirToOne =
      (EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>) path.getToOneBeed();
    WellBeanBeed well = bidirToOne.getOwner();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>(well.run);
    edit.setGoal(run != null ? run.wells : null);
    edit.perform();
  }

}
