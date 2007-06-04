/*<license>
Copyright 2007 - $Date: 2007-05-08 16:19:51 +0200 (di, 08 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.collection.set;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Beed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date: 2007-05-08 16:19:51 +0200 (di, 08 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 852 $",
         date     = "$Date: 2007-05-08 16:19:51 +0200 (di, 08 mei 2007) $")
public class Tuple<_Element1_ extends Beed<?>, _Element2_ extends Beed<?>> {

  public Tuple(_Element1_ element1, _Element2_ element2) {
    $element1 = element1;
    $element2 = element2;
  }


  /*<property name="element1">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final _Element1_ getElement1() {
    return $element1;
  }

  private final _Element1_ $element1;

  /*</property>*/


  /*<property name="element2">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final _Element2_ getElement2() {
    return $element2;
  }

  private final _Element2_ $element2;

  /*</property>*/


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Tuple) {
      Tuple other = (Tuple) obj;
      return other.getElement1().equals(getElement1()) &&
             other.getElement2().equals(getElement2());
    }
    return false;
  }


}

