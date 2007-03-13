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

package org.beedra_II.property.collection;


import static java.util.Collections.unmodifiableCollection;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.beedra_II.edit.Edit;
import org.ppeew.annotations.vcs.CvsInfo;


/**
 * Event that notifies of changes in an actual {@link CollectionBeed}.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
final class ActualCollectionEvent<_Element_>
    extends AbstractCollectionEvent<_Element_, Collection<_Element_>>
    implements CollectionEvent<_Element_, Collection<_Element_>>{

  /**
   * @pre  source != null;
   * @pre  edit != null;
   * @pre  (edit.getState() == DONE) || (edit.getState() == UNDONE);
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post getEditState() == edit.getState();
   * @post addedElements != null
   *           ? getAddedElements().equals(addedElements)
   *           : getAddedElements().isEmpty();
   * @post removedElements != null
   *           ? getRemovedElements().equals(removedElements)
   *           : getRemovedElements().isEmpty();
   */
  public ActualCollectionEvent(CollectionBeed<_Element_, ?> source,
                               Collection<_Element_> addedElements,
                               Collection<_Element_> removedElements,
                               Edit<?> edit) {
    super(source,
          addedElements == null ? Collections.<_Element_>emptySet() : new LinkedList<_Element_>(addedElements),
          removedElements == null ? Collections.<_Element_>emptySet() : new LinkedList<_Element_>(removedElements),
          edit);
  }

  @Override
  protected Collection<_Element_> unmodifiable(Collection<_Element_> c) {
    return unmodifiableCollection(c);
  }

}

