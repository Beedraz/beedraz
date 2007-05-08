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

package org.beedra_II.property.association.set;


import static org.junit.Assert.assertEquals;

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.association.set.BidirToManyBeed;
import org.beedra_II.property.association.set.BidirToOneEdit;
import org.beedra_II.property.association.set.BidirToOneEvent;
import org.beedra_II.property.association.set.EditableBidirToOneBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBidirToOneEvent {

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
    EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> source =
      new EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many);
    // old and new value
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> oldValue =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>(one);
    BidirToManyBeed<OneBeanBeed, ManyBeanBeed> newValue =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>(one);
    // edit
    EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> target =
      new EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many);
    BidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit = new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>(target);
    edit.perform();
    // test constructor
    BidirToOneEvent<OneBeanBeed, ManyBeanBeed> bidirToOneEvent =
      new BidirToOneEvent<OneBeanBeed, ManyBeanBeed>(source, oldValue, newValue, edit);
    assertEquals(bidirToOneEvent.getSource(), source);
    assertEquals(bidirToOneEvent.getOldValue(), oldValue);
    assertEquals(bidirToOneEvent.getNewValue(), newValue);
    assertEquals(bidirToOneEvent.getEdit(), edit);
    assertEquals(bidirToOneEvent.getEditState(), edit.getState());
    // old and new value
    oldValue = null;
    newValue = null;
    edit = null;
    // test constructor
    bidirToOneEvent = new BidirToOneEvent<OneBeanBeed, ManyBeanBeed>(source, oldValue, newValue, edit);
    assertEquals(bidirToOneEvent.getSource(), source);
    assertEquals(bidirToOneEvent.getOldValue(), null);
    assertEquals(bidirToOneEvent.getNewValue(), null);
    assertEquals(bidirToOneEvent.getEdit(), edit);
    assertEquals(bidirToOneEvent.getEditState(), null);
  }

}