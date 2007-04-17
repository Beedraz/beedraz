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

package org.beedra_II.property.bool;

import org.beedra_II.StubListener;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.AbstractTestBinaryExprBeed;
import org.junit.Assert;


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
  protected void changeLeftArgument(EditableBooleanBeed editableArgumentBeed, Boolean newValue) {
    try {
      BooleanEdit edit = new BooleanEdit(editableArgumentBeed);
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
  protected void changeRightArgument(EditableBooleanBeed editableArgumentBeed, Boolean newValue) {
    changeLeftArgument(editableArgumentBeed, newValue);
  }

  @Override
  protected EditableBooleanBeed createEditableLeftArgumentBeed(AggregateBeed owner) {
    return new EditableBooleanBeed(owner);
  }

  @Override
  protected EditableBooleanBeed createEditableRightArgumentBeed(AggregateBeed owner) {
    return createEditableLeftArgumentBeed(owner);
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
  protected Boolean valueFromLeft(BooleanBeed argumentBeed) {
    return argumentBeed.getBoolean();
  }

  @Override
  protected Boolean valueFromRight(BooleanBeed argumentBeed) {
    return valueFromLeft(argumentBeed);
  }

}
