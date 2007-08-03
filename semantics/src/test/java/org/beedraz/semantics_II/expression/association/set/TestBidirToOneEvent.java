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


import static org.junit.Assert.assertEquals;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.TestActualOldNewEvent;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class TestBidirToOneEvent
    extends TestActualOldNewEvent<BidirToManyBeed<OneBeanBeed, ManyBeanBeed>,
                                  EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>,
                                  BidirToOneEvent<OneBeanBeed, ManyBeanBeed>> {

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

  @Override
  protected BidirToOneEvent<OneBeanBeed, ManyBeanBeed> createActualOldNewEvent(EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> source, BidirToManyBeed<OneBeanBeed, ManyBeanBeed> old, BidirToManyBeed<OneBeanBeed, ManyBeanBeed> newV) {
    return new BidirToOneEvent<OneBeanBeed, ManyBeanBeed>(source, old, newV, null);
  }

  @Override
  protected EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> createSource() throws IllegalEditException {
    return new EditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>(new ManyBeanBeed());
  }

  @Override
  protected BidirToManyBeed<OneBeanBeed, ManyBeanBeed> oldValue() {
    return new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>(new OneBeanBeed());
  }

  @Override
  protected BidirToManyBeed<OneBeanBeed, ManyBeanBeed> middleValue() {
    return new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>(new OneBeanBeed());
  }

  @Override
  protected BidirToManyBeed<OneBeanBeed, ManyBeanBeed> newValue() {
    return new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>(new OneBeanBeed());
  }

  @Override
  protected BidirToManyBeed<OneBeanBeed, ManyBeanBeed> otherValue(BidirToManyBeed<OneBeanBeed, ManyBeanBeed> value) {
    return new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>(new OneBeanBeed());
  }

}

class OneBeanBeed extends AbstractBeanBeed {
  // NOP
}

class ManyBeanBeed extends AbstractBeanBeed {
  // NOP
}
