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

import org.beedra_II.StubListener;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.StubAggregateBeed;
import org.beedra_II.bean.RunBeanBeed;
import org.beedra_II.bean.WellBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.path.ConstantPath;
import org.beedra_II.path.NullPath;
import org.beedra_II.path.Path;
import org.beedra_II.path.Paths;
import org.beedra_II.path.ToOneBeanPath;
import org.beedra_II.property.association.set.BidirToOneEdit;
import org.beedra_II.property.association.set.EditableBidirToOneBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.smallfries_I.MathUtil;


public class TestBooleanEqualBeanBeedsBeed {

  @Before
  public void setUp() throws Exception {
    $aggregateBeed = new StubAggregateBeed();
    $leftArgument1 = new RunBeanBeed();
    $leftArgumentPath1 = new ConstantPath<RunBeanBeed>($leftArgument1);
    $rightArgument1 = new RunBeanBeed();
    $rightArgumentPath1 = new ConstantPath<RunBeanBeed>($rightArgument1);
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
    $leftPathToRun1 = new ToOneBeanPath<RunBeanBeed>(Paths.fix($well1.run));
    $leftPathToRun2 = new ToOneBeanPath<RunBeanBeed>(Paths.fix($well2.run));
    $rightPathToRun3 = new ToOneBeanPath<RunBeanBeed>(Paths.fix($well3.run));
    $rightPathToRun4 = new ToOneBeanPath<RunBeanBeed>(Paths.fix($well4.run));
  }

  @After
  public void tearDown() throws Exception {
    $leftArgument1 = null;
    $leftArgumentPath1 = null;
    $rightArgument1 = null;
    $rightArgumentPath1 = null;
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
  private RunBeanBeed $leftArgument1;
  private Path<RunBeanBeed> $leftArgumentPath1;
  private RunBeanBeed $rightArgument1;
  private Path<RunBeanBeed> $rightArgumentPath1;
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
  public void testSetArgument_1L() {
    $subject.addListener($listener);
    $subject.setLeftArgumentPath(new NullPath<RunBeanBeed>());
    validateSubjectFromArgument(null, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetArgument_1R() {
    $subject.addListener($listener);
    $subject.setRightArgumentPath(new NullPath<RunBeanBeed>());
    validateSubjectFromArgument(null, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetArgument_1LR() {
    $subject.addListener($listener);
    $subject.setLeftArgumentPath(new NullPath<RunBeanBeed>());
    $subject.setRightArgumentPath(new NullPath<RunBeanBeed>());
    validateSubjectFromArgument(null, null);
    validateEvent(null, null, null, null);
  }

  @Test
  public void testSetArgument_2L() {
    $subject.addListener($listener);
    $subject.setLeftArgumentPath($leftArgumentPath1);
    validateSubjectFromArgument($leftArgument1, null);
    validateEvent(null, $leftArgument1, null, null);
  }

  @Test
  public void testSetArgument_2R() {
    $subject.addListener($listener);
    $subject.setRightArgumentPath($rightArgumentPath1);
    validateSubjectFromArgument(null, $rightArgument1);
    validateEvent(null, null, null, $rightArgument1);
  }

  @Test
  public void testSetArgument_2LR() {
    $subject.addListener($listener);
    $subject.setLeftArgumentPath($leftArgumentPath1);
    $subject.setRightArgumentPath($rightArgumentPath1);
    validateSubjectFromArgument($leftArgument1, $rightArgument1);
    validateEvent(null, $leftArgument1, null, $rightArgument1);
  }

  private void validateSubjectFromArgument(RunBeanBeed leftArgument, RunBeanBeed rightArgument) {
    assertEquals(leftArgument, $subject.getLeftArg());
    assertEquals(rightArgument, $subject.getRightArg());
    assertNotNull($subject.get());
    assertEquals(leftArgument == rightArgument, $subject.getboolean());
  }

  @Test
  public void testSetArgument_3L() {
    $subject.addListener($listener);
    changeLeftArgumentPath($leftPathToRun1);
    $subject.setLeftArgumentPath($leftArgumentPath1);
    validateSubjectFromArgument($run1, null);
    validateEvent(null, $run1, null, null);
    changeRightArgumentPath($rightPathToRun3);
    $subject.setRightArgumentPath($rightArgumentPath1);
    validateSubjectFromArgument($run1, $run3);
    validateEvent($run1, $run1, null, $run3);
    $subject.setLeftArgumentPath($leftArgumentPath1);
    validateSubjectFromArgument($run1, $run3);
    validateEvent($run1, $run1, $run3, $run3);
    $subject.setLeftArgumentPath(null);
    validateSubjectFromArgument(null, $run3);
    validateEvent($run1, null, $run3, $run3);
    $subject.setLeftArgumentPath($leftArgumentPath1);
    validateSubjectFromArgument($run1, $run3);
    validateEvent(null, $run1, $run3, $run3);
    changeLeftArgumentPath($leftPathToRun2);
    $subject.setLeftArgumentPath($leftArgumentPath1);
    validateSubjectFromArgument($run2, $run3);
    validateEvent($run1, $run2, $run3, $run3);
    changeLeftArgumentPath($leftPathToRun2);
    $subject.setLeftArgumentPath($leftArgumentPath1);
    validateSubjectFromArgument($run2, $run3);
    validateEvent($run2, $run2, $run3, $run3);
  }

  @Test
  public void testSetArgument_3R() {
    $subject.addListener($listener);
    changeRightArgumentPath($rightPathToRun3);
    $subject.setRightArgumentPath($rightArgumentPath1);
    validateSubjectFromArgument(null, $run3);
    validateEvent(null, null, null, $run3);
    changeLeftArgumentPath($leftPathToRun1);
    $subject.setLeftArgumentPath($leftArgumentPath1);
    validateSubjectFromArgument($run1, $run3);
    validateEvent(null, $run1, $run3, $run3);
    $subject.setRightArgumentPath($rightArgumentPath1);
    validateSubjectFromArgument($run1, $run3);
    validateEvent($run1, $run1, $run3, $run3);
    $subject.setRightArgumentPath(null);
    validateSubjectFromArgument($run1, null);
    validateEvent($run1, $run1, $run3, null);
    $subject.setRightArgumentPath($rightArgumentPath1);
    validateSubjectFromArgument($run1, $run3);
    validateEvent($run1, $run1, null, $run3);
    changeRightArgumentPath($rightPathToRun4);
    $subject.setRightArgumentPath($rightArgumentPath1);
    validateSubjectFromArgument($run1, $run4);
    validateEvent($run1, $run1, $run3, $run4);
    changeRightArgumentPath($rightPathToRun4);
    $subject.setRightArgumentPath($rightArgumentPath1);
    validateSubjectFromArgument($run1, $run4);
    validateEvent($run1, $run1, $run4, $run4);
  }


  @Test
  public void testDynamics() throws EditStateException, IllegalEditException {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    changeLeftArgumentPath($leftPathToRun1);
    $subject.setLeftArgumentPath($leftArgumentPath1);
    validateSubjectFromArgument($run1, null);
    validateEvent(null, $run1, null, null);
    changeRightArgumentPath($rightPathToRun3);
    $subject.setRightArgumentPath($rightPathToRun3);
    validateSubjectFromArgument($run1, $run3);
    validateEvent($run1, $run1, null, $run3);
    // now change the value of the left argument path
    changeArgumentPath($leftPathToRun1, $run2);
    validateSubjectFromArgument($run2, $run3);
    validateEvent($run1, $run2, $run3, $run3);
    changeArgumentPath($leftPathToRun1, $run1);
    validateSubjectFromArgument($run1, $run3);
    validateEvent($run2, $run1, $run3, $run3);
    changeArgumentPath($leftPathToRun1, $run1);
    validateSubjectFromArgument($run1, $run3);
    validateEvent($run1, $run1, $run3, $run3);
    changeArgumentPath($leftPathToRun1, null);
    validateSubjectFromArgument(null, $run3);
    validateEvent($run1, null, $run3, $run3);
    changeArgumentPath($leftPathToRun1, null);
    validateSubjectFromArgument(null, $run3);
    validateEvent(null, null, $run3, $run3);
    // now change the value of the right argument path
    changeArgumentPath($rightPathToRun3, $run4);
    validateSubjectFromArgument(null, $run4);
    validateEvent(null, null, $run3, $run4);
    changeArgumentPath($rightPathToRun3, $run3);
    validateSubjectFromArgument(null, $run3);
    validateEvent(null, null, $run4, $run3);
    changeArgumentPath($rightPathToRun3, $run3);
    validateSubjectFromArgument(null, $run3);
    validateEvent(null, null, $run3, $run3);
    changeArgumentPath($rightPathToRun3, null);
    validateSubjectFromArgument(null, null);
    validateEvent(null, null, $run3, null);
    changeArgumentPath($rightPathToRun3, null);
    validateSubjectFromArgument(null, null);
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

  private void changeLeftArgumentPath(Path<RunBeanBeed> path) {
    $leftArgumentPath1 = path;
    $leftArgument1 = path.get();
  }

  private void changeRightArgumentPath(Path<RunBeanBeed> path) {
    $rightArgumentPath1 = path;
    $rightArgument1 = path.get();
  }

  /**
   * The given path is a path from a well to a run.
   * Change the run of that well to the given run
   *
   * @pre  path != null;
   * @pre  path.getToOneBeed() instanceof EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>;
   * @pre  path.getToOneBeed().getOwner() != null;
   */
  private void changeArgumentPath(ToOneBeanPath<RunBeanBeed> path, RunBeanBeed run) throws EditStateException, IllegalEditException {
    @SuppressWarnings("unchecked")
    EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed> bidirToOne =
      (EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>) path.getToOneBeed();
    WellBeanBeed well = bidirToOne.getOwner();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>(well.run);
    edit.setGoal(run != null ? run.wells : null);
    edit.perform();
  }

}
