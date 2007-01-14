package org.beedra_II.beed.integer;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedra_II.BeedraBean;
import org.beedra_II.beed.AbstractBeed;
import org.beedra_II.beed.BeedChangeEvent;
import org.beedra_II.beed.BeedChangeListener;
import org.toryt.util_I.annotations.vcs.CvsInfo;

import static org.beedra.util_I.Comparison.equalsWithNull;


/**
 * @invar (forall IntegerBeed ib: ! isTerm(ib)) ? get() == 0;
 *        list is empty, then 0 result, not null
 * @invar (exists IntegerBeed ib: isTerm(ib) && ib.get() == null) ? get() == null;
          null term results in null
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class IntegerSumBeed<_BeedraBean_ extends BeedraBean>
    extends AbstractBeed<_BeedraBean_, Integer>
    implements IntegerBeed<_BeedraBean_> {

  /**
   * @pre bean != null;
   * @pre Collections.noNull(terms);
   * @post get() == 0;
   */
  public IntegerSumBeed(_BeedraBean_ bean) {
    super(bean);
  }

  private class Listener implements BeedChangeListener<BeedChangeEvent<Integer>> {

    public void propertyChange(BeedChangeEvent<Integer> event) {
      recalculate();
      // MUDO optimization
    }

  }

  /**
   * @basic
   */
  public final boolean isTerm(IntegerBeed<?> term) {
    return $terms.keySet().contains(term);
  }

  /**
   * @pre term != null;
   * @post isTerm(term);
   */
  public final void addTerm(IntegerBeed<?> term) {
    assert term != null;
    synchronized (term) { // MUDO is this correct?
      Listener listener = new Listener();
      $terms.put(term, listener);
      // recalculate(); optimization
      if ($value != null) {
        if (term.get() == null) { // new value is null, and it is a change, because $value was not null
          Integer oldValue = $value;
          $value = null;
          fireChangeEvent(new BeedChangeEvent<Integer>(this, oldValue, null));
        }
        else { // old value and term not null, there is a new value, and it is old + term
          Integer oldValue = $value;
          $value += term.get();
          fireChangeEvent(new BeedChangeEvent<Integer>(this, oldValue, $value));
        }
      }
      // otherwise, there is an existing null term; the new term cannot change null value
    }
  }

  /**
   * @post ! isTerm(term);
   */
  public final void removeTerm(IntegerBeed<?> term) {
    synchronized (term) { // MUDO is this correct?
      Listener listener = $terms.get(term);
      term.removeChangeListener(listener);
      $terms.remove(term);
      // recalculate(); optimization
      if (term.get() == null) {
        /* $value was null because of this term, or because there are more terms
           we can't know, recalculate completely */
        recalculate();
      }
      else if ($value != null) { // old value and term not null, there is a new value, and it is old - term
        assert term.get() != null;
        Integer oldValue = $value;
        $value -= term.get();
        fireChangeEvent(new BeedChangeEvent<Integer>(this, oldValue, $value));
      }
      /* else, term != null, but $value is null; this means there is another term that is null,
         and removing this term won't change that */
    }
  }

  /**
   * @invar $terms != null;
   * @invar Collections.noNull($terms);
   */
  private final Map<IntegerBeed<?>, Listener> $terms = new HashMap<IntegerBeed<?>, Listener>();

  public Integer get() {
    return $value;
  }

  private void recalculate() {
    Integer newValue = 0;
    for (IntegerBeed<?> term : $terms.keySet()) {
      Integer termValue = term.get();
      if (termValue == null) {
        newValue = null;
      }
      newValue += termValue; // autoboxing
    }
    if (! equalsWithNull(newValue, $value)) {
      Integer oldValue = $value;
      $value = newValue;
      fireChangeEvent(new BeedChangeEvent<Integer>(this, oldValue, newValue));
    }
  }

  private Integer $value = 0;

}

