/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.topologicalupdate;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.beedra_II.event.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestAbstractUpdateSource {

  private AbstractUpdateSource $subject;

  private Dependent<Event> $dependent1;

  private UpdateSource $dependentUpdateSource;

  private Dependent<Event> $dependent2;

  public class StubDependent extends Dependent<Event> {

    @Override
    public UpdateSource getDependentUpdateSource() {
      return $dependentUpdateSource;
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
    $dependentUpdateSource = new UpdateSource() {

      public void addDependent(Dependent<?> dependent) {
        // NOP
      }

      public Set<Dependent<?>> getDependents() {
        return null;
      }

      public int getMaximumRootUpdateSourceDistance() {
        return 0;
      }

      public void removeDependent(Dependent<?> dependent) {
        // NOP
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

