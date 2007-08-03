/*<license>
 Copyright 2007 - $Date: 2007-05-11 01:03:09 +0200 (vr, 11 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.enumeration;


import static org.beedraz.semantics_II.expression.enumeration.EnumBeeds.editableEnumBeed;
import static org.junit.Assert.assertEquals;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.TestActualOldNewEvent;
import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-11 01:03:09 +0200 (vr, 11 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 884 $",
         date     = "$Date: 2007-05-11 01:03:09 +0200 (vr, 11 mei 2007) $")
public class TestEnumEvent
    extends TestActualOldNewEvent<Colors, EditableEnumBeed<Colors>, EnumEvent<Colors>> {

  @Test
  public void constructor() throws EditStateException, IllegalEditException {
    // source
    AggregateBeed owner = new StubBeanBeed();
    EnumBeed<Colors> source = editableEnumBeed(null, owner);
    // old and new value
    Colors oldValue = Colors.BLUE;
    Colors newValue = Colors.YELLOW;
    // edit
    EditableEnumBeed<Colors> target = editableEnumBeed(Colors.BLUE, owner);
    EnumEdit<Colors> edit = new EnumEdit<Colors>(target);
    edit.perform();
    // test constructor
    EnumEvent<Colors> enumEvent = new EnumEvent<Colors>(source, oldValue, newValue, edit);
    assertEquals(enumEvent.getSource(), source);
    assertEquals(enumEvent.getOldValue(), oldValue);
    assertEquals(enumEvent.getNewValue(), newValue);
    assertEquals(enumEvent.getEdit(), edit);
    assertEquals(enumEvent.getEditState(), edit.getState());
    // old and new value can be null
    oldValue = null;
    newValue = Colors.BLUE;
    // test constructor
    enumEvent = new EnumEvent<Colors>(source, oldValue, newValue, edit);
    assertEquals(enumEvent.getSource(), source);
    assertEquals(enumEvent.getOldValue(), null);
    assertEquals(enumEvent.getNewValue(), newValue);
    assertEquals(enumEvent.getEdit(), edit);
    assertEquals(enumEvent.getEditState(), edit.getState());
    // edit is null
    edit = null;
    // test constructor
    enumEvent = new EnumEvent<Colors>(source, oldValue, newValue, edit);
    assertEquals(enumEvent.getSource(), source);
    assertEquals(enumEvent.getOldValue(), null);
    assertEquals(enumEvent.getNewValue(), newValue);
    assertEquals(enumEvent.getEdit(), null);
    assertEquals(enumEvent.getEditState(), null);
  }

  @Override
  protected EnumEvent<Colors> createActualOldNewEvent(EditableEnumBeed<Colors> source, Colors old, Colors newV) {
    return new EnumEvent<Colors>(source, old, newV, null);
  }

  @Override
  protected EditableEnumBeed<Colors> createSource() throws IllegalEditException {
    return EnumBeeds.editableEnumBeed(Colors.RED, new AbstractAggregateBeed() {/*NOP*/});
  }

  @Override
  protected Colors oldValue() {
    return Colors.YELLOW;
  }

  @Override
  protected Colors middleValue() {
    return Colors.BLUE;
  }

  @Override
  protected Colors newValue() {
    return Colors.RED;
  }

  @Override
  protected Colors otherValue(Colors value) {
    if (value == null) {
      return Colors.BLUE;
    }
    else if (value == Colors.BLUE) {
      return Colors.RED;
    }
    else if (value == Colors.RED) {
      return Colors.YELLOW;
    }
    else if (value == Colors.YELLOW) {
      return Colors.BLUE;
    }
    else {
      assert false: "Shouldn't happen";
      return null;
    }
  }

}


enum Colors {
  RED, BLUE, YELLOW
}
