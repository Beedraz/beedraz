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

package org.beedra_II.property.association.set.sorted;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Listener;
import org.beedra_II.property.association.set.sorted.EditableSortedBidirToOneBeed;
import org.beedra_II.property.association.set.sorted.SortedBidirToManyBeed;
import org.beedra_II.property.association.set.sorted.SortedBidirToOneEdit;
import org.beedra_II.property.association.set.sorted.SortedBidirToOneEvent;
import org.beedra_II.property.set.sorted.SortedSetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableSortedBidirToOneBeed {

  public class MyEditableSortedBidirToOneBeed<_One_ extends BeanBeed, _Many_ extends BeanBeed>
      extends EditableSortedBidirToOneBeed<_One_, _Many_> {

    public MyEditableSortedBidirToOneBeed(_Many_ owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(SortedBidirToOneEvent<_One_, _Many_> event) {
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

  public class StubEditableSortedBidirToOneBeedListener implements Listener<SortedSetEvent<Integer>> {

    public void beedChanged(SortedSetEvent<Integer> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SortedSetEvent<Integer> $event;

  }

  @Before
  public void setUp() throws Exception {
    $many = new ManyBeanBeed();
    $editableSortedBidirToOneBeed =
      new MyEditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
    $one = new OneBeanBeed();
    $sortedBidirToManyBeed =
      new SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
    $sortedBidirToOneEdit =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableSortedBidirToOneBeed);
    $sortedBidirToOneEdit.perform();
    $sortedBidirToOneEvent =
      new SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>($editableSortedBidirToOneBeed, null, $sortedBidirToManyBeed, $sortedBidirToOneEdit);
    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
//  $listener3 = new StubEditableSortedBidirToOneBeedListener();
//  $listener4 = new StubEditableSortedBidirToOneBeedListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many;
  private MyEditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableSortedBidirToOneBeed;
  private OneBeanBeed $one;
  private SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> $sortedBidirToManyBeed;
  private SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> $sortedBidirToOneEdit;
  private SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> $sortedBidirToOneEvent;
  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;
//  private StubEditableSortedBidirToOneBeedListener $listener3;
//  private StubEditableSortedBidirToOneBeedListener $listener4;

  @Test
  public void constructor() {
    assertEquals($editableSortedBidirToOneBeed.getOwner(), $many);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $many.addListener($listener1);
    $many.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableSortedBidirToOneBeed.fire($sortedBidirToOneEvent);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($sortedBidirToOneEvent, $listener1.$event.getCause());
    assertEquals($sortedBidirToOneEvent, $listener1.$event.getCause());
  }

  @Test
  public void getOne() throws EditStateException, IllegalEditException {
    // set the to many side to null
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableSortedBidirToOneBeed);
    edit.setGoal(null);
    edit.perform();
    assertNull($editableSortedBidirToOneBeed.get());
    assertNull($editableSortedBidirToOneBeed.getOne());
    // set the to many side to some value
    edit = new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableSortedBidirToOneBeed);
    edit.setGoal(new SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one));
    edit.perform();
    assertNotNull($editableSortedBidirToOneBeed.get());
    assertEquals($editableSortedBidirToOneBeed.getOne(), $one);
  }

  @Test
  public void createInitialEvent() {
    SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> initialEvent =
      $editableSortedBidirToOneBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $editableSortedBidirToOneBeed);
    assertEquals(initialEvent.getOldValue(), null);
    assertEquals(initialEvent.getNewValue(), $editableSortedBidirToOneBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }
}
