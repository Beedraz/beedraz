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

package org.beedra_II;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.beedra_II.aggregate.AbstractAggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.event.Listener;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerEdit;
import org.beedra_II.property.integer.IntegerEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestAbstractAggregateBeed {

  public class StubAbstractAggregateBeed extends AbstractAggregateBeed {
    // NOP
  }

  public class StubListener implements Listener<PropagatedEvent> {


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
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AbstractAggregateBeed $subject = new StubAbstractAggregateBeed();

  private EditableIntegerBeed $beed1 = new EditableIntegerBeed($subject);
//  private Beed<IntegerEvent> $beed2 = new EditableIntegerBeed($subject);

  private IntegerEdit $edit = new IntegerEdit($beed1);
  private IntegerEvent $event = new IntegerEvent($beed1, new Integer(0), new Integer(1), $edit);

  private StubListener $listener1 = new StubListener();
  private StubListener $listener2 = new StubListener();

  @Test
  public void testIsAggregateElement() {
    // basic
  }

  @Test
  public void testRegisterAggregateElement() {
    // add listeners to the aggregated beed
    $subject.addListener($listener1);
    $subject.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // register another beed
    $subject.registerAggregateElement($beed1);
    // fire a change on the registered beed
    $beed1.fireChangeEvent($event);
    // listeners of the aggregated beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event, $listener1.$event.getCause());
    assertEquals($event, $listener1.$event.getCause());
  }
}

