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
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.HashSet;
import java.util.Set;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.bean.StubBeanBeed;
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
public class TestSetEvent {

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
    SetBeed<Integer, ?> source = new EditableSetBeed<Integer>($owner);
    // old and new value
    Set<Integer> addedElements = null;
    Set<Integer> removedElements = null;
    // edit
    EditableSetBeed<Integer> target = new EditableSetBeed<Integer>($owner);
    SetEdit<Integer> edit = new SetEdit<Integer>(target);
    edit.perform();
    // test constructor
    SetEvent<Integer> setEvent =
      new ActualSetEvent<Integer>(source, addedElements, removedElements, edit);
    assertEquals(setEvent.getSource(), source);
    assertTrue(setEvent.getAddedElements().isEmpty());
    assertTrue(setEvent.getRemovedElements().isEmpty());
    assertEquals(setEvent.getEdit(), edit);
    assertEquals(setEvent.getEditState(), edit.getState());
    // old and new value
    addedElements = new HashSet<Integer>();
    Integer added = 4;
    addedElements.add(added);
    removedElements = new HashSet<Integer>();
    Integer removed1 = 5;
    Integer removed2 = 6;
    removedElements.add(removed1);
    removedElements.add(removed2);
    // test constructor
    setEvent = new ActualSetEvent<Integer>(source, addedElements, removedElements, edit);
    assertEquals(setEvent.getSource(), source);
    assertTrue(setEvent.getAddedElements().size() == 1);
    assertTrue(setEvent.getAddedElements().contains(added));
    assertTrue(setEvent.getRemovedElements().size() == 2);
    assertTrue(setEvent.getRemovedElements().contains(removed1));
    assertTrue(setEvent.getRemovedElements().contains(removed2));
    assertEquals(setEvent.getEdit(), edit);
    assertEquals(setEvent.getEditState(), edit.getState());
  }

}