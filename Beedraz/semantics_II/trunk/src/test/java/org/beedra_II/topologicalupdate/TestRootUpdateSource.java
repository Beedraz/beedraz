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

  private RootUpdateSource<IntegerEvent> $subject;

//  private Dependent<Event> $dependent1;
//
//  private Dependent<Event> $dependent2;

  private IntegerEvent $event;
  private IntegerEvent $event3;
  private IntegerEvent $event4;
  private IntegerEvent $event5;
  private IntegerEvent $event6;
  private IntegerEvent $event7;
  private IntegerEvent $event8;
  private IntegerEvent $event9;

  private StubDependentUpdateSource $dNullEvent1;
  private StubDependentUpdateSource $dNullEvent2;
  private StubDependentUpdateSource $d3;
  private StubDependentUpdateSource $d4;
  private StubDependentUpdateSource $d5;
  private StubDependentUpdateSource $d6;
  private StubDependentUpdateSource $d7;
  private StubDependentUpdateSource $d8;
  private StubDependentUpdateSource $d9;

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
    $subject = new RootUpdateSource<IntegerEvent>() { /* NOP */ };
    $event = createIntegerEvent();
    $event3 = createIntegerEvent();
    $event4 = createIntegerEvent();
    $event5 = createIntegerEvent();
    $event6 = createIntegerEvent();
    $event7 = createIntegerEvent();
    $event8 = createIntegerEvent();
    $event9 = createIntegerEvent();
    $dNullEvent1 = new StubDependentUpdateSource(null);
    $dNullEvent2 = new StubDependentUpdateSource(null);
    $d3 = new StubDependentUpdateSource($event3);
    $d4 = new StubDependentUpdateSource($event4);
    $d5 = new StubDependentUpdateSource($event5);
    $d6 = new StubDependentUpdateSource($event6);
    $d7 = new StubDependentUpdateSource($event7);
    $d8 = new StubDependentUpdateSource($event8);
    $d9 = new StubDependentUpdateSource($event9);
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
    assertEquals(0, $subject.getMaximumRootUpdateSourceDistance());
  }

  /**
   *                      $subject
   */
  @Test
  public void testUpdateDependents1() {
    $subject.updateDependents($event);
  }

  /**
   *                      $subject
   *                         |
   *                    $dNullEvent1
   */
  @Test
  public void testUpdateDependents2() {
    $dNullEvent1.addUpdateSource($subject);
    $subject.updateDependents($event);
    assertEquals(1, $dNullEvent1.$updated);
    assertEquals(1, $dNullEvent1.$events.size());
    assertTrue($dNullEvent1.$events.keySet().contains($subject));
    assertEquals($event, $dNullEvent1.$events.get($subject));
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
    $dNullEvent1.addUpdateSource($subject);
    $dNullEvent2.addUpdateSource($subject);
    $subject.updateDependents($event);
    assertEquals(1, $dNullEvent1.$updated);
    assertEquals(1, $dNullEvent1.$events.size());
    assertTrue($dNullEvent1.$events.keySet().contains($subject));
    assertEquals($event, $dNullEvent1.$events.get($subject));
    assertEquals(1, $dNullEvent2.$updated);
    assertEquals(1, $dNullEvent2.$events.size());
    assertTrue($dNullEvent2.$events.keySet().contains($subject));
    assertEquals($event, $dNullEvent2.$events.get($subject));
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
    $dNullEvent1.addUpdateSource($subject);
    $dNullEvent2.addUpdateSource($dNullEvent1); // $d1 event is null
    $subject.updateDependents($event);
    assertEquals(1, $dNullEvent1.$updated);
    assertEquals(1, $dNullEvent1.$events.size());
    assertTrue($dNullEvent1.$events.keySet().contains($subject));
    assertEquals($event, $dNullEvent1.$events.get($subject));
    assertEquals(0, $dNullEvent2.$updated);
    assertNull($dNullEvent2.$events);
  }

  /**
   *                      $subject
   *                         |
   *                        $d3
   */
  @Test
  public void testUpdateDependents5() {
    $d3.addUpdateSource($subject);
    $subject.updateDependents($event);
    assertEquals(1, $d3.$updated);
    assertEquals(1, $d3.$events.size());
    assertTrue($d3.$events.keySet().contains($subject));
    assertEquals($event, $d3.$events.get($subject));
  }

  /**
   *                      $subject
   *                         |
   *                       ------
   *                      |      |
   *                     $d3    $d4
   */
  @Test
  public void testUpdateDependents6() {
    $d3.addUpdateSource($subject);
    $d4.addUpdateSource($subject);
    $subject.updateDependents($event);
    assertEquals(1, $d3.$updated);
    assertTrue($d3.$events.size() >= 1);
    assertTrue($d3.$events.size() <= 2);
    assertTrue($d3.$events.keySet().contains($subject));
    assertEquals($event, $d3.$events.get($subject));
    assertEquals(1, $d4.$updated);
    assertTrue($d4.$events.size() >= 1);
    assertTrue($d4.$events.size() <= 2);
    assertTrue($d4.$events.keySet().contains($subject));
    assertEquals($event, $d4.$events.get($subject));
    assertTrue($d3.$events.keySet().contains($d4) || $d4.$events.keySet().contains($d3));
    assertTrue($d3.$events.get($d4) == $event4 || $d4.$events.get($d3) == $event3);
  }

  /**
   *                      $subject
   *                         |
   *                        $d3
   *                         |
   *                        $d4
   */
  @Test
  public void testUpdateDependents7() {
    $d3.addUpdateSource($subject);
    $d4.addUpdateSource($d3);
    $subject.updateDependents($event);
    assertEquals(1, $d3.$updated);
    assertEquals(1, $d3.$events.size());
    assertTrue($d3.$events.keySet().contains($subject));
    assertEquals($event, $d3.$events.get($subject));
    assertEquals(1, $d4.$updated);
    assertEquals(2, $d4.$events.size());
    assertTrue($d4.$events.keySet().contains($subject));
    assertEquals($event, $d4.$events.get($subject));
    assertTrue($d4.$events.keySet().contains($d3));
    assertEquals($event3, $d4.$events.get($d3));
  }

  /**
   *                      $subject
   *                          |
   *                   -----------------
   *                  |                 |
   *                 $d3               $d8
   *                  |                 |
   *            --------------          |
   *           |      |       |         |
   *          $d4    $d5     $d6        |
   *           |      |       |         |
   *            --------------          |
   *                  |                 |
   *                 $d7                |
   *                  |                 |
   *                   -----------------
   *                          |
   *                         $d9
   */
  @Test
  public void testUpdateDependents8a() {
    $d3.addUpdateSource($subject);
    $d4.addUpdateSource($d3);
    $d5.addUpdateSource($d3);
    $d6.addUpdateSource($d3);
    $d7.addUpdateSource($d4);
    $d7.addUpdateSource($d5);
    $d7.addUpdateSource($d6);
    $d8.addUpdateSource($subject);
    $d9.addUpdateSource($d8);
    $d9.addUpdateSource($d7);
    $subject.updateDependents($event);
    checkTopology8();
  }

  /**
   *                      $subject
   *                          |
   *                   -----------------
   *                  |                 |
   *                 $d3               $d8
   *                  |                 |
   *            --------------          |
   *           |      |       |         |
   *          $d4    $d5     $d6        |
   *           |      |       |         |
   *            --------------          |
   *                  |                 |
   *                 $d7                |
   *                  |                 |
   *                   -----------------
   *                          |
   *                         $d9
   */
  @Test
  public void testUpdateDependents8b() {
    $d9.addUpdateSource($d7);
    $d9.addUpdateSource($d8);
    $d8.addUpdateSource($subject);
    $d7.addUpdateSource($d6);
    $d7.addUpdateSource($d5);
    $d7.addUpdateSource($d4);
    $d6.addUpdateSource($d3);
    $d5.addUpdateSource($d3);
    $d4.addUpdateSource($d3);
    $d3.addUpdateSource($subject);
    $subject.updateDependents($event);
    checkTopology8();
  }

  /**
   *                      $subject
   *                          |
   *                   -----------------
   *                  |                 |
   *                 $d3               $d8
   *                  |                 |
   *            --------------          |
   *           |      |       |         |
   *          $d4    $d5     $d6        |
   *           |      |       |         |
   *            --------------          |
   *                  |                 |
   *                 $d7                |
   *                  |                 |
   *                   -----------------
   *                          |
   *                         $d9
   */
  @Test
  public void testUpdateDependents8c() {
    $d5.addUpdateSource($d3);
    $d9.addUpdateSource($d8);
    $d7.addUpdateSource($d5);
    $d7.addUpdateSource($d6);
    $d3.addUpdateSource($subject);
    $d7.addUpdateSource($d4);
    $d9.addUpdateSource($d7);
    $d4.addUpdateSource($d3);
    $d8.addUpdateSource($subject);
    $d6.addUpdateSource($d3);
    $subject.updateDependents($event);
    checkTopology8();
  }

  private void printMaximumRootUpdateSourceDistance() {
    System.out.println("                      $subject" + mrusd($subject));
    System.out.println("                          |");
    System.out.println("                   -----------------");
    System.out.println("                  |                 |");
    System.out.println("                 $d3" + mrusd($d3) + "           $d8" + mrusd($d8));
    System.out.println("                  |                 |");
    System.out.println("            --------------          |");
    System.out.println("           |      |       |         |");
    System.out.println("          $d4" + mrusd($d4) + "$d5" + mrusd($d5) + " $d6" + mrusd($d6)+ "    |");
    System.out.println("           |      |       |         |");
    System.out.println("            --------------          |");
    System.out.println("                  |                 |");
    System.out.println("                 $d7" + mrusd($d7) + "            |");
    System.out.println("                  |                 |");
    System.out.println("                   -----------------");
    System.out.println("                          |");
    System.out.println("                         $d9" + mrusd($d9));

  }

  private String mrusd(UpdateSource us) {
    return " (" + us.getMaximumRootUpdateSourceDistance() + ")";
  }

  private void checkTopology8() {
    printMaximumRootUpdateSourceDistance();
    checkDependent8Level1($d3);

    checkDependent8Level1($d8);

    checkDependent8Level2($d4);
    checkDependent8Level2($d5);
    checkDependent8Level2($d6);

    assertEquals(1, $d7.$updated);
    assertTrue($d7.$events.size() >= 5);
    assertTrue($d7.$events.size() <= 6);
    assertTrue($d7.$events.keySet().contains($subject));
    assertEquals($event, $d7.$events.get($subject));
    assertTrue($d7.$events.keySet().contains($d3));
    assertEquals($event3, $d7.$events.get($d3));
    assertTrue($d7.$events.keySet().contains($d4));
    assertEquals($event4, $d7.$events.get($d4));
    assertTrue($d7.$events.keySet().contains($d5));
    assertEquals($event5, $d7.$events.get($d5));
    assertTrue($d7.$events.keySet().contains($d6));
    assertEquals($event6, $d7.$events.get($d6));

    assertEquals(1, $d9.$updated);
    assertEquals(7, $d9.$events.size());
    assertTrue($d9.$events.keySet().contains($subject));
    assertEquals($event, $d9.$events.get($subject));
    assertTrue($d9.$events.keySet().contains($d3));
    assertEquals($event3, $d9.$events.get($d3));
    assertTrue($d9.$events.keySet().contains($d4));
    assertEquals($event4, $d9.$events.get($d4));
    assertTrue($d9.$events.keySet().contains($d5));
    assertEquals($event5, $d9.$events.get($d5));
    assertTrue($d9.$events.keySet().contains($d6));
    assertEquals($event6, $d9.$events.get($d6));
    assertTrue($d9.$events.keySet().contains($d7));
    assertEquals($event7, $d9.$events.get($d7));
    assertTrue($d9.$events.keySet().contains($d8));
    assertEquals($event8, $d9.$events.get($d8));
  }

  private void checkDependent8Level1(StubDependentUpdateSource dependent) {
    assertEquals(1, dependent.$updated);
    assertTrue(dependent.$events.size() >= 1);
    assertTrue(dependent.$events.size() <= 2);
    assertTrue(dependent.$events.keySet().contains($subject));
    assertEquals($event, dependent.$events.get($subject));
  }

  private void checkDependent8Level2(StubDependentUpdateSource dependent) {
    assertEquals(1, dependent.$updated);
    assertTrue(dependent.$events.size() >= 2);
    assertTrue(dependent.$events.size() <= 5);
    assertTrue(dependent.$events.keySet().contains($subject));
    assertEquals($event, dependent.$events.get($subject));
    assertTrue(dependent.$events.keySet().contains($d3));
    assertEquals($event3, dependent.$events.get($d3));
  }

//  /**
//   *              $subject          $subject2
//   *                  |                 |
//   *               ------              $d3
//   *              |      |              |
//   *             $d4    $d5             |
//   *              |      |              |
//   *              |    -----            |
//   *              |   |     |           |
//   *               ---       -----------
//   *                |             |
//   *               $d7           $d6
//   *                |             |
//   *                 -------------
//   *                       |
//   *                      $d9
//   */
//  @Test
//  public void testUpdateDependents9a() {
//    $d3.addUpdateSource($subject);
//    $d4.addUpdateSource($d3);
//    $d5.addUpdateSource($d3);
//    $d6.addUpdateSource($d3);
//    $d7.addUpdateSource($d4);
//    $d7.addUpdateSource($d5);
//    $d7.addUpdateSource($d6);
//    $d8.addUpdateSource($subject);
//    $d9.addUpdateSource($d8);
//    $d9.addUpdateSource($d7);
//    $subject.updateDependents($event);
//    checkTopology8();
//  }

}

