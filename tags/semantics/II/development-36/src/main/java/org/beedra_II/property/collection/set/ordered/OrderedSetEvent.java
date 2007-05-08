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


import org.beedra_II.property.collection.OrderedCollectionEvent;
import org.beedra_II.property.collection.set.SetEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.collection_I.OrderedSet;


/**
 * <p>Event that notifies of changes in a {@link OrderedSetBeed}.
 *   The changes are represented by the ordered set before the change ({@link #getOldValue()})
 *   and after the change ({@link #getNewValue()}).</p>
 *
 * @note In contrast to supertypes, the collection type is not generic in this
 *       class, since there are no more subtypes defined in the Java Collection
 *       API or our code below {@link OrderedSet}. When this should change, this
 *       class should be made generic with respect to the collection type too.</p>
 *
 * @package
 * <p>Actual {@link OrderedSetBeed} instances will send instances of
 *   {@link ActualOrderedSetEvent} as events, but that is hidden for the user.</p>
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof OrderedSetBeed
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface OrderedSetEvent<_Element_>
    extends SetEvent<_Element_>,
            OrderedCollectionEvent<_Element_, OrderedSet<_Element_>> {

  /**
   * @basic
   */
  OrderedSet<_Element_> getAddedElements();

  /**
   * @basic
   */
  OrderedSet<_Element_> getRemovedElements();

}

