package org.beedra_II.property.number.integer.long64;


import static org.ppeew.smallfries_I.MathUtil.castToDouble;
import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MathUtil.castToBigInteger;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.number.integer.IntegerEvent;
import org.beedra_II.property.simple.EditableSimplePropertyBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableLongBeed
    extends EditableSimplePropertyBeed<Long, IntegerEvent>
    implements LongBeed {

  /**
   * @pre owner != null;
   */
  public EditableLongBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldInteger() == null;
   * @post  result.getNewInteger() == get();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected IntegerEvent createInitialEvent() {
    return new ActualLongEvent(this, null, get(), null);
  }

  public Double getDouble() {
    return castToDouble(getLong());
  }

  public Long getLong() {
    return get();
  }

  public BigInteger getBigInteger() {
    return castToBigInteger(getLong());
  }

  public BigDecimal getBigDecimal() {
    return castToBigDecimal(getLong());
  }

}

