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

package org.beedra_II.property.bool;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.beedra_II.Event;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.path.Path;
import org.beedra_II.path.PathEvent;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * Beed that expresses whether two {@link BeanBeed bean beeds} are equal or not.
 *
 * @invar  get() == getLeftArgument() == getRightArgument();
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanEqualBeanBeedsBeed<_BeanBeed_ extends BeanBeed>
    extends AbstractPropertyBeed<BooleanEvent>
    implements BooleanBeed {

  /**
   * @post  getLeftArgument() == null;
   * @post  getRightArgument() == null;
   * @post  get() == true;
   */
  public BooleanEqualBeanBeedsBeed() {
    super(null);
    $value = true;
  }

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected BooleanEvent filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
        /* Events are from:
         * - the left argument,
         * - the left argument path,
         * - the right argument or
         * - the right argument path
         * React to the path events first, setting the corresponding argument.
         * Then do a recalculate.
         */
        Boolean oldValue = get();
        PathEvent<_BeanBeed_> leftArgumentPathEvent = (PathEvent<_BeanBeed_>)events.get($leftArgumentPath);
        if (leftArgumentPathEvent != null) {
          setLeftArg(leftArgumentPathEvent.getNewValue());
        }
        PathEvent<_BeanBeed_> rightArgumentPathEvent = (PathEvent<_BeanBeed_>)events.get($rightArgumentPath);
        if (rightArgumentPathEvent != null) {
          setRightArg(rightArgumentPathEvent.getNewValue());
        }
        recalculate();
        if (! MathUtil.equalValue(oldValue, get())) {
          return createNewEvent(oldValue, get(), edit);
        }
        else {
          return null;
        }
      }

    };


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
  public final Path<? extends _BeanBeed_> getLeftArgumentPath() {
    return $leftArgumentPath;
  }

  /**
   * The old left argument path is removed as update source.
   * The new left argument path is added as update source.
   * The left argument is replaced by the new left argument: see {@link #setLeftArg(BeanBeed)}.
   */
  public final void setLeftArgumentPath(Path<? extends _BeanBeed_> leftArgumentPath) {
    if ($leftArgumentPath != null) {
      $dependent.removeUpdateSource($leftArgumentPath);
    }
    $leftArgumentPath = leftArgumentPath;
    if ($leftArgumentPath != null) {
      $dependent.addUpdateSource($leftArgumentPath);
      setLeftArg($leftArgumentPath.get());
    }
    else {
      setLeftArg(null);
    }
  }

  private Path<? extends _BeanBeed_> $leftArgumentPath;

  /**
   * @basic
   */
  protected final _BeanBeed_ getLeftArg() {
    return $leftArgument;
  }

  /**
   * @post getLeftArg() == leftArgument;
   */
  private final void setLeftArg(_BeanBeed_ leftArgument) {
    Boolean oldValue = get();
    if ($leftArgument != null) {
      $dependent.removeUpdateSource($leftArgument);
    }
    $leftArgument = leftArgument;
    recalculate();
    if ($leftArgument != null) {
      $dependent.addUpdateSource($leftArgument);
    }
    if (! MathUtil.equalValue(oldValue, get())) {
      updateDependents(createNewEvent(oldValue, get(), null));
    }
  }

  private _BeanBeed_ $leftArgument;

  /*</property>*/


  /*<property name="right argument">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends _BeanBeed_> getRightArgumentPath() {
    return $rightArgumentPath;
  }

  /**
   * The old right argument path is removed as update source.
   * The new right argument path is added as update source.
   * The right argument is replaced by the new right argument: see {@link #setRightArg(BeanBeed)}.
   */
  public final void setRightArgumentPath(Path<? extends _BeanBeed_> rightArgumentPath) {
    if ($rightArgumentPath != null) {
      $dependent.removeUpdateSource($rightArgumentPath);
    }
    $rightArgumentPath = rightArgumentPath;
    if ($rightArgumentPath != null) {
      $dependent.addUpdateSource($rightArgumentPath);
      setRightArg($rightArgumentPath.get());
    }
    else {
      setRightArg(null);
    }
  }

  private Path<? extends _BeanBeed_> $rightArgumentPath;

  /**
   * @basic
   */
  protected final _BeanBeed_ getRightArg() {
    return $rightArgument;
  }

  /**
   * @post getRightArg() == rightArgument;
   */
  private final void setRightArg(_BeanBeed_ rightArgument) {
    Boolean oldValue = get();
    if ($rightArgument != null) {
      $dependent.removeUpdateSource($rightArgument);
    }
    $rightArgument = rightArgument;
    recalculate();
    if ($rightArgument != null) {
      $dependent.addUpdateSource($rightArgument);
    }
    if (! MathUtil.equalValue(oldValue, get())) {
      updateDependents(createNewEvent(oldValue, get(), null));
    }
  }

  private _BeanBeed_ $rightArgument;

  /*</property>*/

  protected final BooleanEvent createNewEvent(Boolean oldValue, Boolean newValue, Edit<?> edit) {
    return new BooleanEvent(this, oldValue, newValue, edit);
  }


  public boolean isEffective() {
    return true;
  }

  public final Boolean getBoolean() {
    return Boolean.valueOf(getboolean());
  }

  public final boolean getboolean() {
    return $value;
  }

  public final Boolean get() {
    return getBoolean();
  }

  private boolean $value;



  private void recalculate() {
    $value = (getLeftArg() == getRightArg());
  }



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
  public String getOperatorString() {
    return "==";
  }

}

