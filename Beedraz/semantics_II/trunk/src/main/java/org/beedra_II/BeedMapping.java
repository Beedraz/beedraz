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


import org.beedra_II.event.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.Mapping;


/**
 * A mapping maps a given beed of type _From_ to an
 * element of type _To_.
 *
 * @author  Jan Dockx
 * @author  Peopleware n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface BeedMapping<_From_ extends Beed<_FromEvent_>,
                             _FromEvent_ extends Event,
                             _To_>
    extends Mapping<_From_, _To_> {

  // NOP

}

