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

package org.beedra_II.property.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableSimplePropertyBeed {

  public class StubEditableSimplePropertyBeed extends
      EditableSimplePropertyBeed<Integer, StubEditableSimplePropertyBeedEvent> {

    protected StubEditableSimplePropertyBeed(AggregateBeed owner) {
      super(owner);
    }

    @Override
    protected StubEditableSimplePropertyBeedEvent createInitialEvent() {
      return $initialEvent;
    }

    public StubEditableSimplePropertyBeedEvent $initialEvent = new StubEditableSimplePropertyBeedEvent(this);
  }

  public class StubEditableSimplePropertyBeedEvent extends Event<Edit<?>> {

    public StubEditableSimplePropertyBeedEvent(StubEditableSimplePropertyBeed source) {
      super(source, null);
    }

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

  public class StubEditableSimplePropertyBeedListener implements Listener<StubEditableSimplePropertyBeedEvent> {

    public void beedChanged(StubEditableSimplePropertyBeedEvent event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public StubEditableSimplePropertyBeedEvent $event;

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
  private StubEditableSimplePropertyBeed $editableSimplePropertyBeed = new StubEditableSimplePropertyBeed($owner);
  private StubEditableSimplePropertyBeedEvent $event1 = new StubEditableSimplePropertyBeedEvent($editableSimplePropertyBeed);
  private PropagatedEventListener $listener1 = new PropagatedEventListener();
  private PropagatedEventListener $listener2 = new PropagatedEventListener();
  private StubEditableSimplePropertyBeedListener $listener3 = new StubEditableSimplePropertyBeedListener();
  private StubEditableSimplePropertyBeedListener $listener4 = new StubEditableSimplePropertyBeedListener();

  @Test
  public void constructor() {
    assertEquals($editableSimplePropertyBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableSimplePropertyBeed.fireEvent($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertTrue($listener1.$event instanceof PropagatedEvent);
    assertTrue($listener2.$event instanceof PropagatedEvent);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
  }

  @Test
  public void assign() {
    Integer newValue = new Integer(5);
    $editableSimplePropertyBeed.assign(newValue);
    assertEquals($editableSimplePropertyBeed.get(), newValue);
  }

  @Test
  public void safeValueCopy() {
    Integer newValue = new Integer(5);
    Integer copy = $editableSimplePropertyBeed.safeValueCopy(newValue);
    assertTrue(copy == newValue);
  }

  @Test
  public void isAcceptable() {
    boolean isAcceptable = $editableSimplePropertyBeed.isAcceptable(new Integer(5));
    assertEquals(isAcceptable, true);
  }

  @Test
  public void fireEvent() {
    // register listeners
    $editableSimplePropertyBeed.addListener($listener3);
    $editableSimplePropertyBeed.addListener($listener4);
    // fire event
    $editableSimplePropertyBeed.fireEvent($event1);
    // checks
    assertNotNull($listener3.$event);
    assertNotNull($listener4.$event);
    assertEquals($listener3.$event, $event1);
    assertEquals($listener4.$event, $event1);
  }
}
