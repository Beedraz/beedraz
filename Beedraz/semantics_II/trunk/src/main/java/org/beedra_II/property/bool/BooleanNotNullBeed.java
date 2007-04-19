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


import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that expresses whether the given beed is effective or not.
 *
 * @invar getOperand() != null && getOperand().isEffective()
 *          ? get() == true
 *          : get() == false; // so, when the operand is effective, this beed is always effective
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanNotNullBeed extends AbstractRealArgBooleanUnaryExpressionBeed {

  /**
   * @pre getOperand() != null;
   */
  @Override
  protected void recalculate() {
    assignEffective(true);
    setValue(getOperand().isEffective());
  }

  /**
   * The operator of this binary expression.
   */
  @Override
  public String getOperatorString() {
    return "not_null";
  }

  @Override
  protected boolean calculateValue(double operand) {
    throw new IllegalStateException();
  }
}

