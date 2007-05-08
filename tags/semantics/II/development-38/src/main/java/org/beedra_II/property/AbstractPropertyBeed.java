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

package org.beedra_II.property;


import org.beedra_II.AbstractBeed;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Event;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Support for implementations of {@link PropertyBeed}.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractPropertyBeed<_Event_ extends Event>
    extends AbstractBeed<_Event_>
    implements PropertyBeed<_Event_> {

  /**
   * @pre owner != null;
   */
  protected AbstractPropertyBeed(AggregateBeed owner) {
    assert owner != null;
    $owner = owner;
    owner.registerAggregateElement(this);
  }

  /**
   * {@inheritDoc}
   *
   * This method should be final, but it is overwritten in
   * BidirToManyBeed for a cast. If the owner types
   * was generic, this would not be necessary, and this
   * method could be final.
   */
  public AggregateBeed getOwner() {
    return $owner;
  }

  /**
   * @invar $owner != null;
   */
  private final AggregateBeed $owner;

 }
