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

package org.beedra_II.property.set.ordered;


import java.util.List;

import org.beedra_II.property.simple.SimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A {@link SimplePropertyBeed} whose value is of type {@link List}
 * and that sends events of type {@link OrderedSetEvent}
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar   get() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface OrderedSetBeed<_Element_>
    extends SimplePropertyBeed<List<_Element_>, OrderedSetEvent<_Element_>> {

  // NOP

}

