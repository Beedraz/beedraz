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

package org.beedraz.semantics_II.expression.association.set;


import static java.util.Collections.singleton;
import static org.beedraz.semantics_II.Edit.State.DONE;
import static org.beedraz.semantics_II.Edit.State.UNDONE;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.HashMap;
import java.util.Map;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.expression.SimpleExpressionEdit;
import org.beedraz.semantics_II.expression.collection.set.ActualSetEvent;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * @mudo definition
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class BidirToOneEdit<_One_ extends BeanBeed,
                            _Many_ extends BeanBeed>
    extends SimpleExpressionEdit<BidirToManyBeed<_One_, _Many_>, EditableBidirToOneBeed<_One_, _Many_>, BidirToOneEvent<_One_, _Many_>> {

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
      getInitial().getOwner().deregisterAggregateElement(getTarget().getOwner());
      getInitial().remove(getTarget().getOwner());
    }
    super.performance();
    if (getGoal() != null) {
      getGoal().add(getTarget().getOwner());
      getGoal().getOwner().registerAggregateElement(getTarget().getOwner());
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
      getGoal().getOwner().deregisterAggregateElement(getTarget().getOwner());
      getGoal().remove(getTarget().getOwner());
    }
    super.unperformance();
    if (getInitial() != null) {
      getInitial().add(getTarget().getOwner());
      getInitial().getOwner().registerAggregateElement(getTarget().getOwner());
    }
  }

  /**
   * @post  result.size() >= 1;
   * @post  result.size() <= 3;
   * @post  result.get(getTarget()) = event;
   * @post  result.get(getTarget()).getSource() == getTarget();
   * @post  getOldValue() == null ? result.get(getTarget()).getOldValue() == null :
   *                                result.get(getTarget()).getOldValue().equals(getOldValue());
   * @post  getNewValue() == null ? result.get(getTarget()).getNewValue() == null :
   *                                result.get(getTarget()).getNewValue().equals(getNewValue());
   * @post  result.get(this).getEdit() == this;
   *
   * @mudo more posts
   */
  @Override
  protected final Map<AbstractBeed<?>, Event> createEvents() {
    assert (getState() == DONE) || (getState() == UNDONE);
    HashMap<AbstractBeed<?>, Event> result = new HashMap<AbstractBeed<?>, Event>();
    Event targetEvent =
        new BidirToOneEvent<_One_, _Many_>(getTarget(), getOldValue(), getNewValue(), this);
    result.put(getTarget(), targetEvent);
    BidirToManyBeed<_One_, _Many_> oldToMany = getOldValue();
    BidirToManyBeed<_One_, _Many_> newToMany = getNewValue();
    if (oldToMany != null) {
      /* We can use getTarget().getOwner() here as singleton for removed elements,
       * although the target already may have changed further before this method is called,
       * because the target's owner cannot change.
       */
      Event removedEvent =
          new ActualSetEvent<_Many_>(oldToMany, null, singleton(getTarget().getOwner()), this);
      result.put(oldToMany, removedEvent);
    }
    if (newToMany != null) {
      /* We can use getTarget().getOwner() here as singleton for added elements,
       * although the target already may have changed further before this method is called,
       * because the target's owner cannot change.
       */
      Event addedEvent =
          new ActualSetEvent<_Many_>(newToMany, singleton(getTarget().getOwner()), null, this);
      result.put(newToMany, addedEvent);
    }
    return result;
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

