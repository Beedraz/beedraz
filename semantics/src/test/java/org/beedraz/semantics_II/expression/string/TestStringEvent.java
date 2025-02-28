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

package org.beedraz.semantics_II.expression.string;


import static org.junit.Assert.assertEquals;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.TestActualOldNewEvent;
import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestStringEvent
    extends TestActualOldNewEvent<String, EditableStringBeed, StringEvent> {

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

  @Override
  protected StringEvent createActualOldNewEvent(EditableStringBeed source, String old, String newV) {
    return new StringEvent(source, old, newV, null);
  }

  @Override
  protected EditableStringBeed createSource() throws IllegalEditException {
    return StringBeeds.editableStringBeed("some value", new AbstractAggregateBeed(){/*NOP*/});
  }

  @Override
  protected String oldValue() {
    return "old";
  }

  @Override
  protected String middleValue() {
    return "middle";
  }

  @Override
  protected String newValue() {
    return "new";
  }

  @Override
  protected String otherValue(String value) {
    if (value == null) {
      return "a";
    }
    else {
      return value + " x";
    }
  }

}