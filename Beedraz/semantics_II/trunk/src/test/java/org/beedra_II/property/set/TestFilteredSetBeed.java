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

public class TestFilteredSetBeed {

  public class MyFilteredSetBeed extends FilteredSetBeed<WellBeanBeed, PropagatedEvent> {

    public MyFilteredSetBeed(FilterCriterionFactory<WellBeanBeed> filterCriterionFactory, AggregateBeed owner) {
      super(filterCriterionFactory, owner);
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
    $filterCriterionFactory = new FilterCriterionFactory<WellBeanBeed>() {

        public FilterCriterion<WellBeanBeed> createFilterCriterion(WellBeanBeed element) {
          return new AbstractFilterCriterion<WellBeanBeed>(element) {

            /**
             * The cq value should be effective and even.
             */
            public boolean isValid() {
              return getElement().cq.get() != null &&
                     getElement().cq.get().intValue() % 2 == 0;
            }

          };
        }
    };
    $filteredSetBeed = new MyFilteredSetBeed($filterCriterionFactory, $owner);
    $run = new RunBeanBeed();
    $wellNull = new WellBeanBeed();
    $well0 = new WellBeanBeed();
    $well1 = new WellBeanBeed();
    $well2 = new WellBeanBeed();
    $well3 = new WellBeanBeed();
    $cqNull = null;
    $cq0 = new Integer(0);
    $cq1 = new Integer(1);
    $cq2 = new Integer(2);
    $cq3 = new Integer(3);
    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
    $event = new SetEvent<WellBeanBeed>($filteredSetBeed, null, null, null);
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
    IntegerEdit integerEdit = new IntegerEdit($wellNull.cq);
    integerEdit.setGoal($cqNull);
    integerEdit.perform();
    integerEdit = new IntegerEdit($well0.cq);
    integerEdit.setGoal($cq0);
    integerEdit.perform();
    integerEdit = new IntegerEdit($well1.cq);
    integerEdit.setGoal($cq1);
    integerEdit.perform();
    integerEdit = new IntegerEdit($well2.cq);
    integerEdit.setGoal($cq2);
    integerEdit.perform();
    integerEdit = new IntegerEdit($well3.cq);
    integerEdit.setGoal($cq3);
    integerEdit.perform();
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
  private Integer $cqNull;
  private Integer $cq0;
  private Integer $cq1;
  private Integer $cq2;
  private Integer $cq3;
  private FilterCriterionFactory<WellBeanBeed> $filterCriterionFactory;
  private MyFilteredSetBeed $filteredSetBeed;
  private MyBeanBeed $owner;
  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;
  private SetEvent<WellBeanBeed> $event;

  @Test
  public void constructor() {
    assertEquals($filteredSetBeed.getOwner(), $owner);
    assertEquals($filteredSetBeed.getFilterCriterionFactory(), $filterCriterionFactory);
    assertEquals($filteredSetBeed.getSource(), null);
    assertTrue($filteredSetBeed.get().isEmpty());
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $filteredSetBeed.fireChangeEventPublic($event);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event, $listener1.$event.getCause());
    assertEquals($event, $listener1.$event.getCause());
  }

  @Test
  public void setSource() {
    SetBeed<WellBeanBeed> source1 = new EditableSetBeed<WellBeanBeed>($owner);
    $filteredSetBeed.setSource(source1);
    assertEquals($filteredSetBeed.getSource(), source1);
    SetBeed<WellBeanBeed> source2 = null;
    $filteredSetBeed.setSource(source2);
    assertEquals($filteredSetBeed.getSource(), source2);
  }

  @Test
  public void get() {
    // filter the even wells
    SetBeed<WellBeanBeed> source = $run.wells;
    $filteredSetBeed.setSource(source);
    Set<WellBeanBeed> result = $filteredSetBeed.get();
    assertEquals(result.size(), 2);
    assertTrue(result.contains($well0));
    assertTrue(result.contains($well2));
    Iterator<WellBeanBeed> iterator = result.iterator(); // we do not know the order
    assertTrue(iterator.hasNext());
    WellBeanBeed next = iterator.next();
    assertTrue(next == $well0 || next == $well2);
    assertTrue(iterator.hasNext());
    next = iterator.next();
    assertTrue(next == $well0 || next == $well2);
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
    FilterCriterionFactory<WellBeanBeed> factory =
      new FilterCriterionFactory<WellBeanBeed>() {

        public FilterCriterion<WellBeanBeed> createFilterCriterion(WellBeanBeed element) {
          return new AbstractFilterCriterion<WellBeanBeed>(element) {

            public boolean isValid() {
              return getElement().cq.get() != null &&
                     getElement().cq.get().intValue() % 2 == 1;
            }

          };
        }

    };
    $filteredSetBeed = new MyFilteredSetBeed(factory, $owner);
    $filteredSetBeed.setSource(source);
    result = $filteredSetBeed.get();
    assertEquals(result.size(), 2);
    assertTrue(result.contains($well1));
    assertTrue(result.contains($well3));
    iterator = result.iterator();
    assertTrue(iterator.hasNext()); // we do not know the order
    iterator.next();
    assertTrue(iterator.hasNext());
    iterator.next();
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
    factory = new FilterCriterionFactory<WellBeanBeed>() {

      public FilterCriterion<WellBeanBeed> createFilterCriterion(WellBeanBeed element) {
        return new AbstractFilterCriterion<WellBeanBeed>(element) {

          public boolean isValid() {
            return getElement().cq.get() == null;
          }

        };
      }

    };
    $filteredSetBeed = new MyFilteredSetBeed(factory, $owner);
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
  public void createInitialEvent() {
    SetEvent<WellBeanBeed> initialEvent = $filteredSetBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $filteredSetBeed);
    assertEquals(initialEvent.getAddedElements(), $filteredSetBeed.get());
    assertEquals(initialEvent.getRemovedElements(), new HashSet<IntegerBeed>());
    assertEquals(initialEvent.getEdit(), null);
  }

}