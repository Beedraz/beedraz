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
 * <p>{@link Path} that always return {@code null}
 *   and never changes.</p>
 *
 * @author Jan Dockx
 *
 * @invar get() == null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class NullPath<_SelectedBeed_ extends Beed<?>>
    extends AbstractIndependentPath<_SelectedBeed_> {

  public final _SelectedBeed_ get() {
    return null;
  }

  @Override
  protected String otherToStringInformation() {
    return "null";
  }

}

