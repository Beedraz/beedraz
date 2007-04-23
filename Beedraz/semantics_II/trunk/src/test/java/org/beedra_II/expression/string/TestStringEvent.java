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

package org.beedra_II.expression.string;


import static org.junit.Assert.assertEquals;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.expression.string.EditableStringBeed;
import org.beedra_II.expression.string.StringBeed;
import org.beedra_II.expression.string.StringEdit;
import org.beedra_II.expression.string.StringEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStringEvent {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  @Test
  public void constructor() throws EditStateException, IllegalEditException {
    // source
    AggregateBeed owner = new StubBeanBeed();
    StringBeed source = new EditableStringBeed(owner);
    // old and new value
    String oldValue = "old";
    String newValue = "new";
    // edit
    EditableStringBeed target = new EditableStringBeed(owner);
    StringEdit edit = new StringEdit(target);
    edit.perform();
    // test constructor
    StringEvent stringEvent = new StringEvent(source, oldValue, newValue, edit);
    assertEquals(stringEvent.getSource(), source);
    assertEquals(stringEvent.getOldValue(), oldValue);
    assertEquals(stringEvent.getNewValue(), newValue);
    assertEquals(stringEvent.getEdit(), edit);
    assertEquals(stringEvent.getEditState(), edit.getState());
    // old and new value are null
    oldValue = null;
    newValue = null;
    // test constructor
    stringEvent = new StringEvent(source, oldValue, newValue, edit);
    assertEquals(stringEvent.getSource(), source);
    assertEquals(stringEvent.getOldValue(), null);
    assertEquals(stringEvent.getNewValue(), null);
    assertEquals(stringEvent.getEdit(), edit);
    assertEquals(stringEvent.getEditState(), edit.getState());
    // edit is null
    edit = null;
    // test constructor
    stringEvent = new StringEvent(source, oldValue, newValue, edit);
    assertEquals(stringEvent.getSource(), source);
    assertEquals(stringEvent.getOldValue(), null);
    assertEquals(stringEvent.getNewValue(), null);
    assertEquals(stringEvent.getEdit(), null);
    assertEquals(stringEvent.getEditState(), null);
  }

}