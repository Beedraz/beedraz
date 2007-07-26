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

import java.util.HashMap;
import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Conditional {@link Path}. The path has a {@link #getKeyBeedPath()} that
 *   references a {@link EnumBeed}. Each possible value of the {@link #getKey() key}
 *   can be mapped to a {@link Path} of type {@code _SelectedBeed_}.
 *   The {@code EnumSwitchPath} returns the {@link #get() selected beed} that
 *   is returned by the {@link #getSelectedPath() case path that
 *   corresponds to the current value} of the {@link #getKeyBeed()}.
 *   When there is no corresponding {@link #getCasePaths() case path}, then
 *   {@code null} is returned.</p>
 *
 * @author  Jan Dockx
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar   getKeyBeedPath() != null
 *            ? getKeyBeed() == getKeyBeedPath().get()
 *            : true;
 * @invar   getKeyBeed() != null
 *            ? getKey() == getKeyBeed().get()
 *            : true;
 */
@Copyright("2007 - $Date: 2007-05-08 16:22:50 +0200 (Tue, 08 May 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 853 $",
         date     = "$Date: 2007-05-08 16:22:50 +0200 (Tue, 08 May 2007) $")
public class EnumSwitchPath<_Enum_ extends Enum<_Enum_>,
                            _SelectedBeed_ extends Beed<?>>
    extends AbstractDependentPath<_SelectedBeed_> {



  @Override
  protected final PathEvent<_SelectedBeed_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    /* Events are from the key beed path, the key beed, or the selected path.
     */
    _SelectedBeed_ oldValue = $selectedBeed;
    PathEvent<EnumBeed<_Enum_>> keyBeedPathEvent = (PathEvent<EnumBeed<_Enum_>>)events.get($keyBeedPath);
    if (keyBeedPathEvent != null) {
      setKeyBeed(keyBeedPathEvent.getNewValue());
      /* we are now no longer interested in key beed events or selected path events: everything will
       * be updated in any case
       */
    }
    else {
      EnumEvent<_Enum_> keyBeedEvent = (EnumEvent<_Enum_>)events.get($keyBeed);
      if (keyBeedEvent != null) {
        setKey(keyBeedEvent.getNewValue());
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


  /*<property name="key">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends EnumBeed<_Enum_>> getKeyBeedPath() {
    return $keyBeedPath;
  }

  public final void setKeyBeedPath(Path<? extends EnumBeed<_Enum_>> keyBeedPath) {
    if ($keyBeedPath instanceof AbstractDependentPath) {
      removeUpdateSource($keyBeedPath);
    }
    $keyBeedPath = keyBeedPath;
    if ($keyBeedPath != null) {
      setKeyBeed($keyBeedPath.get());
      if ($keyBeedPath instanceof AbstractDependentPath) {
        addUpdateSource($keyBeedPath);
      }
    }
    else {
      setKeyBeed(null);
    }
  }

  private Path<? extends EnumBeed<_Enum_>> $keyBeedPath;

  /**
   * @basic
   */
  public final EnumBeed<_Enum_> getKeyBeed() {
    return $keyBeed;
  }

  /**
   * @post getKeyBeed() == keyBeed;
   */
  private final void setKeyBeed(EnumBeed<_Enum_> keyBeed) {
    if ($keyBeed != null) {
      removeUpdateSource($keyBeed);
    }
    $keyBeed = keyBeed;
    if ($keyBeed != null) {
      setKey($keyBeed.get());
      addUpdateSource($keyBeed);
    }
    else {
      setKey(null);
    }
  }

  private EnumBeed<_Enum_> $keyBeed;

  /**
   * @basic
   */
  public final _Enum_ getKey() {
    return $key;
  }

  /**
   * @post getKey() == key;
   */
  private final void setKey(_Enum_ key) {
    $key = key;
    if ($key != null) {
      setSelectedPath($casePaths.get($key));
    }
    else {
      setSelectedPath(null);
    }
  }

  private _Enum_ $key;

  /*</property>*/



  /*<property name="case paths">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Map<_Enum_, Path<_SelectedBeed_>> getCasePaths() {
    return new HashMap<_Enum_, Path<_SelectedBeed_>>($casePaths);
  }

  /**
   * @pre key != null;
   * @post getCasePaths().get(key) == path;
   */
  public final void addCasePath(_Enum_ key, Path<_SelectedBeed_> path) {
    assert key != null;
    Path<_SelectedBeed_> oldCasePath = $casePaths.put(key, path);
    if (key == $key) {
      assert oldCasePath == $selectedPath;
      setSelectedPath(path);
    }
  }

  /**
   * @pre key != null;
   * @post getCasePaths().get(key) == null;
   */
  public final void removeCasePath(_Enum_ key) {
    assert key != null;
    Path<_SelectedBeed_> oldCasePath = $casePaths.remove(key);
    if (key == $key) {
      assert oldCasePath == $selectedPath;
      setSelectedPath(null);
    }
  }

  private final Map<_Enum_, Path<_SelectedBeed_>> $casePaths =
      new HashMap<_Enum_, Path<_SelectedBeed_>>();

  /*</property>*/



  /*<property name="selected path">*/
  //-----------------------------------------------------------------

  /**
   * @return getKey() != null
   *           ? getCasePaths().get(getKey())
   *           : null;
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
   * @return getSelectedPath() != null
   *           ? getSelectedPath().get()
   *           : null;
   */
  public final _SelectedBeed_ get() {
    return $selectedBeed;
  }

  private _SelectedBeed_ $selectedBeed;

  /*</property>*/

}

