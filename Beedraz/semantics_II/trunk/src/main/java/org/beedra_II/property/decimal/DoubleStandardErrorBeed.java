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


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.collection.set.SetBeed;
import org.beedra_II.property.collection.set.SetEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * A beed that computes the standard error of a given set of beeds of type {@link DoubleBeed}.
 *
 * @invar getSource() == null ==> getDouble() == null;
 * @invar getSource() != null ==>
 *        (exists DoubleBeed db; getSource().get().contains(db); db.getDouble() == null)
 *            ==> getDouble() == null;
 *        If the value of one of the beeds in the given set is null,
 *        then the value of the standard error beed is null.
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() == SE { db.getDouble() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective,
 *        then the value of the standard error beed is the standard error of the values of
 *        all beeds in the given set. The standard error of an empty set or of a set containing
 *        only one element is null.
 *        e.g. when  getSource() = {1, 2, 3, 4}
 *             then  getDouble() = Math.sqrt(dividend/divisor)
 *             where divisor = 4*3
 *             and   dividend = (1-mean)^2 + (2-mean)^2 + (3-mean)^2 + (4-mean)^2)
 *             and   mean = (1 + 2 + 3 + 4)/4
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleStandardErrorBeed
    extends AbstractPropertyBeed<DoubleEvent>
    implements DoubleBeed<DoubleEvent> {


  /**
   * @pre   owner != null;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoubleStandardErrorBeed(AggregateBeed owner) {
    super(owner);
  }


  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<DoubleBeed<DoubleEvent>, ?> getSource() {
    return $source;
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    getDouble() == the standard error of the given source
   * @post    The DoubleStandardErrorBeed is registered as a listener of the given SetBeed.
   * @post    The DoubleStandardErrorBeed is registered as a listener of all DoubleBeeds in
   *          the given SetBeed. (The reason is that the DoubleStandardErrorBeed should be
   *          notified (and then recalculate) when one of the DoubleBeeds changes.)
   * @post    The listeners of this beed are notified when the value changes.
   */
  public final void setSource(SetBeed<DoubleBeed<DoubleEvent>, ?> source) {
    // set the source
    $source = source;
    if (source != null) {
      // register the DoubleStandardErrorBeed as listener of the given SetBeed
      source.addListener($sourceListener);
      // register the DoubleStandardErrorBeed as listener of all DoubleBeeds in the given SetBeed
      for (DoubleBeed<DoubleEvent> beed : source.get()) {
        beed.addListener($beedListener);
      }
    }
    // recalculate and notify the listeners if the value has changed
    Double oldValue = $value;
    recalculate();
    if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
      fireChangeEvent(new ActualDoubleEvent(DoubleStandardErrorBeed.this, oldValue, $value, null)); // edit = null
    }
  }

  private SetBeed<DoubleBeed<DoubleEvent>, ?> $source;

  /*</property>*/


  /**
   * A listener that will be registered as listener of the {@link #getSource()}.
   */
  private final Listener<SetEvent<DoubleBeed<DoubleEvent>>> $sourceListener =
        new Listener<SetEvent<DoubleBeed<DoubleEvent>>>() {

    /**
     * @post    The DoubleStandardErrorBeed is registered as a listener of all DoubleBeeds
     *          that are added to the SetBeed by the given event. (The reason is that
     *          the DoubleStandardErrorBeed should be notified (and then recalculate) when one
     *          of the DoubleBeeds changes.)
     * @post    The DoubleStandardErrorBeed is removed as listener of all DoubleBeeds
     *          that are removed from the SetBeed by the given event.
     * @post    getDouble() == the standard error of the given source
     * @post    The listeners of this beed are notified when the value changes.
     */
    public void beedChanged(SetEvent<DoubleBeed<DoubleEvent>> event) {
      // add the DoubleStandardErrorBeed as listener of all DoubleBeeds that are added to the SetBeed by the given event
      Set<DoubleBeed<DoubleEvent>> added = event.getAddedElements();
      for (DoubleBeed<DoubleEvent> beed : added) {
        beed.addListener($beedListener);
      }
      // remove the DoubleStandardErrorBeed as listener from all DoubleBeeds that are removed from the SetBeed by the given event
      Set<DoubleBeed<DoubleEvent>> removed = event.getRemovedElements();
      for (DoubleBeed<DoubleEvent> beed : removed) {
        beed.removeListener($beedListener);
      }
      // recalculate and notify the listeners if the value has changed
      Double oldValue = $value;
      recalculate();
      if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new ActualDoubleEvent(DoubleStandardErrorBeed.this, oldValue, $value, event.getEdit()));
      }
    }

  };


  private final Listener<DoubleEvent> $beedListener = new Listener<DoubleEvent>() {

    /**
     * @post    getDouble() == the standard error of the given source
     * @post    The listeners of this beed are notified when the value changes.
     */
    public void beedChanged(DoubleEvent event) {
      // recalculate and notify the listeners if the value has changed
      Double oldValue = $value;
      recalculate();
      if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new ActualDoubleEvent(DoubleStandardErrorBeed.this, oldValue, $value, event.getEdit()));
      }
    }

  };


  public final Double getDouble() {
    return $value;
  }


  /**
   * The value of $value is recalculated. This is done by iterating over the beeds
   * in the source set beed.
   * When the source is null, the result is null.
   * When the source contains zero or one beeds, the result is null.
   * When one of the terms is null, the result is null.
   * When all terms are effective, the result is the standard error of the values
   * of the beeds.
   */
  public void recalculate() {
    Double standardError;
    if (getSource() == null) {
      standardError = null;
    }
    else if (getSource().get().size() == 0 ||
             getSource().get().size() == 1) {
      standardError = null;
    }
    else {
      assert getSource() != null;
      assert getSource().get().size() > 1;
      // compute the average
      Double average = 0.0;
      for (DoubleBeed<DoubleEvent> beed : getSource().get()) {
        Double beedValue = beed.getDouble();
        if (beedValue == null) {
          average = null;
          break;
        }
        average += beedValue; // autoboxing
      }
      if (average != null) {
        average = average / getSource().get().size(); // divisor is not zero (see if-condition)!
      }
      // compute the standard error
      if (average != null) {
        standardError = 0.0;
        for (DoubleBeed<DoubleEvent> beed : getSource().get()) {
          Double beedValue = beed.getDouble();
          if (beedValue == null) {
            standardError = null;
            break;
          }
          standardError += Math.pow(beedValue - average, 2); // autoboxing
        }
        if (standardError != null) {
          int size = getSource().get().size();
          standardError = standardError / (size * (size - 1)); // divisor is not zero (see if-condition)!
          standardError = Math.sqrt(standardError);
        }
      }
      else {
        standardError = null;
      }
    }
    $value = standardError;
  }


  private Double $value = null;


  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldDouble() == null;
   * @post  result.getNewDouble() == getDouble();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final DoubleEvent createInitialEvent() {
    return new ActualDoubleEvent(this, null, getDouble(), null);
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
  }

}

