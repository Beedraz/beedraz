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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.GeometricStandardError;


/**
 * A beed that computes the population geometric standard error of a given set of beeds of type
 * {@link org.beedraz.semantics_II.expression.number.real.RealBeed}.
 *
 * population_geometric_standard_error(x_1, ..., x_n) =
 *   Math.exp(population_standard_error(Math.log(x_1), ..., Math.log(x_n)))
 *
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() == population_geometric_standard_error { db.getDouble() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective, then the value
 *        of the population geometric standard error beed is the population geometric standard error of
 *        the values of all beeds in the given set.
 *        See {@link GeometricStandardError} for more information.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoublePopulationGeometricStandardErrorBeed extends AbstractDoubleCommonsMathSetComputationBeed {


  /**
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoublePopulationGeometricStandardErrorBeed() {
    this(null);
  }

  /**
   * @post  getSource() == null;
   * @post  getDouble() == null;
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  public DoublePopulationGeometricStandardErrorBeed(AggregateBeed owner) {
    super(new GeometricStandardError(false), owner);
  }

  @Override
  public final String getOperatorString() {
    return "population_geometric_standard_error";
  }

}

