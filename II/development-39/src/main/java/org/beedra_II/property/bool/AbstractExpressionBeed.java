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


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.property.AbstractPropertyBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of boolean expression beeds.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractExpressionBeed
    extends AbstractPropertyBeed<BooleanEvent>
    implements BooleanBeed {

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  get() == null;
   * @post ! isEffective();
   */
  protected AbstractExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  protected abstract BooleanEvent createNewEvent(Boolean oldValue, Boolean newValue, Edit<?> edit);

  public final boolean isEffective() {
    return $effective;
  }

  protected void assignEffective(boolean effective) {
    $effective = effective;
  }

  private boolean $effective = false;

  public abstract Boolean get();

  public final Boolean getBoolean() {
    return $effective ? Boolean.valueOf(getboolean()) : null;
  }

  @Override
  protected final String otherToStringInformation() {
    return get() == null ? "null" : get().toString();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
  }

}

