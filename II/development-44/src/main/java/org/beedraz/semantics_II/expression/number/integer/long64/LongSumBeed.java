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
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.AbstractDependentExpressionBeed;
import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.beedraz.semantics_II.expression.number.integer.IntegerEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * A beed that is the sum of zero or more other beeds of type
 * {@link LongBeed}.
 *
 * @invar getNbOccurrences(null) == 0;
 * @invar (forall LongBeed lb; ; getNbOccurrences(lb) >= 0);
 * @invar (exists LongBeed lb; ; getNbOccurrences(lb) > 0 && lb.getLong() == null)
 *            ==> getLong() == null;
 *        If one of the terms is null, then the value of the sum beed is null.
 * @invar (forAll LongBeed lb; ; getNbOccurrences(lb) > 0 ==> lb.getLong() != null)
 *            ==> getLong() == sum { lb.getLong() * getNbOccurrences(lb) | lb instanceof LongBeed};
 *        If all terms are effective, then the value of the sum beed is the
 *        sum of the value of each term multiplied by the corresponding
 *        number of occurrences.
 *        e.g. getLong() = 3 * 5 + 2 * 11
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class LongSumBeed
    extends AbstractDependentExpressionBeed<ActualLongEvent>
    implements LongBeed {

  /**
   * @post  getLong() == 0;
   * @post  (forall LongBeed lb; ; getNbOccurrences(lb) == 0};
   * @post  isEffective();
   */
  public LongSumBeed() {
    this(null);
  }

  /**
   * @post  getLong() == 0;
   * @post  (forall LongBeed lb; ; getNbOccurrences(lb) == 0};
   * @post  isEffective();
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  public LongSumBeed(AggregateBeed owner) {
    super(owner);
  }

  public final boolean isEffective() {
    return $effective;
  }

  private boolean $effective = true;


  @Override
  protected ActualLongEvent filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    // recalculate(); optimization
    boolean oldEffective = $effective;
    long oldValue = $value;
    if ($effective) {
      for (Map.Entry<Beed<?>, Event> entry : events.entrySet()) {
        IntegerEvent event = (IntegerEvent)entry.getValue();
        assert event.getOldLong() != null :
          "The old value contained in the event must be effective since $value != null.";
        if (event.getNewLong() == null) {
          $effective = false;
          break;
        }
        else {
          $value += event.getLongDelta() * getNbOccurrences((IntegerBeed<?>)entry.getKey());
          // MUDO in events long instead of Long too
        }
      }
    }
    else {
      recalculate();
    }
    if ((oldEffective != $effective) || !MathUtil.equalPrimitiveValue(oldValue, $value)) {
      return new ActualLongEvent(
          LongSumBeed.this,
          oldEffective ? oldValue : null,
          $effective ? $value : null,
          edit);
    }
    else {
      return null;
    }
  }

  /**
   * @basic
   */
  public final int getNbOccurrences(IntegerBeed<?> term) {
    int count = 0;
    for (IntegerBeed<?> t : $terms) {
      if (t.equals(term)) {
        count++;
      }
    }
    return count;
  }

  /**
   * @pre   term != null;
   * @post  new.getNbOccurrences(term) == getNbOccurrences(term) + 1;
   */
  public final void addTerm(IntegerBeed<?> term) {
    assert term != null;
    addUpdateSource(term);
    $terms.add(term);
    // recalculate(); optimization
    if ($effective) {
      long oldValue = $value;
      if (! term.isEffective()) {
        $effective = false;
      }
      else {
        $value += term.getlong(); // MUDO overflow
      }
      if ((! $effective) || (term.getlong() != 0)) {
        updateDependents(new ActualLongEvent(this, oldValue, $effective ? $value : null, null));
      }
    }
    // otherwise, there is an existing null term; the new term cannot change null value
  }

  /**
   * @post  getNbOccurrences() > 0
   *          ? new.getNbOccurrences(term) == getNbOccurrences(term) - 1;
   *          : true;
   */
  public final void removeTerm(IntegerBeed<?> term) {
    if ($terms.contains(term)) {
      $terms.remove(term);
      if (! $terms.contains(term)) {
        removeUpdateSource(term);
      }
      // recalculate(); optimization
      boolean oldEffective = $effective;
      long oldValue = $value;
      /*
       * term.getLong() == null && getNbOccurrences() == 0  ==>  recalculate
       * term.getLong() == null && getNbOccurrences() > 0   ==>  new.$value == old.$value == null
       * term.getLong() != null && $value != null           ==>  new.$value == old.$value - term.getLong()
       * term.getLong() != null && $value == null           ==>  new.$value == old.$value == null
       */
      if ((! term.isEffective()) && (! $terms.contains(term))) {
          /* $value was null because of this term. After the remove,
           * the value can be null because of another term, or
           * can be some value: we can't know, recalculate completely
           */
         recalculate();
      }
      else if ($effective) {
        // since $value is effective, all terms are effective
        // the new value of the sum beed is the old value minus the value of the removed term
        assert term.isEffective();
        $value -= term.getlong();
      }
      // else: in all other cases, the value of $value is null, and stays null
      if ((oldEffective != $effective) || !MathUtil.equalPrimitiveValue(oldValue, $value)) {
        updateDependents(new ActualLongEvent(this, oldEffective ? oldValue : null, $effective ? $value : null, null));
      }
      /* else, term != null, but $value is null; this means there is another term that is null,
         and removing this term won't change that */
      // else, term was not one of our terms: do nothing
    }
  }

  private List<IntegerBeed<?>> $terms = new ArrayList<IntegerBeed<?>>();

  public final Double getDouble() {
    return $effective ? Double.valueOf($value) : null;
  }

  public final double getdouble() {
    return $value;
  }

  public final Long getLong() {
    return $effective ? Long.valueOf($value) : null;
  }

  public final long getlong() {
    return $value;
  }

  /**
   * The value of $value is recalculated. This is done by iterating over the terms.
   * When there are no terms, the result is zero.
   * When one of the terms is null, the result is null.
   * When all terms are effective, the result is the sum of the value of each term
   * multiplied by the corresponding number of occurrences.
   */
  public void recalculate() {
    $value = 0L;
    for (IntegerBeed<?> term : $terms) {
      if (! term.isEffective()) {
        $effective = false;
        return;
      }
      $value += term.getlong();
    }
    $effective = true;
  }

  private long $value = 0L;

  @Override
  protected String otherToStringInformation() {
    return getLong() + " (# " + $terms.size() + ")";
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + getLong() + "\n");
    sb.append(indent(level + 1) + "number of terms:" + $terms.size() + "\n");
  }

  public BigInteger getBigInteger() {
    return castToBigInteger(getLong());
  }

  public BigDecimal getBigDecimal() {
    return castToBigDecimal(getLong());
  }

  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    Iterator<IntegerBeed<?>> i = $terms.iterator();
    while (i.hasNext()) {
      IntegerBeed<?> argument = i.next();
      sb.append("(");
      if (depth == 1) {
        sb.append(numberFormat.format(argument.getdouble()));
      }
      else {
        argument.toStringDepth(sb, depth - 1, numberFormat);
      }
      sb.append(")" + getNbOccurrencesOperatorString() + getNbOccurrences(argument));
      if (i.hasNext()) {
        sb.append(getOperatorString());
      }
    }
  }

  public final String getOperatorString() {
    return "+";
  }

  public final String getNbOccurrencesOperatorString() {
    return "*";
  }
}

