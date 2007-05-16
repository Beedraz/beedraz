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

package org.beedraz.semantics_II.expression.collection.set;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.bean.RunBeanBeed;
import org.beedraz.semantics_II.bean.WellBeanBeed;
import org.beedraz.semantics_II.expression.association.set.BidirToOneEdit;
import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.ActualLongEvent;
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
public class TestUnionBeed {

  public class MyUnionBeed extends UnionBeed<WellBeanBeed> {

    /**
     * Made public for testing reasons.
     */
    public void publicUpdateDependents(SetEvent<WellBeanBeed> event) {
      updateDependents(event);
    }

  }


  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    $unionBeed = new MyUnionBeed();
    $run = new RunBeanBeed();
    $wellNull = new WellBeanBeed();
    $well0 = new WellBeanBeed();
    $well1 = new WellBeanBeed();
    $well2 = new WellBeanBeed();
    $well3 = new WellBeanBeed();
    $cqNull = null;
    $cq0 = new Long(0);
    $cq1 = new Long(1);
    $cq2 = new Long(2);
    $cq3 = new Long(3);
    $listener3 = new StubListener<SetEvent<WellBeanBeed>>();
    $listener5 = new StubListener<ActualLongEvent>();
    // add the wells to the run
    BidirToOneEdit<RunBeanBeed, WellBeanBeed> edit =
      new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($wellNull.run);
    edit.setGoal($run.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well0.run);
    edit.setGoal($run.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well1.run);
    edit.setGoal($run.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well2.run);
    edit.setGoal($run.wells);
    edit.perform();
    edit = new BidirToOneEdit<RunBeanBeed, WellBeanBeed>($well3.run);
    edit.setGoal($run.wells);
    edit.perform();
    // initialise the cq values
    LongEdit longEdit = new LongEdit($wellNull.cq);
    longEdit.setGoal($cqNull);
    longEdit.perform();
    longEdit = new LongEdit($well0.cq);
    longEdit.setGoal($cq0);
    longEdit.perform();
    longEdit = new LongEdit($well1.cq);
    longEdit.setGoal($cq1);
    longEdit.perform();
    longEdit = new LongEdit($well2.cq);
    longEdit.setGoal($cq2);
    longEdit.perform();
    longEdit = new LongEdit($well3.cq);
    longEdit.setGoal($cq3);
    longEdit.perform();
    // initialise the set beeds
    $setBeed1 = createSetBeed($well1, $well2);
    $setBeed2 = createSetBeed($well3);
    $setBeed3 = createSetBeed($well0, $well1, $well3);
    // initialise the source
    $source = new EditableSetBeed<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>();
    addElement($source, $setBeed1);
    addElement($source, $setBeed2);
    addElement($source, $setBeed3);
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private RunBeanBeed $run;
  private WellBeanBeed $wellNull;
  private WellBeanBeed $well0;
  private WellBeanBeed $well1;
  private WellBeanBeed $well2;
  private WellBeanBeed $well3;
  private Long $cqNull;
  private Long $cq0;
  private Long $cq1;
  private Long $cq2;
  private Long $cq3;
  private MyUnionBeed $unionBeed;
  private EditableSetBeed<WellBeanBeed> $setBeed1;
  private EditableSetBeed<WellBeanBeed> $setBeed2;
  private EditableSetBeed<WellBeanBeed> $setBeed3;
  private EditableSetBeed<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>> $source;
  private StubListener<SetEvent<WellBeanBeed>> $listener3;
  private StubListener<ActualLongEvent> $listener5;

  @Test
  public void constructor() {
    assertEquals($unionBeed.getSource(), null);
    assertTrue($unionBeed.get().isEmpty());
  }

  /**
   * Source is null.
   */
  @Test
  public void setSource1() throws EditStateException, IllegalEditException {
    // register listeners to the UnionBeed
    $unionBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    SetBeed<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>, ?> source = null;
    $unionBeed.setSource(source);
    assertEquals($unionBeed.getSource(), source);
    assertTrue($unionBeed.get().isEmpty());
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Source is effective.
   */
  @Test
  public void setSource2() throws EditStateException, IllegalEditException {
    // register listeners to the UnionBeed
    $unionBeed.addListener($listener3);
    assertNull($listener3.$event);
    // check setSource
    $unionBeed.setSource($source); // {1,2} union {3} union {0,1,3}
    assertEquals($unionBeed.getSource(), $source);
    assertEquals($unionBeed.get().size(), 4);
    assertTrue($unionBeed.get().contains($well0));
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well2));
    assertTrue($unionBeed.get().contains($well3));
    // value has changed, so the listeners of the union beed are notified
    Set<WellBeanBeed> added = new HashSet<WellBeanBeed>();
    added.add($well0);
    added.add($well1);
    added.add($well2);
    added.add($well3);
    Set<WellBeanBeed> removed = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), null);
    // The UnionBeed is registered as listener of the source, so when
    // the source changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    WellBeanBeed well4 = createWellBeanBeed(4L);
    EditableSetBeed<WellBeanBeed> setBeed = createSetBeed(well4);
    SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>> sourceEdit =
      new SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>($source);
    sourceEdit.addElementToAdd(setBeed);
    sourceEdit.perform(); // {1,2} union {3} union {0,1,3} union {4}
    removed = new HashSet<WellBeanBeed>();
    added = new HashSet<WellBeanBeed>();
    added.add(well4);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), sourceEdit);
    assertEquals($unionBeed.get().size(), 5);
    assertTrue($unionBeed.get().contains($well0));
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well2));
    assertTrue($unionBeed.get().contains($well3));
    assertTrue($unionBeed.get().contains(well4));
    // The UnionBeed is registered as listener of all beeds in the source,
    // so when one of them changes, the beed should be notified
    WellBeanBeed well5 = new WellBeanBeed();
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd(well5);
    setEdit.perform(); // {1,2} union {3} union {0,1,3} union {4,5}
    removed = new HashSet<WellBeanBeed>();
    added = new HashSet<WellBeanBeed>();
    added.add(well5);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit);
    assertEquals($unionBeed.get().size(), 6);
    assertTrue($unionBeed.get().contains($well0));
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well2));
    assertTrue($unionBeed.get().contains($well3));
    assertTrue($unionBeed.get().contains(well4));
    assertTrue($unionBeed.get().contains(well5));
    // change the beed again
    $listener3.reset();
    assertNull($listener3.$event);
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToRemove(well4);
    setEdit.perform(); // {1,2} union {3} union {0,1,3} union {5}
    removed = new HashSet<WellBeanBeed>();
    removed.add(well4);
    added = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit);
    assertEquals($unionBeed.get().size(), 5);
    assertTrue($unionBeed.get().contains($well0));
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well2));
    assertTrue($unionBeed.get().contains($well3));
    assertTrue($unionBeed.get().contains(well5));
    // When a beed is removed from the source, the UnionBeed is removed as listener
    // of that beed.
    $listener3.reset();
    assertNull($listener3.$event);
    sourceEdit = new SetEdit<SetBeed<WellBeanBeed,SetEvent<WellBeanBeed>>>($source);
    sourceEdit.addElementToRemove($setBeed1);
    sourceEdit.perform(); // {3} union {0,1,3} union {5}
    removed = new HashSet<WellBeanBeed>();
    removed.add($well2);
    added = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $unionBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), sourceEdit);
    assertEquals($unionBeed.get().size(), 4);
    assertTrue($unionBeed.get().contains($well0));
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well3));
    assertTrue($unionBeed.get().contains(well5));
    $listener3.reset();
    assertNull($listener3.$event);
    // so, when the removed beed is edited, the union beed is NOT notified
    setEdit = new SetEdit<WellBeanBeed>($setBeed1);
    setEdit.addElementToAdd(well4);
    setEdit.perform(); // {3} union {0,1,3} union {5}
    assertNull($listener3.$event); // the UnionBeed is NOT notified
    // and the value of the union beed is correct
    assertEquals($unionBeed.get().size(), 4);
    assertTrue($unionBeed.get().contains($well0));
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well3));
    assertTrue($unionBeed.get().contains(well5));
  }

  @Test
  public void get() throws EditStateException, IllegalEditException {
    // add a source
    $unionBeed.setSource($source); // {1,2} union {3} union {0,1,3}
    Set<WellBeanBeed> result = $unionBeed.get();
    assertEquals(result.size(), 4);
    assertTrue(result.contains($well0));
    assertTrue(result.contains($well1));
    assertTrue(result.contains($well2));
    assertTrue(result.contains($well3));
    Iterator<WellBeanBeed> iterator = result.iterator(); // we do not know the order
    assertTrue(iterator.hasNext());
    WellBeanBeed next = iterator.next();
    assertTrue(next == $well0 || next == $well1 || next == $well2 || next == $well3);
    assertTrue(iterator.hasNext());
    next = iterator.next();
    assertTrue(next == $well0 || next == $well1 || next == $well2 || next == $well3);
    assertTrue(iterator.hasNext());
    next = iterator.next();
    assertTrue(next == $well0 || next == $well1 || next == $well2 || next == $well3);
    assertTrue(iterator.hasNext());
    next = iterator.next();
    assertTrue(next == $well0 || next == $well1 || next == $well2 || next == $well3);
    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
    // add another source
    $source = new EditableSetBeed<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>();
    addElement($source, $setBeed1);
    addElement($source, $setBeed2);
    $unionBeed.setSource($source);
    result = $unionBeed.get(); // {1,2} union {3}
    assertEquals(result.size(), 3);
    assertTrue(result.contains($well1));
    assertTrue(result.contains($well2));
    assertTrue(result.contains($well3));
    iterator = result.iterator();
    assertTrue(iterator.hasNext()); // we do not know the order
    next = iterator.next();
    assertTrue(next == $well1 || next == $well2 || next == $well3);
    assertTrue(iterator.hasNext());
    next = iterator.next();
    assertTrue(next == $well1 || next == $well2 || next == $well3);
    assertTrue(iterator.hasNext());
    next = iterator.next();
    assertTrue(next == $well1 || next == $well2 || next == $well3);
    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
    // source = null
    $source = null;
    $unionBeed.setSource($source);
    result = $unionBeed.get();
    assertEquals(result.size(), 0);
    iterator = result.iterator();
    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
  }


  @Test
  public void recalculate() throws EditStateException, IllegalEditException {
    // union beed has no source
    $unionBeed.recalculate();
    assertTrue($unionBeed.get().isEmpty());
    // create source
    EditableSetBeed<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>> source =
      new EditableSetBeed<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>();
    // add source to mean beed
    $unionBeed.setSource(source); // {}
    // recalculate (source contains no elements)
    $unionBeed.recalculate();
    assertTrue($unionBeed.get().isEmpty());
    SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>> sourceEdit =
      new SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>(source);
    sourceEdit.addElementToAdd($setBeed1);
    sourceEdit.perform();
    // recalculate (source contains {1,2})
    $unionBeed.recalculate();
    assertEquals($unionBeed.get().size(), 2);
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well2));
    // recalculate (setBeed contains beed 1)
    $unionBeed.recalculate();
    assertEquals($unionBeed.get().size(), 2);
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well2));
    // add beed
    sourceEdit = new SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>(source);
    sourceEdit.addElementToAdd($setBeed2);
    sourceEdit.perform();
    // recalculate (setBeed contains {1,2} and {3})
    $unionBeed.recalculate();
    assertEquals($unionBeed.get().size(), 3);
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well2));
    assertTrue($unionBeed.get().contains($well3));
    // recalculate (setBeed contains {1,2} and {3})
    assertEquals($unionBeed.get().size(), 3);
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well2));
    assertTrue($unionBeed.get().contains($well3));
    // add beed
    sourceEdit = new SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>(source);
    sourceEdit.addElementToAdd($setBeed3);
    sourceEdit.perform();
    // recalculate (setBeed contains {1,2} and {3} and {0,1,3})
    $unionBeed.recalculate();
    assertEquals($unionBeed.get().size(), 4);
    assertTrue($unionBeed.get().contains($well0));
    assertTrue($unionBeed.get().contains($well1));
    assertTrue($unionBeed.get().contains($well2));
    assertTrue($unionBeed.get().contains($well3));
  }

  @Test
  public void getSizeAndCardinality() throws EditStateException, IllegalEditException {
    // add a listener to the size beed
    IntegerBeed<ActualLongEvent> sizeBeed = $unionBeed.getSize();
    sizeBeed.addListener($listener5);
    assertNull($listener5.$event);
    // check the size
    assertEquals($unionBeed.getSize().getLong(), 0L);
    assertEquals($unionBeed.getCardinality().getLong(), 0L);
    // add source
    $unionBeed.setSource($source); // {1,2} union {3} union {0,1,3}
    // check the size
    assertEquals($unionBeed.getSize().getLong(), 4L);
    assertEquals($unionBeed.getCardinality().getLong(), 4L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 0L);
    assertEquals($listener5.$event.getNewLong(), 4L);
    assertEquals($listener5.$event.getEdit(), null);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>> sourceEdit =
      new SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>($source);
    sourceEdit.addElementToRemove($setBeed1);
    sourceEdit.perform(); // {3} union {0,1,3}
    // check the size
    assertEquals($unionBeed.getSize().getLong(), 3L);
    assertEquals($unionBeed.getCardinality().getLong(), 3L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 4L);
    assertEquals($listener5.$event.getNewLong(), 3L);
    assertEquals($listener5.$event.getEdit(), sourceEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add elements
    EditableSetBeed<WellBeanBeed> setBeed = new EditableSetBeed<WellBeanBeed>();
    addElement(setBeed, $well1);
    sourceEdit = new SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>($source);
    sourceEdit.addElementToAdd(setBeed);
    sourceEdit.perform(); // {3} union {0,1,3} union {1}
    // check the size
    assertEquals($unionBeed.getSize().getLong(), 3L);
    assertEquals($unionBeed.getCardinality().getLong(), 3L);
    // check the listener (size has not changed, so no events should be sent)
    assertNull($listener5.$event);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add elements
    sourceEdit = new SetEdit<SetBeed<WellBeanBeed, SetEvent<WellBeanBeed>>>($source);
    sourceEdit.addElementToAdd($setBeed1);
    sourceEdit.perform(); // {3} union {0,1,3} union {1} union {1,2}
    // check the size
    assertEquals($unionBeed.getSize().getLong(), 4L);
    assertEquals($unionBeed.getCardinality().getLong(), 4L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 3L);
    assertEquals($listener5.$event.getNewLong(), 4L);
    assertEquals($listener5.$event.getEdit(), sourceEdit);
  }

  /*
   * Utility methods
   * @param <T>
   * @param elements
   * @return
   * @throws EditStateException
   * @throws IllegalEditException
   */

  private <T> EditableSetBeed<T> createSetBeed(final T... elements) throws EditStateException, IllegalEditException {
    EditableSetBeed<T> setBeed = new EditableSetBeed<T>();
    for (T element : elements) {
      addElement(setBeed, element);
    }
    return setBeed;
  }

  private <T> void addElement(final EditableSetBeed<T> setBeed, final T element) throws EditStateException, IllegalEditException {
    SetEdit<T> setEdit = new SetEdit<T>(setBeed);
    setEdit.addElementToAdd(element);
    setEdit.perform();
  }

//  private <T> void removeElement(final EditableSetBeed<T> setBeed, final T element) throws EditStateException, IllegalEditException {
//    SetEdit<T> setEdit = new SetEdit<T>(setBeed);
//    setEdit.addElementToRemove(element);
//    setEdit.perform();
//  }

  private WellBeanBeed createWellBeanBeed(Long cq) {
    try {
      WellBeanBeed wellBeanBeed = new WellBeanBeed();
      LongEdit edit = new LongEdit(wellBeanBeed.cq);
      edit.setGoal(cq);
      edit.perform();
      assertEquals(wellBeanBeed.cq.get(), cq);
      return wellBeanBeed;
    }
    catch (EditStateException e) {
      assertTrue(false);
      return null;
    }
    catch (IllegalEditException e) {
      assertTrue(false);
      return null;
    }
  }

}