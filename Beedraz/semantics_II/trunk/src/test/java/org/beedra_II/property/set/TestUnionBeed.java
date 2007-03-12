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

package org.beedra_II.property.set;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Listener;
import org.beedra_II.property.association.set.BidirToManyBeed;
import org.beedra_II.property.association.set.BidirToOneEdit;
import org.beedra_II.property.association.set.EditableBidirToOneBeed;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerBeed;
import org.beedra_II.property.integer.IntegerEdit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestUnionBeed {

  public class MyUnionBeed extends UnionBeed<WellBeanBeed> {

    public MyUnionBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * Made public for testing reasons.
     */
    public void fireChangeEventPublic(SetEvent<WellBeanBeed> event) {
      super.fireChangeEvent(event);
    }

  }

  public class RunBeanBeed extends AbstractBeanBeed {
    /**
     * The wells contained in this run.
     */
    public final BidirToManyBeed<RunBeanBeed, WellBeanBeed> wells =
      new BidirToManyBeed<RunBeanBeed, WellBeanBeed>(this);

  }

  public class WellBeanBeed extends AbstractBeanBeed {

    /**
     * The run in which the well is contained.
     */
    public final EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed> run =
      new EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>(this);

    /**
     * The Cq value of the well.
     */
    public final EditableIntegerBeed cq =
      new EditableIntegerBeed(this);

  }

  public class MyBeanBeed extends AbstractBeanBeed {
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

  @Before
  public void setUp() throws Exception {
    $owner = new MyBeanBeed();
    $unionBeed = new MyUnionBeed($owner);
    $runA = new RunBeanBeed();
    $runB = new RunBeanBeed();
    $runC = new RunBeanBeed();
    $wellA1 = new WellBeanBeed();
    $wellA2 = new WellBeanBeed();
    $wellA3 = new WellBeanBeed();
    $wellB1 = new WellBeanBeed();
    $wellB2 = new WellBeanBeed();
    $wellC1 = new WellBeanBeed();
    $wellC2 = new WellBeanBeed();
    $wellC3 = new WellBeanBeed();
    $wellC4 = new WellBeanBeed();
    $cqA1 = new Integer(0);
    $cqA2 = new Integer(1);
    $cqA3 = new Integer(2);
    $cqB1 = new Integer(3);
    $cqB2 = new Integer(4);
    $cqC1 = new Integer(5);
    $cqC2 = new Integer(6);
    $cqC3 = new Integer(7);
    $cqC4 = new Integer(8);
    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
    $event = new SetEvent<WellBeanBeed>($unionBeed, null, null, null);
    // add the wells to the runs
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellA1.run);
    edit.setGoal($runA.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellA2.run);
    edit.setGoal($runA.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellA3.run);
    edit.setGoal($runA.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellB1.run);
    edit.setGoal($runB.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellB2.run);
    edit.setGoal($runB.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellC1.run);
    edit.setGoal($runC.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellC2.run);
    edit.setGoal($runC.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellC3.run);
    edit.setGoal($runC.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellC4.run);
    edit.setGoal($runC.wells);
    edit.perform();
    // set the cq values
    IntegerEdit integerEdit = new IntegerEdit($wellA1.cq);
    integerEdit.setGoal($cqA1);
    integerEdit.perform();
    integerEdit = new IntegerEdit($wellA2.cq);
    integerEdit.setGoal($cqA2);
    integerEdit.perform();
    integerEdit = new IntegerEdit($wellA3.cq);
    integerEdit.setGoal($cqA3);
    integerEdit.perform();
    integerEdit = new IntegerEdit($wellB1.cq);
    integerEdit.setGoal($cqB1);
    integerEdit.perform();
    integerEdit = new IntegerEdit($wellB2.cq);
    integerEdit.setGoal($cqB2);
    integerEdit.perform();
    integerEdit = new IntegerEdit($wellC1.cq);
    integerEdit.setGoal($cqC1);
    integerEdit.perform();
    integerEdit = new IntegerEdit($wellC2.cq);
    integerEdit.setGoal($cqC2);
    integerEdit.perform();
    integerEdit = new IntegerEdit($wellC3.cq);
    integerEdit.setGoal($cqC3);
    integerEdit.perform();
    integerEdit = new IntegerEdit($wellC4.cq);
    integerEdit.setGoal($cqC4);
    integerEdit.perform();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private RunBeanBeed $runA;
  private WellBeanBeed $wellA1;
  private WellBeanBeed $wellA2;
  private WellBeanBeed $wellA3;
  private Integer $cqA1;
  private Integer $cqA2;
  private Integer $cqA3;
  private RunBeanBeed $runB;
  private WellBeanBeed $wellB1;
  private WellBeanBeed $wellB2;
  private Integer $cqB1;
  private Integer $cqB2;
  private RunBeanBeed $runC;
  private WellBeanBeed $wellC1;
  private WellBeanBeed $wellC2;
  private WellBeanBeed $wellC3;
  private WellBeanBeed $wellC4;
  private Integer $cqC1;
  private Integer $cqC2;
  private Integer $cqC3;
  private Integer $cqC4;
  private MyUnionBeed $unionBeed;
  private MyBeanBeed $owner;
  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;
  private SetEvent<WellBeanBeed> $event;

  @Test
  public void constructor() {
    assertEquals($unionBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $unionBeed.fireChangeEventPublic($event);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event, $listener1.$event.getCause());
    assertEquals($event, $listener1.$event.getCause());
  }

  @Test
  public void addSource() {
    assertEquals($unionBeed.getSources().size(), 0);
    $unionBeed.addSource($runA.wells);
    assertEquals($unionBeed.getSources().size(), 1);
    assertTrue($unionBeed.getSources().contains($runA.wells));
    $unionBeed.addSource($runB.wells);
    assertEquals($unionBeed.getSources().size(), 2);
    assertTrue($unionBeed.getSources().contains($runA.wells));
    assertTrue($unionBeed.getSources().contains($runB.wells));
    $unionBeed.addSource($runA.wells); // add the same again
    assertEquals($unionBeed.getSources().size(), 2);
    assertTrue($unionBeed.getSources().contains($runA.wells));
    assertTrue($unionBeed.getSources().contains($runB.wells));
    $unionBeed.addSource($runC.wells);
    assertEquals($unionBeed.getSources().size(), 3);
    assertTrue($unionBeed.getSources().contains($runA.wells));
    assertTrue($unionBeed.getSources().contains($runB.wells));
    assertTrue($unionBeed.getSources().contains($runC.wells));
  }

  @Test
  public void removeSource() {
    $unionBeed.addSource($runA.wells);
    $unionBeed.addSource($runB.wells);
    $unionBeed.addSource($runC.wells);
    assertEquals($unionBeed.getSources().size(), 3);
    assertTrue($unionBeed.getSources().contains($runA.wells));
    assertTrue($unionBeed.getSources().contains($runB.wells));
    assertTrue($unionBeed.getSources().contains($runC.wells));
    $unionBeed.removeSource($runA.wells);
    assertEquals($unionBeed.getSources().size(), 2);
    assertTrue($unionBeed.getSources().contains($runB.wells));
    assertTrue($unionBeed.getSources().contains($runC.wells));
    $unionBeed.removeSource($runB.wells);
    assertEquals($unionBeed.getSources().size(), 1);
    assertTrue($unionBeed.getSources().contains($runC.wells));
    $unionBeed.removeSource($runA.wells); // remove source that is not in getSources
    assertEquals($unionBeed.getSources().size(), 1);
    assertTrue($unionBeed.getSources().contains($runC.wells));
    $unionBeed.removeSource($runC.wells);
    assertEquals($unionBeed.getSources().size(), 0);
  }

  /**
   * All sets are disjunct.
   */
  @Test
  public void get1() {
    $unionBeed.addSource($runA.wells);
    $unionBeed.addSource($runB.wells);
    $unionBeed.addSource($runC.wells);
    Set<WellBeanBeed> result = $unionBeed.get();
    assertEquals(
        result.size(),
        $runA.wells.get().size() + $runB.wells.get().size() +
        $runC.wells.get().size());
    assertTrue(result.contains($wellA1));
    assertTrue(result.contains($wellA2));
    assertTrue(result.contains($wellA3));
    assertTrue(result.contains($wellB1));
    assertTrue(result.contains($wellB2));
    assertTrue(result.contains($wellC1));
    assertTrue(result.contains($wellC2));
    assertTrue(result.contains($wellC3));
    assertTrue(result.contains($wellC4));
    Iterator<WellBeanBeed> iterator = result.iterator();
    for (int i = 0; i < result.size(); i++) {
      assertTrue(iterator.hasNext());
      iterator.next(); // we know nothing about the order of the elements returned by the iterator
    }
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
    // no sources
    $unionBeed.removeSource($runA.wells);
    $unionBeed.removeSource($runB.wells);
    $unionBeed.removeSource($runC.wells);
    result = $unionBeed.get();
    assertEquals(result.size(), 0);
    iterator = result.iterator();
    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
  }
  /**
   * Sets are not disjunct.
   * @throws IllegalEditException
   * @throws EditStateException
   */
  @Test
  public void get2() throws EditStateException, IllegalEditException {
    // create two set beeds whose sets overlap
    EditableSetBeed<WellBeanBeed> setBeed1 = new EditableSetBeed<WellBeanBeed>($owner);
    SetEdit<WellBeanBeed> setEdit1 = new SetEdit<WellBeanBeed>(setBeed1);
    setEdit1.addElementToAdd($wellC1);
    setEdit1.addElementToAdd($wellC2);
    setEdit1.addElementToAdd($wellC3);
    setEdit1.perform();
    EditableSetBeed<WellBeanBeed> setBeed2 = new EditableSetBeed<WellBeanBeed>($owner);
    SetEdit<WellBeanBeed> setEdit2 = new SetEdit<WellBeanBeed>(setBeed2);
    setEdit2.addElementToAdd($wellC3);
    setEdit2.addElementToAdd($wellC4);
    setEdit2.perform();
    // check get
    $unionBeed.addSource(setBeed1);
    $unionBeed.addSource(setBeed2);
    Set<WellBeanBeed> result = $unionBeed.get();
    assertEquals(result.size(), 4); // not 5!
    assertTrue(result.contains($wellC1));
    assertTrue(result.contains($wellC2));
    assertTrue(result.contains($wellC3));
    assertTrue(result.contains($wellC4));
    Iterator<WellBeanBeed> iterator = result.iterator();
    for (int i = 0; i < result.size(); i++) {
      assertTrue(iterator.hasNext());
      iterator.next(); // we know nothing about the order of the elements returned by the iterator
    }
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
  }

  @Test
  public void createInitialEvent() {
    SetEvent<WellBeanBeed> initialEvent = $unionBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $unionBeed);
    assertEquals(initialEvent.getAddedElements(), $unionBeed.get());
    assertEquals(initialEvent.getRemovedElements(), new HashSet<WellBeanBeed>());
    assertEquals(initialEvent.getEdit(), null);
  }

  @Test
  public void computeSum() {
    $unionBeed.addSource($runA.wells);
    $unionBeed.addSource($runB.wells);
    $unionBeed.addSource($runC.wells);
    Mapping<WellBeanBeed, IntegerBeed> mapping =
      new Mapping<WellBeanBeed, IntegerBeed>() {

        /**
         * @pre  from != null;
         */
        public IntegerBeed map(WellBeanBeed from) {
          return from.cq;
        }

    };
    MappedSetBeed<WellBeanBeed, PropagatedEvent, IntegerBeed> mappedSetBeed =
      new MappedSetBeed<WellBeanBeed, PropagatedEvent, IntegerBeed>(mapping, $owner);
    mappedSetBeed.setSource($unionBeed);
    Integer sum = 0;
    for (IntegerBeed cq : mappedSetBeed.get()) {
      sum += cq.getInteger();
    }
    assertEquals(sum, 0+1+2+3+4+5+6+7+8);
  }

}