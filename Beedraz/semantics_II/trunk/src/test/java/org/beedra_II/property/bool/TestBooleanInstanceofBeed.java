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


public class TestBooleanInstanceofBeed {

  public class MyRunBeanBeed extends RunBeanBeed {
    // NOP
  }

  public class YourRunBeanBeed extends RunBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    $aggregateBeed = new StubAggregateBeed();
    $beed = new MyRunBeanBeed();
    $beedPath = new ConstantPath<RunBeanBeed>($beed);
    $subject = new BooleanInstanceofBeed<RunBeanBeed>($aggregateBeed, MyRunBeanBeed.class);
    $listener = new StubListener<BooleanEvent>();
    // initialise runs
    $myRun = new MyRunBeanBeed();
    $yourRun = new YourRunBeanBeed();
    $well1 = new WellBeanBeed();
    $well2 = new WellBeanBeed();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well1.run);
    edit.setGoal($myRun.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well2.run);
    edit.setGoal($yourRun.wells);
    edit.perform();
    // initialise paths
    $pathToMyRun = new ToOneBeanPath<RunBeanBeed>(Paths.fix($well1.run));
    $pathToYourRun = new ToOneBeanPath<RunBeanBeed>(Paths.fix($well2.run));
  }

  @After
  public void tearDown() throws Exception {
    $beed = null;
    $beedPath = null;
    $subject = null;
    $aggregateBeed = null;
    $listener = null;
    $myRun = null;
    $yourRun = null;
    $well1 = null;
    $well2 = null;
    $pathToMyRun = null;
    $pathToYourRun = null;
  }

  protected BooleanInstanceofBeed<RunBeanBeed> $subject;
  protected AggregateBeed $aggregateBeed;
  private RunBeanBeed $beed;
  private Path<RunBeanBeed> $beedPath;
  StubListener<BooleanEvent> $listener;
  private RunBeanBeed $myRun;
  private RunBeanBeed $yourRun;
  private WellBeanBeed $well1;
  private WellBeanBeed $well2;
  private ToOneBeanPath<RunBeanBeed> $pathToMyRun;
  private ToOneBeanPath<RunBeanBeed> $pathToYourRun;

  @Test
  public void testSetArgument_1() {
    $subject.addListener($listener);
    $subject.setBeedPath(new NullPath<RunBeanBeed>());
    validateSubjectFromBeed(null);
    validateEvent(null, null);
  }

  @Test
  public void testSetArgument_2() {
    $subject.addListener($listener);
    $subject.setBeedPath($beedPath);
    validateSubjectFromBeed($beed);
    validateEvent(null, $beed);
  }

  private boolean valueFrom(RunBeanBeed beed) {
    return beed instanceof MyRunBeanBeed;
  }

  private void validateSubjectFromBeed(RunBeanBeed beed) {
    assertEquals(beed, $subject.getBeed());
    assertNotNull($subject.get());
    assertEquals(valueFrom(beed), $subject.getboolean());
  }

  @Test
  public void testSetArgument_3() {
    $subject.addListener($listener);
    changeBeedPath($pathToMyRun);
    $subject.setBeedPath($beedPath);
    validateSubjectFromBeed($myRun);
    validateEvent(null, $myRun);
    $subject.setBeedPath($beedPath);
    validateSubjectFromBeed($myRun);
    validateEvent($myRun, $myRun);
    $subject.setBeedPath(null);
    validateSubjectFromBeed(null);
    validateEvent($myRun, null);
    $subject.setBeedPath($beedPath);
    validateSubjectFromBeed($myRun);
    validateEvent(null, $myRun);
    changeBeedPath($pathToYourRun);
    $subject.setBeedPath($beedPath);
    validateSubjectFromBeed($yourRun);
    validateEvent($myRun, $yourRun);
    changeBeedPath($pathToYourRun);
    $subject.setBeedPath($beedPath);
    validateSubjectFromBeed($yourRun);
    validateEvent($yourRun, $yourRun);
  }

  @Test
  public void testDynamics() throws EditStateException, IllegalEditException {
    $subject.addListener($listener);
    assertTrue($subject.isListener($listener));
    changeBeedPath($pathToMyRun);
    $subject.setBeedPath($beedPath);
    validateSubjectFromBeed($myRun);
    validateEvent(null, $myRun);
    // now change the value of the beed path
    changeBeedPath($pathToMyRun, $yourRun);
    validateSubjectFromBeed($yourRun);
    validateEvent($myRun, $yourRun);
    changeBeedPath($pathToMyRun, $myRun);
    validateSubjectFromBeed($myRun);
    validateEvent($yourRun, $myRun);
    changeBeedPath($pathToMyRun, $myRun);
    validateSubjectFromBeed($myRun);
    validateEvent($myRun, $myRun);
    changeBeedPath($pathToMyRun, null);
    validateSubjectFromBeed(null);
    validateEvent($myRun, null);
    changeBeedPath($pathToMyRun, null);
    validateSubjectFromBeed(null);
    validateEvent(null, null);
  }

  private void validateEvent(RunBeanBeed oldBeed, RunBeanBeed newBeed) {
    Boolean expectedOldValue = valueFrom(oldBeed);
    Boolean expectedNewValue = valueFrom(newBeed);
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

  private void changeBeedPath(Path<RunBeanBeed> path) {
    $beedPath = path;
    $beed = path.get();
  }

  /**
   * The given path is a path from a well to a run.
   * Change the run of that well to the given run
   *
   * @pre  path != null;
   * @pre  path.getToOneBeed() instanceof EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>;
   * @pre  path.getToOneBeed().getOwner() != null;
   */
  private void changeBeedPath(ToOneBeanPath<RunBeanBeed> path, RunBeanBeed run) throws EditStateException, IllegalEditException {
    @SuppressWarnings("unchecked")
    EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed> bidirToOne =
      (EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>) path.getToOneBeed();
    WellBeanBeed well = bidirToOne.getOwner();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>(well.run);
    edit.setGoal(run != null ? run.wells : null);
    edit.perform();
  }

}
