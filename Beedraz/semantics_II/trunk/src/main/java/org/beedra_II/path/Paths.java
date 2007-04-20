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

package org.beedra_II.path;


import org.beedra_II.Beed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Convenience methods for working with paths.</p>
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class Paths {

  /**
   * Quickly create a constant path for a beed we know.
   */
  public static <_Beed_ extends Beed<?>> ConstantPath<_Beed_> path(_Beed_ beed) {
    return new ConstantPath<_Beed_>(beed);
  }
//
//  /**
//   * Create path to a property beed using reflection.
//   *
//   * @param origin
//   *        The bean beed path that returns the bean to select the property from.
//   */
//  public static <_PropertyBeed_ extends PropertyBeed<?>> property(Path<? extends BeanBeed> bbp, String path)

}

