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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.double64.EditableDoubleBeed;
import org.junit.Test;


public class TestBooleanLTBeed
    extends AbstractTestBooleanBinaryExpressionBeed<BooleanLTBeed> {

  @Test
  public void testConstructor() {
    BooleanLTBeed beed = new BooleanLTBeed($aggregateBeed);
    assertEquals($aggregateBeed, beed.getOwner());
    assertNull(beed.getFirstArgument());
    assertNull(beed.getSecondArgument());
    assertNull(beed.get());
  }

  @Override
  protected Boolean expectedValue(Double leftArgumentValue, Double rightArgumentValue) {
    return leftArgumentValue < rightArgumentValue;
  }

  @Override
  protected BooleanLTBeed createSubject(AggregateBeed owner) {
    return new BooleanLTBeed(owner);
  }

  @Override
  protected Boolean valueFromSubject(BooleanLTBeed argumentBeed) {
    return argumentBeed.get();
  }

  @Override
  protected RealBeed<?> getLeftArgument() {
    return $subject.getFirstArgument();
  }

  @Override
  protected RealBeed<?> getRightArgument() {
    return $subject.getSecondArgument();
  }

  @Override
  protected void setLeftArgument(EditableDoubleBeed leftArgument) {
    $subject.setFirstArgument(leftArgument);
  }

  @Override
  protected void setRightArgument(EditableDoubleBeed rightArgument) {
    $subject.setSecondArgument(rightArgument);
  }

}
