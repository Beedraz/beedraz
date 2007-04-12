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
import org.junit.Test;


public class TestBooleanNotNullBeed
    extends AbstractTestBooleanUnaryExpressionBeed<BooleanNotNullBeed> {

  @Test
  public void testConstructor() {
    BooleanGTBeed beed = new BooleanGTBeed($aggregateBeed);
    assertEquals($aggregateBeed, beed.getOwner());
    assertNull(beed.getLeftArgument());
    assertNull(beed.getRightArgument());
    assertNull(beed.get());
  }

  @Override
  protected Boolean expectedValueNotNull(Double argumentValue) {
    return argumentValue != null;
  }

  @Override
  protected Boolean expectedValueNull() {
    return false;
  }

  @Override
  protected BooleanNotNullBeed createSubject(AggregateBeed owner) {
    return new BooleanNotNullBeed(owner);
  }

  @Override
  protected final Boolean valueFromSubject(BooleanNotNullBeed argumentBeed) {
    return argumentBeed.getBoolean();
  }

  @Override
  protected void checkNewValueNull(BooleanEvent event) {
    assertEquals(false, newValueFrom(event));
  }

  @Override
  protected void checkOldValueNull(BooleanEvent event) {
    assertEquals(false, oldValueFrom(event));
  }

  @Override
  protected void checkArgumentValueNull() {
    assertEquals(false, $subject.get());
  }

}
