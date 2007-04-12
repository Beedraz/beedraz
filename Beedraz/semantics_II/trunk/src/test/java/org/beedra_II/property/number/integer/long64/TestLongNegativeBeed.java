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

package org.beedra_II.property.number.integer.long64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.number.integer.long64.LongNegativeBeed;
import org.junit.Test;




public class TestLongNegativeBeed
    extends AbstractTestLongUnaryExpressionBeed<LongNegativeBeed> {

  @Test
  public void testConstructor() {
    LongNegativeBeed inb = new LongNegativeBeed($aggregateBeed);
    assertEquals($aggregateBeed, inb.getOwner());
    assertNull(inb.getArgument());
    assertNull(inb.getLong());
    assertNull(inb.getLong());
  }

  @Override
  protected Long expectedValueNotNull(Long argumentValue) {
    return - argumentValue;
  }

  @Override
  protected LongNegativeBeed createSubject(AggregateBeed owner) {
    return new LongNegativeBeed(owner);
  }

  @Override
  protected Long valueFromSubject(LongNegativeBeed argumentBeed) {
    return argumentBeed.getLong();
  }

}
