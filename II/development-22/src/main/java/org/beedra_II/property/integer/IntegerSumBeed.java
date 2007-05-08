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

package org.beedra_II.property.integer;


import static org.ppeew.smallfries_I.MathUtil.castToDouble;

import java.util.HashMap;
import java.util.Map;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * A beed that is the sum of zero or more other beeds of type
 * {@link IntegerBeed}.
 *
 * @invar getNbOccurrences(null) == 0;
 * @invar (forall IntegerBeed ib; ; getNbOccurrences(ib) >= 0);
 * @invar (exists IntegerBeed ib; ; getNbOccurrences(ib) > 0 && ib.getInteger() == null)
 *            ==> getInteger() == null;
 *        If one of the terms is null, then the value of the sum beed is null.
 * @invar (forAll IntegerBeed ib; ; getNbOccurrences(ib) > 0 ==> ib.getInteger() != null)
 *            ==> getInteger() == sum { ib.getInteger() * getNbOccurrences(ib) | ib instanceof IntegerBeed};
 *        If all terms are effective, then the value of the sum beed is the
 *        sum of the value of each term multiplied by the corresponding
 *        number of occurrences.
 *        e.g. getInteger() = 3 * 5 + 2 * 11
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class IntegerSumBeed
    extends AbstractPropertyBeed<IntegerEvent>
    implements IntegerBeed {

  /**
   * @pre   owner != null;
   * @post  getInteger() == 0;
   * @post  (forall IntegerBeed ib; ; getNbOccurrences(ib) == 0};
   */
  public IntegerSumBeed(AggregateBeed owner) {
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
      Integer oldValue = $value;
      if ($value !=  null) {
        assert $value != null;
        assert event.getOldInteger() != null :
          "event old value must be not null because all old terms were not null," +
          " because $value != null";
        $value = (event.getNewInteger() == null)
                     ? null
                     : $value + (event.getIntegerDelta() * getNbOccurrences());
      }
      else if ((event.getNewInteger() != null) && (event.getOldInteger() == null)) {
        recalculate();
      }
      // else: NOP
      if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new ActualIntegerEvent(IntegerSumBeed.this, oldValue, $value, event.getEdit()));
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
  public final int getNbOccurrences(IntegerBeed term) {
    TermListener termListener = $terms.get(term);
    return termListener != null ? termListener.getNbOccurrences() : 0;
  }

  /**
   * @pre   term != null;
   * @post  new.getNbOccurrences(term) == getNbOccurrences(term) + 1;
   */
  public final void addTerm(IntegerBeed term) {
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
        Integer oldValue = $value;
        $value = (term.getInteger() == null) ? null : $value + term.getInteger(); // MUDO overflow
        fireChangeEvent(new ActualIntegerEvent(this, oldValue, $value, null));
      }
      // otherwise, there is an existing null term; the new term cannot change null value
    }
  }

  /**
   * @post  getNbOccurrences() > 0
   *          ? new.getNbOccurrences(term) == getNbOccurrences(term) - 1;
   *          : true;
   */
  public final void removeTerm(IntegerBeed term) {
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
        Integer oldValue = $value;
        /*
         * term.getInteger() == null && getNbOccurrences() == 0  ==>  recalculate
         * term.getInteger() == null && getNbOccurrences() > 0   ==>  new.$value == old.$value == null
         * term.getInteger() != null && $value != null           ==>  new.$value == old.$value - term.getInteger()
         * term.getInteger() != null && $value == null           ==>  new.$value == old.$value == null
         */
        if (term.getInteger() == null && getNbOccurrences(term) == 0) {
            /* $value was null because of this term. After the remove,
             * the value can be null because of another term, or
             * can be some value: we can't know, recalculate completely
             */
           recalculate();
        }
        else if ($value != null) {
          // since $value is effective, all terms are effective
          // the new value of the sum beed is the old value minus the value of the removed term
          assert term.getInteger() != null;
          $value -= term.getInteger();
        }
        // else: in all other cases, the value of $value is null, and stays null
        if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
          fireChangeEvent(new ActualIntegerEvent(this, oldValue, $value, null));
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
  private final Map<IntegerBeed, TermListener> $terms = new HashMap<IntegerBeed, TermListener>();

  public final Double getDouble() {
    return castToDouble(getInteger());
  }

  public final Integer getInteger() {
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
    Integer newValue = 0;
    for (IntegerBeed term : $terms.keySet()) {
      Integer termValue = term.getInteger();
      if (termValue == null) {
        newValue = null;
        break;
      }
      newValue += termValue * getNbOccurrences(term); // autoboxing
    }
    $value = newValue;
  }

  private Integer $value = 0;

  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldInteger() == null;
   * @post  result.getNewInteger() == getInteger();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final IntegerEvent createInitialEvent() {
    return new ActualIntegerEvent(this, null, getInteger(), null);
  }

}

