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


import java.util.Collection;

import org.beedra_II.edit.Edit;
import org.beedra_II.property.simple.ActualOldNewEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractOrderedCollectionEvent<_Element_,
                                                     _Collection_ extends Collection<_Element_>>
    extends ActualOldNewEvent<_Collection_>
    implements CollectionEvent<_Element_, _Collection_> {

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
  protected AbstractOrderedCollectionEvent(CollectionBeed<_Element_, ?> source,
                                           _Collection_ oldValue,
                                           _Collection_ newValue,
                                           Edit<?> edit) {
    super(source, oldValue, newValue, edit);
    assert oldValue != null;
    assert newValue != null;
    assert ! oldValue.equals(newValue);
  }

  /**
   * This method is implemented lazily. If you don't need this list,
   * it will never be generated.
   *
   * @result result.equals(getNewValue().removeAll(getOldValue()));
   */
  public final _Collection_ getAddedElements() {
    if ($addedElements == null) {
      _Collection_ acc = copyOf(getNewValue());
      acc.removeAll(getOldValue());
      $addedElements = unmodifiable(acc);
    }
    return $addedElements;
  }

  private _Collection_ $addedElements;

  /**
   * This method is implemented lazily. If you don't need this list,
   * it will never be generated.
   *
   * @result result.equals(getOldValue().removeAll(getNewValue()));
   */
  public final _Collection_ getRemovedElements() {
    if ($removedElements == null) {
      _Collection_ acc = copyOf(getOldValue());
      acc.removeAll(getNewValue());
      $removedElements = unmodifiable(acc);
    }
    return $removedElements;
  }

  private _Collection_ $removedElements;

  protected abstract _Collection_ copyOf(_Collection_ c);

  protected abstract _Collection_ unmodifiable(_Collection_ c);

}

