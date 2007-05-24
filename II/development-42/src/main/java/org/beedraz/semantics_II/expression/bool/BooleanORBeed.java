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
 * A binary expression representing the logical OR.
 *
 * The truth table of p OR q:
 *
 *   p    |  q    |  p OR q
 * -------------------------
 *   F    |  F    |     F
 *   F    |  T    |     T
 *   T    |  F    |     T
 *   T    |  T    |     T
 *
 * @invar getLeftOperand() != null && getRightOperand() != null
 *          ? ( getLeftOperand().get() == null || getRightOperand().get() == null
 *                ? get() == null
 *                : get() == getLeftOperand().get() || getRightOperand().get() )
 *          : get() == null;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class BooleanORBeed extends AbstractBooleanBinaryLogicalExpressionBeed {

  public BooleanORBeed() {
    this(null);
  }

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  public BooleanORBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @pre leftOperand != null;
   * @pre rightOperand != null;
   */
  @Override
  protected final boolean calculateValue(boolean leftOperand, boolean rightOperand) {
    return leftOperand || rightOperand;
  }


  @Override
  public final String getOperatorString() {
    return "||";
  }

}

