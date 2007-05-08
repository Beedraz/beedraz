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

package org.beedra_II.property;


import org.beedra_II.Beed;
import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * <p>{@link Beed Beeds} <em>of
 *   another beed</em>. They are <em>owned</em>
 *   (see {@link #getOwner()}), they do not exist on their own.</p>
 * <p>{@code PropertyBeeds} are like properties
 *   of JavaBeans. The JavaBean is the owner of the
 *   property.</p>
 * <p>Events send by {@code PropertyBeeds} are just
 *   {@link Event BeedEvents}.</p>
 *
 * @author Jan Dockx
 *
 * @invar getOwner() != null;
 * @invar getOwner() == this'getOwner();
 *
 * @mudo since the owner os not always a Bean, other name? owned beed? weak beed?
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface PropertyBeed<_Event_ extends Event>
    extends Beed<_Event_> {

  /**
   * @basic
   */
  Beed<?> getOwner();

  /**
   * This beed will send an event of type {@code _Event_} during listener
   * registration. This event will carry the current state of the beed as
   * &quot;new&quot; information, and the &quot;old&quot; information in
   * the event will be {@code null} or some other form of N/A. If you don't
   * want initial events, use {@link #addListener(Listener)} instead.
   *
   * @pre listener != null;
   * @post isListener(listener);
   * @post initialEvent ? exists (_Event_ event) {listener.beedChanged(event)};
   */
  void addListenerInitialEvent(Listener<? super _Event_> listener);

}

