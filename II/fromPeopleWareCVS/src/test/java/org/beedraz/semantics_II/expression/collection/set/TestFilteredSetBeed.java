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


import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.edit.EditStateException;
import org.beedraz.semantics_II.edit.IllegalEditException;
import org.beedraz.semantics_II.expression.association.set.BidirToManyBeed;
import org.beedraz.semantics_II.expression.association.set.BidirToOneEdit;
import org.beedraz.semantics_II.expression.association.set.EditableBidirToOneBeed;
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanEQBeed;
import org.beedraz.semantics_II.expression.bool.BooleanNullBeed;
import org.beedraz.semantics_II.expression.collection.set.EditableSetBeed;
import org.beedraz.semantics_II.expression.collection.set.FilteredSetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEdit;
import org.beedraz.semantics_II.expression.collection.set.SetEvent;
import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.ActualLongEvent;
import org.beedraz.semantics_II.expression.number.integer.long64.EditableLongBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.LongEdit;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleConstantBeed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleModBeed;
import org.beedraz.semantics_II.path.ConstantPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathFactory;
import org.beedraz.semantics_II.path.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestFilteredSetBeed {

  public class MyNewFilteredSetBeed extends FilteredSetBeed<WellBeanBeed> {

    public MyNewFilteredSetBeed(PathFactory<WellBeanBeed, BooleanBeed> criterion) {
      super(criterion);
    }

    /**
     * Made public for testing reasons.
     */
    public void publicUpdateDependents(SetEvent<WellBeanBeed> event) {
      updateDependents(event);
    }

  }

  public class RunBeanBeed extends AbstractBeanBeed {
    /**
     * The wells contained in this run.
     */
    public final BidirToManyBeed<RunBeanBeed, WellBeanBeed> wells =
      new BidirToManyBeed<RunBeanBeed, WellBeanBeed>(this);

  }

  public class WellBeanBeed extends AbstractBeanBeed {

    /**
     * The run in which the well is contained.
     */
    public final EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed> run =
      new EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>(this);

    /**
     * The Cq value of the well.
     */
    public final EditableLongBeed cq = new EditableLongBeed(this);

  }

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    $criterion = new PathFactory<WellBeanBeed, BooleanBeed>() {
        // filter the wells whose cq value is effective and even
        public Path<BooleanBeed> createPath(WellBeanBeed startBeed) {
          // construct the %
          DoubleModBeed modBeed = new DoubleModBeed();
          modBeed.setDividendPath(path(startBeed.cq));
          modBeed.setDivisorPath(path(new DoubleConstantBeed(2)));
          // construct the ==
          BooleanEQBeed eqBeed = new BooleanEQBeed();
          eqBeed.setLeftOperandPath(path(modBeed));
          eqBeed.setRightOperandPath(path(new DoubleConstantBeed(0)));
          return new ConstantPath<BooleanBeed>(eqBeed);
        }
    };
    $filteredSetBeed = new MyNewFilteredSetBeed($criterion);
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
  private PathFactory<WellBeanBeed, BooleanBeed> $criterion;
  private MyNewFilteredSetBeed $filteredSetBeed;
  private StubListener<SetEvent<WellBeanBeed>> $listener3;
  private StubListener<ActualLongEvent> $listener5;

  @Test
  public void constructor() {
    assertEquals($filteredSetBeed.getCriterion(), $criterion);
    assertEquals($filteredSetBeed.getSource(), null);
    assertEquals($filteredSetBeed.getSourcePath(), null);
    assertTrue($filteredSetBeed.get().isEmpty());
  }

  /**
   * Source is null.
   */
  @Test
  public void setSourcePath1() throws EditStateException, IllegalEditException {
    // register listeners to the NewFilteredSetBeed
    $filteredSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // set the sourcePath
    Path<? extends SetBeed<WellBeanBeed, ?>> sourcePath = null;
    $filteredSetBeed.setSourcePath(sourcePath);
    // source path and source should be set, beed should be updated
    assertEquals($filteredSetBeed.getSourcePath(), sourcePath);
    assertEquals($filteredSetBeed.getSource(), null);
    assertTrue($filteredSetBeed.get().isEmpty());
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Source is effective.
   */
  @Test
  public void setSourcePath2() throws EditStateException, IllegalEditException {
    // register listeners to the NewFilteredSetBeed
    $filteredSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // set the source path
    EditableSetBeed<WellBeanBeed> source = createSource();
    Path<? extends SetBeed<WellBeanBeed, ?>> sourcePath =
      new ConstantPath<SetBeed<WellBeanBeed,?>>(source);
    $filteredSetBeed.setSourcePath(sourcePath);
    // source path and source should be set, beed should be updated
    assertEquals($filteredSetBeed.getSourcePath(), sourcePath);
    assertEquals($filteredSetBeed.getSource(), source);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
    // value has changed, so the listeners of the beed are notified
    Set<WellBeanBeed> added = new HashSet<WellBeanBeed>();
    added.add($well2);
    Set<WellBeanBeed> removed = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), null);
    // the FilteretSetBeed is registered as listener of the source path,
    // so when the source path changes
    // @mudo How can I construct a path to a source that can change?
    // The NewFilteredSetBeed is registered as listener of the source, so when
    // the source changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    WellBeanBeed well4 = createWellBeanBeed(4L);
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd(well4);
    setEdit.perform();
    removed = new HashSet<WellBeanBeed>();
    added = new HashSet<WellBeanBeed>();
    added.add(well4);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit);
    assertEquals($filteredSetBeed.get().size(), 2);
    assertTrue($filteredSetBeed.get().contains($well2));
    assertTrue($filteredSetBeed.get().contains(well4));
    // The NewFilteredSetBeed is registered as listener of all beeds
    // in the source (i.e. of the corresponding filter criteria),
    // so when one of them changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    LongEdit longEdit = new LongEdit(well4.cq);
    longEdit.setGoal(5L);
    longEdit.perform();
    removed = new HashSet<WellBeanBeed>();
    removed.add(well4);
    added = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), longEdit);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
    // change the beed again
    $listener3.reset();
    assertNull($listener3.$event);
    longEdit = new LongEdit(well4.cq);
    longEdit.setGoal(6L);
    longEdit.perform();
    removed = new HashSet<WellBeanBeed>();
    added = new HashSet<WellBeanBeed>();
    added.add(well4);
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), longEdit);
    assertEquals($filteredSetBeed.get().size(), 2);
    assertTrue($filteredSetBeed.get().contains($well2));
    assertTrue($filteredSetBeed.get().contains(well4));

    // When a beed is removed from the source, the NewFilteredSetBeed is removed as listener
    // of that beed.
    $listener3.reset();
    assertNull($listener3.$event);
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove(well4);
    setEdit.perform();
    removed = new HashSet<WellBeanBeed>();
    removed.add(well4);
    added = new HashSet<WellBeanBeed>();
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $filteredSetBeed);
    assertEquals($listener3.$event.getAddedElements(), added);
    assertEquals($listener3.$event.getRemovedElements(), removed);
    assertEquals($listener3.$event.getEdit(), setEdit);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));
    $listener3.reset();
    assertNull($listener3.$event);
    // so, when the removed beed is edited, the filtered set beed is NOT notified
    longEdit = new LongEdit(well4.cq);
    longEdit.setGoal(7L);
    longEdit.perform();
    assertNull($listener3.$event); // the NewFilteredSetBeed is NOT notified
    // and the value of the filtered set beed is correct
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));

    // the filter criterion of an element could return a new beed
    // @mudo construct a test
  }

  /**
   * Are update sources removed properly?
   */
  @Test
  public void setSourcePath3() throws EditStateException, IllegalEditException {
    // register listeners to the NewFilteredSetBeed
    $filteredSetBeed.addListener($listener3);
    assertNull($listener3.$event);
    // set the source path
    EditableSetBeed<WellBeanBeed> source = createSource();
    Path<? extends SetBeed<WellBeanBeed, ?>> sourcePath =
      new ConstantPath<SetBeed<WellBeanBeed,?>>(source);
    $filteredSetBeed.setSourcePath(sourcePath);
    // source path and source should be set, beed should be updated
    assertEquals($filteredSetBeed.getSourcePath(), sourcePath);
    assertEquals($filteredSetBeed.getSource(), source);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well2));

    // when the source path is replaced by another one, it should be removed
    // as update source
    EditableSetBeed<WellBeanBeed> sourceOther = createOtherSource();
    Path<? extends SetBeed<WellBeanBeed, ?>> sourcePathOther =
      new ConstantPath<SetBeed<WellBeanBeed,?>>(sourceOther);
    $filteredSetBeed.setSourcePath(sourcePathOther);
    // source path and source should be set, beed should be updated
    assertEquals($filteredSetBeed.getSourcePath(), sourcePathOther);
    assertEquals($filteredSetBeed.getSource(), sourceOther);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well0));

    // when the old source path is changed, the beed should not be
    // notified
    $listener3.reset();
    assertNull($listener3.$event);
    // @mudo the source path cannot be changed

    // when the source is replaced by another one, it should be removed
    // as update source
    // so, when the old source is changed, the beed should not be
    // notified
    $listener3.reset();
    assertNull($listener3.$event);
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove($well1);
    setEdit.perform();
    // listener is not notified
    assertNull($listener3.$event);
    // beed has not changed
    assertEquals($filteredSetBeed.getSourcePath(), sourcePathOther);
    assertEquals($filteredSetBeed.getSource(), sourceOther);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well0));

    // when the beeds are replaced by other beeds, they are removed
    // as update source (i.e. the corresponding filter criteria are removed)
    // so, when one of those beeds is changed, the beed should not be
    // notified
    $listener3.reset();
    assertNull($listener3.$event);

    LongEdit longEdit = new LongEdit($well1.cq);
    longEdit.setGoal(6L);
    longEdit.perform();
    // listener is not notified
    assertNull($listener3.$event);
    // beed has not changed
    assertEquals($filteredSetBeed.getSourcePath(), sourcePathOther);
    assertEquals($filteredSetBeed.getSource(), sourceOther);
    assertEquals($filteredSetBeed.get().size(), 1);
    assertTrue($filteredSetBeed.get().contains($well0));

  }

  @Test
  public void get() throws EditStateException, IllegalEditException {
    // filter the even wells
    SetBeed<WellBeanBeed, ?> source = createSource();
    Path<? extends SetBeed<WellBeanBeed, ?>> sourcePath =
      new ConstantPath<SetBeed<WellBeanBeed,?>>(source);
    $filteredSetBeed.setSourcePath(sourcePath);
    Set<WellBeanBeed> result = $filteredSetBeed.get();
    assertEquals(result.size(), 1);
    assertTrue(result.contains($well2));
    Iterator<WellBeanBeed> iterator = result.iterator(); // we do not know the order
    assertTrue(iterator.hasNext());
    WellBeanBeed next = iterator.next();
    assertTrue(next == $well2);
    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
    // filter the odd wells
    PathFactory<WellBeanBeed, BooleanBeed> criterion =
      new PathFactory<WellBeanBeed, BooleanBeed>() {
        // filter the wells whose cq value is effective and odd
        public Path<BooleanBeed> createPath(WellBeanBeed startBeed) {
          // construct the %
          DoubleModBeed modBeed = new DoubleModBeed();
          modBeed.setDividendPath(path(startBeed.cq));
          modBeed.setDivisorPath(path(new DoubleConstantBeed(2)));
          // construct the ==
          BooleanEQBeed eqBeed = new BooleanEQBeed();
          eqBeed.setLeftOperandPath(path(modBeed));
          eqBeed.setRightOperandPath(path(new DoubleConstantBeed(1)));
          return new ConstantPath<BooleanBeed>(eqBeed);
        }
    };
    $filteredSetBeed = new MyNewFilteredSetBeed(criterion);
    $filteredSetBeed.setSourcePath(sourcePath);
    result = $filteredSetBeed.get();
    assertEquals(result.size(), 2);
    assertTrue(result.contains($well1));
    assertTrue(result.contains($well3));
    iterator = result.iterator();
    assertTrue(iterator.hasNext()); // we do not know the order
    next = iterator.next();
    assertTrue(next == $well1 || next == $well3);
    assertTrue(iterator.hasNext());
    next = iterator.next();
    assertTrue(next == $well1 || next == $well3);
    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      // should not be reached
      assertTrue(false);
    }
    catch(NoSuchElementException exc) {
      assertTrue(true);
    }
    // filter the wells whose cq value is empty
    criterion = new PathFactory<WellBeanBeed, BooleanBeed>() {
        // filter the wells whose cq value is null
        public Path<BooleanBeed> createPath(WellBeanBeed startBeed) {
          // construct the ==
          BooleanNullBeed nullBeed = new BooleanNullBeed();
          nullBeed.setOperandPath(Paths.path(startBeed.cq));
          return new ConstantPath<BooleanBeed>(nullBeed);
        }
    };
    $filteredSetBeed = new MyNewFilteredSetBeed(criterion);
    $filteredSetBeed.setSourcePath(sourcePath);
    result = $filteredSetBeed.get();
    assertEquals(result.size(), 1);
    assertTrue(result.contains($wellNull));
    iterator = result.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(iterator.next(), $wellNull);
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
    sourcePath = null;
    $filteredSetBeed.setSourcePath(sourcePath);
    result = $filteredSetBeed.get();
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

  private EditableSetBeed<WellBeanBeed> createSource() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<WellBeanBeed> setBeed =
      new EditableSetBeed<WellBeanBeed>();
    // add beeds to set
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($well1);
    setEdit.perform();
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($well2);
    setEdit.perform();
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($well3);
    setEdit.perform();
    setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($wellNull);
    setEdit.perform();
    return setBeed;
  }

  private EditableSetBeed<WellBeanBeed> createOtherSource() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<WellBeanBeed> setBeed =
      new EditableSetBeed<WellBeanBeed>();
    // add beeds to set
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(setBeed);
    setEdit.addElementToAdd($well0);
    setEdit.perform();
    return setBeed;
  }

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

  @Test
  public void getSizeAndCardinality() throws EditStateException, IllegalEditException {
    // add a listener to the size beed
    IntegerBeed<ActualLongEvent> sizeBeed = $filteredSetBeed.getSize();
    sizeBeed.addListener($listener5);
    assertNull($listener5.$event);
    // check the size
    assertEquals($filteredSetBeed.getSize().getLong(), 0L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 0L);
    // add source
    EditableSetBeed<WellBeanBeed> source = createSource();
    Path<? extends SetBeed<WellBeanBeed, ?>> sourcePath =
      new ConstantPath<SetBeed<WellBeanBeed,?>>(source);
    $filteredSetBeed.setSourcePath(sourcePath);
    // check the size
    assertEquals($filteredSetBeed.getSize().getLong(), 1L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 1L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 0L);
    assertEquals($listener5.$event.getNewLong(), 1L);
    assertEquals($listener5.$event.getEdit(), null);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add elements
    SetEdit<WellBeanBeed> setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToAdd(createWellBeanBeed(5L));
    setEdit.addElementToAdd(createWellBeanBeed(6L));
    setEdit.perform();
    // check the size
    assertEquals($filteredSetBeed.getSize().getLong(), 2L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 2L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 1L);
    assertEquals($listener5.$event.getNewLong(), 2L);
    assertEquals($listener5.$event.getEdit(), setEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove($well1);
    setEdit.perform();
    // check the size
    assertEquals($filteredSetBeed.getSize().getLong(), 2L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 2L);
    // check the listener (size has not changed, so no events should be sent)
    assertNull($listener5.$event);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements
    setEdit = new SetEdit<WellBeanBeed>(source);
    setEdit.addElementToRemove($well2);
    setEdit.perform();
    // check the size
    assertEquals($filteredSetBeed.getSize().getLong(), 1L);
    assertEquals($filteredSetBeed.getCardinality().getLong(), 1L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 2L);
    assertEquals($listener5.$event.getNewLong(), 1L);
    assertEquals($listener5.$event.getEdit(), setEdit);
  }

}