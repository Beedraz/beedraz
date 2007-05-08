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


import java.math.BigInteger;

import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.IntegerEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed containing a {@link Long} value.
 * Listeners of the beed can receive events of type
 * {@link IntegerEvent}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface LongBeed extends IntegerBeed<ActualLongEvent> {

  /**
   * @basic
   */
  Long getLong();

  /**
   * @result MathUtil.equalValue(result, getInteger());
   */
  BigInteger getBigInteger();

  /**
   * @result MathUtil.equalValue(result, getInteger());
   */
  Double getDouble();

}

