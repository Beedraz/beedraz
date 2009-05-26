/*<license>
Copyright 2007 - $Date: 2007-05-11 00:24:35 +0200 (Fri, 11 May 2007) $ by the authors mentioned below.

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

import org.beedraz.semantics_II.Edit.State;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Listener that can be registered with an {@link Edit} to receive
 * notification of state changes.
 *
 * @author  Jan Dockx
 */
@Copyright("2007 - $Date: 2007-05-11 00:24:35 +0200 (Fri, 11 May 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 876 $",
         date     = "$Date: 2007-05-11 00:24:35 +0200 (Fri, 11 May 2007) $")
public interface EditStateChangeListener {

  /**
   * The state of {@code edit} changed from {@code oldState} to {@code newState}.
   *
   * @pre edit != null;
   * @pre oldState != null;
   * @pre newState != null;
   * @re oldState == NOT_YET_PERFORMED ? newState == DONE || newState == DEAD;
   * @pre oldState == DONE ? newState == UNDONE || newState == DEAD;
   * @pre oldState == UNDONE ? newState == DONE || newState == DEAD;
   * @pre oldState != DEAD;
   * @pre newState != NOT_YET_PERFORMED;
   */
  void stateChanged(Edit<?> edit, State oldState, State newState);

}
