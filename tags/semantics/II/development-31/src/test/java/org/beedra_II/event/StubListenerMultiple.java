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

package org.beedra_II.event;


public class StubListenerMultiple<_Event_ extends Event>
    implements Listener<_Event_> {

  public void beedChanged(_Event_ event) {
    if ($event1 == null) {
      $event1 = event;
    }
    else if ($event2 == null) {
      $event2 = event;
    }
  }

  public void reset() {
    $event1 = null;
    $event2 = null;
  }

  public _Event_ $event1;
  public _Event_ $event2;

}
