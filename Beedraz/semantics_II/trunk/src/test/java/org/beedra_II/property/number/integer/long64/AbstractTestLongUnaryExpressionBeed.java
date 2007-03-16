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
import org.beedra_II.property.number.integer.IntegerEvent;
import org.beedra_II.property.number.integer.long64.EditableLongBeed;
import org.beedra_II.property.number.integer.long64.LongBeed;
import org.beedra_II.property.number.integer.long64.LongEdit;
import org.junit.Assert;


public abstract class AbstractTestLongUnaryExpressionBeed<_UEB_ extends AbstractUnaryExpressionBeed<Long, LongBeed, ?, IntegerEvent>>
    extends AbstractTestUnaryExpressionBeed<Long,
                                            LongBeed,
                                            IntegerEvent,
                                            _UEB_,
                                            EditableLongBeed> {

  @Override
  protected void initGoals() {
    $goal1 = 22L;
    $goal2 = -33L;
    $goalMIN = Long.MIN_VALUE; // - MIN_VALUE == MIN_VALUE (2-bit complement)
    $goalMAX = Long.MAX_VALUE;
  }

  @Override
  protected void changeArgument(EditableLongBeed editableArgumentBeed, Long newValue) {
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
  protected StubListener<IntegerEvent> createStubListener() {
    return new StubListener<IntegerEvent>();
  }

  @Override
  protected Long newValueFrom(IntegerEvent event) {
    return event.getNewLong();
  }

  @Override
  protected Long oldValueFrom(IntegerEvent event) {
    return event.getOldLong();
  }

  @Override
  protected Long valueFrom(LongBeed argumentBeed) {
    return argumentBeed.getLong();
  }


}
