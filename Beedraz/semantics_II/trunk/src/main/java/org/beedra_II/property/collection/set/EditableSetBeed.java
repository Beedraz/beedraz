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

package org.beedra_II.property.collection.set;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.beedra_II.EditableBeed;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.AbstractPropertyBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * {@link SimplePB} whose value can be changed directly
 * by the user.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableSetBeed<_Element_>
    extends AbstractPropertyBeed<SetEvent<_Element_, ?>>
    implements SetBeed<_Element_, SetEvent<_Element_, ?>, Set<_Element_>>,
               EditableBeed<SetEvent<_Element_, ?>> {

  /**
   * @pre ownerBeed != null;
   */
  public EditableSetBeed(AggregateBeed ownerBeed) {
    super(ownerBeed);
  }

  /**
   * @basic
   */
  public final Set<_Element_> get() {
    return Collections.unmodifiableSet($set);
  }

  /**
   * @pre elements != null;
   * @pre ComparisonUtil.intersection(get(), elements).isEmpty();
   */
  final void addElements(Set<_Element_> elements) {
    assert elements != null;
    assert org.ppeew.collection_I.CollectionUtil.intersection(get(), elements).isEmpty();
    $set.addAll(elements);
  }

  /**
   * @pre elements != null;
   * @pre get().containsAll(elements);
   */
  final void removeElements(Set<_Element_> elements) {
    assert elements != null;
    assert get().containsAll(elements);
    $set.removeAll(elements);
  }

  private Set<_Element_> $set = new HashSet<_Element_>();

  void fireEvent(SetEvent<_Element_, ?> event) {
    fireChangeEvent(event);
  }

  /**
   * @post  result != null;
   * @post  result.getAddedElements().equals(get());
   * @post  result.getRemovedElements().isEmpty();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected SetEvent<_Element_, ?> createInitialEvent() {
    return new ActualSetEvent<_Element_>(this, get(), null, null);
  }

  public boolean isAcceptable(Set<_Element_> elementsToAdd, Set<_Element_> elementsToRemove) {
    return true;
  }
}

