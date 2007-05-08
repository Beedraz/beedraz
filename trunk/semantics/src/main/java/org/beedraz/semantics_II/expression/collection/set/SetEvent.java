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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Set;

import org.beedraz.semantics_II.expression.collection.CollectionEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Event that notifies of changes in a {@link SetBeed}.
 *   The changes are represented by a set of {@link #getAddedElements()
 *   added elements} and a set of {@link #getRemovedElements()}.</p>
 * <p>This interface doubles as the generalized super type for events send by
 *   set beeds and subtypes, and as actual event type send by actual
 *   {@link SetBeed} instances. The generic parameter {@code _Set_}
 *   is necessary to be able to use this type as the generalized super type
 *   for events send by set beeds and subtypes.</p>
 * <p>Users interested in these events as generalized events sends by
 *   set beeds in general, should use the type as
 *   <code>SetEvent&lt;<var>MyElement</var>, ?&gt;</code>.</p>
 * <p>Users interested in the events send by actual {@link SetBeed}
 *   instances should use this type as <code>SetEvent&lt;<var>MyElement</var>,
 *   Set&gt;</code>.</p>
 *
 * @package
 * <p>Actual {@link SetBeed} instances will send instances of
 *   {@link ActualSetEvent} as events, but that is hidden for the user.</p>
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof SetBeed
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface SetEvent<_Element_> extends CollectionEvent<_Element_> {

  /**
   * @basic
   */
  Set<_Element_> getAddedElements();

  /**
   * @basic
   */
  Set<_Element_> getRemovedElements();

}

