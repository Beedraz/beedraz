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

package org.beedra_II.property.set;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.beedra_II.EditableBeed;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.AbstractPropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


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
public abstract class EditableSetBeed<_Element_>
    extends AbstractPropertyBeed<SetEvent<_Element_, SetEdit<_Element_>>>
    implements SetBeed<_Element_, SetEdit<_Element_>>,
               EditableBeed<SetEvent<_Element_, SetEdit<_Element_>>> {

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

  void addElements(Set<_Element_> elements) {
    $set.addAll(elements);
  }

  void removeElements(Set<_Element_> elements) {
    $set.removeAll(elements);
  }

  private Set<_Element_> $set = new HashSet<_Element_>();

  void fireEvent(SetEvent<_Element_, SetEdit<_Element_>> event) {
    fireChangeEvent(event);
  }

}

