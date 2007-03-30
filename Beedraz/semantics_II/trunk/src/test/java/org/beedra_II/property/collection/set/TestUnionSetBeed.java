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
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
import org.beedra_II.property.number.integer.long64.EditableLongBeed;
import org.beedra_II.property.number.integer.long64.LongBeed;
import org.beedra_II.property.number.integer.long64.LongEdit;
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
    public void publicUpdateDependents(SetEvent<WellBeanBeed> event) {
      updateDependents(event);
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
    public final EditableLongBeed cq =
      new EditableLongBeed(this);

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
    $cqA1 = new Long(0);
    $cqA2 = new Long(1);
    $cqA3 = new Long(2);
    $cqB1 = new Long(3);
    $cqB2 = new Long(4);
    $cqC1 = new Long(5);
    $cqC2 = new Long(6);
    $cqC3 = new Long(7);
    $cqC4 = new Long(8);
    $listener1 = new StubListener<PropagatedEvent>();
    $listener2 = new StubListener<PropagatedEvent>();
    $listener3 = new StubListener<SetEvent<WellBeanBeed>>();
    $listener5 = new StubListener<ActualLongEvent>();
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
    LongEdit longEdit = new LongEdit($wellA1.cq);
    longEdit.setGoal($cqA1);
    longEdit.perform();
    longEdit = new LongEdit($wellA2.cq);
    longEdit.setGoal($cqA2);
    longEdit.perform();
    longEdit = new LongEdit($wellA3.cq);
    longEdit.setGoal($cqA3);
    longEdit.perform();
    longEdit = new LongEdit($wellB1.cq);
    longEdit.setGoal($cqB1);
    longEdit.perform();
    longEdit = new LongEdit($wellB2.cq);
    longEdit.setGoal($cqB2);
    longEdit.perform();
    longEdit = new LongEdit($wellC1.cq);
    longEdit.setGoal($cqC1);
    longEdit.perform();
    longEdit = new LongEdit($wellC2.cq);
    longEdit.setGoal($cqC2);
    longEdit.perform();
    longEdit = new LongEdit($wellC3.cq);
    longEdit.setGoal($cqC3);
    longEdit.perform();
    longEdit = new LongEdit($wellC4.cq);
    longEdit.setGoal($cqC4);
    longEdit.perform();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private RunBeanBeed $runA;
  private WellBeanBeed $wellA1;
  private WellBeanBeed $wellA2;
  private WellBeanBeed $wellA3;
  private Long $cqA1;
  private Long $cqA2;
  private Long $cqA3;
  private RunBeanBeed $runB;
  private WellBeanBeed $wellB1;
  private WellBeanBeed $wellB2;
  private Long $cqB1;
  private Long $cqB2;
  private RunBeanBeed $runC;
  private WellBeanBeed $wellC1;
  private WellBeanBeed $wellC2;
  private WellBeanBeed $wellC3;
  private WellBeanBeed $wellC4;
  private Long $cqC1;
  private Long $cqC2;
  private Long $cqC3;
  private Long $cqC4;
  private MyUnionBeed $unionSetBeed;
  private MyBeanBeed $owner;
  private StubListener<PropagatedEvent> $listener1;
  private StubListener<PropagatedEvent> $listener2;
  private StubListener<SetEvent<WellBeanBeed>> $listener3;
  private StubListener<ActualLongEvent> $listener5;
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
    $unionSetBeed.publicUpdateDependents($event);
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
    assertEquals(0, $unionSetBeed.getSources().size());
    assertEquals(0, $unionSetBeed.get().size());
    $unionSetBeed.addSource($runA.wells);
    assertEquals(1, $unionSetBeed.getSources().size());
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
    assertFalse(UnionSetBeed.contains(sources, $wellA1));
    assertFalse(UnionSetBeed.contains(sources, $wellB1));
    assertFalse(UnionSetBeed.contains(sources, $wellC1));
    sources.add($runA.wells);
    assertTrue(UnionSetBeed.contains(sources, $wellA1));
    assertTrue(UnionSetBeed.contains(sources, $wellA2));
    assertTrue(UnionSetBeed.contains(sources, $wellA3));
    assertFalse(UnionSetBeed.contains(sources, $wellB1));
    assertFalse(UnionSetBeed.contains(sources, $wellC1));
    sources.add($runB.wells);
    assertTrue(UnionSetBeed.contains(sources, $wellA1));
    assertTrue(UnionSetBeed.contains(sources, $wellA2));
    assertTrue(UnionSetBeed.contains(sources, $wellA3));
    assertTrue(UnionSetBeed.contains(sources, $wellB1));
    assertTrue(UnionSetBeed.contains(sources, $wellB2));
    assertFalse(UnionSetBeed.contains(sources, $wellC1));
    sources.remove($runA.wells);
    assertFalse(UnionSetBeed.contains(sources, $wellA1));
    assertTrue(UnionSetBeed.contains(sources, $wellB1));
    assertTrue(UnionSetBeed.contains(sources, $wellB2));
    assertFalse(UnionSetBeed.contains(sources, $wellC1));
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
  public void getSizeAndCardinality() throws EditStateException, IllegalEditException {
    // add a listener to the size beed
    IntegerBeed<ActualLongEvent> sizeBeed = $unionSetBeed.getSize();
    sizeBeed.addListener($listener5);
    assertNull($listener5.$event);
    // check the size (empty)
    assertEquals($unionSetBeed.getSize().getLong(), 0L);
    assertEquals($unionSetBeed.getCardinality().getLong(), 0L);
    // add source
    EditableSetBeed<WellBeanBeed> sourceA = createSourceA();
    $unionSetBeed.addSource(sourceA);
    // check the size (wellA1, wellA2)
    assertEquals($unionSetBeed.getSize().getLong(), 3L);
    assertEquals($unionSetBeed.getCardinality().getLong(), 3L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 0L);
    assertEquals($listener5.$event.getNewLong(), 3L);
    assertEquals($listener5.$event.getEdit(), null);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add extra source
    EditableSetBeed<WellBeanBeed> sourceB = createSourceB();
    $unionSetBeed.addSource(sourceB);
    // check the size (wellA1, wellA2, wellB1, wellB2, wellB3)
    assertEquals($unionSetBeed.getSize().getLong(), 5L);
    assertEquals($unionSetBeed.getCardinality().getLong(), 5L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 3L);
    assertEquals($listener5.$event.getNewLong(), 5L);
    assertEquals($listener5.$event.getEdit(), null);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add elements
    WellBeanBeed well = new WellBeanBeed();
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(sourceA);
    setEdit.addElementToAdd(well);
    setEdit.perform();
    // check the size (wellA1, wellA2, wellB1, wellB2, wellB3, well)
    assertEquals($unionSetBeed.getSize().getLong(), 6L);
    assertEquals($unionSetBeed.getCardinality().getLong(), 6L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 5L);
    assertEquals($listener5.$event.getNewLong(), 6L);
    assertEquals($listener5.$event.getEdit(), setEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    setEdit = new SetEdit<WellBeanBeed>(sourceB);
    setEdit.addElementToRemove($wellB1);
    setEdit.perform();
    // check the size (wellA1, wellA2, wellB2, wellB3, well)
    assertEquals($unionSetBeed.getSize().getLong(), 5L);
    assertEquals($unionSetBeed.getCardinality().getLong(), 5L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 6L);
    assertEquals($listener5.$event.getNewLong(), 5L);
    assertEquals($listener5.$event.getEdit(), setEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add element that is already in another source
    setEdit = new SetEdit<WellBeanBeed>(sourceA);
    setEdit.addElementToAdd($wellB2);
    setEdit.perform();
    // check the size (wellA1, wellA2, wellB1, wellB2, wellB3, well)
    assertEquals($unionSetBeed.getSize().getLong(), 5L);
    assertEquals($unionSetBeed.getCardinality().getLong(), 5L);
    // check the listener (should not be notified since the size has not changed)
    assertNull($listener5.$event);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove element that is also in another source
    setEdit = new SetEdit<WellBeanBeed>(sourceB);
    setEdit.addElementToRemove($wellB2);
    setEdit.perform();
    // check the size (wellA1, wellA2, wellB1, wellB2, wellB3, well)
    assertEquals($unionSetBeed.getSize().getLong(), 5L);
    assertEquals($unionSetBeed.getCardinality().getLong(), 5L);
    // check the listener (should not be notified since the size has not changed)
    assertNull($listener5.$event);
  }

  @Test
  public void computeSum() {
    $unionSetBeed.addSource($runA.wells);
    $unionSetBeed.addSource($runB.wells);
    $unionSetBeed.addSource($runC.wells);
    BeedMapping<WellBeanBeed, LongBeed> mapping =
      new BeedMapping<WellBeanBeed, LongBeed>() {

        /**
         * @pre  from != null;
         */
        public LongBeed map(WellBeanBeed from) {
          return from.cq;
        }

    };
    MappedSetBeed<WellBeanBeed, PropagatedEvent, LongBeed> mappedSetBeed =
      new MappedSetBeed<WellBeanBeed, PropagatedEvent, LongBeed>(mapping, $owner);
    mappedSetBeed.setSource($unionSetBeed);
    Long sum = 0L;
    for (LongBeed cq : mappedSetBeed.get()) {
      sum += cq.getLong();
    }
    assertEquals(sum, 0L + 1L + 2L + 3L + 4L + 5L + 6L + 7L + 8L);
  }

  private EditableSetBeed<WellBeanBeed> createSourceA() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<WellBeanBeed> setBeed =
      new EditableSetBeed<WellBeanBeed>($owner);
    // add beeds to set
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($wellA1);
    setEdit.perform();
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($wellA2);
    setEdit.perform();
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($wellA3);
    setEdit.perform();
    return setBeed;
  }

  private EditableSetBeed<WellBeanBeed> createSourceB() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<WellBeanBeed> setBeed =
      new EditableSetBeed<WellBeanBeed>($owner);
    // add beeds to set
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($wellB1);
    setEdit.perform();
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($wellB2);
    setEdit.perform();
    return setBeed;
  }
}