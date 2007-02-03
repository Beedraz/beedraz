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


import java.util.HashMap;
import java.util.Map;

import org.beedra_II.Beed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


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
public class IntegerSumBeed
    extends AbstractPropertyBeed<IntegerEvent>
    implements IntegerBeed<IntegerEvent> {

  /**
   * @pre source != null;
   * @post get() == 0;
   * @post (forall IntegerBeed t) {! isTerm(t)};
   */
  public IntegerSumBeed(Beed<?> source) {
    super(source);
  }

  private class TermListener implements Listener<IntegerEvent> {

    public void beedChanged(IntegerEvent event) {
      // recalculate(); optimization
      Integer oldValue = $value;
      if ($value !=  null) {
        assert $value != null;
        assert event.getOldValue() != null :
          "event old value must be not null because all old terms were not null," +
          " because $value != null";
        $value = (event.getNewValue() == null) ? null : $value + event.getDelta();
      }
      else if ((event.getNewValue() != null) && (event.getOldValue() == null)) {
        recalculate();
      }
      // else: NOP
      fireChangeEvent(new FinalIntegerEvent(IntegerSumBeed.this, oldValue, null));
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
    synchronized (term) { // TODO is this correct?
      TermListener termListener = new TermListener();
      term.addListener(termListener);
      $terms.put(term, termListener);
      // recalculate(); optimization
      if ($value != null) {
        Integer oldValue = $value;
        $value = (term.get() == null) ? null : $value + term.get(); // MUDO overflow
        fireChangeEvent(new FinalIntegerEvent(this, oldValue, $value));
      }
      // otherwise, there is an existing null term; the new term cannot change null value
    }
  }

  /**
   * @post ! isTerm(term);
   */
  public final void removeTerm(IntegerBeed<?> term) {
    synchronized (term) { // TODO is this correct?
      TermListener termListener = $terms.get(term);
      if (termListener != null) {
        term.removeListener(termListener);
        $terms.remove(term);
        // recalculate(); optimization
        Integer oldValue = $value;
        if (term.get() == null) {
          /* $value was null because of this term, or because there are more terms
             we can't know, recalculate completely */
          recalculate();
        }
        else if ($value != null) { // old value and term not null, there is a new value, and it is old - term
          assert term.get() != null;
          $value -= term.get();
        }
        fireChangeEvent(new FinalIntegerEvent(this, oldValue, $value));
        /* else, term != null, but $value is null; this means there is another term that is null,
           and removing this term won't change that */
      }
      // else, term was not one of our terms
    }
  }

  /**
   * @invar $terms != null;
   * @invar Collections.noNull($terms);
   */
  private final Map<IntegerBeed<?>, TermListener> $terms = new HashMap<IntegerBeed<?>, TermListener>();

  public final Integer get() {
    return $value;
  }

  private void recalculate() {
    Integer newValue = 0;
    for (IntegerBeed<?> term : $terms.keySet()) {
      Integer termValue = term.get();
      if (termValue == null) {
        newValue = null;
        break;
      }
      newValue += termValue; // autoboxing
    }
  }

  private Integer $value = 0;

  @Override
  protected final IntegerEvent createInitialEvent() {
    return new FinalIntegerEvent(this, null, $value);
  }

}

