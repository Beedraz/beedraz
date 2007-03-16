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


import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MathUtil.castToBigInteger;
import static org.ppeew.smallfries_I.MathUtil.castToDouble;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.number.integer.IntegerEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * A beed that is the sum of zero or more other beeds of type
 * {@link LongBeed}.
 *
 * @invar getNbOccurrences(null) == 0;
 * @invar (forall LongBeed ib; ; getNbOccurrences(ib) >= 0);
 * @invar (exists LongBeed ib; ; getNbOccurrences(ib) > 0 && ib.getInteger() == null)
 *            ==> getInteger() == null;
 *        If one of the terms is null, then the value of the sum beed is null.
 * @invar (forAll LongBeed ib; ; getNbOccurrences(ib) > 0 ==> ib.getInteger() != null)
 *            ==> getInteger() == sum { ib.getInteger() * getNbOccurrences(ib) | ib instanceof LongBeed};
 *        If all terms are effective, then the value of the sum beed is the
 *        sum of the value of each term multiplied by the corresponding
 *        number of occurrences.
 *        e.g. getInteger() = 3 * 5 + 2 * 11
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class LongSumBeed
    extends AbstractPropertyBeed<ActualLongEvent>
    implements LongBeed {

  /**
   * @pre   owner != null;
   * @post  getInteger() == 0;
   * @post  (forall LongBeed ib; ; getNbOccurrences(ib) == 0};
   */
  public LongSumBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @invar  getNbOccurrences() > 0;
   */
  private class TermListener implements Listener<IntegerEvent> {

    /**
     * @post  getNbOccurrences() == 1;
     */
    public TermListener() {
      $nbOccurrences = 1;
    }

    public void beedChanged(IntegerEvent event) {
      // recalculate(); optimization
      Long oldValue = $value;
      if ($value !=  null) {
        assert $value != null;
        assert event.getOldLong() != null :
          "event old value must be not null because all old terms were not null," +
          " because $value != null";
        $value = (event.getNewLong() == null)
                     ? null
                     : $value + (event.getLongDelta() * getNbOccurrences());
      }
      else if ((event.getNewLong() != null) && (event.getOldLong() == null)) {
        recalculate();
      }
      // else: NOP
      if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new ActualLongEvent(LongSumBeed.this, oldValue, $value, event.getEdit()));
      }
    }

    public int getNbOccurrences() {
      return $nbOccurrences;
    }

    /**
     * @pre  nbOccurrences > 0;
     */
    public void setNbOccurrences(int nbOccurrences) {
      assert nbOccurrences > 0;
      $nbOccurrences = nbOccurrences;
    }

    public void increaseNbOccurrences() {
      $nbOccurrences += 1;
    }

    /**
     * @pre  nbOccurrences > 1;
     */
    public void decreaseNbOccurrences() {
      assert getNbOccurrences() > 1;
      $nbOccurrences -= 1;
    }

    private int $nbOccurrences;

  }

  /**
   * @basic
   */
  public final int getNbOccurrences(LongBeed term) {
    TermListener termListener = $terms.get(term);
    return termListener != null ? termListener.getNbOccurrences() : 0;
  }

  /**
   * @pre   term != null;
   * @post  new.getNbOccurrences(term) == getNbOccurrences(term) + 1;
   */
  public final void addTerm(LongBeed term) {
    assert term != null;
    synchronized (term) { // TODO is this correct?
      TermListener termListener = $terms.get(term);
      if (termListener != null) {
        assert getNbOccurrences(term) > 0;
        termListener.increaseNbOccurrences();
      }
      else {
        assert getNbOccurrences(term) == 0;
        termListener = new TermListener();
        term.addListener(termListener);
        $terms.put(term, termListener);
      }
      // recalculate(); optimization
      if ($value != null) {
        Long oldValue = $value;
        $value = (term.getLong() == null) ? null : $value + term.getLong(); // MUDO overflow
        fireChangeEvent(new ActualLongEvent(this, oldValue, $value, null));
      }
      // otherwise, there is an existing null term; the new term cannot change null value
    }
  }

  /**
   * @post  getNbOccurrences() > 0
   *          ? new.getNbOccurrences(term) == getNbOccurrences(term) - 1;
   *          : true;
   */
  public final void removeTerm(LongBeed term) {
    synchronized (term) { // TODO is this correct?
      TermListener termListener = $terms.get(term);
      if (termListener != null) {
        assert getNbOccurrences(term) > 0;
        if (getNbOccurrences(term) > 1) {
          termListener.decreaseNbOccurrences();
        }
        else {
          assert getNbOccurrences(term) == 1;
          term.removeListener(termListener);
          $terms.remove(term);
        }
        // recalculate(); optimization
        Long oldValue = $value;
        /*
         * term.getInteger() == null && getNbOccurrences() == 0  ==>  recalculate
         * term.getInteger() == null && getNbOccurrences() > 0   ==>  new.$value == old.$value == null
         * term.getInteger() != null && $value != null           ==>  new.$value == old.$value - term.getInteger()
         * term.getInteger() != null && $value == null           ==>  new.$value == old.$value == null
         */
        if (term.getLong() == null && getNbOccurrences(term) == 0) {
            /* $value was null because of this term. After the remove,
             * the value can be null because of another term, or
             * can be some value: we can't know, recalculate completely
             */
           recalculate();
        }
        else if ($value != null) {
          // since $value is effective, all terms are effective
          // the new value of the sum beed is the old value minus the value of the removed term
          assert term.getLong() != null;
          $value -= term.getLong();
        }
        // else: in all other cases, the value of $value is null, and stays null
        if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
          fireChangeEvent(new ActualLongEvent(this, oldValue, $value, null));
        }
        /* else, term != null, but $value is null; this means there is another term that is null,
           and removing this term won't change that */
      }
      // else, term was not one of our terms: do nothing
    }
  }

  /**
   * @invar $terms != null;
   * @invar Collections.noNull($terms);
   */
  private final Map<LongBeed, TermListener> $terms = new HashMap<LongBeed, TermListener>();

  public final Double getDouble() {
    return castToDouble(getLong());
  }

  public final Long getLong() {
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
    Long newValue = 0L;
    for (LongBeed term : $terms.keySet()) {
      Long termValue = term.getLong();
      if (termValue == null) {
        newValue = null;
        break;
      }
      newValue += termValue * getNbOccurrences(term); // autoboxing
    }
    $value = newValue;
  }

  private Long $value = 0L;

  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldInteger() == null;
   * @post  result.getNewInteger() == getInteger();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final ActualLongEvent createInitialEvent() {
    return new ActualLongEvent(this, null, getLong(), null);
  }


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

}

