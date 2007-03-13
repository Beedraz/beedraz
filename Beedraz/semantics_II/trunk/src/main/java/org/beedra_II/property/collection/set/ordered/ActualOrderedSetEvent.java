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


import static org.ppeew.collection.CollectionUtil.unmodifiableOrderedSet;

import org.beedra_II.edit.Edit;
import org.beedra_II.property.collection.AbstractOrderedCollectionEvent;
import org.beedra_II.property.collection.list.ListBeed;
import org.ppeew.collection.LinkedListOrderedSet;
import org.ppeew.collection.OrderedSet;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * Event that notifies of changes in an actual {@link ListBeed}.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public final class ActualOrderedSetEvent<_Element_>
    extends AbstractOrderedCollectionEvent<_Element_, OrderedSet<_Element_>>
    implements OrderedSetEvent<_Element_> {



  /**
   */
  /**
   * @pre  source != null;
   * @pre  (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @pre  oldValue != null;
   * @pre  newValue != null;
   * @pre  ! oldValue.equals(newValue);;
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   * @post getOldValue().equals(oldValue);
   * @post getNewValue().equals(newValue);
   */
  public ActualOrderedSetEvent(OrderedSetBeed<_Element_, ?> source,
                               OrderedSet<_Element_> oldValue,
                               OrderedSet<_Element_> newValue,
                               Edit<?> edit) {
    super(source, oldValue, newValue, edit);
  }

  @Override
  protected OrderedSet<_Element_> copyOf(OrderedSet<_Element_> c) {
    return new LinkedListOrderedSet<_Element_>(c);
  }

  @Override
  protected OrderedSet<_Element_> unmodifiable(OrderedSet<_Element_> c) {
    return unmodifiableOrderedSet(c);
  }

}

