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

package org.beedraz.semantics_II.path;


import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.expression.collection.CollectionBeed;
import org.beedraz.semantics_II.expression.collection.SizePath;
import org.beedraz.semantics_II.expression.collection.set.EditableSetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEdit;
import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 913 $",
         date     = "$Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $")
public class TestSizePath {

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
    $element1 = new StubBeanBeed();
    $element2 = new StubBeanBeed();
    $collectionBeed = createCollection($element1, $element2);
    $collectionBeedPath = path($collectionBeed);
    $sizePath = new SizePath($collectionBeedPath);
    $listener1 = new StubListener<PathEvent<IntegerBeed<?>>>();
  }

  @After
  public void tearDown() throws Exception {
    $owner = null;
    $element1 = null;
    $element2 = null;
    $collectionBeed = null;
    $collectionBeedPath = null;
    $sizePath = null;
    $listener1 = null;
  }

  private StubBeanBeed $owner;
  private StubBeanBeed $element1;
  private StubBeanBeed $element2;
  private SizePath $sizePath;
  private Path<? extends CollectionBeed<StubBeanBeed, ?>> $collectionBeedPath;
  private EditableSetBeed<StubBeanBeed> $collectionBeed;
  private StubListener<PathEvent<IntegerBeed<?>>> $listener1;

  @Test
  public void constructor() {
    assertEquals($sizePath.getCollectionBeedPath(), $collectionBeedPath);
    assertEquals($sizePath.getCollectionBeed(), $collectionBeed);
    assertTrue($sizePath.get() == $collectionBeed.getSize());
  }

  /**
   * The path is registered as listener of the collection.
   * When the collection changes, the size path still returns the same size beed.
   */
  @Test
  public void get1() throws EditStateException, IllegalEditException {
    IntegerBeed<?> sizeBeed = $collectionBeed.getSize();
    // $collectionBeed = {$element1, $element2}
    assertTrue($sizePath.get() == sizeBeed);
    // add a listener to the path
    $sizePath.addListener($listener1);
    assertNull($listener1.$event);
    // change the collection beed
    removeElement($collectionBeed, $element2);
    // $collectionBeed = {$element1}
    assertTrue($sizePath.get() == sizeBeed);
    assertNull($listener1.$event);
  }

  /**
   * The path is registered as listener of the collection beed path.
   * When the path references another collection, the size path will return the
   * corresponding size beed.
   */
  @Test
  public void get2() throws EditStateException, IllegalEditException {
    MutablePath<CollectionBeed<StubBeanBeed, ?>> mutableCollectionBeedPath =
      new MutablePath<CollectionBeed<StubBeanBeed,?>>();
    mutableCollectionBeedPath.set($collectionBeed);
    $sizePath = new SizePath(mutableCollectionBeedPath);
    // $collectionBeed = {$element1, $element2}
    assertTrue($sizePath.get() == $collectionBeed.getSize());
    // add a listener to the path
    $sizePath.addListener($listener1);
    assertNull($listener1.$event);
    // change the collection beed path
    CollectionBeed<StubBeanBeed, ?> newCollectionBeed =
      createCollection($element1);
    mutableCollectionBeedPath.set(newCollectionBeed);
    // $collectionBeed = {$element1}
    assertTrue($sizePath.get() == newCollectionBeed.getSize());
    assertNotNull($listener1.$event);
    assertEquals(null, $listener1.$event.getEdit());
    assertEquals($collectionBeed.getSize(), $listener1.$event.getOldValue());
    assertEquals(newCollectionBeed.getSize(), $listener1.$event.getNewValue());
    assertEquals($sizePath, $listener1.$event.getSource());
    // change the collection beed path again
    mutableCollectionBeedPath.set($collectionBeed);
    // $collectionBeed = {$element1}
    assertTrue($sizePath.get() == $collectionBeed.getSize());
    assertNotNull($listener1.$event);
    assertEquals(null, $listener1.$event.getEdit());
    assertEquals(newCollectionBeed.getSize(), $listener1.$event.getOldValue());
    assertEquals($collectionBeed.getSize(), $listener1.$event.getNewValue());
    assertEquals($sizePath, $listener1.$event.getSource());
  }

  private EditableSetBeed<StubBeanBeed> createCollection(StubBeanBeed... elements) throws EditStateException, IllegalEditException {
    EditableSetBeed<StubBeanBeed> setBeed = new EditableSetBeed<StubBeanBeed>($owner);
    for (StubBeanBeed element : elements) {
      addElement(setBeed, element);
    }
    return setBeed;
  }

  private SetEdit<StubBeanBeed> addElement(EditableSetBeed<StubBeanBeed> setBeed, StubBeanBeed element) throws EditStateException, IllegalEditException {
    SetEdit<StubBeanBeed> setEdit = new SetEdit<StubBeanBeed>(setBeed);
    setEdit.addElementToAdd(element);
    setEdit.perform();
    return setEdit;
  }

  private SetEdit<StubBeanBeed> removeElement(EditableSetBeed<StubBeanBeed> setBeed, StubBeanBeed element) throws EditStateException, IllegalEditException {
    SetEdit<StubBeanBeed> setEdit = new SetEdit<StubBeanBeed>(setBeed);
    setEdit.addElementToRemove(element);
    setEdit.perform();
    return setEdit;
  }
}
