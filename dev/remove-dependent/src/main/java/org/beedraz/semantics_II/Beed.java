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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.lang.ref.WeakReference;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Beeds are the declarative unit of semantic state, relevant for the system.
 *   Beeds warn {@linkplain Listener registered Listeners} and invite
 *   {@linkplain org.beedraz.semantics_II.Dependent
 *   registered dependents} to update themselves when they change, using an
 *   {@link Event} that describes the change. Some beeds can be
 *   {@linkplain org.beedraz.semantics_II.Edit edited}
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
 *   {@linkplain Edit#perform() executed} at the appropriate time. Beeds
 *   should never offer public methods that allow to change directly
 *   the semantic state they hold.</p>
 * <p>See {@link Edit} for more information.</p>
 *
 * <h3>Beed Graph</h3>
 * <p>Beeds are related in <em>an acyclic directed graph</em>. E.g., a beed
 *   that represents an arithmetic expression depends on beeds that are its
 *   operands.</p>
 * <p>Building, changing, and breaking down these beed relation is referred to
 *   as making <dfn>structural changes</dfn>. Such <dfn>dependent beeds</dfn>
 *   have public methods to add, remove or change operands, to make structural
 *   changes possible. Structural changes are not done normally through edits,
 *   although this is allowed in principle. Normally, these methods are used
 *   when initialising and de-initialising beeds, but they can be used in
 *   principle at any time.</p>
 * <p>When the opererands of a dependent beed changes, the semantic state of
 *   the dependent beed will possibly change too. If this happens, the dependent
 *   beed will warn {@linkplain Listener registered listeners} and invite
 *   {@linkplain org.beedraz.semantics_II.Dependent registered
 *   dependents} to update themselves, like an editable beed. However, since
 *   the final reason for the change is now a structural change, which is
 *   implemented as a regular Java mutator, there is no {@link Edit} involved.
 *   If the structural change does not change the semantic state of the
 *   dependent beed (e.g., exchanging operands +1 and -1 with +7 and -7 in
 *   a dependent beed that represents the sum of its operands), no events are
 *   sent. The structural change in itself does not trigger events, and is not
 *   observable.</p>
 *
 * <h3>Events and Listeners</h3>
 * <p>The {@code Beed} - {@link Listener} - {@link Event} structure implements the
 *   traditional <a href="http://en.wikipedia.org/wiki/Observer_pattern">Model -
 *   View -Controller (a.k.a. Observer - Observable, a.k.a.
 *   <acronym title="Model - View - Controller">MVC</acronym>) design pattern</a>.
 *   Generic type invocations define the type of the events {@code Beeds} send.
 *   This static type is defined by the type parameter {@code _Event_}.
 *   Registered listeners should be able to accept events of type
 *   {@code _Event_} or its subtypes (a beed might send an event with a more specific
 *   dynamic type). Since the type of events being send is only guaranteed to be
 *   {@code _Event_}, only listeners that are content with <em>events of type
 *   {@code _Event_} or more general event types</em>, are accepted.
 *   The type {@code Listener<? super _Event_>} is an interface with all
 *   the methods defined in {@link Listener}. It is used as the polymorph type
 *   for listener registration.</p>
 * <p>Listener registration (see {@link #addListener(Listener)},
 *   {@link #removeListener(Listener)}, and {@link #isListener(Listener)}) is
 *   implemented by concrete implementations of this interface using
 *   {@linkplain WeakReference weak references}. This means that when an object is
 *   only registered as a listener with a beed, and not referenced by any other
 *   object using a <em>strong reference</em>, it will be garbage collected.
 *   However, users should explicitly remove listeners when possible (it keeps
 *   the mind clean).</p>
 * <p>See {@link Event} and {@link Listener} for more information.</p>
 *
 * <h3>Update of Dependents</h3>
 * <p>Where {@linkplain Listener listeners} implement the traditional
 *   <acronym title="Model - View - Controller">MVC</acronym> pattern,
 *   the {@linkplain Dependent dependents mechanism} extends this notion
 *   with a <dfn>topological walk</dfn> through the beed structure.
 *   Dependent beeds are asked, in <em>topological order</em>, to update
 *   themselves, and to describe their change with an {@link Event}.</p>
 * <img src="doc-files/topologicalUpdate/img/TopologicalUpdate.png" />
 * <p>When all dependent beeds are updated in topological order.
 *   all changed beeds are visited again in topological order, and asked
 *   to send their event to registered listeners.</p>
 * <p>The need for the topological update mechanism, and the algorithm,
 *   is explained in a <a href="doc-files/topologicalUpdate/why.html">separate
 *   document</a>.</p>
 * <p>Dependent registration is implemented by concrete implementations of this
 *   interface using regular (strong) references. To avoid memory leaks,
 *   users that add a dependent must also remove that dependent at some time.</p>
 *
 * @author Jan Dockx
 *
 * @invar getMaximumRootUpdateSourceDistance() >= 0;
 * @invar getMaximumRootUpdateSourceDistance() == 0 ? getUpdateSources().isEmpty();
 * @invar for (Beed<?> us : getUpdateSources()) {
 *          getMaximumRootUpdateSourceDistance() > us.getMaximumRootUpdateSourceDistance()
 *        };
 * @invar getDependents() != null;
 *
 * @todo to be added: validation, civilization?
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface Beed<_Event_ extends Event> {

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



  /*<section name="dependents">*/
  //------------------------------------------------------------------

  /**
   * @basic
   * @init getDependents().isEmpty();
   */
  Set<Dependent> getDependents();

  /**
   * @return getDependents().contains(dependent);
   *
   * @deprecated use getDependents instead
   */
  @Deprecated
  boolean isDependent(Dependent dependent);

  /**
   * @result getDependents().contains(dependent) ||
   *           exists (Dependent d : getDependents()) {d.isTransitiveDependent(dependent)};
   */
  boolean isTransitiveDependent(Dependent dependent);

  /**
   * @pre dependent != null;
   * @pre ! isTransitiveDependent(dependent);
   *      no loops
   * @pre dependent.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance();
   * @post getDependents().contains(dependent);
   *
   * @note {@code dependent.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance();}
   *       is obviously a weird and hard precondition for a public method.
   *       It is however crucial for the topological update algorithm. We would have liked
   *       it better if we could limit access to this method for that reason, but the method
   *       has to be declared in this type, and this type has to be an interface, because
   *       beeds need multiple inheritance. Thus, the method must be public.
   *       It is ill-advised to use the method yourself, though. If you make structural
   *       changes through {@link Dependent#addUpdateSource(Beed)} and {@link Dependent#removeUpdateSource(Beed)}
   *       only, everything will be cared for.
   */
  void addDependent(Dependent dependent);

  /**
   * @post ! getDependents().contains(dependent);
   *
   * @note It is ill-advised to use the method yourself, though. If you make structural
   *       changes through {@link Dependent#addUpdateSource(Beed)} and {@link Dependent#removeUpdateSource(Beed)}
   *       only, everything will be cared for.
   */
  void removeDependent(Dependent dependent);

  /**
   * @basic
   */
  int getMaximumRootUpdateSourceDistance();

  /*</section>*/



  /*<section name="update sources">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  Set<? extends Beed<?>> getUpdateSources();

  /**
   * @result union(getUpdateSources,
   *               map(Beed<?> beed : getUpdateSources) {beed.getUpdateSourcesTransitiveClosure()});
   */
  Set<? extends Beed<?>> getUpdateSourcesTransitiveClosure();

  /**
   * @result filter(getUpdateSourcesTransitiveClosure(),
   *                boolean lambda(Beed<?> beed) {beed.getMaximumRootUpdateSourceDistance() == 0});
   */
  Set<? extends Beed<?>> getRootUpdateSources();

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

