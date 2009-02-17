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
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.path.ConstantPath;
import org.beedraz.semantics_II.path.NullPath;
import org.beedraz.semantics_II.path.Path;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestDoubleSumBeed
    extends AbstractTestRealArgDoubleBinaryExpressionBeed<DoubleSumBeed> {

  @Test
  public void testConstructor() {
    DoubleSumBeed beed = new DoubleSumBeed();
    assertNull(beed.getLeftOperand());
    assertNull(beed.getRightOperand());
    assertNull(beed.getDouble());
  }

  @Override
  protected Double expectedValue(Double leftOperandValue, Double rightOperandValue) {
    return leftOperandValue + rightOperandValue;
  }

  @Override
  protected DoubleSumBeed createSubject() {
    return new DoubleSumBeed();
  }

  @Override
  protected Double valueFromSubject(DoubleSumBeed operandBeed) {
    return operandBeed.getDouble();
  }

  @Override
  protected RealBeed<?> getLeftOperand() {
    return $subject.getLeftOperand();
  }

  @Override
  protected RealBeed<?> getRightOperand() {
    return $subject.getRightOperand();
  }

  @Override
  protected void setLeftOperandPath(Path<? extends RealBeed<?>> leftOperandPath) {
    $subject.setLeftOperandPath(leftOperandPath);
  }

  @Override
  protected void setRightOperandPath(Path<? extends RealBeed<?>> rightOperandPath) {
    $subject.setRightOperandPath(rightOperandPath);
  }

  /**
   * Bug: difference of null and NaN is 0, and should be null.
   */
  @Test
  public void testBug1() throws EditStateException, IllegalEditException {
    DoubleSumBeed ddb = new DoubleSumBeed();
    ddb.setLeftOperandPath(new NullPath<RealBeed<?>>());
    EditableDoubleBeed edb = new EditableDoubleBeed($aggregateBeed);
    DoubleEdit edit = new DoubleEdit(edb);
    edit.setGoal(Double.NaN);
    edit.perform();
    ddb.setRightOperandPath(new ConstantPath<RealBeed<?>>(edb));
    assertNull(ddb.getDouble());
  }

}
