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

package org.beedra_II.expression.number.real.double64.stat;


import org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic;
import org.beedra_II.expression.collection.set.SetBeed;
import org.beedra_II.expression.number.real.RealBeed;
import org.beedra_II.expression.number.real.double64.AbstractDoubleSetComputationBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * {@link AbstractDoubleSetComputationBeed} that delegates computation
 * to a Apache Jakarta Commons {@link StorelessUnivariateStatistic}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractDoubleCommonsMathSetComputationBeed extends AbstractDoubleSetComputationBeed {

  /**
   * @pre   sus != null;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public AbstractDoubleCommonsMathSetComputationBeed(StorelessUnivariateStatistic sus) {
    super();
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

