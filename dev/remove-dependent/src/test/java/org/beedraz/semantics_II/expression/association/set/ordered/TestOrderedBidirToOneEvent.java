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


import static org.junit.Assert.assertEquals;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.TestActualOldNewEvent;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestOrderedBidirToOneEvent
    extends TestActualOldNewEvent<OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>,
                                  EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>,
                                  OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>>{

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  @Test
  public void constructor() throws EditStateException, IllegalEditException {
    OneBeanBeed one = new OneBeanBeed();
    ManyBeanBeed many = new ManyBeanBeed();
    // source
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> source =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many);
    // old and new value
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> oldValue =
      new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>(one);
    OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> newValue =
      new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>(one);
    // edit
    EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> target =
      new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many);
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit = new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(target);
    edit.perform();
    // test constructor
    OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> OrderedBidirToOneEvent =
      new OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>(source, oldValue, newValue, edit);
    assertEquals(OrderedBidirToOneEvent.getSource(), source);
    assertEquals(OrderedBidirToOneEvent.getOldValue(), oldValue);
    assertEquals(OrderedBidirToOneEvent.getNewValue(), newValue);
    assertEquals(OrderedBidirToOneEvent.getEdit(), edit);
    assertEquals(OrderedBidirToOneEvent.getEditState(), edit.getState());
    // old and new value
    oldValue = null;
    newValue = null;
    edit = null;
    // test constructor
    OrderedBidirToOneEvent = new OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>(source, oldValue, newValue, edit);
    assertEquals(OrderedBidirToOneEvent.getSource(), source);
    assertEquals(OrderedBidirToOneEvent.getOldValue(), null);
    assertEquals(OrderedBidirToOneEvent.getNewValue(), null);
    assertEquals(OrderedBidirToOneEvent.getEdit(), edit);
    assertEquals(OrderedBidirToOneEvent.getEditState(), null);
  }

  @Override
  protected OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> createActualOldNewEvent(EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> source, OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> old, OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> newV) {
    return new OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>(source, old, newV, null);
  }

  @Override
  protected EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> createSource() throws IllegalEditException {
    return new EditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
  }

  @Override
  protected OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> oldValue() {
    return new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>(new OneBeanBeed());
  }

  @Override
  protected OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> middleValue() {
    return new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>(new OneBeanBeed());
  }

  @Override
  protected OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> newValue() {
    return new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>(new OneBeanBeed());
  }

  @Override
  protected OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> otherValue(OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> value) {
    return new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>(new OneBeanBeed());
  }

}


class OneBeanBeed extends AbstractBeanBeed {
  // NOP
}

class ManyBeanBeed extends AbstractBeanBeed {
  // NOP
}