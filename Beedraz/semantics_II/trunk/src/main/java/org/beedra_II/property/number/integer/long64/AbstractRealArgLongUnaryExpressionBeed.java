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


import org.beedra_II.edit.Edit;
import org.beedra_II.property.number.AbstractRealArgUnaryExpressionBeed;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.IntegerEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>General code for Intger implementations of {@link AbstractRealArgUnaryExpressionBeed}.</p>
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractRealArgLongUnaryExpressionBeed
    extends AbstractRealArgUnaryExpressionBeed<
                                        Long,
                                        ActualLongEvent,
                                        IntegerBeed<?>,
                                        IntegerEvent>
    implements LongBeed {

  public final long getlong() {
    return $value;
  }

  private long $value;

  @SuppressWarnings("cast")
  public double getdouble() {
    return (double)$value;
  }

  @Override
  public final Long get() {
    return getLong();
  }

  public final Long getLong() {
    return isEffective() ? Long.valueOf($value) : null;
  }

  @Override
  protected final ActualLongEvent createNewEvent(Long oldValue, Long newValue, Edit<?> edit) {
    return new ActualLongEvent(this, oldValue, newValue, edit);
  }

  @Override
  protected final void recalculateFrom(IntegerBeed<?> argument) {
    $value = calculateValue(argument.getlong());
  }

  protected abstract long calculateValue(long argument);

}

