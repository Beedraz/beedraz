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
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.IntegerEvent;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.ppeew.annotations_I.vcs.CvsInfo;


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
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class LongSumBeed
    extends AbstractPropertyBeed<ActualLongEvent>
    implements LongBeed {

  /**
   * @pre   owner != null;
   * @post  getLong() == 0;
   * @post  (forall LongBeed lb; ; getNbOccurrences(lb) == 0};
   * @post  isEffective();
   */
  public LongSumBeed(AggregateBeed owner) {
    super(owner);
  }

  public final boolean isEffective() {
    return $effective;
  }

  private boolean $effective = true;


  private final Dependent<IntegerBeed<?>> $dependent =
    new AbstractUpdateSourceDependentDelegate<IntegerBeed<?>, ActualLongEvent>(this) {

    @Override
    protected ActualLongEvent filteredUpdate(Map<IntegerBeed<?>, Event> events) {
      // recalculate(); optimization
      boolean oldEffective = $effective;
      long oldValue = $value;
      if ($effective) {
        for (Map.Entry<IntegerBeed<?>, Event> entry : events.entrySet()) {
          IntegerEvent event = (IntegerEvent)entry.getValue();
          assert event.getOldLong() != null :
            "The old value contained in the event must be effective since $value != null.";
          if (event.getNewLong() == null) {
            $effective = false;
            break;
          }
          else {
            $value += event.getLongDelta() * getNrOfOccurences(entry.getKey());
            // MUDO in events long instead of Long too
          }
        }
      }
      else {
        recalculate();
      }
      if ((oldEffective != $effective) || (oldValue != $value)) {
        /* MUDO for now, we take the first edit we get, under the assumption that all events have
         * the same edit; with compound edits, we should gather different edits
         */
        assert events.size() > 0;
        Iterator<Event> iter = events.values().iterator();
        Event event = iter.next();
        Edit<?> edit = event.getEdit();
        return new ActualLongEvent(LongSumBeed.this, oldEffective ? oldValue : null, $effective ? $value : null, edit);
      }
      else {
        return null;
      }
    }

  };

  public final int getMaximumRootUpdateSourceDistance() {
    /* FIX FOR CONSTRUCTION PROBLEM
     * At construction, the super constructor is called with the future owner
     * of this property beed. Eventually, in the constructor code of AbstractPropertyBeed,
     * this object is registered as update source with the dependent of the
     * aggregate beed. During that registration process, the dependent
     * checks to see if we need to ++ our maximum root update source distance.
     * This involves a call to this method getMaximumRootUpdateSourceDistance.
     * Since however, we are still doing initialization in AbstractPropertyBeed,
     * initialization code (and construction code) further down is not yet executed.
     * This means that our $dependent is still null, and this results in a NullPointerException.
     * On the other hand, we cannot move the concept of $dependent up, since not all
     * property beeds have a dependent.
     * The fix implemented here is the following:
     * This problem only occurs during construction. During construction, we will
     * not have any update sources, so our maximum root update source distance is
     * effectively 0.
     */
    /*
     * TODO This only works if we add no update sources during construction,
     *      so a better solution should be sought.
     */
    return $dependent == null ? 0 : $dependent.getMaximumRootUpdateSourceDistance();
  }

  /**
   * @basic
   */
  public final int getNbOccurrences(IntegerBeed<?> term) {
    return $dependent.getNrOfOccurences(term);
  }

  /**
   * @pre   term != null;
   * @post  new.getNbOccurrences(term) == getNbOccurrences(term) + 1;
   */
  public final void addTerm(IntegerBeed<?> term) {
    assert term != null;
    $dependent.addUpdateSource(term);
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
    if ($dependent.getUpdateSourcesOccurencesMap().containsKey(term)) {
      $dependent.removeUpdateSource(term);
      // recalculate(); optimization
      boolean oldEffective = $effective;
      long oldValue = $value;
      /*
       * term.getLong() == null && getNbOccurrences() == 0  ==>  recalculate
       * term.getLong() == null && getNbOccurrences() > 0   ==>  new.$value == old.$value == null
       * term.getLong() != null && $value != null           ==>  new.$value == old.$value - term.getLong()
       * term.getLong() != null && $value == null           ==>  new.$value == old.$value == null
       */
      if (! term.isEffective() && getNbOccurrences(term) == 0) {
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
      if ((oldEffective != $effective) || (oldValue != $value)) {
        updateDependents(new ActualLongEvent(this, oldEffective ? oldValue : null, $effective ? $value : null, null));
      }
      /* else, term != null, but $value is null; this means there is another term that is null,
         and removing this term won't change that */
    }
    // else, term was not one of our terms: do nothing
  }

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
    @SuppressWarnings("cast")
    Map<IntegerBeed<?>, Integer> termMap = (Map<IntegerBeed<?>, Integer>)$dependent.getUpdateSourcesOccurencesMap();
    for (Map.Entry<IntegerBeed<?>, Integer> entry : termMap.entrySet()) {
      IntegerBeed<?> term = entry.getKey();
      if (! term.isEffective()) {
        $effective = false;
        return;
      }
      $value += entry.getKey().getlong() * entry.getValue();
    }
    $effective = true;
  }

  private long $value = 0L;

  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldLong() == null;
   * @post  result.getNewLong() == getLong();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final ActualLongEvent createInitialEvent() {
    return new ActualLongEvent(this, null, getLong(), null);
  }


  @Override
  protected String otherToStringInformation() {
    return getLong() + " (# " + $dependent.getUpdateSourcesSize() + ")";
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + getLong() + "\n");
    sb.append(indent(level + 1) + "number of terms:" + $dependent.getUpdateSourcesSize() + "\n");
  }

  public BigInteger getBigInteger() {
    return castToBigInteger(getLong());
  }

  public BigDecimal getBigDecimal() {
    return castToBigDecimal(getLong());
  }

}

