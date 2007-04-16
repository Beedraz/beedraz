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

package org.beedra_II.property.bool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.beedra_II.bean.AbstractBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEditableBooleanBeed {

  public class MyEditableBooleanBeed extends EditableBooleanBeed {
    public MyEditableBooleanBeed() {
      super();
    }

    /**
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(BooleanEvent event) {
      updateDependents(event);
    }

  }

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    $editableBooleanBeed = new MyEditableBooleanBeed();
    $stringEdit = new BooleanEdit($editableBooleanBeed);
    $stringEdit.perform();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private MyEditableBooleanBeed $editableBooleanBeed;
  private BooleanEdit $stringEdit;

  @Test
  public void constructor() {
    assertNull($editableBooleanBeed.getOwner());
    assertEquals(null, $editableBooleanBeed.get());
  }

}
