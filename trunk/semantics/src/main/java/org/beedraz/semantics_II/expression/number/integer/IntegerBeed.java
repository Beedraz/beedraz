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


import java.math.BigInteger;

import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed representing an mathematical integer number.
 * Listeners of the beed can receive events of type
 * {@link IntegerEvent}. Integers cannot be represented
 * in a computer. For now, {@code IntegerBeeds} offer
 * support for both {@link BigInteger} and {@link Long}
 * representations. Note that using the first has a big
 * impact on performance.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @invar isEffective() ?? getBigInteger() != null;
 * @invar isEffective() ?? getLong() != null;
 * @invar equalValue(getLong(), getBigInteger());
 * @invar equalValue(getLong(), getDouble());
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface IntegerBeed<_Event_ extends IntegerEvent> extends RealBeed<_Event_> {

  /**
   * @basic
   */
  BigInteger getBigInteger();

  /**
   * @basic
   */
  long getlong();

  /**
   * @basic
   */
  Long getLong();

}

