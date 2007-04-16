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

public class TestBooleanXORBeed
    extends AbstractTestBooleanBinaryLogicalExpressionBeed<BooleanXORBeed> {

  @Test
  public void testConstructor() {
    BooleanXORBeed beed = new BooleanXORBeed($aggregateBeed);
    assertEquals($aggregateBeed, beed.getOwner());
    assertNull(beed.getLeftArgument());
    assertNull(beed.getRightArgument());
    assertNull(beed.get());
  }

  @Override
  protected Boolean expectedValue(Boolean leftArgumentValue, Boolean rightArgumentValue) {
    return leftArgumentValue ^ rightArgumentValue;
  }

  @Override
  protected BooleanXORBeed createSubject(AggregateBeed owner) {
    return new BooleanXORBeed(owner);
  }

}
