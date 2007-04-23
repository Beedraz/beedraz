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

package org.beedra_II.expression.number.integer;


import java.math.BigInteger;

import org.beedra_II.expression.number.real.RealEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Old-new like event whose source is an {@link IntegerBeed} and
 * that represents an old an new mathematical integer value.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof LongBeed;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface IntegerEvent extends RealEvent {

  /**
   * @basic
   */
  BigInteger getNewBigInteger();

  /**
   * @basic
   */
  Long getNewLong();

  /**
   * @result MathUtil.equalValue(getNewInteger(), result);
   */
  Double getNewDouble();

  /**
   * @basic
   */
  BigInteger getOldBigInteger();

  /**
   * @basic
   */
  Long getOldLong();

  /**
   * @result MathUtil.equalValue(getOldInteger(), result);
   */
  Double getOldDouble();

  /**
   * @return ((getOldBigInteger() == null) || (getNewBigInteger() == null)) ? null : getNewBigInteger() - getOldBigInteger();
   */
  BigInteger getBigIntegerDelta();

  /**
   * @return ((getOldLong() == null) || (getNewLong() == null)) ? null : getNewLong() - getOldLong();
   */
  Long getLongDelta();

  /**
   * @result MathUtil.equalValue(getIntegerDelta(), result);
   */
  Double getDoubleDelta();

}

