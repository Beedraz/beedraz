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

package org.beedra_II.property.bool;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TestBooleanConditionalORBeed
    extends AbstractTestBooleanConditionalBinaryExpressionBeed<BooleanConditionalORBeed> {

  @Test
  public void testConstructor() {
    BooleanConditionalORBeed beed = new BooleanConditionalORBeed();
    assertNull(beed.getLeftOperand());
    assertNull(beed.getRightOperand());
    assertNull(beed.get());
  }

  @Override
  protected BooleanConditionalORBeed createSubject() {
    return new BooleanConditionalORBeed();
  }

  @Override
  protected boolean isDecisive(boolean leftValue) {
    return leftValue;
  }

  @Override
  protected boolean expectedEffectiveValue(boolean leftValue, boolean rightValue) {
    return leftValue || rightValue;
  }

}
