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

package org.beedra_II.property.association;


import org.beedra_II.bean.BeanBeed;
import org.beedra_II.event.Event;
import org.beedra_II.property.simple.AbstractOldNewEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * {@link Event} that carries a simple old and new value,
 * expressing the changed that occured in {@link #getSource()}.
 * The {@link #getSource() source} must be a {@link SimplePB}.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof StringBeed;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class FinalBidirToOneEvent<_One_ extends BeanBeed>
    extends AbstractOldNewEvent<_One_>
    implements BidirToOneEvent<_One_> {

  /**
   * @pre source != null;
   * @post getSource() == sourcel
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   */
  public FinalBidirToOneEvent(EditableBidirToOneBeed<_One_, ?> source, _One_ oldValue, _One_ newValue) {
    super(source, oldValue, newValue);
  }

}

