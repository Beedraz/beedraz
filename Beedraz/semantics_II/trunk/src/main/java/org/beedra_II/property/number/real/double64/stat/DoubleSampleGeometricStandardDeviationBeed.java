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


import org.beedra_II.property.number.real.double64.DoubleBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.GeometricStandardDeviation;


/**
 * A beed that computes the sample geometric standard deviation of a given set of beeds of type
 * {@link DoubleBeed}.
 *
 * sample_geometric_standard_deviation(x_1, ..., x_n) =
 *   Math.exp(sample_standard_deviation(Math.log(x_1), ..., Math.log(x_n)))
 *
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() == sample_geometric_standard_deviation { db.getDouble() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective, then the value
 *        of the sample geometric standard deviation beed is the sample geometric standard deviation of
 *        the values of all beeds in the given set.
 *        See {@link GeometricStandardDeviation} for more information.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleSampleGeometricStandardDeviationBeed extends AbstractDoubleCommonsMathSetComputationBeed {


  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoubleSampleGeometricStandardDeviationBeed() {
    super(new GeometricStandardDeviation(true));
  }

  @Override
  public final String getOperatorString() {
    return "sample_geometric_standard_deviation";
  }
}

