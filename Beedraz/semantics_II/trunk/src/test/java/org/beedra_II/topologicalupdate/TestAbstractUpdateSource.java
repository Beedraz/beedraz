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

package org.beedra_II.topologicalupdate;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.beedra_II.event.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestAbstractUpdateSource {

  private AbstractUpdateSource $subject;

  private Dependent<Event> $dependent1;

  private Dependent<Event> $dependent2;

  public class StubDependent extends Dependent<Event> {

    @Override
    public UpdateSource getDependentUpdateSource() {
      return null;
    }

    @Override
    public Event update(Map<UpdateSource, Event> events) {
      return null;
    }

  }

  @Before
  public void setUp() throws Exception {
    $subject = new AbstractUpdateSource() {

      public int getMaximumRootUpdateSourceDistance() {
        return 0;
      }

    };
    $dependent1 = new StubDependent();
    $dependent2 = new StubDependent();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  @Test
  public void testAddDependent() {
    $subject.addDependent($dependent1);
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
    $subject.addDependent($dependent2);
    assertTrue($subject.getDependents().contains($dependent2));
    assertEquals(2, $subject.getDependents().size());
  }

  @Test
  public void testRemoveDependent1() {
    $subject.addDependent($dependent1);
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
    $subject.removeDependent($dependent1);
    assertFalse($subject.getDependents().contains($dependent1));
    assertTrue($subject.getDependents().isEmpty());
  }

  @Test
  public void testRemoveDependent2() {
    $subject.addDependent($dependent1);
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
    $subject.removeDependent(null);
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
  }

  @Test
  public void testRemoveDependent3() {
    $subject.addDependent($dependent1);
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
    $subject.removeDependent($dependent2);
    assertEquals(1, $subject.getDependents().size());
    assertTrue($subject.getDependents().contains($dependent1));
  }

  @Test
  public void testRemoveDependent4() {
    $subject.removeDependent($dependent2);
    assertTrue($subject.getDependents().isEmpty());
  }

}

