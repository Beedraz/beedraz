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


import static org.junit.Assert.assertNull;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.expression.number.real.double64.DoubleLog10Beed;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestDoubleLog10Beed
    extends AbstractTestRealArgDoubleUnaryExpressionBeed<DoubleLog10Beed> {

  @Test
  public void testConstructor() {
    DoubleLog10Beed log10Beed = new DoubleLog10Beed();
    assertNull(log10Beed.getOperand());
    assertNull(log10Beed.getDouble());
  }

  @Override
  protected Double expectedValueNotNull(Double operandValue) {
    return Math.log10(operandValue);
  }

  @Override
  protected DoubleLog10Beed createSubject() {
    return new DoubleLog10Beed();
  }

  @Override
  protected Double valueFromSubject(DoubleLog10Beed operandBeed) {
    return operandBeed.getDouble();
  }

}
