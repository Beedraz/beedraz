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

package org.beedra_II.property.simple;


import org.beedra_II.event.Event;
import org.beedra_II.property.PropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * <p>A {@link PropertyBeed} whose state is expressed
 *   by one {@link #get() value} of type {@code _Type_}.
 *   In general, the state can be {@link #get() retrieved},
 *   but explicitly setting state requires a subtype.</p>
 * <p>{@code SimplePropertyBeed SimplePropertyBeeds} send
 *   {@link Event BeedEvents}. Most {@code SimplePropertyBeed SimplePropertyBeeds}
 *   send {@link OldNewEvent OldNewBeedEvents}.</p>
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface SimplePropertyBeed<_Type_,
                                    _Event_ extends Event<?>>
    extends PropertyBeed<_Event_> {

  /**
   * @basic
   */
  _Type_ get();

}

