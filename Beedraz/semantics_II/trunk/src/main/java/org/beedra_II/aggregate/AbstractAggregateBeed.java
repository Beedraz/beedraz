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

package org.beedra_II.aggregate;


import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.beedra_II.AbstractBeed;
import org.beedra_II.Beed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Support for implementations of {@link BeanBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractAggregateBeed
    extends AbstractBeed<AggregateEvent>
    implements AggregateBeed {

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected AggregateEvent filteredUpdate(Map<UpdateSource, Event> events, Edit<?>  edit) {
        assert events.size() > 0;
        AggregateEvent result = new AggregateEvent(AbstractAggregateBeed.this, edit);
        for (Event event : events.values()) {
         try {
          result.addComponentEvent(event);
        }
        catch (AggregateEventStateException exc) {
          assert false : "AggregateEventStateException should not happen: we are not closed. " + exc;
        }
        }
        result.close();
        return result;
      }

    };

  public final int getMaximumRootUpdateSourceDistance() {
    return $dependent.getMaximumRootUpdateSourceDistance();
  }

  public final boolean isAggregateElement(Beed<?> b) {
    return (b != null) && b.isDependent($dependent);
  }

  public final void registerAggregateElement(Beed<?> b) {
    assert b != null;
    $dependent.addUpdateSource(b);
  }

  public final void deregisterAggregateElement(Beed<?> b) {
    assert b != null;
    $dependent.removeUpdateSource(b);
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

}
