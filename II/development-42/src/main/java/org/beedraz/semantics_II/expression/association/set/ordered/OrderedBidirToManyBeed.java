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

package org.beedraz.semantics_II.expression.association.set.ordered;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.expression.collection.set.AbstractSetBeed;
import org.beedraz.semantics_II.expression.collection.set.ordered.OrderedSetBeed;
import org.beedraz.semantics_II.expression.collection.set.ordered.OrderedSetEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.collection_I.CollectionUtil;
import org.ppeew.collection_I.LinkedListOrderedSet;
import org.ppeew.collection_I.OrderedSet;


/**
 * {@link OrderedSetBeed} that represents the 'many' side of a bidirectional association.
 * We assume that the list cannot contain any duplicates.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getOwner() instanceof _One_;
 * @invar  getOne() != null;
 * @invar  (forAll _Many_ m1; get().contains(m1);
 *            (forAll _Many_ m2; get().contains(m2);
 *               m1 != m2));
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class OrderedBidirToManyBeed<_One_ extends BeanBeed,
                                   _Many_ extends BeanBeed>
    extends AbstractSetBeed<_Many_, OrderedSetEvent<_Many_>>
    implements OrderedSetBeed<_Many_, OrderedSetEvent<_Many_>> {

  /**
   * @pre one != null;
   * @post getOne() == one;
   * @post one.registerAggregateElement(this);
   */
  public OrderedBidirToManyBeed(_One_ one) {
    super(one);
    assert one != null;
    $one = one;
  }

  /**
   * @invar $one != null;
   */
  private final _One_ $one;

  /**
   * @basic
   * 
   * @mudo rename to getOne();
   */
  public final _One_ getOwner() {
    return $one;
  }

  /**
   * @default  !get().contains(goal) &&
   *           (  position == null ||
   *              (  position >= 0 &&
   *                 position <= get().size()));
   */
  public boolean isAcceptable(_Many_ goal, Integer position) {
    return !get().contains(goal) &&
           (  position == null ||
              (  position >= 0 &&
                 position <= get().size()));
  }

  public final OrderedSet<_Many_> get() {
    return CollectionUtil.unmodifiableOrderedSet($many);
  }

  /**
   * Returns the index in this list of the specified element,
   * or -1 if this list does not contain this element.
   * Since there are no duplicates in the list, this index
   * is unique.
   */
  public int indexOf(_Many_ many) {
    return $many.indexOf(many);
  }

  /**
   * If {@code many} is already in the set, it is moved to the new position.
   *
   * @pre  position >= 0;
   * @pre  position <= $many.size();
   * @pre  many != null;
   * @post get().contains(many);
   */
  void add(int position, _Many_ many) {
    assert position >= 0;
    assert position <= $many.size();
    assert many != null;
    $many.add(position, many);
  }

  /**
   * @pre  get().contains(many);
   * @post !get().contains(many);
   */
  void remove(_Many_ many) {
    assert get().contains(many);
    $many.remove(many);
  }

  private final OrderedSet<_Many_> $many = new LinkedListOrderedSet<_Many_>();

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

  public final Set<? extends Beed<?>> getUpdateSources() {
    return Collections.emptySet();
  }

  public final Set<? extends Beed<?>> getUpdateSourcesTransitiveClosure() {
    return Collections.emptySet();
  }

}

