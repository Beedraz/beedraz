/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.property.decimal;

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



public class TestEditableDoubleBeed {

  public class MyEditableDoubleBeed extends EditableDoubleBeed {
    public MyEditableDoubleBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * fireChangeEvent is made public for testing reasons
     */
    public void fire(DoubleEvent event) {
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
    $editableDoubleBeed = new MyEditableDoubleBeed($owner);
    $stringEdit = new DoubleEdit($editableDoubleBeed);
    $stringEdit.perform();
    $event1 = new ActualDoubleEvent($editableDoubleBeed, new Double(0), new Double(1), $stringEdit);
    $listener1 = new PropagatedEventListener();
    $listener2 = new PropagatedEventListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner;
  private MyEditableDoubleBeed $editableDoubleBeed;
  private DoubleEdit $stringEdit;
  private DoubleEvent $event1;
  private PropagatedEventListener $listener1;
  private PropagatedEventListener $listener2;

  @Test
  public void constructor() {
    assertEquals($editableDoubleBeed.getOwner(), $owner);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableDoubleBeed.fire($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event1, $listener1.$event.getCause());
    assertEquals($event1, $listener1.$event.getCause());
  }

  @Test
  public void createInitialEvent() {
    DoubleEvent initialEvent = $editableDoubleBeed.createInitialEvent();
    assertEquals(initialEvent.getSource(), $editableDoubleBeed);
    assertEquals(initialEvent.getOldDouble(), null);
    assertEquals(initialEvent.getNewDouble(), $editableDoubleBeed.get());
    assertEquals(initialEvent.getEdit(), null);
  }

}

