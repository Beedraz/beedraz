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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.AbstractTestBinaryExprBeed;
import org.junit.Assert;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractTestBooleanArgBooleanBinaryExpressionBeed<
                                       _UEB_ extends AbstractBooleanArgBooleanBinaryExpressionBeed>
    extends AbstractTestBinaryExprBeed<Boolean,
                                       BooleanEvent,
                                       Boolean,
                                       BooleanBeed,
                                       BooleanBeed,
                                       _UEB_,
                                       EditableBooleanBeed,
                                       EditableBooleanBeed> {

  @Override
  protected void initGoals() {
    $leftGoal1 = Boolean.FALSE;
    $leftGoal2 = Boolean.TRUE;
    $leftGoalMIN = Boolean.FALSE;
    $leftGoalMAX = Boolean.TRUE;
    $rightGoal1 = Boolean.TRUE;
    $rightGoal2 = Boolean.FALSE;
    $rightGoalMIN = Boolean.FALSE;
    $rightGoalMAX = Boolean.TRUE;
  }

  @Override
  protected void changeLeftOperand(EditableBooleanBeed editableOperandBeed, Boolean newValue) {
    try {
      BooleanEdit edit = new BooleanEdit(editableOperandBeed);
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
  protected void changeRightOperand(EditableBooleanBeed editableOperandBeed, Boolean newValue) {
    changeLeftOperand(editableOperandBeed, newValue);
  }

  @Override
  protected EditableBooleanBeed createEditableLeftOperandBeed(AggregateBeed owner) {
    return new EditableBooleanBeed(owner);
  }

  @Override
  protected EditableBooleanBeed createEditableRightOperandBeed(AggregateBeed owner) {
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
  protected Boolean valueFromLeft(BooleanBeed operandBeed) {
    return operandBeed.getBoolean();
  }

  @Override
  protected Boolean valueFromRight(BooleanBeed operandBeed) {
    return valueFromLeft(operandBeed);
  }

}
