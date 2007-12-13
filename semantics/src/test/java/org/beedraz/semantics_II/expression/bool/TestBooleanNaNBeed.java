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

package org.beedraz.semantics_II.expression.bool;


import static org.junit.Assert.assertNull;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.expression.bool.BooleanNaNBeed;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-08 16:27:52 +0200 (di, 08 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 854 $",
         date     = "$Date: 2007-05-08 16:27:52 +0200 (di, 08 mei 2007) $")
public class TestBooleanNaNBeed
    extends AbstractTestRealArgBooleanUnaryExpressionBeed<BooleanNaNBeed> {

  @Test
  public void testConstructor() {
    BooleanNaNBeed beed = new BooleanNaNBeed();
    assertNull(beed.getOperand());
    assertNull(beed.get());
  }

  @Override
  protected Boolean expectedValueNotNull(Double operandValue) {
    return operandValue.isNaN();
  }

  @Override
  protected Boolean expectedValueNull() {
    return false;
  }

  @Override
  protected BooleanNaNBeed createSubject() {
    return new BooleanNaNBeed();
  }

  @Override
  protected final Boolean valueFromSubject(BooleanNaNBeed operandBeed) {
    return operandBeed.getBoolean();
  }

}
