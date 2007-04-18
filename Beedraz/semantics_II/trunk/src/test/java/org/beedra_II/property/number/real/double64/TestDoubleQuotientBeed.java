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

import static org.junit.Assert.assertNull;

import org.beedra_II.path.Path;
import org.beedra_II.property.number.real.RealBeed;
import org.junit.Test;




public class TestDoubleQuotientBeed
    extends AbstractTestRealArgDoubleBinaryExpressionBeed<DoubleQuotientBeed> {

  @Test
  public void testConstructor() {
    DoubleQuotientBeed beed = new DoubleQuotientBeed();
    assertNull(beed.getNumerator());
    assertNull(beed.getDenominator());
    assertNull(beed.getDouble());
  }

  @Override
  protected Double expectedValue(Double leftOperandValue, Double rightOperandValue) {
    return leftOperandValue / rightOperandValue;
  }

  @Override
  protected DoubleQuotientBeed createSubject() {
    return new DoubleQuotientBeed();
  }

  @Override
  protected Double valueFromSubject(DoubleQuotientBeed operandBeed) {
    return operandBeed.getDouble();
  }

  @Override
  protected RealBeed<?> getLeftOperand() {
    return $subject.getNumerator();
  }

  @Override
  protected RealBeed<?> getRightOperand() {
    return $subject.getDenominator();
  }

  @Override
  protected void setLeftOperandPath(Path<? extends RealBeed<?>> leftOperandPath) {
    $subject.setNumeratorPath(leftOperandPath);
  }

  @Override
  protected void setRightOperandPath(Path<? extends RealBeed<?>> rightOperandPath) {
    $subject.setDenominatorPath(rightOperandPath);
  }

}
