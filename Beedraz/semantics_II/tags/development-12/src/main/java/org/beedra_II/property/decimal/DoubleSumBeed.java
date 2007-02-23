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

package org.beedra_II.property.decimal;


import java.util.HashMap;
import java.util.Map;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A beed that is the sum of zero or more other beeds of type
 * {@link DoubleBeed}.
 *
 * @invar getNbOccurrences(null) == 0;
 * @invar (forall DoubleBeed ib; ; getNbOccurrences(ib) >= 0);
 * @invar (exists DoubleBeed ib; ; getNbOccurrences(ib) > 0 && ib.get() == null)
 *            ==> get() == null;
 *        If one of the terms is null, then the value of the sum beed is null.
 * @invar (forAll DoubleBeed ib; ; getNbOccurrences(ib) > 0 ==> ib.get() != null)
 *            ==> get() == sum { ib.get() * getNbOccurrences(ib) | ib instanceof DoubleBeed};
 *        If all terms are effective, then the value of the sum beed is the
 *        sum of the value of each term multiplied by the corresponding
 *        number of occurrences.
 *        e.g. get() = 3 * 5.2 + 2 * 11.3
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleSumBeed
    extends AbstractPropertyBeed<DoubleEvent>
    implements DoubleBeed {

  /**
   * @pre   owner != null;
   * @post  get() == 0;
   * @post  (forall DoubleBeed ib; ; getNbOccurrences(ib) == 0};
   */
  public DoubleSumBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @invar  getNbOccurrences() > 0;
   */
  private class TermListener implements Listener<DoubleEvent> {

    /**
     * @post  getNbOccurrences() == 1;
     */
    public TermListener() {
      $nbOccurrences = 1;
    }

    public void beedChanged(DoubleEvent event) {
      // recalculate(); optimization
      Double oldValue = $value;
      if ($value !=  null) {
        assert $value != null;
        assert event.getOldValue() != null :
          "event old value must be not null because all old terms were not null," +
          " because $value != null";
        $value = (event.getNewValue() == null)
                     ? null
                     : $value + (event.getDelta() * getNbOccurrences());
      }
      else if ((event.getNewValue() != null) && (event.getOldValue() == null)) {
        recalculate();
      }
      // else: NOP
      fireChangeEvent(new DoubleEvent(DoubleSumBeed.this, oldValue, $value, event.getEdit()));
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
  public final int getNbOccurrences(DoubleBeed term) {
    TermListener termListener = $terms.get(term);
    return termListener != null ? termListener.getNbOccurrences() : 0;
  }

  /**
   * @pre   term != null;
   * @post  new.getNbOccurrences(term) == getNbOccurrences(term) + 1;
   */
  public final void addTerm(DoubleBeed term) {
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
        Double oldValue = $value;
        $value = (term.get() == null) ? null : $value + term.get(); // MUDO overflow
        fireChangeEvent(new DoubleEvent(this, oldValue, $value, null));
      }
      // otherwise, there is an existing null term; the new term cannot change null value
    }
  }

  /**
   * @post  getNbOccurrences() > 0
   *          ? new.getNbOccurrences(term) == getNbOccurrences(term) - 1;
   *          : true;
   */
  public final void removeTerm(DoubleBeed term) {
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
        Double oldValue = $value;
        /**
         * term.get() == null && getNbOccurrences() == 0  ==>  recalculate
         * term.get() == null && getNbOccurrences() > 0   ==>  new.$value == old.$value == null
         * term.get() != null && $value != null           ==>  new.$value == old.$value - term.get()
         * term.get() != null && $value == null           ==>  new.$value == old.$value == null
         */
        if (term.get() == null && getNbOccurrences(term) == 0) {
            /* $value was null because of this term. After the remove,
             * the value can be null because of another term, or
             * can be some value: we can't know, recalculate completely
             */
           recalculate();
        }
        else if ($value != null) {
          // since $value is effective, all terms are effective
          // the new value of the sum beed is the old value minus the value of the removed term
          assert term.get() != null;
          $value -= term.get();
        }
        // else: in all other cases, the value of $value is null, and stays null
        fireChangeEvent(new DoubleEvent(this, oldValue, $value, null));
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
  private final Map<DoubleBeed, TermListener> $terms = new HashMap<DoubleBeed, TermListener>();

  public final Double get() {
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
    Double newValue = 0.0;
    for (DoubleBeed term : $terms.keySet()) {
      Double termValue = term.get();
      if (termValue == null) {
        newValue = null;
        break;
      }
      newValue += termValue * getNbOccurrences(term); // autoboxing
    }
    $value = newValue;
  }

  private Double $value = 0.0;

  @Override
  protected final DoubleEvent createInitialEvent() {
    return new DoubleEvent(this, null, $value, null);
  }

}

