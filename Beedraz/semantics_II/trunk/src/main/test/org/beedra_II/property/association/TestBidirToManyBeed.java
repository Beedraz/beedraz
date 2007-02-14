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

package org.beedra_II.property.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.set.SetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBidirToManyBeed {

  public class MyBidirToManyBeed<_One_ extends BeanBeed, _Many_ extends BeanBeed>
      extends BidirToManyBeed<_One_, _Many_> {

    public MyBidirToManyBeed(_One_ owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(SetEvent<_Many_, BidirToOneEdit<_One_, _Many_>> event) {
      fireChangeEvent(event);
    }
  }

  public class OneBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class ManyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class PropagatedEventListener implements Listener<PropagatedEvent> {

    public void beedChanged(PropagatedEvent event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public PropagatedEvent $event;

  }

  public class StubSetEventListener implements Listener<SetEvent<ManyBeanBeed, BidirToOneEdit<OneBeanBeed, ManyBeanBeed>>> {

    public void beedChanged(SetEvent<ManyBeanBeed, BidirToOneEdit<OneBeanBeed, ManyBeanBeed>> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SetEvent<ManyBeanBeed, BidirToOneEdit<OneBeanBeed, ManyBeanBeed>> $event;

  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many = new ManyBeanBeed();
  private EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableBidirToOneBeed =
    new EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
  private OneBeanBeed $one = new OneBeanBeed();
  private MyBidirToManyBeed<OneBeanBeed, ManyBeanBeed> $bidirToManyBeed =
    new MyBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
  private BidirToOneEdit<OneBeanBeed, ManyBeanBeed> $bidirToOneEdit =
    new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
  private SetEvent<ManyBeanBeed, BidirToOneEdit<OneBeanBeed, ManyBeanBeed>> $setEvent =
    new SetEvent<ManyBeanBeed, BidirToOneEdit<OneBeanBeed,ManyBeanBeed>>($bidirToManyBeed, null, null, $bidirToOneEdit);
  private PropagatedEventListener $listener1 = new PropagatedEventListener();
  private PropagatedEventListener $listener2 = new PropagatedEventListener();
  private StubSetEventListener $listener3 = new StubSetEventListener();
  private StubSetEventListener $listener4 = new StubSetEventListener();

  @Test
  public void constructor() {
    assertEquals($bidirToManyBeed.getOwner(), $one);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $one.addListener($listener1);
    $one.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $bidirToManyBeed.fire($setEvent);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertTrue($listener1.$event instanceof PropagatedEvent);
    assertTrue($listener2.$event instanceof PropagatedEvent);
    assertEquals($setEvent, $listener1.$event.getCause());
    assertEquals($setEvent, $listener1.$event.getCause());
  }

  @Test
  public void add() {
    ManyBeanBeed many1 = new ManyBeanBeed();
    $bidirToManyBeed.add(many1);
    assertTrue($bidirToManyBeed.get().contains(many1));
    ManyBeanBeed many2 = new ManyBeanBeed();
    $bidirToManyBeed.add(many2);
    assertTrue($bidirToManyBeed.get().contains(many2));
  }

  @Test
  public void fireAddedEvent() {
    // add listeners to the bidirToManyBeed
    $bidirToManyBeed.addListener($listener3);
    $bidirToManyBeed.addListener($listener4);
    assertNull($listener3.$event);
    assertNull($listener4.$event);
    // fire events
    ManyBeanBeed many1 = new ManyBeanBeed();
    BidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit1 =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    $bidirToManyBeed.fireAddedEvent(many1, edit1);
    assertNotNull($listener3.$event);
    assertTrue($listener3.$event instanceof SetEvent);
    assertEquals($listener3.$event.getSource(), $bidirToManyBeed);
    assertEquals($listener3.$event.getAddedElements().size(), 1);
    assertTrue($listener3.$event.getAddedElements().contains(many1));
    assertTrue($listener3.$event.getRemovedElements().isEmpty());
    assertEquals($listener3.$event.getEdit(), edit1);
    assertNotNull($listener4.$event);
    assertTrue($listener4.$event instanceof SetEvent);
    assertEquals($listener4.$event.getSource(), $bidirToManyBeed);
    assertEquals($listener4.$event.getAddedElements().size(), 1);
    assertTrue($listener4.$event.getAddedElements().contains(many1));
    assertTrue($listener4.$event.getRemovedElements().isEmpty());
    assertEquals($listener4.$event.getEdit(), edit1);
    // reset
    $listener3.reset();
    $listener4.reset();
    // fire events
    ManyBeanBeed many2 = new ManyBeanBeed();
    BidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit2 =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    $bidirToManyBeed.fireAddedEvent(many2, edit2);
    assertNotNull($listener3.$event);
    assertTrue($listener3.$event instanceof SetEvent);
    assertEquals($listener3.$event.getSource(), $bidirToManyBeed);
    assertEquals($listener3.$event.getAddedElements().size(), 1);
    assertTrue($listener3.$event.getAddedElements().contains(many2));
    assertTrue($listener3.$event.getRemovedElements().isEmpty());
    assertEquals($listener3.$event.getEdit(), edit2);
    assertNotNull($listener4.$event);
    assertTrue($listener4.$event instanceof SetEvent);
    assertEquals($listener4.$event.getSource(), $bidirToManyBeed);
    assertEquals($listener4.$event.getAddedElements().size(), 1);
    assertTrue($listener4.$event.getAddedElements().contains(many2));
    assertTrue($listener4.$event.getRemovedElements().isEmpty());
    assertEquals($listener4.$event.getEdit(), edit2);
  }

//  @Test
//  public void createInitialEvent1() {
//    assertTrue("the implementation of this method calls the constructor " +
//        "of SetEvent; the last parameter of this constructor should be " +
//        "effective according to the documentation", false);
//  }

  @Test
  public void createInitialEvent2() {
    SetEvent<ManyBeanBeed, BidirToOneEdit<OneBeanBeed, ManyBeanBeed>> initialEvent =
      $bidirToManyBeed.createInitialEvent();
    assertTrue(initialEvent instanceof SetEvent); // @mudo enough?
    assertEquals(initialEvent.getSource(), $bidirToManyBeed);
    assertEquals(initialEvent.getAddedElements(), $bidirToManyBeed.get());
    assertTrue(initialEvent.getRemovedElements().isEmpty());
    assertEquals(initialEvent.getEdit(), null);
  }
}
