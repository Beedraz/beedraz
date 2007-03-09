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
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.event.Listener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestEditableDateBeed {

  public class MyEditableDateBeed extends EditableDateBeed {
    public MyEditableDateBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(DateEvent event) {
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
    $editableDateBeed = new MyEditableDateBeed($owner);
    $stringEdit = new DateEdit($editableDateBeed);
    $stringEdit.perform();
    $event1 = new DateEvent($editableDateBeed, Util.createDate(1, 1, 1901), Util.createDate(2, 2, 1902), $stringEdit);
    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner;
  private MyEditableDateBeed $editableDateBeed;
  private DateEdit $stringEdit;
  private DateEvent $event1;
  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;

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
    $editableDateBeed.fire($event1);
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

