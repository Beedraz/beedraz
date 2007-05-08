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

package org.beedra_II.property.number.real.double64;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.collection.set.EditableSetBeed;
import org.beedra_II.property.collection.set.SetBeed;
import org.beedra_II.property.collection.set.SetEdit;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.RealEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.smallfries_I.MathUtil;


public class TestDoublePopulationStandardErrorBeed {

//  private static final double DOUBLE_ERROR = 0.0001;

  public class MyDoublePopulationStandardErrorBeed extends DoublePopulationStandardErrorBeed {
    public MyDoublePopulationStandardErrorBeed(AggregateBeed owner) {
      super(owner);
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
  private MyDoublePopulationStandardErrorBeed $doublePopulationStandardErrorBeed = new MyDoublePopulationStandardErrorBeed($owner);
  private ActualDoubleEvent $event1 = new ActualDoubleEvent($doublePopulationStandardErrorBeed, new Double(0), new Double(1), null);
      // @mudo Laatste argument mag niet null zijn??
  private StubListener<PropagatedEvent> $listener1 = new StubListener<PropagatedEvent>();
  private StubListener<PropagatedEvent> $listener2 = new StubListener<PropagatedEvent>();
  private StubListener<RealEvent> $listener3 = new StubListener<RealEvent>();

  @Test
  public void constructor() {
    assertEquals($doublePopulationStandardErrorBeed.getOwner(), $owner);
    assertEquals($doublePopulationStandardErrorBeed.getSource(), null);
    assertEquals($doublePopulationStandardErrorBeed.getDouble(), null);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $doublePopulationStandardErrorBeed.publicUpdateDependents($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
    // no terms registered (cannot be tested)
  }

  /**
   * Source is null.
   */
  @Test
  public void setSource1() throws EditStateException, IllegalEditException {
    // register listeners to the DoublePopulationStandardErrorBeed
    $doublePopulationStandardErrorBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    SetBeed<RealBeed<?>, ?> source = null;
    $doublePopulationStandardErrorBeed.setSource(source);
    assertEquals($doublePopulationStandardErrorBeed.getSource(), source);
    assertEquals($doublePopulationStandardErrorBeed.getDouble(), null);
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Source is effective.
   */
  @Test
  public void setSource2() throws EditStateException, IllegalEditException {
    // register listeners to the DoublePopulationStandardErrorBeed
    $doublePopulationStandardErrorBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    EditableSetBeed<RealBeed<?>> source = createSource();
    $doublePopulationStandardErrorBeed.setSource(source);
    assertEquals($doublePopulationStandardErrorBeed.getSource(), source);
    Double standardError1 = MathUtil.populationStandardError(1.0, 2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError1, Math.ulp($doublePopulationStandardErrorBeed.getDouble()) * 4));
    // value has changed, so the listeners of the standard error beed are notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doublePopulationStandardErrorBeed);
    assertEquals($listener3.$event.getOldDouble(), null);
    assertTrue(MathUtil.equalValue($listener3.$event.getNewDouble(), standardError1));
    assertEquals($listener3.$event.getEdit(), null);
    // The DoublePopulationStandardErrorBeed is registered as listener of the source, so when
    // the source changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    SetEdit<RealBeed<?>> setEdit = new SetEdit<RealBeed<?>>(source);
    EditableDoubleBeed goal = createEditableDoubleBeed(5.0);
    setEdit.addElementToAdd(goal);
    setEdit.perform();
    Double standardError2 = MathUtil.populationStandardError(1.0, 2.0, 3.0, 4.0, 5.0);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doublePopulationStandardErrorBeed);
    assertTrue(MathUtil.equalValue($listener3.$event.getOldDouble(), standardError1));
    assertTrue(MathUtil.equalValue($listener3.$event.getNewDouble(), standardError2));
    assertEquals($listener3.$event.getEdit(), setEdit);
    // The DoublePopulationStandardErrorBeed is registered as listener of all double beeds in the source,
    // so when one of them changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    DoubleEdit doubleEdit = new DoubleEdit(goal);
    doubleEdit.setGoal(6.0);
    doubleEdit.perform();
    Double standardError3 = MathUtil.populationStandardError(1.0, 2.0, 3.0, 4.0, 6.0);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doublePopulationStandardErrorBeed);
    assertTrue(MathUtil.equalValue($listener3.$event.getOldDouble(), standardError2));
    assertTrue(MathUtil.equalValue($listener3.$event.getNewDouble(), standardError3));
    assertEquals($listener3.$event.getEdit(), doubleEdit);
    // When a new beed is added to the source, the DoublePopulationStandardErrorBeed is added as a listener
    // of that beed. See above.

    // When a beed is removed from the source, the DoublePopulationStandardErrorBeed is removed as listener
    // of that beed.
    $listener3.reset();
    assertNull($listener3.$event);
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToRemove(goal);
    setEdit.perform();
    Double standardError4 = MathUtil.populationStandardError(1.0, 2.0, 3.0, 4.0);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doublePopulationStandardErrorBeed);
    assertTrue(MathUtil.equalValue($listener3.$event.getOldDouble(), standardError3));
    assertTrue(MathUtil.equalValue($listener3.$event.getNewDouble(), standardError4));
    assertEquals($listener3.$event.getEdit(), setEdit);
    $listener3.reset();
    assertNull($listener3.$event);
    doubleEdit = new DoubleEdit(goal);
    doubleEdit.setGoal(7.0);
    doubleEdit.perform();
    assertNull($listener3.$event); // the DoublePopulationStandardErrorBeed is NOT notified
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError4));

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
    // double standard error beed has no source
    assertEquals($doublePopulationStandardErrorBeed.getDouble(), null);
    // create source
    EditableSetBeed<RealBeed<?>> source =
      new EditableSetBeed<RealBeed<?>>($owner);
    // add source to standard error beed
    $doublePopulationStandardErrorBeed.setSource(source);
    // recalculate (setBeed contains no elements)
    $doublePopulationStandardErrorBeed.publicRecalculate();
    assertEquals($doublePopulationStandardErrorBeed.getDouble(), Double.NaN);
    // add beed
    SetEdit<RealBeed<?>> setEdit =
      new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    // recalculate (setBeed contains beed 1)
    $doublePopulationStandardErrorBeed.publicRecalculate();
    assertEquals(0.0, $doublePopulationStandardErrorBeed.getDouble());
    // recalculate (setBeed contains beed 1)
    $doublePopulationStandardErrorBeed.publicRecalculate();
    assertEquals(0.0, $doublePopulationStandardErrorBeed.getDouble());
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    // recalculate (setBeed contains beed 1 and 2)
    $doublePopulationStandardErrorBeed.publicRecalculate();
    double standardError = MathUtil.populationStandardError(1.0, 2.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError, Math.ulp($doublePopulationStandardErrorBeed.getDouble()) * 4));
    // recalculate (setBeed contains beed 1 and 2)
    $doublePopulationStandardErrorBeed.publicRecalculate();
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2 and 3)
    $doublePopulationStandardErrorBeed.publicRecalculate();
    standardError = MathUtil.populationStandardError(1.0, 2.0, 3.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2, 3 and 4)
    $doublePopulationStandardErrorBeed.publicRecalculate();
    standardError = MathUtil.populationStandardError(1.0, 2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    // add beed
    setEdit = new SetEdit<RealBeed<?>>(source);
    setEdit.addElementToAdd(beedNull);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2, 3, 4 and null)
    $doublePopulationStandardErrorBeed.publicRecalculate();
    assertEquals(null, $doublePopulationStandardErrorBeed.getDouble());
  }

  @Test
  public void createInitialEvent() {
    RealEvent initialEvent = $doublePopulationStandardErrorBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $doublePopulationStandardErrorBeed);
    assertEquals(initialEvent.getOldDouble(), null);
    assertEquals(initialEvent.getNewDouble(), $doublePopulationStandardErrorBeed.getDouble());
    assertEquals(initialEvent.getEdit(), null);
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
      new EditableSetBeed<RealBeed<?>>($owner);
    // add set beed to standard error beed
    $doublePopulationStandardErrorBeed.setSource(setBeed);
    // add beed
    assertEquals($doublePopulationStandardErrorBeed.getDouble(), Double.NaN);
    SetEdit<RealBeed<?>> setEdit =
      new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    assertEquals(0.0, $doublePopulationStandardErrorBeed.getDouble());
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    double standardError = MathUtil.populationStandardError(1.0, 2.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError, Math.ulp($doublePopulationStandardErrorBeed.getDouble()) * 4));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    standardError = MathUtil.populationStandardError(1.0, 2.0, 3.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    standardError = MathUtil.populationStandardError(1.0, 2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    setEdit = new SetEdit<RealBeed<?>>(setBeed); // add an element that is already there
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    standardError = MathUtil.populationStandardError(1.0, 2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed1);
    setEdit.perform();
    standardError = MathUtil.populationStandardError(2.0, 3.0, 4.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed2);
    setEdit.perform();
    standardError = MathUtil.populationStandardError(3.0, 4.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed3);
    setEdit.perform();
    assertEquals(0.0, $doublePopulationStandardErrorBeed.getDouble());
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed4);
    setEdit.perform();
    assertEquals($doublePopulationStandardErrorBeed.getDouble(), Double.NaN);
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToRemove(beed4); // remove an element that is not there
    setEdit.perform();
    assertEquals($doublePopulationStandardErrorBeed.getDouble(), Double.NaN);
    // change beeds of the source
    setBeed = new EditableSetBeed<RealBeed<?>>($owner);
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    $doublePopulationStandardErrorBeed.setSource(setBeed);
    standardError = MathUtil.populationStandardError(1.0, 2.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    DoubleEdit doubleEdit = new DoubleEdit(beed1);
    doubleEdit.setGoal(1.5);
    doubleEdit.perform();
    standardError = MathUtil.populationStandardError(1.5, 2.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    doubleEdit = new DoubleEdit(beed2);
    doubleEdit.setGoal(2.5);
    doubleEdit.perform();
    standardError = MathUtil.populationStandardError(1.5, 2.5);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    doubleEdit = new DoubleEdit(beed1);
    doubleEdit.setGoal(1.0);
    doubleEdit.perform();
    standardError = MathUtil.populationStandardError(1.0, 2.5);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    doubleEdit = new DoubleEdit(beed2);
    doubleEdit.setGoal(2.0);
    doubleEdit.perform();
    standardError = MathUtil.populationStandardError(1.0, 2.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    // change beeds that are added later to the source
    setEdit = new SetEdit<RealBeed<?>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    standardError = MathUtil.populationStandardError(1.0, 2.0, 3.0);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    doubleEdit = new DoubleEdit(beed3);
    doubleEdit.setGoal(3.5);
    doubleEdit.perform();
    standardError = MathUtil.populationStandardError(1.0, 2.0, 3.5);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
    doubleEdit = new DoubleEdit(beed3);
    doubleEdit.setGoal(3.7);
    doubleEdit.perform();
    standardError = MathUtil.populationStandardError(1.0, 2.0, 3.7);
    assertTrue(MathUtil.equalValue($doublePopulationStandardErrorBeed.getDouble(), standardError));
  }

  private EditableSetBeed<RealBeed<?>> createSource() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<RealBeed<?>> setBeed =
      new EditableSetBeed<RealBeed<?>>($owner);
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
