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
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.aggregate.StubAggregateBeed;
import org.beedraz.semantics_II.bean.RunBeanBeed;
import org.beedraz.semantics_II.bean.WellBeanBeed;
import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.expression.association.set.BidirToOneEdit;
import org.beedraz.semantics_II.expression.association.set.EditableBidirToOneBeed;
import org.beedraz.semantics_II.expression.bool.AbstractBeanArgBooleanUnaryExpressionBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEvent;
import org.beedraz.semantics_II.path.ConstantPath;
import org.beedraz.semantics_II.path.NullPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.Paths;
import org.beedraz.semantics_II.path.ToOneBeanPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.MathUtil;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractTestBeanArgBooleanUnaryExpressionBeed<
                             _BBU_ extends AbstractBeanArgBooleanUnaryExpressionBeed<?>> {

  protected abstract RunBeanBeed initBeed();
  protected abstract _BBU_ initSubject();
  protected abstract RunBeanBeed initRun1();
  protected abstract RunBeanBeed initRun2();

  @Before
  public void setUp() throws Exception {
    $aggregateBeed = new StubAggregateBeed();
    $beed = initBeed();
    $beedPath = new ConstantPath<RunBeanBeed>($beed);
    $subject = initSubject();
    $listener = new StubListener<BooleanEvent>();
    // initialise runs
    $myRun = initRun1();
    $yourRun = initRun2();
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
    $pathToMyRun = new ToOneBeanPath<RunBeanBeed>(Paths.path($well1.run));
    $pathToYourRun = new ToOneBeanPath<RunBeanBeed>(Paths.path($well2.run));
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

  protected _BBU_ $subject;
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
  public void testSetOperand_1() {
    $subject.addListener($listener);
    $subject.setBeedPath(new NullPath<RunBeanBeed>());
    validateSubjectFromBeed(null);
    validateEvent(null, null);
  }

  @Test
  public void testSetOperand_2() {
    $subject.addListener($listener);
    $subject.setBeedPath($beedPath);
    validateSubjectFromBeed($beed);
    validateEvent(null, $beed);
  }

  protected abstract boolean valueFrom(RunBeanBeed beed);

  private void validateSubjectFromBeed(RunBeanBeed beed) {
    assertEquals(beed, $subject.getBeed());
    assertNotNull($subject.get());
    assertEquals(valueFrom(beed), $subject.getboolean());
  }

  @Test
  public void testSetOperand_3() {
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
