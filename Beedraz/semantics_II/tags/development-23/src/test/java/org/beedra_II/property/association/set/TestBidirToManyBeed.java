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

package org.beedra_II.property.association.set;

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
import org.beedra_II.property.collection.set.ActualSetEvent;
import org.beedra_II.property.collection.set.SetEvent;
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
    public void fire(SetEvent<_Many_> event) {
      fireChangeEvent(event);
    }
  }

  public class OneBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class ManyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class StubSetEventListener implements Listener<SetEvent<ManyBeanBeed>> {

    public void beedChanged(SetEvent<ManyBeanBeed> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SetEvent<ManyBeanBeed> $event;

  }

  @Before
  public void setUp() throws Exception {
    $many = new ManyBeanBeed();
    $editableBidirToOneBeed =
      new EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
    $one = new OneBeanBeed();
    $bidirToManyBeed =
      new MyBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
    $bidirToOneEdit =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    $bidirToOneEdit.perform();
    $setEvent = new ActualSetEvent<ManyBeanBeed>($bidirToManyBeed, null, null, $bidirToOneEdit);
    $listener1 = new StubListener<PropagatedEvent>();
    $listener2 = new StubListener<PropagatedEvent>();
    $listener3 = new StubSetEventListener();
    $listener4 = new StubSetEventListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many;
  private EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableBidirToOneBeed;
  private OneBeanBeed $one = new OneBeanBeed();
  private MyBidirToManyBeed<OneBeanBeed, ManyBeanBeed> $bidirToManyBeed;
  private BidirToOneEdit<OneBeanBeed, ManyBeanBeed> $bidirToOneEdit;
  private SetEvent<ManyBeanBeed> $setEvent;
  private StubListener<PropagatedEvent> $listener1;
  private StubListener<PropagatedEvent> $listener2;
  private StubSetEventListener $listener3;
  private StubSetEventListener $listener4;

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
  public void fireAddedEvent() throws EditStateException, IllegalEditException {
    // add listeners to the bidirToManyBeed
    $bidirToManyBeed.addListener($listener3);
    $bidirToManyBeed.addListener($listener4);
    assertNull($listener3.$event);
    assertNull($listener4.$event);
    // fire events
    ManyBeanBeed many1 = new ManyBeanBeed();
    BidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit1 =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    edit1.perform();
    $bidirToManyBeed.fireAddedEvent(many1, edit1);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $bidirToManyBeed);
    assertEquals($listener3.$event.getAddedElements().size(), 1);
    assertTrue($listener3.$event.getAddedElements().contains(many1));
    assertTrue($listener3.$event.getRemovedElements().isEmpty());
    assertEquals($listener3.$event.getEdit(), edit1);
    assertNotNull($listener4.$event);
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
    edit2.perform();
    $bidirToManyBeed.fireAddedEvent(many2, edit2);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $bidirToManyBeed);
    assertEquals($listener3.$event.getAddedElements().size(), 1);
    assertTrue($listener3.$event.getAddedElements().contains(many2));
    assertTrue($listener3.$event.getRemovedElements().isEmpty());
    assertEquals($listener3.$event.getEdit(), edit2);
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getSource(), $bidirToManyBeed);
    assertEquals($listener4.$event.getAddedElements().size(), 1);
    assertTrue($listener4.$event.getAddedElements().contains(many2));
    assertTrue($listener4.$event.getRemovedElements().isEmpty());
    assertEquals($listener4.$event.getEdit(), edit2);
  }

  @Test
  public void remove() {
    ManyBeanBeed many1 = new ManyBeanBeed();
    ManyBeanBeed many2 = new ManyBeanBeed();
    $bidirToManyBeed.add(many1);
    assertTrue($bidirToManyBeed.get().contains(many1));
    assertFalse($bidirToManyBeed.get().contains(many2));
    $bidirToManyBeed.add(many2);
    assertTrue($bidirToManyBeed.get().contains(many1));
    assertTrue($bidirToManyBeed.get().contains(many2));
    $bidirToManyBeed.remove(many2);
    assertTrue($bidirToManyBeed.get().contains(many1));
    assertFalse($bidirToManyBeed.get().contains(many2));
    $bidirToManyBeed.remove(many1);
    assertFalse($bidirToManyBeed.get().contains(many1));
    assertFalse($bidirToManyBeed.get().contains(many2));
  }

  @Test
  public void fireRemovedEvent() throws EditStateException, IllegalEditException {
    // add listeners to the bidirToManyBeed
    $bidirToManyBeed.addListener($listener3);
    $bidirToManyBeed.addListener($listener4);
    assertNull($listener3.$event);
    assertNull($listener4.$event);
    // fire events
    ManyBeanBeed many1 = new ManyBeanBeed();
    BidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit1 =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    edit1.perform();
    $bidirToManyBeed.fireRemovedEvent(many1, edit1);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $bidirToManyBeed);
    assertTrue($listener3.$event.getAddedElements().isEmpty());
    assertEquals($listener3.$event.getRemovedElements().size(), 1);
    assertTrue($listener3.$event.getRemovedElements().contains(many1));
    assertEquals($listener3.$event.getEdit(), edit1);
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getSource(), $bidirToManyBeed);
    assertTrue($listener4.$event.getAddedElements().isEmpty());
    assertEquals($listener4.$event.getRemovedElements().size(), 1);
    assertTrue($listener4.$event.getRemovedElements().contains(many1));
    assertEquals($listener4.$event.getEdit(), edit1);
    // reset
    $listener3.reset();
    $listener4.reset();
    // fire events
    ManyBeanBeed many2 = new ManyBeanBeed();
    BidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit2 =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    edit2.perform();
    $bidirToManyBeed.fireRemovedEvent(many2, edit2);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $bidirToManyBeed);
    assertTrue($listener3.$event.getAddedElements().isEmpty());
    assertEquals($listener3.$event.getRemovedElements().size(), 1);
    assertTrue($listener3.$event.getRemovedElements().contains(many2));
    assertEquals($listener3.$event.getEdit(), edit2);
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getSource(), $bidirToManyBeed);
    assertTrue($listener4.$event.getAddedElements().isEmpty());
    assertEquals($listener4.$event.getRemovedElements().size(), 1);
    assertTrue($listener4.$event.getRemovedElements().contains(many2));
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
    SetEvent<ManyBeanBeed> initialEvent =
      $bidirToManyBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $bidirToManyBeed);
    assertEquals(initialEvent.getAddedElements(), $bidirToManyBeed.get());
    assertTrue(initialEvent.getRemovedElements().isEmpty());
    assertEquals(initialEvent.getEdit(), null);
  }
}
