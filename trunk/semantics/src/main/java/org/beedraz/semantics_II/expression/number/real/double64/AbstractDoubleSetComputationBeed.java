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

package org.beedraz.semantics_II.expression.number.real.double64;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MathUtil.castToBigDecimal;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.AbstractDependentExpressionBeed;
import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEvent;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * A beed that does a computation on a given set of beeds of type {@link RealBeed}.
 *
 * @invar getSourcePath() != null
 *          ? getSource() == getSourcePath().get()
 *          : getSource() == null;
 * @invar getSource() == null ==> getDouble() == null;
 * @invar getSource() != null ==>
 *        (exists DoubleBeed db; getSource().get().contains(db); db.getDouble() == null)
 *            ==> getDouble() == null;
 *        If the value of one of the beeds in the given set is null,
 *        then the value of this beed is null.
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() != null;
 *        If the values of all beeds in the given set are effective,
 *        then the value of this beed is effective.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractDoubleSetComputationBeed
    extends AbstractDependentExpressionBeed<ActualDoubleEvent>
    implements DoubleBeed {

  /**
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractDoubleSetComputationBeed(AggregateBeed owner) {
    super(owner);
  }

  public final boolean isEffective() {
    return $effective;
  }

  protected void assignEffective(boolean effective) {
    $effective = effective;
  }

  private boolean $effective = false;

  /**
   * Events can come from the {@link #$source} or the {@link #$sourcePath}.
   * When the path changes, change the source to the new value, register the source and the
   * beeds in the source and recalculate.
   * When the source changes, consider the beeds that are removed and remove them as
   * update source; consider the beeds that are added and add them as update source;
   * recalculate.
   * When the value of this beed changes, return an event containing the old and new value.
   */
  @Override
  protected ActualDoubleEvent filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    boolean oldEffective = $effective;
    double oldValue = $value;
    @SuppressWarnings("unchecked")
    PathEvent<SetBeed<RealBeed<?>, ?>> pathEvent =
      (PathEvent<SetBeed<RealBeed<?>, ?>>)events.get($sourcePath); // we know this cast is correct
    if (pathEvent != null) {
      setSource(pathEvent.getNewValue());
    }
    else {
      // if the source changes (elements added and / or removed
      if (events.keySet().contains($source)) {
        @SuppressWarnings("unchecked")
        SetEvent<RealBeed<?>> setEvent = (SetEvent<RealBeed<?>>)events.get($source);
        sourceChanged(setEvent);
      }
      // if the value of one of the elements changes
      // we do nothing special here, just a total recalculate
      // IDEA there is room for optimalization here
      // recalculate and notify the listeners if the value has changed
      assert $source != null;
      recalculate($source);
    }
    if ((oldEffective != $effective) || ! MathUtil.equalPrimitiveValue(oldValue, $value)) {
      return new ActualDoubleEvent(
          AbstractDoubleSetComputationBeed.this,
          oldEffective ? oldValue : null, // When oldEffective and $effective are different, the values of
              // oldValue and $value are not necessarily different, while the constructor of the event
              // expects two different values
              // To solve this problem, we return null for the value that corresponds to the boolean
              // value (oldEffective or $effective) that is false. One of the two values will be null,
              // the other one is a double value (or NaN, or Infinity).
          $effective ? $value : null,
          edit);
    }
    else {
      return null;
    }
  }

  /**
   * @post    All RealBeeds that are added to the SetBeed by the given event
   *          become update sources.
   *          (The reason is that the AbstractDoubleSetComputationBeed should be
   *          notified (and then recalculate) when one of the DoubleBeeds
   *          changes.)
   * @post    All RealBeeds that are removed from the SetBeed by the given
   *          event are no longer update sources.
   * @post    getDouble() is recalculated.
   */
  private void sourceChanged(SetEvent<RealBeed<?>> event) {
    /* All RealBeeds that are added to the SetBeed by the given event
     * become update sources
     */
    Set<RealBeed<?>> added = event.getAddedElements();
    for (RealBeed<?> beed : added) {
      addUpdateSource(beed);
    }
    /* All RealBeeds that are removed from the SetBeed by the given event
     * stop being update sources
     */
    Set<RealBeed<?>> removed = event.getRemovedElements();
    for (RealBeed<?> beed : removed) {
      removeUpdateSource(beed);
    }
  }



  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends SetBeed<RealBeed<?>, ?>> getSourcePath() {
    return $sourcePath;
  }

  /**
   * @post  getSourcePath() == sourcePath;
   * @post
   */
  public final void setSourcePath(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    boolean oldEffective = isEffective();
    double oldValue = $value;
    if ($sourcePath != null) {
      removeUpdateSource($sourcePath);
    }
    $sourcePath = sourcePath;
    if ($sourcePath != null) {
      addUpdateSource($sourcePath);
      setSource($sourcePath.get());
    }
    else {
      setSource(null);
    }
    if ((oldEffective != isEffective()) || (oldValue != $value)) {
      updateDependents(
          new ActualDoubleEvent(
              AbstractDoubleSetComputationBeed.this,
              oldEffective ? oldValue : null,
              isEffective() ? $value : null,
              null)); // edit = null
    }
  }

  private Path<? extends SetBeed<RealBeed<?>, ?>> $sourcePath;

  /**
   * @return getSourcePath() == null ? null : getSourcePath().get();
   */
  public final SetBeed<RealBeed<?>, ?> getSource() {
    return $source;
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    getDouble() == the value resulting from the given source
   * @post    The given source is added as update source (if it is effective).
   * @post    The previous source is removed as update source (if it is effective).
   * @post    All beeds in the given source are added as update sources. (The reason
   *          is that this source should be notified (and then recalculate)
   *          when one of the beeds in the given source changes.)
   * @post    All beeds in the previous source are removed as update sources.
   */
  private void setSource(SetBeed<RealBeed<?>, ?> source) {
    if ($source != null) {
      for (RealBeed<?> beed : $source.get()) {
        removeUpdateSource(beed);
      }
      removeUpdateSource($source);
    }
    // set the source
    $source = source;
    if ($source != null) {
      addUpdateSource($source);
      for (RealBeed<?> beed : $source.get()) {
        addUpdateSource(beed);
      }
      recalculate($source);
    }
    else {
      assignEffective(false);
    }
  }

  private SetBeed<RealBeed<?>, ?> $source;

  /*</property>*/



  /*<property name="value*/
  //------------------------------------------------------------------

  public final double getdouble() {
    return $value;
  }

  public final Double getDouble() {
    return $effective ? Double.valueOf(getdouble()) : null;
  }

  public final BigDecimal getBigDecimal() {
    return castToBigDecimal(getDouble());
  }

  /**
   * The value of this beed.
   */
  private double $value;

  /*</property>*/



  /**
   * The value of this beed is recalculated.
   *
   * @pre source != null;
   */
  protected abstract void recalculate(SetBeed<RealBeed<?>, ?> source);


  protected final void assignValue(Double value) {
    $value = value;
  }

  @Override
  protected String otherToStringInformation() {
    return getDouble() + " (# " + getSource().get().size() + ")";
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + getDouble() + "\n");
    sb.append(indent(level + 1) + "number of elements in source:" + getSource().get().size() + "\n");
    getSource().toString(sb, level + 2);
  }

  public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
    sb.append(getOperatorString());
    sb.append("(");
    Iterator<RealBeed<?>> i = getSource().get().iterator();
    while (i.hasNext()) {
      RealBeed<?> argument = i.next();
      sb.append("(");
      if (depth == 1) {
        sb.append(numberFormat.format(argument.getdouble()));
      }
      else {
        argument.toStringDepth(sb, depth - 1, numberFormat);
      }
      sb.append(")");
      if (i.hasNext()) {
        sb.append(",");
      }
    }
    sb.append(")");
  }

  /**
   * The operator of this binary expression.
   */
  public abstract String getOperatorString();

}

