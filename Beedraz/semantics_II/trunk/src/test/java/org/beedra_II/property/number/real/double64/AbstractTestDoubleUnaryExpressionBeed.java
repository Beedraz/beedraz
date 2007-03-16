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

package org.beedra_II.property.number.real.double64;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.number.AbstractUnaryExpressionBeed;
import org.beedra_II.property.number.AbstractTestUnaryExpressionBeed;
import org.junit.Assert;


public abstract class AbstractTestDoubleUnaryExpressionBeed<_UEB_ extends AbstractUnaryExpressionBeed<Double, DoubleBeed<DoubleEvent>, ?, DoubleEvent>>
    extends AbstractTestUnaryExpressionBeed<Double,
                                            DoubleBeed<DoubleEvent>,
                                            DoubleEvent,
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
  protected StubListener<DoubleEvent> createStubListener() {
    return new StubListener<DoubleEvent>();
  }

  @Override
  protected Double newValueFrom(DoubleEvent event) {
    return event.getNewDouble();
  }

  @Override
  protected Double oldValueFrom(DoubleEvent event) {
    return event.getOldDouble();
  }

  @Override
  protected Double valueFrom(DoubleBeed<DoubleEvent> argumentBeed) {
    return argumentBeed.getDouble();
  }


}
