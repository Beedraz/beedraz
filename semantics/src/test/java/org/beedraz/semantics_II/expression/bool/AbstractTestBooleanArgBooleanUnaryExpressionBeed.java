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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.AbstractTestUnaryExprBeed;
import org.beedraz.semantics_II.expression.bool.AbstractBooleanArgBooleanUnaryExpressionBeed;
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEdit;
import org.beedraz.semantics_II.expression.bool.BooleanEvent;
import org.beedraz.semantics_II.expression.bool.EditableBooleanBeed;
import org.junit.Assert;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
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
