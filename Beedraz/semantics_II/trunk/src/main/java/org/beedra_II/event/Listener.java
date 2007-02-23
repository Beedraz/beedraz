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

package org.beedra_II.event;


import org.beedra_II.Beed;
import org.beedra_II.event.Event;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * <p>Listen to changes in {@code Beed Beeds}.
 *   Changes are expressed in {@code _Event_} instances.</p>
 * <p>There is only one (generic) listener type in this framework.
 *   By generic type invocation, each event type creates its own
 *   listener type. The event type hierarchy and the listener type
 *   hierarchy are completely <em>covariant</em>.</p>
 * <p>The type parameter {@code _Event_} says what the static type is
 *   of events that this listener needs. Via polymorphism, the listener
 *   also accepts subtypes of {@code _Event_}. The actual type parameter
 *   {@code _Event_} in concrete generic type invocations has to be a subtype
 *   of {@link Event}{@code &lt;? extends Beed&lt;?&gt;&gt;}. Generic type
 *   invocations can choose how broad or limiting they want {@code _Event_}
 *   to be. E.g.,</p>
 * <ul>
 *   <li>a listener can be defined that accepts any {@link Event}, as
 *     {@code Listener&lt;Event&lt;?&gt;&gt;};</li>
 *   <li>a listener can be defined that only accepts the specific kinds of
 *     events {@code MySpecialEvent} from any {@link Beed}, as
 *     {@code Listener&lt;MySpecialEvent&lt;?&gt;&gt;};</li>
 *   <li>a listener can be defined that only accepts the specific kinds of
 *     events {@code MySpecialEvent} from the specific beed
 *     {@code MySpecificBeed}, as
 *     {@code Listener&lt;MySpecialEvent&lt;MySpecificBeed&gt;&gt;};</li>
 *   <li>or a listener can be defined that only accepts the specific kinds of
 *     events {@code MySpecialEvent} from the beed {@code MySpecificBeed} or
 *     any of its subtypes, as
 *     {@code Listener&lt;MySpecialEvent&lt;? extends MySpecificBeed&gt;&gt;};
 *     the type {@code MySpecialEvent&lt;? extends MySpecificBeed&gt;} defines
 *     an interface that only offers the methods of {@code MySpecialEvent}
 *     that do not have an argument of type parameter {@code _Source_}.</li>
 * </ul>
 * <p>Static listener types can be defined in even more variants, e.g.,</p>
 * <ul>
 *   <li>a static listener type can be defined to which any listener can be
 *     assigned that accepts events of type {@code MySpecialEvent} from any
 *     {@link Beed}, or any more general event from any {@link Beed}, as
 *     {@code Listener&lt;? super MySpecialEvent&lt;?&gt;&gt;}; this
 *     parametrized type plays an important role in the generic type
 *     declaration of {@link Beed}.</li>
 * </ul>
 * <p>Most listener types will be defined as an anonymous inner class,
 *   combining generic type invocation with instantiation, as follows:</p>
 * <pre>Listener&lt;MySpecialEvent&lt;?&gt;&gt; listener = new Listener&lt;MySpecialEvent&lt;?&gt;&gt;() {
 *    beedChanged(MySpecialEvent&lt;?&gt; event) {
 *      <var>listener code here</var>
 *    }
 *  };</pre>
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface Listener<_Event_ extends Event> {

  /**
   * @pre event != null;
   */
  void beedChanged(_Event_ event);

}

