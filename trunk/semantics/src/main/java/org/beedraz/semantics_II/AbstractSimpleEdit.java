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

package org.beedraz.semantics_II;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Superclass with common code for non-compound edits.
 *
 * @author  Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractSimpleEdit<_Target_ extends AbstractBeed<_Event_>,
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

  /*</property>*/

}
