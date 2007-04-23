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

package org.beedraz.semantics_II.expression.collection.set;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.beedraz.semantics_II.BeedMapping;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateEvent;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.bean.RunBeanBeed;
import org.beedraz.semantics_II.bean.WellBeanBeed;
import org.beedraz.semantics_II.edit.EditStateException;
import org.beedraz.semantics_II.edit.IllegalEditException;
import org.beedraz.semantics_II.expression.association.set.BidirToOneEdit;
import org.beedraz.semantics_II.expression.collection.set.EditableSetBeed;
import org.beedraz.semantics_II.expression.collection.set.MappedSetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEdit;
import org.beedraz.semantics_II.expression.collection.set.SetEvent;
import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.ActualLongEvent;
import org.beedraz.semantics_II.expression.number.integer.long64.LongBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.LongEdit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMappedSetBeed {


  public class MyMappedSetBeed extends MappedSetBeed<WellBeanBeed, AggregateEvent, LongBeed> {

    public MyMappedSetBeed(BeedMapping<WellBeanBeed, LongBeed> mapping) {
      super(mapping);
    }

    /**
     * Made public for testing reasons.
     */
    public void publicUpdateDependents(SetEvent<LongBeed> event) {
      updateDependents(event);
    }

  }


  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    $mapping = new BeedMapping<WellBeanBeed, LongBeed>() {
        public LongBeed map(WellBeanBeed from) {
          return from.cq;
        }
        public boolean dependsOnBeed() {
          return true;
        }
    };
    $mappedSetBeed = new MyMappedSetBeed($mapping);
    $run = new RunBeanBeed();
    $well1 = new WellBeanBeed();
    $well2 = new WellBeanBeed();
    $well3 = new WellBeanBeed();
    $cq1 = new Long(1);
    $cq2 = new Long(2);
    $cq3 = new Long(3);
    $listener3 = new StubListener<SetEvent<LongBeed>>();
    $listener4 = new StubListener<SetEvent<Long>>();
    $listener5 = new StubListener<ActualLongEvent>();
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
    LongEdit integerEdit1 = new LongEdit($well1.cq);
    integerEdit1.setGoal($cq1);
    integerEdit1.perform();
    LongEdit integerEdit2 = new LongEdit($well2.cq);
    integerEdit2.setGoal($cq2);
    integerEdit2.perform();
    LongEdit integerEdit3 = new LongEdit($well3.cq);
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
  private Long $cq1;
  private Long $cq2;
  private Long $cq3;
  private BeedMapping<WellBeanBeed, LongBeed> $mapping;
  private MyMappedSetBeed $mappedSetBeed;
  private StubListener<SetEvent<LongBeed>> $listener3;
  private StubListener<SetEvent<Long>> $listener4;
  private StubListener<ActualLongEvent> $listener5;

  @Test
  public void constructor() {
    assertEquals($mappedSetBeed.getMapping(), $mapping);
    assertNull($mappedSetBeed.getSource());
    assertNotNull($mappedSetBeed.get());
    assertTrue($mappedSetBeed.get().isEmpty());
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
    SetBeed<WellBeanBeed, ?> source = null;
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
    Set<LongBeed> added = new HashSet<LongBeed>();
    added.add($well1.cq);
    added.add($well2.cq);
    added.add($well3.cq);
    Set<LongBeed> removed = new HashSet<LongBeed>();
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
    WellBeanBeed goal = createWellBeanBeed(5L);
    setEdit.addElementToAdd(goal);
    setEdit.perform();
    removed = new HashSet<LongBeed>();
    added = new HashSet<LongBeed>();
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

    // When a new beed is added to the source, the MappedSetBeed is added as a listener
    // of that beed. See setSource3.

    // When a beed is removed from the source, the MappedSetBeed is removed as listener
    // of that beed.
    $listener3.reset();
    assertNull($listener3.$event);
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove(goal);
    setEdit.perform();
    removed = new HashSet<LongBeed>();
    removed.add(goal.cq);
    added = new HashSet<LongBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $mappedSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit);
    $listener3.reset();
    assertNull($listener3.$event);
    // so, when the removed beed is edited, the mapped set beed is NOT notified
    LongEdit longEdit = new LongEdit(goal.cq);
    longEdit.setGoal(7L);
    longEdit.perform();
    assertNull($listener3.$event); // the MappedSetBeed is NOT notified
    // and the value of the mapped set beed is correct
    Set<LongBeed> result = new HashSet<LongBeed>();
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
    BeedMapping<WellBeanBeed, Long> mapping = new BeedMapping<WellBeanBeed, Long>() {
      public Long map(WellBeanBeed from) {
        return from.cq.get();
      }
      public boolean dependsOnBeed() {
        return true;
      }
    };
    // define a new mapped set beed
    MappedSetBeed<WellBeanBeed, AggregateEvent, Long> mappedSetBeed =
      new MappedSetBeed<WellBeanBeed, AggregateEvent, Long>(mapping);
    // register listeners to the MappedSetBeed
    mappedSetBeed.addListener($listener4);
    assertNull($listener4.$event);

    // set the source
    $listener4.reset();
    EditableSetBeed<WellBeanBeed> source = createSource();
    mappedSetBeed.setSource(source);
    assertEquals(source, mappedSetBeed.getSource());
    assertEquals(3, mappedSetBeed.get().size());
    assertTrue(mappedSetBeed.get().contains($well1.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well2.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well3.cq.get()));
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getSource(), mappedSetBeed);
    assertEquals(3, $listener4.$event.getAddedElements().size());
    assertTrue($listener4.$event.getAddedElements().contains($well1.cq.get()));
    assertTrue($listener4.$event.getAddedElements().contains($well2.cq.get()));
    assertTrue($listener4.$event.getAddedElements().contains($well3.cq.get()));
    assertTrue($listener4.$event.getRemovedElements().isEmpty());
    assertNull($listener4.$event.getEdit());

    // add an extra beed to the source
    $listener4.reset();
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(source);
    WellBeanBeed goal = createWellBeanBeed(5L);
    setEdit.addElementToAdd(goal);
    setEdit.perform();
    assertEquals(4, mappedSetBeed.get().size());
    assertTrue(mappedSetBeed.get().contains($well1.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well2.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well3.cq.get()));
    assertTrue(mappedSetBeed.get().contains(goal.cq.get()));
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getSource(), mappedSetBeed);
    assertEquals(1, $listener4.$event.getAddedElements().size());
    assertTrue($listener4.$event.getAddedElements().contains(goal.cq.get()));
    assertTrue($listener4.$event.getRemovedElements().isEmpty());
    assertEquals(setEdit, $listener4.$event.getEdit());

    // The MappedSetBeed is registered as listener of all beeds in the source,
    // so when one of them changes, the beed should be notified
    $listener4.reset();
    assertNull($listener4.$event);
    LongEdit longEdit = new LongEdit(goal.cq);
    longEdit.setGoal(6L);
    longEdit.perform();
    // YES, BUT THERE ARE NO EVENTS, BECAUSE NOTHING IS CHANGED
//    assertNotNull($listener4.$event);
//    assertEquals($listener4.$event.getSource(), mappedSetBeed);
//    assertEquals(Collections.EMPTY_SET, $listener4.$event.getAddedElements());
//    assertEquals(Collections.EMPTY_SET, $listener4.$event.getRemovedElements());
//    assertEquals(longEdit, $listener4.$event.getEdit());
    assertEquals(4, mappedSetBeed.get().size());
    assertTrue(mappedSetBeed.get().contains($well1.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well2.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well3.cq.get()));
    assertTrue(mappedSetBeed.get().contains(goal.cq.get()));
    // When a new beed is added to the source, the MappedSetBeed is added as a listener
    // of that beed. See above.

    // remove a extra beed from the source
    $listener4.reset();
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove($well1);
    setEdit.perform();
    assertEquals(3, mappedSetBeed.get().size());
    assertTrue(mappedSetBeed.get().contains($well2.cq.get()));
    assertTrue(mappedSetBeed.get().contains($well3.cq.get()));
    assertTrue(mappedSetBeed.get().contains(goal.cq.get()));
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getSource(), mappedSetBeed);
    assertTrue($listener4.$event.getAddedElements().isEmpty());
    assertEquals(1, $listener4.$event.getRemovedElements().size());
    assertTrue($listener4.$event.getRemovedElements().contains($well1.cq.get()));
    assertEquals(setEdit, $listener4.$event.getEdit());

    // mix
    $listener4.reset();
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well1);
    setEdit.addElementToRemove($well2);
    setEdit.addElementToRemove($well3);
    setEdit.perform();
    assertEquals(2, mappedSetBeed.get().size());
    assertTrue(mappedSetBeed.get().contains($well1.cq.get()));
    assertTrue(mappedSetBeed.get().contains(goal.cq.get()));
    assertNotNull($listener4.$event);
    assertEquals($listener4.$event.getSource(), mappedSetBeed);
    assertEquals(1, $listener4.$event.getAddedElements().size());
    assertTrue($listener4.$event.getAddedElements().contains($well1.cq.get()));
    assertEquals(2, $listener4.$event.getRemovedElements().size());
    assertTrue($listener4.$event.getRemovedElements().contains($well2.cq.get()));
    assertTrue($listener4.$event.getRemovedElements().contains($well3.cq.get()));
    assertEquals(setEdit, $listener4.$event.getEdit());
  }

  @Test
  public void get() {
    SetBeed<WellBeanBeed, ?> source = $run.wells;
    $mappedSetBeed.setSource(source);
    Set<LongBeed> result = $mappedSetBeed.get();
    assertEquals(result.size(), source.get().size());
    assertTrue(result.contains($well1.cq));
    assertTrue(result.contains($well2.cq));
    assertTrue(result.contains($well3.cq));
    Iterator<LongBeed> iteratorCq = result.iterator();
    assertTrue(iteratorCq.hasNext());
    LongBeed next = iteratorCq.next();
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
    assertTrue($mappedSetBeed.get().isEmpty());
    // create source
    EditableSetBeed<WellBeanBeed> source =
      new EditableSetBeed<WellBeanBeed>();
    // add source to mean beed
    $mappedSetBeed.setSource(source);
    // recalculate (setBeed contains no elements)
    assertTrue($mappedSetBeed.get().isEmpty());
    // add beed
    SetEdit<WellBeanBeed> setEdit =
      new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well1);
    setEdit.perform();
    // recalculate (setBeed contains beed 1)
    assertEquals($mappedSetBeed.get().size(), 1);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    // recalculate (setBeed contains beed 1)
    assertEquals($mappedSetBeed.get().size(), 1);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    // add beed
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well2);
    setEdit.perform();
    // recalculate (setBeed contains beed 1 and 2)
    assertEquals($mappedSetBeed.get().size(), 2);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    // recalculate (setBeed contains beed 1 and 2)
    assertEquals($mappedSetBeed.get().size(), 2);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    // add beed
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well3);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2 and 3)
    assertEquals($mappedSetBeed.get().size(), 3);
    assertTrue($mappedSetBeed.get().contains($well1.cq));
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    assertTrue($mappedSetBeed.get().contains($well3.cq));
  }

  @Test
  public void computeSum() {
    // test1
    $mappedSetBeed.setSource($run.wells);
    Long sum = 0L;
    for (LongBeed cq : $mappedSetBeed.get()) {
      sum += cq.getLong();
    }
    assertEquals(sum, 6L);
    // test2
    $mappedSetBeed.setSource(null);
    sum = 0L;
    for (LongBeed cq : $mappedSetBeed.get()) {
      sum += cq.getLong();
    }
    assertEquals(sum, 0L);
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
    assertTrue($run.wells.get().contains($well1));
    runEdit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well1.run);
    runEdit.setGoal(null);
    runEdit.perform();
    assertFalse($run.wells.get().contains($well1));
    assertEquals($mappedSetBeed.get().size(), 3);
    assertTrue($mappedSetBeed.get().contains($well2.cq));
    assertTrue($mappedSetBeed.get().contains($well3.cq));
    assertTrue($mappedSetBeed.get().contains(well.cq));
    // change the source
    $mappedSetBeed.setSource(null);
    assertEquals($mappedSetBeed.get().size(), 0);
    // change the source
    $mappedSetBeed.setSource(new EditableSetBeed<WellBeanBeed>());
    assertEquals($mappedSetBeed.get().size(), 0);
  }

  private EditableSetBeed<WellBeanBeed> createSource() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<WellBeanBeed> setBeed =
      new EditableSetBeed<WellBeanBeed>();
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

  private WellBeanBeed createWellBeanBeed(Long cq) {
    try {
      WellBeanBeed wellBeanBeed = new WellBeanBeed();
      LongEdit edit = new LongEdit(wellBeanBeed.cq);
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

  @Test
  public void getSizeAndCardinality() throws EditStateException, IllegalEditException {
    // add a listener to the size beed
    IntegerBeed<ActualLongEvent> sizeBeed = $mappedSetBeed.getSize();
    sizeBeed.addListener($listener5);
    assertNull($listener5.$event);
    // check the size
    assertEquals($mappedSetBeed.getSize().getLong(), 0L);
    assertEquals($mappedSetBeed.getCardinality().getLong(), 0L);
    // add source
    EditableSetBeed<WellBeanBeed> source = createSource();
    $mappedSetBeed.setSource(source);
    // check the size
    assertEquals($mappedSetBeed.getSize().getLong(), 3L);
    assertEquals($mappedSetBeed.getCardinality().getLong(), 3L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 0L);
    assertEquals($listener5.$event.getNewLong(), 3L);
    assertEquals($listener5.$event.getEdit(), null);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add elements
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd(createWellBeanBeed(5L));
    setEdit.addElementToAdd(createWellBeanBeed(6L));
    setEdit.perform();
    // check the size
    assertEquals($mappedSetBeed.getSize().getLong(), 5L);
    assertEquals($mappedSetBeed.getCardinality().getLong(), 5L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 3L);
    assertEquals($listener5.$event.getNewLong(), 5L);
    assertEquals($listener5.$event.getEdit(), setEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove($well1);
    setEdit.perform();
    // check the size
    assertEquals($mappedSetBeed.getSize().getLong(), 4L);
    assertEquals($mappedSetBeed.getCardinality().getLong(), 4L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 5L);
    assertEquals($listener5.$event.getNewLong(), 4L);
    assertEquals($listener5.$event.getEdit(), setEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove($well2);
    setEdit.perform();
    // check the size
    assertEquals($mappedSetBeed.getSize().getLong(), 3L);
    assertEquals($mappedSetBeed.getCardinality().getLong(), 3L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 4L);
    assertEquals($listener5.$event.getNewLong(), 3L);
    assertEquals($listener5.$event.getEdit(), setEdit);
  }

}