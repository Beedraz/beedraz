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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.Listener;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateEvent;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEvent;
import org.beedraz.semantics_II.expression.number.integer.long64.EditableLongBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.LongEdit;
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
public class TestEditableBidirToOneBeed {

  public class MyEditableBidirToOneBeed<_One_ extends BeanBeed, _Many_ extends BeanBeed>
      extends EditableBidirToOneBeed<_One_, _Many_> {

    public MyEditableBidirToOneBeed(_Many_ owner) {
      super(owner);
    }

    /**
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(BidirToOneEvent<_One_, _Many_> event) {
      updateDependents(event);
    }

  }

  public class OneBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class ManyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class StubEditableBidirToOneBeedListener implements Listener<SetEvent<Integer>> {

    public void beedChanged(SetEvent<Integer> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public SetEvent<Integer> $event;

  }

  @Before
  public void setUp() throws Exception {
    $many = new ManyBeanBeed();
    $editableBidirToOneBeed =
      new MyEditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
    $one = new OneBeanBeed();
    $bidirToManyBeed =
      new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
    $bidirToOneEdit =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    $bidirToOneEdit.perform();
    $bidirToOneEvent =
      new BidirToOneEvent<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed, null, $bidirToManyBeed, $bidirToOneEdit);
    $listener1 = new StubListener<AggregateEvent>();
    $listener2 = new StubListener<AggregateEvent>();
//  $listener3 = new StubEditableBidirToOneBeedListener();
//  $listener4 = new StubEditableBidirToOneBeedListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many;
  private MyEditableBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableBidirToOneBeed;
  private OneBeanBeed $one;
  private BidirToManyBeed<OneBeanBeed, ManyBeanBeed> $bidirToManyBeed;
  private BidirToOneEdit<OneBeanBeed, ManyBeanBeed> $bidirToOneEdit;
  private BidirToOneEvent<OneBeanBeed, ManyBeanBeed> $bidirToOneEvent;
  private StubListener<AggregateEvent> $listener1;
  private StubListener<AggregateEvent> $listener2;
//  private StubEditableBidirToOneBeedListener $listener3;
//  private StubEditableBidirToOneBeedListener $listener4;

  @Test
  public void constructor() {
    assertEquals($editableBidirToOneBeed.getOwner(), $many);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $many.addListener($listener1);
    $many.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableBidirToOneBeed.publicUpdateDependents($bidirToOneEvent);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentevents().size());
    assertEquals(1, $listener2.$event.getComponentevents().size());
    assertTrue($listener1.$event.getComponentevents().contains($bidirToOneEvent));
    assertTrue($listener2.$event.getComponentevents().contains($bidirToOneEvent));
  }

  @Test
  public void getOne() throws EditStateException, IllegalEditException {
    // set the to many side to null
    BidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    edit.setGoal(null);
    edit.perform();
    assertNull($editableBidirToOneBeed.get());
    assertNull($editableBidirToOneBeed.getOne());
    // set the to many side to some value
    edit =
      new BidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableBidirToOneBeed);
    edit.setGoal(new BidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one));
    edit.perform();
    assertNotNull($editableBidirToOneBeed.get());
    assertEquals($editableBidirToOneBeed.getOne(), $one);
  }

  public class ManyBean extends AbstractBeanBeed {

    public final EditableLongBeed lb = new EditableLongBeed(this);

    public final EditableBidirToOneBeed<OneBean, ManyBean> toOne = new EditableBidirToOneBeed<OneBean, ManyBean>(this);

  }

  public class OneBean extends AbstractBeanBeed {

    public final BidirToManyBeed<OneBean, ManyBean> toMany = new BidirToManyBeed<OneBean, ManyBean>(this);

  }

  /**
   * Events propagate from manies to ones.
   */
  @Test
  public void eventPropagation() throws EditStateException, IllegalEditException {
    ManyBean manyBean = new ManyBean();
    OneBean oneBean1 = new OneBean();
    OneBean oneBean2 = new OneBean();
    BidirToOneEdit<OneBean, ManyBean> edit = new BidirToOneEdit<OneBean, ManyBean>(manyBean.toOne);
    edit.setGoal(oneBean1.toMany);
    edit.perform();
    StubListener<AggregateEvent> listener1 = new StubListener<AggregateEvent>();
    oneBean1.addListener(listener1);
    StubListener<Event> listener2 = new StubListener<Event>();
    oneBean2.addListener(listener2);

    listener1.reset();
    listener2.reset();
    LongEdit lEdit = new LongEdit(manyBean.lb);
    lEdit.setGoal(7L);
    lEdit.perform();
    assertNotNull(listener1.$event);
    StringBuffer out = new StringBuffer();
    listener1.$event.toString(out, 1);
//    System.out.println(out);
    assertNull(listener2.$event);

    listener1.reset();
    listener2.reset();
    edit = new BidirToOneEdit<OneBean, ManyBean>(manyBean.toOne);
    edit.setGoal(oneBean2.toMany);
    edit.perform();
    assertNotNull(listener1.$event);
    out = new StringBuffer();
    listener1.$event.toString(out, 1);
//    System.out.println(out);
    assertNotNull(listener2.$event);
    out = new StringBuffer();
    listener2.$event.toString(out, 1);
//    System.out.println(out);

    listener1.reset();
    listener2.reset();
    lEdit = new LongEdit(manyBean.lb);
    lEdit.setGoal(77L);
    lEdit.perform();
    assertNull(listener1.$event);
    assertNotNull(listener2.$event);
    out = new StringBuffer();
    listener2.$event.toString(out, 1);
//    System.out.println(out);

    listener1.reset();
    listener2.reset();
    edit = new BidirToOneEdit<OneBean, ManyBean>(manyBean.toOne);
    edit.setGoal(null);
    edit.perform();
    assertNull(listener1.$event);
    assertNotNull(listener2.$event);
//    System.out.println(listener1.$event);
    out = new StringBuffer();
    listener2.$event.toString(out, 1);
//    System.out.println(out);

    listener1.reset();
    listener2.reset();
    lEdit = new LongEdit(manyBean.lb);
    lEdit.setGoal(777L);
    lEdit.perform();
    assertNull(listener1.$event);
    assertNull(listener2.$event);
  }

}
