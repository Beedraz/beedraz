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


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collection;

import org.beedra_II.edit.Edit;
import org.beedra_II.event.AbstractEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


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
public abstract class AbstractCollectionEvent<_Element_, _Collection_ extends Collection<_Element_>>
    extends AbstractEvent
    implements CollectionEvent<_Element_> {

  /**
   * @pre  source != null;
   * @pre addedElements != null;
   * @pre addedElements is fresh, owned by this;
   * @pre removedElements != null;
   * @pre removedElements is fresh, owned by this;
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
  protected AbstractCollectionEvent(CollectionBeed<_Element_, ?> source,
                                    _Collection_ addedElements,
                                    _Collection_ removedElements,
                                    Edit<?> edit) {
    super(source, edit);
    $addedElements = addedElements;
    $removedElements = removedElements;
  }

  protected abstract _Collection_ unmodifiable(_Collection_ c);

  /**
   * @basic
   */
  public final _Collection_ getAddedElements() {
    return unmodifiable($addedElements);
  }

 /**
   * @invar $addedElements != null;
   */
  private final _Collection_ $addedElements;

  /**
   * @basic
   */
  public final _Collection_ getRemovedElements() {
    return unmodifiable($removedElements);
  }

  /**
   * @invar $removedElements != null;
   */
  private final _Collection_ $removedElements;

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", added elements: " + getAddedElements() +
           ", removed elements: " + getRemovedElements();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "added elements: " + getAddedElements() + "\n");
    sb.append(indent(level + 1) + "removed elements: " + getRemovedElements() + "\n");
  }

}

