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

package org.beedraz.semantics_II.expression;


import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.OldNewEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>A {@link Beed} whose state is expressed
 *   by one {@link #get() reference} of type {@code _Type_}.
 *   In general, the state can be {@link #get() retrieved},
 *   but explicitly setting state requires a subtype.</p>
 * <p>Most, but not all, {@code SimpleExpressionBeed SimpleExpressionBeeds}
 *   send {@link OldNewEvent OldNewBeedEvents}.</p>
 *
 * @author Jan Dockx
 *
 * @note This interface has little use in its own, but is important
 *       for reuse in, e.g., the SWT peripherals.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface SimpleExpressionBeed<_Type_,
                                    _Event_ extends Event>
    extends Beed<_Event_> {

  /**
   * @basic
   */
  _Type_ get();

}

