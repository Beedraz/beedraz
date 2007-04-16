/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.property.date;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestEditableDateBeed {

  @Before
  public void setUp() throws Exception {
    $editableDateBeed = new StubEditableDateBeed();
    $stringEdit = new DateEdit($editableDateBeed);
    $stringEdit.perform();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private StubEditableDateBeed $editableDateBeed;
  private DateEdit $stringEdit;

  @Test
  public void constructor() {
    assertNull($editableDateBeed.getOwner());
    assertEquals(null, $editableDateBeed.get());
  }

}

