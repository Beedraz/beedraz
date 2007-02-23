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
    $many = new ManyBeanBeed();
    $editableBidirToOneBeed =
      new MyEditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
    $one = new OneBeanBeed();
    $bidirToManyBeed =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
    $bidirToOneEdit =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    $bidirToOneEdit.perform();
    $bidirToOneEvent =
      new BidirToOneEvent<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed, null, $bidirToManyBeed, $bidirToOneEdit);
    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
//  $listener3 = new StubEditableBidirToOneBeedListener();
//  $listener4 = new StubEditableBidirToOneBeedListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many;
  private MyEditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableBidirToOneBeed;
  private OneBeanBeed $one;
  private BidirToManyBeed<OneBeanBeed, ManyBeanBeed> $bidirToManyBeed;
  private BidirToOneEdit<OneBeanBeed, ManyBeanBeed> $bidirToOneEdit;
  private BidirToOneEvent<OneBeanBeed, ManyBeanBeed> $bidirToOneEvent;
  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;
//  private StubEditableBidirToOneBeedListener $listener3;
//  private StubEditableBidirToOneBeedListener $listener4;

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
  public void createInitialEvent() {
    BidirToOneEvent<OneBeanBeed, ManyBeanBeed> initialEvent =
      $editableBidirToOneBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $editableBidirToOneBeed);
    assertEquals(initialEvent.getOldValue(), null);
    assertEquals(initialEvent.getNewValue(), $editableBidirToOneBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }
}
