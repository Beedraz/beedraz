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

package org.beedra_II.property.association.set.ordered;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Iterator;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.property.collection.set.AbstractSetBeed;
import org.beedra_II.property.collection.set.ordered.ActualOrderedSetEvent;
import org.beedra_II.property.collection.set.ordered.OrderedSetBeed;
import org.beedra_II.property.collection.set.ordered.OrderedSetEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;
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
 * @invar  (forAll _Many_ m1; get().contains(m1);
 *            (forAll _Many_ m2; get().contains(m2);
 *               m1 != m2));
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class OrderedBidirToManyBeed<_One_ extends BeanBeed,
                                   _Many_ extends BeanBeed>
    extends AbstractSetBeed<_Many_, OrderedSetEvent<_Many_>>
    implements OrderedSetBeed<_Many_, OrderedSetEvent<_Many_>> {

  public OrderedBidirToManyBeed(_One_ bean) {
    super(bean);
  }

  @Override
  @SuppressWarnings("unchecked")
  public final _One_ getOwner() {
    return (_One_)super.getOwner();
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

  /**
   * @post  result != null;
   * @post  result.getSource() == this;
   * @post  result.getOldValue() == null;
   * @post  result.getNewValue() == get();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected final OrderedSetEvent<_Many_> createInitialEvent() {
    return new ActualOrderedSetEvent<_Many_>(this, null, get(), null);
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
    Iterator<_Many_> iter = $many.iterator();
    while (iter.hasNext()) {
      _Many_ element = iter.next();
      element.toString(sb, level + 2);
    }
  }

  public final int getMaximumRootUpdateSourceDistance() {
    return 0;
  }

}

