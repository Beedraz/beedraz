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

package org.beedraz.semantics_II.expression.bool;


import static org.beedraz.semantics_II.expression.bool.BooleanBeeds.editableBooleanBeed;
import static org.junit.Assert.assertEquals;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.TestActualOldNewEvent;
import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
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
public class TestBooleanEvent
    extends TestActualOldNewEvent<Boolean, EditableBooleanBeed, BooleanEvent> {

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
    BooleanBeed source = new EditableBooleanBeed(owner);
    // old and new value
    Boolean oldValue = true;
    Boolean newValue = false;
    // edit
    EditableBooleanBeed target = new EditableBooleanBeed(owner);
    BooleanEdit edit = new BooleanEdit(target);
    edit.perform();
    // test constructor
    BooleanEvent booleanEvent = new BooleanEvent(source, oldValue, newValue, edit);
    assertEquals(booleanEvent.getSource(), source);
    assertEquals(booleanEvent.getOldValue(), oldValue);
    assertEquals(booleanEvent.getNewValue(), newValue);
    assertEquals(booleanEvent.getEdit(), edit);
    assertEquals(booleanEvent.getEditState(), edit.getState());
    // old and new value
    oldValue = null;
    newValue = null;
    // test constructor
    booleanEvent = new BooleanEvent(source, oldValue, newValue, edit);
    assertEquals(booleanEvent.getSource(), source);
    assertEquals(booleanEvent.getOldValue(), null);
    assertEquals(booleanEvent.getNewValue(), null);
    assertEquals(booleanEvent.getEdit(), edit);
    assertEquals(booleanEvent.getEditState(), edit.getState());
    // edit is null
    edit = null;
    // test constructor
    booleanEvent = new BooleanEvent(source, oldValue, newValue, edit);
    assertEquals(booleanEvent.getSource(), source);
    assertEquals(booleanEvent.getOldValue(), null);
    assertEquals(booleanEvent.getNewValue(), null);
    assertEquals(booleanEvent.getEdit(), null);
    assertEquals(booleanEvent.getEditState(), null);
 }

  @Override
  protected BooleanEvent createActualOldNewEvent(EditableBooleanBeed source, Boolean old, Boolean newV) {
    return new BooleanEvent(source, old, newV, null);
  }

  @Override
  protected EditableBooleanBeed createSource() throws IllegalEditException {
    return editableBooleanBeed(false, new AbstractAggregateBeed() {/*NOP*/});
  }

  @Override
  protected Boolean oldValue() {
    return false;
  }

  @Override
  protected Boolean middleValue() {
    return true;
  }

  @Override
  protected Boolean newValue() {
    return null;
  }

  @Override
  protected Boolean otherValue(Boolean value) {
    if (value == null) {
      return false;
    }
    else {
      return !value;
    }
  }

}