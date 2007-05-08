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


import javax.swing.undo.UndoableEdit;

import org.beedra_II.BeedEvent;
import org.beedra_II.DoBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * {@link SimplePB} whose value can be changed directly
 * by the user.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface SimplePDB<_Type_, _Event_ extends BeedEvent<? extends SimplePDB<_Type_, _Event_>> & UndoableEdit>
    extends SimplePB<_Type_, _Event_>, DoBeed<_Event_> {

  /**
   * @post get().equals(t);
   *       When this is a reference type, equals is implemented
   *       as {@code ==}, so this then means {@code get() == t}.
   */
  void set(_Type_ t);

}

