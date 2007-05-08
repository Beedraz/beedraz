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

package org.beedra_II.property.association.set.ordered;


import static org.beedra_II.edit.Edit.State.DONE;
import static org.beedra_II.edit.Edit.State.NOT_YET_PERFORMED;
import static org.beedra_II.edit.Edit.State.UNDONE;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.property.simple.SimplePropertyEdit;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.collection_I.LinkedListOrderedSet;
import org.ppeew.collection_I.OrderedSet;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class OrderedBidirToOneEdit<_One_ extends BeanBeed,
                                  _Many_ extends BeanBeed>
    extends SimplePropertyEdit<OrderedBidirToManyBeed<_One_, _Many_>,
                               EditableOrderedBidirToOneBeed<_One_, _Many_>,
                               OrderedBidirToOneEvent<_One_, _Many_>> {

  /**
   * @pre  target != null;
   * @post getTarget() == target;
   */
  public OrderedBidirToOneEdit(EditableOrderedBidirToOneBeed<_One_, _Many_> target) {
    super(target);
  }


  /*<property name="goalPosition">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Integer getGoalPosition() {
    return $goalPosition;
  }

  /**
   * @post ComparisonUtil.equalsWithNull(goalValue, getGoalValue());
   */
  public final void setGoalPosition(Integer goalPosition) throws EditStateException {
    if (getState() != NOT_YET_PERFORMED) {
      throw new EditStateException(this, getState(), NOT_YET_PERFORMED);
    }
    $goalPosition = goalPosition;
    recalculateValidity();
  }

  private Integer $goalPosition;

  /*</property>*/


  /*<property name="goalPosition">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Integer getInitialPosition() {
    return $initialPosition;
  }

  private Integer $initialPosition;

  /*</property>*/

  /**
   * @return getState() == DONE ? getInitialPosition() : getGoalPosition();
   */
  public final Integer getOldPosition() {
    return getState() == DONE ? getInitialPosition() : getGoalPosition();
  }

  /**
   * @return getState() == DONE ? getGoalPosition() : getInitialPosition();
   */
  public final Integer getNewPosition() {
    return getState() == DONE ? getGoalPosition() : getInitialPosition();
  }

  /**
   * @return  getTarget().isAcceptable(getGoal()) &&
   *          ( getGoal() == null ||
   *            getGoal().isAcceptable(getTarget().getOwner(), getGoalPosition()) );
   */
  @Override
  protected boolean isAcceptable() {
    return super.isAcceptable() &&
           ( getGoal() == null ||
             getGoal().isAcceptable(getTarget().getOwner(), getGoalPosition()) );
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getInitial(), getTarget().get());
   * @post ComparisonUtil.equalsWithNull(
   *           getInitialPosition(),
   *           getTarget().get() != null
   *             ? getTarget().get().indexOf(getTarget().getOwner()))
   *             : null;
   */
  @Override
  protected final void storeInitialState() {
    super.storeInitialState();
    $initialPosition =
      getTarget().get() != null
        ? getTarget().get().indexOf(getTarget().getOwner())
        : null;
  }

  /**
   * @return  ! ComparisonUtil.equalsWithNull(getInitial(), getGoal()) ||
   *          ! ComparisonUtil.equalsWithNull(getInitialPosition(), getGoalPosition());
   */
  @Override
  public final boolean isChange() {
    return super.isChange() ||
           ! ComparisonUtil.equalsWithNull(getInitialPosition(), getGoalPosition());
  }

  /**
   * @return ComparisonUtil.equalsWithNull(getInitial(), getTarget().get()) &&
   *         ComparisonUtil.equalsWithNull(
   *             getInitialPosition(),
   *             getTarget().get() != null
   *               ? getTarget().get().indexOf(getTarget().getOwner())
   *               : null);
   */
  @Override
  protected final boolean isInitialStateCurrent() {
    return super.isInitialStateCurrent() &&
           ComparisonUtil.equalsWithNull(
               getInitialPosition(),
               getTarget().get() != null
                 ? getTarget().get().indexOf(getTarget().getOwner())
                 : null);
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getGoal(), getTarget().get());
   * @post getInitial() != null
   *         ==> !getInitial().get().contains(getTarget().getOwner());
   * @post let
   *         new_position = getGoalPosition() == null
   *                          ? getGoal().get().size()
   *                          : getGoalPosition().intValue()
   *       in
   *         getGoal() != null
   *           ==> getGoal().get().indexOf(getTarget().getOwner()) == new_position;
   * @post let
   *         new_position = getGoalPosition() == null
   *                          ? getGoal().get().size()
   *                          : getGoalPosition().intValue()
   *       in
   *         getGoal() != null && getGoalPosition() == null
   *           ==> new.getGoalPosition() == old.getGoal().get().size();
   */
  @Override
  protected final void performance() {
    if (getGoal() != null && getGoalPosition() == null) {
      int newPosition;
      if (getGoal() == getInitial()) {
        newPosition = getGoal().get().size() - 1;
      }
      else {
        newPosition = getGoal().get().size();
      }
      try {
        setGoalPosition(newPosition);
      }
      catch (EditStateException e) {
        // Should not happen
        e.printStackTrace();
      }
    }
    if (getInitial() != null) {
      getInitial().remove(getTarget().getOwner());
    }
    super.performance();
    if (getGoal() != null) {
      getGoal().add(getGoalPosition(), getTarget().getOwner());
    }
  }


  /**
   * @return ComparisonUtil.equalsWithNull(getGoal(), getTarget().get()) &&
   *         (  getTarget().get() == null ||
   *            ComparisonUtil.equalsWithNull(
   *               getGoalPosition() != null
   *                 ? getGoalPosition()
   *                 : getTarget().get().get().size() - 1,
   *               getTarget().get().indexOf(getTarget().getOwner())));
   */
  @Override
  protected final boolean isGoalStateCurrent() {
    return super.isGoalStateCurrent() &&
           (getTarget().get() == null ||
            ComparisonUtil.equalsWithNull(
                getGoalPosition() != null
                  ? getGoalPosition()
                  : getTarget().get().get().size() - 1,
                getTarget().get().indexOf(getTarget().getOwner())));
  }

  /**
   * @post ComparisonUtil.equalsWithNull(getInitial(), getTarget().get());
   * @post let
   *         new_position = getInitialPosition() == null
   *                          ? getInitial().get().size()
   *                          : getInitialPosition().intValue()
   *       in
   *         getInitial() != null
   *           ==> getInitial().get().indexOf(getTarget().getOwner()) == new_position;
   * @post getGoal() != null
   *         ==> !getGoal().get().contains(getTarget().getOwner());
   */
  @Override
  protected final void unperformance() {
    if (getGoal() != null) {
      getGoal().remove(getTarget().getOwner());
    }
    super.unperformance();
    if (getInitial() != null) {
      int newPosition = getInitialPosition() == null
                          ? getInitial().get().size()
                          : getInitialPosition().intValue();
      getInitial().add(newPosition, getTarget().getOwner());
    }
  }

  /**
   *
   */
  @Override
  protected final void notifyListeners() {
    super.notifyListeners();
    assert (getState() == DONE) || (getState() == UNDONE);
    OrderedBidirToManyBeed<_One_, _Many_> oldToMany = getOldValue();
    Integer oldPosition = getOldPosition();
    OrderedBidirToManyBeed<_One_, _Many_> newToMany = getNewValue();
    Integer newPosition = getNewPosition();
    if (oldToMany != null) {
      OrderedSet<_Many_> oldValue = new LinkedListOrderedSet<_Many_>();
      oldValue.addAll(oldToMany.get());
      oldValue.add(oldPosition, getTarget().getOwner());
      OrderedSet<_Many_> newValue = new LinkedListOrderedSet<_Many_>();
      newValue.addAll(oldToMany.get());
      oldToMany.fireChangeEvent(oldValue, newValue, this);
    }
    if (newToMany != null) {
      OrderedSet<_Many_> oldValue = new LinkedListOrderedSet<_Many_>();
      oldValue.addAll(newToMany.get());
      oldValue.remove(newPosition.intValue());
      OrderedSet<_Many_> newValue = new LinkedListOrderedSet<_Many_>();
      newValue.addAll(newToMany.get());
      newToMany.fireChangeEvent(oldValue, newValue, this);
    }
  }

  /**
   * @post  result.getSource() == getTarget();
   * @post  result.getOldValue() == getOldValue();
   * @post  result.getNewValue() == getNewValue();
   * @post  result.getEdit() == this;
   */
  @Override
  protected OrderedBidirToOneEvent<_One_, _Many_> createEvent() {
    return new OrderedBidirToOneEvent<_One_, _Many_>(getTarget(), getOldValue(), getNewValue(), this);
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

