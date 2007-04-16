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

package org.beedra_II.property.number.real.double64.doublerootbeed;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.number.real.double64.AbstractTestDoubleConstantUnaryExpressionBeed;
import org.beedra_II.property.number.real.double64.DoubleRootBeed;
import org.junit.Test;


public abstract class AbstractTestDoubleRootBeed
    extends AbstractTestDoubleConstantUnaryExpressionBeed<DoubleRootBeed> {

  protected AbstractTestDoubleRootBeed(double constant) {
    super(constant);
  }

  @Override
  protected DoubleRootBeed createSubject(AggregateBeed owner) {
    return new DoubleRootBeed($constant);
  }

  @Override
  protected final Double expectedValueNotNull(Double argumentValue) {
    return Math.pow(argumentValue, 1 / $constant);
  }

  @Test
  public void testConstructor() {
    DoubleRootBeed drb = new DoubleRootBeed($constant);
    validateConstructor(drb);
  }

}
