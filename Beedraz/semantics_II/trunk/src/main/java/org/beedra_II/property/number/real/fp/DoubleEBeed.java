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

package org.beedra_II.property.number.real.fp;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.decimal.DoubleBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>A beed that is the natural power of an {@link #getArgument() argument}
 *   {@link DoubleBeed} (<code>e<sup>x</sup></code>).</p>
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleEBeed
    extends AbstractDoubleUnaryExpressionBeed {

  /**
   * @pre   owner != null;
   * @post  getDouble() == null;
   * @post  getArgument() == null;
   */
  public DoubleEBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * @pre argumentValue != null;
   */
  @Override
  protected final Double calculateValue(Double argumentValue) {
    assert argumentValue != null;
    return Math.exp(argumentValue);
  }

}

