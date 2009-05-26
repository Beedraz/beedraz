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

package org.beedraz.semantics_II.expression;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.indent;

import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Abstract implementation of expression beeds.
 *
 * @invar isEffective() ?? get() != null;
 *
 * @mudo Isn't the clue of this class originally that is works for expressions of primitive type?
 *       Where we use a isEffective method? Shouldn't the name reflect this?
 *       And now the clue is also that the beed is dependent! Name should reflect this too.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractPrimitiveDependentExprBeed<_Result_ extends Object,
                                                         _ResultEvent_ extends Event>
    extends AbstractDependentExpressionBeed<_ResultEvent_> {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractPrimitiveDependentExprBeed(AggregateBeed owner) {
    super(owner);
  }


  /**
   * Return the value of this beed, as an object.
   *
   * @basic
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

