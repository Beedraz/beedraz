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

import org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.expression.number.real.double64.AbstractDoubleSetComputationBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * {@link AbstractDoubleSetComputationBeed} that delegates computation
 * to a Apache Jakarta Commons {@link StorelessUnivariateStatistic}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractDoubleCommonsMathSetComputationBeed extends AbstractDoubleSetComputationBeed {

  /**
   * @pre   sus != null;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractDoubleCommonsMathSetComputationBeed(StorelessUnivariateStatistic sus, AggregateBeed owner) {
    super(owner);
    assert sus != null;
    $calculator = sus;
  }

  /**
   * The value of this beed is recalculated.
   */
  @Override
  protected final void recalculate(SetBeed<RealBeed<?>, ?> source) {
    $calculator.clear();
    for (RealBeed<?> realBeed : source.get()) {
      if (! realBeed.isEffective()) {
        assignEffective(false);
        return;
      }
      $calculator.increment(realBeed.getdouble());
    }
    assignValue($calculator.getResult());
    assignEffective(true);
  }

  // MUDO use second moment and merge with standard error

  private final StorelessUnivariateStatistic $calculator;

}

