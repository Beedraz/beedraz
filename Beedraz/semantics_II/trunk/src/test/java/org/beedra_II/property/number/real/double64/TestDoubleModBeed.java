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

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.number.real.RealBeed;
import org.junit.Test;




public class TestDoubleModBeed
    extends AbstractTestDoubleBinaryExpressionBeed<DoubleModBeed> {

  @Test
  public void testConstructor() {
    DoubleModBeed beed = new DoubleModBeed($aggregateBeed);
    assertEquals($aggregateBeed, beed.getOwner());
    assertNull(beed.getDividend());
    assertNull(beed.getDivisor());
    assertNull(beed.getDouble());
  }

  @Override
  protected Double expectedValue(Double leftArgumentValue, Double rightArgumentValue) {
    return leftArgumentValue % rightArgumentValue;
  }

  @Override
  protected DoubleModBeed createSubject(AggregateBeed owner) {
    return new DoubleModBeed(owner);
  }

  @Override
  protected Double valueFromSubject(DoubleModBeed argumentBeed) {
    return argumentBeed.getDouble();
  }

  @Override
  protected RealBeed<?> getLeftArgument() {
    return $subject.getDividend();
  }

  @Override
  protected RealBeed<?> getRightArgument() {
    return $subject.getDivisor();
  }

  @Override
  protected void setLeftArgument(EditableDoubleBeed leftArgument) {
    $subject.setDividend(leftArgument);
  }

  @Override
  protected void setRightArgument(EditableDoubleBeed rightArgument) {
    $subject.setDivisor(rightArgument);
  }

  /**
   * Bug: mod of null and NaN is 0, and should be null.
   */
  @Test
  public void testBug1() throws EditStateException, IllegalEditException {
    DoubleModBeed dmb = new DoubleModBeed($aggregateBeed);
    dmb.setDividend(null);
    EditableDoubleBeed edb = new EditableDoubleBeed($aggregateBeed);
    DoubleEdit edit = new DoubleEdit(edb);
    edit.setGoal(Double.NaN);
    edit.perform();
    dmb.setDivisor(edb);
    assertNull(dmb.getDouble());
  }
}
