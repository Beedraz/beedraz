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

package org.beedraz.semantics_II.expression.number.real;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.math.BigDecimal;

import org.beedraz.semantics_II.Event;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Old-new event like event whose source is a {@link RealBeed} and
 * that represents a simple old and new real value.
 *
 * @author  Jan Dockx
 *
 * @invar getSource() instanceof RealBeed;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface RealEvent extends Event {

  /**
   * @basic
   */
  Double getOldDouble();

  /**
   * @basic
   */
  Double getNewDouble();

  /**
   * @return (getOldDouble() == null) || (getNewDouble() == null)
   *             ? null
   *             : getNewDouble() - getOldDouble();
   */
  Double getDoubleDelta();

  /**
   * @basic
   */
  BigDecimal getOldBigDecimal();

  /**
   * @basic
   */
  BigDecimal getNewBigDecimal();

  /**
   * @return (getOldBigDecimal() == null) || (getNewBigDecimal() == null)
   *             ? null
   *             : getNewBigDecimal() - getOldBigDecimal();
   */
  BigDecimal getBigDecimalDelta();

}

