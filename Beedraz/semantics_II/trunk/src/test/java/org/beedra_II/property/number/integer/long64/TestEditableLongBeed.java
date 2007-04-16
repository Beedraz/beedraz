/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.property.number.integer.long64;


import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestEditableLongBeed {

  @Before
  public void setUp() throws Exception {
    $editableIntegerBeed = new StubEditableLongBeed();
    $stringEdit = new LongEdit($editableIntegerBeed);
    $stringEdit.perform();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private StubEditableLongBeed $editableIntegerBeed;
  private LongEdit $stringEdit;

  @Test
  public void constructor() {
    assertNull($editableIntegerBeed.getOwner());
  }

}

