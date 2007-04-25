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

package org.beedraz.semantics_II.expression.number.real.double64;


import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.edit.EditStateException;
import org.beedraz.semantics_II.edit.IllegalEditException;
import org.beedraz.semantics_II.expression.collection.set.EditableSetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEdit;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.expression.number.real.RealEvent;
import org.beedraz.semantics_II.path.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.smallfries_I.MathUtil;


public class TestDoubleSetSumBeed {

  public class MyDoubleSetSumBeed extends DoubleSetSumBeed {
    public MyDoubleSetSumBeed() {
      super();
    }

    /**
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(ActualDoubleEvent event) {
      updateDependents(event);
    }

    public final void publicRecalculate() {
      assert getSource() != null;
      recalculate(getSource());
    }

  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner = new StubBeanBeed();
  private MyDoubleSetSumBeed $doubleSetSumBeed = new MyDoubleSetSumBeed();
  private StubListener<RealEvent> $listener3 = new StubListener<RealEvent>();

  @Test
  public void constructor() {
    assertEquals($doubleSetSumBeed.getSourcePath(), null);
    assertEquals($doubleSetSumBeed.getSource(), null);
    assertEquals($doubleSetSumBeed.getDouble(), null);
  }

  /**
   * Source is null.
   */
  @Test
  public void setSource1() throws EditStateException, IllegalEditException {
    // register listeners to the DoubleSetSumBeed
    $doubleSetSumBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    SetBeed<RealBeed<?>, ?> source = null;
    Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath = path(source);
    $doubleSetSumBeed.setSourcePath(sourcePath);
    assertEquals($doubleSetSumBeed.getSourcePath(), sourcePath);
    assertEquals($doubleSetSumBeed.getSource(), source);
    assertEquals($doubleSetSumBeed.getDouble(), null);
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Source is effective.
   */
  @Test
  public void setSource2() throws EditStateException, IllegalEditException {
    // register listeners to the DoubleSetSumBeed
    $doubleSetSumBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    EditableSetBeed<RealBeed<?>> source = createSource();
    Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath = path(source);
    $doubleSetSumBeed.setSourcePath(sourcePath);
    assertEquals($doubleSetSumBeed.getSourcePath(), sourcePath);
    assertEquals($doubleSetSumBeed.getSource(), source);
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0));
    // value has changed, so the listeners of the sum beed are notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doubleSetSumBeed);
    assertEquals($listener3.$event.getOldDouble(), null);
    assertEquals($listener3.$event.getNewDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0));
    assertEquals($listener3.$event.getEdit(), null);
    // The DoubleSetSumBeed is registered as listener of the source, so when
    // the source changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    SetEdit<RealBeed<?>> setEdit = new SetEdit<RealBeed<?>>(source);
    EditableDoubleBeed goal = createEditableDoubleBeed(5.0);
    setEdit.addElementToAdd(goal);
    setEdit.perform();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doubleSetSumBeed);
    assertEquals($listener3.$event.getOldDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0));
    assertEquals($listener3.$event.getNewDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0, 5.0));
    assertEquals($listener3.$event.getEdit(), setEdit);
    // The DoubleSetSumBeed is registered as listener of all double beeds in the source,
    // so when one of them changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    DoubleEdit doubleEdit = new DoubleEdit(goal);
    doubleEdit.setGoal(6.0);
    doubleEdit.perform();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doubleSetSumBeed);
    assertEquals($listener3.$event.getOldDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0, 5.0));
    assertEquals($listener3.$event.getNewDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0, 6.0));
    assertEquals($listener3.$event.getEdit(), doubleEdit);
    // When a new beed is added to the source, the DoubleSetSumBeed is added as a listener
    // of that beed. See above.

    // When a beed is removed from the source, the DoubleSetSumBeed is removed as listener
    // of that beed.
    $listener3.reset();
    assertNull($listener3.$event);
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToRemove(goal);
    setEdit.perform();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doubleSetSumBeed);
    assertEquals($listener3.$event.getOldDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0, 6.0));
    assertEquals($listener3.$event.getNewDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0));
    assertEquals($listener3.$event.getEdit(), setEdit);
    $listener3.reset();
    assertNull($listener3.$event);
    doubleEdit = new DoubleEdit(goal);
    doubleEdit.setGoal(7.0);
    doubleEdit.perform();
    assertNull($listener3.$event); // the DoubleSetSumBeed is NOT notified
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0));

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
    // double sum beed has no source
    // cannot test: violates precondition
//    $doubleSetSumBeed.publicRecalculate();
    assertEquals($doubleSetSumBeed.getDouble(), null);
    // create source
    EditableSetBeed<RealBeed<?>> source =
      new EditableSetBeed<RealBeed<?>>();
    Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath = path(source);
    // add source to sum beed
    $doubleSetSumBeed.setSourcePath(sourcePath);
    // recalculate (setBeed contains no elements)
    $doubleSetSumBeed.publicRecalculate();
    assertEquals($doubleSetSumBeed.getDouble(), 0.0d);
    // add beed
    SetEdit<RealBeed<?>> setEdit =
      new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    // recalculate (setBeed contains beed 1)
    $doubleSetSumBeed.publicRecalculate();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0));
    // recalculate (setBeed contains beed 1)
    $doubleSetSumBeed.publicRecalculate();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    // recalculate (setBeed contains beed 1 and 2)
    $doubleSetSumBeed.publicRecalculate();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0));
    // recalculate (setBeed contains beed 1 and 2)
    $doubleSetSumBeed.publicRecalculate();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2 and 3)
    $doubleSetSumBeed.publicRecalculate();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.0));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2, 3 and 4)
    $doubleSetSumBeed.publicRecalculate();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beedNull);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2, 3, 4 and null)
    $doubleSetSumBeed.publicRecalculate();
    assertEquals($doubleSetSumBeed.getDouble(), null);
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
    EditableSetBeed<RealBeed<?>> setBeed =
      new EditableSetBeed<RealBeed<?>>();
    Path<? extends SetBeed<RealBeed<?>, ?>> setBeedPath = path(setBeed);
    // add set beed to sum beed
    $doubleSetSumBeed.setSourcePath(setBeedPath);
    // add beed
    assertEquals($doubleSetSumBeed.getDouble(), 0.0d);
    SetEdit<RealBeed<?>> setEdit =
      new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.0));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0));
    setEdit = new SetEdit<RealBeed<?>>(setBeed); // add an element that is already there
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.0, 4.0));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed1);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(2.0, 3.0, 4.0));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed2);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(3.0, 4.0));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed3);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(4.0));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed4);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), 0.0d);
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed4); // remove an element that is not there
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), 0.0d);
    // change beeds of the source
    setBeed = new EditableSetBeed<RealBeed<?>>();
    setBeedPath = path(setBeed);
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    $doubleSetSumBeed.setSourcePath(setBeedPath);
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0));
    DoubleEdit doubleEdit = new DoubleEdit(beed1);
    doubleEdit.setGoal(1.5);
    doubleEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.5, 2.0));
    doubleEdit = new DoubleEdit(beed2);
    doubleEdit.setGoal(2.5);
    doubleEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.5, 2.5));
    doubleEdit = new DoubleEdit(beed1);
    doubleEdit.setGoal(1.0);
    doubleEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.5));
    doubleEdit = new DoubleEdit(beed2);
    doubleEdit.setGoal(2.0);
    doubleEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0));
    // change beeds that are added later to the source
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.0));
    doubleEdit = new DoubleEdit(beed3);
    doubleEdit.setGoal(3.5);
    doubleEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.5));
    doubleEdit = new DoubleEdit(beed3);
    doubleEdit.setGoal(3.7);
    doubleEdit.perform();
    assertEquals($doubleSetSumBeed.getDouble(), MathUtil.sum(1.0, 2.0, 3.7));
  }

  private EditableSetBeed<RealBeed<?>> createSource() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<RealBeed<?>> setBeed =
      new EditableSetBeed<RealBeed<?>>();
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
