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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.AbstractUnaryExprBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Abstract implementation of unary expression beeds, that represent a value derived
 * from one operand of type {@link BooleanBeed}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractBooleanArgUnaryExprBeed<_Result_ extends Object,
                                                      _ResultEvent_ extends Event>
    extends AbstractUnaryExprBeed<_Result_, _ResultEvent_, BooleanBeed>  {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractBooleanArgUnaryExprBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected boolean hasEffectiveOperandValue() {
    return getOperand().isEffective();
  }

}

