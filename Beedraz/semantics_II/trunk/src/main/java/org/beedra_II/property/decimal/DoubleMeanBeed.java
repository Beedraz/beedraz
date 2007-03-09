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

import org.beedra.util_I.Comparison;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.set.SetBeed;
import org.beedra_II.property.set.SetEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A beed that computes the mean of a given set of beeds of type {@link DoubleBeed}.
 *
 * @invar getSource() == null ==> get() == null;
 * @invar getSource() != null ==>
 *        (exists DoubleBeed db; getSource().get().contains(db); db.get() == null)
 *            ==> get() == null;
 *        If the value of one of the beeds in the given set is null,
 *        then the value of the mean beed is null.
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.get() != null)
 *            ==> get() == avg { db.get() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective,
 *        then the value of the mean beed is the mean of the values of all beeds in the
 *        given set. The mean of an empty set is null.
 *        e.g. get() = (5.1 + 3.2 + 4.9) / 3
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleMeanBeed
    extends AbstractPropertyBeed<DoubleEvent>
    implements DoubleBeed {


  /**
   * @pre   owner != null;
   * @post  getSource() == null;
   * @post  get() == null;
   */
  public DoubleMeanBeed(AggregateBeed owner) {
    super(owner);
  }


  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<DoubleBeed> getSource() {
    return $source;
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    get() == the mean of the given source
   * @post    The DoubleMeanBeed is registered as a listener of the given SetBeed.
   * @post    The DoubleMeanBeed is registered as a listener of all DoubleBeeds in
   *          the given SetBeed. (The reason is that the DoubleMeanBeed should be
   *          notified (and then recalculate) when one of the DoubleBeeds changes.)
   * @post    The listeners of this beed are notified when the value changes.
   */
  public final void setSource(SetBeed<DoubleBeed> source) {
    // set the source
    $source = source;
    if (source != null) {
      // register the DoubleMeanBeed as listener of the given SetBeed
      source.addListener($sourceListener);
      // register the DoubleMeanBeed as listener of all DoubleBeeds in the given SetBeed
      for (DoubleBeed beed : source.get()) {
        beed.addListener($beedListener);
      }
    }
    // recalculate and notify the listeners if the value has changed
    Double oldValue = $value;
    recalculate();
    if (! Comparison.equalsWithNull(oldValue, $value)) {
      fireChangeEvent(new DoubleEvent(DoubleMeanBeed.this, oldValue, $value, null)); // edit = null
    }
  }

  private SetBeed<DoubleBeed> $source;

  /*</property>*/


  /**
   * A listener that will be registered as listener of the {@link #getSource()}.
   */
  private final Listener<SetEvent<DoubleBeed>> $sourceListener = new Listener<SetEvent<DoubleBeed>>() {

    /**
     * @post    The DoubleMeanBeed is registered as a listener of all DoubleBeeds
     *          that are added to the SetBeed by the given event. (The reason is that
     *          the DoubleMeanBeed should be notified (and then recalculate) when one
     *          of the DoubleBeeds changes.)
     * @post    get() == the mean of the given source
     * @post    The listeners of this beed are notified when the value changes.
     */
    public void beedChanged(SetEvent<DoubleBeed> event) {
      // add the DoubleMeanBeed as listener of all DoubleBeeds that are added to the SetBeed by the given event
      Set<DoubleBeed> added = event.getAddedElements();
      for (DoubleBeed beed : added) {
        beed.addListener($beedListener);
      }
      // recalculate and notify the listeners if the value has changed
      Double oldValue = $value;
      recalculate();
      if (! Comparison.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new DoubleEvent(DoubleMeanBeed.this, oldValue, $value, event.getEdit()));
      }
    }

  };


  private final Listener<DoubleEvent> $beedListener = new Listener<DoubleEvent>() {

    /**
     * @post    get() == the mean of the given source
     * @post    The listeners of this beed are notified when the value changes.
     */
    public void beedChanged(DoubleEvent event) {
      // recalculate and notify the listeners if the value has changed
      Double oldValue = $value;
      recalculate();
      if (! Comparison.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new DoubleEvent(DoubleMeanBeed.this, oldValue, $value, event.getEdit()));
      }
    }

  };


  public final Double get() {
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
      for (DoubleBeed beed : getSource().get()) {
        Double beedValue = beed.get();
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
   * @post  result.getOldValue() == null;
   * @post  result.getNewValue() == get();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final DoubleEvent createInitialEvent() {
    return new DoubleEvent(this, null, get(), null);
  }

}

