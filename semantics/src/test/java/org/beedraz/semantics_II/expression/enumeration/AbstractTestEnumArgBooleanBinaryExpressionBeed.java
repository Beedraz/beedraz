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

package org.beedraz.semantics_II.expression.enumeration;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.AbstractTestBinaryExprBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEvent;
import org.junit.Assert;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-11 01:05:19 +0200 (vr, 11 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 885 $",
         date     = "$Date: 2007-05-11 01:05:19 +0200 (vr, 11 mei 2007) $")
public abstract class AbstractTestEnumArgBooleanBinaryExpressionBeed<
                                       _UEB_ extends AbstractEnumArgBooleanBinaryExpressionBeed<StubEnum>>
    extends AbstractTestBinaryExprBeed<Boolean,
                                       BooleanEvent,
                                       StubEnum,
                                       EnumBeed<StubEnum>,
                                       EnumBeed<StubEnum>,
                                       _UEB_,
                                       EditableEnumBeed<StubEnum>,
                                       EditableEnumBeed<StubEnum>> {

  @Override
  protected void initGoals() {
    $leftGoal1 = StubEnum.STUB_ENUM_VALUE_1;
    $leftGoal2 = StubEnum.STUB_ENUM_VALUE_2;
    $rightGoal1 = StubEnum.STUB_ENUM_VALUE_3;
    $rightGoal2 = StubEnum.STUB_ENUM_VALUE_4;
  }

  @Override
  protected void changeLeftOperand(EditableEnumBeed<StubEnum> editableOperandBeed, StubEnum newValue) {
    try {
      EnumEdit<StubEnum> edit = new EnumEdit<StubEnum>(editableOperandBeed);
      edit.setGoal(newValue);
      edit.perform();
    }
    catch (EditStateException exc) {
      Assert.fail();
    }
    catch (IllegalEditException exc) {
      Assert.fail();
    }
  }

  @Override
  protected void changeRightOperand(EditableEnumBeed<StubEnum> editableOperandBeed, StubEnum newValue) {
    changeLeftOperand(editableOperandBeed, newValue);
  }

  @Override
  protected EditableEnumBeed<StubEnum> createEditableLeftOperandBeed(AggregateBeed owner) {
    return new EditableEnumBeed<StubEnum>(owner);
  }

  @Override
  protected EditableEnumBeed<StubEnum> createEditableRightOperandBeed(AggregateBeed owner) {
    return createEditableLeftOperandBeed(owner);
  }

  @Override
  protected StubListener<BooleanEvent> createStubListener() {
    return new StubListener<BooleanEvent>();
  }

  @Override
  protected Boolean newValueFrom(BooleanEvent event) {
    return event.getNewValue();
  }

  @Override
  protected Boolean oldValueFrom(BooleanEvent event) {
    return event.getOldValue();
  }

  @Override
  protected StubEnum valueFromLeft(EnumBeed<StubEnum> operandBeed) {
    return operandBeed.get();
  }

  @Override
  protected StubEnum valueFromRight(EnumBeed<StubEnum> operandBeed) {
    return valueFromLeft(operandBeed);
  }

}
