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

import org.beedra_II.BeedFilter;
import org.beedra_II.StubListener;
import org.beedra_II.aggregate.AggregateEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.association.set.BidirToManyBeed;
import org.beedra_II.property.association.set.BidirToOneEdit;
import org.beedra_II.property.association.set.EditableBidirToOneBeed;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
import org.beedra_II.property.number.integer.long64.EditableLongBeed;
import org.beedra_II.property.number.integer.long64.LongEdit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestFilteredSetBeed {

  public class MyFilteredSetBeed extends FilteredSetBeed<WellBeanBeed, AggregateEvent> {

    public MyFilteredSetBeed(BeedFilter<WellBeanBeed> filter) {
      super(filter);
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

    public WellBeanBeed() {
      registerAggregateElement(cq);
    }

    /**
     * The run in which the well is contained.
     */
    public final EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed> run =
      new EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>(this);

    /**
     * The Cq value of the well.
     */
    public final EditableLongBeed cq = new EditableLongBeed();

  }

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    $owner = new MyBeanBeed();
    $filter = new BeedFilter<WellBeanBeed>() {
        public boolean filter(WellBeanBeed element) {
          return element.cq.get() != null &&
          element.cq.get().intValue() % 2 == 0;
        }
        public boolean dependsOnBeed() {
          return true;
        }
    };
    $filteredSetBeed = new MyFilteredSetBeed($filter);
    $run = new RunBeanBeed();
    $wellNull = new WellBeanBeed();
    $well0 = new WellBeanBeed();
    $well1 = new WellBeanBeed();
    $well2 = new WellBeanBeed();
    $well3 = new WellBeanBeed();
    $cqNull = null;
    $cq0 = new Long(0);
    $cq1 = new Long(1);
    $cq2 = new Long(2);
    $cq3 = new Long(3);
    $listener1 = new StubListener<AggregateEvent>();
    $listener2 = new StubListener<AggregateEvent>();
    $listener3 = new StubListener<SetEvent<WellBeanBeed>>();
    $listener5 = new StubListener<ActualLongEvent>();
    $event = new ActualSetEvent<WellBeanBeed>($filteredSetBeed, null, null, null);
    // add the wells to the run
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellNull.run);
    edit.setGoal($run.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well0.run);
    edit.setGoal($run.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well1.run);
    edit.setGoal($run.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well2.run);
    edit.setGoal($run.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well3.run);
    edit.setGoal($run.wells);
    edit.perform();
    // initialise the cq values
    LongEdit longEdit = new LongEdit($wellNull.cq);
    longEdit.setGoal($cqNull);
    longEdit.perform();
    longEdit = new LongEdit($well0.cq);
    longEdit.setGoal($cq0);
    longEdit.perform();
    longEdit = new LongEdit($well1.cq);
    longEdit.setGoal($cq1);
    longEdit.perform();
    longEdit = new LongEdit($well2.cq);
    longEdit.setGoal($cq2);
    longEdit.perform();
    longEdit = new LongEdit($well3.cq);
    longEdit.setGoal($cq3);
    longEdit.perform();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private RunBeanBeed $run;
  private WellBeanBeed $wellNull;
  private WellBeanBeed $well0;
  private WellBeanBeed $well1;
  private WellBeanBeed $well2;
  private WellBeanBeed $well3;
  private Long $cqNull;
  private Long $cq0;
  private Long $cq1;
  private Long $cq2;
  private Long $cq3;
  private BeedFilter<WellBeanBeed> $filter;
  private MyFilteredSetBeed $filteredSetBeed;
  private MyBeanBeed $owner;
  private StubListener<AggregateEvent> $listener1;
  private StubListener<AggregateEvent> $listener2;
  private StubListener<SetEvent<WellBeanBeed>> $listener3;
  private StubListener<ActualLongEvent> $listener5;
  private SetEvent<WellBeanBeed> $event;

  @Test
  public void constructor() {
    assertEquals($filteredSetBeed.getOwner(), $owner);
    assertEquals($filteredSetBeed.getFilter(), $filter);
    assertEquals($filteredSetBeed.getSource(), null);
    assertTrue($filteredSetBeed.get().isEmpty());
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $filteredSetBeed.publicUpdateDependents($event);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentevents().size());
    assertEquals(1, $listener2.$event.getComponentevents().size());
    assertTrue($listener1.$event.getComponentevents().contains($event));
    assertTrue($listener2.$event.getComponentevents().contains($event));
  }

  /**
   * Source is null.
   */
  @Test
  public void setSource1() throws EditStateException, IllegalEditException {
    // register listeners to the FilteredSetBeed
    $filteredSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    SetBeed<WellBeanBeed, ?> source = null;
    $filteredSetBeed.setSource(source);
    assertEquals($filteredSetBeed.getSource(), source);
    assertTrue($filteredSetBeed.get().isEmpty());
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Source is effective.
   */
  @Test
  public void setSource2() throws EditStateException, IllegalEditException {
    // register listeners to the FilteredSetBeed
    $filteredSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    EditableSetBeed<WellBeanBeed> source = createSource();
    $filteredSetBeed.setSource(source);
    assertEquals($filteredSetBeed.getSource(), source);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
    // value has changed, so the listeners of the filtered set beed are notified
    Set<WellBeanBeed> added = new HashSet<WellBeanBeed>();
    added.add($well2);
    Set<WellBeanBeed> removed = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), null);
    // The FilteredSetBeed is registered as listener of the source, so when
    // the source changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(source);
    WellBeanBeed well4 = createWellBeanBeed(4L);
    setEdit.addElementToAdd(well4);
    setEdit.perform();
    removed = new HashSet<WellBeanBeed>();
    added = new HashSet<WellBeanBeed>();
    added.add(well4);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit);
    assertEquals($filteredSetBeed.get().size(), 2);
    assertTrue($filteredSetBeed.get().contains($well2));
    assertTrue($filteredSetBeed.get().contains(well4));
    // The FilteredSetBeed is registered as listener of all beeds in the source,
    // so when one of them changes, the beed should be notified
    LongEdit longEdit = new LongEdit(well4.cq);
    longEdit.setGoal(5L);
    longEdit.perform();
    removed = new HashSet<WellBeanBeed>();
    removed.add(well4);
    added = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), longEdit);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
    // change the beed again
    $listener3.reset();
    assertNull($listener3.$event);
    longEdit = new LongEdit(well4.cq);
    longEdit.setGoal(6L);
    longEdit.perform();
    removed = new HashSet<WellBeanBeed>();
    added = new HashSet<WellBeanBeed>();
    added.add(well4);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), longEdit);
    assertEquals($filteredSetBeed.get().size(), 2);
    assertTrue($filteredSetBeed.get().contains($well2));
    assertTrue($filteredSetBeed.get().contains(well4));

    // When a beed is removed from the source, the FilteredSetBeed is removed as listener
    // of that beed.
    $listener3.reset();
    assertNull($listener3.$event);
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove(well4);
    setEdit.perform();
    removed = new HashSet<WellBeanBeed>();
    removed.add(well4);
    added = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
    $listener3.reset();
    assertNull($listener3.$event);
    // so, when the removed beed is edited, the filtered set beed is NOT notified
    longEdit = new LongEdit(well4.cq);
    longEdit.setGoal(7L);
    longEdit.perform();
    assertNull($listener3.$event); // the FilteredSetBeed is NOT notified
    // and the value of the filtered set beed is correct
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
  }

  @Test
  public void get() throws EditStateException, IllegalEditException {
    // filter the even wells
    SetBeed<WellBeanBeed, ?> source = createSource();
    $filteredSetBeed.setSource(source);
    Set<WellBeanBeed> result = $filteredSetBeed.get();
    assertEquals(result.size(), 1);
    assertTrue(result.contains($well2));
    Iterator<WellBeanBeed> iterator = result.iterator(); // we do not know the order
    assertTrue(iterator.hasNext());
    WellBeanBeed next = iterator.next();
    assertTrue(next == $well2);
    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
    // filter the odd wells
    BeedFilter<WellBeanBeed> filter =
      new BeedFilter<WellBeanBeed>() {
        public boolean filter(WellBeanBeed element) {
          return element.cq.get() != null &&
                 element.cq.get().intValue() % 2 == 1;
        }
        public boolean dependsOnBeed() {
          return true;
        }
    };
    $filteredSetBeed = new MyFilteredSetBeed(filter);
    $filteredSetBeed.setSource(source);
    result = $filteredSetBeed.get();
    assertEquals(result.size(), 2);
    assertTrue(result.contains($well1));
    assertTrue(result.contains($well3));
    iterator = result.iterator();
    assertTrue(iterator.hasNext()); // we do not know the order
    next = iterator.next();
    assertTrue(next == $well1 || next == $well3);
    assertTrue(iterator.hasNext());
    next = iterator.next();
    assertTrue(next == $well1 || next == $well3);
    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
    // filter the wells whose cq value is empty
    filter = new BeedFilter<WellBeanBeed>() {

      public boolean filter(WellBeanBeed element) {
        return element.cq.get() == null;
      }
      public boolean dependsOnBeed() {
        return true;
      }
    };
    $filteredSetBeed = new MyFilteredSetBeed(filter);
    $filteredSetBeed.setSource(source);
    result = $filteredSetBeed.get();
    assertEquals(result.size(), 1);
    assertTrue(result.contains($wellNull));
    iterator = result.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(iterator.next(), $wellNull);
    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
    // source = null
    source = null;
    $filteredSetBeed.setSource(source);
    result = $filteredSetBeed.get();
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


  @Test
  public void recalculate() throws EditStateException, IllegalEditException {
    // double mean beed has no source
    $filteredSetBeed.recalculate();
    assertTrue($filteredSetBeed.get().isEmpty());
    // create source
    EditableSetBeed<WellBeanBeed> source =
      new EditableSetBeed<WellBeanBeed>();
    // add source to mean beed
    $filteredSetBeed.setSource(source);
    // recalculate (setBeed contains no elements)
    $filteredSetBeed.recalculate();
    assertTrue($filteredSetBeed.get().isEmpty());
    // add beed
    SetEdit<WellBeanBeed> setEdit =
      new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well1);
    setEdit.perform();
    // recalculate (setBeed contains beed 1)
    $filteredSetBeed.recalculate();
    assertEquals($filteredSetBeed.get().size(), 0);
    // recalculate (setBeed contains beed 1)
    $filteredSetBeed.recalculate();
    assertEquals($filteredSetBeed.get().size(), 0);
    // add beed
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well2);
    setEdit.perform();
    // recalculate (setBeed contains beed 1 and 2)
    $filteredSetBeed.recalculate();
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
    // recalculate (setBeed contains beed 1 and 2)
    $filteredSetBeed.recalculate();
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
    // add beed
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd($well3);
    setEdit.perform();
    // recalculate (setBeed contains beed 1, 2 and 3)
    $filteredSetBeed.recalculate();
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
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
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($wellNull);
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
    IntegerBeed<ActualLongEvent> sizeBeed = $filteredSetBeed.getSize();
    sizeBeed.addListener($listener5);
    assertNull($listener5.$event);
    // check the size
    assertEquals($filteredSetBeed.getSize().getLong(), 0L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 0L);
    // add source
    EditableSetBeed<WellBeanBeed> source = createSource();
    $filteredSetBeed.setSource(source);
    // check the size
    assertEquals($filteredSetBeed.getSize().getLong(), 1L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 1L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 0L);
    assertEquals($listener5.$event.getNewLong(), 1L);
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
    assertEquals($filteredSetBeed.getSize().getLong(), 2L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 2L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 1L);
    assertEquals($listener5.$event.getNewLong(), 2L);
    assertEquals($listener5.$event.getEdit(), setEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove($well1);
    setEdit.perform();
    // check the size
    assertEquals($filteredSetBeed.getSize().getLong(), 2L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 2L);
    // check the listener (size has not changed, so no events should be sent)
    assertNull($listener5.$event);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove($well2);
    setEdit.perform();
    // check the size
    assertEquals($filteredSetBeed.getSize().getLong(), 1L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 1L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 2L);
    assertEquals($listener5.$event.getNewLong(), 1L);
    assertEquals($listener5.$event.getEdit(), setEdit);
  }

}