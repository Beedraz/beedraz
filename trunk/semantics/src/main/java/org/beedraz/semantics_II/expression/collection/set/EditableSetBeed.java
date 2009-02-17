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

package org.beedraz.semantics_II.expression.collection.set;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppeew.collection_I.CollectionUtil.intersection;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * {@link org.beedraz.semantics_II.expression.ExpressionBeed} whose value can be changed directly
 * by the user.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class EditableSetBeed<_Element_>
    extends AbstractSetBeed<_Element_, SetEvent<_Element_>> {

  /**
   * @pre owner != null;
   * @post get().isEmpty();
   * @post owner.registerAggregateElement(this);
   */
  public EditableSetBeed(AggregateBeed owner) {
    super(owner);
    assert owner != null;
  }

  /**
   * @basic
   */
  public final Set<_Element_> get() {
    return Collections.unmodifiableSet($set);
  }

  /**
   * @pre elements != null;
   * @pre ComparisonUtil.intersection(get(), elements).isEmpty();
   */
  final void addElements(Set<_Element_> elements) {
    assert elements != null;
    assert intersection(get(), elements).isEmpty();
    $set.addAll(elements);
  }

  /**
   * @pre elements != null;
   * @pre get().containsAll(elements);
   */
  final void removeElements(Set<_Element_> elements) {
    assert elements != null;
    assert get().containsAll(elements);
    $set.removeAll(elements);
  }

  private Set<_Element_> $set = new HashSet<_Element_>();

  public boolean isAcceptable(Set<_Element_> elementsToAdd, Set<_Element_> elementsToRemove) {
    return true;
  }

  @Override
  protected String otherToStringInformation() {
    return "hashCode: " + hashCode() +
           "; #: " + get().size();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "elements:\n");
    Iterator<_Element_> iter = get().iterator();
    while (iter.hasNext()) {
      _Element_ element = iter.next();
      sb.append(indent(level + 2) + element.toString() + "\n");
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

