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


import org.beedra_II.edit.Edit;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * <p>{@link Event} send by {@link EditableBeed EditableBeeds}, that
 *   carries the event that cause the expressed change.</p>
 *
 * @author Jan Dockx
 *
 * @invar getSource() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface EditEvent<_Edit_ extends Edit<?>> extends Event {

  /**
   * @basic
   */
  _Edit_ getEdit(); // MUDO

}

