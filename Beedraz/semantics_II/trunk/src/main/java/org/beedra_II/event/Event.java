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

package org.beedra_II.event;


import static org.beedra.util_I.MultiLineToStringUtil.indent;
import static org.beedra.util_I.MultiLineToStringUtil.objectToString;
import static org.beedra_II.edit.Edit.State.DONE;
import static org.beedra_II.edit.Edit.State.UNDONE;

import org.beedra_II.Beed;
import org.beedra_II.edit.Edit;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * <p>Notifies interested {@link Listener Listeners} of changes
 *   in {@link #getChangedBeed()}.</p>
 * <p>Events should be <em>immutable</em> during event propagation.
 *   Non-abstract subtypes should be declared {@code final} to ensure
 *   immutability. All mutating operations, if any, should throw an
 *   exception if called during event propagation.</p>
 * <p>The type parameter {@code _ChangeBeed_} says what the static
 *   type is of the beeds that send this event. Via polymorphism,
 *   the event can also be send by subtypes of {@code _ChangeBeed_}.
 *   The actual type parameter {@code _ChangeBeed_} in concrete
 *   generic invocations has to be a subtype of
 *
 *   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *
 *   {@link Beed}{@code &lt;? super _This_&gt;},
 *   which means that the {@link Beed} type has to declare the static
 *   type of the events it sends as the generic invocation itself or
 *   one of its XXX
 *
 *   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *
 *   class of actual sending beed > _ChangeBeed_
 *
 * <p>Events are specific for the type of {@link Beed} that sends them.
 *   The event type hierarchy is <em>covariant</em> with the {@link Beed}
 *   hierarchy. The type of the events sent by a beed of type {@code B} is
 *   {@code Event&lt;B&gt;}, which should be {@code final}.</p>
 * <p>A concrete event can eventually be send by {@link Beed Beeds} of type
 *   the {@code _Source_} and subtypes.</p>
 * <p>The edit is the final cause of this event. This can be {@code null},
 *   for example when the value of derived beeds changes due to structure
 *   changes, or for initial events.</p>
 *
 * @author Jan Dockx
 *
 * @invar getSource() != null;
 * @invar (getEdit() != null) ? getEdit().getState() == DONE || getEdit().getState() == UNDONE;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class Event<_Edit_ extends Edit<?>> {

  /**
   * @pre source != null;
   * @pre (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post getEditState() == edit.getState();
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   */
  public Event(Beed<?> source, _Edit_ edit) {
    assert source != null;
    assert (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE) : true;
    $source = source;
    $edit = edit;
    $editState = (edit != null) ? edit.getState() : null;
  }

  /**
   * @basic
   */
  public final Beed<?> getSource() {
    return $source;
  }

  /**
   * @invar $source != null;
   */
  private final Beed<?> $source;

  /**
   * @basic
   */
  public final _Edit_ getEdit() {
    return $edit;
  }

  private final _Edit_ $edit;

  /**
   * @basic
   */
  public Edit.State getEditState() {
    return $editState;
  }

  private final Edit.State $editState;

  @Override
  public final String toString() {
    return getClass().getSimpleName() + //"@" + hashCode() +
           "[" + otherToStringInformation() + "]";
  }

  protected String otherToStringInformation() {
    return "source: " + getSource() +
           ", edit: " + getEdit() +
           ", edit state: " + getEditState();
  }

  /**
   * Multiline instance information.
   * Use in debugging or logging only.
   *
   * @pre sb != null;
   * @pre level >= 0;
   */
  public void toString(StringBuffer sb, int level) {
    assert sb != null;
    assert level >= 0;
    objectToString(this, sb, level);
    sb.append(indent(level + 1) + "source:\n");
    getSource().toString(sb, level + 2);
    sb.append(indent(level + 1) + "edit:\n");
    getEdit().toString(sb, level + 2);
    sb.append(indent(level + 1) + "edit state: " + getEditState() + "\n");
  }

}

