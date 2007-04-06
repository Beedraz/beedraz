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

package org.beedra_II.property.number.real.double64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.beedra_II.property.number.real.double64.AbstractDoubleConstantUnaryExpressionBeed;





public abstract class AbstractTestDoubleConstantUnaryExpressionBeed<_DoubleBeed_ extends AbstractDoubleConstantUnaryExpressionBeed>
    extends AbstractTestDoubleUnaryExpressionBeed<_DoubleBeed_> {

  protected AbstractTestDoubleConstantUnaryExpressionBeed(double constant) {
    $constant = constant;
  }

  protected double $constant;

  @Override
  protected final Double valueFromSubject(_DoubleBeed_ argumentBeed) {
    return argumentBeed.getDouble();
  }

  protected final void validateConstructor(_DoubleBeed_ inb) {
    assertEquals($aggregateBeed, inb.getOwner());
    assertNull(inb.getArgument());
    assertNull(inb.getDouble());
    assertEquals($constant, inb.getConstant());
  }

}
