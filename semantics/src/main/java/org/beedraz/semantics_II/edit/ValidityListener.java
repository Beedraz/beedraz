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

package org.beedraz.semantics_II.edit;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.edit.Edit.State;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Implementations can be registered with {@link Edit Edits},
 *   and are warned when the <em>edit target goal state validity</em>
 *   ({@link #isValid()}) changes. Since we observe merely changes
 *   in a boolean value, we do not employ an event, but use
 *   a 2-argument method {@link #validityChanged(Edit, boolean)}.</p>
 * <p>Implementations are also warned when the state of an {@link Edit Edit}
 *   they are registered with changes from {@link State#NOT_YET_PERFORMED}.
 *   When this happens, all registered validity listeners are removed from
 *   the edit. With this warning, they can do clean-up and make themselves
 *   ready for gc.</p>
 *
 * @author Jan Dockx
 * @author  PeopleWare n.v.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface ValidityListener {

  /**
   * The {@link Edit#isValid() validity} of {@code edit} changed.
   *
   * @pre target != null;
   */
  void validityChanged(Edit<?> edit, boolean newValidity);

  /**
   * Tells the listener that he has been removed as a validity listener
   * from {@code target}. This happens when the {@code target} is
   * {@link Edit#kill() killed} or {@likn Edit#perform() performed}.
   *
   * @pre target != null;
   */
  void listenerRemoved(Edit<?> target);

}

