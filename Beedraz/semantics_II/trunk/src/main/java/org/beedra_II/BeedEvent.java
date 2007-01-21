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


import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * Notices interested {@link BeedListener BeedChangeListeners}
 * of changes in {@link #getSource()}.
 *
 * The generic parameter _Source_ is needed only for undoability.
 * See {@link DoBeed}.
 *
 * @author Jan Dockx
 *
 * @invar getSource() != null;
 * @note must be immutable
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BeedEvent<_Source_ extends Beed> {

  /**
   * @pre source != null;
   * @post getSource() == source;
   */
  public BeedEvent(_Source_ source) {
    assert source != null;
    $source = source;
  }

  /**
   * @basic
   */
  public final _Source_ getSource() {
    return $source;
  }

  /**
   * @invar $source != null;
   */
  private final _Source_ $source;

}

