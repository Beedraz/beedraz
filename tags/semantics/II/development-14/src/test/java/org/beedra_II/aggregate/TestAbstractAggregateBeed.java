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

package org.beedra_II.aggregate;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.beedra_II.event.Listener;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerEdit;
import org.beedra_II.property.integer.IntegerEvent;
import org.beedra_II.property.integer.IntegerSumBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestAbstractAggregateBeed {

  public class StubAbstractAggregateBeed extends AbstractAggregateBeed {
    /**
     * Makes the fireChangeEvent method public for testing reasons.
     * @param event
     */
    public void fire(PropagatedEvent event) {
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

  public class MyEditableIntegerBeed extends EditableIntegerBeed {

    public MyEditableIntegerBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * Makes the fireChangeEvent method public for testing reasons.
     * @param event
     */
    public void fire(IntegerEvent event) {
      fireChangeEvent(event);
    }
  }

  public class MyIntegerSumBeed extends IntegerSumBeed {

    public MyIntegerSumBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * Makes the fireChangeEvent method public for testing reasons.
     * @param event
     */
    public void fire(IntegerEvent event) {
      fireChangeEvent(event);
    }
  }

  @Before
  public void setUp() throws Exception {
    $aggregateBeed1 = new StubAbstractAggregateBeed();
    $aggregateBeed2 = new StubAbstractAggregateBeed();

    $beed1 = new MyEditableIntegerBeed($aggregateBeed1);
//    $beed2 = new MyIntegerSumBeed($subject);
    $beed3 = new MyEditableIntegerBeed($aggregateBeed2);

    $edit1 = new IntegerEdit($beed1);
    $edit1.perform();
    $event1 = new IntegerEvent($beed1, new Integer(1), new Integer(11), $edit1);
//    $edit2 = new IntegerEdit($beed2);
//    $event2 = new IntegerEvent($beed2, new Integer(2), new Integer(22), $edit2);
    $edit3 = new IntegerEdit($beed3);
    $edit3.perform();
    $event3 = new IntegerEvent($beed3, new Integer(3), new Integer(33), $edit3);

    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AbstractAggregateBeed $aggregateBeed1;
  private AbstractAggregateBeed $aggregateBeed2;

  private MyEditableIntegerBeed $beed1;
//  private MyIntegerSumBeed $beed2;
  private MyEditableIntegerBeed $beed3;

  private IntegerEdit $edit1;
  private IntegerEvent $event1;
//  private IntegerEdit $edit2;
//  private IntegerEvent $event2;
  private IntegerEdit $edit3;
  private IntegerEvent $event3;

  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;

  @Test
  public void isAggregateElement() {
    // basic
  }

  @Test
  public void registerAggregateElement() {
    // add listeners to the aggregate beed
    $aggregateBeed1.addListener($listener1);
    $aggregateBeed1.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // register a beed with the aggregate beed
    $aggregateBeed1.registerAggregateElement($beed1);
    // fire a change on the registered beed
    $beed1.fire($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
    // reset the listeners
    $listener1.reset();
    $listener2.reset();
    // fire a change on a beed that is not registered
    $beed3.fire($event3);
    // listeners of the aggregate beed should not be notified
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // reset the listeners
    $listener1.reset();
    $listener2.reset();
    // register another beeds with the aggregate beed
    $aggregateBeed1.registerAggregateElement($beed3);
    // fire a change on the registered beeds
    $beed3.fire($event3);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event3, $listener1.$event.getCause());
    assertEquals($event3, $listener1.$event.getCause());
  }
}

