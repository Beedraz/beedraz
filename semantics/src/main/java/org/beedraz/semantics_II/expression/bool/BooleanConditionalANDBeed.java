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

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A binary expression representing the conditional AND.
 *
 *  left operand  |  left value  | right operand |  right value  |  AND
 * ----------------------------------------------------------------------
 *       null     |      x       |       x       |       x       |  null
 *     not null   |    null      |       x       |       x       |  null
 *     not null   |      F       |       x       |       x       |   F
 *     not null   |      T       |      null     |       x       |  null
 *     not null   |      T       |   not null    |     null      |  null
 *     not null   |      T       |   not null    |       F       |   F
 *     not null   |      T       |   not null    |       T       |   T
 *
 * @invar getLeftOperand() == null || getLeftOperand().get() == null
 *          ==> get() == null;
 * @invar getLeftOperand() != null && getLeftOperand().get() == false
 *          ==> get() == false;
 * @invar getLeftOperand() != null && getLeftOperand().get() == true && getRightOperand() == null
 *          ==> get() == null;
 * @invar getLeftOperand() != null && getLeftOperand().get() == true && getRightOperand().get() == null
 *          ==> get() == null;
 * @invar getLeftOperand() != null && getLeftOperand().get() == true && getRightOperand().get() == false
 *          ==> get() == false;
 * @invar getLeftOperand() != null && getLeftOperand().get() == true && getRightOperand().get() == true
 *          ==> get() == true;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class BooleanConditionalANDBeed extends AbstractBooleanConditionalBinaryExpressionBeed {


  public BooleanConditionalANDBeed() {
    this(null);
  }

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  public BooleanConditionalANDBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected boolean isDecisive(BooleanBeed leftOperand) {
    return !leftOperand.getboolean();
  }


  @Override
  protected void recalculateConditionalFrom(BooleanBeed leftOperand, BooleanBeed rightOperand) {
    setValue(leftOperand.getboolean() && rightOperand.getboolean());
  }


  @Override
  public final String getOperatorString() {
    return "&&";
  }

}

