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


import static org.beedra.util_I.MultiLineToStringUtil.indent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.set.ordered.OrderedSetBeed;
import org.beedra_II.property.set.ordered.OrderedSetEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


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
    extends AbstractPropertyBeed<OrderedSetEvent<_Many_>>
    implements OrderedSetBeed<_Many_> {

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

  public final List<_Many_> get() {
    return Collections.unmodifiableList($many);
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

  void add(int position, _Many_ many) {
    assert position >= 0;
    assert position <= $many.size();
    assert many != null;
    $many.add(position, many);
  }

  void remove(_Many_ many) {
    $many.remove(many);
  }

  void fireChangeEvent(List<_Many_> oldValue,
                       List<_Many_> newValue,
                       OrderedBidirToOneEdit<_One_, _Many_> edit) {
    fireChangeEvent(new OrderedSetEvent<_Many_>(this, oldValue, newValue, edit));
  }

  private final List<_Many_> $many = new ArrayList<_Many_>();

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
    return new OrderedSetEvent<_Many_>(this, null, get(), null);
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

}

