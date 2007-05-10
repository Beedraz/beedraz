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

package org.beedraz.semantics_II.expression.bool;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Map;

import org.beedraz.semantics_II.AbstractDependentBeed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.beedraz.semantics_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * Abstract implementation of binary expression beeds, that represent a boolean value derived
 * from 2 operands of type {@link BeanBeed}.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractBeanArgBooleanUnaryExpressionBeed<_BeanBeed_ extends BeanBeed>
    extends AbstractDependentBeed<BooleanEvent>
    implements BooleanBeed {

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
    if ($beedPath instanceof AbstractDependentPath) {
      removeUpdateSource($beedPath);
    }
    $beedPath = beedPath;
    if ($beedPath instanceof AbstractDependentPath) {
      addUpdateSource($beedPath);
    }
    if ($beedPath != null) {
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
    $beed = beed;
    recalculate();
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

