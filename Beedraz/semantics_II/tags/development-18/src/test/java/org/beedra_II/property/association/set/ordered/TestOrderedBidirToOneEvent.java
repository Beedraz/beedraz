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


import static org.junit.Assert.assertEquals;

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.association.set.ordered.EditableOrderedBidirToOneBeed;
import org.beedra_II.property.association.set.ordered.OrderedBidirToManyBeed;
import org.beedra_II.property.association.set.ordered.OrderedBidirToOneEdit;
import org.beedra_II.property.association.set.ordered.OrderedBidirToOneEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestOrderedBidirToOneEvent {

  public class OneBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class ManyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

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

}