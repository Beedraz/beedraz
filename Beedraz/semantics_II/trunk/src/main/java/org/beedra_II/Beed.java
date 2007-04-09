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

package org.beedra_II;


import java.lang.ref.WeakReference;

import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Beeds are the declarative unit of semantic state, relevant for the system.
 *   Beeds warn {@link Listener registered Listeners} and invite
 *   {@link org.beedra_II.topologicalupdate.Dependent
 *   registered Dependents} to update themselves when they change, using an
 *   {@link Event} that describes the change. Some beeds can be
 *   {@link org.beedra_II.edit.Edit edited}
 *   directly, others derive their state from beeds they depend on.</p>
 * <p>There are no special interfaces to tag these 2 kinds (it turned out that did
 *   not add any value). The name of a beed type should always be of the form
 *   {@code ...Beed}. The class name of editable beeds should always be of the form
 *   {@code Editable...Beed}, and the name of beeds whose semantic state cannot be
 *   edited directly should never start with &quot;Editable&quot;.</p>
 *
 * <h3>Events and Listeners</h3>
 * <p>Generic type invocations define the type of the events Beeds send.
 *   This static type is defined by the type parameter {@code _Event_}.
 *   Registered listeners should be able to accept events of type
 *   {@code _Event_} or its subtypes (a beed might send an event with a more specific
 *   dynamic type). Since the type of events being send is only guaranteed to be
 *   {@code _Event_}, only listeners that are content with events of type
 *   {@code _Event_} or more general events are accepted.</p>
 * <p>Concrete beeds define exactly which type of concrete events they send
 *   via the generic parameter {@code _Event_}. Beeds accept listener registrations from
 *   listeners that can accept events of type {@code _Event_} and listeners that can
 *   accept events from super types of {@code _Event_}. The type
 *   {@code Listener<? super _Event_>} is an interface with all
 *   the methods defined in {@link Listener}. It is used as the polymorph type
 *   for listener registration.</p>
 * <p>Listeners are implemented by concrete implementations using
 *   <em>weak references</em> (see {@link WeakReference}). This means that when an
 *   object is only registered as a listener with a beed, and not referenced by
 *   any other object using a <em>strong reference</em>, it will be garbage
 *   collected.</p>
 * <p>See {@link Event} and {@link Listener} for more information.</p>
 *
 * <h3>Update of Dependents
 *
 * <h3>Editable Beeds</h3>
 * <p>Changing the state of editable beans should always happen via
 *   creation of an appropriate {@link Edit}, which is then
 *   {@link Edit#execute() executed} at the appropriate time. Beeds
 *   should never offer public methods that allow to change directly
 *   the semantic state they hold.</p>
 * <p>See {@link Edit} for more information.</p>
 *
 * @author Jan Dockx
 *
 * @todo to be added: validation, civilization?
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface Beed<_Event_ extends Event> extends UpdateSource {

  /**
   * @basic
   * @init foreach (Listener&lt;? super _Event_&gt; l) { ! isListener(l)};
   */
  boolean isListener(Listener<? super _Event_> listener);

  /**
   * @pre listener != null;
   * @post isListener(listener);
   */
  void addListener(Listener<? super _Event_> listener);

  /**
   * @post ! isListener(listener);
   */
  void removeListener(Listener<? super _Event_> listener);

  /**
   * Multiline instance information. This should be used
   * purely for debugging and logging.
   *
   * @pre sb != null;
   * @pre level >= 0;
   */
  void toString(StringBuffer sb, int i);

}

