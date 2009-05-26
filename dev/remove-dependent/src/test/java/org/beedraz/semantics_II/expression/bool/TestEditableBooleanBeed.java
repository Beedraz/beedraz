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

package org.beedraz.semantics_II.expression.bool;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.TopologicalUpdateAccess;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateEvent;
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
public class TestEditableBooleanBeed {

  public class MyEditableBooleanBeed extends EditableBooleanBeed {
    public MyEditableBooleanBeed(AggregateBeed owner) {
      super(owner);
    }

    /**
     * Makes the updateDependents method public for testing reasons.
     */
    public void publicTopologicalUpdateStart(BooleanEvent event) {
      TopologicalUpdateAccess.topologicalUpdate(this, event);
    }


  }

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    $owner = new MyBeanBeed();
    $editableBooleanBeed = new MyEditableBooleanBeed($owner);
    $stringEdit = new BooleanEdit($editableBooleanBeed);
    $stringEdit.perform();
    $event1 = new BooleanEvent($editableBooleanBeed, Boolean.TRUE, Boolean.FALSE, null);
    $listener1 = new StubListener<AggregateEvent>();
    $listener2 = new StubListener<AggregateEvent>();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private AggregateBeed $owner;
  private MyEditableBooleanBeed $editableBooleanBeed;
  private BooleanEdit $stringEdit;
  private BooleanEvent $event1;
  private StubListener<AggregateEvent> $listener1;
  private StubListener<AggregateEvent> $listener2;

  @Test
  public void constructor() {
    assertTrue($owner.isAggregateElement($editableBooleanBeed));
    // the abstract property beed should be registered with the owner:
    // add listeners to the property beed
    $owner.addListener($listener1);
    $owner.addListener($listener2);
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    // fire a change on the registered beed
    $editableBooleanBeed.publicTopologicalUpdateStart($event1);
    // listeners of the aggregate beed should be notified
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals(1, $listener1.$event.getComponentEvents().size());
    assertEquals(1, $listener2.$event.getComponentEvents().size());
    assertTrue($listener1.$event.getComponentEvents().contains($event1));
    assertTrue($listener2.$event.getComponentEvents().contains($event1));
  }

}
