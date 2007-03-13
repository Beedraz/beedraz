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

package org.beedra_II.edit;


import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * MUDO docs
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface ValidityListener {

  /**
   * @pre target != null;
   */
  void validityChanged(Edit<?> target, boolean newValidity);

  /**
   * Tells the listener that he has been removed as a validity listener
   * from {@code target}. This happens when the {@code target} is
   * {@link Edit#kill() killed} or {@likn Edit#perform() performed}.
   *
   * @pre target != null;
   */
  void listenerRemoved(Edit<?> target);

}

