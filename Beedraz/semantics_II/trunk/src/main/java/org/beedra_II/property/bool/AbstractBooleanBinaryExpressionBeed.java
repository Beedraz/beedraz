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

package org.beedra_II.property.bool;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.RealEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of binary expression beeds, that represent a boolean value derived
 * from 2 arguments of type {@link RealBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBooleanBinaryExpressionBeed
    extends AbstractBinaryExpressionBeed<RealBeed<?>, RealEvent, RealBeed<?>, RealEvent>
    implements BooleanBeed {

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  getBoolean() == null;
   * @post  getLeftArgument() == null;
   * @post  getRightArgument() == null;
   */
  protected AbstractBooleanBinaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  public final boolean getboolean() {
    return $value;
  }

  private boolean $value;

  @Override
  public final Boolean get() {
    return getBoolean();
  }

  @Override
  protected final BooleanEvent createNewEvent(Boolean oldValue, Boolean newValue, Edit<?> edit) {
    return new BooleanEvent(this, oldValue, newValue, edit);
  }

  @Override
  protected final void recalculateFrom(RealBeed<?> leftArgument, RealBeed<?> rightArgument) {
    $value = calculateValue(leftArgument.getdouble(), rightArgument.getdouble());
  }

  protected abstract boolean calculateValue(double leftArgument, double rightArgument);

}

