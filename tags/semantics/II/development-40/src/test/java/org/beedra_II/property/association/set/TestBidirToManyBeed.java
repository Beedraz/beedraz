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

import org.beedra_II.Listener;
import org.beedra_II.StubListener;
import org.beedra_II.aggregate.AggregateEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.collection.set.ActualSetEvent;
import org.beedra_II.property.collection.set.SetEvent;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
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
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(SetEvent<_Many_> event) {
      updateDependents(event);
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
    $many1 = new ManyBeanBeed();
    $editableBidirToOneBeed =
      new EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
    $one = new OneBeanBeed();
    $bidirToManyBeed =
      new MyBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
    $bidirToOneEdit =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    $bidirToOneEdit.perform();
    $setEvent = new ActualSetEvent<ManyBeanBeed>($bidirToManyBeed, null, null, $bidirToOneEdit);
    $listener1 = new StubListener<AggregateEvent>();
    $listener2 = new StubListener<AggregateEvent>();
    $listener5 = new StubListener<ActualLongEvent>();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many;
  private ManyBeanBeed $many1;
  private EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableBidirToOneBeed;
  private OneBeanBeed $one = new OneBeanBeed();
  private MyBidirToManyBeed<OneBeanBeed, ManyBeanBeed> $bidirToManyBeed;
  private BidirToOneEdit<OneBeanBeed, ManyBeanBeed> $bidirToOneEdit;
  private SetEvent<ManyBeanBeed> $setEvent;
  private StubListener<AggregateEvent> $listener1;
  private StubListener<AggregateEvent> $listener2;
  private StubListener<ActualLongEvent> $listener5;

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
    $bidirToManyBeed.publicUpdateDependents($setEvent);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentevents().size());
    assertEquals(1, $listener2.$event.getComponentevents().size());
    assertTrue($listener1.$event.getComponentevents().contains($setEvent));
    assertTrue($listener2.$event.getComponentevents().contains($setEvent));
  }

  @Test
  public void add() {
//    // check size
//    assertEquals($bidirToManyBeed.getSize().getLong(), 0L);
//    assertEquals($bidirToManyBeed.getCardinality().getLong(), 0L);
//    // add elements
    ManyBeanBeed many1 = new ManyBeanBeed();
    $bidirToManyBeed.add(many1);
    assertTrue($bidirToManyBeed.get().contains(many1));
//    assertEquals($bidirToManyBeed.getSize().getLong(), 1L);
//    assertEquals($bidirToManyBeed.getCardinality().getLong(), 1L);
//    // add elements
    ManyBeanBeed many2 = new ManyBeanBeed();
    $bidirToManyBeed.add(many2);
    assertTrue($bidirToManyBeed.get().contains(many2));
//    assertEquals($bidirToManyBeed.getSize().getLong(), 2L);
//    assertEquals($bidirToManyBeed.getCardinality().getLong(), 2L);
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
  public void getSizeAndCardinality() throws EditStateException, IllegalEditException {
    // add a listener to the size beed
    IntegerBeed<ActualLongEvent> sizeBeed = $bidirToManyBeed.getSize();
    sizeBeed.addListener($listener5);
    assertNull($listener5.$event);
    // check the size
    assertEquals($bidirToManyBeed.getSize().getLong(), 0L);
    assertEquals($bidirToManyBeed.getCardinality().getLong(), 0L);
    // add elements
    $bidirToOneEdit = new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    $bidirToOneEdit.setGoal($bidirToManyBeed);
    $bidirToOneEdit.perform();
    // check the size
    assertEquals($bidirToManyBeed.getSize().getLong(), 1L);
    assertEquals($bidirToManyBeed.getCardinality().getLong(), 1L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 0L);
    assertEquals($listener5.$event.getNewLong(), 1L);
    assertEquals($listener5.$event.getEdit(), $bidirToOneEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add elements
    assertFalse($bidirToManyBeed.get().contains($many1));
    EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> oneBeed = new EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many1);
    assertNull(oneBeed.get());
    $bidirToOneEdit = new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>(oneBeed);
    $bidirToOneEdit.setGoal($bidirToManyBeed);
    $bidirToOneEdit.perform();
    assertTrue($bidirToManyBeed.get().contains($many1));
    // check the size
    assertEquals($bidirToManyBeed.getSize().getLong(), 2L);
    assertEquals($bidirToManyBeed.getCardinality().getLong(), 2L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 1L);
    assertEquals($listener5.$event.getNewLong(), 2L);
    assertEquals($listener5.$event.getEdit(), $bidirToOneEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    $bidirToOneEdit = new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>(oneBeed);
    $bidirToOneEdit.setGoal(null);
    $bidirToOneEdit.perform();
    // check the size
    assertEquals($bidirToManyBeed.getSize().getLong(), 1L);
    assertEquals($bidirToManyBeed.getCardinality().getLong(), 1L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 2L);
    assertEquals($listener5.$event.getNewLong(), 1L);
    assertEquals($listener5.$event.getEdit(), $bidirToOneEdit);
  }
}
