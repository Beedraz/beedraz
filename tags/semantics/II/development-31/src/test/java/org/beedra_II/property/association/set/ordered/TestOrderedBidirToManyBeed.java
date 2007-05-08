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

package org.beedra_II.property.association.set.ordered;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Listener;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.collection.set.ordered.OrderedSetEvent;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.collection_I.LinkedListOrderedSet;
import org.ppeew.collection_I.OrderedSet;

public class TestOrderedBidirToManyBeed {

  public class MyOrderedBidirToManyBeed<_One_ extends BeanBeed, _Many_ extends BeanBeed>
      extends OrderedBidirToManyBeed<_One_, _Many_> {

    public MyOrderedBidirToManyBeed(_One_ owner) {
      super(owner);
    }

    /**
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(OrderedSetEvent<_Many_> event) {
      updateDependents(event);
    }

  }

  public class OneBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class ManyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class StubOrderedSetEventListener implements Listener<OrderedSetEvent<ManyBeanBeed>> {

    public void beedChanged(OrderedSetEvent<ManyBeanBeed> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public OrderedSetEvent<ManyBeanBeed> $event;

  }

  @Before
  public void setUp() throws Exception {
    $many = new ManyBeanBeed();
    $editableOrderedBidirToOneBeed =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
    $one = new OneBeanBeed();
    $orderedBidirToManyBeed =
      new MyOrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
    $orderedBidirToOneEdit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed);
    $orderedBidirToOneEdit.perform();
//    OrderedSet<ManyBeanBeed> oldS = CollectionUtil.emptyOrderedSet();
    OrderedSet<ManyBeanBeed> newS = new LinkedListOrderedSet<ManyBeanBeed>();
    newS.add($many);
//    $listEvent = new ActualOrderedSetEvent<ManyBeanBeed>($orderedBidirToManyBeed, oldS, newS, $orderedBidirToOneEdit);
    $listener1 = new StubListener<PropagatedEvent>();
    $listener2 = new StubListener<PropagatedEvent>();
    $listener3 = new StubOrderedSetEventListener();
    $listener4 = new StubOrderedSetEventListener();
    $listener5 = new StubListener<ActualLongEvent>();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many;
  private EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableOrderedBidirToOneBeed;
  private OneBeanBeed $one = new OneBeanBeed();
  private MyOrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> $orderedBidirToManyBeed;
  private OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> $orderedBidirToOneEdit;
//  private OrderedSetEvent<ManyBeanBeed> $listEvent;
  private StubListener<PropagatedEvent> $listener1;
  private StubListener<PropagatedEvent> $listener2;
  private StubOrderedSetEventListener $listener3;
  private StubOrderedSetEventListener $listener4;
  private StubListener<ActualLongEvent> $listener5;

  @Test
  public void constructor() {
    assertEquals($orderedBidirToManyBeed.getOwner(), $one);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $one.addListener($listener1);
    $one.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
//    // fire a change on the registered beed
// YOU CAN'T DO THAT OUT OF CONTEXT
//    $orderedBidirToManyBeed.publicUpdateDependents($listEvent);
//    // listeners of the aggregate beed should be notified
//    assertNotNull($listener1.$event);
//    assertNotNull($listener2.$event);
//    assertEquals($listEvent, $listener1.$event.getCause());
//    assertEquals($listEvent, $listener1.$event.getCause());
  }

  @Test
  public void indexOf() throws EditStateException, IllegalEditException {
    // create some bidirectional associations with $orderedBidirToManyBeed
    ManyBeanBeed many1 = new ManyBeanBeed();
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne1 =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many1);
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit1 =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(toOne1);
    edit1.setGoal($orderedBidirToManyBeed);
    edit1.perform();
    ManyBeanBeed many2 = new ManyBeanBeed();
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne2 =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many2);
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit2 =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(toOne2);
    edit2.setGoal($orderedBidirToManyBeed);
    edit2.perform();
    ManyBeanBeed many3 = new ManyBeanBeed();
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne3 =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many3);
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit3 =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(toOne3);
    edit3.setGoal($orderedBidirToManyBeed);
    edit3.perform();
    ManyBeanBeed many4 = new ManyBeanBeed();
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne4 =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many4);
    // check indexOf
    assertEquals($orderedBidirToManyBeed.indexOf(toOne1.getOwner()), 0);
    assertEquals($orderedBidirToManyBeed.indexOf(toOne2.getOwner()), 1);
    assertEquals($orderedBidirToManyBeed.indexOf(toOne3.getOwner()), 2);
    assertEquals($orderedBidirToManyBeed.indexOf(toOne4.getOwner()), -1);
  }
  @Test
  public void add() {
    ManyBeanBeed many1 = new ManyBeanBeed();
    $orderedBidirToManyBeed.add(0, many1);
//    System.out.println($orderedBidirToManyBeed);
    assertTrue($orderedBidirToManyBeed.get().contains(many1));
    assertEquals($orderedBidirToManyBeed.get().get(0), many1);
    ManyBeanBeed many2 = new ManyBeanBeed();
    $orderedBidirToManyBeed.add(1, many2);
//    System.out.println($orderedBidirToManyBeed);
    assertTrue($orderedBidirToManyBeed.get().contains(many1));
    assertTrue($orderedBidirToManyBeed.get().contains(many2));
    assertEquals($orderedBidirToManyBeed.get().get(0), many1);
    assertEquals($orderedBidirToManyBeed.get().get(1), many2);
    ManyBeanBeed many3 = new ManyBeanBeed();
    $orderedBidirToManyBeed.add(0, many3);
//    System.out.println($orderedBidirToManyBeed);
    assertTrue($orderedBidirToManyBeed.get().contains(many1));
    assertTrue($orderedBidirToManyBeed.get().contains(many2));
    assertTrue($orderedBidirToManyBeed.get().contains(many3));
    assertEquals($orderedBidirToManyBeed.get().get(0), many3);
    assertEquals($orderedBidirToManyBeed.get().get(1), many1);
    assertEquals($orderedBidirToManyBeed.get().get(2), many2);
    $orderedBidirToManyBeed.add(2, many3);
//    System.out.println($orderedBidirToManyBeed);
    assertTrue($orderedBidirToManyBeed.get().contains(many1));
    assertTrue($orderedBidirToManyBeed.get().contains(many2));
    assertTrue($orderedBidirToManyBeed.get().contains(many3));
    assertEquals($orderedBidirToManyBeed.get().get(0), many1);
    assertEquals($orderedBidirToManyBeed.get().get(1), many2);
    assertEquals($orderedBidirToManyBeed.get().get(2), many3);
  }

  @Test
  public void remove() {
    ManyBeanBeed many1 = new ManyBeanBeed();
    ManyBeanBeed many2 = new ManyBeanBeed();
    $orderedBidirToManyBeed.add(0, many1);
    assertEquals($orderedBidirToManyBeed.get().get(0), many1);
    assertFalse($orderedBidirToManyBeed.get().contains(many2));
    $orderedBidirToManyBeed.add(1, many2);
    assertEquals($orderedBidirToManyBeed.get().get(0), many1);
    assertEquals($orderedBidirToManyBeed.get().get(1), many2);
    $orderedBidirToManyBeed.remove(many2);
    assertEquals($orderedBidirToManyBeed.get().get(0), many1);
    assertFalse($orderedBidirToManyBeed.get().contains(many2));
    $orderedBidirToManyBeed.remove(many1);
    assertFalse($orderedBidirToManyBeed.get().contains(many1));
    assertFalse($orderedBidirToManyBeed.get().contains(many2));
  }

  @Test
  public void fireChangeEvent() throws EditStateException, IllegalEditException {
    // add listeners to the orderedBidirToManyBeed
    $orderedBidirToManyBeed.addListener($listener3);
    $orderedBidirToManyBeed.addListener($listener4);
    assertNull($listener3.$event);
    assertNull($listener4.$event);
    // fire events
    ManyBeanBeed m1 = new ManyBeanBeed();
    ManyBeanBeed m2 = new ManyBeanBeed();
    ManyBeanBeed m3 = new ManyBeanBeed();
    OrderedSet<ManyBeanBeed> oldValue = new LinkedListOrderedSet<ManyBeanBeed>();
    oldValue.add(m1);
    OrderedSet<ManyBeanBeed> newValue = new LinkedListOrderedSet<ManyBeanBeed>();
    newValue.add(m2);
    newValue.add(m3);
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit1 =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed);
    edit1.perform();
    // MUDO EDIT NEEDS A GOAL TO TEST THIS
//    assertNotNull($listener3.$event);
//    assertEquals($listener3.$event.getSource(), $orderedBidirToManyBeed);
//    assertEquals($listener3.$event.getOldValue().size(), 1);
//    assertTrue($listener3.$event.getOldValue().contains(m1));
//    assertEquals($listener3.$event.getNewValue().size(), 2);
//    assertTrue($listener3.$event.getNewValue().contains(m2));
//    assertTrue($listener3.$event.getNewValue().contains(m3));
//    assertEquals($listener3.$event.getEdit(), edit1);
//    assertNotNull($listener4.$event);
//    assertEquals($listener4.$event.getSource(), $orderedBidirToManyBeed);
//    assertEquals($listener4.$event.getOldValue().size(), 1);
//    assertTrue($listener4.$event.getOldValue().contains(m1));
//    assertEquals($listener4.$event.getNewValue().size(), 2);
//    assertTrue($listener4.$event.getNewValue().contains(m2));
//    assertTrue($listener4.$event.getNewValue().contains(m3));
//    assertEquals($listener4.$event.getEdit(), edit1);
  }

  @Test
  public void createInitialEvent2() {
    OrderedSetEvent<ManyBeanBeed> initialEvent =
      $orderedBidirToManyBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $orderedBidirToManyBeed);
    assertEquals(initialEvent.getOldValue(), null);
    assertEquals(initialEvent.getNewValue(), $orderedBidirToManyBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }

  @Test
  public void getSizeAndCardinality() throws EditStateException, IllegalEditException {
    // add a listener to the size beed
    IntegerBeed<ActualLongEvent> sizeBeed = $orderedBidirToManyBeed.getSize();
    sizeBeed.addListener($listener5);
    assertNull($listener5.$event);
    // check the size (empty)
    assertEquals($orderedBidirToManyBeed.getSize().getLong(), 0L);
    assertEquals($orderedBidirToManyBeed.getCardinality().getLong(), 0L);
    // add elements
    $orderedBidirToOneEdit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed);
    $orderedBidirToOneEdit.setGoal($orderedBidirToManyBeed);
    $orderedBidirToOneEdit.perform();
    // check the size ($editableOrderedBidirToOneBeed)
    assertEquals($orderedBidirToManyBeed.getSize().getLong(), 1L);
    assertEquals($orderedBidirToManyBeed.getCardinality().getLong(), 1L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 0L);
    assertEquals($listener5.$event.getNewLong(), 1L);
    assertEquals($listener5.$event.getEdit(), $orderedBidirToOneEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add elements
    ManyBeanBeed many2 = new ManyBeanBeed();
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> oneBeed =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many2);
    $orderedBidirToOneEdit = new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(oneBeed);
    $orderedBidirToOneEdit.setGoal($orderedBidirToManyBeed);
    $orderedBidirToOneEdit.perform();
    // check the size ($editableOrderedBidirToOneBeed, oneBeed)
    assertEquals($orderedBidirToManyBeed.getSize().getLong(), 2L);
    assertEquals($orderedBidirToManyBeed.getCardinality().getLong(), 2L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 1L);
    assertEquals($listener5.$event.getNewLong(), 2L);
    assertEquals($listener5.$event.getEdit(), $orderedBidirToOneEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    $orderedBidirToOneEdit = new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(oneBeed);
    $orderedBidirToOneEdit.setGoal(null);
    $orderedBidirToOneEdit.perform();
    // check the size ($editableOrderedBidirToOneBeed)
    assertEquals($orderedBidirToManyBeed.getSize().getLong(), 1L);
    assertEquals($orderedBidirToManyBeed.getCardinality().getLong(), 1L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 2L);
    assertEquals($listener5.$event.getNewLong(), 1L);
    assertEquals($listener5.$event.getEdit(), $orderedBidirToOneEdit);
  }

}
