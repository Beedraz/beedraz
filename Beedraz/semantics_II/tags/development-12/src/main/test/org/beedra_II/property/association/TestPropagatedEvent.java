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

package org.beedra_II.property.association;


import static org.junit.Assert.assertEquals;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.aggregate.PropagatedEvent;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Event;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerBeed;
import org.beedra_II.property.integer.IntegerEdit;
import org.beedra_II.property.integer.IntegerEvent;
import org.beedra_II.property.string.EditableStringBeed;
import org.beedra_II.property.string.StringBeed;
import org.beedra_II.property.string.StringEdit;
import org.beedra_II.property.string.StringEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPropagatedEvent {

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
    // event source
    AggregateBeed owner = new MyBeanBeed();
    IntegerBeed eventSource = new EditableIntegerBeed(owner);
    // old and new value
    Integer oldValue = 0;
    Integer newValue = 1;
    // edit
    EditableIntegerBeed target = new EditableIntegerBeed(owner);
    IntegerEdit edit = new IntegerEdit(target);
    edit.perform();
    // cause
    Event cause = new IntegerEvent(eventSource, oldValue, newValue, edit);
    // propagated event source
    AggregateBeed source = new MyBeanBeed();
    // test constructor
    PropagatedEvent propagatedEvent = new PropagatedEvent(source, cause);
    assertEquals(propagatedEvent.getSource(), source);
    assertEquals(propagatedEvent.getCause(), cause);
    assertEquals(propagatedEvent.getEdit(), cause.getEdit());
    assertEquals(propagatedEvent.getEditState(), cause.getEdit().getState());
  }

  @Test
  public void constructor2() throws EditStateException, IllegalEditException {
    // event source
    AggregateBeed owner = new MyBeanBeed();
    StringBeed eventSource = new EditableStringBeed(owner);
    // old and new value
    String oldValue = "0";
    String newValue = "1";
    // edit
    EditableStringBeed target = new EditableStringBeed(owner);
    StringEdit edit = new StringEdit(target);
    edit.perform();
    // cause
    Event cause = new StringEvent(eventSource, oldValue, newValue, edit);
    // propagated event source
    AggregateBeed source = new MyBeanBeed();
    // test constructor
    PropagatedEvent propagatedEvent = new PropagatedEvent(source, cause);
    assertEquals(propagatedEvent.getSource(), source);
    assertEquals(propagatedEvent.getCause(), cause);
    assertEquals(propagatedEvent.getEdit(), cause.getEdit());
    assertEquals(propagatedEvent.getEditState(), cause.getEdit().getState());
  }
}