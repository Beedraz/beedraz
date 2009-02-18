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

import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleSampleVarianceBeed;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.MathUtil;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestDoubleSampleVarianceBeed
    extends AbstractTestDoubleCommonsMathSetComputationBeed<MyDoubleSampleVarianceBeed>{

  @Test
  public void forceTest() {
    // to let the compiler know that this is a unit test
  }

  @Override
  protected double computeStatistic(double... values) {
    return MathUtil.sampleVariance(values);
  }

  @Override
  protected MyDoubleSampleVarianceBeed createSubject() {
    return new MyDoubleSampleVarianceBeed();
  }

  @Override
  protected void recalculate(MyDoubleSampleVarianceBeed subject) {
    subject.publicRecalculate();
  }

  @Override
  protected void updateDependents(MyDoubleSampleVarianceBeed subject, Event event) {
    subject.publicUpdateDependents(event);
  }

}

class MyDoubleSampleVarianceBeed extends DoubleSampleVarianceBeed {
  public MyDoubleSampleVarianceBeed() {
    super();
  }

  /**
   * updateDependents is made public for testing reasons
   */
  public void publicUpdateDependents(Event event) {
    updateDependents(event);
  }

  public final void publicRecalculate() {
    assert getSource() != null;
    recalculate(getSource());
  }

}
