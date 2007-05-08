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

package org.beedraz.semantics_II.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedraz.semantics_II.StubEvent;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateEvent;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableSimpleExpressionBeed {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner = new StubBeanBeed();
  private StubEditableSimpleExpressionBeed $editableSimpleExpressionBeed =
    new StubEditableSimpleExpressionBeed($owner);
  private StubEvent $event1 = new StubEvent($editableSimpleExpressionBeed);
  private StubListener<AggregateEvent> $listener1 = new StubListener<AggregateEvent>();
  private StubListener<AggregateEvent> $listener2 = new StubListener<AggregateEvent>();
  private StubListener<StubEvent> $listener3 = new StubListener<StubEvent>();
  private StubListener<StubEvent> $listener4 = new StubListener<StubEvent>();

  @Test
  public void constructor() {
    assertEquals($editableSimpleExpressionBeed.getOwner(), $owner);
    // the beed should be registered with the owner:
    // add listeners to the beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableSimpleExpressionBeed.publicUpdateDependents($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentevents().size());
    assertEquals(1, $listener2.$event.getComponentevents().size());
    assertTrue($listener1.$event.getComponentevents().contains($event1));
    assertTrue($listener2.$event.getComponentevents().contains($event1));
  }

  @Test
  public void assign() {
    Integer newValue = new Integer(5);
    $editableSimpleExpressionBeed.assign(newValue);
    assertEquals($editableSimpleExpressionBeed.get(), newValue);
  }

  @Test
  public void safeValueCopy() {
    Object newValue = new Object();
    Object copy = $editableSimpleExpressionBeed.publicSafeValueCopy(newValue);
    assertEquals(newValue, copy);
  }

  @Test
  public void isAcceptable() {
    boolean isAcceptable = $editableSimpleExpressionBeed.isAcceptable(new Integer(5));
    assertEquals(isAcceptable, true);
  }

  @Test
  public void fireEvent() {
    // register listeners
    $editableSimpleExpressionBeed.addListener($listener3);
    $editableSimpleExpressionBeed.addListener($listener4);
    // fire event
    $editableSimpleExpressionBeed.publicUpdateDependents($event1);
    // checks
    assertNotNull($listener3.$event);
    assertNotNull($listener4.$event);
    assertEquals($listener3.$event, $event1);
    assertEquals($listener4.$event, $event1);
  }

}
