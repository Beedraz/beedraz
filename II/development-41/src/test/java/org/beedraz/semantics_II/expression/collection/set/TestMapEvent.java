/*<license>
 Copyright 2007 - $Date: 2007/04/23 16:00:26 $ by the authors mentioned below.

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
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestMapEvent {

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  @Test
  public void constructor() throws EditStateException, IllegalEditException {
    // source
    MapBeed<String, Integer, ?> source = new EditableMapBeed<String, Integer>();
    // added and removed elements (null)
    Map<String, Integer> addedElements = null;
    Map<String, Integer> removedElements = null;
    // edit
    EditableMapBeed<String, Integer> target = new EditableMapBeed<String, Integer>();
    MapEdit<String, Integer> edit = new MapEdit<String, Integer>(target);
    edit.perform();
    // test constructor
    MapEvent<String, Integer> mapEvent =
      new ActualMapEvent<String, Integer>(source, addedElements, removedElements, edit);
    assertEquals(mapEvent.getSource(), source);
    assertTrue(mapEvent.getAddedElements().isEmpty());
    assertTrue(mapEvent.getRemovedElements().isEmpty());
    assertEquals(mapEvent.getEdit(), edit);
    assertEquals(mapEvent.getEditState(), edit.getState());
    // added and removed elements (effective)
    addedElements = new HashMap<String, Integer>();
    String addedKey = "horse";
    Integer addedValue = 4;
    addedElements.put(addedKey, addedValue);
    removedElements = new HashMap<String, Integer>();
    String removedKey1 = "cow";
    Integer removedValue1 = 5;
    String removedKey2 = "sheep";
    Integer removedValue2 = 6;
    removedElements.put(removedKey1, removedValue1);
    removedElements.put(removedKey2, removedValue2);
    // test constructor
    mapEvent = new ActualMapEvent<String, Integer>(source, addedElements, removedElements, edit);
    assertEquals(mapEvent.getSource(), source);
    assertTrue(mapEvent.getAddedElements().size() == 1);
    assertTrue(mapEvent.getAddedElements().keySet().contains(addedKey));
    assertEquals(addedValue, mapEvent.getAddedElements().get(addedKey));
    assertTrue(mapEvent.getRemovedElements().size() == 2);
    assertTrue(mapEvent.getRemovedElements().keySet().contains(removedKey1));
    assertEquals(removedValue1, mapEvent.getRemovedElements().get(removedKey1));
    assertTrue(mapEvent.getRemovedElements().keySet().contains(removedKey2));
    assertEquals(removedValue2, mapEvent.getRemovedElements().get(removedKey2));
    assertEquals(mapEvent.getEdit(), edit);
    assertEquals(mapEvent.getEditState(), edit.getState());
  }

}