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

package org.beedraz.semantics_II;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 886 $",
         date     = "$Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $")
public abstract class TestActualOldNewEvent<_Type_,
                                            _Source_ extends AbstractBeed<_SourceEvent_>,
                                            _SourceEvent_ extends Event> {

  /**
   * Combine two events with the same type and source, but whose states do not match.
   */
  @Test
  public void createCombinedEvent1() throws IllegalEditException {
    _Source_ source = createSource();
    _Type_ old = oldValue();
    _Type_ middle = middleValue();
    _Type_ otherMiddle = otherValue(middle);
    _Type_ newV = newValue();
    // create two events
    ActualOldNewEvent<_Type_> event1 = createActualOldNewEvent(source, old, middle);
    ActualOldNewEvent<_Type_> event2 = createActualOldNewEvent(source, otherMiddle, newV);
    // create a compoundEdit
    CompoundEdit<_Source_, _SourceEvent_> compoundEdit =
      new CompoundEdit<_Source_, _SourceEvent_>(source);
    try {
      event1.combineWith(event2, compoundEdit);
      // should not be reached
      assertTrue(false);
    }
    catch (CannotCombineEventsException e) {
      assertEquals(CannotCombineEventsException.Reason.INCOMPATIBLE_STATES, e.getReason());
    }
  }

  protected abstract _Source_ createSource() throws IllegalEditException;
  protected abstract _Type_ oldValue();
  protected abstract _Type_ middleValue();
  protected abstract _Type_ otherValue(_Type_ value);
  protected abstract _Type_ newValue();
  protected abstract ActualOldNewEvent<_Type_> createActualOldNewEvent(_Source_ source, _Type_ old, _Type_ newV);

  /**
   * Combine two events with the same type and source, and whose states match.
   */
  @Test
  public void createCombinedEvent2() throws IllegalEditException, CannotCombineEventsException {
    _Source_ source = createSource();
    _Type_ old = oldValue();
    _Type_ middle = otherValue(old);
    _Type_ newV = otherValue(middle);
    // create two events
    ActualOldNewEvent<_Type_> event1 = createActualOldNewEvent(source, old, middle);
    ActualOldNewEvent<_Type_> event2 = createActualOldNewEvent(source, middle, newV);
    // create a compoundEdit
    CompoundEdit<_Source_, _SourceEvent_> compoundEdit =
      new CompoundEdit<_Source_, _SourceEvent_>(source);
    Event combinedEvent = event1.combineWith(event2, compoundEdit);
    assertEquals(event1.getClass(), combinedEvent.getClass());
    @SuppressWarnings("unchecked")
    ActualOldNewEvent<_Type_> combinedDoubleEvent =
      (ActualOldNewEvent<_Type_>) combinedEvent;
    assertEquals(event1.getSource(), combinedDoubleEvent.getSource());
    assertEquals(compoundEdit, combinedDoubleEvent.getEdit());
    assertEquals(compoundEdit.getState(), combinedDoubleEvent.getEditState());
    assertEquals(old, combinedDoubleEvent.getOldValue());
    assertEquals(newV, combinedDoubleEvent.getNewValue());
  }
}