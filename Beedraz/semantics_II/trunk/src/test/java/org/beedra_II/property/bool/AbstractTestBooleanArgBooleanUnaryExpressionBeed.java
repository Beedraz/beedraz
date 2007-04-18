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
import org.beedra_II.property.AbstractTestUnaryExprBeed;
import org.junit.Assert;


public abstract class AbstractTestBooleanArgBooleanUnaryExpressionBeed<
                                      _UEB_ extends AbstractBooleanArgBooleanUnaryExpressionBeed>
    extends AbstractTestUnaryExprBeed<Boolean,
                                      BooleanEvent,
                                      Boolean,
                                      BooleanBeed,
                                      _UEB_,
                                      EditableBooleanBeed> {

  @Override
  protected void initGoals() {
    $goal1 = Boolean.TRUE;
    $goal2 = Boolean.FALSE;
    $goalMIN = Boolean.FALSE;
    $goalMAX = Boolean.FALSE;
  }

  @Override
  protected void changeOperand(EditableBooleanBeed editableOperandBeed, Boolean newValue) {
    try {
      BooleanEdit edit = new BooleanEdit(editableOperandBeed);
      edit.setGoal(newValue);
      edit.perform();
    }
    catch (Exception e) {
      Assert.fail();
    }
  }

  @Override
  protected EditableBooleanBeed createEditableOperandBeed(AggregateBeed owner) {
    return new EditableBooleanBeed(owner);
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
  protected Boolean valueFrom(BooleanBeed operandBeed) {
    return operandBeed.getBoolean();
  }


}
