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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra_II.event.StubEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestUpdateSourceUpdateDependents {

  private StubUpdateSource $subject1;
  private StubUpdateSource $subject2;
  private int $mrusd2 = 6;

//  private StubEvent $event;
//  private StubEvent[] $events;

  private StubDependentUpdateSource $dNullEvent1;
  private StubDependentUpdateSource $dNullEvent2;
  private StubDependentUpdateSource[] $ds;

  @Before
  public void setUp() throws Exception {
    $subject1 = new StubUpdateSource();
    $subject2 = new StubUpdateSource($mrusd2);
    $dNullEvent1 = new StubDependentUpdateSource(false);
    $dNullEvent2 = new StubDependentUpdateSource(false);
  }

  private void initDs(int n) {
    $ds = new StubDependentUpdateSource[n];
    for (int i = 0; i < n; i++) {
      $ds[i] = new StubDependentUpdateSource(true);
    }
  }

  @After
  public void tearDown() throws Exception {
    $subject1 = null;
    $subject2 = null;
    $dNullEvent1 = null;
    $dNullEvent2 = null;
    $ds = null;
  }

  @Test
  public void testGetMaximumRootUpdateSourceDistance() {
    assertEquals(0, $subject1.getMaximumRootUpdateSourceDistance());
    assertEquals($mrusd2, $subject2.getMaximumRootUpdateSourceDistance());
  }

  /**
   *                      $subject
   */
  @Test
  public void testUpdateDependents1() {
    $subject1.updateDependents();
    eventFired($subject1);
  }

  private void eventFired(AbstractStubUpdateSource sus) {
    eventFired(sus, sus.$myEvent);
  }

  private void eventFired(AbstractStubUpdateSource asus, StubEvent event) {
    if (event != null) {
      assertNotNull(asus.$firedEvent);
    }
    assertEquals(event, asus.$firedEvent);
  }

  /**
   *                      $subject
   *                         |
   *                    $dNullEvent1
   */
  @Test
  public void testUpdateDependents2() {
    $dNullEvent1.addUpdateSource($subject1);
    $subject1.updateDependents();
    updated($dNullEvent1, $subject1);
    eventFired($dNullEvent1, null);
    eventFired($subject1);
  }

  private void updated(StubDependentUpdateSource sdus, AbstractStubUpdateSource... suss) {
    if ((suss == null) || (suss.length <= 0)) {
      assertEquals(0, sdus.$updated);
      assertNull(sdus.$events);
    }
    else {
      assertEquals(1, sdus.$updated);
      assertTrue(sdus.$events.size() >= suss.length);
      for (int i = 0; i < suss.length; i++) {
        assertTrue(sdus.$events.containsKey(suss[i]));
        assertEquals(suss[i].$myEvent, sdus.$events.get(suss[i]));
      }
    }
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
    $subject1.updateDependents();
    updated($dNullEvent1, $subject1);
    updated($dNullEvent2, $subject1);
    eventFired($dNullEvent1, null);
    eventFired($dNullEvent2, null);
    eventFired($subject1);
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
    $subject1.updateDependents();
    updated($dNullEvent1, $subject1);
    updated($dNullEvent2);
    eventFired($dNullEvent1, null);
    eventFired($dNullEvent2, null);
    eventFired($subject1);
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
    $subject1.updateDependents();
    updated($ds[0], $subject1);
    eventFired($ds[0]);
    eventFired($subject1);
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
    $subject1.updateDependents();
    updated($ds[0], $subject1);
    updated($ds[1], $subject1);
    eventFired($ds[0]);
    eventFired($ds[1]);
    eventFired($subject1);
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
    $subject1.updateDependents();
    updated($ds[0], $subject1);
    updated($ds[1], $subject1, $ds[0]);
    eventFired($ds[0]);
    eventFired($ds[1]);
    eventFired($subject1);
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
    $subject1.updateDependents();
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
    $subject1.updateDependents();
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
    $subject1.updateDependents();
    checkTopology8();
  }

//  private void printMaximumRootUpdateSourceDistance() {
//    System.out.println("                      $subject" + mrusd($subject1));
//    System.out.println("                          |");
//    System.out.println("                   -----------------");
//    System.out.println("                  |                 |");
//    System.out.println("                 $ds[0]" + mrusd($ds[0]) + "           $ds[5]" + mrusd($ds[5]));
//    System.out.println("                  |                 |");
//    System.out.println("            --------------          |");
//    System.out.println("           |      |       |         |");
//    System.out.println("          $ds[1]" + mrusd($ds[1]) + "$ds[2]" + mrusd($ds[2]) + " $ds[3]" + mrusd($ds[3])+ "    |");
//    System.out.println("           |      |       |         |");
//    System.out.println("            --------------          |");
//    System.out.println("                  |                 |");
//    System.out.println("                 $ds[4]" + mrusd($ds[4]) + "            |");
//    System.out.println("                  |                 |");
//    System.out.println("                   -----------------");
//    System.out.println("                          |");
//    System.out.println("                         $ds[6]" + mrusd($ds[6]));
//
//  }

//  private String mrusd(UpdateSource us) {
//    return " (" + us.getMaximumRootUpdateSourceDistance() + ")";
//  }

  private void checkTopology8() {
    updated($ds[0], $subject1);
    updated($ds[5], $subject1);
    updated($ds[1], $subject1, $ds[0]);
    updated($ds[2], $subject1, $ds[0]);
    updated($ds[3], $subject1, $ds[0]);
    updated($ds[4], $subject1, $ds[0], $ds[1], $ds[2], $ds[3]);
    updated($ds[6], $subject1, $ds[0], $ds[1], $ds[2], $ds[3], $ds[4], $ds[5]);
    eventFired($subject1);
    for (int i = 0; i < 7; i++) {
      eventFired($ds[i]);
    }
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
    $subject1.updateDependents();
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
    $subject1.updateDependents();
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
    $subject1.updateDependents();
    checkTopology9();
  }

  private void checkTopology9() {
    updated($ds[0]);
    updated($ds[1], $subject1);
    updated($ds[2], $subject1);
    updated($ds[3], $subject1, $ds[2]);
    updated($ds[4], $subject1, $ds[1], $ds[2]);
    updated($ds[5], $subject1, $ds[1], $ds[2], $ds[4], $ds[3]);
    eventFired($subject1);
    for (int i = 1; i < 6; i++) {
      eventFired($ds[i]);
    }
    eventFired($subject2, null);
    eventFired($ds[0], null);
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
    $subject1.updateDependents();
    checkTopology10();
  }

  private void checkTopology10() {
    updated($ds[1], $subject1);
    updated($ds[2], $subject1);
    updated($ds[3], $subject1, $ds[2]);
    updated($ds[4], $subject1, $ds[1], $ds[2]);
    updated($ds[5], $subject1, $ds[1], $ds[2], $ds[4], $ds[3]);
    eventFired($subject1);
    for (int i = 1; i < 6; i++) {
      eventFired($ds[i]);
    }
    eventFired($subject2, null);
    updated($ds[0]);
    eventFired($ds[0], null);
    for (int i = 6; i <= 200; i++) {
      updated($ds[i]);
      eventFired($ds[i], null);
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
    $subject1.updateDependents();
    checkTopology11();
  }

  private void checkTopology11() {
    updated($ds[0]);
    updated($ds[1], $subject1);
    updated($ds[2], $subject1);
    updated($ds[6], $subject1, $ds[2]);
    updated($ds[3], buildEventSources(6, 201, $subject1, $ds[2]));
    updated($ds[4], buildEventSources(6, 201, $subject1, $ds[1], $ds[2]));
    updated($ds[5], buildEventSources(6, 201, $subject1, $ds[1], $ds[2], $ds[3], $ds[4]));
    eventFired($subject1);
    eventFired($ds[0], null);
    for (int i = 1; i <= 200; i++) {
      eventFired($ds[i]);
    }
    eventFired($subject2, null);
  }

  private AbstractStubUpdateSource[] buildEventSources(int from, int until, AbstractStubUpdateSource... other) {
    AbstractStubUpdateSource[] result = new AbstractStubUpdateSource[until - from + other.length];
    for (int i = 0; i < other.length; i++) {
      result[i] = other[i];
    }
    for (int i = from; i < until; i++) {
      result[i - from + other.length] = $ds[i];
    }
    return result;
  }

}

