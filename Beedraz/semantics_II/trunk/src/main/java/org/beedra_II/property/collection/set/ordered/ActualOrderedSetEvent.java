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

package org.beedra_II.property.collection.set.ordered;


import static java.util.Collections.unmodifiableList;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.beedra_II.edit.Edit;
import org.beedra_II.property.collection.AbstractCollectionEvent;
import org.beedra_II.property.collection.CollectionBeed;
import org.beedra_II.property.collection.set.sorted.SortedSetBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * Event that notifies of changes in a {@link CollectionBeed}.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof CollectionBeed
 * @invar getAddedElements() != null;
 * @invar getRemovedElements() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class ActualOrderedSetEvent<_Element_>
    extends AbstractCollectionEvent<_Element_, List<_Element_>>
    implements OrderedSetEvent<_Element_, List<_Element_>> {

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
  public ActualOrderedSetEvent(SortedSetBeed<_Element_, ?> source,
                              SortedSet<_Element_> addedElements,
                              SortedSet<_Element_> removedElements,
                              Edit<?> edit) {
    super(source,
          addedElements == null ? new TreeSet<_Element_>() : new TreeSet<_Element_>(addedElements),
          removedElements == null ? new TreeSet<_Element_>() : new TreeSet<_Element_>(removedElements),
          edit);
  }

  @Override
  protected List<_Element_> unmodifiable(List<_Element_> c) {
    return unmodifiableList(c);
  }

}

