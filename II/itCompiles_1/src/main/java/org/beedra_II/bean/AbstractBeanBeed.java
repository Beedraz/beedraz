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

package org.beedra_II.bean;


import org.beedra_II.AbstractBeed;
import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
import org.beedra_II.property.PropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * Support for implementations of {@link BeanBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBeanBeed
    extends AbstractBeed<BeanEvent>
    implements BeanBeed {

  private final Listener<Event> $propagationListener = new Listener<Event>() {

    public void beedChanged(Event event) {
      fireChangeEvent(new BeanEvent(AbstractBeanBeed.this, event));
    }

  };

  protected final void registerProperty(PropertyBeed<?> pb) {
    pb.addListener($propagationListener);
  }

}
