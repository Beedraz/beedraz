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

package org.beedraz.semantics_II.expression.number.real.double64;


import static org.beedraz.semantics_II.expression.number.real.double64.DoubleBeeds.editableDoubleBeed;
import static org.junit.Assert.assertEquals;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.TestActualOldNewEvent;
import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.expression.number.real.RealEvent;
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
public class TestActualDoubleEvent
    extends TestActualOldNewEvent<Double, EditableDoubleBeed, ActualDoubleEvent> {

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
    DoubleBeed source = new EditableDoubleBeed(owner);
    // old and new value
    Double oldValue = 0.0;
    Double newValue = 1.0;
    // edit
    EditableDoubleBeed target = new EditableDoubleBeed(owner);
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
//
//  /**
//   * Combine two events with the same type and source, but whose states do not match.
//   */
//  @Test
//  public void createCombinedEvent1() throws IllegalEditException {
//    EditableDoubleBeed beed = editableDoubleBeed(5, new AbstractAggregateBeed() {/*NOP*/});
//    Double old1 = 4.0;
//    Double new1 = 4.1;
//    Double old2 = new1 + 1;
//    Double new2 = 5.1;
//    // create two events
//    ActualDoubleEvent event1 = new ActualDoubleEvent(beed, old1, new1, null);
//    ActualDoubleEvent event2 = new ActualDoubleEvent(beed, old2, new2, null);
//    // create a compoundEdit
//    CompoundEdit<EditableDoubleBeed, ActualDoubleEvent> compoundEdit =
//      new CompoundEdit<EditableDoubleBeed, ActualDoubleEvent>(beed);
//    try {
//      event1.combineWith(event2, compoundEdit);
//      // should not be reached
//      assertTrue(false);
//    }
//    catch (CannotCombineEventsException e) {
//      assertEquals(CannotCombineEventsException.Reason.INCOMPATIBLE_STATES, e.getReason());
//    }
//  }
//
//  /**
//   * Combine two events with the same type and source, and whose states match.
//   */
//  @Test
//  public void createCombinedEvent2() throws IllegalEditException, CannotCombineEventsException {
//    EditableDoubleBeed beed = editableDoubleBeed(5, new AbstractAggregateBeed() {/*NOP*/});
//    Double old = 4.0;
//    Double middle = old + 1;
//    Double newV = middle + 1;
//    // create two events
//    ActualDoubleEvent event1 = new ActualDoubleEvent(beed, old, middle, null);
//    ActualDoubleEvent event2 = new ActualDoubleEvent(beed, middle, newV, null);
//    // create a compoundEdit
//    CompoundEdit<EditableDoubleBeed, ActualDoubleEvent> compoundEdit =
//      new CompoundEdit<EditableDoubleBeed, ActualDoubleEvent>(beed);
//    Event combinedEvent = event1.combineWith(event2, compoundEdit);
//    assertEquals(event1.getClass(), combinedEvent.getClass());
//    ActualDoubleEvent combinedDoubleEvent = (ActualDoubleEvent) combinedEvent;
//    assertEquals(event1.getSource(), combinedDoubleEvent.getSource());
//    assertEquals(compoundEdit, combinedDoubleEvent.getEdit());
//    assertEquals(compoundEdit.getState(), combinedDoubleEvent.getEditState());
//    assertEquals(old, combinedDoubleEvent.getOldValue());
//    assertEquals(newV, combinedDoubleEvent.getNewValue());
//  }

  @Override
  protected ActualDoubleEvent createActualOldNewEvent(EditableDoubleBeed source, Double old, Double newV) {
    return new ActualDoubleEvent(source, old, newV, null);
  }

  @Override
  protected EditableDoubleBeed createSource() throws IllegalEditException {
    return editableDoubleBeed(5, new AbstractAggregateBeed() {/*NOP*/});
  }

  @Override
  protected Double oldValue() {
    return 1.1;
  }

  @Override
  protected Double middleValue() {
    return 2.2;
  }

  @Override
  protected Double newValue() {
    return 3.3;
  }

  @Override
  protected Double otherValue(Double value) {
    if (value == null) {
      return 1.0;
    }
    else {
      return value + 1;
    }
  }
}