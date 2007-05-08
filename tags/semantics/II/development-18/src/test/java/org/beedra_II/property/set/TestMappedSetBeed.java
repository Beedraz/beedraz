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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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

public class TestMappedSetBeed {

  public class MyMappedSetBeed extends MappedSetBeed<WellBeanBeed, IntegerBeed> {

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
    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
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
  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;
  private SetEvent<IntegerBeed> $event;

  @Test
  public void constructor() {
    assertEquals($mappedSetBeed.getOwner(), $owner);
    assertEquals($mappedSetBeed.getMapping(), $mapping);
    assertEquals($mappedSetBeed.getSource(), null);
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

  @Test
  public void setSource() {
    SetBeed<WellBeanBeed> source1 = new EditableSetBeed<WellBeanBeed>($owner);
    $mappedSetBeed.setSource(source1);
    assertEquals($mappedSetBeed.getSource(), source1);
    SetBeed<WellBeanBeed> source2 = null;
    $mappedSetBeed.setSource(source2);
    assertEquals($mappedSetBeed.getSource(), source2);
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
    Iterator<WellBeanBeed> iteratorWell = $run.wells.get().iterator();
    assertTrue(iteratorCq.hasNext());
    assertEquals(iteratorCq.next(), iteratorWell.next().cq);
    assertTrue(iteratorCq.hasNext());
    assertEquals(iteratorCq.next(), iteratorWell.next().cq);
    assertTrue(iteratorCq.hasNext());
    assertEquals(iteratorCq.next(), iteratorWell.next().cq);
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
      sum += cq.get();
    }
    assertEquals(sum, 6);
    // test2
    $mappedSetBeed.setSource(null);
    sum = 0;
    for (IntegerBeed cq : $mappedSetBeed.get()) {
      sum += cq.get();
    }
    assertEquals(sum, 0);
  }

}