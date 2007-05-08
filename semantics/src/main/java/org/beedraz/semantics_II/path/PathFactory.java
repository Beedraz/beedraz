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


import org.beedraz.semantics_II.Beed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Create a {@link Path}. Pathfactories are necessary,
 *   because paths hold state, and we need instances
 *   when we use them.</p>
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface PathFactory<_StartBeed_ extends Beed<?>,
                             _ResultBeed_ extends Beed<?>> {

  /**
   * @pre startBeed != null;
   * @result result != null;
   */
  Path<? extends _ResultBeed_> createPath(_StartBeed_ startBeed);

}

