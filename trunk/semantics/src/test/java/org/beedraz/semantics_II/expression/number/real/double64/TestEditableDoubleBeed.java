/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedraz.semantics_II.expression.number.real.double64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateEvent;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.expression.number.real.double64.ActualDoubleEvent;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleEdit;
import org.beedraz.semantics_II.expression.number.real.double64.EditableDoubleBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class TestEditableDoubleBeed {

  public class MyEditableDoubleBeed extends EditableDoubleBeed {
    public MyEditableDoubleBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(ActualDoubleEvent event) {
      updateDependents(event);
    }

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
    $listener1 = new StubListener<AggregateEvent>();
    $listener2 = new StubListener<AggregateEvent>();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner;
  private MyEditableDoubleBeed $editableDoubleBeed;
  private DoubleEdit $stringEdit;
  private ActualDoubleEvent $event1;
  private StubListener<AggregateEvent> $listener1;
  private StubListener<AggregateEvent> $listener2;

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
    $editableDoubleBeed.publicUpdateDependents($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentevents().size());
    assertEquals(1, $listener2.$event.getComponentevents().size());
    assertTrue($listener1.$event.getComponentevents().contains($event1));
    assertTrue($listener2.$event.getComponentevents().contains($event1));
  }

}

