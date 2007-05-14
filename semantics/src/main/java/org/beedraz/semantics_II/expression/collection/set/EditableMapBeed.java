/*<license>
Copyright 2007 - $Date: 2007/04/23 16:00:23 $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.collection.set;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * {@link MapBeed} whose value can be changed directly by the user.
 *
 * @author Nele Smeets
 * @author Peopleware n.v.
 */
@CvsInfo(revision = "$Revision: 1.1 $",
         date     = "$Date: 2007/04/23 16:00:23 $",
         state    = "$State: Exp $",
         tag      = "$Name:  $")
public class EditableMapBeed<_Key_, _Value_>
    extends AbstractBeed<MapEvent<_Key_, _Value_>>
    implements MapBeed<_Key_, _Value_, MapEvent<_Key_, _Value_>> {

  public static int countEditableMapBeed = 0;
//  public static Set<EditableMapBeed> instancesEditableMapBeed = new HashSet<EditableMapBeed>();


  /**
   * @post  keySet().isEmpty();
   */
  public EditableMapBeed() {
    super();
    countEditableMapBeed++;
//    instancesEditableMapBeed.add(this);
  }

  /**
   * @basic
   */
  public final _Value_ get(_Key_ key) {
    return $map.get(key);
  }

  /**
   * @basic
   */
  public final Set<_Key_> keySet() {
    return $map.keySet();
  }

  /**
   * @pre key != null;
   */
  final void put(_Key_ key, _Value_ value) {
    assert key != null;
    $map.put(key, value);
  }

  /**
   * @pre $map.keySet().contains(key);
   */
  final void remove(_Key_ key) {
    assert $map.keySet().contains(key);
    $map.remove(key);
  }

  private Map<_Key_, _Value_> $map = new HashMap<_Key_, _Value_>();

  /**
   * Remark-protected: This method is used in {@link MapEdit#isAcceptable()}.
   *
   * @default  true;
   */
  public boolean isAcceptable(Map<_Key_, _Value_> elementsToAdd, Map<_Key_, _Value_> elementsToRemove) {
    return true;
  }

  @Override
  protected String otherToStringInformation() {
    return "hashCode: " + hashCode() +
           "; #: " + $map.keySet().size();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "elements:\n");
    Iterator<_Key_> iter = $map.keySet().iterator();
    while (iter.hasNext()) {
      _Key_ key = iter.next();
      sb.append(indent(level + 2) +
          "key: " + key.toString() + ", value: " + $map.get(key).toString() + "\n");
    }
  }

  void packageUpdateDependents(MapEvent<_Key_, _Value_> event) {
    updateDependents(event);
  }

  public final int getMaximumRootUpdateSourceDistance() {
    return 0;
  }

  public final Set<? extends UpdateSource> getUpdateSources() {
    return Collections.emptySet();
  }

  public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
    return Collections.emptySet();
  }

}

