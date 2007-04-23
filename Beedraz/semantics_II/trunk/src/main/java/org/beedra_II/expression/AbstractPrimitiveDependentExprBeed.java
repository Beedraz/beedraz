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

package org.beedra_II.expression;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import org.beedra_II.AbstractDependentBeed;
import org.beedra_II.Event;
import org.beedra_II.edit.Edit;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of expression beeds.
 *
 * @invar isEffective() ?? get() != null;
 *
 * @mudo Isn't the clue of this class originally that is works for expressions of primitive type?
 *       Where we use a isEffective method? Shouldn't the name reflect this?
 *       And now the clue is also that the beed is dependent! Name should reflect this too.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractPrimitiveDependentExprBeed<_Result_ extends Object,
                                                         _ResultEvent_ extends Event>
    extends AbstractDependentBeed<_ResultEvent_> {

  /**
   * Return the value of this beed, as an object.
   *
   * @basic
   *
   * @mudo Isn't this a {@link SimpleBeed} then?
   */
  public abstract _Result_ get();

  protected abstract _ResultEvent_ createNewEvent(_Result_ oldValue, _Result_ newValue, Edit<?> edit);

  /**
   * Whether {@code r1} and {@code r2} should be considered semantically equivalent.
   *
   * @todo Why don't we use r1.equals(r2)?
   */
  protected abstract boolean equalValue(_Result_ r1, _Result_ r2);



  /*<property name="effective">*/
  //------------------------------------------------------------------

  /**
   * @basic
   * @init false;
   */
  public final boolean isEffective() {
    return $effective;
  }

  /**
   * @post isEffective() == effective;
   */
  protected void assignEffective(boolean effective) {
    $effective = effective;
  }

  private boolean $effective = false;

  /*</property>*/



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

