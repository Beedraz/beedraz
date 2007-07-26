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

package org.beedraz.semantics_II.aggregate;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Event;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Exception thrown by an {@link Event} to signal an illegal state.
 *
 * @author  Jan Dockx
 *
 * @invar getEvent() != null;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class AggregateEventStateException extends Exception {

  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @post ComparisonUtil.equalsWithNull(s, getMessage());
   * @post getEvent() == event;
   */
  public AggregateEventStateException(Event event, String s) {
    super(s);
    $event = event;
  }

  /*</construction>*/



  /*<property name="event">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  final public Event getEvent() {
    return $event;
  }

  /**
   * The event that triggered this exception.
   *
   * @invar	 $event != null;
   */
  private Event $event;

  /*</property>*/

}
