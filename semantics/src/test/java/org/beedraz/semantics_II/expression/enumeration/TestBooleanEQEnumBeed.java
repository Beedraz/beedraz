/*<license>
 Copyright 2007 - $Date: 2007-05-08 16:27:52 +0200 (di, 08 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.enumeration;


import static org.junit.Assert.assertNull;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.path.Path;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-08 16:27:52 +0200 (di, 08 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 854 $",
         date     = "$Date: 2007-05-08 16:27:52 +0200 (di, 08 mei 2007) $")
public class TestBooleanEQEnumBeed
    extends AbstractTestEnumArgBooleanBinaryExpressionBeed<BooleanEQEnumBeed<StubEnum>> {

  @Test
  public void testConstructor() {
    BooleanEQEnumBeed<StubEnum> beed = new BooleanEQEnumBeed<StubEnum>();
    assertNull(beed.getLeftOperand());
    assertNull(beed.getRightOperand());
    assertNull(beed.get());
  }

  @Override
  protected Boolean expectedValue(StubEnum leftOperandValue, StubEnum rightOperandValue) {
    return leftOperandValue == rightOperandValue;
  }

  @Override
  protected BooleanEQEnumBeed<StubEnum> createSubject() {
    return new BooleanEQEnumBeed<StubEnum>();
  }

  @Override
  protected Boolean valueFromSubject(BooleanEQEnumBeed<StubEnum> operandBeed) {
    return operandBeed.get();
  }

  @Override
  protected EnumBeed<StubEnum> getLeftOperand() {
    return $subject.getLeftOperand();
  }

  @Override
  protected EnumBeed<StubEnum> getRightOperand() {
    return $subject.getRightOperand();
  }

  @Override
  protected void setLeftOperandPath(Path<? extends EnumBeed<StubEnum>> leftOperandPath) {
    $subject.setLeftOperandPath(leftOperandPath);
  }

  @Override
  protected void setRightOperandPath(Path<? extends EnumBeed<StubEnum>> rightOperandPath) {
    $subject.setRightOperandPath(rightOperandPath);
  }

}
