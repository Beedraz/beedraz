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

package org.beedra_II.property;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import org.beedra_II.Event;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of expression beeds.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractExprBeed<_Result_ extends Object,
                                       _ResultEvent_ extends Event>
    extends AbstractPropertyBeed<_ResultEvent_> {

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  get() == null;
   * @post  ! isEffective();
   */
  protected AbstractExprBeed(AggregateBeed owner) {
    super(owner);
  }

  protected abstract _ResultEvent_ createNewEvent(_Result_ oldValue, _Result_ newValue, Edit<?> edit);


  /*<property name="secondArgument">*/
  //------------------------------------------------------------------

  public final boolean isEffective() {
    return $effective;
  }

  protected void assignEffective(boolean effective) {
    $effective = effective;
  }

  private boolean $effective = false;

  /*</property>*/


  public abstract _Result_ get();

//  public final Double getDouble() {
//    return $effective ? Double.valueOf(getdouble()) : null;
//  }

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

