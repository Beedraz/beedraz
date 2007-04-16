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

import org.beedra_II.AbstractBeed;
import org.beedra_II.Event;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.path.Path;
import org.beedra_II.path.PathEvent;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * Abstract implementation of binary expression beeds, that represent a boolean value derived
 * from 2 arguments of type {@link BeanBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBeanArgBooleanUnaryExpressionBeed<_BeanBeed_ extends BeanBeed>
    extends AbstractBeed<BooleanEvent>
    implements BooleanBeed {

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected BooleanEvent filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
        /* Events are from:
         * - the beed,
         * - the beed path
         * React to the path events first, setting the corresponding beed.
         * Then do a recalculate.
         */
        Boolean oldValue = get();
        PathEvent<_BeanBeed_> beedPathEvent = (PathEvent<_BeanBeed_>)events.get($beedPath);
        if (beedPathEvent != null) {
          setBeed(beedPathEvent.getNewValue());
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


  /*<property name="beed">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends _BeanBeed_> getBeedPath() {
    return $beedPath;
  }

  /**
   * The old beed path is removed as update source.
   * The new beed path is added as update source.
   * The beed is replaced by the new beed: see {@link #setBeed(BeanBeed)}.
   */
  public final void setBeedPath(Path<? extends _BeanBeed_> beedPath) {
    if ($beedPath != null) {
      $dependent.removeUpdateSource($beedPath);
    }
    $beedPath = beedPath;
    if ($beedPath != null) {
      $dependent.addUpdateSource($beedPath);
      setBeed($beedPath.get());
    }
    else {
      setBeed(null);
    }
  }

  private Path<? extends _BeanBeed_> $beedPath;

  /**
   * @basic
   */
  protected final _BeanBeed_ getBeed() {
    return $beed;
  }

  /**
   * @post getBeed() == beed;
   */
  private final void setBeed(_BeanBeed_ beed) {
    Boolean oldValue = get();
    if ($beed != null) {
      $dependent.removeUpdateSource($beed);
    }
    $beed = beed;
    recalculate();
    if ($beed != null) {
      $dependent.addUpdateSource($beed);
    }
    if (! MathUtil.equalValue(oldValue, get())) {
      updateDependents(createNewEvent(oldValue, get(), null));
    }
  }

  private _BeanBeed_ $beed;

  /*</property>*/

  protected final BooleanEvent createNewEvent(Boolean oldValue, Boolean newValue, Edit<?> edit) {
    return new BooleanEvent(this, oldValue, newValue, edit);
  }

  public final boolean isEffective() {
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

  protected void setValue(boolean value) {
    $value = value;
  }

  private boolean $value;

  protected abstract void recalculate();

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
    sb.append(indent(level + 1) + "beed:\n");
    if (getBeed() == null) {
      sb.append(indent(level + 2) + "null");
    }
    else {
      getBeed().toString(sb, level + 2);
    }
  }

}

