package org.beedra_II.expression.number.integer.long64;


import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MathUtil.castToBigInteger;
import static org.ppeew.smallfries_I.MathUtil.castToDouble;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.expression.EditableSimpleExpressionBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * @mudo break link with editable simple property beed and use long instead of Long internally.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableLongBeed
    extends EditableSimpleExpressionBeed<Long, ActualLongEvent>
    implements LongBeed {

  /**
   * @pre owner != null;
   * @post getOwner() == owner;
   */
  public EditableLongBeed(AggregateBeed owner) {
    super(owner);
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

  public long getlong() {
    return get().longValue();
  }

  public double getdouble() {
    return get().doubleValue();
  }

  public boolean isEffective() {
    return get() != null;
  }

  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append(numberFormat.format(get()));
  }
}

