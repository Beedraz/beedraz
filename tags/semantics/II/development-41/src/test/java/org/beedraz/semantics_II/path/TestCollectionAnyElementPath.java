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

package org.beedraz.semantics_II.path;


import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.expression.collection.CollectionBeed;
import org.beedraz.semantics_II.expression.collection.set.EditableSetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEdit;
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
public class TestCollectionAnyElementPath {

  @Before
  public void setUp() throws Exception {
    $element1 = new StubBeanBeed();
    $element2 = new StubBeanBeed();
    $collectionBeed = createCollection($element1, $element2);
    $collectionBeedPath = path($collectionBeed);
    $collectionAnyElementPath = new CollectionAnyElementPath<StubBeanBeed>($collectionBeedPath);
    $listener1 = new StubListener<PathEvent<StubBeanBeed>>();
  }

  @After
  public void tearDown() throws Exception {
    $element1 = null;
    $element2 = null;
    $collectionBeed = null;
    $collectionBeedPath = null;
    $collectionAnyElementPath = null;
    $listener1 = null;
  }

  private StubBeanBeed $element1;
  private StubBeanBeed $element2;
  private CollectionAnyElementPath<StubBeanBeed> $collectionAnyElementPath;
  private Path<? extends CollectionBeed<StubBeanBeed, ?>> $collectionBeedPath;
  private EditableSetBeed<StubBeanBeed> $collectionBeed;
  private StubListener<PathEvent<StubBeanBeed>> $listener1;

  @Test
  public void constructor() {
    assertEquals($collectionAnyElementPath.getCollectionBeedPath(), $collectionBeedPath);
    assertEquals($collectionAnyElementPath.getCollectionBeed(), $collectionBeed);
    assertTrue($collectionAnyElementPath.get() == $element1 ||
               $collectionAnyElementPath.get() == $element2);
  }

  /**
   * The path is registered as listener of the collection beed path and of the collection
   * when they change, the path should be updated
   */
  @Test
  public void get() throws EditStateException, IllegalEditException {
    // $collectionBeed = {$element1, $element2}
    assertTrue($collectionAnyElementPath.get() == $element1 ||
               $collectionAnyElementPath.get() == $element2);
    // add a listener to the path
    $collectionAnyElementPath.addListener($listener1);
    assertNull($listener1.$event);
    // change the collection beed
    StubBeanBeed oldSelected = $collectionAnyElementPath.get();
    SetEdit<StubBeanBeed> removeEdit = removeElement($collectionBeed, $element2);
    // $collectionBeed = {$element1}
    assertTrue($collectionAnyElementPath.get() == $element1);
    if (oldSelected != $element1) { // the selected element was removed
      assertNotNull($listener1.$event);
      assertEquals(removeEdit, $listener1.$event.getEdit());
      assertEquals(oldSelected, $listener1.$event.getOldValue());
      assertEquals($element1, $listener1.$event.getNewValue());
      assertEquals($collectionAnyElementPath, $listener1.$event.getSource());
    }
    else {
      assertNull($listener1.$event); // the selected beed has not changed!
    }
    // change the collection beed
    $listener1.reset();
    removeEdit = removeElement($collectionBeed, $element1);
    // $collectionBeed = { }
    assertTrue($collectionAnyElementPath.get() == null);
    assertNotNull($listener1.$event);
    assertEquals(removeEdit, $listener1.$event.getEdit());
    assertEquals($element1, $listener1.$event.getOldValue());
    assertEquals(null, $listener1.$event.getNewValue());
    assertEquals($collectionAnyElementPath, $listener1.$event.getSource());
    // change the collection beed
    $listener1.reset();
    SetEdit<StubBeanBeed> addEdit = addElement($collectionBeed, $element1);
    // $collectionBeed = {$element1}
    assertTrue($collectionAnyElementPath.get() == $element1);
    assertNotNull($listener1.$event);
    assertEquals(addEdit, $listener1.$event.getEdit());
    assertEquals(null, $listener1.$event.getOldValue());
    assertEquals($element1, $listener1.$event.getNewValue());
    assertEquals($collectionAnyElementPath, $listener1.$event.getSource());
    // change the collection beed
    $listener1.reset();
    addEdit = addElement($collectionBeed, $element2);
    // $collectionBeed = {$element1, $element2}
    assertTrue($collectionAnyElementPath.get() == $element1 ||
               $collectionAnyElementPath.get() == $element2);
    assertNull($listener1.$event); // when adding an element to a not-empty source, the selected element is not changed

    // mudo: can we construct a path to a collection beed that can be changed?
  }

  private EditableSetBeed<StubBeanBeed> createCollection(StubBeanBeed... elements) throws EditStateException, IllegalEditException {
    EditableSetBeed<StubBeanBeed> setBeed = new EditableSetBeed<StubBeanBeed>();
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
