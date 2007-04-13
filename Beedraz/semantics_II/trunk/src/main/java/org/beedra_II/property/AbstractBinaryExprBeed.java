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


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.beedra_II.Beed;
import org.beedra_II.Event;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of binary expression beeds, that represent a value derived
 * from 2 arguments.
 *
 * @invar getLeftArg() == null || getRightArg() == null
 *          ? get() == null
 *          : true;
 *
 * @protected
 * Accessor methods for the {@link #getLeftArg() left argument} and the {@link #getRightArg()
 * right argument} are kept protected, to force subclasses to provide meaningful public names for the
 * arguments.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBinaryExprBeed<_Result_ extends Object,
                                             _ResultEvent_ extends Event,
                                             _LeftArgumentBeed_ extends Beed<? extends _LeftArgumentEvent_>,
                                             _LeftArgumentEvent_ extends Event,
                                             _RightArgumentBeed_ extends Beed<? extends _RightArgumentEvent_>,
                                             _RightArgumentEvent_ extends Event>
    extends AbstractExprBeed<_Result_, _ResultEvent_>  {

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  getLeftArg() == null;
   * @post  getRightArg() == null;
   * @post  get() == null;
   */
  public AbstractBinaryExprBeed(AggregateBeed owner) {
    super(owner);
  }

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected _ResultEvent_ filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
        assert $leftArgument != null || $rightArgument != null;
        _Result_ oldValue = get();
        recalculate();
        if (! equalValue(oldValue, get())) {
          return createNewEvent(oldValue, get(), edit);
        }
        else {
          return null;
        }
      }

    };

  protected abstract boolean equalValue(_Result_ r1, _Result_ r2);

  public final int getMaximumRootUpdateSourceDistance() {
    /* FIX FOR CONSTRUCTION PROBLEM
     * At construction, the super constructor is called with the future owner
     * of this property beed. Eventually, in the constructor code of AbstractPropertyBeed,
     * this object is registered as update source with the dependent of the
     * aggregate beed. During that registration process, the dependent
     * checks to see if we need to ++ our maximum root update source distance.
     * This involves a call to this method getMaximumRootUpdateSourceDistance.
     * Since however, we are still doing initialization in AbstractPropertyBeed,
     * initialization code (and construction code) further down is not yet executed.
     * This means that our $dependent is still null, and this results in a NullPointerException.
     * On the other hand, we cannot move the concept of $dependent up, since not all
     * property beeds have a dependent.
     * The fix implemented here is the following:
     * This problem only occurs during construction. During construction, we will
     * not have any update sources, so our maximum root update source distance is
     * effectively 0.
     */
    /*
     * TODO This only works if we only add 1 update source during construction,
     *      so a better solution should be sought.
     */
    return $dependent == null ? 0 : $dependent.getMaximumRootUpdateSourceDistance();
  }


  /*<property name="left argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  protected final _LeftArgumentBeed_ getLeftArg() {
    return $leftArgument;
  }

  /**
   * @post getLeftArg() == leftArgument;
   */
  protected final void setLeftArg(_LeftArgumentBeed_ leftArgument) {
    _Result_ oldValue = get();
    if ($leftArgument != null) {
      $dependent.removeUpdateSource($leftArgument);
    }
    $leftArgument = leftArgument;
    recalculate();
    if ($leftArgument != null) {
      $dependent.addUpdateSource($leftArgument);
    }
    if (! equalValue(oldValue, get())) {
      updateDependents(createNewEvent(oldValue, get(), null));
    }
  }

  private _LeftArgumentBeed_ $leftArgument;

  /*</property>*/


  /*<property name="right argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  protected final _RightArgumentBeed_ getRightArg() {
    return $rightArgument;
  }

  /**
   * @post getRightArg() == rightArgument;
   */
  protected final void setRightArg(_RightArgumentBeed_ rightArgument) {
    _Result_ oldValue = get();
    if ($rightArgument != null) {
      $dependent.removeUpdateSource($rightArgument);
    }
    $rightArgument = rightArgument;
    recalculate();
    if ($rightArgument != null) {
      $dependent.addUpdateSource($rightArgument);
    }
    if (! equalValue(oldValue, get())) {
      updateDependents(createNewEvent(oldValue, get(), null));
    }
  }

  private _RightArgumentBeed_ $rightArgument;

  /*</property>*/



  /**
   * @pre $leftArgument != null || $rightArgument != null;
   */
  private void recalculate() {
    if (($leftArgument != null) && hasEffectiveLeftArgument() &&
        ($rightArgument != null) && hasEffectiveRightArgument()) {
      recalculateFrom($leftArgument, $rightArgument);
      assignEffective(true);
    }
    else {
      assignEffective(false);
    }
  }

  protected abstract boolean hasEffectiveLeftArgument();

  protected abstract boolean hasEffectiveRightArgument();

  /**
   * @pre $leftArgument != null;
   * @pre leftArgument.isEffective();
   * @pre $rightArgument != null;
   * @pre $rightArgument.isEffective();
   */
  protected abstract void recalculateFrom(_LeftArgumentBeed_ leftArgument, _RightArgumentBeed_ rightArgument);

  public final Set<? extends UpdateSource> getUpdateSources() {
    return $dependent.getUpdateSources();
  }

  private final static Set<? extends UpdateSource> PHI = Collections.emptySet();

  public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
    /* fixed to make it possible to use this method during construction,
     * before $dependent is initialized. But that is bad code, and should be
     * fixed.
     */
    return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
  }

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
    sb.append(indent(level + 1) + "arguments:\n");
    if (getLeftArg() == null && getRightArg() == null) {
      sb.append(indent(level + 2) + "null");
    }
    if (getLeftArg() != null) {
      getLeftArg().toString(sb, level + 2);
    }
    if (getRightArg() != null) {
      getRightArg().toString(sb, level + 2);
    }
  }


  /**
   * The operator of this binary expression.
   */
  public abstract String getOperatorString();

}

