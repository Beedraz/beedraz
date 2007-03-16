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

import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.collection.set.SetBeed;
import org.beedra_II.property.collection.set.SetEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


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
public abstract class DoubleSetComputationBeed
    extends AbstractPropertyBeed<DoubleEvent>
    implements DoubleBeed<DoubleEvent> {


  /**
   * @pre   owner != null;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoubleSetComputationBeed(AggregateBeed owner) {
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
   * @post    getDouble() == the mean of the given source
   * @post    The DoubleSetComputationBeed is registered as a listener of the
   *          given SetBeed.
   * @post    The DoubleSetComputationBeed is registered as a listener of all
   *          DoubleBeeds in the given SetBeed. (The reason is that the
   *          DoubleSetComputationBeed should be notified (and then recalculate)
   *          when one of the DoubleBeeds changes.)
   * @post    The listeners of this beed are notified when the value changes.
   */
  public final void setSource(SetBeed<DoubleBeed<DoubleEvent>, ?> source) {
    // set the source
    $source = source;
    if (source != null) {
      // register the DoubleSetComputationBeed as listener of the given SetBeed
      source.addListener($sourceListener);
      // register the DoubleSetComputationBeed as listener of all DoubleBeeds
      // in the given SetBeed
      for (DoubleBeed<DoubleEvent> beed : source.get()) {
        beed.addListener($beedListener);
      }
    }
    // recalculate and notify the listeners if the value has changed
    Double oldValue = $value;
    recalculate();
    if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
      fireChangeEvent(
          new ActualDoubleEvent(
              DoubleSetComputationBeed.this, oldValue, $value, null)); // edit = null
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
     * @post    The DoubleSetComputationBeed is registered as a listener of all
     *          DoubleBeeds that are added to the SetBeed by the given event.
     *          (The reason is that the DoubleSetComputationBeed should be
     *          notified (and then recalculate) when one of the DoubleBeeds
     *          changes.)
     * @post    The DoubleSetComputationBeed is removed as listener of all
     *          DoubleBeeds that are removed from the SetBeed by the given
     *          event.
     * @post    getDouble() is recalculated
     * @post    The listeners of this beed are notified when the value changes.
     */
    public void beedChanged(SetEvent<DoubleBeed<DoubleEvent>> event) {
      // add the DoubleSetComputationBeed as listener of all DoubleBeeds that
      // are added to the SetBeed by the given event
      Set<DoubleBeed<DoubleEvent>> added = event.getAddedElements();
      for (DoubleBeed<DoubleEvent> beed : added) {
        beed.addListener($beedListener);
      }
      // remove the DoubleSetComputationBeed as listener from all DoubleBeeds
      // that are removed from the SetBeed by the given event
      Set<DoubleBeed<DoubleEvent>> removed = event.getRemovedElements();
      for (DoubleBeed<DoubleEvent> beed : removed) {
        beed.removeListener($beedListener);
      }
      // recalculate and notify the listeners if the value has changed
      Double oldValue = $value;
      recalculate();
      if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(
            new ActualDoubleEvent(
                DoubleSetComputationBeed.this, oldValue, $value, event.getEdit()));
      }
    }

  };


  /**
   * A listener that will be registered as listener of the {@link DoubleBeed beeds}
   * in the {@link #getSource()}.
   */
  private final Listener<DoubleEvent> $beedListener = new Listener<DoubleEvent>() {

    /**
     * @post    getDouble() is recalculated
     * @post    The listeners of this beed are notified when the value changes.
     */
    public void beedChanged(DoubleEvent event) {
      // recalculate and notify the listeners if the value has changed
      Double oldValue = $value;
      recalculate();
      if (! ComparisonUtil.equalsWithNull(oldValue, $value)) {
        fireChangeEvent(new ActualDoubleEvent(DoubleSetComputationBeed.this, oldValue, $value, event.getEdit()));
      }
    }

  };


  /*<property name="value*/
  //------------------------------------------------------------------

  public final Double getDouble() {
    return $value;
  }


  /**
   * The value of this beed is recalculated.
   */
  public abstract void recalculate();


  protected final void setValue(Double value) {
    $value = value;
  }


  /**
   * The value of this beed.
   */
  private Double $value = null;

  /*</property>*/


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
    getSource().toString(sb, level + 2);
  }

}

