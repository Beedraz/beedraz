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
 * <p>There is only one generic listener type in this framework.
 *   By generic instantion, each event type creates its own
 *   listener type. The event type hierarchy and the listener type
 *   hierarchy are completely <em>covariant</em>.</p>
 * <p>You can instantiate a listener that listens for events sent by
 *   a specific source beed type {@code B} of type {@code E&lt;B&gt;}
 *   (where {@code B} implements {@link Beed} and {@code E} extends
 *   {@link Event}) as follows:</p>
 * <pre>Listener&lt;E&lt;B&gt;&gt; listener = new Listener&lt;E&lt;B&gt;&gt;() {
 *    beedChanged(E&lt;B&gt; event) {
 *      <var>listener code here</var>
 *    }
 *  };</pre>
 * <p>This creates an anonymous inner class that implements the interface
 *   {@code Listener&lt;E&lt;B&gt;&gt;}.</p>
 * <p>You can instantiate a listener that listens for events sent by
 *   any instance of polymorph super type of beeds {@code SuperB}, a super
 *   type of actual beed types {@code B1}, {@code B2}, {@code B3}, ...,
 *   as follows:</p>
 * <pre>Listener&lt;Event&lt;? extends SuperB&gt;&gt; listener = new Listener&lt;Event&lt;? extends SuperB&gt;&gt;() {
 *    beedChanged(Event&lt;? extends SuperB&gt; event) {
 *      <var>listener code here</var>
 *    }
 *  };</pre>
 * <p>The type {@code Event&lt;? extends SuperB&gt;} is an interface that
 *   defines a polymorph type, with a subtype hierarchy completely
 *   <em>covariant</em> with the {@code SuperB} hierarchy. The methods
 *   in the interface {@code Event&lt;? extends SuperB&gt;} are the methods
 *   featured in {@code Event&lt;_Beed_&gt;}, except the methods with an argument
 *   of type {@code _Beed_}. There should be no such messages in the
 *   class {@link Event}.</p>
 *
 *   LISTENERS OF A SUPERTYPE OF EVENT E CAN ALSO ACCEPT EVENTS OF TYPE E, WHICH WE WILL SEND, VIA POLYMORPHISM
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface Listener<_Source_ extends Beed<_Source_, _Event_>,
                          _Event_ extends Event<_Source_, _Event_>> {

  /**
   * @pre event != null;
   */
  void beedChanged(_Event_ event);

}

