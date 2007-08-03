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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Collection;
import java.util.HashSet;

import org.beedraz.semantics_II.expression.collection.set.EditableSetBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;

import org.junit.Test;


@Copyright("2007 - $Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 886 $",
         date     = "$Date: 2007-05-11 01:10:13 +0200 (vr, 11 mei 2007) $")
public class TestActualCollectionEvent extends TestAbstractCollectionEvent<Collection<String>> {

  @Test
  public void emptyTest() {
    // NOP
  }

  @Override
  protected ActualCollectionEvent<String> createCollectionEvent(
      EditableSetBeed<String> beed, Collection<String> added, Collection<String> removed) {
    return new ActualCollectionEvent<String>(beed, added, removed, null);
  }

  @Override
  protected Collection<String> createEmptyCollection() {
    return new HashSet<String>();
  }

}