package org.beedra_II.property.number.integer.long64;


import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MathUtil.castToBigInteger;
import static org.ppeew.smallfries_I.MathUtil.castToDouble;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

import org.beedra_II.property.simple.EditableSimplePropertyBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * @mudo break link with editable simple property beed and use long instead of Long internally.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableLongBeed
    extends EditableSimplePropertyBeed<Long, ActualLongEvent>
    implements LongBeed {

  /**
   */
  public EditableLongBeed() {
    super(null);
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

