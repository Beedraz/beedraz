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

package org.beedra_II.property.number.real.double64.stat;


import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.number.real.double64.DoubleBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that computes the sample standard deviation of a given set of beeds of type
 * {@link DoubleBeed}.
 *
 * sample_standard_deviation(x_1, ..., x_n) = Math.sqrt(sum((x_i - mean)^2) / (n - 1))
 *
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() == sample_standard_deviation { db.getDouble() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective, then the value
 *        of the sample standard deviation beed is the sample standard deviation of
 *        the values of all beeds in the given set.
 *        The sample standard deviation of an empty set is {@link Double#NaN}.
 *        The sample standard deviation of a set containing only one element is 0.
 *        e.g. when  getSource() = {1, 2, 3, 4}
 *             then  getDouble() = Math.sqrt(dividend/divisor)
 *             where divisor = 3
 *             and   dividend = (1-mean)^2 + (2-mean)^2 + (3-mean)^2 + (4-mean)^2)
 *             and   mean = (1 + 2 + 3 + 4)/4
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleSampleStandardDeviationBeed extends AbstractDoubleCommonsMathSetComputationBeed {


  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoubleSampleStandardDeviationBeed(AggregateBeed owner) {
    super(owner, new StandardDeviation(true));
  }

}

