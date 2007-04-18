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
 * A beed that expresses whether the first operand is less than or
 * equal to the second operand.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanLEBeed extends AbstractBooleanBinaryRelationalExpressionBeed {

  /**
   * @pre leftOperand != null;
   * @pre rightOperand != null;
   */
  @Override
  protected final boolean calculateValue(double leftOperand, double rightOperand) {
    return leftOperand <= rightOperand;
  }


  @Override
  public final String getOperatorString() {
    return "<=";
  }

}

