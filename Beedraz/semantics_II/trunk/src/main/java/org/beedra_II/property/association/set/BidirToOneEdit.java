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

package org.beedra_II.property.association.set;


import static org.beedra_II.edit.Edit.State.DONE;
import static org.beedra_II.edit.Edit.State.UNDONE;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.HashMap;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.event.Event;
import org.beedra_II.property.collection.set.ActualSetEvent;
import org.beedra_II.property.simple.SimplePropertyEdit;
import org.beedra_II.topologicalupdate.AbstractUpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.collection_I.Singleton;


/**
 * @mudo definition
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BidirToOneEdit<_One_ extends BeanBeed,
                            _Many_ extends BeanBeed>
    extends SimplePropertyEdit<BidirToManyBeed<_One_, _Many_>, EditableBidirToOneBeed<_One_, _Many_>, BidirToOneEvent<_One_, _Many_>> {

  /**
   * @pre  target != null;
   * @post getTarget() == target;
   */
  public BidirToOneEdit(EditableBidirToOneBeed<_One_, _Many_> target) {
    super(target);
  }

  @Override
  protected boolean isAcceptable() {
    return super.isAcceptable() &&
           ( getGoal() == null ||
             getGoal().isAcceptable(getTarget().getOwner()) );
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getGoal(), getTarget().get());
   * @post getInitial() != null
   *         ? !getInitial().get().contains(getTarget().getOwner());
   * @post getGoal() != null
   *         ? getGoal().get().contains(getTarget().getOwner());
   */
  @Override
  protected final void performance() {
    if (getInitial() != null) {
      getInitial().remove(getTarget().getOwner());
    }
    super.performance();
    if (getGoal() != null) {
      getGoal().add(getTarget().getOwner());
    }
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getInitial(), getTarget().get());
   * @post getInitial() != null
   *         ? getInitial().get().contains(getTarget().getOwner());
   * @post getGoal() != null
   *         ? !getGoal().get().contains(getTarget().getOwner());
   */
  @Override
  protected final void unperformance() {
    if (getGoal() != null) {
      getGoal().remove(getTarget().getOwner());
    }
    super.unperformance();
    if (getInitial() != null) {
      getInitial().add(getTarget().getOwner());
    }
  }

  /**
   * @todo super method should be final; can we fix this with a change listener to the to-one that propagates?
   */
  @Override
  protected final void updateDependents() {
    HashMap<AbstractUpdateSource, Event> events = new HashMap<AbstractUpdateSource, Event>();
    events.put(getTarget(), createEvent());
    assert (getState() == DONE) || (getState() == UNDONE);
    BidirToManyBeed<_One_, _Many_> oldToMany = getOldValue();
    BidirToManyBeed<_One_, _Many_> newToMany = getNewValue();
    if (oldToMany != null) {
      ActualSetEvent<_Many_> removedEvent =
          new ActualSetEvent<_Many_>(oldToMany, null, new Singleton<_Many_>(getTarget().getOwner()), this);
      events.put(oldToMany, removedEvent);
    }
    if (newToMany != null) {
      ActualSetEvent<_Many_> addedEvent =
          new ActualSetEvent<_Many_>(newToMany, new Singleton<_Many_>(getTarget().getOwner()), null, this);
      events.put(newToMany, addedEvent);
    }
    EditableBidirToOneBeed.packageUpdateDependents(events);
  }

  /**
   * @post  result.getSource() == getTarget();
   * @post  result.getOldValue() == getOldValue();
   * @post  result.getNewValue() == getNewValue();
   * @post  result.getEdit() == this;
   */
  @Override
  protected BidirToOneEvent<_One_, _Many_> createEvent() {
    return new BidirToOneEvent<_One_, _Many_>(getTarget(), getOldValue(), getNewValue(), this);
  }

  @Override
  protected void toStringGoalInitial(StringBuffer sb, int level) {
    sb.append(indent(level) + "goal:");
    if (getGoal() == null) {
      sb.append(" null\n");
    }
    else {
      sb.append("\n");
      getGoal().toString(sb, level + 1);
      sb.append(indent(level + 1) + "owner:\n");
      getGoal().getOwner().toString(sb, level + 2);
    }
    sb.append(indent(level) + "initial:");
    if (getInitial() == null) {
      sb.append(" null\n");
    }
    else {
      sb.append("\n");
      getInitial().toString(sb, level + 1);
      sb.append(indent(level + 1) + "owner:\n");
      getInitial().getOwner().toString(sb, level + 2);
    }
  }

}

