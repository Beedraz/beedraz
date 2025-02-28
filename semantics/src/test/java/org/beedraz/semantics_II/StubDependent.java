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

package org.beedraz.semantics_II;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Dependent;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.Listener;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class StubDependent extends Dependent implements Beed<StubEvent> {

  @Override
  void fireEvent(Event event) {
    $firedEvent = event;
  }

  @Override
  public Beed<?> getDependentUpdateSource() {
    return $myDependentUpdateSource;
  }

  @Override
  public Set<Dependent> getDependents() {
    return Collections.emptySet();
  }

  @Override
  protected Event filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    $updated++;
    $events = events;
    return $myEvent;
  }

  public int $updated = 0;

  public Map<Beed<?>, Event> $events;

  public final StubBeed $myDependentUpdateSource = new StubBeed();

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

  @Deprecated
  public boolean isDependent(Dependent dependent) {
    return false;
  }

  public boolean isTransitiveDependent(Dependent dependent) {
    return false;
  }

  public void removeDependent(Dependent dependent) {
    // NOP
  }

  public Set<? extends Beed<?>> getRootUpdateSources() {
    HashSet<Beed<?>> result = new HashSet<Beed<?>>();
    for (Beed<?> us : getUpdateSourcesTransitiveClosure()) {
      if (us.getMaximumRootUpdateSourceDistance() == 0) {
        result.add(us);
      }
    }
    return result;
  }

}
