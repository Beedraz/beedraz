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

package org.beedraz.semantics_II;


import static org.beedraz.semantics_II.expression.bool.BooleanBeeds.editableBooleanBeed;
import static org.beedraz.semantics_II.expression.number.real.double64.DoubleBeeds.editableDoubleBeed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEdit;
import org.beedraz.semantics_II.expression.bool.BooleanEvent;
import org.beedraz.semantics_II.expression.bool.EditableBooleanBeed;
import org.beedraz.semantics_II.expression.enumeration.EditableEnumBeed;
import org.beedraz.semantics_II.expression.enumeration.EnumBeeds;
import org.beedraz.semantics_II.expression.enumeration.EnumEdit;
import org.beedraz.semantics_II.expression.enumeration.EnumEvent;
import org.beedraz.semantics_II.expression.number.integer.long64.EditableLongBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.LongEdit;
import org.beedraz.semantics_II.expression.number.real.double64.ActualDoubleEvent;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleEdit;
import org.beedraz.semantics_II.expression.number.real.double64.EditableDoubleBeed;
import org.beedraz.semantics_II.expression.string.EditableStringBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestAbstractEvent {

  public class MyAbstractEvent extends AbstractEvent {

    public MyAbstractEvent(Beed<?> source, Edit<?> edit) {
      super(source, edit);
    }

    @Override
    protected Event createCombinedEvent(Event other, CompoundEdit<?, ?> edit) {
      return new MyAbstractEvent(getSource(), edit);
    }

  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  AggregateBeed $owner = new StubBeanBeed();
  EditableLongBeed $target = new EditableLongBeed($owner);
  LongEdit $edit = new LongEdit($target);
  Beed<?> $source = new EditableStringBeed($owner);
  Event $event;

  @Test
  public void constructor() throws EditStateException, IllegalEditException {
    // the state of the edit should be DONE or UNDONE
    Long goal = 2L;
    $edit.setGoal(goal);
    $edit.perform();
    // create a new event
    $event = new MyAbstractEvent($source, $edit) {
      // NOP
    };
    assertEquals($event.getSource(), $source);
    assertEquals($event.getEdit(), $edit);
    assertEquals($event.getEditState(), $edit.getState());
    // create a new event with an edit that is null
    $event = new MyAbstractEvent($source, null) {
      // NOP
    };
    assertEquals($event.getSource(), $source);
    assertEquals($event.getEdit(), null);
    assertEquals($event.getEditState(), null);
  }

  /**
   * Try to combine events of a different type.
   */
  @Test
  public void combineWith1() throws IllegalEditException, EditStateException {
    // create a double event
    EditableDoubleBeed doubleBeed =
      editableDoubleBeed(5, new AbstractAggregateBeed() {/*NOP*/});
    DoubleEdit doubleEdit = new DoubleEdit(doubleBeed);
    ActualDoubleEvent doubleEvent = new ActualDoubleEvent(doubleBeed, 1.1, 2.2, doubleEdit);
    // create a boolean event
    EditableBooleanBeed booleanBeed =
      editableBooleanBeed(false, new AbstractAggregateBeed() {/*NOP*/});
    BooleanEdit booleanEdit = new BooleanEdit(booleanBeed);
    BooleanEvent booleanEvent = new BooleanEvent(booleanBeed, false, true, booleanEdit);
    // create a compoundEdit
    CompoundEdit<EditableDoubleBeed, ActualDoubleEvent> compoundEdit =
      new CompoundEdit<EditableDoubleBeed, ActualDoubleEvent>(doubleBeed);
    compoundEdit.addComponentEdit(doubleEdit);
    // combine
    try {
      doubleEvent.combineWith(booleanEvent, compoundEdit);
      // should not be reached
      assertTrue(false);
    }
    catch (CannotCombineEventsException e) {
      assertEquals(CannotCombineEventsException.Reason.DIFFERENT_TYPE, e.getReason());
    }
  }

  public enum Colors {
    RED,
    YELLOW,
    BLUE
  }

  public enum Sizes {
    SMALL,
    MEDIUM,
    LARGE
  }

  /**
   * Try to combine events of the same type but with different sources.
   * Remark: this method cannot distinguish two uses of a generic type with
   * a different generic parameter. It does not see that the types are different,
   * but it sees that the sources are different.
   */
  @Test
  public void combineWith2() throws IllegalEditException, EditStateException {
    // create an enum event
    EditableEnumBeed<Colors> colorBeed =
      EnumBeeds.editableEnumBeed(Colors.RED, new AbstractAggregateBeed() {/*NOP*/});
    EnumEdit<Colors> colorEdit = new EnumEdit<Colors>(colorBeed);
    EnumEvent<Colors> colorEvent = new EnumEvent<Colors>(colorBeed, Colors.RED, Colors.BLUE, colorEdit);
    // create another enum event
    EditableEnumBeed<Sizes> sizeBeed =
      EnumBeeds.editableEnumBeed(Sizes.SMALL, new AbstractAggregateBeed() {/*NOP*/});
    EnumEdit<Sizes> sizeEdit = new EnumEdit<Sizes>(sizeBeed);
    EnumEvent<Sizes> sizeEvent = new EnumEvent<Sizes>(sizeBeed, Sizes.SMALL, Sizes.SMALL, sizeEdit);
    // create a compoundEdit
    CompoundEdit<EditableEnumBeed<Colors>, EnumEvent<Colors>> compoundEdit =
      new CompoundEdit<EditableEnumBeed<Colors>, EnumEvent<Colors>>(colorBeed);
    // combine
    try {
      colorEvent.combineWith(sizeEvent, compoundEdit);
      // should not be reached
      assertTrue(false);
    }
    catch (CannotCombineEventsException e) {
      assertEquals(CannotCombineEventsException.Reason.DIFFERENT_SOURCE, e.getReason());
    }
  }

}