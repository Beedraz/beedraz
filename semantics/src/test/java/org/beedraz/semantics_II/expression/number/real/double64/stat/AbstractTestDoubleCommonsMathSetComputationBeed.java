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

package org.beedraz.semantics_II.expression.number.real.double64.stat;


import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.expression.collection.set.EditableSetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEdit;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.expression.number.real.RealEvent;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleEdit;
import org.beedraz.semantics_II.expression.number.real.double64.EditableDoubleBeed;
import org.beedraz.semantics_II.path.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.MathUtil;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractTestDoubleCommonsMathSetComputationBeed<_CMSCB_ extends AbstractDoubleCommonsMathSetComputationBeed> {

  protected abstract _CMSCB_ createSubject();
  protected abstract double computeStatistic(double... values);
  protected abstract void updateDependents(_CMSCB_ subject, Event event);
  protected abstract void recalculate(_CMSCB_ subject);

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner = new StubBeanBeed();
  private _CMSCB_ $subject = createSubject();
  private StubListener<RealEvent> $listener3 = new StubListener<RealEvent>();

  @Test
  public void constructor() {
    assertEquals($subject.getSource(), null);
    assertEquals($subject.getDouble(), null);
  }

  /**
   * Source is null.
   */
  @Test
  public void setSource1() throws EditStateException, IllegalEditException {
    // register listeners to the beed
    $subject.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    SetBeed<RealBeed<?>, ?> source = null;
    Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath = path(source);
    $subject.setSourcePath(sourcePath);
    assertEquals($subject.getSourcePath(), sourcePath);
    assertEquals($subject.getSource(), source);
    assertEquals($subject.getDouble(), null);
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Source is effective.
   */
  @Test
  public void setSource2() throws EditStateException, IllegalEditException {
    // register listeners to the beed
    $subject.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    EditableSetBeed<RealBeed<?>> source = createSource();
    Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath = path(source);
    $subject.setSourcePath(sourcePath);
    assertEquals($subject.getSourcePath(), sourcePath);
    assertEquals($subject.getSource(), source);
    Double statistic1 = computeStatistic(1.0, 2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic1, Math.ulp($subject.getDouble()) * 2));
    // value has changed, so the listeners of the beed are notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $subject);
    assertEquals($listener3.$event.getOldDouble(), null);
    assertTrue(MathUtil.equalValue($listener3.$event.getNewDouble(), statistic1));
    assertEquals($listener3.$event.getEdit(), null);
    // The beed is registered as listener of the source, so when
    // the source changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    SetEdit<RealBeed<?>> setEdit = new SetEdit<RealBeed<?>>(source);
    EditableDoubleBeed goal = createEditableDoubleBeed(5.0);
    setEdit.addElementToAdd(goal);
    setEdit.perform();
    Double statistic2 = computeStatistic(1.0, 2.0, 3.0, 4.0, 5.0);
    if (!statistic2.equals(statistic1)) {
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getSource(), $subject);
      assertTrue(MathUtil.equalValue($listener3.$event.getOldDouble(), statistic1));
      assertTrue(MathUtil.equalValue($listener3.$event.getNewDouble(), statistic2));
      assertEquals($listener3.$event.getEdit(), setEdit);
    }
    // The beed is registered as listener of all double beeds in the source,
    // so when one of them changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    DoubleEdit doubleEdit = new DoubleEdit(goal);
    doubleEdit.setGoal(-6.0);
    doubleEdit.perform();
    Double statistic3 = computeStatistic(1.0, 2.0, 3.0, 4.0, -6.0);
    if (!statistic3.equals(statistic2)) {
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getSource(), $subject);
      assertTrue(MathUtil.equalValue($listener3.$event.getOldDouble(), statistic2));
      assertTrue(MathUtil.equalValue($listener3.$event.getNewDouble(), statistic3));
      assertEquals($listener3.$event.getEdit(), doubleEdit);
    }
    // When a new beed is added to the source, the beed is added as a listener
    // of that beed. See above.

    // When a beed is removed from the source, the beed is removed as listener
    // of that beed.
    $listener3.reset();
    assertNull($listener3.$event);
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToRemove(goal);
    setEdit.perform();
    Double statistic4 = computeStatistic(1.0, 2.0, 3.0, 4.0);
    if (!statistic4.equals(statistic3)) {
      assertNotNull($listener3.$event);
      assertEquals($listener3.$event.getSource(), $subject);
      assertTrue(MathUtil.equalValue($listener3.$event.getOldDouble(), statistic3));
      assertTrue(MathUtil.equalValue($listener3.$event.getNewDouble(), statistic4));
      assertEquals($listener3.$event.getEdit(), setEdit);
    }
    $listener3.reset();
    assertNull($listener3.$event);
    doubleEdit = new DoubleEdit(goal);
    doubleEdit.setGoal(7.0);
    doubleEdit.perform();
    assertNull($listener3.$event); // the beed is NOT notified
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic4));

  }


  private EditableDoubleBeed createEditableDoubleBeed(Double value) {
    try {
      EditableDoubleBeed editableDoubleBeed = new EditableDoubleBeed($owner);
      DoubleEdit edit = new DoubleEdit(editableDoubleBeed);
      edit.setGoal(value);
      edit.perform();
      assertEquals(editableDoubleBeed.get(), value);
      return editableDoubleBeed;
    }
    catch (EditStateException e) {
      assertTrue(false);
      return null;
    }
    catch (IllegalEditException e) {
      assertTrue(false);
      return null;
    }
  }


  @Test
  public void recalculate() throws EditStateException, IllegalEditException {
    // create beeds
    EditableDoubleBeed beed1 = createEditableDoubleBeed(1.0);
    EditableDoubleBeed beed2 = createEditableDoubleBeed(2.0);
    EditableDoubleBeed beed3 = createEditableDoubleBeed(3.0);
    EditableDoubleBeed beed4 = createEditableDoubleBeed(4.0);
    EditableDoubleBeed beedNull = createEditableDoubleBeed(null);
    // double beed has no source
    assertEquals($subject.getDouble(), null);
    // create source
    EditableSetBeed<RealBeed<?>> source = new EditableSetBeed<RealBeed<?>>($owner);
    Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath = path(source);
    // add source to beed
    $subject.setSourcePath(sourcePath);
    // recalculate (setBeed contains no elements)
    recalculate($subject);
    double statistic = computeStatistic();
    assertEquals(statistic, $subject.getDouble());
    // add beed
    SetEdit<RealBeed<?>> setEdit =
      new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    // recalculate (setBeed contains beed 1)
    recalculate($subject);
    statistic = computeStatistic(1.0);
    assertEquals(statistic, $subject.getDouble());
    // recalculate (setBeed contains beed 1)
    recalculate($subject);
    assertEquals(statistic, $subject.getDouble());
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    // recalculate (setBeed contains beed 1 and 2)
    recalculate($subject);
    statistic = computeStatistic(1.0, 2.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic, Math.ulp($subject.getDouble()) * 4));
    // recalculate (setBeed contains beed 1 and 2)
    recalculate($subject);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2 and 3)
    recalculate($subject);
    statistic = computeStatistic(1.0, 2.0, 3.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2, 3 and 4)
    recalculate($subject);
    statistic = computeStatistic(1.0, 2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beedNull);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2, 3, 4 and null)
    recalculate($subject);
    assertEquals(null, $subject.getDouble());
  }

  /**
   * Source gooit events bij add en remove.
   * @throws EditStateException
   * @throws IllegalEditException
   */
  @Test
  public void test() throws EditStateException, IllegalEditException {
    // create beeds
    EditableDoubleBeed beed1 = createEditableDoubleBeed(1.0);
    EditableDoubleBeed beed2 = createEditableDoubleBeed(2.0);
    EditableDoubleBeed beed3 = createEditableDoubleBeed(3.0);
    EditableDoubleBeed beed4 = createEditableDoubleBeed(4.0);
    // create set beed
    EditableSetBeed<RealBeed<?>> setBeed = new EditableSetBeed<RealBeed<?>>($owner);
    Path<? extends SetBeed<RealBeed<?>, ?>> setBeedPath = path(setBeed);
    // add set beed to beed
    $subject.setSourcePath(setBeedPath);
    // add beed
    assertEquals($subject.getDouble(), Double.NaN);
    SetEdit<RealBeed<?>> setEdit =
      new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    double statistic = computeStatistic(1.0);
    assertEquals(statistic, $subject.getDouble());
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    statistic = computeStatistic(1.0, 2.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic, Math.ulp($subject.getDouble()) * 4));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    statistic = computeStatistic(1.0, 2.0, 3.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    statistic = computeStatistic(1.0, 2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    setEdit = new SetEdit<RealBeed<?>>(setBeed); // add an element that is already there
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    statistic = computeStatistic(1.0, 2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed1);
    setEdit.perform();
    statistic = computeStatistic(2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed2);
    setEdit.perform();
    statistic = computeStatistic(3.0, 4.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed3);
    setEdit.perform();
    statistic = computeStatistic(4.0);
    assertEquals(statistic, $subject.getDouble());
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed4);
    setEdit.perform();
    assertEquals($subject.getDouble(), Double.NaN);
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed4); // remove an element that is not there
    setEdit.perform();
    assertEquals($subject.getDouble(), Double.NaN);
    // change beeds of the source
    setBeed = new EditableSetBeed<RealBeed<?>>($owner);
    setBeedPath = path(setBeed);
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    $subject.setSourcePath(setBeedPath);
    statistic = computeStatistic(1.0, 2.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    DoubleEdit doubleEdit = new DoubleEdit(beed1);
    doubleEdit.setGoal(1.5);
    doubleEdit.perform();
    statistic = computeStatistic(1.5, 2.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    doubleEdit = new DoubleEdit(beed2);
    doubleEdit.setGoal(2.5);
    doubleEdit.perform();
    statistic = computeStatistic(1.5, 2.5);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    doubleEdit = new DoubleEdit(beed1);
    doubleEdit.setGoal(1.0);
    doubleEdit.perform();
    statistic = computeStatistic(1.0, 2.5);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    doubleEdit = new DoubleEdit(beed2);
    doubleEdit.setGoal(2.0);
    doubleEdit.perform();
    statistic = computeStatistic(1.0, 2.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    // change beeds that are added later to the source
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    statistic = computeStatistic(1.0, 2.0, 3.0);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    doubleEdit = new DoubleEdit(beed3);
    doubleEdit.setGoal(3.5);
    doubleEdit.perform();
    statistic = computeStatistic(1.0, 2.0, 3.5);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
    doubleEdit = new DoubleEdit(beed3);
    doubleEdit.setGoal(3.7);
    doubleEdit.perform();
    statistic = computeStatistic(1.0, 2.0, 3.7);
    assertTrue(MathUtil.equalValue($subject.getDouble(), statistic));
  }

  private EditableSetBeed<RealBeed<?>> createSource() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<RealBeed<?>> setBeed = new EditableSetBeed<RealBeed<?>>($owner);
    // create beeds
    EditableDoubleBeed beed1 = createEditableDoubleBeed(1.0);
    EditableDoubleBeed beed2 = createEditableDoubleBeed(2.0);
    EditableDoubleBeed beed3 = createEditableDoubleBeed(3.0);
    EditableDoubleBeed beed4 = createEditableDoubleBeed(4.0);
    // add beeds to set
    SetEdit<RealBeed<?>> setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    return setBeed;
  }

}
