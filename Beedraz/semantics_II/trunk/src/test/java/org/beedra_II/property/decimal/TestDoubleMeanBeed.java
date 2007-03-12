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

package org.beedra_II.property.decimal;


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
import org.beedra_II.property.set.EditableSetBeed;
import org.beedra_II.property.set.SetBeed;
import org.beedra_II.property.set.SetEdit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestDoubleMeanBeed {

  public class MyDoubleMeanBeed extends DoubleMeanBeed {
    public MyDoubleMeanBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(DoubleEvent event) {
      fireChangeEvent(event);
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
  private MyDoubleMeanBeed $doubleMeanBeed = new MyDoubleMeanBeed($owner);
  private DoubleEvent $event1 = new ActualDoubleEvent($doubleMeanBeed, new Double(0), new Double(1), null);
      // @mudo Laatste argument mag niet null zijn??
  private StubListener<PropagatedEvent> $listener1 = new StubListener<PropagatedEvent>();
  private StubListener<PropagatedEvent> $listener2 = new StubListener<PropagatedEvent>();
  private StubListener<DoubleEvent> $listener3 = new StubListener<DoubleEvent>();

  @Test
  public void constructor() {
    assertEquals($doubleMeanBeed.getOwner(), $owner);
    assertEquals($doubleMeanBeed.getSource(), null);
    assertEquals($doubleMeanBeed.getDouble(), null);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $doubleMeanBeed.fire($event1);
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
    // register listeners to the DoubleMeanBeed
    $doubleMeanBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    SetBeed<DoubleBeed<DoubleEvent>> source = null;
    $doubleMeanBeed.setSource(source);
    assertEquals($doubleMeanBeed.getSource(), source);
    assertEquals($doubleMeanBeed.getDouble(), null);
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Source is effective.
   */
  @Test
  public void setSource2() throws EditStateException, IllegalEditException {
    // register listeners to the DoubleMeanBeed
    $doubleMeanBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    EditableSetBeed<DoubleBeed<DoubleEvent>> source = createSource();
    $doubleMeanBeed.setSource(source);
    assertEquals($doubleMeanBeed.getSource(), source);
    assertEquals($doubleMeanBeed.getDouble(), 10.0/4);
    // value has changed, so the listeners of the mean beed are notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doubleMeanBeed);
    assertEquals($listener3.$event.getOldDouble(), null);
    assertEquals($listener3.$event.getNewDouble(), 10.0/4);
    assertEquals($listener3.$event.getEdit(), null);
    // The DoubleMeanBeed is registered as listener of the source, so when
    // the source changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    SetEdit<DoubleBeed<DoubleEvent>> setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(source);
    EditableDoubleBeed goal = createEditableDoubleBeed(5.0);
    setEdit.addElementToAdd(goal);
    setEdit.perform();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doubleMeanBeed);
    assertEquals($listener3.$event.getOldDouble(), 10.0/4);
    assertEquals($listener3.$event.getNewDouble(), 15.0/5);
    assertEquals($listener3.$event.getEdit(), setEdit);
    // The DoubleMeanBeed is registered as listener of all double beeds in the source,
    // so when one of them changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    DoubleEdit doubleEdit = new DoubleEdit(goal);
    doubleEdit.setGoal(6.0);
    doubleEdit.perform();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doubleMeanBeed);
    assertEquals($listener3.$event.getOldDouble(), 15.0/5);
    assertEquals($listener3.$event.getNewDouble(), 16.0/5);
    assertEquals($listener3.$event.getEdit(), doubleEdit);
    // When a new beed is added to the source, the DoubleMeanBeed is added as a listener
    // of that beed. See above.

    // When a beed is removed from the source, the DoubleMeanBeed is removed as listener
    // of that beed.
    $listener3.reset();
    assertNull($listener3.$event);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(source);
    setEdit.addElementToRemove(goal);
    setEdit.perform();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $doubleMeanBeed);
    assertEquals($listener3.$event.getOldDouble(), 16.0/5);
    assertEquals($listener3.$event.getNewDouble(), 10.0/4);
    assertEquals($listener3.$event.getEdit(), setEdit);
    $listener3.reset();
    assertNull($listener3.$event);
    doubleEdit = new DoubleEdit(goal);
    doubleEdit.setGoal(7.0);
    doubleEdit.perform();
    assertNull($listener3.$event); // the DoubleMeanBeed is NOT notified
    assertEquals($doubleMeanBeed.getDouble(), 10.0/4);

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
    // double mean beed has no source
    $doubleMeanBeed.recalculate();
    assertEquals($doubleMeanBeed.getDouble(), null);
    // create source
    EditableSetBeed<DoubleBeed<DoubleEvent>> source =
      new EditableSetBeed<DoubleBeed<DoubleEvent>>($owner);
    // add source to mean beed
    $doubleMeanBeed.setSource(source);
    // recalculate (setBeed contains no elements)
    $doubleMeanBeed.recalculate();
    assertEquals($doubleMeanBeed.getDouble(), null);
    // add beed
    SetEdit<DoubleBeed<DoubleEvent>> setEdit =
      new SetEdit<DoubleBeed<DoubleEvent>>(source);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    // recalculate (setBeed contains beed 1)
    $doubleMeanBeed.recalculate();
    assertEquals($doubleMeanBeed.getDouble(), 1.0);
    // recalculate (setBeed contains beed 1)
    $doubleMeanBeed.recalculate();
    assertEquals($doubleMeanBeed.getDouble(), 1.0);
    // add beed
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(source);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    // recalculate (setBeed contains beed 1 and 2)
    $doubleMeanBeed.recalculate();
    assertEquals($doubleMeanBeed.getDouble(), 3.0/2);
    // recalculate (setBeed contains beed 1 and 2)
    $doubleMeanBeed.recalculate();
    assertEquals($doubleMeanBeed.getDouble(), 3.0/2);
    // add beed
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(source);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2 and 3)
    $doubleMeanBeed.recalculate();
    assertEquals($doubleMeanBeed.getDouble(), 6.0/3);
    // add beed
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(source);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2, 3 and 4)
    $doubleMeanBeed.recalculate();
    assertEquals($doubleMeanBeed.getDouble(), 10.0/4);
    // add beed
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(source);
    setEdit.addElementToAdd(beedNull);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2, 3, 4 and null)
    $doubleMeanBeed.recalculate();
    assertEquals($doubleMeanBeed.getDouble(), null);
  }

  @Test
  public void createInitialEvent() {
    DoubleEvent initialEvent = $doubleMeanBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $doubleMeanBeed);
    assertEquals(initialEvent.getOldDouble(), null);
    assertEquals(initialEvent.getNewDouble(), $doubleMeanBeed.getDouble());
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
    EditableSetBeed<DoubleBeed<DoubleEvent>> setBeed =
      new EditableSetBeed<DoubleBeed<DoubleEvent>>($owner);
    // add set beed to mean beed
    $doubleMeanBeed.setSource(setBeed);
    // add beed
    assertEquals($doubleMeanBeed.getDouble(), null);
    SetEdit<DoubleBeed<DoubleEvent>> setEdit =
      new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 1.0);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 3.0/2);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 6.0/3);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 10.0/4);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed); // add an element that is already there
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 10.0/4);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToRemove(beed1);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 9.0/3);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToRemove(beed2);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 7.0/2);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToRemove(beed3);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 4.0/1);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToRemove(beed4);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), null);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToRemove(beed4); // remove an element that is not there
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), null);
    // change beeds of the source
    setBeed = new EditableSetBeed<DoubleBeed<DoubleEvent>>($owner);
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    $doubleMeanBeed.setSource(setBeed);
    assertEquals($doubleMeanBeed.getDouble(), 3.0/2);
    DoubleEdit doubleEdit = new DoubleEdit(beed1);
    doubleEdit.setGoal(1.5);
    doubleEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 3.5/2);
    doubleEdit = new DoubleEdit(beed2);
    doubleEdit.setGoal(2.5);
    doubleEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 4.0/2);
    doubleEdit = new DoubleEdit(beed1);
    doubleEdit.setGoal(1.0);
    doubleEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 3.5/2);
    doubleEdit = new DoubleEdit(beed2);
    doubleEdit.setGoal(2.0);
    doubleEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 3.0/2);
    // change beeds that are added later to the source
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 6.0/3);
    doubleEdit = new DoubleEdit(beed3);
    doubleEdit.setGoal(3.5);
    doubleEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 6.5/3);
    doubleEdit = new DoubleEdit(beed3);
    doubleEdit.setGoal(3.7);
    doubleEdit.perform();
    assertEquals($doubleMeanBeed.getDouble(), 6.7/3);
  }

  private EditableSetBeed<DoubleBeed<DoubleEvent>> createSource() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<DoubleBeed<DoubleEvent>> setBeed =
      new EditableSetBeed<DoubleBeed<DoubleEvent>>($owner);
    // create beeds
    EditableDoubleBeed beed1 = createEditableDoubleBeed(1.0);
    EditableDoubleBeed beed2 = createEditableDoubleBeed(2.0);
    EditableDoubleBeed beed3 = createEditableDoubleBeed(3.0);
    EditableDoubleBeed beed4 = createEditableDoubleBeed(4.0);
    // add beeds to set
    SetEdit<DoubleBeed<DoubleEvent>> setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed1);
    setEdit.perform();
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed2);
    setEdit.perform();
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed3);
    setEdit.perform();
    setEdit = new SetEdit<DoubleBeed<DoubleEvent>>(setBeed);
    setEdit.addElementToAdd(beed4);
    setEdit.perform();
    return setBeed;
  }
}
