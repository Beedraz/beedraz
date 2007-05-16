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

package org.beedraz.semantics_II.expression.association.set;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.expression.collection.set.AbstractSetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEvent;
import org.beedraz.semantics_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * @invar getOwner() instanceof _One_;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class BidirToManyBeed<_One_ extends BeanBeed,
                             _Many_ extends BeanBeed>
    extends AbstractSetBeed<_Many_, SetEvent<_Many_>>
    implements SetBeed<_Many_, SetEvent<_Many_>> {

  public BidirToManyBeed(_One_ bean) {
    super();
    $bean = bean;
    $bean.registerAggregateElement(this);
  }

  private _One_ $bean;

  @SuppressWarnings("unchecked")
  public final _One_ getOwner() {
    return $bean;
  }

  /**
   * @default  true;
   */
  public boolean isAcceptable(_Many_ goal) {
    return true;
  }

  public final Set<_Many_> get() {
    return Collections.unmodifiableSet($many);
  }

  /**
   * @pre  many != null;
   * @pre  !get().contains(many);
   * @post get().contains(many);
   */
  void add(_Many_ many) {
    assert many != null;
    assert !get().contains(many);
    $many.add(many);
  }

  /**
   * @pre  get().contains(many);
   * @post !get().contains(many);
   */
  void remove(_Many_ many) {
    assert get().contains(many);
    $many.remove(many);
  }

  private final Set<_Many_> $many = new HashSet<_Many_>();

  @Override
  protected String otherToStringInformation() {
    return "#: " + get().size();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "elements:\n");
    Iterator<_Many_> iter = $many.iterator();
    while (iter.hasNext()) {
      _Many_ element = iter.next();
      element.toString(sb, level + 2);
    }
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

