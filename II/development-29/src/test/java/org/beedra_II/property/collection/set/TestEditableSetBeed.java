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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.StubListener;
import org.beedra_II.event.StubListenerMultiple;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableSetBeed {

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    $owner = new MyBeanBeed();
    $editableSetBeed = new EditableSetBeed<Integer>($owner);
    $addedElements = new HashSet<Integer>();
    $removedElements = new HashSet<Integer>();
    $setEdit = new SetEdit<Integer>($editableSetBeed);
    $setEdit.perform();
    $event1 = new ActualSetEvent<Integer>($editableSetBeed, $addedElements, $removedElements, $setEdit);
    $listener1 = new StubListenerMultiple<PropagatedEvent>();
    $listener2 = new StubListenerMultiple<PropagatedEvent>();
    $listener3 = new StubListener<SetEvent<Integer>>();
    $listener4 = new StubListener<SetEvent<Integer>>();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner;
  private EditableSetBeed<Integer> $editableSetBeed;
  private Set<Integer> $addedElements;
  private Set<Integer> $removedElements;
  private SetEdit<Integer> $setEdit;
  private SetEvent<Integer> $event1;
  private StubListenerMultiple<PropagatedEvent> $listener1;
  private StubListenerMultiple<PropagatedEvent> $listener2;
  private StubListener<SetEvent<Integer>> $listener3;
  private StubListener<SetEvent<Integer>> $listener4;

  @Test
  public void constructor() {
    assertEquals($editableSetBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event1);
    assertNull($listener1.$event2);
    assertNull($listener2.$event1);
    assertNull($listener2.$event2);
    // fire a change on the registered beed
    $editableSetBeed.fireEvent($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event1);
    assertNotNull($listener2.$event1);
    assertNotNull($listener1.$event2);
    assertNotNull($listener2.$event2);
    assertEquals($event1, $listener1.$event1.getCause());
    assertEquals($event1, $listener2.$event1.getCause());
    assertTrue($listener1.$event2.getCause() instanceof ActualLongEvent);
    if ($listener1.$event2.getCause() instanceof ActualLongEvent) {
      ActualLongEvent longEvent = (ActualLongEvent) $listener1.$event2.getCause();
      assertEquals(longEvent.getSource(), $editableSetBeed.$sizeBeed);
      assertEquals(longEvent.getNewLong(), new Long(0));
      assertEquals(longEvent.getOldLong(), new Long(0));
      assertEquals(longEvent.getEdit(), $event1.getEdit());
    }
    assertTrue($listener2.$event2.getCause() instanceof ActualLongEvent);
    if ($listener2.$event2.getCause() instanceof ActualLongEvent) {
      ActualLongEvent longEvent = (ActualLongEvent) $listener2.$event2.getCause();
      assertEquals(longEvent.getSource(), $editableSetBeed.$sizeBeed);
      assertEquals(longEvent.getNewLong(), new Long(0));
      assertEquals(longEvent.getOldLong(), new Long(0));
      assertEquals(longEvent.getEdit(), $event1.getEdit());
    }
  }

  @Test
  public void addElements() {
    Set<Integer> added1 = new HashSet<Integer>();
    added1.add(new Integer(1));
    added1.add(new Integer(2));
    // check size
    assertEquals($editableSetBeed.getSize().getLong(), 0L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 0L);
    // add first elements
    $editableSetBeed.addElements(added1);
    assertEquals($editableSetBeed.get(), added1);
    assertEquals($editableSetBeed.getSize().getLong(), 2L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 2L);
    // add extra elements
    Set<Integer> added2 = new HashSet<Integer>();
    added2.add(new Integer(3));
    added2.add(new Integer(4));
    $editableSetBeed.addElements(added2);
    Set<Integer> union = new HashSet<Integer>();
    union.addAll(added1);
    union.addAll(added2);
    assertEquals($editableSetBeed.get(), union);
    assertEquals($editableSetBeed.getSize().getLong(), 4L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 4L);
  }

  @Test
  public void removeElements() {
    Set<Integer> added = new HashSet<Integer>();
    added.add(new Integer(1));
    added.add(new Integer(2));
    // check size
    assertEquals($editableSetBeed.getSize().getLong(), 0L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 0L);
    // add elements
    $editableSetBeed.addElements(added);
    // check size
    assertEquals($editableSetBeed.getSize().getLong(), 2L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 2L);
    // remove elements
    Set<Integer> removed1 = new HashSet<Integer>();
    removed1.add(new Integer(2));
    $editableSetBeed.removeElements(removed1);
    Set<Integer> set1 = new HashSet<Integer>();
    set1.add(new Integer(1));
    assertEquals($editableSetBeed.get(), set1);
    assertEquals($editableSetBeed.getSize().getLong(), 1L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 1L);
    // remove more elements
    Set<Integer> removed2 = new HashSet<Integer>();
    $editableSetBeed.removeElements(removed2);
    assertEquals($editableSetBeed.get(), set1);
    assertEquals($editableSetBeed.getSize().getLong(), 1L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 1L);
    // remove more elements
    Set<Integer> removed3 = new HashSet<Integer>();
    removed3.add(new Integer(1));
    Set<Integer> setEmpty = new HashSet<Integer>();
    $editableSetBeed.removeElements(removed3);
    assertEquals($editableSetBeed.get(), setEmpty);
    assertEquals($editableSetBeed.getSize().getLong(), 0L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 0L);
  }

  @Test
  public void fireEvent1() {
    // register listeners
    $editableSetBeed.addListener($listener3);
    $editableSetBeed.addListener($listener4);
    // fire $editableSetBeed
    $editableSetBeed.fireEvent($event1);
    // checks
    assertNotNull($listener3.$event);
    assertNotNull($listener4.$event);
    assertEquals($event1, $listener3.$event);
    assertEquals($event1, $listener4.$event);
  }

  @Test
  public void fireEvent2() {
    // add listener to owner
    $owner.addListener($listener1);
    // fire $editableSetBeed
    $editableSetBeed.fireEvent($event1);
    // checks
    assertNotNull($listener1.$event1);
    assertEquals($listener1.$event1.getCause(), $event1);
    assertNotNull($listener1.$event2);
    assertTrue($listener1.$event2.getCause() instanceof ActualLongEvent);
    if ($listener1.$event2.getCause() instanceof ActualLongEvent) {
      ActualLongEvent longEvent = (ActualLongEvent) $listener1.$event2.getCause();
      assertEquals(longEvent.getSource(), $editableSetBeed.$sizeBeed);
      assertEquals(longEvent.getNewLong(), new Long(0));
      assertEquals(longEvent.getOldLong(), new Long(0));
      assertEquals(longEvent.getEdit(), $event1.getEdit());
    }
  }

  @Test
  public void createInitialEvent() {
    SetEvent<Integer> initialEvent = $editableSetBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $editableSetBeed);
    assertEquals(initialEvent.getAddedElements(), $editableSetBeed.get());
    assertEquals(initialEvent.getRemovedElements(), new HashSet<Integer>());
    assertEquals(initialEvent.getEdit(), null);
  }

  @Test
  public void getSizeAndCardinality() throws EditStateException, IllegalEditException {
    IntegerBeed<?> sizeBeed = $editableSetBeed.getSize();
    assertEquals(sizeBeed.getOwner(), $editableSetBeed.getOwner());
    assertEquals(sizeBeed.getLong(), 0L);
    assertEquals($editableSetBeed.getSize().getLong(), 0L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 0L);
    // add elements
    SetEdit<Integer> setEdit = new SetEdit<Integer>($editableSetBeed);
    setEdit.addElementToAdd(1);
    setEdit.addElementToAdd(2);
    setEdit.perform();
    assertEquals(sizeBeed.getLong(), 2L);
    assertEquals($editableSetBeed.getSize().getLong(), 2L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 2L);
    // add elements
    setEdit = new SetEdit<Integer>($editableSetBeed);
    setEdit.addElementToAdd(3);
    setEdit.perform();
    assertEquals(sizeBeed.getLong(), 3L);
    assertEquals($editableSetBeed.getSize().getLong(), 3L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 3L);
    // remove elements
    setEdit = new SetEdit<Integer>($editableSetBeed);
    setEdit.addElementToRemove(1);
    setEdit.addElementToRemove(3);
    setEdit.perform();
    assertEquals(sizeBeed.getLong(), 1L);
    assertEquals($editableSetBeed.getSize().getLong(), 1L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 1L);
    // remove elements
    setEdit = new SetEdit<Integer>($editableSetBeed);
    setEdit.addElementToRemove(2);
    setEdit.perform();
    assertEquals(sizeBeed.getLong(), 0L);
    assertEquals($editableSetBeed.getSize().getLong(), 0L);
    assertEquals($editableSetBeed.getCardinality().getLong(), 0L);
  }


}
