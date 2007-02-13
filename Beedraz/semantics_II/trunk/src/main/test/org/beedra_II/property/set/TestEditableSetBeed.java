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

import java.util.HashSet;
import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.event.Listener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableSetBeed {

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

  public class StubEditableSetBeedListener implements Listener<SetEvent<Integer, SetEdit<Integer>>> {

    public void beedChanged(SetEvent<Integer, SetEdit<Integer>> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SetEvent<Integer, SetEdit<Integer>> $event;

  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner = new MyBeanBeed();
  private EditableSetBeed<Integer> $editableSetBeed = new EditableSetBeed<Integer>($owner);
  private Set<Integer> $addedElements = new HashSet<Integer>();
  private Set<Integer> $removedElements = new HashSet<Integer>();
  private SetEdit<Integer> $setEdit = new SetEdit<Integer>($editableSetBeed);
  private SetEvent<Integer, SetEdit<Integer>> $event1 =
    new SetEvent<Integer, SetEdit<Integer>>($editableSetBeed, $addedElements, $removedElements, $setEdit);
  private PropagatedEventListener $listener1 = new PropagatedEventListener();
  private PropagatedEventListener $listener2 = new PropagatedEventListener();
  private StubEditableSetBeedListener $listener3 = new StubEditableSetBeedListener();
  private StubEditableSetBeedListener $listener4 = new StubEditableSetBeedListener();

  @Test
  public void constructor() {
    assertEquals($editableSetBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableSetBeed.fireEvent($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertTrue($listener1.$event instanceof PropagatedEvent);
    assertTrue($listener2.$event instanceof PropagatedEvent);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
  }

  @Test
  public void addElements() {
    Set<Integer> added1 = new HashSet<Integer>();
    added1.add(new Integer(1));
    added1.add(new Integer(2));
    // add first elements
    $editableSetBeed.addElements(added1);
    assertEquals($editableSetBeed.get(), added1);
    // add extra elements
    Set<Integer> added2 = new HashSet<Integer>();
    added2.add(new Integer(3));
    added2.add(new Integer(4));
    $editableSetBeed.addElements(added2);
    Set<Integer> union = new HashSet<Integer>();
    union.addAll(added1);
    union.addAll(added2);
    assertEquals($editableSetBeed.get(), union);
  }

  @Test
  public void removeElements() {
    Set<Integer> added = new HashSet<Integer>();
    added.add(new Integer(1));
    added.add(new Integer(2));
    // add elements
    $editableSetBeed.addElements(added);
    // remove elements
    Set<Integer> removed1 = new HashSet<Integer>();
    removed1.add(new Integer(2));
    $editableSetBeed.removeElements(removed1);
    Set<Integer> set1 = new HashSet<Integer>();
    set1.add(new Integer(1));
    assertEquals($editableSetBeed.get(), set1);
    // remove more elements
    Set<Integer> removed2 = new HashSet<Integer>();
    $editableSetBeed.removeElements(removed2);
    assertEquals($editableSetBeed.get(), set1);
    // remove more elements
    Set<Integer> removed3 = new HashSet<Integer>();
    removed3.add(new Integer(1));
    Set<Integer> setEmpty = new HashSet<Integer>();
    $editableSetBeed.removeElements(removed3);
    added.remove(new Integer(1));
    assertEquals($editableSetBeed.get(), setEmpty);
  }

  @Test
  public void fireEvent() {
    // register listeners
    $editableSetBeed.addListener($listener3);
    $editableSetBeed.addListener($listener4);
    // fire $editableSetBeed
    $editableSetBeed.fireEvent($event1);
    // checks
    assertNotNull($listener3.$event);
    assertNotNull($listener4.$event);
    assertEquals($listener3.$event, $event1);
    assertEquals($listener4.$event, $event1);
  }

  @Test
  public void createInitialEvent1() {
    assertTrue("the implementation of this method calls the constructor " +
        "of SetEvent; the last parameter of this constructor should be " +
        "effective according to the documentation", false);
  }

  @Test
  public void createInitialEvent2() {
    SetEvent<Integer, SetEdit<Integer>> initialEvent = $editableSetBeed.createInitialEvent();
    assertTrue(initialEvent instanceof SetEvent); // @mudo enough?
    assertEquals(initialEvent.getSource(), $editableSetBeed);
    assertEquals(initialEvent.getAddedElements(), $editableSetBeed.get());
    assertEquals(initialEvent.getRemovedElements(), new HashSet<Integer>());
    assertEquals(initialEvent.getEdit(), null);
  }
}
