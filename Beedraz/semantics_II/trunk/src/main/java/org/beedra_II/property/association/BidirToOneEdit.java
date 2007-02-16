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


import static org.beedra.util_I.MultiLineToStringUtil.indent;
import static org.beedra_II.edit.Edit.State.DONE;
import static org.beedra_II.edit.Edit.State.UNDONE;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.property.simple.SimpleEdit;
import org.toryt.util_I.annotations.vcs.CvsInfo;


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
    extends SimpleEdit<BidirToManyBeed<_One_, _Many_>, EditableBidirToOneBeed<_One_, _Many_>, BidirToOneEvent<_One_, _Many_>> {

  /**
   * @pre target != null;
   * @post getTarget() == target;
   */
  public BidirToOneEdit(EditableBidirToOneBeed<_One_, _Many_> target) {
    super(target);
  }

  /**
   * @post Comparison.equalsWithNull(getGoal(), getTarget().get());
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
   * @post Comparison.equalsWithNull(getInitial(), getTarget().get());
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

  @Override
  protected final void notifyListeners() {
    super.notifyListeners();
    assert (getState() == DONE) || (getState() == UNDONE);
    BidirToManyBeed<_One_, _Many_> oldToMany = (getState() == DONE) ? getInitial() : getGoal();
    BidirToManyBeed<_One_, _Many_> newToMany = (getState() == DONE) ? getGoal() : getInitial();
    if (oldToMany != null) {
      oldToMany.fireRemovedEvent(getTarget().getOwner(), this);
    }
    if (newToMany != null) {
      newToMany.fireAddedEvent(getTarget().getOwner(), this);
    }
  }

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

