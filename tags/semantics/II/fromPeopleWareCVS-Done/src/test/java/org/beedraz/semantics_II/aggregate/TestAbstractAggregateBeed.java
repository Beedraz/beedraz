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

package org.beedraz.semantics_II.aggregate;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedraz.semantics_II.StubEvent;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateEvent;
import org.beedraz.semantics_II.edit.StubSimpleEdit;
import org.beedraz.semantics_II.expression.StubEditableSimpleExpressionBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestAbstractAggregateBeed {

  @Before
  public void setUp() throws Exception {
    $aggregateBeed1 = new StubAggregateBeed();
    $aggregateBeed2 = new StubAggregateBeed();

    $beed1 = new StubEditableSimpleExpressionBeed($aggregateBeed1);
//    $beed2 = new MyIntegerSumBeed($subject);
    $beed3 = new StubEditableSimpleExpressionBeed($aggregateBeed2);

    $edit1 = new StubSimpleEdit($beed1);
    $edit1.perform();
    $event1 = new StubEvent($beed1);
//    $edit2 = new LongEdit($beed2);
//    $event2 = new IntegerEvent($beed2, new Integer(2), new Integer(22), $edit2);
    $edit3 = new StubSimpleEdit($beed3);
    $edit3.perform();
    $event3 = new StubEvent($beed3);

    $listener1 = new StubListener<AggregateEvent>();
    $listener2 = new StubListener<AggregateEvent>();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private StubAggregateBeed $aggregateBeed1;
  private StubAggregateBeed $aggregateBeed2;

  private StubEditableSimpleExpressionBeed $beed1;
//  private MyIntegerSumBeed $beed2;
  private StubEditableSimpleExpressionBeed $beed3;

  private StubSimpleEdit $edit1;
  private StubEvent $event1;
//  private LongEdit $edit2;
//  private IntegerEvent $event2;
  private StubSimpleEdit $edit3;
  private StubEvent $event3;

  private StubListener<AggregateEvent> $listener1;
  private StubListener<AggregateEvent> $listener2;

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
    $beed1.publicUpdateDependents($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertTrue($listener1.$event.getComponentevents().contains($event1));
    assertTrue($listener2.$event.getComponentevents().contains($event1));
    // reset the listeners
    $listener1.reset();
    $listener2.reset();
    // fire a change on a beed that is not registered
    $beed3.publicUpdateDependents($event3);
    // listeners of the aggregate beed should not be notified
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // reset the listeners
    $listener1.reset();
    $listener2.reset();
    // register another beeds with the aggregate beed
    $aggregateBeed1.registerAggregateElement($beed3);
    // fire a change on the registered beeds
    $beed3.publicUpdateDependents($event3);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentevents().size());
    assertEquals(1, $listener2.$event.getComponentevents().size());
    assertTrue($listener1.$event.getComponentevents().contains($event3));
    assertTrue($listener2.$event.getComponentevents().contains($event3));
  }

}

