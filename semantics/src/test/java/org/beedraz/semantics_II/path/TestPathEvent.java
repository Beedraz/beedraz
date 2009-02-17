/*<license>
 Copyright 2007 - $Date: 2007-05-11 01:05:19 +0200 (vr, 11 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.path;


import static org.beedraz.semantics_II.expression.string.StringBeeds.editableStringBeed;
import static org.junit.Assert.assertEquals;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.TestActualOldNewEvent;
import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEdit;
import org.beedraz.semantics_II.expression.bool.BooleanEvent;
import org.beedraz.semantics_II.expression.bool.EditableBooleanBeed;
import org.beedraz.semantics_II.expression.string.StringBeed;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-11 01:05:19 +0200 (vr, 11 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 885 $",
         date     = "$Date: 2007-05-11 01:05:19 +0200 (vr, 11 mei 2007) $")
public class TestPathEvent
    extends TestActualOldNewEvent<StringBeed, MutablePath<StringBeed>, PathEvent<StringBeed>> {

  public class MyBeanBeed extends AbstractBeanBeed {
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
  protected PathEvent<StringBeed> createActualOldNewEvent(MutablePath<StringBeed> source, StringBeed old, StringBeed newV) {
    return new PathEvent<StringBeed>(source, old, newV, null);
  }

  @Override
  protected MutablePath<StringBeed> createSource() throws IllegalEditException {
    return new MutablePath<StringBeed>(
        editableStringBeed("some value", new AbstractAggregateBeed() {/*NOP*/}));
  }

  @Override
  protected StringBeed oldValue() {
    try {
      return editableStringBeed("old", new AbstractAggregateBeed() {/*NOP*/});
    }
    catch (IllegalEditException e) {
      assert false: "Shouldn't happen";
      return null;
    }
  }

  @Override
  protected StringBeed middleValue() {
    try {
      return editableStringBeed("middle", new AbstractAggregateBeed() {/*NOP*/});
    }
    catch (IllegalEditException e) {
      assert false: "Shouldn't happen";
      return null;
    }
  }

  @Override
  protected StringBeed newValue() {
    try {
      return editableStringBeed("new", new AbstractAggregateBeed() {/*NOP*/});
    }
    catch (IllegalEditException e) {
      assert false: "Shouldn't happen";
      return null;
    }
  }

  @Override
  protected StringBeed otherValue(StringBeed value) {
    try {
      if (value == null || value.get() == null) {
        return editableStringBeed("some value", new AbstractAggregateBeed() {/*NOP*/});
      }
      else {
        return editableStringBeed(value.get() + " x", new AbstractAggregateBeed() {/*NOP*/});
      }
    }
    catch(IllegalEditException exc) {
      assert false: "Shouldn't happen";
      return null;
    }
  }


}