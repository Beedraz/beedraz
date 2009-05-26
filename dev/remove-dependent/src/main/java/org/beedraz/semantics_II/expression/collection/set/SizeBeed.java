package org.beedraz.semantics_II.expression.collection.set;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;

import org.beedraz.semantics_II.AbstractDependentBeed;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.expression.number.integer.long64.ActualLongEvent;
import org.beedraz.semantics_II.expression.number.integer.long64.LongBeed;
import org.ppwcode.util.smallfries_I.MathUtil;

/**
 * A beed representing the size of this set beed.
 * All sets beeds should have a size beed.
 * The following idiom can be used:
 *
 * @invar  get() == EditableSetBeed.this.get().size();
 */
class SizeBeed
    extends AbstractDependentBeed<ActualLongEvent>
    implements LongBeed {

  /**
   * @invar $setBeed != null;
   */
  private final SetBeed<?, ?> $setBeed;

  /**
   * Creates a new SizeBeed. The {@code setBeed} is
   * an update source of this SizeBeed: when the set changes, the
   * size is updated accordingly.
   */
  SizeBeed(SetBeed<?, ?> setBeed) {
    $setBeed = setBeed;
    addUpdateSource($setBeed);
  }

  public final boolean isEffective() {
    return true;
  }

  @Override
  protected ActualLongEvent filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    // We only get events from our set beed, and that can only be 1
    assert events.size() == 1;
    Iterator<Event> iter = events.values().iterator();
    Event event = iter.next();
    SetEvent<?> setEvent = (SetEvent<?>)event;
    if (setEvent.getAddedElements().size() != setEvent.getRemovedElements().size()) {
      long oldSize = $size;
      $size += setEvent.getAddedElements().size();
      $size -= setEvent.getRemovedElements().size();
      assert $setBeed.get().size() == $size :
              "size in size beed should be " + $setBeed.get().size() +
              " but is " + $size;
      return new ActualLongEvent(this, oldSize, (long)$size, edit);
    }
    else { // the size didn't change
      assert $setBeed.get().size() == $size;
      return null;
    }
  }

  public final int get() {
    return $size;
  }

  private int $size = 0;

  public final long getlong() {
    return $size;
  }

  public BigInteger getBigInteger() {
    return MathUtil.castToBigInteger(get());
  }

  public Double getDouble() {
    return MathUtil.castToDouble(get());
  }

  public final double getdouble() {
    return $size;
  }

  public Long getLong() {
    return MathUtil.castToLong(get());
  }

  public BigDecimal getBigDecimal() {
    return MathUtil.castToBigDecimal(get());
  }

  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append(numberFormat.format(getdouble()));
  }

}