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

package org.beedraz.semantics_II.aggregate;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.AbstractDependentBeed;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Support for implementations of {@link AggregateBeed}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractAggregateBeed
    extends AbstractDependentBeed<AggregateEvent>
    implements AggregateBeed {

  @Override
  protected AggregateEvent filteredUpdate(Map<Beed<?>, Event> events, Edit<?>  edit) {
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
