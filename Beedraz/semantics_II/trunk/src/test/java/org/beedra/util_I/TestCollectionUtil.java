/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedra.util_I;

import static org.beedra.util_I.CollectionUtil.intersection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class TestCollectionUtil {

//  @BeforeClass
//  public static void setUpBeforeClass() throws Exception {
//  }
//
//  @AfterClass
//  public static void tearDownAfterClass() throws Exception {
//  }
//
  @Before
  public void setUp() {
    $s1 = new HashSet<String>();
    $s1.add("lala");
    $s1.add("lele");
    $s1.add("lili");
    $s1.add("lolo");
    $s1.add("lulu");
    $s1.add("papa");
    $s1.add("pipi");
    $s1.add("popo");
    $s2 = new TreeSet<String>();
    $s2.add("kaka");
    $s2.add("keke");
    $s2.add("kiki");
    $s2.add("koko");
    $s2.add("kuku");
    $s2.add("papa");
    $s2.add("pipi");
    $s2.add("popo");
    $e1 = Collections.emptySet();
    $e2 = Collections.emptySet();
  }

  @After
  public void tearDown() {
    $s1 = null;
    $s2 = null;
    $e1 = null;
    $e2 = null;
  }

  private Set<String> $s1;
  private Set<String> $s2;
  private Set<String> $e1;
  private Set<String> $e2;

  @Test
  public void testIntersection1() {
    Set<String> i = intersection($e1, $e2);
    assertNotNull(i);
    assertTrue(i.isEmpty());
  }

  @Test
  public void testIntersection2a() {
    Set<String> i = intersection($e1, $s2);
    assertNotNull(i);
    assertTrue(i.isEmpty());
  }

  @Test
  public void testIntersection2b() {
    Set<String> i = intersection($s1, $e2);
    assertNotNull(i);
    assertTrue(i.isEmpty());
  }

  @Test
  public void testIntersection3() {
    Set<String> i = intersection($s1, $s2);
    assertNotNull(i);
    assertTrue(! i.isEmpty());
    assertEquals(3, i.size());
    assertTrue(i.contains("papa"));
    assertTrue(i.contains("pipi"));
    assertTrue(i.contains("popo"));
  }

  @Test
  public void testEmptySortedSet() {
    SortedSet<String> ssstr1 = CollectionUtil.emptySortedSet();
    SortedSet<String> ssstr2 = CollectionUtil.emptySortedSet();
    assertTrue(ssstr1 == ssstr2);
    assertTrue(ssstr1.isEmpty());
    assertEquals(0, ssstr1.size());
    assertTrue(! ssstr1.contains("DODO"));
    assertNull(ssstr1.comparator());
    try {
      ssstr1.first();
      fail();
    }
    catch (NoSuchElementException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    try {
      ssstr1.last();
      fail();
    }
    catch (NoSuchElementException nseExc) {
      // we need to get here
    }
    catch (Throwable t) {
      fail();
    }
    assertEquals(ssstr1, ssstr1.headSet("dada"));
    assertEquals(ssstr1, ssstr1.tailSet("dodo"));
    assertEquals(ssstr1, ssstr1.subSet("aaaa", "zzzz"));
  }

}

