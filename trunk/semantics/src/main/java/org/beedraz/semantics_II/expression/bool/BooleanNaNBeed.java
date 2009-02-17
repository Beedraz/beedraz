/*<license>
Copyright 2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $ by the authors mentioned below.

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
 * A beed that expresses whether the given beed has value {@link Double#NaN} or not.
 *
 * @invar getOperand() != null &&
 *        getOperand().isEffective() &&
 *        getOperand().getDouble().isNaN()
 *          ? get() == true
 *          : get() == false;
 */
@Copyright("2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 913 $",
         date     = "$Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $")
public class BooleanNaNBeed extends AbstractRealArgBooleanUnaryExpressionBeed {

  public BooleanNaNBeed() {
    this(null);
  }

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  public BooleanNaNBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @pre getOperand() != null;
   */
  @Override
  protected void recalculate() {
    if (getOperand() != null) {
      assignEffective(true);
      setValue(getOperand().isEffective() && getOperand().getDouble().isNaN());
    }
    else {
      assignEffective(false);
    }
  }

  /**
   * The operator of this binary expression.
   */
  @Override
  public String getOperatorString() {
    return "nan";
  }

  @Override
  protected boolean calculateValue(double operand) {
    throw new IllegalStateException();
  }

}

