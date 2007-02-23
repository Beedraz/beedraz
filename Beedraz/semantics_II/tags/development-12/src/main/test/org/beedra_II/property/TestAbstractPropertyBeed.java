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

package org.beedra_II.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractPropertyBeed {

  public class StubAbstractPropertyBeed extends
      AbstractPropertyBeed<StubAbstractPropertyBeedEvent> {

    protected StubAbstractPropertyBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * Makes the fireChangeEvent method public for testing reasons.
     *
     * @param event
     */
    public void fire(StubAbstractPropertyBeedEvent event) {
      fireChangeEvent(event);
    }

    @Override
    protected StubAbstractPropertyBeedEvent createInitialEvent() {
      return $initialEvent;
    }

    public StubAbstractPropertyBeedEvent $initialEvent = new StubAbstractPropertyBeedEvent(this);
  }

  public class StubAbstractPropertyBeedEvent extends Event {

    public StubAbstractPropertyBeedEvent(StubAbstractPropertyBeed source) {
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

  public class StubAbstractPropertyBeedListener implements Listener<StubAbstractPropertyBeedEvent> {

    public void beedChanged(StubAbstractPropertyBeedEvent event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public StubAbstractPropertyBeedEvent $event;

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
  private StubAbstractPropertyBeed $propertyBeed = new StubAbstractPropertyBeed($owner);
  private StubAbstractPropertyBeedEvent $event1 = new StubAbstractPropertyBeedEvent($propertyBeed);
  private PropagatedEventListener $listener1 = new PropagatedEventListener();
  private PropagatedEventListener $listener2 = new PropagatedEventListener();
  private StubAbstractPropertyBeedListener $listener3 = new StubAbstractPropertyBeedListener();

  @Test
  public void constructor() {
    assertEquals($propertyBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $propertyBeed.fire($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
  }

  @Test
  public void addListenerInitialEvent() {
    $propertyBeed.addListenerInitialEvent($listener3);
    assertTrue($propertyBeed.isListener($listener3));
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event, $propertyBeed.$initialEvent);
  }
}
