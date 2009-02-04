/*<license>
Copyright 2009 - $Date: 2007-05-24 17:18:53 +0200 (Thu, 24 May 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.number.real.double64;

import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;

/**
 * A beed that is the absolute value of an {@link #getOperand() operand} {@link DoubleBeed}.
 */
@Copyright("2009 - $Date: 2007-05-24 17:18:53 +0200 (Thu, 24 May 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 916 $",
         date     = "$Date: 2007-05-24 17:18:53 +0200 (Thu, 24 May 2007) $")
public class DoubleAbsoluteBeed extends AbstractRealArgDoubleUnaryExpressionBeed {

	  /**
	   * @post  getDouble() == null;
	   * @post  getOperand() == null;
	   */
	  public DoubleAbsoluteBeed() {
	    this(null);
	  }

	  /**
	   * @post  getDouble() == null;
	   * @post  getOperand() == null;
	   * @post  owner != null ? owner.registerAggregateElement(this);
	   */
	  public DoubleAbsoluteBeed(AggregateBeed owner) {
	    super(owner);
	  }

	  /**
	   * @pre operandValue != null;
	   */
	  @Override
	  protected final double calculateValue(double operandValue) {
	    return Math.abs(operandValue);
	  }

	  /**
	   * @mudo Fix operator string.
	   */
	  @Override
	  public final String getOperatorString() {
	    return "||";
	  }
}
