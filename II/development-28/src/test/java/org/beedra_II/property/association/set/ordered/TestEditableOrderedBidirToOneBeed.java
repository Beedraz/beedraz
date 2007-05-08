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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Listener;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.collection.set.ordered.OrderedSetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableOrderedBidirToOneBeed {

  public class MyEditableOrderedBidirToOneBeed<_One_ extends BeanBeed, _Many_ extends BeanBeed>
      extends EditableOrderedBidirToOneBeed<_One_, _Many_> {

    public MyEditableOrderedBidirToOneBeed(_Many_ owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(OrderedBidirToOneEvent<_One_, _Many_> event) {
      fireChangeEvent(event);
    }
  }

  public class OneBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class ManyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class StubEditableOrderedBidirToOneBeedListener implements Listener<OrderedSetEvent<Integer>> {

    public void beedChanged(OrderedSetEvent<Integer> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public OrderedSetEvent<Integer> $event;

  }

  @Before
  public void setUp() throws Exception {
    $many = new ManyBeanBeed();
    $editableOrderedBidirToOneBeed =
      new MyEditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
    $one = new OneBeanBeed();
    $OrderedBidirToManyBeed =
      new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
    $OrderedBidirToOneEdit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed);
    $OrderedBidirToOneEdit.perform();
    $OrderedBidirToOneEvent =
      new OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed, null, $OrderedBidirToManyBeed, $OrderedBidirToOneEdit);
    $listener1 = new StubListener<PropagatedEvent>();
    $listener2 = new StubListener<PropagatedEvent>();
//  $listener3 = new StubEditableOrderedBidirToOneBeedListener();
//  $listener4 = new StubEditableOrderedBidirToOneBeedListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many;
  private MyEditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableOrderedBidirToOneBeed;
  private OneBeanBeed $one;
  private OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> $OrderedBidirToManyBeed;
  private OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> $OrderedBidirToOneEdit;
  private OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> $OrderedBidirToOneEvent;
  private StubListener<PropagatedEvent> $listener1;
  private StubListener<PropagatedEvent> $listener2;
//  private StubEditableOrderedBidirToOneBeedListener $listener3;
//  private StubEditableOrderedBidirToOneBeedListener $listener4;

  @Test
  public void constructor() {
    assertEquals($editableOrderedBidirToOneBeed.getOwner(), $many);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $many.addListener($listener1);
    $many.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableOrderedBidirToOneBeed.fire($OrderedBidirToOneEvent);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($OrderedBidirToOneEvent, $listener1.$event.getCause());
    assertEquals($OrderedBidirToOneEvent, $listener1.$event.getCause());
  }

  @Test
  public void getOne() throws EditStateException, IllegalEditException {
    // set the to many side to null
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed);
    edit.setGoal(null);
    edit.perform();
    assertNull($editableOrderedBidirToOneBeed.get());
    assertNull($editableOrderedBidirToOneBeed.getOne());
    // set the to many side to some value
    edit = new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed);
    edit.setGoal(new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one));
    edit.perform();
    assertNotNull($editableOrderedBidirToOneBeed.get());
    assertEquals($editableOrderedBidirToOneBeed.getOne(), $one);
  }

  @Test
  public void createInitialEvent() {
    OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> initialEvent =
      $editableOrderedBidirToOneBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $editableOrderedBidirToOneBeed);
    assertEquals(initialEvent.getOldValue(), null);
    assertEquals(initialEvent.getNewValue(), $editableOrderedBidirToOneBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }
}
