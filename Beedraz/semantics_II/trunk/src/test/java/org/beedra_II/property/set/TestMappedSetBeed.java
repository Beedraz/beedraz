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

public class TestMappedSetBeed {


  public class MyMappedSetBeed extends MappedSetBeed<WellBeanBeed, PropagatedEvent, IntegerBeed> {

    public MyMappedSetBeed(Mapping<WellBeanBeed, IntegerBeed> mapping, AggregateBeed owner) {
      super(mapping, owner);
    }

    /**
     * Made public for testing reasons.
     */
    public void fireChangeEventPublic(SetEvent<IntegerBeed> event) {
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
    $mapping = new Mapping<WellBeanBeed, IntegerBeed>() {
        public IntegerBeed map(WellBeanBeed from) {
          return from.cq;
        }
    };
    $mappedSetBeed = new MyMappedSetBeed($mapping, $owner);
    $run = new RunBeanBeed();
    $well1 = new WellBeanBeed();
    $well2 = new WellBeanBeed();
    $well3 = new WellBeanBeed();
    $cq1 = new Integer(1);
    $cq2 = new Integer(2);
    $cq3 = new Integer(3);
    $listener1 = new StubListener<PropagatedEvent>();
    $listener2 = new StubListener<PropagatedEvent>();
    $listener3 = new StubListener<SetEvent<IntegerBeed>>();
    $listener4 = new StubListener<SetEvent<Integer>>();
    $event = new SetEvent<IntegerBeed>($mappedSetBeed, null, null, null);
    // add the wells to the run
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit1 =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well1.run);
    edit1.setGoal($run.wells);
    edit1.perform();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit2 =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well2.run);
    edit2.setGoal($run.wells);
    edit2.perform();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit3 =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well3.run);
    edit3.setGoal($run.wells);
    edit3.perform();
    // initialise the cq values
    IntegerEdit integerEdit1 = new IntegerEdit($well1.cq);
    integerEdit1.setGoal($cq1);
    integerEdit1.perform();
    IntegerEdit integerEdit2 = new IntegerEdit($well2.cq);
    integerEdit2.setGoal($cq2);
    integerEdit2.perform();
    IntegerEdit integerEdit3 = new IntegerEdit($well3.cq);
    integerEdit3.setGoal($cq3);
    integerEdit3.perform();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private RunBeanBeed $run;
  private WellBeanBeed $well1;
  private WellBeanBeed $well2;
  private WellBeanBeed $well3;
  private Integer $cq1;
  private Integer $cq2;
  private Integer $cq3;
  private Mapping<WellBeanBeed, IntegerBeed> $mapping;
  private MyMappedSetBeed $mappedSetBeed;
  private MyBeanBeed $owner;
  private StubListener<PropagatedEvent> $listener1;
  private StubListener<PropagatedEvent> $listener2;
  private StubListener<SetEvent<IntegerBeed>> $listener3;
  private StubListener<SetEvent<Integer>> $listener4;
  private SetEvent<IntegerBeed> $event;

  @Test
  public void constructor() {
    assertEquals($mappedSetBeed.getOwner(), $owner);
    assertEquals($mappedSetBeed.getMapping(), $mapping);
    assertNull($mappedSetBeed.getSource());
    assertNotNull($mappedSetBeed.get());
    assertTrue($mappedSetBeed.get().isEmpty());
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $mappedSetBeed.fireChangeEventPublic($event);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event, $listener1.$event.getCause());
    assertEquals($event, $listener1.$event.getCause());
  }

  /**
   * Source is null.
   */
  @Test
  public void setSource1() throws EditStateException, IllegalEditException {
    // register listeners to the MappedSetBeed
    $mappedSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    SetBeed<WellBeanBeed> source = null;
    $mappedSetBeed.setSource(source);
    assertEquals($mappedSetBeed.getSource(), source);
    assertTrue($mappedSetBeed.get().isEmpty());
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Source is effective.
   */
  @Test
  public void setSource2() throws EditStateException, IllegalEditException {
    // register listeners to the MappedSetBeed
    $mappedSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    EditableSetBeed<WellBeanBeed> source = createSource();
    $mappedSetBeed.setSource(source);
    assertEquals($mappedSetBeed.getSource(), source);
    assertEquals($mappedSetBeed.get().size(), 3);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    assertTrue($mappedSetBeed.get().contains($well3.cq));
    // value has changed, so the listeners of the mean beed are notified
    Set<IntegerBeed> added = new HashSet<IntegerBeed>();
    added.add($well1.cq);
    added.add($well2.cq);
    added.add($well3.cq);
    Set<IntegerBeed> removed = new HashSet<IntegerBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $mappedSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), null);
    // The MappedSetBeed is registered as listener of the source, so when
    // the source changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(source);
    WellBeanBeed goal = createWellBeanBeed(5);
    setEdit.addElementToAdd(goal);
    setEdit.perform();
    removed = new HashSet<IntegerBeed>();
    added = new HashSet<IntegerBeed>();
    added.add(goal.cq);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $mappedSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit);
    assertEquals($mappedSetBeed.get().size(), 4);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    assertTrue($mappedSetBeed.get().contains($well3.cq));
    assertTrue($mappedSetBeed.get().contains(goal.cq));
    // The MappedSetBeed is registered as listener of all beeds in the source,
    // so when one of them changes, the beed should be notified
    // This cannot be tested with this mapping, because the cq beed cannot be changed.
    // See setSource3

    // When a new beed is added to the source, the DoubleMeanBeed is added as a listener
    // of that beed. See setSource3.

    // When a beed is removed from the source, the MappedSetBeed is removed as listener
    // of that beed.
    $listener3.reset();
    assertNull($listener3.$event);
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove(goal);
    setEdit.perform();
    removed = new HashSet<IntegerBeed>();
    removed.add(goal.cq);
    added = new HashSet<IntegerBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $mappedSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit);
    $listener3.reset();
    assertNull($listener3.$event);
    // so, when the removed beed is edited, the mapped set beed is NOT notified
    IntegerEdit integerEdit = new IntegerEdit(goal.cq);
    integerEdit.setGoal(7);
    integerEdit.perform();
    assertNull($listener3.$event); // the DoubleMeanBeed is NOT notified
    // and the value of the mapped set beed is correct
    Set<IntegerBeed> result = new HashSet<IntegerBeed>();
    result.add($well1.cq);
    result.add($well2.cq);
    result.add($well3.cq);
    assertEquals($mappedSetBeed.get(), result);
  }

  /**
   * We use another mapping so that we can change the result set by
   * changing one of the well bean beeds.
   */
  @Test
  public void setSource3() throws EditStateException, IllegalEditException {
    // define a new mapping
    // @remark this mapping is only injective for certain source sets!
    Mapping<WellBeanBeed, Integer> mapping = new Mapping<WellBeanBeed, Integer>() {
      public Integer map(WellBeanBeed from) {
        return from.cq.get();
      }
    };
    // define a new mapped set beed
    MappedSetBeed<WellBeanBeed, PropagatedEvent, Integer> mappedSetBeed =
      new MappedSetBeed<WellBeanBeed, PropagatedEvent, Integer>(mapping, $owner);
    // register listeners to the MappedSetBeed
    mappedSetBeed.addListener($listener4);
    assertNull($listener4.$event);
    // set the source
    EditableSetBeed<WellBeanBeed> source = createSource();
    mappedSetBeed.setSource(source);
    assertEquals(mappedSetBeed.getSource(), source);
    assertEquals(mappedSetBeed.get().size(), 3);
    assertTrue(mappedSetBeed.get().contains($well1.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well2.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well3.cq.get()));
    // add an extra beed to the source
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(source);
    WellBeanBeed goal = createWellBeanBeed(5);
    setEdit.addElementToAdd(goal);
    setEdit.perform();
    assertEquals(mappedSetBeed.get().size(), 4);
    assertTrue(mappedSetBeed.get().contains($well1.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well2.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well3.cq.get()));
    assertTrue(mappedSetBeed.get().contains(goal.cq.get()));
    // The MappedSetBeed is registered as listener of all beeds in the source,
    // so when one of them changes, the beed should be notified
    $listener4.reset();
    assertNull($listener4.$event);
    IntegerEdit integerEdit = new IntegerEdit(goal.cq);
    integerEdit.setGoal(6);
    integerEdit.perform();
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getSource(), mappedSetBeed);
    assertEquals($listener4.$event.getAddedElements(), new HashSet<Integer>());
    assertEquals($listener4.$event.getRemovedElements(), new HashSet<Integer>());
    assertEquals($listener4.$event.getEdit(), integerEdit);
    assertEquals(mappedSetBeed.get().size(), 4);
    assertTrue(mappedSetBeed.get().contains($well1.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well2.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well3.cq.get()));
    assertTrue(mappedSetBeed.get().contains(goal.cq.get()));
    // When a new beed is added to the source, the DoubleMeanBeed is added as a listener
    // of that beed. See above.
  }

  @Test
  public void get() {
    SetBeed<WellBeanBeed> source = $run.wells;
    $mappedSetBeed.setSource(source);
    Set<IntegerBeed> result = $mappedSetBeed.get();
    assertEquals(result.size(), source.get().size());
    assertTrue(result.contains($well1.cq));
    assertTrue(result.contains($well2.cq));
    assertTrue(result.contains($well3.cq));
    Iterator<IntegerBeed> iteratorCq = result.iterator();
    assertTrue(iteratorCq.hasNext());
    IntegerBeed next = iteratorCq.next();
    assertTrue(next == $well1.cq || next == $well2.cq || next == $well3.cq);
    assertTrue(iteratorCq.hasNext());
    next = iteratorCq.next();
    assertTrue(next == $well1.cq || next == $well2.cq || next == $well3.cq);
    assertTrue(iteratorCq.hasNext());
    next = iteratorCq.next();
    assertTrue(next == $well1.cq || next == $well2.cq || next == $well3.cq);
    assertFalse(iteratorCq.hasNext());
    try {
      iteratorCq.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
    // source = null
    source = null;
    $mappedSetBeed.setSource(source);
    result = $mappedSetBeed.get();
    assertEquals(result.size(), 0);
    iteratorCq = result.iterator();
    assertFalse(iteratorCq.hasNext());
    try {
      iteratorCq.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
  }


  @Test
  public void recalculate() throws EditStateException, IllegalEditException {
    // double mean beed has no source
    $mappedSetBeed.recalculate();
    assertTrue($mappedSetBeed.get().isEmpty());
    // create source
    EditableSetBeed<WellBeanBeed> source =
      new EditableSetBeed<WellBeanBeed>($owner);
    // add source to mean beed
    $mappedSetBeed.setSource(source);
    // recalculate (setBeed contains no elements)
    $mappedSetBeed.recalculate();
    assertTrue($mappedSetBeed.get().isEmpty());
    // add beed
    SetEdit<WellBeanBeed> setEdit =
      new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well1);
    setEdit.perform();
    // recalculate (setBeed contains beed 1)
    $mappedSetBeed.recalculate();
    assertEquals($mappedSetBeed.get().size(), 1);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    // recalculate (setBeed contains beed 1)
    $mappedSetBeed.recalculate();
    assertEquals($mappedSetBeed.get().size(), 1);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    // add beed
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well2);
    setEdit.perform();
    // recalculate (setBeed contains beed 1 and 2)
    $mappedSetBeed.recalculate();
    assertEquals($mappedSetBeed.get().size(), 2);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    // recalculate (setBeed contains beed 1 and 2)
    $mappedSetBeed.recalculate();
    assertEquals($mappedSetBeed.get().size(), 2);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    // add beed
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well3);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2 and 3)
    $mappedSetBeed.recalculate();
    assertEquals($mappedSetBeed.get().size(), 3);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    assertTrue($mappedSetBeed.get().contains($well3.cq));
  }

  @Test
  public void createInitialEvent() {
    SetEvent<IntegerBeed> initialEvent = $mappedSetBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $mappedSetBeed);
    assertEquals(initialEvent.getAddedElements(), $mappedSetBeed.get());
    assertEquals(initialEvent.getRemovedElements(), new HashSet<IntegerBeed>());
    assertEquals(initialEvent.getEdit(), null);
  }

  @Test
  public void computeSum() {
    // test1
    $mappedSetBeed.setSource($run.wells);
    Integer sum = 0;
    for (IntegerBeed cq : $mappedSetBeed.get()) {
      sum += cq.getInteger();
    }
    assertEquals(sum, 6);
    // test2
    $mappedSetBeed.setSource(null);
    sum = 0;
    for (IntegerBeed cq : $mappedSetBeed.get()) {
      sum += cq.getInteger();
    }
    assertEquals(sum, 0);
  }

  @Test
  public void test() throws EditStateException, IllegalEditException {
    $mappedSetBeed.setSource($run.wells);
    assertEquals($mappedSetBeed.get().size(), 3);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    assertTrue($mappedSetBeed.get().contains($well3.cq));
    // add extra well to $run
    WellBeanBeed well = new WellBeanBeed();
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> runEdit =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>(well.run);
    runEdit.setGoal($run.wells);
    runEdit.perform();
    assertEquals($mappedSetBeed.get().size(), 4);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    assertTrue($mappedSetBeed.get().contains($well3.cq));
    assertTrue($mappedSetBeed.get().contains(well.cq));
    // remove well from $run
    runEdit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well1.run);
    runEdit.setGoal(null);
    runEdit.perform();
    assertEquals($mappedSetBeed.get().size(), 3);
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    assertTrue($mappedSetBeed.get().contains($well3.cq));
    assertTrue($mappedSetBeed.get().contains(well.cq));
    // change the source
    $mappedSetBeed.setSource(null);
    assertEquals($mappedSetBeed.get().size(), 0);
    // change the source
    $mappedSetBeed.setSource(new EditableSetBeed<WellBeanBeed>($owner));
    assertEquals($mappedSetBeed.get().size(), 0);
  }

  private EditableSetBeed<WellBeanBeed> createSource() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<WellBeanBeed> setBeed =
      new EditableSetBeed<WellBeanBeed>($owner);
    // add beeds to set
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($well1);
    setEdit.perform();
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($well2);
    setEdit.perform();
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($well3);
    setEdit.perform();
    return setBeed;
  }

  private WellBeanBeed createWellBeanBeed(Integer cq) {
    try {
      WellBeanBeed wellBeanBeed = new WellBeanBeed();
      IntegerEdit edit = new IntegerEdit(wellBeanBeed.cq);
      edit.setGoal(cq);
      edit.perform();
      assertEquals(wellBeanBeed.cq.get(), cq);
      return wellBeanBeed;
    }
    catch (EditStateException e) {
      assertTrue(false);
      return null;
    }
    catch (IllegalEditException e) {
      assertTrue(false);
      return null;
    }
  }

}