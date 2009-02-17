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

package org.beedraz.semantics_II.expression.bool;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.AbstractBinaryExprBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Abstract implementation of binary expression beeds, that represent a value derived
 * from two operands of type {@link BooleanBeed}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractBooleanArgBinaryExprBeed<
                                            _Result_ extends Object,
                                            _ResultEvent_ extends Event>
    extends AbstractBinaryExprBeed<_Result_,
                                   _ResultEvent_,
                                   BooleanBeed,
                                   BooleanEvent,
                                   BooleanBeed,
                                   BooleanEvent>  {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractBooleanArgBinaryExprBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected boolean hasEffectiveLeftOperand() {
    return getLeftOprnd().isEffective();
  }

  @Override
  protected boolean hasEffectiveRightOperand() {
    return getRightOprnd().isEffective();
  }

}

