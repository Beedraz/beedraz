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

import java.util.Map;

import org.beedra_II.Beed;
import org.beedra_II.Event;
import org.beedra_II.edit.Edit;
import org.beedra_II.path.Path;
import org.beedra_II.path.PathEvent;
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

  @Override
  protected final _ResultEvent_ filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
    /* Events are from the argument, the left argument path, the right argument path, or
     * any combination.
     * React to event from paths first, setting the arguments. Then do a recalculate.
     */
    _Result_ oldValue = get();
    PathEvent<_LeftArgumentBeed_> leftPathEvent = (PathEvent<_LeftArgumentBeed_>)events.get($leftArgumentPath);
    if (leftPathEvent != null) {
      setLeftArg(leftPathEvent.getNewValue());
    }
    PathEvent<_RightArgumentBeed_> rightPathEvent = (PathEvent<_RightArgumentBeed_>)events.get($rightArgumentPath);
    if (rightPathEvent != null) {
      setRightArg(rightPathEvent.getNewValue());
    }
    recalculate();
    if (! equalValue(oldValue, get())) {
      return createNewEvent(oldValue, get(), edit);
    }
    else {
      return null;
    }
  }



  /*<property name="left argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   * @init null;
   */
  protected final Path<? extends _LeftArgumentBeed_> getLeftArgPath() {
    return $leftArgumentPath;
  }

  /**
   * @return getLeftArgPath() == null ? null : getLeftArgPath().get();
   */
  protected final _LeftArgumentBeed_ getLeftArg() {
    return $leftArgument;
  }

  protected final void setLeftArgPath(Path<? extends _LeftArgumentBeed_> beedPath) {
    _LeftArgumentBeed_ oldLeftArgument = $leftArgument;
    if ($leftArgumentPath != null) {
      removeUpdateSource($leftArgumentPath);
    }
    $leftArgumentPath = beedPath;
    _LeftArgumentBeed_ leftArgument = null;
    if ($leftArgumentPath != null) {
      leftArgument = $leftArgumentPath.get();
      addUpdateSource($leftArgumentPath);
    }
    if (leftArgument != oldLeftArgument) {
      setLeftArg(leftArgument);
    }
  }

  private Path<? extends _LeftArgumentBeed_> $leftArgumentPath;

  /**
   * @post getLeftArg() == leftArgument;
   */
  private final void setLeftArg(_LeftArgumentBeed_ leftArgument) {
    _Result_ oldValue = get();
    if ($leftArgument != null) {
      removeUpdateSource($leftArgument);
    }
    $leftArgument = leftArgument;
    recalculate();
    if ($leftArgument != null) {
      addUpdateSource($leftArgument);
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
   * @init null;
   */
  protected final Path<? extends _RightArgumentBeed_> getRightArgPath() {
    return $rightArgumentPath;
  }

  /**
   * @return getRightArgPath() == null ? null : getRightArgPath().get();
   */
  protected final _RightArgumentBeed_ getRightArg() {
    return $rightArgument;
  }

  protected final void setRightArgPath(Path<? extends _RightArgumentBeed_> beedPath) {
    _RightArgumentBeed_ oldRightArgument = $rightArgument;
    if ($rightArgumentPath != null) {
      removeUpdateSource($rightArgumentPath);
    }
    $rightArgumentPath = beedPath;
    _RightArgumentBeed_ rightArgument = null;
    if ($rightArgumentPath != null) {
      rightArgument = $rightArgumentPath.get();
      addUpdateSource($rightArgumentPath);
    }
    if (rightArgument != oldRightArgument) {
      setRightArg(rightArgument);
    }
  }

  private Path<? extends _RightArgumentBeed_> $rightArgumentPath;

  /**
   * @post getRightArg() == rightArgument;
   */
  private final void setRightArg(_RightArgumentBeed_ rightArgument) {
    _Result_ oldValue = get();
    if ($rightArgument != null) {
      removeUpdateSource($rightArgument);
    }
    $rightArgument = rightArgument;
    recalculate();
    if ($rightArgument != null) {
      addUpdateSource($rightArgument);
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

  /**
   * Implement like {@code getLeftArg().isEffective()}, with
   * appropriate methods offered by {@code _LeftArgumentBeed_}.
   */
  protected abstract boolean hasEffectiveLeftArgument();

  /**
   * Implement like {@code getRightArg().isEffective()}, with
   * appropriate methods offered by {@code _RightArgumentBeed_}.
   */
  protected abstract boolean hasEffectiveRightArgument();

  /**
   * Recalculate the value of this beed, and store the result, where
   * implementations that return the result can pick it up.
   *
   * @pre $leftArgument != null;
   * @pre leftArgument.isEffective();
   * @pre $rightArgument != null;
   * @pre $rightArgument.isEffective();
   */
  protected abstract void recalculateFrom(_LeftArgumentBeed_ leftArgument, _RightArgumentBeed_ rightArgument);

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

