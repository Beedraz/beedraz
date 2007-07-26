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

import java.util.HashMap;
import java.util.Map;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Implementation of some general methods for {@link Edit Edits} that
 *   benefit from generics.</p>
 * <p>This class and {@link Edit} were separated, because the methods in
 *   this class need a generic parameter that is only needed by protected
 *   methods, and is of no importance to public users, who see {@link Edit}.
 *   In general, there should be no reason for concrete edit implementations to
 *   avoid extending this class.</p>
 *
 * @author  Jan Dockx
 * @author  PeopleWare n.v.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractEdit<_Target_ extends AbstractBeed<?>,
                                   _Event_ extends Event>
    extends Edit<_Target_> {


  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @pre target != null;
   * @post getTarget() == target;
   * @post getState() == NOT_YET_PERFORMED;
   */
  protected AbstractEdit(_Target_ target) {
    super(target);
  }

  /*</construction>*/


  /**
   * Utility method for some subclasses that want to return a
   * singleton map with 1 event for the target.
   *
   * @pre event != null;
   * @pre event.getSource() == getTarget();
   * @post result.size() == 1;
   * @post result.get(getTarget()) = event;
   */
  protected final Map<AbstractBeed<?>, _Event_> singletonEventMap(_Event_ event) {
    assert event != null;
    assert event.getSource() == getTarget();
    HashMap<AbstractBeed<?>, _Event_> result = new HashMap<AbstractBeed<?>, _Event_>(1);
    result.put(getTarget(), event);
    return result;
  }

  // MUDO this is not a good reason for having generics here: _Event_

}
