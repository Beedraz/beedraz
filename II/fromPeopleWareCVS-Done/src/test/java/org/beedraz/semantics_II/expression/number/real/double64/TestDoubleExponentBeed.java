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

import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleExponentBeed;
import org.beedraz.semantics_II.path.Path;
import org.junit.Test;




public class TestDoubleExponentBeed
    extends AbstractTestRealArgDoubleBinaryExpressionBeed<DoubleExponentBeed> {

  @Test
  public void testConstructor() {
    DoubleExponentBeed beed = new DoubleExponentBeed();
    assertNull(beed.getBase());
    assertNull(beed.getExponent());
    assertNull(beed.getDouble());
  }

  @Override
  protected Double expectedValue(Double leftOperandValue, Double rightOperandValue) {
    return Math.pow(leftOperandValue, rightOperandValue);
  }

  @Override
  protected DoubleExponentBeed createSubject() {
    return new DoubleExponentBeed();
  }

  @Override
  protected Double valueFromSubject(DoubleExponentBeed operandBeed) {
    return operandBeed.getDouble();
  }

  @Override
  protected RealBeed<?> getLeftOperand() {
    return $subject.getBase();
  }

  @Override
  protected RealBeed<?> getRightOperand() {
    return $subject.getExponent();
  }

  @Override
  protected void setLeftOperandPath(Path<? extends RealBeed<?>> leftOperandPath) {
    $subject.setBasePath(leftOperandPath);
  }

  @Override
  protected void setRightOperandPath(Path<? extends RealBeed<?>> rightOperandPath) {
    $subject.setExponentPath(rightOperandPath);
  }

}
