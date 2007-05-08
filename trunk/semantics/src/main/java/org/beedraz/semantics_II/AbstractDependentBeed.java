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


import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.edit.Edit;
import org.beedraz.semantics_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedraz.semantics_II.topologicalupdate.Dependent;
import org.beedraz.semantics_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Support for beeds that derive their state from other beeds.
 * Derived beeds have a {@link Dependent dependent delegate},
 * where {@link UpdateSource update sources} are registered.
 * The update method of the dependent delegates to an abstract
 * method of this class. Subtypes should implement
 * that method, dealing with events from all beeds they registered
 * with the dependent.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractDependentBeed<_Event_ extends Event>
    extends AbstractBeed<_Event_> {

  /**
   * This method is called by the dependent when it is asked to update.
   * Subtypes should implement with code that deals with events from any update source
   * they have registered with the dependent.
   */
  protected abstract _Event_ filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit);


  /*<section name="dependent">*/
  //------------------------------------------------------------------

  /**
   * @mudo must be private, but protected now to force errors in subtypes
   */
  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

    @Override
    protected _Event_ filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
      return AbstractDependentBeed.this.filteredUpdate(events, edit);
    }

  };

  protected final void addUpdateSource(UpdateSource us) {
    $dependent.addUpdateSource(us);
  }

  protected final void removeUpdateSource(UpdateSource us) {
    $dependent.removeUpdateSource(us);
  }


  /*<section name="update source">*/
  //------------------------------------------------------------------

  public final int getMaximumRootUpdateSourceDistance() {
    return $dependent.getMaximumRootUpdateSourceDistance();
  }

  public final Set<? extends UpdateSource> getUpdateSources() {
    return $dependent.getUpdateSources();
  }

  private final static Set<? extends UpdateSource> PHI = Collections.emptySet();

  public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
    /* fixed to make it possible to use this method during construction,
     * before $dependent is initialized. But that is bad code, and should be
     * fixed.
     */
    return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
  }

  /*</section>*/

}

