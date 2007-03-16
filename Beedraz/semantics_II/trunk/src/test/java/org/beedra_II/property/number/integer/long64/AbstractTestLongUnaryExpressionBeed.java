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

package org.beedra_II.property.number.integer.long64;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.StubListener;
import org.beedra_II.property.number.AbstractUnaryExpressionBeed;
import org.beedra_II.property.number.AbstractTestUnaryExpressionBeed;
import org.beedra_II.property.number.integer.long64.EditableLongBeed;
import org.beedra_II.property.number.integer.long64.LongBeed;
import org.beedra_II.property.number.integer.long64.LongEdit;
import org.beedra_II.property.number.integer.long64.LongEvent;
import org.junit.Assert;


public abstract class AbstractTestLongUnaryExpressionBeed<_UEB_ extends AbstractUnaryExpressionBeed<Integer, LongBeed, LongEvent>>
    extends AbstractTestUnaryExpressionBeed<Integer,
                                            LongBeed,
                                            LongEvent,
                                            _UEB_,
                                            EditableLongBeed> {

  @Override
  protected void initGoals() {
    $goal1 = 22;
    $goal2 = -33;
    $goalMIN = Integer.MIN_VALUE; // - MIN_VALUE == MIN_VALUE (2-bit complement)
    $goalMAX = Integer.MAX_VALUE;
  }

  @Override
  protected void changeArgument(EditableLongBeed editableArgumentBeed, Integer newValue) {
    try {
      LongEdit edit = new LongEdit(editableArgumentBeed);
      edit.setGoal(newValue);
      edit.perform();
    }
    catch (Exception e) {
      Assert.fail();
    }
  }

  @Override
  protected EditableLongBeed createEditableArgumentBeed(AggregateBeed owner) {
    return new EditableLongBeed(owner);
  }

  @Override
  protected StubListener<LongEvent> createStubListener() {
    return new StubListener<LongEvent>();
  }

  @Override
  protected Integer newValueFrom(LongEvent event) {
    return event.getNewInteger();
  }

  @Override
  protected Integer oldValueFrom(LongEvent event) {
    return event.getOldInteger();
  }

  @Override
  protected Integer valueFrom(LongBeed argumentBeed) {
    return argumentBeed.getInteger();
  }


}
