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

import java.util.Map;

import org.beedra_II.AbstractDependentBeed;
import org.beedra_II.Event;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.path.AbstractDependentPath;
import org.beedra_II.path.Path;
import org.beedra_II.path.PathEvent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * Beed that expresses whether two {@link BeanBeed bean beeds} are equal or not.
 *
 * @invar  get() == getLeftArg() == getRightArg();
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanEqualBeanBeedsBeed<_BeanBeed_ extends BeanBeed>
    extends AbstractDependentBeed<BooleanEvent>
    implements BooleanBeed {

  /**
   * @post  getLeftArg() == null;
   * @post  getRightArg() == null;
   * @post  get() == true;
   */
  public BooleanEqualBeanBeedsBeed() {
    $value = true;
  }

  @Override
  protected BooleanEvent filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
    /* Events are from:
     * - the left operand,
     * - the left operand path,
     * - the right operand or
     * - the right operand path
     * React to the path events first, setting the corresponding operand.
     * Then do a recalculate.
     */
    Boolean oldValue = get();
    PathEvent<_BeanBeed_> leftOperandPathEvent = (PathEvent<_BeanBeed_>)events.get($leftOperandPath);
    if (leftOperandPathEvent != null) {
      setLeftArg(leftOperandPathEvent.getNewValue());
    }
    PathEvent<_BeanBeed_> rightOperandPathEvent = (PathEvent<_BeanBeed_>)events.get($rightOperandPath);
    if (rightOperandPathEvent != null) {
      setRightArg(rightOperandPathEvent.getNewValue());
    }
    recalculate();
    if (! MathUtil.equalValue(oldValue, get())) {
      return createNewEvent(oldValue, get(), edit);
    }
    else {
      return null;
    }
  }



  /*<property name="left operand">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends _BeanBeed_> getLeftOperandPath() {
    return $leftOperandPath;
  }

  /**
   * The old left operand path is removed as update source.
   * The new left operand path is added as update source.
   * The left operand is replaced by the new left operand: see {@link #setLeftArg(BeanBeed)}.
   */
  public final void setLeftOperandPath(Path<? extends _BeanBeed_> leftOperandPath) {
    if ($leftOperandPath instanceof AbstractDependentPath) {
      removeUpdateSource($leftOperandPath);
    }
    $leftOperandPath = leftOperandPath;
    if ($leftOperandPath instanceof AbstractDependentPath) {
      addUpdateSource($leftOperandPath);
    }
    if ($leftOperandPath != null) {
      setLeftArg($leftOperandPath.get());
    }
    else {
      setLeftArg(null);
    }
  }

  private Path<? extends _BeanBeed_> $leftOperandPath;

  /**
   * @basic
   */
  protected final _BeanBeed_ getLeftArg() {
    return $leftOperand;
  }

  /**
   * @post getLeftArg() == leftOperand;
   */
  private final void setLeftArg(_BeanBeed_ leftOperand) {
    Boolean oldValue = get();
    $leftOperand = leftOperand;
    recalculate();
    if (! MathUtil.equalValue(oldValue, get())) {
      updateDependents(createNewEvent(oldValue, get(), null));
    }
  }

  private _BeanBeed_ $leftOperand;

  /*</property>*/


  /*<property name="right operand">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends _BeanBeed_> getRightOperandPath() {
    return $rightOperandPath;
  }

  /**
   * The old right operand path is removed as update source.
   * The new right operand path is added as update source.
   * The right operand is replaced by the new right operand: see {@link #setRightArg(BeanBeed)}.
   */
  public final void setRightOperandPath(Path<? extends _BeanBeed_> rightOperandPath) {
    if ($rightOperandPath instanceof AbstractDependentPath) {
      removeUpdateSource($rightOperandPath);
    }
    $rightOperandPath = rightOperandPath;
    if ($rightOperandPath instanceof AbstractDependentPath) {
      addUpdateSource($rightOperandPath);
    }
    if ($rightOperandPath != null) {
      setRightArg($rightOperandPath.get());
    }
    else {
      setRightArg(null);
    }
  }

  private Path<? extends _BeanBeed_> $rightOperandPath;

  /**
   * @basic
   */
  protected final _BeanBeed_ getRightArg() {
    return $rightOperand;
  }

  /**
   * @post getRightArg() == rightOperand;
   */
  private final void setRightArg(_BeanBeed_ rightOperand) {
    Boolean oldValue = get();
    $rightOperand = rightOperand;
    recalculate();
    if (! MathUtil.equalValue(oldValue, get())) {
      updateDependents(createNewEvent(oldValue, get(), null));
    }
  }

  private _BeanBeed_ $rightOperand;

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

  @Override
  public final void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
    sb.append(indent(level + 1) + "operands:\n");
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

