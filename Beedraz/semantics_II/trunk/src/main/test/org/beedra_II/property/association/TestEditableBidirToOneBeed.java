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
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Listener;
import org.beedra_II.property.set.SetEdit;
import org.beedra_II.property.set.SetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableBidirToOneBeed {

  public class MyEditableBidirToOneBeed<_One_ extends BeanBeed, _Many_ extends BeanBeed>
      extends EditableBidirToOneBeed<_One_, _Many_> {

    public MyEditableBidirToOneBeed(_Many_ owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(BidirToOneEvent<_One_, _Many_> event) {
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

  public class StubEditableBidirToOneBeedListener implements Listener<SetEvent<Integer, SetEdit<Integer>>> {

    public void beedChanged(SetEvent<Integer, SetEdit<Integer>> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SetEvent<Integer, SetEdit<Integer>> $event;

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
  private MyEditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableBidirToOneBeed =
    new MyEditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
  private OneBeanBeed $one = new OneBeanBeed();
  private BidirToManyBeed<OneBeanBeed, ManyBeanBeed> $bidirToManyBeed =
    new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
  private BidirToOneEdit<OneBeanBeed, ManyBeanBeed> $bidirToOneEdit =
    new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
  private BidirToOneEvent<OneBeanBeed, ManyBeanBeed> $bidirToOneEvent =
    new BidirToOneEvent<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed, null, $bidirToManyBeed, $bidirToOneEdit);
  private PropagatedEventListener $listener1 = new PropagatedEventListener();
  private PropagatedEventListener $listener2 = new PropagatedEventListener();
//  private StubEditableBidirToOneBeedListener $listener3 = new StubEditableBidirToOneBeedListener();
//  private StubEditableBidirToOneBeedListener $listener4 = new StubEditableBidirToOneBeedListener();

  @Test
  public void constructor() {
    assertEquals($editableBidirToOneBeed.getOwner(), $many);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $many.addListener($listener1);
    $many.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableBidirToOneBeed.fire($bidirToOneEvent);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertTrue($listener1.$event instanceof PropagatedEvent);
    assertTrue($listener2.$event instanceof PropagatedEvent);
    assertEquals($bidirToOneEvent, $listener1.$event.getCause());
    assertEquals($bidirToOneEvent, $listener1.$event.getCause());
  }

  @Test
  public void getOne() throws EditStateException, IllegalEditException {
    // set the to many side to null
    BidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    edit.setGoal(null);
    edit.perform();
    assertNull($editableBidirToOneBeed.get());
    assertNull($editableBidirToOneBeed.getOne());
    // set the to many side to some value
    edit =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    edit.setGoal(new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one));
    edit.perform();
    assertNotNull($editableBidirToOneBeed.get());
    assertEquals($editableBidirToOneBeed.getOne(), $one);
  }

  @Test
  public void createInitialEvent1() {
    assertTrue("the implementation of this method calls the constructor " +
        "of BidirToOneEvent; the last parameter of this constructor should be " +
        "effective according to the documentation", false);
  }

  @Test
  public void createInitialEvent2() {
    BidirToOneEvent<OneBeanBeed, ManyBeanBeed> initialEvent =
      $editableBidirToOneBeed.createInitialEvent();
    assertTrue(initialEvent instanceof BidirToOneEvent); // @mudo enough?
    assertEquals(initialEvent.getSource(), $editableBidirToOneBeed);
    assertEquals(initialEvent.getOldValue(), null);
    assertEquals(initialEvent.getNewValue(), $editableBidirToOneBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }
}
