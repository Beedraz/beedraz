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
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.collection.set.SetBeed;
import org.beedra_II.property.collection.set.SetEvent;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * A beed that does a computation on a given set of beeds of type {@link DoubleBeed}.
 *
 * @invar getSource() == null ==> getDouble() == null;
 * @invar getSource() != null ==>
 *        (exists DoubleBeed db; getSource().get().contains(db); db.getDouble() == null)
 *            ==> getDouble() == null;
 *        If the value of one of the beeds in the given set is null,
 *        then the value of this beed is null.
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() != null;
 *        If the values of all beeds in the given set are effective,
 *        then the value of this beed is effective.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractDoubleSetComputationBeed
    extends AbstractPropertyBeed<ActualDoubleEvent>
    implements DoubleBeed {


  /**
   * @pre   owner != null;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public AbstractDoubleSetComputationBeed(AggregateBeed owner) {
    super(owner);
  }

  public final boolean isEffective() {
    return $effective;
  }

  protected void assignEffective(boolean effective) {
    $effective = effective;
  }

  private boolean $effective = false;

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected ActualDoubleEvent filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
        // if the source changes (elements added and / or removed
        if (events.keySet().contains($source)) {
          @SuppressWarnings("unchecked")
          SetEvent<RealBeed<?>> setEvent = (SetEvent<RealBeed<?>>)events.get($source);
          sourceChanged(setEvent);
        }
        // if the value of one of the elements changes
        // we do nothing special here, just a total recalculate
        // IDEA there is room for optimalization here
        // recalculate and notify the listeners if the value has changed
        boolean oldEffective = $effective;
        double oldValue = $value;
        assert $source != null;
        recalculate($source);
        if ((oldEffective != $effective) || ! MathUtil.equalPrimitiveValue(oldValue, $value)) {
          return new ActualDoubleEvent(
              AbstractDoubleSetComputationBeed.this,
              oldEffective ? oldValue : null, // When oldEffective and $effective are different, the values of
                  // oldValue and $value are not necessarily different, while the constructor of the event
                  // expects two different values
                  // To solve this problem, we return null for the value that corresponds to the boolean
                  // value (oldEffective or $effective) that is false. One of the two values will be null,
                  // the other one is a double value (or NaN, or Infinity).
              $effective ? $value : null,
              edit);
        }
        else {
          return null;
        }
      }

      /**
       * @post    All RealBeeds that are added to the SetBeed by the given event
       *          become update sources.
       *          (The reason is that the AbstractDoubleSetComputationBeed should be
       *          notified (and then recalculate) when one of the DoubleBeeds
       *          changes.)
       * @post    All RealBeeds that are removed from the SetBeed by the given
       *          event are no longer update sources.
       * @post    getDouble() is recalculated.
       */
      private void sourceChanged(SetEvent<RealBeed<?>> event) {
        /* All RealBeeds that are added to the SetBeed by the given event
         * become update sources
         */
        Set<RealBeed<?>> added = event.getAddedElements();
        for (RealBeed<?> beed : added) {
          addUpdateSource(beed);
        }
        /* All RealBeeds that are removed from the SetBeed by the given event
         * stop being update sources
         */
        Set<RealBeed<?>> removed = event.getRemovedElements();
        for (RealBeed<?> beed : removed) {
          removeUpdateSource(beed);
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
     * TODO This only works if we only add 1 update source during construction,
     *      so a better solution should be sought.
     */
    return $dependent == null ? 0 : $dependent.getMaximumRootUpdateSourceDistance();
  }




  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<RealBeed<?>, ?> getSource() {
    return $source;
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    getDouble() == the mean of the given source
   * @post    The AbstractDoubleSetComputationBeed is registered as a listener of the
   *          given SetBeed.
   * @post    The AbstractDoubleSetComputationBeed is registered as a listener of all
   *          DoubleBeeds in the given SetBeed. (The reason is that the
   *          AbstractDoubleSetComputationBeed should be notified (and then recalculate)
   *          when one of the DoubleBeeds changes.)
   * @post    The listeners of this beed are notified when the value changes.
   */
  public final void setSource(SetBeed<RealBeed<?>, ?> source) {
    if ($source != null) {
      for (RealBeed<?> beed : $source.get()) {
        $dependent.removeUpdateSource(beed);
      }
      $dependent.removeUpdateSource($source);
    }
    // set the source
    $source = source;
    boolean oldEffective = isEffective();
    double oldValue = $value;
    if ($source != null) {
      $dependent.addUpdateSource($source);
      for (RealBeed<?> beed : $source.get()) {
        $dependent.addUpdateSource(beed);
      }
      recalculate($source);
    }
    else {
      assignEffective(false);
    }
    if ((oldEffective != isEffective()) || (oldValue != $value)) {
      updateDependents(
          new ActualDoubleEvent(
              AbstractDoubleSetComputationBeed.this,
              oldEffective ? oldValue : null,
              isEffective() ? $value : null,
              null)); // edit = null
    }
  }

  private SetBeed<RealBeed<?>, ?> $source;

  /*</property>*/



  /*<property name="value*/
  //------------------------------------------------------------------

  public final double getdouble() {
    return $value;
  }

  public final Double getDouble() {
    return $effective ? Double.valueOf(getdouble()) : null;
  }

  public final BigDecimal getBigDecimal() {
    return castToBigDecimal(getDouble());
  }

  /**
   * The value of this beed.
   */
  private double $value;

  /*</property>*/



  /**
   * The value of this beed is recalculated.
   *
   * @pre source != null;
   */
  protected abstract void recalculate(SetBeed<RealBeed<?>, ?> source);


  protected final void assignValue(Double value) {
    $value = value;
  }

  public final Set<? extends UpdateSource> getUpdateSources() {
    return $dependent.getUpdateSources();
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
  protected String otherToStringInformation() {
    return getDouble() + " (# " + getSource().get().size() + ")";
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + getDouble() + "\n");
    sb.append(indent(level + 1) + "number of elements in source:" + getSource().get().size() + "\n");
    getSource().toString(sb, level + 2);
  }

  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append(getOperatorString());
    sb.append("(");
    Iterator<RealBeed<?>> i = getSource().get().iterator();
    while (i.hasNext()) {
      RealBeed<?> argument = i.next();
      sb.append("(");
      if (depth == 1) {
        sb.append(numberFormat.format(argument.getdouble()));
      }
      else {
        argument.toStringDepth(sb, depth - 1, numberFormat);
      }
      sb.append(")");
      if (i.hasNext()) {
        sb.append(",");
      }
    }
    sb.append(")");
  }

  /**
   * The operator of this binary expression.
   */
  public abstract String getOperatorString();

}

