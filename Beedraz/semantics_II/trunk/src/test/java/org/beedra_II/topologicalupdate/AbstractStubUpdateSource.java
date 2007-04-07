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

package org.beedra_II.topologicalupdate;


import org.beedra_II.Beed;
import org.beedra_II.Event;
import org.beedra_II.Listener;
import org.beedra_II.event.StubEvent;


public abstract class AbstractStubUpdateSource
    extends AbstractUpdateSource
    implements Beed<StubEvent> {

  protected AbstractStubUpdateSource(boolean event) {
    if (event) {
      $myEvent = new StubEvent(this);
    }
    else {
      $myEvent = null;
    }
  }

  protected AbstractStubUpdateSource() {
    this(true);
  }

  public void updateDependents() {
    updateDependents($myEvent);
  }

  public final StubEvent $myEvent;

  @Override
  protected void fireEvent(Event event) {
    $firedEvent = (StubEvent)event;
  }

  public StubEvent $firedEvent;


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

}
