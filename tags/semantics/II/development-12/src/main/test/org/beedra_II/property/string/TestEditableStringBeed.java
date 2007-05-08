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

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.event.Listener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableStringBeed {

  public class MyEditableStringBeed extends EditableStringBeed {
    public MyEditableStringBeed(AggregateBeed owner) {
      super(owner);
      // TODO Auto-generated constructor stub
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(StringEvent event) {
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
    $owner = new MyBeanBeed();
    $editableStringBeed = new MyEditableStringBeed($owner);
    $stringEdit = new StringEdit($editableStringBeed);
    $stringEdit.perform();
    $event1 = new StringEvent($editableStringBeed, "old", "event", $stringEdit);
    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner;
  private MyEditableStringBeed $editableStringBeed;
  private StringEdit $stringEdit;
  private StringEvent $event1;
  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;

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
    $editableStringBeed.fire($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
  }

  @Test
  public void createInitialEvent() {
    StringEvent initialEvent = $editableStringBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $editableStringBeed);
    assertEquals(initialEvent.getOldValue(), null);
    assertEquals(initialEvent.getNewValue(), $editableStringBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }
}
