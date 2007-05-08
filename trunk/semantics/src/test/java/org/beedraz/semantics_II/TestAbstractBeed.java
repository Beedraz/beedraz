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

package org.beedraz.semantics_II;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

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
public class TestAbstractBeed {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private StubBeed $subject = new StubBeed();

  private StubListener<StubEvent> $listener1 = new StubListener<StubEvent>();
  private StubListener<StubEvent> $listener2 = new StubListener<StubEvent>();

  private StubEvent $event = new StubEvent($subject);

  @Test
  public void addChangeListener() {
    assertTrue(! $subject.isListener($listener1));
    assertTrue(! $subject.isListener($listener2));
    $subject.addListener($listener1);
    assertTrue($subject.isListener($listener1));
    assertTrue(! $subject.isListener($listener2));
    $subject.addListener($listener2);
    assertTrue($subject.isListener($listener1));
    assertTrue($subject.isListener($listener2));
  }

  @Test
  public void removeChangeListener1() {
    $subject.addListener($listener1);
    $subject.addListener($listener2);
    assertTrue($subject.isListener($listener1));
    assertTrue($subject.isListener($listener2));
    $subject.removeListener($listener1);
    assertTrue(! $subject.isListener($listener1));
    assertTrue($subject.isListener($listener2));
    $subject.removeListener($listener2);
    assertTrue(! $subject.isListener($listener1));
    assertTrue(! $subject.isListener($listener2));
  }

  @Test
  public void removeChangeListener2() {
    $subject.addListener($listener1);
    $subject.addListener($listener2);
    assertTrue($subject.isListener($listener1));
    assertTrue($subject.isListener($listener2));
    $subject.removeListener(null);
    assertTrue($subject.isListener($listener1));
    assertTrue($subject.isListener($listener2));
  }

  @Test
  public void removeChangeListener3() {
    $subject.addListener($listener1);
    assertTrue($subject.isListener($listener1));
    assertTrue(! $subject.isListener($listener2));
    $subject.removeListener($listener2);
    assertTrue($subject.isListener($listener1));
    assertTrue(! $subject.isListener($listener2));
  }

  @Test
  public void fireChangeEvent() {
    $subject.addListener($listener1);
    $subject.addListener($listener2);
    assertTrue($subject.isListener($listener1));
    assertTrue($subject.isListener($listener2));
    assertNull($listener1.$event);
    assertNull($listener2.$event);
    $subject.publicUpdateDependents($event);
    assertNotNull($listener1.$event);
    assertNotNull($listener2.$event);
    assertEquals($event, $listener1.$event);
    assertEquals($event, $listener2.$event);
  }

}

