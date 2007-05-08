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

package org.beedra_II.property.integer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.event.Listener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableIntegerBeed {

  public class MyEditableIntegerBeed extends EditableIntegerBeed {
    public MyEditableIntegerBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(IntegerEvent event) {
      fireChangeEvent(event);
    }
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

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
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
  private MyEditableIntegerBeed $editableIntegerBeed = new MyEditableIntegerBeed($owner);
  private IntegerEdit $stringEdit = new IntegerEdit($editableIntegerBeed);
  private IntegerEvent $event1 = new IntegerEvent($editableIntegerBeed, new Integer(0), new Integer(1), $stringEdit);
  private PropagatedEventListener $listener1 = new PropagatedEventListener();
  private PropagatedEventListener $listener2 = new PropagatedEventListener();

  @Test
  public void testConstructor() {
    assertEquals($editableIntegerBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableIntegerBeed.fire($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertTrue($listener1.$event instanceof PropagatedEvent);
    assertTrue($listener2.$event instanceof PropagatedEvent);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
  }

  @Test
  public void testCreateInitialEvent1() {
    assertTrue("the implementation of this method calls the constructor " +
        "of IntegerEvent; the last parameter of this constructor should be " +
        "effective according to the documentation", false);
  }

  @Test
  public void testCreateInitialEvent2() {
    IntegerEvent initialEvent = $editableIntegerBeed.createInitialEvent();
    assertTrue(initialEvent instanceof IntegerEvent); // @mudo enough?
    assertEquals(initialEvent.getSource(), $editableIntegerBeed);
    assertEquals(initialEvent.getOldValue(), null);
    assertEquals(initialEvent.getNewValue(), $editableIntegerBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }
}
