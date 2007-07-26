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

package org.beedraz.semantics_II.expression.collection.list;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.List;

import org.beedraz.semantics_II.expression.collection.OrderedCollectionEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Event that notifies of changes in a {@link ListBeed}.
 *   The changes are represented by the list before the change {@link #getOldValue()}
 *   and {@link #getNewValue()}.</p>
 *
 * @note In contrast to supertypes, the collection type is not generic in this
 *       class, since there are no more subtypes defined in the Java Collection
 *       API below {@link List}. When this should change, this class should
 *       be made generic with respect to the collection type too.</p>
 *
 * @package
 * <p>Actual {@link ListBeed} instances will send instances of
 *   {@link ActualListEvent} as events, but that is hidden for the user.</p>
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof ListBeed
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface ListEvent<_Element_>
    extends OrderedCollectionEvent<_Element_, List<_Element_>> {

  /**
   * @basic
   */
  List<_Element_> getAddedElements();

  /**
   * @basic
   */
  List<_Element_> getRemovedElements();

}

