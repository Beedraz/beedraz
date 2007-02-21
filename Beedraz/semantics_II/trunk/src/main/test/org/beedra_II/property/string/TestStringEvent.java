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

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.bean.AbstractBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStringEvent {

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  @Test
  public void constructor() {
    // source
    AggregateBeed owner = new MyBeanBeed();
    StringBeed source = new EditableStringBeed(owner);
    // old and new value
    String oldValue = "old";
    String newValue = "new";
    // edit
    EditableStringBeed target = new EditableStringBeed(owner);
    StringEdit edit = new StringEdit(target);
    // test constructor
    StringEvent stringEvent = new StringEvent(source, oldValue, newValue, edit);
    assertEquals(stringEvent.getSource(), source);
    assertEquals(stringEvent.getOldValue(), oldValue);
    assertEquals(stringEvent.getNewValue(), newValue);
    assertEquals(stringEvent.getEdit(), edit);
    assertEquals(stringEvent.getEditState(), edit.getState());
    // old and new value
    oldValue = null;
    newValue = null;
    // test constructor
    stringEvent = new StringEvent(source, oldValue, newValue, edit);
    assertEquals(stringEvent.getSource(), source);
    assertEquals(stringEvent.getOldValue(), null);
    assertEquals(stringEvent.getNewValue(), null);
    assertEquals(stringEvent.getEdit(), edit);
    assertEquals(stringEvent.getEditState(), edit.getState());
  }

}