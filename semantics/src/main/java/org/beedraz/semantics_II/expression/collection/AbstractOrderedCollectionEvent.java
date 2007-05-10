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

package org.beedraz.semantics_II.expression.collection;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Collection;

import org.beedraz.semantics_II.ActualOldNewEvent;
import org.beedraz.semantics_II.Edit;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractOrderedCollectionEvent<_Element_,
                                                     _Collection_ extends Collection<_Element_>>
    extends ActualOldNewEvent<_Collection_>
    implements CollectionEvent<_Element_> {

  /**
   * @pre  source != null;
   * @pre  (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @pre  newValue != null;
   * @pre  ! oldValue.equals(newValue);;
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   * @post ComparisonUtil.equalsWithNull(getOldValue(), oldValue);
   * @post getNewValue().equals(newValue);
   */
  protected AbstractOrderedCollectionEvent(CollectionBeed<_Element_, ?> source,
                                           _Collection_ oldValue,
                                           _Collection_ newValue,
                                           Edit<?> edit) {
    super(source, oldValue, newValue, edit);
    assert newValue != null;
  }

  /**
   * This method is implemented lazily. If you don't need this list,
   * it will never be generated.
   *
   * @result result.equals(getNewValue().removeAll(getOldValue()));
   */
  public _Collection_ getAddedElements() {
    if ($addedElements == null) {
      _Collection_ acc = copyOf(getNewValue());
      if (getOldValue() != null) {
        acc.removeAll(getOldValue());
      }
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
  public _Collection_ getRemovedElements() {
    if ($removedElements == null && (getOldValue() != null)) {
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

