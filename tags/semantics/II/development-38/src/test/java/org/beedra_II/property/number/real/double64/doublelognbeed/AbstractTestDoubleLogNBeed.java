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

package org.beedra_II.property.number.real.double64.doublelognbeed;


import static org.junit.Assert.assertEquals;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.property.number.real.double64.AbstractTestDoubleConstantUnaryExpressionBeed;
import org.beedra_II.property.number.real.double64.DoubleConstantBeed;
import org.beedra_II.property.number.real.double64.DoubleLnBeed;
import org.beedra_II.property.number.real.double64.DoubleLog10Beed;
import org.beedra_II.property.number.real.double64.DoubleLogNBeed;
import org.junit.Test;


public abstract class AbstractTestDoubleLogNBeed
    extends AbstractTestDoubleConstantUnaryExpressionBeed<DoubleLogNBeed> {

  protected AbstractTestDoubleLogNBeed(double constant) {
    super(constant);
  }

  @Test
  public void testConstructor() {
    DoubleLogNBeed lb = new DoubleLogNBeed($aggregateBeed, $constant);
    validateConstructor(lb);
  }

  @Override
  protected DoubleLogNBeed createSubject(AggregateBeed owner) {
    return new DoubleLogNBeed(owner, $constant);
  }

  @Override
  protected final Double expectedValue(Double argumentValue) {
    return Math.log(argumentValue) / Math.log($constant);
  }

  @Test
  public void compareWithLnBeed() {
    double[] values =
      new double[] {1, 3.3, 4.4, 55.55, Double.NaN,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
    DoubleLnBeed lnBeed = new DoubleLnBeed(new StubBeanBeed());
    DoubleLogNBeed logNBeed = new DoubleLogNBeed(new StubBeanBeed(), Math.E);
    for (double d : values) {
      DoubleConstantBeed constantBeed = new DoubleConstantBeed(new StubBeanBeed(), d);
      lnBeed.setArgument(constantBeed);
      logNBeed.setArgument(constantBeed);
      assertEquals(lnBeed.getDouble(), logNBeed.getDouble());
    }
  }

  @Test
  public void compareWithLog10Beed() {
    double[] values =
      new double[] {1, 3.3, 4.4, 55.55, Double.NaN,
                    Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
    DoubleLog10Beed log10Beed = new DoubleLog10Beed(new StubBeanBeed());
    DoubleLogNBeed logNBeed = new DoubleLogNBeed(new StubBeanBeed(), 10);
    for (double d : values) {
      DoubleConstantBeed constantBeed = new DoubleConstantBeed(new StubBeanBeed(), d);
      log10Beed.setArgument(constantBeed);
      logNBeed.setArgument(constantBeed);
      assertEquals(log10Beed.getDouble(), logNBeed.getDouble());
    }
  }

}
