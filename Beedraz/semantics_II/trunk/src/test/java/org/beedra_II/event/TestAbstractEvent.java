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

package org.beedra_II.event;


import static org.junit.Assert.assertEquals;

import org.beedra_II.Beed;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerEdit;
import org.beedra_II.property.string.EditableStringBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractEvent {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  AggregateBeed $owner = new StubBeanBeed();
  EditableIntegerBeed $target = new EditableIntegerBeed($owner);
  IntegerEdit $edit = new IntegerEdit($target);
  Beed<?> $source = new EditableStringBeed($owner);
  Event $event;

  @Test
  public void constructor() throws EditStateException, IllegalEditException {
    // the state of the edit should be DONE or UNDONE
    Integer goal = 2;
    $edit.setGoal(goal);
    $edit.perform();
    // create a new event
    $event = new AbstractEvent($source, $edit) {
      // NOP
    };
    assertEquals($event.getSource(), $source);
    assertEquals($event.getEdit(), $edit);
    assertEquals($event.getEditState(), $edit.getState());
    // create a new event with an edit that is null
    $event = new AbstractEvent($source, null) {
      // NOP
    };
    assertEquals($event.getSource(), $source);
    assertEquals($event.getEdit(), null);
    assertEquals($event.getEditState(), null);
  }

}