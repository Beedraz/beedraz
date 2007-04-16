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


import org.beedra_II.edit.Edit;
import org.beedra_II.property.AbstractBooleanArgUnaryExprBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * General code for Boolean implementations of {@link AbstractRealArgumentUnaryExpressionBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBooleanArgBooleanUnaryExpressionBeed
    extends AbstractBooleanArgUnaryExprBeed<Boolean,
                                            BooleanEvent>
    implements BooleanBeed {

  public final Boolean getBoolean() {
    return isEffective() ? Boolean.valueOf(getboolean()) : null;
  }

  @Override
  protected boolean equalValue(Boolean b1, Boolean b2) {
    return MathUtil.equalValue(b1, b2);
  }

  public final boolean getboolean() {
    return $value;
  }

  protected final void setValue(boolean value) {
    $value = value;
  }

  private boolean $value;

  @Override
  protected final BooleanEvent createNewEvent(Boolean oldValue, Boolean newValue, Edit<?> edit) {
    return new BooleanEvent(this, oldValue, newValue, edit);
  }

  @Override
  protected final void recalculateFrom(BooleanBeed argument) {
    $value = calculateValue(argument.getboolean());
  }

  protected abstract boolean calculateValue(boolean argument);

  @Override
  public final Boolean get() {
    return getBoolean();
  }

}

