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

package org.beedra_II.property.integer;


import static org.junit.Assert.assertEquals;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestIntegerEvent {

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
  public void constructor() throws EditStateException, IllegalEditException {
    // source
    AggregateBeed owner = new MyBeanBeed();
    IntegerBeed source = new EditableIntegerBeed(owner);
    // old and new value
    Integer oldValue = 0;
    Integer newValue = 1;
    // edit
    EditableIntegerBeed target = new EditableIntegerBeed(owner);
    IntegerEdit edit = new IntegerEdit(target);
    edit.perform();
    // test constructor
    IntegerEvent integerEvent = new IntegerEvent(source, oldValue, newValue, edit);
    assertEquals(integerEvent.getSource(), source);
    assertEquals(integerEvent.getOldValue(), oldValue);
    assertEquals(integerEvent.getNewValue(), newValue);
    assertEquals(integerEvent.getEdit(), edit);
    assertEquals(integerEvent.getEditState(), edit.getState());
    assertEquals(integerEvent.getDelta(), newValue - oldValue);
    // old and new value
    oldValue = null;
    newValue = 4;
    // test constructor
    integerEvent = new IntegerEvent(source, oldValue, newValue, edit);
    assertEquals(integerEvent.getSource(), source);
    assertEquals(integerEvent.getOldValue(), null);
    assertEquals(integerEvent.getNewValue(), 4);
    assertEquals(integerEvent.getEdit(), edit);
    assertEquals(integerEvent.getEditState(), edit.getState());
    assertEquals(integerEvent.getDelta(), null);
  }

}