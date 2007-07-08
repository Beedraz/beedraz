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

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractStubUpdateSource
    extends AbstractBeed<StubEvent>
    implements Beed<StubEvent> {

  protected AbstractStubUpdateSource(boolean event) {
    if (event) {
      $myEvent = new StubEvent(this);
    }
    else {
      $myEvent = null;
    }
    addListener($listener);
  }

  protected AbstractStubUpdateSource() {
    this(true);
  }

  public void publicTopologicalUpdateStart() {
    TopologicalUpdateAccess.topologicalUpdate(this, $myEvent);
  }

  public final StubEvent $myEvent;

  public StubEvent $firedEvent;

  private Listener<StubEvent> $listener = new Listener<StubEvent>() {

    public void beedChanged(StubEvent event) {
      $firedEvent = event;
    }

  };

}
