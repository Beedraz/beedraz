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

package org.beedra_II.property.number.real.double64;


import static org.junit.Assert.assertEquals;

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.number.real.RealEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestActualDoubleEvent {

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
    DoubleBeed source = new EditableDoubleBeed();
    // old and new value
    Double oldValue = 0.0;
    Double newValue = 1.0;
    // edit
    EditableDoubleBeed target = new EditableDoubleBeed();
    DoubleEdit edit = new DoubleEdit(target);
    edit.perform();
    // test constructor
    RealEvent realEvent = new ActualDoubleEvent(source, oldValue, newValue, edit);
    assertEquals(realEvent.getSource(), source);
    assertEquals(realEvent.getOldDouble(), oldValue);
    assertEquals(realEvent.getNewDouble(), newValue);
    assertEquals(realEvent.getEdit(), edit);
    assertEquals(realEvent.getEditState(), edit.getState());
    assertEquals(realEvent.getDoubleDelta(), newValue - oldValue);
    // old and new value
    oldValue = null;
    newValue = 4.0;
    // test constructor
    realEvent = new ActualDoubleEvent(source, oldValue, newValue, edit);
    assertEquals(realEvent.getSource(), source);
    assertEquals(realEvent.getOldDouble(), null);
    assertEquals(realEvent.getNewDouble(), newValue);
    assertEquals(realEvent.getEdit(), edit);
    assertEquals(realEvent.getEditState(), edit.getState());
    assertEquals(realEvent.getDoubleDelta(), null);
    // edit is null
    edit = null;
    // test constructor
    realEvent = new ActualDoubleEvent(source, oldValue, newValue, edit);
    assertEquals(realEvent.getSource(), source);
    assertEquals(realEvent.getOldDouble(), null);
    assertEquals(realEvent.getNewDouble(), newValue);
    assertEquals(realEvent.getEdit(), null);
    assertEquals(realEvent.getEditState(), null);
  }

}