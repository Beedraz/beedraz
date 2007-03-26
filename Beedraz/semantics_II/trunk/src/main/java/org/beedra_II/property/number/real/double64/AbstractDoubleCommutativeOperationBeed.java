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


import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.RealEvent;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;
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
public abstract class AbstractDoubleCommutativeOperationBeed
    extends AbstractPropertyBeed<ActualDoubleEvent>
    implements DoubleBeed {

  /**
   * @pre   owner != null;
   * @post  getdouble() == initialValue();
   * @post  (forall DoubleBeed db; ; getNbOccurrences(db) == 0};
   * @post  isEffective();
   */
  protected AbstractDoubleCommutativeOperationBeed(AggregateBeed owner) {
    super(owner);
  }

  public final boolean isEffective() {
    return $effective;
  }

  protected void assignEffective(boolean effective) {
    $effective = effective;
  }

  private boolean $effective = true;

  private final Dependent<RealBeed<?>> $dependent =
    new AbstractUpdateSourceDependentDelegate<RealBeed<?>, ActualDoubleEvent>(this) {

      @Override
      protected ActualDoubleEvent filteredUpdate(Map<RealBeed<?>, Event> events) {
        // recalculate(); optimization
        boolean oldEffective = $effective;
        double oldValue = $value;
        if ($effective) {
          for (Map.Entry<RealBeed<?>, Event> entry : events.entrySet()) {
            RealEvent event = (RealEvent)entry.getValue();
            assert event.getOldDouble() != null :
              "The old value contained in the event must be effective since $value != null.";
            Double newDouble = event.getNewDouble();
            if (newDouble == null) {
              assignEffective(false);
              break;
            }
            else {
              assert event.getOldDouble() != null : "we couldn't have been effective if the old value in the event is not";
              assert event.getNewDouble() != null;
              $value = recalculateValue($value, event.getOldDouble(), event.getNewDouble(), getNrOfOccurences(entry.getKey()));
              assert isEffective();
            }
          }
        }
        else {
          recalculate();
        }
        if ((oldEffective != $effective) || !MathUtil.equalPrimitiveValue(oldValue, $value)) {
          /* MUDO for now, we take the first edit we get, under the assumption that all events have
           * the same edit; with compound edits, we should gather different edits
           */
          assert events.size() > 0;
          Iterator<Event> iter = events.values().iterator();
          Event event = iter.next();
          Edit<?> edit = event.getEdit();
          return new ActualDoubleEvent(
              AbstractDoubleCommutativeOperationBeed.this,
              oldEffective ? oldValue : null,
              $effective ? $value : null,
              edit);
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
   * Recalculate the value of this beed: one of the arguments has changed.
   *
   * @param oldValueBeed      The old value of this beed.
   * @param oldValueArgument  The old value of the argument that has changed.
   * @param newValueArgument  The new value of that argument.
   * @param nbOccurrences     The number of occurrences of that argument.
   */
  protected abstract double recalculateValue(double oldValueBeed, double oldValueArgument, double newValueArgument, int nbOccurrences);

  /**
   * @basic
   */
  public final int getNbOccurrences(RealBeed<?> argument) {
    return $dependent.getNrOfOccurences(argument);
  }

  public final Collection<RealBeed<?>> getArguments() {
    return $dependent.getUpdateSourcesCollection();
  }

  /**
   * @pre   argument != null;
   * @post  new.getNbOccurrences(argument) == getNbOccurrences(argument) + 1;
   */
  public final void addArgument(RealBeed<?> argument) {
    assert argument != null;
    $dependent.addUpdateSource(argument);
    // recalculate(); optimization
    if ($effective) {
      double oldValue = $value;
      if (! argument.isEffective()) {
        assignEffective(false);
      }
      else {
        assert argument.isEffective();
        if (argument.getdouble() != initialValue()) {
          $value = recalculateValueAdded($value, argument.getdouble(), 1);
        }
      }
      if ((! $effective) || !MathUtil.equalPrimitiveValue(oldValue, $value)) {
        updateDependents(new ActualDoubleEvent(this, oldValue, $effective ? $value : null, null));
      }
    }
    // otherwise, there is an existing null argument; the new argument cannot change null value
  }

  /**
   * Recalculate the value of this beed: a new argument has been added.
   *
   * @param oldValueBeed     The old value of this beed.
   * @param valueArgument    The value of the argument that has been added.
   * @param nbOccurrences    The number of times that the argument is added.
   */
  protected abstract double recalculateValueAdded(double oldValueBeed, double valueArgument, int nbOccurrences);

  /**
   * @post  getNbOccurrences() > 0
   *          ? new.getNbOccurrences(argument) == getNbOccurrences(argument) - 1;
   *          : true;
   */
  public final void removeArgument(RealBeed<?> argument) {
    if ($dependent.getUpdateSourcesOccurencesMap().containsKey(argument)) {
      $dependent.removeUpdateSource(argument);
      // recalculate(); optimization
      boolean oldEffective = $effective;
      double oldValue = $value;
      /**
       * argument.getDouble() == null && getNbOccurrences() == 0  ==>  recalculate
       * argument.getDouble() == null && getNbOccurrences() > 0   ==>  new.$value == old.$value == null
       * argument.getDouble() != null && $value != null           ==>  new.$value == remove the value of argument.getDouble() from old.$value
       * argument.getDouble() != null && $value == null           ==>  new.$value == old.$value == null
       */
      if ((! argument.isEffective()) && ($dependent.getNrOfOccurences(argument) == 0)) {
          /* $value was null because of this argument. After the remove,
           * the value can be null because of another argument, or
           * can be some value: we can't know, recalculate completely
           */
         recalculate();
      }
      else if ($effective) {
        // since $value is effective, all arguments are effective
        // the new value of this beed beed is the old value 'minus' the value of the removed argument
        assert argument.isEffective();
        $value = recalculateValueRemoved($value, argument.getdouble(), 1);
      }
      // else: in all other cases, the value of $value is null, and stays null
      if ((oldEffective != $effective) || !MathUtil.equalPrimitiveValue(oldValue, $value)) {
        updateDependents(new ActualDoubleEvent(
            this,
            oldEffective ? oldValue : null,
            $effective ? $value : null,
            null));
      }
      /* else, argument != null, but $value is null; this means there is another argument that is null,
         and removing this argument won't change that */
    }
    // else, argument was not one of our arguments: do nothing
  }

  protected final Map<RealBeed<?>, Integer> getArgumentMap() {
    return $dependent.getUpdateSourcesOccurencesMap();
  }

  /**
   * Recalculate the value of this beed: an argument has been removed.
   *
   * @param oldValueBeed     The old value of this beed.
   * @param valueArgument    The value of the argument that has been removed.
   * @param nbOccurrences    The number of times that the argument is removed.
   */
  protected abstract double recalculateValueRemoved(double oldValueBeed, double valueArgument, int nbOccurrences);

  /**
   * The value of this beed is recalculated. This is done by iterating over the
   * arguments.
   * When there are no terms, the result is equal to {@link #initialValue()}.
   * When one of the terms is null, the result is null.
   * When all terms are effective, the result is dependent on the specific subclass.
   */
  protected final void recalculate() {
    $value = initialValue();
    for(Map.Entry<RealBeed<?>, Integer> entry : $dependent.getUpdateSourcesOccurencesMap().entrySet()) {
      RealBeed<?> argument = entry.getKey();
      if (! argument.isEffective()) {
        assignEffective(false);
        return;
      }
      int nrOfOccurences = entry.getValue();
      $value = recalculateValueAdded($value, argument.getdouble(), nrOfOccurences);
    }
    assignEffective(true);
  }

  public final Double get() {
    return getDouble();
  }

  public final Double getDouble() {
    return $effective ? Double.valueOf($value) : null;
  }

  public final BigDecimal getBigDecimal() {
    return castToBigDecimal(getDouble());
  }

  public final double getdouble() {
    return $value;
  }

  private double $value = initialValue();

  /**
   * The initial value of this beed.
   */
  public abstract double initialValue();

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
    return getDouble() + " (# " + $dependent.getUpdateSourcesSize() + ")";
  }

  public final Set<? extends UpdateSource> getUpdateSources() {
    return $dependent.getUpdateSourcesSet();
  }

  private final static Set<? extends UpdateSource> PHI = Collections.emptySet();

  public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
    /* fixed to make it possible to use this method during construction,
     * before $dependent is initialized. But that is bad code, and should be
     * fixed.
     */
    return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value: " + getDouble() + "\n");
    sb.append(indent(level + 1) + "number of " + argumentsToString() + ": " + $dependent.getUpdateSourcesSize() + "\n");
    for (RealBeed<?> argument : $dependent.getUpdateSourcesSet()) {
      argument.toString(sb, level + 2);
      sb.append(indent(level + 2) + "nr of occurences: " + $dependent.getNrOfOccurences(argument) + "\n");
    }
  }

  /**
   * This method is used in toString and returns the correct name of the arguments
   * in a specific subclass: "terms", "factors", ....
   */
  public abstract String argumentsToString();

}

