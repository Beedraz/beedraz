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

package org.beedra_II.property.string;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.AggregateEvent;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.event.StubListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestEditableStringBeed {

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $editableStringBeed = new StubEditableStringBeed($owner);
    $stringEdit = new StringEdit($editableStringBeed);
    $stringEdit.perform();
    $event1 = new StringEvent($editableStringBeed, "old", "event", $stringEdit);
    $listener1 = new StubListener<AggregateEvent>();
    $listener2 = new StubListener<AggregateEvent>();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner;
  private StubEditableStringBeed $editableStringBeed;
  private StringEdit $stringEdit;
  private StringEvent $event1;
  private StubListener<AggregateEvent> $listener1;
  private StubListener<AggregateEvent> $listener2;

  @Test
  public void constructor() {
    assertEquals($editableStringBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableStringBeed.publicUpdateDependents($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentevents().size());
    assertEquals(1, $listener2.$event.getComponentevents().size());
    assertTrue($listener1.$event.getComponentevents().contains($event1));
    assertTrue($listener2.$event.getComponentevents().contains($event1));
  }

}
