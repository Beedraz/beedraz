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

package org.beedra_II.property.association.sorted;


import static org.junit.Assert.assertEquals;

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSortedBidirToOneEvent {

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
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> source =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many);
    // old and new value
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> oldValue =
      new SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>(one);
    SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> newValue =
      new SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>(one);
    // edit
    EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> target =
      new EditableSortedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(many);
    SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit = new SortedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>(target);
    edit.perform();
    // test constructor
    SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> SortedBidirToOneEvent =
      new SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>(source, oldValue, newValue, edit);
    assertEquals(SortedBidirToOneEvent.getSource(), source);
    assertEquals(SortedBidirToOneEvent.getOldValue(), oldValue);
    assertEquals(SortedBidirToOneEvent.getNewValue(), newValue);
    assertEquals(SortedBidirToOneEvent.getEdit(), edit);
    assertEquals(SortedBidirToOneEvent.getEditState(), edit.getState());
    // old and new value
    oldValue = null;
    newValue = null;
    edit = null;
    // test constructor
    SortedBidirToOneEvent = new SortedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>(source, oldValue, newValue, edit);
    assertEquals(SortedBidirToOneEvent.getSource(), source);
    assertEquals(SortedBidirToOneEvent.getOldValue(), null);
    assertEquals(SortedBidirToOneEvent.getNewValue(), null);
    assertEquals(SortedBidirToOneEvent.getEdit(), edit);
    assertEquals(SortedBidirToOneEvent.getEditState(), null);
  }

}