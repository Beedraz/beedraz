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

import java.util.Map;
import java.util.Set;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>More can be implemented in a general way if we know that our
 *   {@link #getDependentUpdateSource()} is an {@link AbstractBeed}
 *   (which it will be most of the time). MUDO THIS IS OBSOLETE; MERGE WITH DEPENDENT</p>
 * <p>From experience, we know that it makes no sense for this type to be generic in the types
 *   of allowed {@link Beed update sources} or {@link Event Events}.</p>
 *
 * @author Jan Dockx
 *
 * @invar getDependentUpdateSource() != null;
 * @invar for (Dependent d: getDependents() {
 *          d.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance()
 *        };
 * @invar getUpdateSource().getMaximumRootUpdateSourceDistance() > 0;
 * @invar getUpdateSource().getMaximumRootUpdateSourceDistance() == getMaximumRootUpdateSourceDistance();
 * @invar for (UpdateSource us : getUpdateSources()) {
 *          us.getMaximalRootUpdateSourceDistance() < getMaximalRootUpdateSourceDistance()
 *        };
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractUpdateSourceDependentDelegate
    extends Dependent {

  /**
   * @pre dependentUpdateSource != null;
   * @post getDependentUpdateSource() == dependentUpdateSource;
   */
  protected AbstractUpdateSourceDependentDelegate(AbstractBeed<?> dependentUpdateSource) {
    $dependentUpdateSource = dependentUpdateSource;
  }

  /*<property name="dependent update source">*/
  //-----------------------------------------------------------------

  @Override
  public final AbstractBeed<?> getDependentUpdateSource() {
    return $dependentUpdateSource;
  }

  @Override
  public final Set<Dependent> getDependents() {
    return $dependentUpdateSource.getDependents();
  }

  @Override
  final void fireEvent(Event event) {
    $dependentUpdateSource.fireEvent(event);
  }

  private final AbstractBeed<?> $dependentUpdateSource;

  /*</property>*/


  /*<section name="update">*/
  //-----------------------------------------------------------------

  @Override
  protected abstract Event filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit);

  /*</section>*/

}