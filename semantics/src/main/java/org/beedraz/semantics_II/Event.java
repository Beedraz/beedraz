/*<license>
Copyright 2007 - $Date:2007-05-08 16:13:31 +0200 (Tue, 08 May 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Notifies interested {@link Listener Listeners} and
 *   {@link Dependent Dependents} of changes in {@link #getSource() a beed}.
 *   The change is described in full by properties of the event,
 *   if possible, either as a delta, or as an old and a new state.
 *   If the change was finally the result of {@link Edit#perform() performing},
 *   {@link Edit#undo() undoing}, or {@link Edit#redo() redoing} an
 *   {@link Edit}, a {@link #getEdit() reference to that edit} is carried
 *   by the event. The same edit is carried by events that signal changes
 *   in beeds that dependent on the editable beed that was changed by the
 *   edit. This means that the {@link Edit#getTarget() edit target} is not necessarily
 *   the same as the {@link #getSource() event source}. If the change was
 *   the result of a structural change, such as adding, removing or changing an
 *   operator, the {@link #getEdit()} is {@code null}.</p>
 * <p>Events should be <em>immutable</em> during event propagation and after event
 *   delivery. All mutating operations, if any, should throw an
 *   exception if called after the start of event propagation. Code that creates
 *   and sends the event is responsible for making sure the event is immutable.</p>
 * <p>Events in general are not generic. Experiments with a generic
 *   {@link #getSource()} or {@link #getEdit()} type showed that this does
 *   not enhance the stability of the interface.</p>
 * <p>Events are specific for the general type of {@link Beed} that sends them.
 *   In general, the intention is to have a specific event type for each
 *   Beed subtype interface. As a consequence, the event interface hierarchy is
 *   <em>covariant</em> with the {@link Beed} interface hierarchy.</p>
 * <p>Note that we cannot extend {@code java.util.EventObject}, because in this framework
 *   event types need multiple inheritance, so we must be able to define them as
 *   interfaces (and {@code java.util.EventObject} is a class).</p>
 *
 * @author Jan Dockx
 *
 * @invar getSource() != null;
 * @invar (getEdit() != null) ? getEdit().getState() == DONE || getEdit().getState() == UNDONE;
 */
@Copyright("2007 - $Date:2007-05-08 16:13:31 +0200 (Tue, 08 May 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision:849 $",
         date     = "$Date:2007-05-08 16:13:31 +0200 (Tue, 08 May 2007) $")
public interface Event extends Cloneable {

  /**
   * @basic
   */
  Beed<?> getSource();

  /**
   * @basic
   */
  Edit<?> getEdit();

  /**
   * @basic
   */
  Edit.State getEditState();

  /**
   * <p>Try to combine this event with {@code other} into a fresh event
   *   of the same type, with {@code edit} as {@link #getEdit() edit}.</p>
   * <p>To be able to do this, this event and {@code other}
   *   need to:</p>
   * <ul>
   *   <li>be of the same type</li>
   *   <li>have the same source</li>
   *   <li>agree on the intermediate state: the new state of this event
   *      needs to be the same as the old state of {@code other}.</li>
   * </ul>
   * <p>There are no other reasons why combining events might fail.</p>
   * <p>We need to create a new event, and not change an existing one
   *  ({@code this} or {@code other}), because events are (supposed to
   *  be) immutable.</p>
   *
   * @pre other != null;
   * @pre edit != null;
   * @pre getEdit() != null;
   * @pre other.getEdit() != null;
   * @pre edit.deepContains(this.getEdit());
   * @pre edit.deepContains(other.getEdit());
   * @post result.getClass() == getClass();
   * @post result.getSource() == getSource();
   * @post result.getEdit() == edit;
   * @post result.getEditState() == edit.getState();
   * @post ; result initial state is this initial state
   * @post ; result goal state is {@code other} initial state
   *
   * @throws CannotCombineEventsException
   *         (getClass() != other.getClass()) &&
   *         (exception.getReason() == CannotCombineEventsException.Reason.DIFFERENT_TYPE);
   * @throws CannotCombineEventsException
   *         (getSource() != other.getSource()) &&
   *         (exception.getReason() == CannotCombineEventsException.Reason.DIFFERENT_SOURCE);
   * @throws CannotCombineEventsException
   *         exception.getReason() == CannotCombineEventsException.Reason.INCOMPATIBLE_STATES;
   *         this goal state is different from other initial state
   *
   * @note Maybe the pre that the edit must be a compound edit is too strong?
   *       It is not really necessary for the method itself, but it is what
   *       this method is intented for. Maybe other pre's are too strong?
   */
  Event combineWith(Event other, CompoundEdit<?, ?> edit) throws CannotCombineEventsException;

  /**
   * Multiline instance information.
   * Use in debugging or logging only.
   *
   * @pre sb != null;
   * @pre level >= 0;
   */
  void toString(StringBuffer sb, int level);

}

