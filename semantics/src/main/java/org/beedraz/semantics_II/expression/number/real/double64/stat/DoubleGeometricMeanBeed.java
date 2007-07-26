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

package org.beedraz.semantics_II.expression.number.real.double64.stat;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.apache.commons.math.stat.descriptive.moment.GeometricMean;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A beed that computes the geometric mean of a given set of beeds of type
 * {@link org.beedraz.semantics_II.expression.number.real.RealBeed}.
 *
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() == geometric-mean { db.getDouble() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective,
 *        then the value of the geometric mean beed is the geometric mean of
 *        the values of all beeds in the given set. The mean of an empty set is NaN.
 *        e.g. getDouble() = (5.1 * 3.2 * 4.9) ^ (1/3)
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoubleGeometricMeanBeed extends AbstractDoubleCommonsMathSetComputationBeed {

  /**
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoubleGeometricMeanBeed() {
    this(null);
  }

  /**
   * @post  getSource() == null;
   * @post  getDouble() == null;
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  public DoubleGeometricMeanBeed(AggregateBeed owner) {
    super(new GeometricMean(), owner);
  }

  @Override
  public final String getOperatorString() {
    return "geometric_mean";
  }

}

