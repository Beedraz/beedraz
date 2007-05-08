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
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractEditEvent<_Edit_ extends Edit<?>>
    implements EditEvent<_Edit_> {

  /**
   * @pre edit != null;
   * @post getEdit() == edit;
   */
  protected AbstractEditEvent(_Edit_ edit) {
    assert edit != null;
    $edit = edit;
    $editState = edit.getState();
  }

  /**
   * @basic
   */
  public final _Edit_ getEdit() {
    return $edit;
  }

  /**
   * @invar $edit != null;
   */
  private final _Edit_ $edit;


  /**
   * @basic
   */
  public Edit.State getEditState() {
    return $editState;
  }

  private final Edit.State $editState;

  /**
   * @basic
   */
  public Beed<?> getSource() {
    return $edit.getTarget();
  }

}

