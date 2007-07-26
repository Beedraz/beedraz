/*<license>
 Copyright 2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $ by the authors mentioned below.

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


import static org.beedraz.semantics_II.expression.number.integer.long64.LongBeeds.editableLongBeed;
import static org.beedraz.semantics_II.expression.string.StringBeeds.editableStringBeed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Set;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.ActualLongEvent;
import org.beedraz.semantics_II.expression.number.integer.long64.EditableLongBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.LongBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.LongEdit;
import org.beedraz.semantics_II.expression.string.EditableStringBeed;
import org.beedraz.semantics_II.expression.string.StringBeed;
import org.beedraz.semantics_II.path.ConstantPath;
import org.beedraz.semantics_II.path.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 913 $",
         date     = "$Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $")
public class TestCartesianProductBeed {

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $cartesianProductBeed = new CartesianProductBeed<StringBeed, LongBeed>($owner);
    $listener3 = new StubListener<SetEvent<CartesianProductBeed<StringBeed, LongBeed>.Tuple>>();
    $listener5 = new StubListener<ActualLongEvent>();
    $stringBeed1 = editableStringBeed("stringBeed1", $owner);
    $stringBeed2 = editableStringBeed("stringBeed2", $owner);
    $longBeed1 = editableLongBeed(0L, $owner);
    $longBeed2 = editableLongBeed(1L, $owner);
    LongEdit longEdit = new LongEdit($longBeed1);
    longEdit.setGoal(0L);
    longEdit.perform();
    longEdit = new LongEdit($longBeed2);
    longEdit.setGoal(1L);
    longEdit.perform();
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  private StubBeanBeed $owner;
  private CartesianProductBeed<StringBeed, LongBeed> $cartesianProductBeed;
  private StubListener<SetEvent<CartesianProductBeed<StringBeed, LongBeed>.Tuple>> $listener3;
  private StubListener<ActualLongEvent> $listener5;
  private EditableStringBeed $stringBeed1;
  private EditableStringBeed $stringBeed2;
  private EditableLongBeed $longBeed1;
  private EditableLongBeed $longBeed2;

  @Test
  public void constructor() {
    assertEquals($cartesianProductBeed.getSourcePath1(), null);
    assertEquals($cartesianProductBeed.getSourcePath2(), null);
    assertEquals($cartesianProductBeed.getSource1(), null);
    assertEquals($cartesianProductBeed.getSource2(), null);
    assertTrue($cartesianProductBeed.get().isEmpty());
  }

  /**
   * Source1 is null.
   */
  @Test
  public void setSourcePath1A() throws EditStateException, IllegalEditException {
    // register listeners
    $cartesianProductBeed.addListener($listener3);
    assertNull($listener3.$event);
    // set the sourcePath
    Path<? extends SetBeed<StringBeed, ?>> sourcePath1 = null;
    $cartesianProductBeed.setSourcePath1(sourcePath1);
    // source path and source should be set, beed should be updated
    assertEquals($cartesianProductBeed.getSourcePath1(), sourcePath1);
    assertEquals($cartesianProductBeed.getSource1(), null);
    assertTrue($cartesianProductBeed.get().isEmpty());
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Source2 is null.
   */
  @Test
  public void setSourcePath2A() throws EditStateException, IllegalEditException {
    // register listeners
    $cartesianProductBeed.addListener($listener3);
    assertNull($listener3.$event);
    // set the sourcePath
    Path<? extends SetBeed<LongBeed, ?>> sourcePath2 = null;
    $cartesianProductBeed.setSourcePath2(sourcePath2);
    // source path and source should be set, beed should be updated
    assertEquals($cartesianProductBeed.getSourcePath2(), sourcePath2);
    assertEquals($cartesianProductBeed.getSource2(), null);
    assertTrue($cartesianProductBeed.get().isEmpty());
    // value has not changed, so the listeners are not notified
    assertNull($listener3.$event);
  }

  /**
   * Both sources are effective.
   */
  @Test
  public void setSourcePathB() throws EditStateException, IllegalEditException {
    // register listeners
    $cartesianProductBeed.addListener($listener3);
    assertNull($listener3.$event);
    // set the source paths
    // source path 1
    EditableSetBeed<StringBeed> source1 = createSource1();
    Path<? extends SetBeed<StringBeed, ?>> sourcePath1 =
      new ConstantPath<SetBeed<StringBeed,?>>(source1);
    $cartesianProductBeed.setSourcePath1(sourcePath1);
    // source path 1 and source 1 should be set, beed should be updated
    assertEquals($cartesianProductBeed.getSourcePath1(), sourcePath1);
    assertEquals($cartesianProductBeed.getSource1(), source1);
    assertEquals($cartesianProductBeed.get().size(), 0); // since source2 contains no elements
    // source path 2
    EditableSetBeed<LongBeed> source2 = createSource2();
    Path<? extends SetBeed<LongBeed, ?>> sourcePath2 =
      new ConstantPath<SetBeed<LongBeed,?>>(source2);
    $cartesianProductBeed.setSourcePath2(sourcePath2);
    // source path 2 and source 2 should be set, beed should be updated
    assertEquals($cartesianProductBeed.getSourcePath2(), sourcePath2);
    assertEquals($cartesianProductBeed.getSource2(), source2);
    assertEquals($cartesianProductBeed.get().size(), 4);
    assertTrue(containsTuple($stringBeed1, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed1, $longBeed2, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed2, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed2, $longBeed2, $cartesianProductBeed.get()));
    // value has changed, so the listeners of the beed are notified
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $cartesianProductBeed);
    assertEquals($listener3.$event.getAddedElements().size(), 4);
    assertTrue(containsTuple($stringBeed1, $longBeed1, $listener3.$event.getAddedElements()));
    assertTrue(containsTuple($stringBeed1, $longBeed2, $listener3.$event.getAddedElements()));
    assertTrue(containsTuple($stringBeed2, $longBeed1, $listener3.$event.getAddedElements()));
    assertTrue(containsTuple($stringBeed2, $longBeed2, $listener3.$event.getAddedElements()));
    assertEquals($listener3.$event.getRemovedElements().size(), 0);
    assertEquals($listener3.$event.getEdit(), null);
    // the beed is registered as listener of the source path,
    // so when the source path changes
    // @mudo How can I construct a path to a source that can change?
    // The beed is registered as listener of the sources, so when
    // one of the sources changes, the beed should be notified
    $listener3.reset();
    assertNull($listener3.$event);
    StringBeed stringBeed = editableStringBeed("extra stringBeed", $owner);
    SetEdit<StringBeed> setEdit = new SetEdit<StringBeed>(source1);
    setEdit.addElementToAdd(stringBeed);
    setEdit.perform();
    assertEquals($cartesianProductBeed.get().size(), 6);
    assertTrue(containsTuple($stringBeed1, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed1, $longBeed2, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed2, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed2, $longBeed2, $cartesianProductBeed.get()));
    assertTrue(containsTuple(stringBeed, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple(stringBeed, $longBeed2, $cartesianProductBeed.get()));
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $cartesianProductBeed);
    assertEquals($listener3.$event.getAddedElements().size(), 2);
    assertTrue(containsTuple(stringBeed, $longBeed1, $listener3.$event.getAddedElements()));
    assertTrue(containsTuple(stringBeed, $longBeed2, $listener3.$event.getAddedElements()));
    assertEquals($listener3.$event.getRemovedElements().size(), 0);
    assertEquals($listener3.$event.getEdit(), setEdit);
    // Change the second source.
    $listener3.reset();
    assertNull($listener3.$event);
    SetEdit<LongBeed> setEdit2 = new SetEdit<LongBeed>(source2);
    setEdit2.addElementToRemove($longBeed1);
    setEdit2.perform();
    assertEquals($cartesianProductBeed.get().size(), 3);
    assertTrue(containsTuple($stringBeed1, $longBeed2, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed2, $longBeed2, $cartesianProductBeed.get()));
    assertTrue(containsTuple(stringBeed, $longBeed2, $cartesianProductBeed.get()));
    assertNotNull($listener3.$event);
    assertEquals($listener3.$event.getSource(), $cartesianProductBeed);
    assertEquals($listener3.$event.getAddedElements().size(), 0);
    assertEquals($listener3.$event.getRemovedElements().size(), 3);
    assertTrue(containsTuple($stringBeed1, $longBeed1, $listener3.$event.getRemovedElements()));
    assertTrue(containsTuple($stringBeed2, $longBeed1, $listener3.$event.getRemovedElements()));
    assertTrue(containsTuple(stringBeed, $longBeed1, $listener3.$event.getRemovedElements()));
    assertEquals($listener3.$event.getEdit(), setEdit2);
  }

  private boolean containsTuple(StringBeed stringBeed, LongBeed longBeed,
      Set<CartesianProductBeed<StringBeed, LongBeed>.Tuple> tuples) {
    for (CartesianProductBeed<StringBeed, LongBeed>.Tuple tuple : tuples) {
      if (tuple.getElement1() == stringBeed && tuple.getElement2() == longBeed) {
        return true;
      }
    }
    return false;
  }

  /**
   * Are update sources removed properly?
   */
  @Test
  public void setSourcePathC() throws EditStateException, IllegalEditException {
    // register listeners
    $cartesianProductBeed.addListener($listener3);
    assertNull($listener3.$event);
    // set the source paths
    EditableSetBeed<StringBeed> source1 = createSource1();
    Path<? extends SetBeed<StringBeed, ?>> sourcePath1 =
      new ConstantPath<SetBeed<StringBeed,?>>(source1);
    $cartesianProductBeed.setSourcePath1(sourcePath1);
    EditableSetBeed<LongBeed> source2 = createSource2();
    Path<? extends SetBeed<LongBeed, ?>> sourcePath2 =
      new ConstantPath<SetBeed<LongBeed,?>>(source2);
    $cartesianProductBeed.setSourcePath2(sourcePath2);
    // source paths and sources should be set, beed should be updated
    assertEquals($cartesianProductBeed.getSourcePath1(), sourcePath1);
    assertEquals($cartesianProductBeed.getSourcePath2(), sourcePath2);
    assertEquals($cartesianProductBeed.getSource1(), source1);
    assertEquals($cartesianProductBeed.getSource2(), source2);
    assertEquals($cartesianProductBeed.get().size(), 4);
    assertTrue(containsTuple($stringBeed1, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed1, $longBeed2, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed2, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed2, $longBeed2, $cartesianProductBeed.get()));

    // when the source path is replaced by another one, the old source path should be removed
    // as update source
    EditableSetBeed<StringBeed> otherSource1 = createOtherSource1();
    Path<? extends SetBeed<StringBeed, ?>> otherSourcePath1 =
      new ConstantPath<SetBeed<StringBeed,?>>(otherSource1);
    $cartesianProductBeed.setSourcePath1(otherSourcePath1);
    // source path and source should be set, beed should be updated
    assertEquals($cartesianProductBeed.getSourcePath1(), otherSourcePath1);
    assertEquals($cartesianProductBeed.getSourcePath2(), sourcePath2);
    assertEquals($cartesianProductBeed.getSource1(), otherSource1);
    assertEquals($cartesianProductBeed.getSource2(), source2);
    assertEquals($cartesianProductBeed.get().size(), 2);
    assertTrue(containsTuple($stringBeed1, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed1, $longBeed2, $cartesianProductBeed.get()));

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
    SetEdit<StringBeed> setEdit = new SetEdit<StringBeed>(source1);
    setEdit.addElementToRemove($stringBeed1);
    setEdit.perform();
    // listener is not notified
    assertNull($listener3.$event);
    // beed has not changed
    assertEquals($cartesianProductBeed.getSourcePath1(), otherSourcePath1);
    assertEquals($cartesianProductBeed.getSourcePath2(), sourcePath2);
    assertEquals($cartesianProductBeed.getSource1(), otherSource1);
    assertEquals($cartesianProductBeed.getSource2(), source2);
    assertEquals($cartesianProductBeed.get().size(), 2);
    assertTrue(containsTuple($stringBeed1, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed1, $longBeed2, $cartesianProductBeed.get()));

  }

  @Test
  public void get() throws EditStateException, IllegalEditException {
    // set the source paths
    EditableSetBeed<StringBeed> source1 = createSource1();
    Path<? extends SetBeed<StringBeed, ?>> sourcePath1 =
      new ConstantPath<SetBeed<StringBeed,?>>(source1);
    $cartesianProductBeed.setSourcePath1(sourcePath1);
    EditableSetBeed<LongBeed> source2 = createSource2();
    Path<? extends SetBeed<LongBeed, ?>> sourcePath2 =
      new ConstantPath<SetBeed<LongBeed,?>>(source2);
    $cartesianProductBeed.setSourcePath2(sourcePath2);
    // check the result
    assertEquals($cartesianProductBeed.getSourcePath1(), sourcePath1);
    assertEquals($cartesianProductBeed.getSourcePath2(), sourcePath2);
    assertEquals($cartesianProductBeed.getSource1(), source1);
    assertEquals($cartesianProductBeed.getSource2(), source2);
    assertEquals($cartesianProductBeed.get().size(), 4);
    assertTrue(containsTuple($stringBeed1, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed1, $longBeed2, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed2, $longBeed1, $cartesianProductBeed.get()));
    assertTrue(containsTuple($stringBeed2, $longBeed2, $cartesianProductBeed.get()));
    // source1 = null
    $cartesianProductBeed.setSourcePath1(null);
    assertEquals($cartesianProductBeed.getSourcePath1(), null);
    assertEquals($cartesianProductBeed.getSourcePath2(), sourcePath2);
    assertEquals($cartesianProductBeed.getSource1(), null);
    assertEquals($cartesianProductBeed.getSource2(), source2);
    assertEquals($cartesianProductBeed.get().size(), 0);
    // source2 = null
    $cartesianProductBeed.setSourcePath2(null);
    assertEquals($cartesianProductBeed.getSourcePath1(), null);
    assertEquals($cartesianProductBeed.getSourcePath2(), null);
    assertEquals($cartesianProductBeed.getSource1(), null);
    assertEquals($cartesianProductBeed.getSource2(), null);
    assertEquals($cartesianProductBeed.get().size(), 0);
  }

  private EditableSetBeed<StringBeed> createSource1() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<StringBeed> setBeed =
      new EditableSetBeed<StringBeed>($owner);
    // add beeds to set
    SetEdit<StringBeed> setEdit = new SetEdit<StringBeed>(setBeed);
    setEdit.addElementToAdd($stringBeed1);
    setEdit.perform();
    setEdit = new SetEdit<StringBeed>(setBeed);
    setEdit.addElementToAdd($stringBeed2);
    setEdit.perform();
    return setBeed;
  }

  private EditableSetBeed<StringBeed> createOtherSource1() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<StringBeed> setBeed =
      new EditableSetBeed<StringBeed>($owner);
    // add beeds to set
    SetEdit<StringBeed> setEdit = new SetEdit<StringBeed>(setBeed);
    setEdit.addElementToAdd($stringBeed1);
    setEdit.perform();
    return setBeed;
  }
  private EditableSetBeed<LongBeed> createSource2() throws EditStateException, IllegalEditException {
    // create set beed
    EditableSetBeed<LongBeed> setBeed =
      new EditableSetBeed<LongBeed>($owner);
    // add beeds to set
    SetEdit<LongBeed> setEdit = new SetEdit<LongBeed>(setBeed);
    setEdit.addElementToAdd($longBeed1);
    setEdit.perform();
    setEdit = new SetEdit<LongBeed>(setBeed);
    setEdit.addElementToAdd($longBeed2);
    setEdit.perform();
    return setBeed;
  }

  @Test
  public void getSizeAndCardinality() throws EditStateException, IllegalEditException {
    // add a listener to the size beed
    IntegerBeed<ActualLongEvent> sizeBeed = $cartesianProductBeed.getSize();
    sizeBeed.addListener($listener5);
    assertNull($listener5.$event);
    // check the size
    assertEquals($cartesianProductBeed.getSize().getLong(), 0L);
    assertEquals($cartesianProductBeed.getCardinality().getLong(), 0L);
    // add sources
    EditableSetBeed<StringBeed> source1 = createSource1();
    Path<? extends SetBeed<StringBeed, ?>> sourcePath1 =
      new ConstantPath<SetBeed<StringBeed,?>>(source1);
    $cartesianProductBeed.setSourcePath1(sourcePath1);
    EditableSetBeed<LongBeed> source2 = createSource2();
    Path<? extends SetBeed<LongBeed, ?>> sourcePath2 =
      new ConstantPath<SetBeed<LongBeed,?>>(source2);
    $cartesianProductBeed.setSourcePath2(sourcePath2);
    // check the size (2*2)
    assertEquals($cartesianProductBeed.getSize().getLong(), 4L);
    assertEquals($cartesianProductBeed.getCardinality().getLong(), 4L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 0L);
    assertEquals($listener5.$event.getNewLong(), 4L);
    assertEquals($listener5.$event.getEdit(), null);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // add elements
    SetEdit<StringBeed> setEdit = new SetEdit<StringBeed>(source1);
    setEdit.addElementToAdd(editableStringBeed("stringBeed3", $owner));
    setEdit.addElementToAdd(editableStringBeed("stringBeed3", $owner));
    setEdit.perform();
    // check the size (4*2)
    assertEquals($cartesianProductBeed.getSize().getLong(), 8L);
    assertEquals($cartesianProductBeed.getCardinality().getLong(), 8L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 4L);
    assertEquals($listener5.$event.getNewLong(), 8L);
    assertEquals($listener5.$event.getEdit(), setEdit);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove/add elements (4*2)
    setEdit = new SetEdit<StringBeed>(source1);
    setEdit.addElementToAdd(editableStringBeed("stringBeed5", $owner));
    setEdit.addElementToRemove($stringBeed2);
    setEdit.perform();
    // check the size
    assertEquals($cartesianProductBeed.getSize().getLong(), 8L);
    assertEquals($cartesianProductBeed.getCardinality().getLong(), 8L);
    // check the listener (size has not changed, so no events should be sent)
    assertNull($listener5.$event);
    // reset
    $listener5.reset();
    assertNull($listener5.$event);
    // remove elements (4*1)
    SetEdit<LongBeed> setEdit2 = new SetEdit<LongBeed>(source2);
    setEdit2.addElementToRemove($longBeed1);
    setEdit2.perform();
    // check the size
    assertEquals($cartesianProductBeed.getSize().getLong(), 4L);
    assertEquals($cartesianProductBeed.getCardinality().getLong(), 4L);
    // check the listener
    assertNotNull($listener5.$event);
    assertEquals($listener5.$event.getSource(), sizeBeed);
    assertEquals($listener5.$event.getOldLong(), 8L);
    assertEquals($listener5.$event.getNewLong(), 4L);
    assertEquals($listener5.$event.getEdit(), setEdit2);
  }

}

