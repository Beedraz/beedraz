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

package org.beedra_II.property.collection.set.sorted;


import static java.util.Collections.unmodifiableSortedSet;

import java.util.SortedSet;
import java.util.TreeSet;

import org.beedra.util_I.CollectionUtil;
import org.beedra_II.edit.Edit;
import org.beedra_II.property.collection.AbstractCollectionEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * Event that notifies of changes in an actual {@link SortedSetBeed}.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class ActualSortedSetEvent<_Element_>
    extends AbstractCollectionEvent<_Element_, SortedSet<_Element_>>
    implements SortedSetEvent<_Element_> {

  /**
   * @pre  source != null;
   * @pre  edit != null;
   * @pre  (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @pre  addedElements != null ? addedElements.comparator() == getSource().getComparator();
   * @pre  removedElements != null ? removedElements.comparator() == getSource().getComparator();
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
  public ActualSortedSetEvent(SortedSetBeed<_Element_, ?> source,
                              SortedSet<_Element_> addedElements,
                              SortedSet<_Element_> removedElements,
                              Edit<?> edit) {
    super(source,
          addedElements == null ? CollectionUtil.<_Element_>emptySortedSet() : new TreeSet<_Element_>(addedElements),
          removedElements == null ? CollectionUtil.<_Element_>emptySortedSet() : new TreeSet<_Element_>(removedElements),
          edit);
    assert addedElements != null ? addedElements.comparator() == source.getComparator() : true;
    assert removedElements != null ? removedElements.comparator() == source.getComparator() : true;
  }

  @Override
  protected SortedSet<_Element_> unmodifiable(SortedSet<_Element_> c) {
    return unmodifiableSortedSet(c);
  }

}

