/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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

package org.ppeew.smallfries_I;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A mapping maps a given element of type {@code _From_} to an
 * element of type {@code _To_}.
 *
 * @invar   (forAll _From_ from1; ;
 *             (forAll _From_ from2; ;)
 *               from1 != from2 ==> map(from1) != map(from2));
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@CvsInfo(revision    = "$Revision$",
         date        = "$Date$",
         state       = "$State$",
         tag         = "$Name$")
public interface Mapping<_From_, _To_> {

  /**
   * @basic
   * @pre from != null;
   */
  _To_ map(_From_ from);

}

