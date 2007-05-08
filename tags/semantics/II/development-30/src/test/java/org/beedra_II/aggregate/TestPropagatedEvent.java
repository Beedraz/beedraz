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

package org.beedra_II.aggregate;


import static org.junit.Assert.assertEquals;

import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.Event;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
import org.beedra_II.property.number.integer.long64.EditableLongBeed;
import org.beedra_II.property.number.integer.long64.LongBeed;
import org.beedra_II.property.number.integer.long64.LongEdit;
import org.beedra_II.property.string.EditableStringBeed;
import org.beedra_II.property.string.StringBeed;
import org.beedra_II.property.string.StringEdit;
import org.beedra_II.property.string.StringEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPropagatedEvent {

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
    AggregateBeed owner = new StubAggregateBeed();
    LongBeed eventSource = new EditableLongBeed(owner);
    // old and new value
    Long oldValue = 0L;
    Long newValue = 1L;
    // edit
    EditableLongBeed target = new EditableLongBeed(owner);
    LongEdit edit = new LongEdit(target);
    edit.perform();
    // cause
    Event cause = new ActualLongEvent(eventSource, oldValue, newValue, edit);
    // propagated event source
    AggregateBeed source = new StubAggregateBeed();
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
    AggregateBeed owner = new StubAggregateBeed();
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
    AggregateBeed source = new StubAggregateBeed();
    // test constructor
    PropagatedEvent propagatedEvent = new PropagatedEvent(source, cause);
    assertEquals(propagatedEvent.getSource(), source);
    assertEquals(propagatedEvent.getCause(), cause);
    assertEquals(propagatedEvent.getEdit(), cause.getEdit());
    assertEquals(propagatedEvent.getEditState(), cause.getEdit().getState());
  }
}