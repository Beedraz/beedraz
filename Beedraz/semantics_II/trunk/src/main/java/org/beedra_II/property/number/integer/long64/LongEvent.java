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


import org.beedra_II.property.number.real.double64.DoubleEvent;
import org.beedra_II.property.simple.OldNewEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * {@link OldNewEvent} whose source is a {@link LongBeed} and
 * that carries a simple old and new value of type {@link Integer}.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof LongBeed;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface LongEvent extends DoubleEvent {

  /**
   * @return getNewValue();
   */
  Integer getNewInteger();

  /**
   * @result MathUtil.equalValue(getNewInteger(), result);
   */
  Double getNewDouble();

  /**
   * @return getOldValue();
   */
  Integer getOldInteger();
  /**
   * @result MathUtil.equalValue(getOldInteger(), result);
   */
  Double getOldDouble();

  /**
   * @return ((getOldValue() == null) || (getNewValue() == null)) ? null : getNewValue() - getOldValue();
   */
  Integer getIntegerDelta();

  /**
   * @result MathUtil.equalValue(getIntegerDelta(), result);
   */
  Double getDoubleDelta();

}

