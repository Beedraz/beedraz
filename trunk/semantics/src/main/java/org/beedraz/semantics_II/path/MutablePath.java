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

package org.beedraz.semantics_II.path;


import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.edit.Edit;
import org.beedraz.semantics_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>{@link Path} returns the beed that is set with the setter.</p>
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class MutablePath<_Beed_ extends Beed<?>>
    extends AbstractDependentPath<_Beed_> {

      /* MUDO we extends AbstractDependentPath, while we should extends AbstractIndependentPath,
       * but we need to do this, because operand path-setting code depends on AbstractDependentPath
       * in a wrong way. This is a quick fix that must be solved in the operand-path-setting code.
       */

  /*<construction>*/
  //-----------------------------------------------------------------

  /**
   * @post get() == null;
   */
  public MutablePath() {
    super();
  }

  /**
   * @post get() == beed();
   */
  public MutablePath(_Beed_ beed) {
    $beed = beed;
    // no dependents, so no event to send
  }

  /*</construction>*/



  public final _Beed_ get() {
    return $beed;
  }

  public final void set(_Beed_ beed) {
    _Beed_ oldBeed = $beed;
    $beed = beed;
    if (oldBeed != $beed) {
      updateDependents(new PathEvent<_Beed_>(this, oldBeed, $beed, null));
    }
  }

  private _Beed_ $beed;

  /* MUDO we extends AbstractDependentPath, while we should extends AbstractIndependentPath,
   * but we need to do this, because operand path-setting code depends on AbstractDependentPath
   * in a wrong way. This is a quick fix that must be solved in the operand-path-setting code.
   */
  @Override
  protected PathEvent<_Beed_> filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
    // TODO Auto-generated method stub
    return null;
  }

}

