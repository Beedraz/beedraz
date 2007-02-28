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

package org.beedra_II.property.list;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.association.sorted.SortedBidirToManyBeed;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerEdit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestListEvent {

  public class MyBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class OneBeanBeed extends AbstractBeanBeed {
    // NOP
  }

  public class ManyBeanBeed extends AbstractBeanBeed {
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
    OneBeanBeed owner = new OneBeanBeed();
    ListBeed<ManyBeanBeed> source =
      new SortedBidirToManyBeed<OneBeanBeed, ManyBeanBeed>(owner);
    // old and new value
    List<ManyBeanBeed> oldValue = new ArrayList<ManyBeanBeed>();
    oldValue.add(new ManyBeanBeed());
    List<ManyBeanBeed> newValue = new ArrayList<ManyBeanBeed>();
    newValue.add(new ManyBeanBeed());
    newValue.add(new ManyBeanBeed());
    // edit
    EditableIntegerBeed target = new EditableIntegerBeed(owner);
    IntegerEdit edit = new IntegerEdit(target);
    edit.perform();
    // test constructor
    ListEvent<ManyBeanBeed> listEvent =
      new ListEvent<ManyBeanBeed>(source, oldValue, newValue, edit);
    assertEquals(listEvent.getSource(), source);
    assertEquals(listEvent.getOldValue(), oldValue);
    assertEquals(listEvent.getNewValue(), newValue);
    assertEquals(listEvent.getEdit(), edit);
    assertEquals(listEvent.getEditState(), edit.getState());
    // old and new value
    oldValue = null;
    newValue = new ArrayList<ManyBeanBeed>();
    newValue.add(new ManyBeanBeed());
    newValue.add(new ManyBeanBeed());
    // test constructor
    listEvent = new ListEvent<ManyBeanBeed>(source, oldValue, newValue, edit);
    assertEquals(listEvent.getSource(), source);
    assertEquals(listEvent.getOldValue(), null);
    assertEquals(listEvent.getNewValue(), newValue);
    assertEquals(listEvent.getEdit(), edit);
    assertEquals(listEvent.getEditState(), edit.getState());
  }

}