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

package org.beedra_II.property.number.integer;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerBeed;
import org.beedra_II.property.integer.IntegerEdit;
import org.beedra_II.property.integer.IntegerEvent;
import org.beedra_II.property.number.AbstractUnaryExpressionBeed;
import org.beedra_II.property.number.AbstractTestUnaryExpressionBeed;
import org.junit.Assert;


public abstract class AbstractTestIntegerUnaryExpressionBeed<_UEB_ extends AbstractUnaryExpressionBeed<Integer, IntegerBeed, IntegerEvent>>
    extends AbstractTestUnaryExpressionBeed<Integer,
                                            IntegerBeed,
                                            IntegerEvent,
                                            _UEB_,
                                            EditableIntegerBeed> {

  @Override
  protected void initGoals() {
    $goal1 = 22;
    $goal2 = -33;
    $goalMIN = Integer.MIN_VALUE; // - MIN_VALUE == MIN_VALUE (2-bit complement)
    $goalMAX = Integer.MAX_VALUE;
  }

  @Override
  protected void changeArgument(EditableIntegerBeed editableArgumentBeed, Integer newValue) {
    try {
      IntegerEdit edit = new IntegerEdit(editableArgumentBeed);
      edit.setGoal(newValue);
      edit.perform();
    }
    catch (Exception e) {
      Assert.fail();
    }
  }

  @Override
  protected EditableIntegerBeed createEditableArgumentBeed(AggregateBeed owner) {
    return new EditableIntegerBeed(owner);
  }

  @Override
  protected StubListener<IntegerEvent> createStubListener() {
    return new StubListener<IntegerEvent>();
  }

  @Override
  protected Integer newValueFrom(IntegerEvent event) {
    return event.getNewInteger();
  }

  @Override
  protected Integer oldValueFrom(IntegerEvent event) {
    return event.getOldInteger();
  }

  @Override
  protected Integer valueFrom(IntegerBeed argumentBeed) {
    return argumentBeed.getInteger();
  }


}
