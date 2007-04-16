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
 * Abstract implementation of unary expression beeds, that represent a value derived
 * from one argument.
 *
 * @invar getArgumentPath() != null
 *          ? getArgument() == getArgumentPath().get()
 *          : null;
 * @invar getArgument() == null
 *          ? get() == null
 *          : true;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractUnaryExprBeed<_Result_ extends Object,
                                            _ResultEvent_ extends Event,
                                            _ArgumentBeed_ extends Beed<? extends _ArgumentEvent_>,
                                            _ArgumentEvent_ extends Event>
    extends AbstractPrimitiveDependentExprBeed<_Result_, _ResultEvent_>  {

  @Override
  protected final _ResultEvent_ filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
    /* Events are from the argument or the argument path, or both.
     * React to event from path first, setting the argument. Then do a recalculate.
     */
    _Result_ oldValue = get();
    PathEvent<_ArgumentBeed_> pathEvent = (PathEvent<_ArgumentBeed_>)events.get($argumentPath);
    if (pathEvent != null) {
      setArgument(pathEvent.getNewValue());
    }
    recalculate();
    if (! equalValue(oldValue, get())) {
      return createNewEvent(oldValue, get(), edit);
    }
    else {
      return null;
    }
  }


  /*<property name="argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   * @init null;
   */
  public final Path<? extends _ArgumentBeed_> getArgumentPath() {
    return $argumentPath;
  }

  /**
   * @return getArgumentPath() == null ? null : getArgumentPath().get();
   */
  public final _ArgumentBeed_ getArgument() {
    return $argument;
  }

  public final void setArgumentPath(Path<? extends _ArgumentBeed_> beedPath) {
    _ArgumentBeed_ oldArgument = $argument;
    if ($argumentPath != null) {
      removeUpdateSource($argumentPath);
    }
    $argumentPath = beedPath;
    _ArgumentBeed_ argument = null;
    if ($argumentPath != null) {
      argument = $argumentPath.get();
      addUpdateSource($argumentPath);
    }
    if (argument != oldArgument) {
      setArgument(argument);
    }
  }

  private Path<? extends _ArgumentBeed_> $argumentPath;

  /**
   * @post getArgument() == argument;
   */
  private final void setArgument(_ArgumentBeed_ argument) {
    _Result_ oldValue = get();
    if ($argument != null) {
      removeUpdateSource($argument);
    }
    $argument = argument;
    if ($argument != null) {
      recalculate();
      addUpdateSource($argument);
    }
    else {
      assignEffective(false);
    }
    if (! equalValue(oldValue, get())) {
      _ResultEvent_ event = createNewEvent(oldValue, get(), null);
      updateDependents(event);
    }
  }

  private _ArgumentBeed_ $argument;

  /*</property>*/



  /**
   * @pre $argument != null;
   *
   * @default  This method could be overriden in subclasses
   */
  protected void recalculate() {
    if (hasEffectiveArgument()) {
      recalculateFrom($argument);
      assignEffective(true);
    }
    else {
      assignEffective(false);
    }
  }

  /**
   * Implement like {@code getArgument().isEffective()}, with
   * appropriate methods offered by {@code _ArgumentBeed_}.
   */
  protected abstract boolean hasEffectiveArgument();

  /**
   * @pre argument != null;
   * @pre argument.isEffective();
   */
  protected abstract void recalculateFrom(_ArgumentBeed_ argument);

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
    sb.append(indent(level + 1) + "argument:\n");
    if (getArgument() != null) {
      getArgument().toString(sb, level + 2);
    }
    else {
      sb.append(indent(level + 2) + "null");
    }
  }

  /**
   * The operator of this binary expression.
   */
  public abstract String getOperatorString();

}

