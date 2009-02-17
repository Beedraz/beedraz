/*<license>
 Copyright 2007 - $Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.number.integer.long64;


import static org.beedraz.semantics_II.expression.number.integer.long64.LongBeeds.editableLongBeed;
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


@Copyright("2007 - $Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 886 $",
         date     = "$Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $")
public class TestActualLongEvent
    extends TestActualOldNewEvent<Long, EditableLongBeed, ActualLongEvent> {

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
    LongBeed source = new EditableLongBeed(owner);
    // old and new value
    Long oldValue = 0L;
    Long newValue = 1L;
    // edit
    EditableLongBeed target = new EditableLongBeed(owner);
    LongEdit edit = new LongEdit(target);
    edit.perform();
    // test constructor
    ActualLongEvent longEvent = new ActualLongEvent(source, oldValue, newValue, edit);
    assertEquals(longEvent.getSource(), source);
    assertEquals(longEvent.getOldLong(), oldValue);
    assertEquals(longEvent.getNewLong(), newValue);
    assertEquals(longEvent.getEdit(), edit);
    assertEquals(longEvent.getEditState(), edit.getState());
    assertEquals(longEvent.getLongDelta(), newValue - oldValue);
    // old and new value
    oldValue = null;
    newValue = 4L;
    // test constructor
    longEvent = new ActualLongEvent(source, oldValue, newValue, edit);
    assertEquals(longEvent.getSource(), source);
    assertEquals(longEvent.getOldLong(), null);
    assertEquals(longEvent.getNewLong(), newValue);
    assertEquals(longEvent.getEdit(), edit);
    assertEquals(longEvent.getEditState(), edit.getState());
    assertEquals(longEvent.getLongDelta(), null);
    // edit is null
    edit = null;
    // test constructor
    longEvent = new ActualLongEvent(source, oldValue, newValue, edit);
    assertEquals(longEvent.getSource(), source);
    assertEquals(longEvent.getOldLong(), null);
    assertEquals(longEvent.getNewLong(), newValue);
    assertEquals(longEvent.getEdit(), null);
    assertEquals(longEvent.getEditState(), null);
  }

  @Override
  protected ActualLongEvent createActualOldNewEvent(EditableLongBeed source, Long old, Long newV) {
    return new ActualLongEvent(source, old, newV, null);
  }

  @Override
  protected EditableLongBeed createSource() throws IllegalEditException {
    return editableLongBeed(5, new AbstractAggregateBeed() {/*NOP*/});
  }

  @Override
  protected Long oldValue() {
    return 1L;
  }

  @Override
  protected Long middleValue() {
    return 2L;
  }

  @Override
  protected Long newValue() {
    return 3L;
  }

  @Override
  protected Long otherValue(Long value) {
    if (value == null) {
      return 1L;
    }
    else {
      return value + 1;
    }
  }
}