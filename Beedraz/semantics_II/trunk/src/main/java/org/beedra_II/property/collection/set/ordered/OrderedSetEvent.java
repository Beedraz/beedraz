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

package org.beedra_II.property.collection.set.ordered;


import java.util.List;
import java.util.Set;

import org.beedra_II.property.collection.list.ListEvent;
import org.beedra_II.property.collection.set.SetEvent;
import org.beedra_II.property.set.SetBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * Event that notifies of changes in a {@link SetBeed}.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof CollectionBeed
 * @invar getAddedElements() != null;
 * @invar getRemovedElements() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface OrderedSetEvent<_Element_, _OrderedSet_ extends Set<_Element_> & List<_Element_>>
    extends SetEvent<_Element_, _OrderedSet_>, ListEvent<_Element_, _OrderedSet_> {

  // NOP

}

