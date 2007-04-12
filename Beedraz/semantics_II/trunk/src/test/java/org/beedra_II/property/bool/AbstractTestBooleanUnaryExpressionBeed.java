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
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.double64.DoubleEdit;
import org.beedra_II.property.number.real.double64.EditableDoubleBeed;
import org.junit.Assert;


public abstract class AbstractTestBooleanUnaryExpressionBeed<_UEB_ extends AbstractBooleanUnaryExpressionBeed>
    extends AbstractTestUnaryExprBeed<Boolean,
                                      BooleanEvent,
                                      Double,
                                      RealBeed<?>,
                                      _UEB_,
                                      EditableDoubleBeed> {

  @Override
  protected void initGoals() {
    $goal1 = -Math.E;
    $goal2 = Double.NEGATIVE_INFINITY;
    $goalMIN = Double.MIN_VALUE; // - MIN_VALUE == MIN_VALUE (2-bit complement)
    $goalMAX = Double.MAX_VALUE;
  }

  @Override
  protected void changeArgument(EditableDoubleBeed editableArgumentBeed, Double newValue) {
    try {
      DoubleEdit edit = new DoubleEdit(editableArgumentBeed);
      edit.setGoal(newValue);
      edit.perform();
    }
    catch (Exception e) {
      Assert.fail();
    }
  }

  @Override
  protected EditableDoubleBeed createEditableArgumentBeed(AggregateBeed owner) {
    return new EditableDoubleBeed(owner);
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
  protected Double valueFrom(RealBeed<?> argumentBeed) {
    return argumentBeed.getDouble();
  }


}
