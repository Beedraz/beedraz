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
import org.beedra_II.property.AbstractUnaryExprBeed;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.RealEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * Abstract implementation of unary expression beeds, that represent a value
 * of type {@link Boolean} derived from one argument of type {@link RealBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractUnaryExpressionBeed<_ArgumentBeed_ extends RealBeed<? extends _ArgumentEvent_>,
                                                  _ArgumentEvent_ extends RealEvent>
    extends AbstractUnaryExprBeed<Boolean,
                                  BooleanEvent,
                                  _ArgumentBeed_,
                                  _ArgumentEvent_>
    implements BooleanBeed{

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  getArgument() == null;
   * @post  get() == null;
   */
  public AbstractUnaryExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  public final Boolean getBoolean() {
    return isEffective() ? Boolean.valueOf(getboolean()) : null;
  }

  @Override
  protected boolean equalValue(Boolean b1, Boolean b2) {
    return MathUtil.equalValue(b1, b2);
  }

}

