/*<license>
Copyright 2007 - $Date: 2007-05-24 17:33:02 +0200 (do, 24 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.enumeration;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.AbstractBinaryExprBeed;
import org.beedraz.semantics_II.expression.enumeration.EnumEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Abstract implementation of binary expression beeds, that represent a value derived
 * from two operands of type {@link EnumBeed}.
 *
 * @author  Nele Smeets
 * @author  Jan Dockx
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date: 2007-05-24 17:33:02 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 920 $",
         date     = "$Date: 2007-05-24 17:33:02 +0200 (do, 24 mei 2007) $")
public abstract class AbstractEnumArgBinaryExprBeed<_Enum_ extends Enum<_Enum_>,
                                                    _Result_ extends Object,
                                                    _ResultEvent_ extends Event>

    extends AbstractBinaryExprBeed<_Result_,
                                  _ResultEvent_,
                                  EnumBeed<_Enum_>,
                                  EnumEvent<_Enum_>,
                                  EnumBeed<_Enum_>,
                                  EnumEvent<_Enum_>>  {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractEnumArgBinaryExprBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected boolean hasEffectiveLeftOperand() {
    return getLeftOprnd().get() != null;
  }

  @Override
  protected boolean hasEffectiveRightOperand() {
    return getRightOprnd().get() != null;
  }

}

