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

package org.beedraz.semantics_II.expression.number.real.double64;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.expression.number.real.double64.AbstractDoubleConstantUnaryExpressionBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractTestDoubleConstantUnaryExpressionBeed<_DoubleBeed_ extends AbstractDoubleConstantUnaryExpressionBeed>
    extends AbstractTestRealArgDoubleUnaryExpressionBeed<_DoubleBeed_> {

  protected AbstractTestDoubleConstantUnaryExpressionBeed(double constant) {
    $constant = constant;
  }

  protected double $constant;

  @Override
  protected final Double valueFromSubject(_DoubleBeed_ operandBeed) {
    return operandBeed.getDouble();
  }

  protected final void validateConstructor(_DoubleBeed_ inb) {
    assertNull(inb.getOperand());
    assertNull(inb.getDouble());
    assertEquals($constant, inb.getConstant());
  }

}
