/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.property.number.integer.long64;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.StubListener;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.AggregateEvent;
import org.beedra_II.bean.StubBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestEditableLongBeed {

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $editableIntegerBeed = new StubEditableLongBeed();
    $stringEdit = new LongEdit($editableIntegerBeed);
    $stringEdit.perform();
    $event1 = new ActualLongEvent($editableIntegerBeed, 0L, 1L, $stringEdit);
    $listener1 = new StubListener<AggregateEvent>();
    $listener2 = new StubListener<AggregateEvent>();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner;
  private StubEditableLongBeed $editableIntegerBeed;
  private LongEdit $stringEdit;
  private ActualLongEvent $event1;
  private StubListener<AggregateEvent> $listener1;
  private StubListener<AggregateEvent> $listener2;

  @Test
  public void constructor() {
    assertEquals($editableIntegerBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableIntegerBeed.publicUpdateDependents($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentevents().size());
    assertEquals(1, $listener2.$event.getComponentevents().size());
    assertTrue($listener1.$event.getComponentevents().contains($event1));
    assertTrue($listener2.$event.getComponentevents().contains($event1));
  }

}

