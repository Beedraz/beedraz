/*<license>
 Copyright 2007 - $Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.collection;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Collection;

import org.beedraz.semantics_II.CannotCombineEventsException;
import org.beedraz.semantics_II.CompoundEdit;
import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.beedraz.semantics_II.expression.collection.set.EditableSetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEdit;
import org.beedraz.semantics_II.expression.collection.set.SetEvent;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 886 $",
         date     = "$Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $")
public abstract class TestAbstractCollectionEvent<_Collection_ extends Collection<String>> {

  /**
   * {a,b,c,d}
   *
   * added: {e,f} removed: {a,b}
   *
   * {c,d,e,f}
   *
   * added: {a,g} removed: {c,e}
   *
   * {a,d,f,g}
   *
   * so the combined event has the following sets:
   *
   * added: {f,g} removed: {b,c,e} (e was not in the original set, so it can be left out)
   */
  @Test
  public void createCombinedEvent() throws IllegalEditException,
      CannotCombineEventsException, EditStateException {
    String a = "a";
    String b = "b";
    String c = "c";
    String d = "d";
    String e = "e";
    String f = "f";
    String g = "g";
    EditableSetBeed<String> beed = new EditableSetBeed<String>(new AbstractAggregateBeed() {/*NOP*/});
    SetEdit<String> edit = new SetEdit<String>(beed);
    edit.addElementToAdd(a);
    edit.addElementToAdd(b);
    edit.addElementToAdd(c);
    edit.addElementToAdd(d);
    edit.perform();
    // create sets
    _Collection_ added1 = createEmptyCollection();
    added1.add(e);
    added1.add(f);
    _Collection_ removed1 = createEmptyCollection();
    removed1.add(a);
    removed1.add(b);
    _Collection_ added2 = createEmptyCollection();
    added2.add(a);
    added2.add(g);
    _Collection_ removed2 = createEmptyCollection();
    removed2.add(c);
    removed2.add(e);
    // create two events
    AbstractCollectionEvent<String, _Collection_> event1 = createCollectionEvent(beed, added1, removed1);
    AbstractCollectionEvent<String, _Collection_> event2 = createCollectionEvent(beed, added2, removed2);
    // create a compoundEdit
    CompoundEdit<EditableSetBeed<String>, SetEvent<String>> compoundEdit =
      new CompoundEdit<EditableSetBeed<String>, SetEvent<String>>(beed);
    Event combinedEvent = event1.combineWith(event2, compoundEdit);
    assertEquals(event1.getClass(), combinedEvent.getClass());
    @SuppressWarnings("unchecked")
    AbstractCollectionEvent<String, _Collection_> combinedCollectionEvent =
      (AbstractCollectionEvent<String, _Collection_>) combinedEvent;
    assertEquals(event1.getSource(), combinedCollectionEvent.getSource());
    assertEquals(compoundEdit, combinedCollectionEvent.getEdit());
    assertEquals(compoundEdit.getState(), combinedCollectionEvent.getEditState());
    assertEquals(2, combinedCollectionEvent.getAddedElements().size());
    assertTrue(combinedCollectionEvent.getAddedElements().contains(f));
    assertTrue(combinedCollectionEvent.getAddedElements().contains(g));
    assertEquals(2, combinedCollectionEvent.getRemovedElements().size());
    assertTrue(combinedCollectionEvent.getRemovedElements().contains(b));
    assertTrue(combinedCollectionEvent.getRemovedElements().contains(c));
  }

  protected abstract AbstractCollectionEvent<String, _Collection_> createCollectionEvent(
      EditableSetBeed<String> beed, _Collection_ added, _Collection_ removed);


  protected abstract _Collection_ createEmptyCollection();

}