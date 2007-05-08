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


import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * <p>Beeds hold semantic state, relevant for the system.
 *   Beeds send events of type {@code _Event_ extends }{@link Event}
 *   to {@link Listener registered listeners} when they change.</p>
 * <p>Some beeds are <em>read-only</em>, others are <em>editable</em> with
 *   respect to the semantic state they represent.
 *   Changing the state of editable beans should always happen via creation of an
 *   appropriate {@link Edit}, which is then {@link Edit#execute() executed}
 *   at the appropriate time. Beeds should never offer public methods
 *   that allow to change directly the semantic state they hold.</p>
 * <p>Generic type invocations define the super type of the events they send.
 *   This static type is defined by the type parameter {@code _Event_}.
 *   Registered listeners should be able to accept events of type
 *   {@code _Event_}, its subtypes or its supertypes, but since the type of
 *   events being send is only guaranteed to be {@code _Event_}, only listeners
 *   that are content with events of type {@code _Event_} or less specific
 *   are accepted. The actual type parameter {@code _Event_} in concrete
 *   generic invocations has to be a subtype of
 *
 *   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *
 *   {@link Event}{@code &lt;? super _This_&gt;}, which means that the event
 *   type has to declare the static type of the beeds that can send it
 *   as the generic invocation itself or one of its supertypes.
 *   In the current type, {@code _This_} is Beed<_Event_>. Subtypes and
 *   generic invocations can declare a stronger
 *
 *   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *
 *   class of actual event sent > _Event_
 *
 * <p>Concrete beeds define exactly which type of concrete events they send
 *   via generic parameter {@code _Event_} (and concrete {@code Event events}
 *   define exactly which kind of Beed is their source; the Beed and {@link Event}
 *   inheritance hierarchy is covariant). Beeds accept listener registrations from
 *   listeners that can accept events of type {@code _Event_} and listeners that can
 *   accept events from super types of {@code _Event_}. The type
 *   {@link Listener}{@code &lt;? super _Event_&gt;} is an interface with all
 *   the methods defined in {@link Listener}. It is used as the polymorph type
 *   for listener registration.</p>
 * <p>Beeds accept
 * Beeds can have listeners that can accept (at least) events
 *   of the type defined above. This means that the type of the listener
 *   should be {@link BeedListener}{@code &lt;? super _Event_&gt;}.</p>
 * <p>A concrete beed sends events specifically defined for the beed type or
 *   one of its supertypes.</p>
 *
 *   LISTENERS OF A SUPERTYPE OF EVENT E CAN ALSO ACCEPT EVENTS OF TYPE E, WHICH WE WILL SEND, VIA POLYMORPHISM
 *
 * @author Jan Dockx
 *
 * @mudo to be added: validation, civilization, propagation
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface Beed<_Event_ extends Event> {

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

