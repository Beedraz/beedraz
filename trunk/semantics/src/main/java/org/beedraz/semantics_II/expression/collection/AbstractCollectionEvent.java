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
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collection;
import java.util.Collections;

import org.beedraz.semantics_II.AbstractEvent;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.CannotCombineEventsException;
import org.beedraz.semantics_II.CompoundEdit;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Event that notifies of changes in a {@link CollectionBeed}.
 *
 * @author Jan Dockx
 *
 * @invar getSource() instanceof CollectionBeed
 * @invar getAddedElements() != null;
 * @invar getRemovedElements() != null;
 * @invar Collections.disjoint(getAddedElements(), getRemovedElements());
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
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
   * @pre Collections.disjoint(getAddedElements(), getRemovedElements());
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
    assert Collections.disjoint(addedElements, removedElements);
    $addedElements = addedElements;
    $removedElements = removedElements;
  }

  protected abstract _Collection_ unmodifiable(_Collection_ c);

  /**
   * @basic
   */
  public _Collection_ getAddedElements() {
    return unmodifiable($addedElements);
  }

 /**
   * @invar $addedElements != null;
   */
  private final _Collection_ $addedElements;

  /**
   * @basic
   */
  public _Collection_ getRemovedElements() {
    return unmodifiable($removedElements);
  }

  /**
   * @invar $removedElements != null;
   */
  private final _Collection_ $removedElements;

  /**
   * <p>Combine added and removed elements.</p>
   * <p>The combined added elements are:
   *   <ol>
   *     <li>{@code this}' added elements, minus the {@code other}'s removed
   *       elements (these elements are first added and then removed again,
   *       so they are not in the resulting set)</li>
   *     <li>the {@code other}'s added elements minus {@code this}' removed
   *       elements (these elements are first removed and then added again,
   *       so they are in the resulting set, but, in fact, nothing happened
   *       to them)</li>
   *   </ol>
   * </p>
   * <p>The combined removed elements are
   *   <ol>
   *     <li>{@code this}' removed elements, minus the {@code other}'s added
   *       elements (these elements are first removed, and then added again,
   *       so they are in the resulting set, but, in fact, nothing happened to
   *       them)</li>
   *     <li>the {@code other}'s removed elements, minus {@code this}' added
   *       elements (these elements are first added, and then removed again,
   *       so they are not in the resulting set)</li>
   *     <li>
   *   </ol>
   * </p>
   * <p>Note that this is true because for {@code this} and {@code other},
   *   added and removed elemens are disjunct collections.</p>
   *
   * @mudo needs unit test
   */
  @Override
  protected AbstractCollectionEvent<_Element_, _Collection_> createCombinedEvent(Event other, CompoundEdit<?, ?> compoundEdit)
      throws CannotCombineEventsException {
    AbstractCollectionEvent<_Element_, Collection<_Element_>> otherACE =
        (AbstractCollectionEvent<_Element_, Collection<_Element_>>)other;
    _Collection_ addedAndNotRemoved = freshCopy(getAddedElements());
    addedAndNotRemoved.removeAll(otherACE.getRemovedElements());
    Collection<_Element_> notRemovedAndAdded = otherACE.freshCopy(otherACE.getAddedElements());
    notRemovedAndAdded.removeAll(getRemovedElements());
    addedAndNotRemoved.addAll(notRemovedAndAdded);
    _Collection_ removedAndNotAdded = freshCopy(getRemovedElements());
    removedAndNotAdded.removeAll(otherACE.getAddedElements());
    Collection<_Element_> notAddedAndRemoved = otherACE.freshCopy(otherACE.getRemovedElements());
    notAddedAndRemoved.removeAll(getAddedElements());
    removedAndNotAdded.addAll(notAddedAndRemoved);
    return createCombinedEvent(getSource(), addedAndNotRemoved, removedAndNotAdded, compoundEdit);
  }

  /**
   * @pre c != null;
   * @post result.equals(c);
   */
  protected abstract _Collection_ freshCopy(_Collection_ c);

  protected abstract AbstractCollectionEvent<_Element_, _Collection_> createCombinedEvent(Beed<?> source,
                                                                                          _Collection_ added,
                                                                                          _Collection_ removed,
                                                                                          CompoundEdit<?, ?> edit);

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

