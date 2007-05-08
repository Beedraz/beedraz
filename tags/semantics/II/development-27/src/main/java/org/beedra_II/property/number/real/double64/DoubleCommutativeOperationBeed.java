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

package org.beedra_II.property.number.real.double64;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.HashMap;
import java.util.Map;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.RealEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;
import org.ppeew.smallfries_I.MathUtil;


/**
 * A beed that performs a commutative arithmetic operation on zero or more
 * beeds of type {@link DoubleBeed}.
 *
 * @invar getNbOccurrences(null) == 0;
 * @invar (forall DoubleBeed db; ; getNbOccurrences(db) >= 0);
 * @invar (exists DoubleBeed db; ; getNbOccurrences(db) > 0 && db.getDouble() == null)
 *            ==> getDouble() == null;
 *        If one of the arguments is null, then the value of this beed is null.
 * @invar (forAll DoubleBeed db; ; getNbOccurrences(db) > 0 ==> db.getDouble() != null)
 *            ==> getDouble() != null;
 *        If all arguments are effective, then the value of this beed is also effective.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class DoubleCommutativeOperationBeed
    extends AbstractPropertyBeed<ActualDoubleEvent>
    implements DoubleBeed {

  /**
   * @pre   owner != null;
   * @post  getDouble() == 0;
   * @post  (forall DoubleBeed db; ; getNbOccurrences(db) == 0};
   */
  public DoubleCommutativeOperationBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @invar  getNbOccurrences() > 0;
   */
  private class ArgumentListener implements Listener<RealEvent> {

    /**
     * @post  getNbOccurrences() == 1;
     */
    public ArgumentListener() {
      $nbOccurrences = 1;
    }

    public void beedChanged(RealEvent event) {
      // recalculate(); optimization
      Double oldValue = $value;
      if ($value !=  null) {
        assert $value != null;
        assert event.getOldDouble() != null :
          "The old value contained in the event must be effective since $value != null.";
        $value = (event.getNewDouble() == null)
                     ? null
                     : recalculateValue($value, event.getOldDouble(), event.getNewDouble(), getNbOccurrences());
      }
      else if ((event.getNewDouble() != null) && (event.getOldDouble() == null)) {
        recalculate();
      }
      // else: NOP
      if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new ActualDoubleEvent(DoubleCommutativeOperationBeed.this, oldValue, $value, event.getEdit()));
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
   * Recalculate the value of this beed: one of the arguments has changed.
   *
   * @param oldValueBeed      The old value of this beed.
   * @param oldValueArgument  The old value of the argument that has changed.
   * @param newValueArgument  The new value of that argument.
   * @param nbOccurrences     The number of occurrences of that argument.
   */
  protected abstract Double recalculateValue(
      Double oldValueBeed,
      Double oldValueArgument,
      Double newValueArgument,
      int nbOccurrences);

  /**
   * @basic
   */
  public final int getNbOccurrences(RealBeed<?> argument) {
    ArgumentListener argumentListener = $arguments.get(argument);
    return argumentListener != null ? argumentListener.getNbOccurrences() : 0;
  }

  /**
   * @pre   argument != null;
   * @post  new.getNbOccurrences(argument) == getNbOccurrences(argument) + 1;
   */
  public final void addArgument(RealBeed<?> argument) {
    assert argument != null;
    synchronized (argument) { // TODO is this correct?
      ArgumentListener argumentListener = $arguments.get(argument);
      if (argumentListener != null) {
        assert getNbOccurrences(argument) > 0;
        argumentListener.increaseNbOccurrences();
      }
      else {
        assert getNbOccurrences(argument) == 0;
        argumentListener = new ArgumentListener();
        argument.addListener(argumentListener);
        $arguments.put(argument, argumentListener);
      }
      // recalculate(); optimization
      if ($value != null) {
        Double oldValue = $value;
        $value = (argument.getDouble() == null)
                   ? null
                   : recalculateValueAdded($value, argument.getDouble(), 1);
        if (! MathUtil.equalValue(oldValue, $value)) {
          fireChangeEvent(new ActualDoubleEvent(this, oldValue, $value, null));
        }
      }
      // otherwise, there is an existing null argument; the new argument cannot change null value
    }
  }

  /**
   * Recalculate the value of this beed: a new argument has been added.
   *
   * @param oldValueBeed     The old value of this beed.
   * @param valueArgument    The value of the argument that has been added.
   * @param nbOccurrences    The number of times that the argument is added.
   */
  protected abstract Double recalculateValueAdded(
      Double oldValueBeed, Double valueArgument, int nbOccurrences);

  /**
   * @post  getNbOccurrences() > 0
   *          ? new.getNbOccurrences(argument) == getNbOccurrences(argument) - 1;
   *          : true;
   */
  public final void removeArgument(RealBeed<?> argument) {
    synchronized (argument) { // TODO is this correct?
      ArgumentListener argumentListener = $arguments.get(argument);
      if (argumentListener != null) {
        assert getNbOccurrences(argument) > 0;
        if (getNbOccurrences(argument) > 1) {
          argumentListener.decreaseNbOccurrences();
        }
        else {
          assert getNbOccurrences(argument) == 1;
          argument.removeListener(argumentListener);
          $arguments.remove(argument);
        }
        // recalculate(); optimization
        Double oldValue = $value;
        /**
         * argument.getDouble() == null && getNbOccurrences() == 0  ==>  recalculate
         * argument.getDouble() == null && getNbOccurrences() > 0   ==>  new.$value == old.$value == null
         * argument.getDouble() != null && $value != null           ==>  new.$value == remove the value of argument.getDouble() from old.$value
         * argument.getDouble() != null && $value == null           ==>  new.$value == old.$value == null
         */
        if (argument.getDouble() == null && getNbOccurrences(argument) == 0) {
            /* $value was null because of this argument. After the remove,
             * the value can be null because of another argument, or
             * can be some value: we can't know, recalculate completely
             */
           recalculate();
        }
        else if ($value != null) {
          // since $value is effective, all arguments are effective
          // the new value of this beed beed is the old value 'minus' the value of the removed argument
          assert argument.getDouble() != null;
          $value = recalculateValueRemoved($value, argument.getDouble(), 1);
        }
        // else: in all other cases, the value of $value is null, and stays null
        if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
          fireChangeEvent(new ActualDoubleEvent(this, oldValue, $value, null));
        }
        /* else, argument != null, but $value is null; this means there is another argument that is null,
           and removing this argument won't change that */
      }
      // else, argument was not one of our arguments: do nothing
    }
  }

  /**
   * Recalculate the value of this beed: an argument has been removed.
   *
   * @param oldValueBeed     The old value of this beed.
   * @param valueArgument    The value of the argument that has been removed.
   * @param nbOccurrences    The number of times that the argument is removed.
   */
  protected abstract Double recalculateValueRemoved(
      Double oldValueBeed, Double valueArgument, int nbOccurrences);

  /**
   * @invar $arguments != null;
   * @invar Collections.noNull($arguments);
   */
  private final Map<RealBeed<?>, ArgumentListener> $arguments =
        new HashMap<RealBeed<?>, ArgumentListener>();

  public final Double getDouble() {
    return $value;
  }

  /**
   * The value of this beed is recalculated. This is done by iterating over the
   * arguments.
   * When there are no terms, the result is equal to {@link #initialValue()}.
   * When one of the terms is null, the result is null.
   * When all terms are effective, the result is dependent on the specific subclass.
   */
  public void recalculate() {
    Double newValue = initialValue();
    for (RealBeed<?> argument : $arguments.keySet()) {
      Double argumentValue = argument.getDouble();
      if (argumentValue == null) {
        newValue = null;
        break;
      }
      newValue = recalculateValueAdded(newValue, argumentValue, getNbOccurrences(argument));
    }
    $value = newValue;
  }

  private Double $value = initialValue();

  /**
   * The initial value of this beed.
   */
  public abstract Double initialValue();

  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldDouble() == null;
   * @post  result.getNewDouble() == getDouble();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final ActualDoubleEvent createInitialEvent() {
    return new ActualDoubleEvent(this, null, getDouble(), null);
  }

  @Override
  protected String otherToStringInformation() {
    return getDouble() + " (# " + $arguments.size() + ")";
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + getDouble() + "\n");
    sb.append(indent(level + 1) + "number of " + argumentsToString() + ":" + $arguments.size() + "\n");
    for (RealBeed<?> argument : $arguments.keySet()) {
      argument.toString(sb, level + 2);
    }
  }

  /**
   * This method is used in toString and returns the correct name of the arguments
   * in a specific subclass: "terms", "factors", ....
   */
  public abstract String argumentsToString();

}

