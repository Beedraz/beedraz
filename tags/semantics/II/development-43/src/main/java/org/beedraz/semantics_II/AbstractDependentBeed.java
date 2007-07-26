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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Support for beeds that derive their state from other beeds.
 * Derived beeds have a {@link Dependent dependent delegate},
 * where {@link UpdateSource update sources} are registered.
 * The update method of the dependent delegates to an abstract
 * method of this class. Subtypes should implement
 * that method, dealing with events from all beeds they registered
 * with the dependent.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractDependentBeed<_Event_ extends Event>
    extends AbstractBeed<_Event_> {

  /**
   * Access to the topological update method. First change this update source locally,
   * then described the change in an event, then call this method with that event.
   * This access for subclasses is intended for non-edit changes, i.e., structural
   * changes.
   *
   * @pre event != null;
   * @pre event.getSource() == this;
   * @pre event.getEdit() == null;
   */
  protected final void updateDependents(Event event) {
    assert event != null;
    assert event.getSource() == this;
    assert event.getEdit() == null;
    HashMap<AbstractBeed<?>, Event> sourceEvents = new HashMap<AbstractBeed<?>, Event>(1);
    sourceEvents.put(this, event);
    TopologicalUpdate.topologicalUpdate(sourceEvents, null);
  }

  /**
   * This method is called by the dependent when it is asked to update.
   * Subtypes should implement with code that deals with events from any update source
   * they have registered with the dependent.
   */
  protected abstract _Event_ filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit);


  /*<section name="dependent">*/
  //------------------------------------------------------------------

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

    @Override
    protected _Event_ filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
      return AbstractDependentBeed.this.filteredUpdate(events, edit);
    }

  };

  protected final void addUpdateSource(Beed<?> us) {
    $dependent.addUpdateSource(us);
  }

  protected final void removeUpdateSource(Beed<?> us) {
    $dependent.removeUpdateSource(us);
  }

  /*</section>*/



  /*<section name="update source">*/
  //------------------------------------------------------------------

  public final int getMaximumRootUpdateSourceDistance() {
    return $dependent.getMaximumRootUpdateSourceDistance();
  }

  public final Set<? extends Beed<?>> getUpdateSources() {
    return $dependent.getUpdateSources();
  }

  private final static Set<? extends Beed<?>> PHI = Collections.emptySet();

  public final Set<? extends Beed<?>> getUpdateSourcesTransitiveClosure() {
    /* fixed to make it possible to use this method during construction,
     * before $dependent is initialized. But that is bad code, and should be
     * fixed.
     */
    return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
  }

  /*</section>*/

}

