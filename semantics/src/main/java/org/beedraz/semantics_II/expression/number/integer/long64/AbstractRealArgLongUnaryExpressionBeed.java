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

package org.beedraz.semantics_II.expression.number.integer.long64;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.expression.number.AbstractRealArgUnaryExpressionBeed;
import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>General code for Intger implementations of {@link AbstractRealArgUnaryExpressionBeed}.</p>
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractRealArgLongUnaryExpressionBeed
    extends AbstractRealArgUnaryExpressionBeed<Long, ActualLongEvent, IntegerBeed<?>>
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
  protected final void recalculateFrom(IntegerBeed<?> operand) {
    $value = calculateValue(operand.getlong());
  }

  protected abstract long calculateValue(long operand);

}

