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

package org.beedra_II.expression.bool;


import org.ppeew.annotations_I.vcs.CvsInfo;


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
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanConditionalANDBeed extends AbstractBooleanConditionalBinaryExpressionBeed {


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

