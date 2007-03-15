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
 * A filter criterion has a reference to an element and expresses whether
 * this element meets the filter criterion or not.
 *
 * @invar   getElement() != null;
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@CvsInfo(revision    = "$Revision$",
         date        = "$Date$",
         state       = "$State$",
         tag         = "$Name$")
public abstract class FilterCriterion<_Element_> {


  /**
   * @param  element
   * @pre    element != null;
   * @post   getElement() == null;
   */
  public FilterCriterion(_Element_ element) {
    $element = element;
  }


  /*<property name="element">*/
  //------------------------------------------------------------------

  /**
   * The element referenced by this filter criterion.
   *
   * @basic
   */
  public final _Element_ getElement() {
    return $element;
  }

  private _Element_ $element;

  /*</property>*/


  /**
   * Returns true when the element satisfies the filter criterion.
   * Returns false otherwise.
   *
   * @basic
   */
  public abstract boolean isValid();

}

