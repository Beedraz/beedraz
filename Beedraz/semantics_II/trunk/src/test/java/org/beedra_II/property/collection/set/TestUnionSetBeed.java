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

package org.beedra_II.property.collection.set;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.beedra_II.BeedMapping;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.association.set.BidirToManyBeed;
import org.beedra_II.property.association.set.BidirToOneEdit;
import org.beedra_II.property.association.set.EditableBidirToOneBeed;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerBeed;
import org.beedra_II.property.integer.IntegerEdit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestUnionSetBeed {

  public class MyUnionBeed extends UnionSetBeed<WellBeanBeed> {

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

  @Before
  public void setUp() throws Exception {
    $owner = new MyBeanBeed();
    $unionSetBeed = new MyUnionBeed($owner);
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
    $listener1 = new StubListener<PropagatedEvent>();
    $listener2 = new StubListener<PropagatedEvent>();
    $listener3 = new StubListener<SetEvent<WellBeanBeed>>();
    $event = new ActualSetEvent<WellBeanBeed>($unionSetBeed, null, null, null);
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
  private MyUnionBeed $unionSetBeed;
  private MyBeanBeed $owner;
  private StubListener<PropagatedEvent> $listener1;
  private StubListener<PropagatedEvent> $listener2;
  private StubListener<SetEvent<WellBeanBeed>> $listener3;
  private SetEvent<WellBeanBeed> $event;

  @Test
  public void constructor() {
    assertEquals($unionSetBeed.getOwner(), $owner);
    assertTrue($unionSetBeed.getSources().isEmpty());
    assertTrue($unionSetBeed.get().isEmpty());
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $unionSetBeed.fireChangeEventPublic($event);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event, $listener1.$event.getCause());
    assertEquals($event, $listener1.$event.getCause());
  }

  @Test
  public void addSource1() {
    assertEquals($unionSetBeed.getSources().size(), 0);
    assertEquals($unionSetBeed.get().size(), 0);
    $unionSetBeed.addSource($runA.wells);
    assertEquals($unionSetBeed.getSources().size(), 1);
    assertTrue($unionSetBeed.getSources().contains($runA.wells));
    assertEquals($unionSetBeed.get().size(), 3);
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    assertTrue($unionSetBeed.get().contains($wellA3));
    $unionSetBeed.addSource($runB.wells);
    assertEquals($unionSetBeed.getSources().size(), 2);
    assertTrue($unionSetBeed.getSources().contains($runA.wells));
    assertTrue($unionSetBeed.getSources().contains($runB.wells));
    assertEquals($unionSetBeed.get().size(), 5);
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    assertTrue($unionSetBeed.get().contains($wellA3));
    assertTrue($unionSetBeed.get().contains($wellB1));
    assertTrue($unionSetBeed.get().contains($wellB2));
    $unionSetBeed.addSource($runA.wells); // add the same again
    assertEquals($unionSetBeed.getSources().size(), 2);
    assertTrue($unionSetBeed.getSources().contains($runA.wells));
    assertTrue($unionSetBeed.getSources().contains($runB.wells));
    assertEquals($unionSetBeed.get().size(), 5);
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    assertTrue($unionSetBeed.get().contains($wellA3));
    assertTrue($unionSetBeed.get().contains($wellB1));
    assertTrue($unionSetBeed.get().contains($wellB2));
    $unionSetBeed.addSource($runC.wells);
    assertEquals($unionSetBeed.getSources().size(), 3);
    assertTrue($unionSetBeed.getSources().contains($runA.wells));
    assertTrue($unionSetBeed.getSources().contains($runB.wells));
    assertTrue($unionSetBeed.getSources().contains($runC.wells));
    assertEquals($unionSetBeed.get().size(), 9);
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    assertTrue($unionSetBeed.get().contains($wellA3));
    assertTrue($unionSetBeed.get().contains($wellB1));
    assertTrue($unionSetBeed.get().contains($wellB2));
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
  }

  /**
   * The union beed should be a listener of each source
   * @throws IllegalEditException
   * @throws EditStateException
   *
   */
  @Test
  public void addSource2() throws EditStateException, IllegalEditException {
    assertEquals($unionSetBeed.getSources().size(), 0);
    assertEquals($unionSetBeed.get().size(), 0);
    $unionSetBeed.addSource($runA.wells);
    assertEquals($unionSetBeed.getSources().size(), 1);
    assertTrue($unionSetBeed.getSources().contains($runA.wells));
    assertEquals($unionSetBeed.get().size(), 3);
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    assertTrue($unionSetBeed.get().contains($wellA3));
    // add listener to the union beed
    $unionSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // change the source
    WellBeanBeed well = new WellBeanBeed();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> runEdit =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>(well.run);
    runEdit.setGoal($runA.wells);
    runEdit.perform();
    // the listener should be notified
    Set<WellBeanBeed> added = new HashSet<WellBeanBeed>();
    added.add(well);
    Set<WellBeanBeed> removed = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), runEdit);
    assertEquals($unionSetBeed.getSources().size(), 1);
    assertTrue($unionSetBeed.getSources().contains($runA.wells));
    assertEquals($unionSetBeed.get().size(), 4);
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    assertTrue($unionSetBeed.get().contains($wellA3));
    assertTrue($unionSetBeed.get().contains(well));
  }

  @Test
  public void removeSource1() {
    $unionSetBeed.addSource($runA.wells);
    $unionSetBeed.addSource($runB.wells);
    $unionSetBeed.addSource($runC.wells);
    assertEquals($unionSetBeed.getSources().size(), 3);
    assertTrue($unionSetBeed.getSources().contains($runA.wells));
    assertTrue($unionSetBeed.getSources().contains($runB.wells));
    assertTrue($unionSetBeed.getSources().contains($runC.wells));
    assertEquals($unionSetBeed.get().size(), 9);
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    assertTrue($unionSetBeed.get().contains($wellA3));
    assertTrue($unionSetBeed.get().contains($wellB1));
    assertTrue($unionSetBeed.get().contains($wellB2));
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    $unionSetBeed.removeSource($runA.wells);
    assertEquals($unionSetBeed.getSources().size(), 2);
    assertTrue($unionSetBeed.getSources().contains($runB.wells));
    assertTrue($unionSetBeed.getSources().contains($runC.wells));
    assertEquals($unionSetBeed.get().size(), 6);
    assertTrue($unionSetBeed.get().contains($wellB1));
    assertTrue($unionSetBeed.get().contains($wellB2));
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    $unionSetBeed.removeSource($runB.wells);
    assertEquals($unionSetBeed.getSources().size(), 1);
    assertTrue($unionSetBeed.getSources().contains($runC.wells));
    assertEquals($unionSetBeed.get().size(), 4);
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    $unionSetBeed.removeSource($runA.wells); // remove source that is not in getSources
    assertEquals($unionSetBeed.getSources().size(), 1);
    assertTrue($unionSetBeed.getSources().contains($runC.wells));
    assertEquals($unionSetBeed.get().size(), 4);
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    $unionSetBeed.removeSource($runC.wells);
    assertEquals($unionSetBeed.getSources().size(), 0);
    assertEquals($unionSetBeed.get().size(), 0);
  }

  /**
   * The union beed should be removed as listener.
   * @throws IllegalEditException
   * @throws EditStateException
   */
  @Test
  public void removeSource2() throws EditStateException, IllegalEditException {
    $unionSetBeed.addSource($runA.wells);
    $unionSetBeed.addSource($runB.wells);
    $unionSetBeed.addSource($runC.wells);
    assertEquals($unionSetBeed.getSources().size(), 3);
    assertTrue($unionSetBeed.getSources().contains($runA.wells));
    assertTrue($unionSetBeed.getSources().contains($runB.wells));
    assertTrue($unionSetBeed.getSources().contains($runC.wells));
    assertEquals($unionSetBeed.get().size(), 9);
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    assertTrue($unionSetBeed.get().contains($wellA3));
    assertTrue($unionSetBeed.get().contains($wellB1));
    assertTrue($unionSetBeed.get().contains($wellB2));
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    $unionSetBeed.removeSource($runA.wells);
    assertEquals($unionSetBeed.getSources().size(), 2);
    assertTrue($unionSetBeed.getSources().contains($runB.wells));
    assertTrue($unionSetBeed.getSources().contains($runC.wells));
    assertEquals($unionSetBeed.get().size(), 6);
    assertTrue($unionSetBeed.get().contains($wellB1));
    assertTrue($unionSetBeed.get().contains($wellB2));
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    // add a listener to the union beed
    $unionSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // change source A
    WellBeanBeed well = new WellBeanBeed();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> runEdit =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>(well.run);
    runEdit.setGoal($runA.wells);
    runEdit.perform();
    // the listener should not be notified
    assertNull($listener3.$event);
    assertEquals($unionSetBeed.getSources().size(), 2);
    assertTrue($unionSetBeed.getSources().contains($runB.wells));
    assertTrue($unionSetBeed.getSources().contains($runC.wells));
    assertEquals($unionSetBeed.get().size(), 6);
    assertTrue($unionSetBeed.get().contains($wellB1));
    assertTrue($unionSetBeed.get().contains($wellB2));
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
  }

  @Test
  public void contains() {
    Set<SetBeed<WellBeanBeed, ?>> sources = new HashSet<SetBeed<WellBeanBeed, ?>>();
    assertFalse($unionSetBeed.contains(sources, $wellA1));
    assertFalse($unionSetBeed.contains(sources, $wellB1));
    assertFalse($unionSetBeed.contains(sources, $wellC1));
    sources.add($runA.wells);
    assertTrue($unionSetBeed.contains(sources, $wellA1));
    assertTrue($unionSetBeed.contains(sources, $wellA2));
    assertTrue($unionSetBeed.contains(sources, $wellA3));
    assertFalse($unionSetBeed.contains(sources, $wellB1));
    assertFalse($unionSetBeed.contains(sources, $wellC1));
    sources.add($runB.wells);
    assertTrue($unionSetBeed.contains(sources, $wellA1));
    assertTrue($unionSetBeed.contains(sources, $wellA2));
    assertTrue($unionSetBeed.contains(sources, $wellA3));
    assertTrue($unionSetBeed.contains(sources, $wellB1));
    assertTrue($unionSetBeed.contains(sources, $wellB2));
    assertFalse($unionSetBeed.contains(sources, $wellC1));
    sources.remove($runA.wells);
    assertFalse($unionSetBeed.contains(sources, $wellA1));
    assertTrue($unionSetBeed.contains(sources, $wellB1));
    assertTrue($unionSetBeed.contains(sources, $wellB2));
    assertFalse($unionSetBeed.contains(sources, $wellC1));
  }


  /**
   * All sets are disjunct.
   */
  @Test
  public void get1() {
    $unionSetBeed.addSource($runA.wells);
    $unionSetBeed.addSource($runB.wells);
    $unionSetBeed.addSource($runC.wells);
    Set<WellBeanBeed> result = $unionSetBeed.get();
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
    $unionSetBeed.removeSource($runA.wells);
    $unionSetBeed.removeSource($runB.wells);
    $unionSetBeed.removeSource($runC.wells);
    result = $unionSetBeed.get();
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
    $unionSetBeed.addSource(setBeed1);
    $unionSetBeed.addSource(setBeed2);
    Set<WellBeanBeed> result = $unionSetBeed.get();
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
    SetEvent<WellBeanBeed> initialEvent = $unionSetBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $unionSetBeed);
    assertEquals(initialEvent.getAddedElements(), $unionSetBeed.get());
    assertEquals(initialEvent.getRemovedElements(), new HashSet<WellBeanBeed>());
    assertEquals(initialEvent.getEdit(), null);
  }

  @Test
  public void testSetBeedListener() throws EditStateException, IllegalEditException {
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
    // add the sources to the union beed
    $unionSetBeed.addSource(setBeed1);
    $unionSetBeed.addSource(setBeed2);
    assertEquals($unionSetBeed.get().size(), 4);
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    // add a listener to the union beed
    $unionSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // add a beed to setBeed1 that is not in the union
    setEdit1 = new SetEdit<WellBeanBeed>(setBeed1);
    setEdit1.addElementToAdd($wellA1);
    setEdit1.perform();
    // check the listener and the value of the union beed
    Set<WellBeanBeed> added = new HashSet<WellBeanBeed>();
    added.add($wellA1);
    Set<WellBeanBeed> removed = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit1);
    assertEquals($unionSetBeed.get().size(), 5);
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    assertTrue($unionSetBeed.get().contains($wellA1));
    // add a beed to setBeed1 that is already in the union
    $listener3.reset();
    setEdit1 = new SetEdit<WellBeanBeed>(setBeed1);
    setEdit1.addElementToAdd($wellC4);
    setEdit1.perform();
    // check the listener and the value of the union beed
    assertNull($listener3.$event);
    assertEquals($unionSetBeed.get().size(), 5);
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    assertTrue($unionSetBeed.get().contains($wellA1));
    // add a beed to setBeed1 that is already in the union and another one
    // that is not in the union
    $listener3.reset();
    setEdit1 = new SetEdit<WellBeanBeed>(setBeed1);
    setEdit1.addElementToAdd($wellA2);
    setEdit1.perform();
    // check the listener and the value of the union beed
    added = new HashSet<WellBeanBeed>();
    added.add($wellA2);
    removed = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit1);
    assertEquals($unionSetBeed.get().size(), 6);
    assertTrue($unionSetBeed.get().contains($wellC1));
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    // add a beed to setBeed1 that is already in that beed
    $listener3.reset();
    setEdit1 = new SetEdit<WellBeanBeed>(setBeed1);
    setEdit1.addElementToAdd($wellC1);
    setEdit1.perform();
    // check the listener and the value of the union beed
    assertNull($listener3.$event);
    // remove a beed from setBeed1 that is not in the other source
    $listener3.reset();
    setEdit1 = new SetEdit<WellBeanBeed>(setBeed1);
    setEdit1.addElementToRemove($wellC1);
    setEdit1.perform();
    // check the listener and the value of the union beed
    added = new HashSet<WellBeanBeed>();
    removed = new HashSet<WellBeanBeed>();
    removed.add($wellC1);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit1);
    assertEquals($unionSetBeed.get().size(), 5);
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    // remove a beed from setBeed1 that is also in the other source
    $listener3.reset();
    setEdit1 = new SetEdit<WellBeanBeed>(setBeed1);
    setEdit1.addElementToRemove($wellC3);
    setEdit1.perform();
    // check the listener and the value of the union beed
    assertNull($listener3.$event);
    assertEquals($unionSetBeed.get().size(), 5);
    assertTrue($unionSetBeed.get().contains($wellC2));
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
    // remove a beed from setBeed1 that is also in the other source
    // and remove a beed that is not
    $listener3.reset();
    setEdit1 = new SetEdit<WellBeanBeed>(setBeed1);
    setEdit1.addElementToRemove($wellC3);
    setEdit1.addElementToRemove($wellC2);
    setEdit1.perform();
    // check the listener and the value of the union beed
    added = new HashSet<WellBeanBeed>();
    removed = new HashSet<WellBeanBeed>();
    removed.add($wellC2);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit1);
    assertEquals($unionSetBeed.get().size(), 4);
    assertTrue($unionSetBeed.get().contains($wellC3));
    assertTrue($unionSetBeed.get().contains($wellC4));
    assertTrue($unionSetBeed.get().contains($wellA1));
    assertTrue($unionSetBeed.get().contains($wellA2));
  }

  @Test
  public void computeSum() {
    $unionSetBeed.addSource($runA.wells);
    $unionSetBeed.addSource($runB.wells);
    $unionSetBeed.addSource($runC.wells);
    BeedMapping<WellBeanBeed, IntegerBeed> mapping =
      new BeedMapping<WellBeanBeed, IntegerBeed>() {

        /**
         * @pre  from != null;
         */
        public IntegerBeed map(WellBeanBeed from) {
          return from.cq;
        }

    };
    MappedSetBeed<WellBeanBeed, PropagatedEvent, IntegerBeed> mappedSetBeed =
      new MappedSetBeed<WellBeanBeed, PropagatedEvent, IntegerBeed>(mapping, $owner);
    mappedSetBeed.setSource($unionSetBeed);
    Integer sum = 0;
    for (IntegerBeed cq : mappedSetBeed.get()) {
      sum += cq.getInteger();
    }
    assertEquals(sum, 0+1+2+3+4+5+6+7+8);
  }

}