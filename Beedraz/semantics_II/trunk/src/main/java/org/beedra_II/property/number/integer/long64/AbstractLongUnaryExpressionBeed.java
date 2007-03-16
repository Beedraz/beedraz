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


import static org.ppeew.smallfries_I.MathUtil.castToDouble;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.property.number.AbstractUnaryExpressionBeed;
import org.beedra_II.property.number.integer.IntegerEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>General code for Intger implementations of {@link AbstractUnaryExpressionBeed}.</p>
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractLongUnaryExpressionBeed
    extends AbstractUnaryExpressionBeed<Long, LongBeed, IntegerEvent, IntegerEvent>
    implements LongBeed {

  /**
   * @pre   owner != null;
   * @post  getInteger() == null;
   * @post  getArgument() == null;
   */
  public AbstractLongUnaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  public final Double getDouble() {
    return castToDouble(getLong());
  }

  public final Long getLong() {
    return get();
  }

  /**
   * @post  result != null;
   * @post  result.getArgument() == this;
   * @post  result.getOldInteger() == null;
   * @post  result.getNewInteger() == getInteger();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final IntegerEvent createInitialEvent() {
    return new ActualLongEvent(this, null, getLong(), null);
  }

  @Override
  protected final IntegerEvent createNewEvent(Long oldValue, Long newValue, Edit<?> edit) {
    return new ActualLongEvent(this, oldValue, newValue, edit);
  }

  @Override
  protected final Long newValueFrom(IntegerEvent event) {
    return event.getNewLong();
  }

  @Override
  protected final Long valueFrom(LongBeed beed) {
    return beed.getLong();
  }

}

