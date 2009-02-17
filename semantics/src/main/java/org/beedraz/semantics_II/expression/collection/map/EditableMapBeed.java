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

package org.beedraz.semantics_II.expression.collection.map;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.vcs.CvsInfo;


/**
 * {@link MapBeed} whose value can be changed directly by the user.
 *
 * @author Nele Smeets
 * @author Peopleware n.v.
 *
 * @mudo move to collection package; map is not a set
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State: Exp $",
         tag      = "$Name:  $")
public class EditableMapBeed<_Key_, _Value_>
    extends AbstractBeed<MapEvent<_Key_, _Value_>>
    implements MapBeed<_Key_, _Value_, MapEvent<_Key_, _Value_>> {


  /**
   * @pre owner != null;
   * @post  keySet().isEmpty();
   * @post owner.registerAggregateElement(this);
   */
  public EditableMapBeed(AggregateBeed owner) {
    assert owner != null;
    owner.registerAggregateElement(this);
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

  public final int getMaximumRootUpdateSourceDistance() {
    return 0;
  }

  public final Set<? extends Beed<?>> getUpdateSources() {
    return Collections.emptySet();
  }

  public final Set<? extends Beed<?>> getUpdateSourcesTransitiveClosure() {
    return Collections.emptySet();
  }

}

