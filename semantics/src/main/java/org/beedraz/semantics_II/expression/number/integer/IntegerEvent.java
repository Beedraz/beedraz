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

package org.beedraz.semantics_II.expression.number.integer;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.math.BigInteger;

import org.beedraz.semantics_II.expression.number.real.RealEvent;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Old-new like event whose source is an {@link IntegerBeed} and
 * that represents an old an new mathematical integer value.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof LongBeed;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
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

