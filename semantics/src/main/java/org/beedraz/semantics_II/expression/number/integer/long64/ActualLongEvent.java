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
import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MathUtil.castToBigInteger;
import static org.ppeew.smallfries_I.MathUtil.castToDouble;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.beedraz.semantics_II.ActualOldNewEvent;
import org.beedraz.semantics_II.CompoundEdit;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.expression.number.integer.IntegerEvent;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * This is the event that is actually send by actual Double beeds.
 * Users should probably use the interface {@link DoubleBeed}.
 *
 * @author Jan Dockx
 *
 * @mudo should be package accesible; only public because it is used in a number of tests
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public final class ActualLongEvent
    extends ActualOldNewEvent<Long>
    implements IntegerEvent {

  /**
   * @pre  source != null;
   * @pre  (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @pre  (oldValue != null) && (newValue != null)
   *          ? ! oldValue.equals(newValue)
   *          : true;
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post oldValue == null || newValue == null
   *          ? getDelta() == null
   *          : getDelta() = newValue - oldValue;
   */
  public ActualLongEvent(LongBeed source, Long oldValue, Long newValue, Edit<?> edit) {
    super(source, oldValue, newValue, edit);
    $delta = ((oldValue == null) || (newValue == null)) ? null : newValue - oldValue; // MUDO overflow
  }

  public Long getNewLong() {
    return getNewValue();
  }

  public Double getNewDouble() {
    return castToDouble(getNewLong());
  }

  public BigInteger getNewBigInteger() {
    return castToBigInteger(getNewLong());
  }

  public BigDecimal getNewBigDecimal() {
    return castToBigDecimal(getNewLong());
  }

  public Long getOldLong() {
    return getOldValue();
  }

  public Double getOldDouble() {
    return castToDouble(getOldLong());
  }

  public BigInteger getOldBigInteger() {
    return castToBigInteger(getOldLong());
  }

  public BigDecimal getOldBigDecimal() {
    return castToBigDecimal(getOldLong());
  }

  public final Long getLongDelta() {
    return $delta;
  }

  public Double getDoubleDelta() {
    return castToDouble(getLongDelta());
  }

  public BigInteger getBigIntegerDelta() {
    return castToBigInteger(getLongDelta());
  }

  public BigDecimal getBigDecimalDelta() {
    return castToBigDecimal(getLongDelta());
  }

  private final Long $delta;

  @Override
  protected final ActualLongEvent safeCreateCombinedEvent(ActualOldNewEvent<Long> other, CompoundEdit<?, ?> compoundEdit) {
    return new ActualLongEvent((LongBeed)getSource(), getOldValue(), other.getNewValue(), compoundEdit);
  }

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", delta: " + getLongDelta();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "delta:" + getLongDelta() + "\n");
  }

}

