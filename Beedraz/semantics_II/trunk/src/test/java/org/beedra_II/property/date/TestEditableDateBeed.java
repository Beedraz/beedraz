/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.property.date;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.event.StubListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestEditableDateBeed {

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $editableDateBeed = new StubEditableDateBeed($owner);
    $stringEdit = new DateEdit($editableDateBeed);
    $stringEdit.perform();
    $event1 = new DateEvent($editableDateBeed, Util.createDate(1, 1, 1901), Util.createDate(2, 2, 1902), $stringEdit);
    $listener1 = new StubListener<PropagatedEvent>();
    $listener2 = new StubListener<PropagatedEvent>();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner;
  private StubEditableDateBeed $editableDateBeed;
  private DateEdit $stringEdit;
  private DateEvent $event1;
  private StubListener<PropagatedEvent> $listener1;
  private StubListener<PropagatedEvent> $listener2;

  @Test
  public void constructor() {
    assertEquals($editableDateBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableDateBeed.publicUpdateDependents($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
  }

  @Test
  public void createInitialEvent() {
    DateEvent initialEvent = $editableDateBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $editableDateBeed);
    assertEquals(initialEvent.getOldValue(), null);
    assertTrue(initialEvent.getNewValue() == $editableDateBeed.get());
    assertEquals(initialEvent.getNewValue(), $editableDateBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }

}

