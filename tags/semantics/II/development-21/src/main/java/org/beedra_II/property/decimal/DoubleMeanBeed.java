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


import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.set.SetBeed;
import org.beedra_II.property.set.SetEvent;
import org.ppeew.annotations.vcs.CvsInfo;
import org.ppeew.smallfries.ComparisonUtil;


/**
 * A beed that computes the mean of a given set of beeds of type {@link DoubleBeed}.
 *
 * @invar getSource() == null ==> getDouble() == null;
 * @invar getSource() != null ==>
 *        (exists DoubleBeed db; getSource().get().contains(db); db.getDouble() == null)
 *            ==> getDouble() == null;
 *        If the value of one of the beeds in the given set is null,
 *        then the value of the mean beed is null.
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() == avg { db.getDouble() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective,
 *        then the value of the mean beed is the mean of the values of all beeds in the
 *        given set. The mean of an empty set is null.
 *        e.g. getDouble() = (5.1 + 3.2 + 4.9) / 3
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleMeanBeed
    extends AbstractPropertyBeed<DoubleEvent>
    implements DoubleBeed<DoubleEvent> {


  /**
   * @pre   owner != null;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoubleMeanBeed(AggregateBeed owner) {
    super(owner);
  }


  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<DoubleBeed<DoubleEvent>> getSource() {
    return $source;
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    getDouble() == the mean of the given source
   * @post    The DoubleMeanBeed is registered as a listener of the given SetBeed.
   * @post    The DoubleMeanBeed is registered as a listener of all DoubleBeeds in
   *          the given SetBeed. (The reason is that the DoubleMeanBeed should be
   *          notified (and then recalculate) when one of the DoubleBeeds changes.)
   * @post    The listeners of this beed are notified when the value changes.
   */
  public final void setSource(SetBeed<DoubleBeed<DoubleEvent>> source) {
    // set the source
    $source = source;
    if (source != null) {
      // register the DoubleMeanBeed as listener of the given SetBeed
      source.addListener($sourceListener);
      // register the DoubleMeanBeed as listener of all DoubleBeeds in the given SetBeed
      for (DoubleBeed<DoubleEvent> beed : source.get()) {
        beed.addListener($beedListener);
      }
    }
    // recalculate and notify the listeners if the value has changed
    Double oldValue = $value;
    recalculate();
    if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
      fireChangeEvent(new ActualDoubleEvent(DoubleMeanBeed.this, oldValue, $value, null)); // edit = null
    }
  }

  private SetBeed<DoubleBeed<DoubleEvent>> $source;

  /*</property>*/


  /**
   * A listener that will be registered as listener of the {@link #getSource()}.
   */
  private final Listener<SetEvent<DoubleBeed<DoubleEvent>>> $sourceListener =
        new Listener<SetEvent<DoubleBeed<DoubleEvent>>>() {

    /**
     * @post    The DoubleMeanBeed is registered as a listener of all DoubleBeeds
     *          that are added to the SetBeed by the given event. (The reason is that
     *          the DoubleMeanBeed should be notified (and then recalculate) when one
     *          of the DoubleBeeds changes.)
     * @post    The DoubleMeanBeed is removed as listener of all DoubleBeeds
     *          that are removed from the SetBeed by the given event.
     * @post    getDouble() == the mean of the given source
     * @post    The listeners of this beed are notified when the value changes.
     */
    public void beedChanged(SetEvent<DoubleBeed<DoubleEvent>> event) {
      // add the DoubleMeanBeed as listener of all DoubleBeeds that are added to the SetBeed by the given event
      Set<DoubleBeed<DoubleEvent>> added = event.getAddedElements();
      for (DoubleBeed<DoubleEvent> beed : added) {
        beed.addListener($beedListener);
      }
      // remove the DoubleMeanBeed as listener from all DoubleBeeds that are removed from the SetBeed by the given event
      Set<DoubleBeed<DoubleEvent>> removed = event.getRemovedElements();
      for (DoubleBeed<DoubleEvent> beed : removed) {
        beed.removeListener($beedListener);
      }
      // recalculate and notify the listeners if the value has changed
      Double oldValue = $value;
      recalculate();
      if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new ActualDoubleEvent(DoubleMeanBeed.this, oldValue, $value, event.getEdit()));
      }
    }

  };


  private final Listener<DoubleEvent> $beedListener = new Listener<DoubleEvent>() {

    /**
     * @post    getDouble() == the mean of the given source
     * @post    The listeners of this beed are notified when the value changes.
     */
    public void beedChanged(DoubleEvent event) {
      // recalculate and notify the listeners if the value has changed
      Double oldValue = $value;
      recalculate();
      if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new ActualDoubleEvent(DoubleMeanBeed.this, oldValue, $value, event.getEdit()));
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
   * When the source contains zero beeds, the result is null.
   * When one of the terms is null, the result is null.
   * When all terms are effective, the result is the average of the values of the beeds.
   */
  public void recalculate() {
    Double newValue;
    if (getSource() == null) {
      newValue = null;
    }
    else if (getSource().get().size() == 0) {
      newValue = null;
    }
    else {
      assert getSource() != null;
      assert getSource().get().size() > 0;
      newValue = 0.0;
      for (DoubleBeed<DoubleEvent> beed : getSource().get()) {
        Double beedValue = beed.getDouble();
        if (beedValue == null) {
          newValue = null;
          break;
        }
        newValue += beedValue; // autoboxing
      }
      if (newValue != null) {
        newValue = newValue / getSource().get().size(); // divisor is not zero (see if-condition)!
      }
    }
    $value = newValue;
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

}

