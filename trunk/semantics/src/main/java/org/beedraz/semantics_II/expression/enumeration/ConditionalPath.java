/*<license>
Copyright 2007 - $Date: 2007-05-08 16:22:50 +0200 (Tue, 08 May 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.enumeration;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEvent;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Conditional {@link Path}. The path has a {@link #getConditionBeedPath()} that
 *   references a {@link BooleanBeed}.
 *   When the {@link #getCondition() condition} is <code>true</code>, then the
 *   {@code ConditionalPath} returns the beed that is referenced by
 *   {@link #getFirstPath()}.
 *   When the {@link #getCondition() condition} is <code>false</code>, then the
 *   {@code ConditionalPath} returns the beed that is referenced by
 *   {@link #getSecondPath()}.
 *   When the {@link #getCondition() condition} is <code>null</code>, then the
 *   {@code ConditionalPath} returns <code>null</code>.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @remark  This class is very similar to {@link EnumSwitchPath}.
 *
 * @invar   getConditionBeedPath() != null
 *            ? getConditionBeed() == getConditionBeedPath().get()
 *            : true;
 * @invar   getConditionBeed() != null
 *            ? getCondition() == getConditionBeed().get()
 *            : true;
 *
 * @invar   getCondition() == true
 *            ? getSelectedPath() == getFirstPath()
 *            : true;
 * @invar   getCondition() == false
 *            ? getSelectedPath() == getSecondPath()
 *            : true;
 * @invar   getCondition() == null
 *            ? getSelectedPath() == null
 *            : true;
 *
 * @result  getSelectedPath() != null
 *            ? get() == getSelectedPath().get()
 *            : get() == null;
 */
@Copyright("2007 - $Date: 2007-05-08 16:22:50 +0200 (Tue, 08 May 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 853 $",
         date     = "$Date: 2007-05-08 16:22:50 +0200 (Tue, 08 May 2007) $")
public class ConditionalPath<_SelectedBeed_ extends Beed<?>>
    extends AbstractDependentPath<_SelectedBeed_> {


  @Override
  protected final PathEvent<_SelectedBeed_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    // Events are from the condition beed path, the condition beed, or the selected path.
    _SelectedBeed_ oldValue = $selectedBeed;
    PathEvent<BooleanBeed> conditionBeedPathEvent = (PathEvent<BooleanBeed>)events.get($conditionBeedPath);
    if (conditionBeedPathEvent != null) {
      setConditionBeed(conditionBeedPathEvent.getNewValue());
      /* we are now no longer interested in condition beed events or selected path events: everything will
       * be updated in any case
       */
    }
    else {
      BooleanEvent conditionBeedEvent = (BooleanEvent)events.get($conditionBeed);
      if (conditionBeedEvent != null) {
        setCondition(conditionBeedEvent.getNewValue());
        /* we are now no longer interested in selected path events: everything will
         * be updated in any case
         */
      }
      else {
        PathEvent<_SelectedBeed_> selectedPathEvent = (PathEvent<_SelectedBeed_>)events.get($selectedPath);
        if (selectedPathEvent != null) {
          $selectedBeed = selectedPathEvent.getNewValue();
        }
      }
    }
    if (oldValue != $selectedBeed) {
      return new PathEvent<_SelectedBeed_>(this, oldValue, $selectedBeed, edit);
    }
    else {
      return null;
    }
  }


  /*<property name="condition">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends BooleanBeed> getConditionBeedPath() {
    return $conditionBeedPath;
  }

  public final void setConditionBeedPath(Path<? extends BooleanBeed> conditionBeedPath) {
    if ($conditionBeedPath instanceof AbstractDependentPath) {
      removeUpdateSource($conditionBeedPath);
    }
    $conditionBeedPath = conditionBeedPath;
    if ($conditionBeedPath != null) {
      setConditionBeed($conditionBeedPath.get());
      if ($conditionBeedPath instanceof AbstractDependentPath) {
        addUpdateSource($conditionBeedPath);
      }
    }
    else {
      setConditionBeed(null);
    }
  }

  private Path<? extends BooleanBeed> $conditionBeedPath;

  /**
   * @basic
   */
  public final BooleanBeed getConditionBeed() {
    return $conditionBeed;
  }

  /**
   * @post getConditionBeed() == conditionBeed;
   */
  private final void setConditionBeed(BooleanBeed conditionBeed) {
    if ($conditionBeed != null) {
      removeUpdateSource($conditionBeed);
    }
    $conditionBeed = conditionBeed;
    if ($conditionBeed != null) {
      setCondition($conditionBeed.get());
      addUpdateSource($conditionBeed);
    }
    else {
      setCondition(null);
    }
  }

  private BooleanBeed $conditionBeed;

  /**
   * @basic
   */
  public final Boolean getCondition() {
    return $condition;
  }

  /**
   * @post getCondition() == condition;
   */
  private final void setCondition(Boolean condition) {
    // set the condition
    $condition = condition;
    // set the selected path to the corresponding path
    Path<? extends _SelectedBeed_> selectedPath;
    if ($condition == Boolean.TRUE) {
      selectedPath = getFirstPath();
    }
    else if ($condition == Boolean.FALSE) {
      selectedPath = getSecondPath();
    }
    else {
      assert $condition == null;
      selectedPath = null;
    }
    setSelectedPath(selectedPath);
  }

  private Boolean $condition;

  /*</property>*/


  /*<property name="firstPath">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends _SelectedBeed_> getFirstPath() {
    return $firstPath;
  }

  /**
   * @post getFirstPath() == firstPath;
   */
  public final void setFirstPath(Path<? extends _SelectedBeed_> firstPath) {
    Path<? extends _SelectedBeed_> oldPath = $firstPath;
    $firstPath = firstPath;
    if ($condition == Boolean.TRUE) {
      assert oldPath == $selectedPath;
      setSelectedPath(firstPath);
    }
  }

  private Path<? extends _SelectedBeed_> $firstPath;

  /*</property>*/


  /*<property name="secondPath">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends _SelectedBeed_> getSecondPath() {
    return $secondPath;
  }

  /**
   * @post getSecondPath() == secondPath;
   */
  public final void setSecondPath(Path<? extends _SelectedBeed_> secondPath) {
    Path<? extends _SelectedBeed_> oldPath = $secondPath;
    $secondPath = secondPath;
    if ($condition == Boolean.FALSE) {
      assert oldPath == $selectedPath;
      setSelectedPath(secondPath);
    }
  }

  private Path<? extends _SelectedBeed_> $secondPath;

  /*</property>*/


  /*<property name="selected path">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends _SelectedBeed_> getSelectedPath() {
    return $selectedPath;
  }

  private void setSelectedPath(Path<? extends _SelectedBeed_> selectedPath) {
    _SelectedBeed_ oldValue = $selectedBeed;
    if ($selectedPath != null) {
      removeUpdateSource($selectedPath);
    }
    $selectedPath = selectedPath;
    if ($selectedPath != null) {
      $selectedBeed = $selectedPath.get();
      addUpdateSource($selectedPath);
    }
    else {
      $selectedBeed = null;
    }
    if (oldValue != $selectedBeed) {
      PathEvent<_SelectedBeed_> event = new PathEvent<_SelectedBeed_>(this, oldValue, $selectedBeed, null);
      updateDependents(event);
    }
  }

  private Path<? extends _SelectedBeed_> $selectedPath;

  /*</property>*/



  /*<property name="selected beed">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final _SelectedBeed_ get() {
    return $selectedBeed;
  }

  private _SelectedBeed_ $selectedBeed;

  /*</property>*/

}

