package org.beedraz.semantics_II.expression.number.integer.long64;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppwcode.util.smallfries_I.MathUtil.castToBigInteger;
import static org.ppwcode.util.smallfries_I.MathUtil.castToDouble;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.EditableSimpleExpressionBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * @mudo break link with editable simple property beed and use long instead of Long internally.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class EditableLongBeed
    extends EditableSimpleExpressionBeed<Long, ActualLongEvent>
    implements LongBeed {

  /**
   * @pre owner != null;
   * @post owner.registerAggregateElement(this);
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

