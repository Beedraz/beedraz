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

import org.beedra_II.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Event that notifies of changes in a {@link CollectionBeed}.
 *   The changes are represented by a collection of {@link #getAddedElements()
 *   added elements} and a collection of {@link #getRemovedElements()}.</p>
 * <p>This interface doubles as the generalized super type for events send by
 *   collection beeds and subtypes, and as actual event type send by actual
 *   {@link CollectionBeed} instances. The generic parameter {@code _Collection_}
 *   is necessary to be able to use this type as the generalized super type
 *   for events send by collection beeds and subtypes.</p>
 * <p>Users interested in these events as generalized events sends by
 *   collection beeds in general, should use the type as
 *   <code>CollectionEvent&lt;<var>MyElement</var>, ?&gt;</code>.</p>
 * <p>Users interested in the events send by actual {@link CollectionBeed}
 *   instances should use this type as <code>CollectionEvent&lt;<var>MyElement</var>,
 *   Collection&gt;</code>.</p>
 *
 * @package
 * <p>Actual {@link CollectionBeed} instances will send instances of
 *   {@link ActualCollectionEvent} as events, but that is hidden for the user.</p>
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
public interface CollectionEvent<_Element_>
    extends Event {

  /**
   * @basic
   */
  Collection<_Element_> getAddedElements();

  /**
   * @basic
   */
  Collection<_Element_> getRemovedElements();

}

