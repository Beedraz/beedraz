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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.Listener;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateEvent;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.expression.collection.set.ordered.OrderedSetEvent;
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
public class TestEditableOrderedBidirToOneBeed {

  public class MyEditableOrderedBidirToOneBeed<_One_ extends BeanBeed, _Many_ extends BeanBeed>
      extends EditableOrderedBidirToOneBeed<_One_, _Many_> {

    public MyEditableOrderedBidirToOneBeed(_Many_ owner) {
      super(owner);
    }

    /**
     * updateDependents is made public for testing reasons
     */
    public void publicUpdateDependents(OrderedBidirToOneEvent<_One_, _Many_> event) {
      updateDependents(event);
    }

  }

  public class OneBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class ManyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class StubEditableOrderedBidirToOneBeedListener implements Listener<OrderedSetEvent<Integer>> {

    public void beedChanged(OrderedSetEvent<Integer> event) {
      $event = event;
    }

    public void reset() {
      $event = null;
    }

    public OrderedSetEvent<Integer> $event;

  }

  @Before
  public void setUp() throws Exception {
    $many = new ManyBeanBeed();
    $editableOrderedBidirToOneBeed =
      new MyEditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed>($many);
    $one = new OneBeanBeed();
    $OrderedBidirToManyBeed =
      new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one);
    $OrderedBidirToOneEdit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed);
    $OrderedBidirToOneEdit.perform();
    $OrderedBidirToOneEvent =
      new OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed, null, $OrderedBidirToManyBeed, $OrderedBidirToOneEdit);
    $listener1 = new StubListener<AggregateEvent>();
    $listener2 = new StubListener<AggregateEvent>();
//  $listener3 = new StubEditableOrderedBidirToOneBeedListener();
//  $listener4 = new StubEditableOrderedBidirToOneBeedListener();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private ManyBeanBeed $many;
  private MyEditableOrderedBidirToOneBeed<OneBeanBeed, ManyBeanBeed> $editableOrderedBidirToOneBeed;
  private OneBeanBeed $one;
  private OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed> $OrderedBidirToManyBeed;
  private OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> $OrderedBidirToOneEdit;
  private OrderedBidirToOneEvent<OneBeanBeed, ManyBeanBeed> $OrderedBidirToOneEvent;
  private StubListener<AggregateEvent> $listener1;
  private StubListener<AggregateEvent> $listener2;
//  private StubEditableOrderedBidirToOneBeedListener $listener3;
//  private StubEditableOrderedBidirToOneBeedListener $listener4;

  @Test
  public void constructor() {
    assertEquals($editableOrderedBidirToOneBeed.getOwner(), $many);
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $many.addListener($listener1);
    $many.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableOrderedBidirToOneBeed.publicUpdateDependents($OrderedBidirToOneEvent);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentevents().size());
    assertEquals(1, $listener2.$event.getComponentevents().size());
    assertTrue($listener1.$event.getComponentevents().contains($OrderedBidirToOneEvent));
    assertTrue($listener2.$event.getComponentevents().contains($OrderedBidirToOneEvent));
  }

  @Test
  public void getOne() throws EditStateException, IllegalEditException {
    // set the to many side to null
    OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed> edit =
      new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed);
    edit.setGoal(null);
    edit.perform();
    assertNull($editableOrderedBidirToOneBeed.get());
    assertNull($editableOrderedBidirToOneBeed.getOne());
    // set the to many side to some value
    edit = new OrderedBidirToOneEdit<OneBeanBeed, ManyBeanBeed>($editableOrderedBidirToOneBeed);
    edit.setGoal(new OrderedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>($one));
    edit.perform();
    assertNotNull($editableOrderedBidirToOneBeed.get());
    assertEquals($editableOrderedBidirToOneBeed.getOne(), $one);
  }

}
