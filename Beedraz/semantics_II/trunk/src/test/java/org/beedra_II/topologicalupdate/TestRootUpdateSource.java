/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra_II.topologicalupdate;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.event.Event;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestRootUpdateSource {

  private RootUpdateSource<IntegerEvent> $subject1;
  private RootUpdateSource<IntegerEvent> $subject2;

  private IntegerEvent $event;
  private IntegerEvent[] $events;

  private StubDependentUpdateSource $dNullEvent1;
  private StubDependentUpdateSource $dNullEvent2;
  private StubDependentUpdateSource[] $ds;

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

  public class StubDependentUpdateSource extends DependentUpdateSource<IntegerEvent> {

    public StubDependentUpdateSource(IntegerEvent event) {
      $event = event;
    }

    private IntegerEvent $event;

    @Override
    protected IntegerEvent update(Map<UpdateSource, Event> events) {
      $updated++;
      $events = new HashMap<UpdateSource, Event>(events);
      return $event;
    }

    public int $updated = 0;

    public Map<UpdateSource, Event> $events;

  }

  @Before
  public void setUp() throws Exception {
    $subject1 = new RootUpdateSource<IntegerEvent>() { /* NOP */ };
    $subject2 = new RootUpdateSource<IntegerEvent>() { /* NOP */ };
    $event = createIntegerEvent();
    $dNullEvent1 = new StubDependentUpdateSource(null);
    $dNullEvent2 = new StubDependentUpdateSource(null);
  }

  private void initDs(int n) {
    $events = new IntegerEvent[n];
    $ds = new StubDependentUpdateSource[n];
    for (int i = 0; i < n; i++) {
      $events[i] = createIntegerEvent();
      $ds[i] = new StubDependentUpdateSource($events[i]);
    }
  }

  private IntegerEvent createIntegerEvent() {
    return new IntegerEvent(new EditableIntegerBeed(new AbstractBeanBeed() { /* NOP */ }), null, null, null);
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  @Test
  public void testGetMaximumRootUpdateSourceDistance() {
    assertEquals(0, $subject1.getMaximumRootUpdateSourceDistance());
  }

  /**
   *                      $subject
   */
  @Test
  public void testUpdateDependents1() {
    $subject1.updateDependents($event);
  }

  /**
   *                      $subject
   *                         |
   *                    $dNullEvent1
   */
  @Test
  public void testUpdateDependents2() {
    $dNullEvent1.addUpdateSource($subject1);
    $subject1.updateDependents($event);
    assertEquals(1, $dNullEvent1.$updated);
    assertEquals(1, $dNullEvent1.$events.size());
    assertTrue($dNullEvent1.$events.keySet().contains($subject1));
    assertEquals($event, $dNullEvent1.$events.get($subject1));
  }

  /**
   *                      $subject
   *                         |
   *                       ------
   *                      |      |
   *             $dNullEvent1  $dNullEvent2
   */
  @Test
  public void testUpdateDependents3() {
    $dNullEvent1.addUpdateSource($subject1);
    $dNullEvent2.addUpdateSource($subject1);
    $subject1.updateDependents($event);
    assertEquals(1, $dNullEvent1.$updated);
    assertEquals(1, $dNullEvent1.$events.size());
    assertTrue($dNullEvent1.$events.keySet().contains($subject1));
    assertEquals($event, $dNullEvent1.$events.get($subject1));
    assertEquals(1, $dNullEvent2.$updated);
    assertEquals(1, $dNullEvent2.$events.size());
    assertTrue($dNullEvent2.$events.keySet().contains($subject1));
    assertEquals($event, $dNullEvent2.$events.get($subject1));
  }

  /**
   *                      $subject
   *                         |
   *                    $dNullEvent1
   *                         |
   *                    $dNullEvent2
   */
  @Test
  public void testUpdateDependents4() {
    $dNullEvent1.addUpdateSource($subject1);
    $dNullEvent2.addUpdateSource($dNullEvent1); // $d1 event is null
    $subject1.updateDependents($event);
    assertEquals(1, $dNullEvent1.$updated);
    assertEquals(1, $dNullEvent1.$events.size());
    assertTrue($dNullEvent1.$events.keySet().contains($subject1));
    assertEquals($event, $dNullEvent1.$events.get($subject1));
    assertEquals(0, $dNullEvent2.$updated);
    assertNull($dNullEvent2.$events);
  }

  /**
   *                      $subject
   *                         |
   *                       $ds[0]
   */
  @Test
  public void testUpdateDependents5() {
    initDs(1);
    $ds[0].addUpdateSource($subject1);
    $subject1.updateDependents($event);
    assertEquals(1, $ds[0].$updated);
    assertEquals(1, $ds[0].$events.size());
    assertTrue($ds[0].$events.keySet().contains($subject1));
    assertEquals($event, $ds[0].$events.get($subject1));
  }

  /**
   *                      $subject
   *                         |
   *                       ------
   *                      |      |
   *                    $ds[0] $ds[1]
   */
  @Test
  public void testUpdateDependents6() {
    initDs(2);
    $ds[0].addUpdateSource($subject1);
    $ds[1].addUpdateSource($subject1);
    $subject1.updateDependents($event);
    assertEquals(1, $ds[0].$updated);
    assertTrue($ds[0].$events.size() >= 1);
    assertTrue($ds[0].$events.size() <= 2);
    assertTrue($ds[0].$events.keySet().contains($subject1));
    assertEquals($event, $ds[0].$events.get($subject1));
    assertEquals(1, $ds[1].$updated);
    assertTrue($ds[1].$events.size() >= 1);
    assertTrue($ds[1].$events.size() <= 2);
    assertTrue($ds[1].$events.keySet().contains($subject1));
    assertEquals($event, $ds[1].$events.get($subject1));
    assertTrue($ds[0].$events.keySet().contains($ds[1]) || $ds[1].$events.keySet().contains($ds[0]));
    assertTrue($ds[0].$events.get($ds[1]) == $events[1] || $ds[1].$events.get($ds[0]) == $events[0]);
  }

  /**
   *                      $subject
   *                         |
   *                       $ds[0]
   *                         |
   *                       $ds[1]
   */
  @Test
  public void testUpdateDependents7() {
    initDs(2);
    $ds[0].addUpdateSource($subject1);
    $ds[1].addUpdateSource($ds[0]);
    $subject1.updateDependents($event);
    assertEquals(1, $ds[0].$updated);
    assertEquals(1, $ds[0].$events.size());
    assertTrue($ds[0].$events.keySet().contains($subject1));
    assertEquals($event, $ds[0].$events.get($subject1));
    assertEquals(1, $ds[1].$updated);
    assertEquals(2, $ds[1].$events.size());
    assertTrue($ds[1].$events.keySet().contains($subject1));
    assertEquals($event, $ds[1].$events.get($subject1));
    assertTrue($ds[1].$events.keySet().contains($ds[0]));
    assertEquals($events[0], $ds[1].$events.get($ds[0]));
  }

  /**
   *                      $subject
   *                          |
   *                   -----------------
   *                  |                 |
   *                $ds[0]            $ds[5]
   *                  |                 |
   *            --------------          |
   *           |      |       |         |
   *         $ds[1] $ds[2]  $ds[3]      |
   *           |      |       |         |
   *            --------------          |
   *                  |                 |
   *                $ds[4]              |
   *                  |                 |
   *                   -----------------
   *                          |
   *                         $ds[6]
   */
  @Test
  public void testUpdateDependents8a() {
    initDs(7);
    $ds[0].addUpdateSource($subject1);
    $ds[1].addUpdateSource($ds[0]);
    $ds[2].addUpdateSource($ds[0]);
    $ds[3].addUpdateSource($ds[0]);
    $ds[4].addUpdateSource($ds[1]);
    $ds[4].addUpdateSource($ds[2]);
    $ds[4].addUpdateSource($ds[3]);
    $ds[5].addUpdateSource($subject1);
    $ds[6].addUpdateSource($ds[5]);
    $ds[6].addUpdateSource($ds[4]);
    $subject1.updateDependents($event);
    checkTopology8();
  }

  /**
   *                      $subject
   *                          |
   *                   -----------------
   *                  |                 |
   *                $ds[0]            $ds[5]
   *                  |                 |
   *            --------------          |
   *           |      |       |         |
   *         $ds[1] $ds[2]  $ds[3]      |
   *           |      |       |         |
   *            --------------          |
   *                  |                 |
   *                $ds[4]              |
   *                  |                 |
   *                   -----------------
   *                          |
   *                         $ds[6]
   */
  @Test
  public void testUpdateDependents8b() {
    initDs(7);
    $ds[6].addUpdateSource($ds[4]);
    $ds[6].addUpdateSource($ds[5]);
    $ds[5].addUpdateSource($subject1);
    $ds[4].addUpdateSource($ds[3]);
    $ds[4].addUpdateSource($ds[2]);
    $ds[4].addUpdateSource($ds[1]);
    $ds[3].addUpdateSource($ds[0]);
    $ds[2].addUpdateSource($ds[0]);
    $ds[1].addUpdateSource($ds[0]);
    $ds[0].addUpdateSource($subject1);
    $subject1.updateDependents($event);
    checkTopology8();
  }

  /**
   *                      $subject
   *                          |
   *                   -----------------
   *                  |                 |
   *                $ds[0]            $ds[5]
   *                  |                 |
   *            --------------          |
   *           |      |       |         |
   *         $ds[1] $ds[2]  $ds[3]      |
   *           |      |       |         |
   *            --------------          |
   *                  |                 |
   *                $ds[4]              |
   *                  |                 |
   *                   -----------------
   *                          |
   *                         $ds[6]
   */
  @Test
  public void testUpdateDependents8c() {
    initDs(7);
    $ds[2].addUpdateSource($ds[0]);
    $ds[6].addUpdateSource($ds[5]);
    $ds[4].addUpdateSource($ds[2]);
    $ds[4].addUpdateSource($ds[3]);
    $ds[0].addUpdateSource($subject1);
    $ds[4].addUpdateSource($ds[1]);
    $ds[6].addUpdateSource($ds[4]);
    $ds[1].addUpdateSource($ds[0]);
    $ds[5].addUpdateSource($subject1);
    $ds[3].addUpdateSource($ds[0]);
    $subject1.updateDependents($event);
    checkTopology8();
  }

  private void printMaximumRootUpdateSourceDistance() {
    System.out.println("                      $subject" + mrusd($subject1));
    System.out.println("                          |");
    System.out.println("                   -----------------");
    System.out.println("                  |                 |");
    System.out.println("                 $ds[0]" + mrusd($ds[0]) + "           $ds[5]" + mrusd($ds[5]));
    System.out.println("                  |                 |");
    System.out.println("            --------------          |");
    System.out.println("           |      |       |         |");
    System.out.println("          $ds[1]" + mrusd($ds[1]) + "$ds[2]" + mrusd($ds[2]) + " $ds[3]" + mrusd($ds[3])+ "    |");
    System.out.println("           |      |       |         |");
    System.out.println("            --------------          |");
    System.out.println("                  |                 |");
    System.out.println("                 $ds[4]" + mrusd($ds[4]) + "            |");
    System.out.println("                  |                 |");
    System.out.println("                   -----------------");
    System.out.println("                          |");
    System.out.println("                         $ds[6]" + mrusd($ds[6]));

  }

  private String mrusd(UpdateSource us) {
    return " (" + us.getMaximumRootUpdateSourceDistance() + ")";
  }

  private void checkTopology8() {
    printMaximumRootUpdateSourceDistance();
    checkDependent8Level1($ds[0]);

    checkDependent8Level1($ds[5]);

    checkDependent8Level2($ds[1]);
    checkDependent8Level2($ds[2]);
    checkDependent8Level2($ds[3]);

    assertEquals(1, $ds[4].$updated);
    assertTrue($ds[4].$events.size() >= 5);
    assertTrue($ds[4].$events.size() <= 6);
    assertTrue($ds[4].$events.keySet().contains($subject1));
    assertEquals($event, $ds[4].$events.get($subject1));
    assertTrue($ds[4].$events.keySet().contains($ds[0]));
    assertEquals($events[0], $ds[4].$events.get($ds[0]));
    assertTrue($ds[4].$events.keySet().contains($ds[1]));
    assertEquals($events[1], $ds[4].$events.get($ds[1]));
    assertTrue($ds[4].$events.keySet().contains($ds[2]));
    assertEquals($events[2], $ds[4].$events.get($ds[2]));
    assertTrue($ds[4].$events.keySet().contains($ds[3]));
    assertEquals($events[3], $ds[4].$events.get($ds[3]));

    assertEquals(1, $ds[6].$updated);
    assertEquals(7, $ds[6].$events.size());
    assertTrue($ds[6].$events.keySet().contains($subject1));
    assertEquals($event, $ds[6].$events.get($subject1));
    assertTrue($ds[6].$events.keySet().contains($ds[0]));
    assertEquals($events[0], $ds[6].$events.get($ds[0]));
    assertTrue($ds[6].$events.keySet().contains($ds[1]));
    assertEquals($events[1], $ds[6].$events.get($ds[1]));
    assertTrue($ds[6].$events.keySet().contains($ds[2]));
    assertEquals($events[2], $ds[6].$events.get($ds[2]));
    assertTrue($ds[6].$events.keySet().contains($ds[3]));
    assertEquals($events[3], $ds[6].$events.get($ds[3]));
    assertTrue($ds[6].$events.keySet().contains($ds[4]));
    assertEquals($events[4], $ds[6].$events.get($ds[4]));
    assertTrue($ds[6].$events.keySet().contains($ds[5]));
    assertEquals($events[5], $ds[6].$events.get($ds[5]));
  }

  private void checkDependent8Level1(StubDependentUpdateSource dependent) {
    assertEquals(1, dependent.$updated);
    assertTrue(dependent.$events.size() >= 1);
    assertTrue(dependent.$events.size() <= 2);
    assertTrue(dependent.$events.keySet().contains($subject1));
    assertEquals($event, dependent.$events.get($subject1));
  }

  private void checkDependent8Level2(StubDependentUpdateSource dependent) {
    assertEquals(1, dependent.$updated);
    assertTrue(dependent.$events.size() >= 2);
    assertTrue(dependent.$events.size() <= 5);
    assertTrue(dependent.$events.keySet().contains($subject1));
    assertEquals($event, dependent.$events.get($subject1));
    assertTrue(dependent.$events.keySet().contains($ds[0]));
    assertEquals($events[0], dependent.$events.get($ds[0]));
  }

  /**
   *              $subject          $subject2
   *                  |                 |
   *               ------             $ds[0]
   *              |      |              |
   *           $ds[1]  $ds[2]           |
   *              |      |              |
   *              |    -----            |
   *              |   |     |           |
   *               ---       -----------
   *                |             |
   *              $ds[4]        $ds[3]
   *                |             |
   *                 -------------
   *                       |
   *                     $ds[5]
   */
  @Test
  public void testUpdateDependents9a() {
    initDs(6);
    $ds[0].addUpdateSource($subject2);
    $ds[1].addUpdateSource($subject1);
    $ds[2].addUpdateSource($subject1);
    $ds[3].addUpdateSource($ds[0]);
    $ds[3].addUpdateSource($ds[2]);
    $ds[4].addUpdateSource($ds[1]);
    $ds[4].addUpdateSource($ds[2]);
    $ds[5].addUpdateSource($ds[3]);
    $ds[5].addUpdateSource($ds[4]);
    $subject1.updateDependents($event);
    checkTopology9();
  }

  /**
   *              $subject          $subject2
   *                  |                 |
   *               ------             $ds[0]
   *              |      |              |
   *           $ds[1]  $ds[2]           |
   *              |      |              |
   *              |    -----            |
   *              |   |     |           |
   *               ---       -----------
   *                |             |
   *              $ds[4]        $ds[3]
   *                |             |
   *                 -------------
   *                       |
   *                     $ds[5]
   */
  @Test
  public void testUpdateDependents9b() {
    initDs(6);
    $ds[5].addUpdateSource($ds[4]);
    $ds[5].addUpdateSource($ds[3]);
    $ds[3].addUpdateSource($ds[0]);
    $ds[4].addUpdateSource($ds[1]);
    $ds[4].addUpdateSource($ds[2]);
    $ds[3].addUpdateSource($ds[2]);
    $ds[2].addUpdateSource($subject1);
    $ds[1].addUpdateSource($subject1);
    $ds[0].addUpdateSource($subject2);
    $subject1.updateDependents($event);
    checkTopology9();
  }

  /**
   *              $subject          $subject2
   *                  |                 |
   *               ------             $ds[0]
   *              |      |              |
   *           $ds[1]  $ds[2]           |
   *              |      |              |
   *              |    -----            |
   *              |   |     |           |
   *               ---       -----------
   *                |             |
   *              $ds[4]        $ds[3]
   *                |             |
   *                 -------------
   *                       |
   *                     $ds[5]
   */
  @Test
  public void testUpdateDependents9c() {
    initDs(6);
    $ds[3].addUpdateSource($ds[2]);
    $ds[0].addUpdateSource($subject2);
    $ds[3].addUpdateSource($ds[0]);
    $ds[4].addUpdateSource($ds[1]);
    $ds[1].addUpdateSource($subject1);
    $ds[5].addUpdateSource($ds[4]);
    $ds[2].addUpdateSource($subject1);
    $ds[5].addUpdateSource($ds[3]);
    $ds[4].addUpdateSource($ds[2]);
    $subject1.updateDependents($event);
    checkTopology9();
  }

  private void checkTopology9() {
    System.out.println();
    for (int i = 0; i < 6; i++) {
      System.out.println("$ds[" + i + "] = " + $ds[i].getMaximumRootUpdateSourceDistance());
    }

    assertEquals(0, $ds[0].$updated);
    assertNull($ds[0].$events);

    checkDependent9Level1($ds[1]);
    checkDependent9Level1($ds[2]);

    checkDependent9Level2($ds[3]);
    checkDependent9Level2($ds[4]);

    assertEquals(1, $ds[5].$updated);
    assertEquals(5, $ds[5].$events.size());
    assertTrue($ds[5].$events.keySet().contains($subject1));
    assertEquals($event, $ds[5].$events.get($subject1));
    for (int i = 4; i >= 1; i--) {
      assertTrue($ds[5].$events.keySet().contains($ds[i]));
      assertEquals($events[i], $ds[5].$events.get($ds[i]));
    }
  }

  private void checkDependent9Level1(StubDependentUpdateSource dependent) {
    assertEquals(1, dependent.$updated);
    assertTrue(dependent.$events.size() >= 1);
    assertTrue(dependent.$events.size() <= 2);
    assertTrue(dependent.$events.keySet().contains($subject1));
    assertEquals($event, dependent.$events.get($subject1));
  }

  private void checkDependent9Level2(StubDependentUpdateSource dependent) {
    assertEquals(1, dependent.$updated);
    assertTrue(dependent.$events.size() >= 3);
    assertTrue(dependent.$events.size() <= 4);
    assertTrue(dependent.$events.keySet().contains($subject1));
    assertEquals($event, dependent.$events.get($subject1));
    for (int i = 2; i >= 1; i--) {
      assertTrue(dependent.$events.keySet().contains($ds[i]));
      assertEquals($events[i], dependent.$events.get($ds[i]));
    }
  }

  /**
   *              $subject          $subject2
   *                  |                 |
   *               ------             $ds[0]
   *              |      |              |
   *           $ds[1]  $ds[2]         $ds[6]
   *              |      |             ...
   *              |    -----         $ds[200]
   *              |   |     |           |
   *               ---       -----------
   *                |             |
   *              $ds[4]        $ds[3]
   *                |             |
   *                 -------------
   *                       |
   *                     $ds[5]
   */
  @Test
  public void testUpdateDependents10() {
    initDs(201);
    $ds[0].addUpdateSource($subject2);
    $ds[1].addUpdateSource($subject1);
    $ds[2].addUpdateSource($subject1);
    $ds[3].addUpdateSource($ds[200]);
    $ds[3].addUpdateSource($ds[2]);
    $ds[4].addUpdateSource($ds[1]);
    $ds[4].addUpdateSource($ds[2]);
    $ds[5].addUpdateSource($ds[3]);
    $ds[5].addUpdateSource($ds[4]);
    $ds[6].addUpdateSource($ds[0]);
    for (int i = 7; i <= 200; i++) {
      $ds[i].addUpdateSource($ds[i - 1]);
    }
    $subject1.updateDependents($event);
    checkTopology10();
  }

  private void checkTopology10() {
    System.out.println();
    for (int i = 0; i < 201; i++) {
      System.out.println("$ds[" + i + "] = " + $ds[i].getMaximumRootUpdateSourceDistance());
    }

    assertEquals(0, $ds[0].$updated);
    assertNull($ds[0].$events);
    for (int i = 6; i <= 200; i++) {
      assertEquals(0, $ds[i].$updated);
      assertNull($ds[i].$events);
    }
    checkDependent9Level1($ds[1]);
    checkDependent9Level1($ds[2]);

    checkDependent9Level2($ds[3]);
    checkDependent9Level2($ds[4]);

    assertEquals(1, $ds[5].$updated);
    assertEquals(5, $ds[5].$events.size());
    assertTrue($ds[5].$events.keySet().contains($subject1));
    assertEquals($event, $ds[5].$events.get($subject1));
    for (int i = 4; i >= 1; i--) {
      assertTrue($ds[5].$events.keySet().contains($ds[i]));
      assertEquals($events[i], $ds[5].$events.get($ds[i]));
    }
  }

  /**
   *              $subject          $subject2
   *                  |                 |
   *               ------             $ds[0]
   *              |      |              |
   *           $ds[1]  $ds[2]           |
   *              |      |              |
   *              |    $ds[6]           |
   *              |     ...             |
   *              |   $ds[200]          |
   *              |      |              |
   *              |    -----            |
   *              |   |     |           |
   *               ---       -----------
   *                |             |
   *              $ds[4]        $ds[3]
   *                |             |
   *                 -------------
   *                       |
   *                     $ds[5]
   */
  @Test
  public void testUpdateDependents11() {
    initDs(201);
    $ds[0].addUpdateSource($subject2);
    $ds[1].addUpdateSource($subject1);
    $ds[2].addUpdateSource($subject1);
    $ds[3].addUpdateSource($ds[0]);
    $ds[3].addUpdateSource($ds[200]);
    $ds[4].addUpdateSource($ds[1]);
    $ds[4].addUpdateSource($ds[200]);
    $ds[5].addUpdateSource($ds[3]);
    $ds[5].addUpdateSource($ds[4]);
    $ds[6].addUpdateSource($ds[2]);
    for (int i = 7; i <= 200; i++) {
      $ds[i].addUpdateSource($ds[i - 1]);
    }
    $subject1.updateDependents($event);
    checkTopology11();
  }

  private void checkTopology11() {
    System.out.println();
    for (int i = 0; i < 201; i++) {
      System.out.println("$ds[" + i + "] = " + $ds[i].getMaximumRootUpdateSourceDistance());
    }

    assertEquals(0, $ds[0].$updated);
    assertNull($ds[0].$events);

    checkDependent9Level1($ds[1]);
    checkDependent9Level1($ds[2]);

    for (int i = 6; i <= 200; i++) {
      assertEquals(1, $ds[i].$updated);
      assertTrue($ds[i].$events.size() >= 2 + i - 6);
      assertTrue($ds[i].$events.size() <= 2 + i - 6 + 1);
      assertTrue($ds[i].$events.keySet().contains($subject1));
      assertEquals($event, $ds[i].$events.get($subject1));
      for (int j = 6; j < i; j++) {
        assertTrue($ds[i].$events.keySet().contains($ds[j]));
        assertEquals($events[j], $ds[i].$events.get($ds[j]));
      }
    }

    checkDependent11Level2($ds[3]);
    checkDependent11Level2($ds[4]);

    assertEquals(1, $ds[5].$updated);
    assertEquals(200, $ds[5].$events.size());
    assertTrue($ds[5].$events.keySet().contains($subject1));
    assertEquals($event, $ds[5].$events.get($subject1));
    for (int i = 1; i <= 200; i++) {
      if (i != 5) {
        assertTrue($ds[5].$events.keySet().contains($ds[i]));
        assertEquals($events[i], $ds[5].$events.get($ds[i]));
      }
    }
  }

  private void checkDependent11Level2(StubDependentUpdateSource dependent) {
    assertEquals(1, dependent.$updated);
    assertTrue(dependent.$events.size() >= 198);
    assertTrue(dependent.$events.size() <= 199);
    assertTrue(dependent.$events.keySet().contains($subject1));
    assertEquals($event, dependent.$events.get($subject1));
    for (int i = 1; i <= 200; i++) {
      if ((i < 3) || (i > 5)) {
        assertTrue(dependent.$events.keySet().contains($ds[i]));
        assertEquals($events[i], dependent.$events.get($ds[i]));
      }
    }
  }

}

