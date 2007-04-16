/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.property.number.real.double64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.beedra_II.bean.AbstractBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class TestEditableDoubleBeed {

  public class MyEditableDoubleBeed extends EditableDoubleBeed {
    public MyEditableDoubleBeed() {
      super();
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
    $editableDoubleBeed = new MyEditableDoubleBeed();
    $stringEdit = new DoubleEdit($editableDoubleBeed);
    $stringEdit.perform();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private MyEditableDoubleBeed $editableDoubleBeed;
  private DoubleEdit $stringEdit;

  @Test
  public void constructor() {
    assertNull($editableDoubleBeed.getOwner());
    assertEquals(null, $editableDoubleBeed.get());
  }

}

