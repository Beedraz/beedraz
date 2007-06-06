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


import static org.beedraz.semantics_II.expression.number.integer.long64.LongBeeds.editableLongBeed;
import static org.beedraz.semantics_II.path.Paths.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.LongBeed;
import org.beedraz.semantics_II.path.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestMapEvent {

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    $owner = new StubBeanBeed();
  }

  @After
  public void tearDown() throws Exception {
    $owner = null;
  }

  private StubBeanBeed $owner;

  @Test
  public void constructor() throws EditStateException, IllegalEditException {
    // source
    MapBeed<String, LongBeed, ?> source =
      new EditableMapBeed<String, LongBeed>($owner);
    // added and removed elements (null)
    Map<String, Path<LongBeed>> addedElements = null;
    Map<String, Path<LongBeed>> removedElements = null;
    // edit
    EditableMapBeed<String, LongBeed> target =
      new EditableMapBeed<String, LongBeed>($owner);
    MapEdit<String, LongBeed> edit =
      new MapEdit<String, LongBeed>(target);
    edit.perform();
    // test constructor
    MapEvent<String, LongBeed> mapEvent =
      new ActualMapEvent<String, LongBeed>(
          source, addedElements, removedElements, edit);
    assertEquals(mapEvent.getSource(), source);
    assertTrue(mapEvent.getAddedElements().isEmpty());
    assertTrue(mapEvent.getRemovedElements().isEmpty());
    assertEquals(mapEvent.getEdit(), edit);
    assertEquals(mapEvent.getEditState(), edit.getState());
    // added and removed elements (effective)
    addedElements = new HashMap<String, Path<LongBeed>>();
    String addedKey = "horse";
    LongBeed addedValue = editableLongBeed(2L, $owner);
    addedElements.put(addedKey, path(addedValue));
    removedElements = new HashMap<String, Path<LongBeed>>();
    String removedKey1 = "cow";
    LongBeed removedValue1 = editableLongBeed(5L, $owner);
    String removedKey2 = "sheep";
    LongBeed removedValue2 = editableLongBeed(6L, $owner);
    removedElements.put(removedKey1, path(removedValue1));
    removedElements.put(removedKey2, path(removedValue2));
    // test constructor
    mapEvent = new ActualMapEvent<String, LongBeed>(source, addedElements, removedElements, edit);
    assertEquals(mapEvent.getSource(), source);
    assertTrue(mapEvent.getAddedElements().size() == 1);
    assertTrue(mapEvent.getAddedElements().keySet().contains(addedKey));
    assertEquals(addedValue, mapEvent.getAddedElements().get(addedKey).get());
    assertTrue(mapEvent.getRemovedElements().size() == 2);
    assertTrue(mapEvent.getRemovedElements().keySet().contains(removedKey1));
    assertEquals(removedValue1, mapEvent.getRemovedElements().get(removedKey1).get());
    assertTrue(mapEvent.getRemovedElements().keySet().contains(removedKey2));
    assertEquals(removedValue2, mapEvent.getRemovedElements().get(removedKey2).get());
    assertEquals(mapEvent.getEdit(), edit);
    assertEquals(mapEvent.getEditState(), edit.getState());
  }

}