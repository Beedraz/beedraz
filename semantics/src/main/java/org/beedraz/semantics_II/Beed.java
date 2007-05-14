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

package org.beedraz.semantics_II;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.lang.ref.WeakReference;

import org.beedraz.semantics_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Beeds are the declarative unit of semantic state, relevant for the system.
 *   Beeds warn {@linkplain Listener registered Listeners} and invite
 *   {@linkplain org.beedraz.semantics_II.topologicalupdate.Dependent
 *   registered dependents} to update themselves when they change, using an
 *   {@link Event} that describes the change. Some beeds can be
 *   {@linkplain org.beedraz.semantics_II.edit.Edit edited}
 *   directly, others derive their state from beeds they depend on.
 *   We refer to the first kind as <dfn>editable beeds</dfn>, and to the
 *   second kind as <dfn>dependent beeds</dfn>.</p>
 * <p>There are no special interfaces to differentiate these 2 kinds
 *   (it turned out that did not add any value). The name of a beed type
 *   should always be of the form {@code ...Beed}. The class name of editable
 *   beeds should always be of the form {@code Editable...Beed}, and the name
 *   of beeds whose semantic state cannot be edited directly should never start
 *   with &quot;Editable&quot;.</p>
 *
 * <h3>Editable Beeds</h3>
 * <p>Changing the state of editable beans should always happen via
 *   creation of an appropriate {@link Edit}, which is then
 *   {@linkplain Edit#execute() executed} at the appropriate time. Beeds
 *   should never offer public methods that allow to change directly
 *   the semantic state they hold.</p>
 * <p>See {@link Edit} for more information.</p>
 *
 * <h3>Beed Graph</h3>
 * <p>Beeds are related in <em>an acyclic directed graph</em>. E.g., a beed
 *   that represents an arithmetic expression depends on beeds that are its
 *   operands.</p>
 * <p>Building, changing, and breaking down these beed relation is refered to
 *   as making <dfn>structural changes</dfn>. Such <dfn>dependent beeds</dfn>
 *   have public methods to add, remove or change operands, to make structural
 *   changes possible. Structural changes are not done normally through edits,
 *   although this is allowed in principle. Normally, these methods are used
 *   when initialising and de-initialising beeds, but they can be used in
 *   principle at any time.</p>
 * <p>When the opererands of a dependent beed changes, the semantic state of
 *   the dependent beed will possibly change too. If this happens, the dependent
 *   beed will warn {@linkplain Listener registered listeners} and invite
 *   {@linkplain org.beedraz.semantics_II.topologicalupdate.Dependent registered
 *   dependents} to update themselves, like an editable beed. However, since
 *   the final reason for the change is now a structural change, which is
 *   implemented as a regular Java mutator, there is no {@link Edit} involved.
 *   If the structural change does not change the semantic state of the
 *   dependent beed (e.g., exchanging operands +1 and -1 with +7 and -7 in
 *   a dependent beed that represents the sum of its operands), no events are
 *   send. The structural change in itself does not trigger events, and is not
 *   observable.</p>
 *
 * <h3>Events and Listeners</h3>
 * <p>Generic type invocations define the type of the events Beeds send.
 *   This static type is defined by the type parameter {@code _Event_}.
 *   Registered listeners should be able to accept events of type
 *   {@code _Event_} or its subtypes (a beed might send an event with a more specific
 *   dynamic type). Since the type of events being send is only guaranteed to be
 *   {@code _Event_}, only listeners that are content with events of type
 *   {@code _Event_} or more general event types, are accepted.
 *   The type {@code Listener<? super _Event_>} is an interface with all
 *   the methods defined in {@link Listener}. It is used as the polymorph type
 *   for listener registration.</p>
 * <p>Listener registration (see {@link #addListener(Listener)},
 *   {@link #removeListener(Listener)}, and {@link #isListener(Listener)}),is
 *   implemented by concrete implementations of this interface using
 *   {@linkplain WeakReference weak references}. This means that when an object is
 *   only registered as a listener with a beed, and not referenced by any other
 *   object using a <em>strong reference</em>, it will be garbage collected.</p>
 * <p>See {@link Event} and {@link Listener} for more information.</p>
 *
 * <h3>Update of Dependents</h3>
 * <p>Where {@linkplain Listener listeners} implement the traditional
 *   Model - View - Controller (or Observer - Observable) pattern,
 *   the {@linkplain Dependent dependents mechanism} extends this notion
 *   with a <dfn>topological walk</dfn> through the beed structure.
 *   Dependent beeds are asked, in <em>topological order</em>, to update
 *   themselves, and to describe their change with an {@link Event}.</p>
 * <img src="doc-files/TopologicalUpdate.png" />
 * <p>When all dependent beeds are updated in topological order.
 *   all changed beeds are visited again in topological order, and asked
 *   to send their event to registered listeners.</p>
 * <p>
 *
 * <p>Dependent registration is implemented by concrete implementations of this
 *   interface using {@linkplain WeakReference weak references}. This means that
 *   when an object is only registered as a listener with a beed, and not referenced
 *   by any other object using a <em>strong reference</em>, it will be garbage
 *   collected.</p>
 *
 * @author Jan Dockx
 *
 * @todo to be added: validation, civilization?
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface Beed<_Event_ extends Event> extends UpdateSource {

  /*<section name="listeners">*/
  //------------------------------------------------------------------

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

  /*</section>*/



  /**
   * Multiline instance information. This should be used
   * purely for debugging and logging.
   *
   * @pre sb != null;
   * @pre level >= 0;
   */
  void toString(StringBuffer sb, int i);

}

