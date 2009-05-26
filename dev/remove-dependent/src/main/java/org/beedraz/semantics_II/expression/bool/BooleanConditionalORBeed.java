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

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * A binary expression representing the conditional OR.
 *
 *  left operand  |  left value  | right operand |  right value  |  OR
 * ----------------------------------------------------------------------
 *       null     |      x       |       x       |       x       |  null
 *     not null   |    null      |       x       |       x       |  null
 *     not null   |      T       |       x       |       x       |   T
 *     not null   |      F       |      null     |       x       |  null
 *     not null   |      F       |   not null    |     null      |  null
 *     not null   |      F       |   not null    |       F       |   F
 *     not null   |      F       |   not null    |       T       |   T
 *
 * @invar getLeftOperand() == null || getLeftOperand().get() == null
 *          ==> get() == null;
 * @invar getLeftOperand() != null && getLeftOperand().get() == true
 *          ==> get() == true;
 * @invar getLeftOperand() != null && getLeftOperand().get() == false && getRightOperand() == null
 *          ==> get() == null;
 * @invar getLeftOperand() != null && getLeftOperand().get() == false && getRightOperand().get() == null
 *          ==> get() == null;
 * @invar getLeftOperand() != null && getLeftOperand().get() == false && getRightOperand().get() == false
 *          ==> get() == false;
 * @invar getLeftOperand() != null && getLeftOperand().get() == false && getRightOperand().get() == true
 *          ==> get() == true;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class BooleanConditionalORBeed extends AbstractBooleanConditionalBinaryExpressionBeed {


  public BooleanConditionalORBeed() {
    this(null);
  }

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  public BooleanConditionalORBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected boolean isDecisive(BooleanBeed leftOperand) {
    return leftOperand.getboolean();
  }


  @Override
  protected void recalculateConditionalFrom(BooleanBeed leftOperand, BooleanBeed rightOperand) {
    setValue(leftOperand.getboolean() || rightOperand.getboolean());
  }


  @Override
  public final String getOperatorString() {
    return "||";
  }

}

