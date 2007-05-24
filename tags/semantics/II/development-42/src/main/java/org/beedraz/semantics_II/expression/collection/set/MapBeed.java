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

package org.beedraz.semantics_II.expression.collection.set;


import java.util.Set;

import org.beedraz.semantics_II.expression.ExpressionBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that maps keys to values.
 * A map cannot contain duplicate keys; each key can map to at most one value.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar   keySet() != null;
 * @invar   !keySet().contains(null);
 * @invar   (forAll _Key_ key; !keySet().contains(key); get(key) == null);
 *
 * @mudo move to collection package; this is not a set
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State: Exp $",
         tag      = "$Name:  $")
public interface MapBeed<_Key_,
                         _Value_,
                         _Event_ extends MapEvent<_Key_, _Value_>>
  extends ExpressionBeed<_Event_> {

  /**
   * @basic
   */
  _Value_ get(_Key_ key);

  /**
   * @basic
   */
  Set<_Key_> keySet();

}

