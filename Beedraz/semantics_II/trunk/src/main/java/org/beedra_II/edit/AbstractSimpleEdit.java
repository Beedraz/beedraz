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


import org.beedra_II.EditableBeed;
import org.beedra_II.event.Event;
import org.ppeew.annotations.vcs.CvsInfo;


/**
 * The top interface for the edit framework. We advise the use of the actual
 * classes in this package. If the do not provide the functionality you need,
 * you can implement your own version implementing this interface.
 *
 * @author  Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractSimpleEdit<_Target_ extends EditableBeed<_Event_>,
                                         _Event_ extends Event>
    extends AbstractEdit<_Target_, _Event_> {


  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @pre target != null;
   * @post getTarget() == target;
   */
  protected AbstractSimpleEdit(_Target_ target) {
    super(target);
  }

  /*</construction>*/



  /*<property name="state">*/
  //-------------------------------------------------------

  @Override
  public final void kill() {
    localKill();
  }

  /*</property>*/

  @Override
  protected final void markPerformed() {
    localMarkPerformed();
  }

}
