/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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

package org.beedraz.semantics_II.topologicalupdate;


import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.Listener;
import org.beedraz.semantics_II.StubEvent;
import org.beedraz.semantics_II.edit.Edit;
import org.beedraz.semantics_II.topologicalupdate.Dependent;
import org.beedraz.semantics_II.topologicalupdate.UpdateSource;


public class StubDependent extends Dependent implements Beed<StubEvent> {

  @Override
  void fireEvent(Event event) {
    $firedEvent = event;
  }

  @Override
  public UpdateSource getDependentUpdateSource() {
    return $myDependentUpdateSource;
  }

  @Override
  Set<Dependent> getDependents() {
    return Collections.emptySet();
  }

  @Override
  protected Event filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
    $updated++;
    $events = events;
    return $myEvent;
  }

  public int $updated = 0;

  public Map<UpdateSource, Event> $events;

  public final StubUpdateSource $myDependentUpdateSource = new StubUpdateSource();

  public final StubEvent $myEvent = new StubEvent(this);

  public Event $firedEvent;



  // stub beed methods

  public void addListener(Listener<? super StubEvent> listener) {
    // NOP
  }

  public boolean isListener(Listener<? super StubEvent> listener) {
    return false;
  }

  public void removeListener(Listener<? super StubEvent> listener) {
    // NOP
  }

  public void toString(StringBuffer sb, int i) {
    // NOP
  }

  public void addDependent(Dependent dependent) {
    // NOP
  }

  public boolean isDependent(Dependent dependent) {
    return false;
  }

  public boolean isTransitiveDependent(Dependent dependent) {
    return false;
  }

  public void removeDependent(Dependent dependent) {
    // NOP
  }

  public Set<? extends UpdateSource> getRootUpdateSources() {
    HashSet<UpdateSource> result = new HashSet<UpdateSource>();
    for (UpdateSource us : getUpdateSourcesTransitiveClosure()) {
      if (us.getMaximumRootUpdateSourceDistance() == 0) {
        result.add(us);
      }
    }
    return result;
  }

}
