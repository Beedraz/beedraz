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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Listener;
import org.beedra_II.property.association.set.sorted.EditableSortedBidirToOneBeed;
import org.beedra_II.property.association.set.sorted.SortedBidirToManyBeed;
import org.beedra_II.property.association.set.sorted.SortedBidirToOneEdit;
import org.beedra_II.property.set.sorted.SortedSetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSortedBidirToManyBeed {

  public class MySortedBidirToManyBeed<_One_ extends BeanBeed, _Many_ extends BeanBeed>
      extends SortedBidirToManyBeed<_One_, _Many_> {

    public MySortedBidirToManyBeed(_One_ owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(SortedSetEvent<_Many_> event) {
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

  public class StubSortedSetEventListener implements Listener<SortedSetEvent<ManyBeanBeed>> {

    public void beedChanged(SortedSetEvent<ManyBeanBeed> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SortedSetEvent<ManyBeanBeed> $event;

  }

  @Before
  public void listUp() throws Exception {
    $many = new ManyBeanBeed();
    $editableSortedBidirToOneBeed =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
    $one = new OneBeanBeed();
    $sortedBidirToManyBeed =
      new MySortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
    $sortedBidirToOneEdit =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableSortedBidirToOneBeed);
    $sortedBidirToOneEdit.perform();
    $listEvent = new SortedSetEvent<ManyBeanBeed>($sortedBidirToManyBeed, null, null, $sortedBidirToOneEdit);
    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
    $listener3 = new StubSortedSetEventListener();
    $listener4 = new StubSortedSetEventListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many;
  private EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableSortedBidirToOneBeed;
  private OneBeanBeed $one = new OneBeanBeed();
  private MySortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> $sortedBidirToManyBeed;
  private SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> $sortedBidirToOneEdit;
  private SortedSetEvent<ManyBeanBeed> $listEvent;
  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;
  private StubSortedSetEventListener $listener3;
  private StubSortedSetEventListener $listener4;

  @Test
  public void constructor() {
    assertEquals($sortedBidirToManyBeed.getOwner(), $one);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $one.addListener($listener1);
    $one.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $sortedBidirToManyBeed.fire($listEvent);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($listEvent, $listener1.$event.getCause());
    assertEquals($listEvent, $listener1.$event.getCause());
  }

  @Test
  public void indexOf() throws EditStateException, IllegalEditException {
    // create some bidirectional associations with $sortedBidirToManyBeed
    ManyBeanBeed many1 = new ManyBeanBeed();
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne1 =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many1);
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit1 =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(toOne1);
    edit1.setGoal($sortedBidirToManyBeed);
    edit1.perform();
    ManyBeanBeed many2 = new ManyBeanBeed();
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne2 =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many2);
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit2 =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(toOne2);
    edit2.setGoal($sortedBidirToManyBeed);
    edit2.perform();
    ManyBeanBeed many3 = new ManyBeanBeed();
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne3 =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many3);
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit3 =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(toOne3);
    edit3.setGoal($sortedBidirToManyBeed);
    edit3.perform();
    ManyBeanBeed many4 = new ManyBeanBeed();
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> toOne4 =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many4);
    // check indexOf
    assertEquals($sortedBidirToManyBeed.indexOf(toOne1.getOwner()), 0);
    assertEquals($sortedBidirToManyBeed.indexOf(toOne2.getOwner()), 1);
    assertEquals($sortedBidirToManyBeed.indexOf(toOne3.getOwner()), 2);
    assertEquals($sortedBidirToManyBeed.indexOf(toOne4.getOwner()), -1);
  }
  @Test
  public void add() {
    ManyBeanBeed many1 = new ManyBeanBeed();
    $sortedBidirToManyBeed.add(0, many1);
    assertTrue($sortedBidirToManyBeed.get().contains(many1));
    assertEquals($sortedBidirToManyBeed.get().get(0), many1);
    ManyBeanBeed many2 = new ManyBeanBeed();
    $sortedBidirToManyBeed.add(1, many2);
    assertTrue($sortedBidirToManyBeed.get().contains(many1));
    assertTrue($sortedBidirToManyBeed.get().contains(many2));
    assertEquals($sortedBidirToManyBeed.get().get(0), many1);
    assertEquals($sortedBidirToManyBeed.get().get(1), many2);
    ManyBeanBeed many3 = new ManyBeanBeed();
    $sortedBidirToManyBeed.add(0, many3);
    assertTrue($sortedBidirToManyBeed.get().contains(many1));
    assertTrue($sortedBidirToManyBeed.get().contains(many2));
    assertTrue($sortedBidirToManyBeed.get().contains(many3));
    assertEquals($sortedBidirToManyBeed.get().get(0), many3);
    assertEquals($sortedBidirToManyBeed.get().get(1), many1);
    assertEquals($sortedBidirToManyBeed.get().get(2), many2);
    $sortedBidirToManyBeed.add(2, many3);
    assertTrue($sortedBidirToManyBeed.get().contains(many1));
    assertTrue($sortedBidirToManyBeed.get().contains(many2));
    assertTrue($sortedBidirToManyBeed.get().contains(many3));
    assertEquals($sortedBidirToManyBeed.get().get(0), many3);
    assertEquals($sortedBidirToManyBeed.get().get(1), many1);
    assertEquals($sortedBidirToManyBeed.get().get(2), many3);
    assertEquals($sortedBidirToManyBeed.get().get(3), many2);
  }

  @Test
  public void remove() {
    ManyBeanBeed many1 = new ManyBeanBeed();
    ManyBeanBeed many2 = new ManyBeanBeed();
    $sortedBidirToManyBeed.add(0, many1);
    assertEquals($sortedBidirToManyBeed.get().get(0), many1);
    assertFalse($sortedBidirToManyBeed.get().contains(many2));
    $sortedBidirToManyBeed.add(1, many2);
    assertEquals($sortedBidirToManyBeed.get().get(0), many1);
    assertEquals($sortedBidirToManyBeed.get().get(1), many2);
    $sortedBidirToManyBeed.remove(many2);
    assertEquals($sortedBidirToManyBeed.get().get(0), many1);
    assertFalse($sortedBidirToManyBeed.get().contains(many2));
    $sortedBidirToManyBeed.remove(many1);
    assertFalse($sortedBidirToManyBeed.get().contains(many1));
    assertFalse($sortedBidirToManyBeed.get().contains(many2));
  }

  @Test
  public void fireChangeEvent() throws EditStateException, IllegalEditException {
    // add listeners to the sortedBidirToManyBeed
    $sortedBidirToManyBeed.addListener($listener3);
    $sortedBidirToManyBeed.addListener($listener4);
    assertNull($listener3.$event);
    assertNull($listener4.$event);
    // fire events
    ManyBeanBeed m1 = new ManyBeanBeed();
    ManyBeanBeed m2 = new ManyBeanBeed();
    ManyBeanBeed m3 = new ManyBeanBeed();
    List<ManyBeanBeed> oldValue = new ArrayList<ManyBeanBeed>();
    oldValue.add(m1);
    List<ManyBeanBeed> newValue = new ArrayList<ManyBeanBeed>();
    newValue.add(m2);
    newValue.add(m3);
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit1 =
      new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableSortedBidirToOneBeed);
    edit1.perform();
    $sortedBidirToManyBeed.fireChangeEvent(oldValue, newValue, edit1);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $sortedBidirToManyBeed);
    assertEquals($listener3.$event.getOldValue().size(), 1);
    assertTrue($listener3.$event.getOldValue().contains(m1));
    assertEquals($listener3.$event.getNewValue().size(), 2);
    assertTrue($listener3.$event.getNewValue().contains(m2));
    assertTrue($listener3.$event.getNewValue().contains(m3));
    assertEquals($listener3.$event.getEdit(), edit1);
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getSource(), $sortedBidirToManyBeed);
    assertEquals($listener4.$event.getOldValue().size(), 1);
    assertTrue($listener4.$event.getOldValue().contains(m1));
    assertEquals($listener4.$event.getNewValue().size(), 2);
    assertTrue($listener4.$event.getNewValue().contains(m2));
    assertTrue($listener4.$event.getNewValue().contains(m3));
    assertEquals($listener4.$event.getEdit(), edit1);
  }

////  @Test
////  public void createInitialEvent1() {
////    assertTrue("the implementation of this method calls the constructor " +
////        "of ListEvent; the last parameter of this constructor should be " +
////        "effective according to the documentation", false);
////  }

  @Test
  public void createInitialEvent2() {
    SortedSetEvent<ManyBeanBeed> initialEvent =
      $sortedBidirToManyBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $sortedBidirToManyBeed);
    assertEquals(initialEvent.getOldValue(), null);
    assertEquals(initialEvent.getNewValue(), $sortedBidirToManyBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }
}
