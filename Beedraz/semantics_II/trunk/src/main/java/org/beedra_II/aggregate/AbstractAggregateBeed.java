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


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedra_II.AbstractDependentBeed;
import org.beedra_II.Beed;
import org.beedra_II.Event;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
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
    extends AbstractDependentBeed<AggregateEvent>
    implements AggregateBeed {

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

  public final boolean isAggregateElement(Beed<?> b) {
    return $aggregateElements.contains(b);
  }

  /**
   * @pre b != null;
   * @post isAggregateElement(b);
   */
  public final void registerAggregateElement(Beed<?> b) {
    assert b != null;
    $aggregateElements.add(b);
    addUpdateSource(b);
  }

  /**
   * @post ! isAggregateElement(b);
   */
  public final void deregisterAggregateElement(Beed<?> b) {
    removeUpdateSource(b);
    $aggregateElements.remove(b);
  }

  /**
   * @invar $aggregateElements != null;
   * @invar Collections.noNull($aggregateElements);
   */
  private final Set<Beed<?>> $aggregateElements = new HashSet<Beed<?>>();

}
