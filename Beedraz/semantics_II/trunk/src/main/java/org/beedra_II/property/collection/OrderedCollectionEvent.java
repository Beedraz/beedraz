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

package org.beedra_II.property.collection;


import java.util.Collection;

import org.beedra_II.property.simple.OldNewEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>The common aspects of events send by ordered collections.
 *   Event that notifies of changes in an ordered {@link CollectionBeed}.
 *   The changes are represented by the ordered collection before the change
 *   ({@link #getOldValue()}) and after the change ({@link #getNewValue()}).</p>
 * <p>For <em>ordered</em> collections (i.e., collections for which the order
 *   is important, and under user control, in contrast to <em>sorted</em> collections,
 *   where the order is defined logically), the added and removed elements offered
 *   by {@link CollectionEvent} is not enough information to describe the change
 *   completely. In that case, an old-new value description is needed.
 *   The collections returned by {@link #getAddedElements()}
 *   and {@link #getRemovedElements()} are provided to be compatible with the super
 *   type {@link CollectionEvent}. Still, these 2 delta-collections might be more
 *   interesting for receiving listeners than the old and the new collection.</p>
 *
 * @author Jan Dockx
 *
 * @invar getOldValue() != null;
 * @invar getNewValue() != null;
 * @invar getAddedElements().equals(getNewValue().removeAll(getOldValue()));
 * @invar getRemovedElements().equals(getOldValue().removeAll(getNewValue()));
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface OrderedCollectionEvent<_Element_,
                                        _Collection_ extends Collection<_Element_>>
    extends CollectionEvent<_Element_>,
            OldNewEvent<_Collection_> {

  // NOP

}

