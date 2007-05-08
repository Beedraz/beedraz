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

package org.beedra_II.property.set;


import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A filter criterion factory creates a {@link FilterCriterion}
 * for a given element.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface FilterCriterionFactory<_Element_> {

  /**
   * Create a filter criterion for the given element.
   *
   * @result  result != null;
   * @result  result.getElement() == element;
   */
  FilterCriterion<_Element_> createFilterCriterion(_Element_ element);
}

