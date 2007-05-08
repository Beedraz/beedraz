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

package org.beedra_II.property.number.decimal;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.property.decimal.ActualDoubleEvent;
import org.beedra_II.property.decimal.DoubleBeed;
import org.beedra_II.property.decimal.DoubleEvent;
import org.beedra_II.property.number.AbstractBinaryExpressionBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>General code for Double implementations of {@link AbstractBinaryExpressionBeed}.</p>
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractDoubleBinaryExpressionBeed
    extends AbstractBinaryExpressionBeed<Double, DoubleBeed<DoubleEvent>, DoubleBeed<DoubleEvent>, DoubleEvent>
    implements DoubleBeed<DoubleEvent> {

  /**
   * @pre   owner != null;
   * @post  getInteger() == null;
   * @post  getArgument() == null;
   */
  protected AbstractDoubleBinaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  public final Double getDouble() {
    return get();
  }

  /**
   * @post  result != null;
   * @post  result.getArgument() == this;
   * @post  result.getOldInteger() == null;
   * @post  result.getNewInteger() == getInteger();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final DoubleEvent createInitialEvent() {
    return new ActualDoubleEvent(this, null, get(), null);
  }

  @Override
  protected final DoubleEvent createNewEvent(Double oldValue, Double newValue, Edit<?> edit) {
    return new ActualDoubleEvent(this, oldValue, newValue, edit);
  }

  @Override
  protected final Double valueFromLeft(DoubleBeed<DoubleEvent> beed) {
    return beed.getDouble();
  }

  @Override
  protected final Double valueFromRight(DoubleBeed<DoubleEvent> beed) {
    return beed.getDouble();
  }

  @Override
  protected final Double newValueFrom(DoubleEvent event) {
    return event.getNewDouble();
  }

}

