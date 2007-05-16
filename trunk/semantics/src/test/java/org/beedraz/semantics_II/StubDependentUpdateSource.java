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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.AbstractUpdateSourceDependentDelegate;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Dependent;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class StubDependentUpdateSource extends AbstractStubUpdateSource {

  public StubDependentUpdateSource(boolean event) {
    super(event);
  }

  public StubDependentUpdateSource() {
    this(false);
  }

  public final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

    @Override
    public StubEvent filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
      $updated++;
      $events = new HashMap<Beed<?>, Event>(events);
      return $myEvent;
    }

  };

  public int $updated = 0;

  public Map<Beed<?>, Event> $events;

  public StubEvent $firedEvent;

  public int getMaximumRootUpdateSourceDistance() {
    return $dependent.getMaximumRootUpdateSourceDistance();
  }

  public void addUpdateSource(Beed<?> updateSource) {
    $dependent.addUpdateSource(updateSource);
  }

  public void removeUpdateSource(Beed<?> updateSource) {
    $dependent.removeUpdateSource(updateSource);
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

}
