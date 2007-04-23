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


import org.beedra_II.Event;
import org.beedra_II.expression.number.real.RealBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of unary expression beeds, that represent a value derived
 * from one operand of type {@link RealBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractRealArgUnaryExprBeed<_Result_ extends Object,
                                                   _ResultEvent_ extends Event,
                                                   _OperandBeed_ extends RealBeed<?>>
    extends AbstractUnaryExprBeed<_Result_, _ResultEvent_, _OperandBeed_>  {

  @Override
  protected boolean hasEffectiveOperand() {
    return getOperand().isEffective();
  }

}

