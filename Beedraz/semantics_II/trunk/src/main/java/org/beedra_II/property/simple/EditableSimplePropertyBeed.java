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

package org.beedra_II.property.simple;


import org.beedra_II.Beed;
import org.beedra_II.EditableBeed;
import org.beedra_II.event.EditEvent;
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
public abstract class EditableSimplePropertyBeed<_Type_, _EditEvent_ extends EditEvent<?>>
    extends AbstractPropertyBeed<_EditEvent_>
    implements SimplePropertyBeed<_Type_, _EditEvent_>,
               EditableBeed<_EditEvent_> {

  /**
   * @pre ownerBeed != null;
   */
  public EditableSimplePropertyBeed(Beed<?> ownerBeed) {
    super(ownerBeed);
  }

  /**
   * @basic
   */
  public final _Type_ get() {
    return safeValueCopy($t);
  }

  /**
   * @post value != null ? get().equals(value) : get() == null;
   * @post ; all registred ap change listeners are warned
   */
  void assign(_Type_ t) {
    $t = t;
//    if (! equalsWithNull(t, $t)) {
//      _Type_ oldValue = $t;
//      $t = safeValueCopy(t);
////      _Source_ andI = this;
////      SimpleEditablePropertyBeed<_Type_, _Source_>  myself = this;
////      SimpleEditablePropertyBeed<_Type_, SimpleEditablePropertyBeed<_Type_,_Source_>>  me = this;
////      me = myself;
////      myself = me;
//      OldNewEdit<_Type_, _Source_> event = new OldNewEdit<_Type_, _Source_>(this, safeValueCopy(oldValue), safeValueCopy($t));
////      UndoableOldNewEvent<_Type_, _Source_> event =
////          new UndoableOldNewEvent<_Type_, _Source_>(this, safeValueCopy(oldValue), safeValueCopy($t));
//      fireChangeEvent(event);
//    }
  }


  private _Type_ $t;

  /**
   * Returns a safe copy of {@code original}.
   * If {@code _Value_} is an immutable type, you can return original.
   * The default implementation is to return {@code original}.
   *
   * @result equalsWithNull(result, original);
   * @protected-result original;
   */
  protected _Type_ safeValueCopy(_Type_ original) {
    return original;
  }

//  protected OldNewEdit<_Type_> createInitialEvent() {
//    return new OldNewEdit<_Type_>(this, null, $t); // should be event, and not an edit, but nevermind; set the state to death
//  }


  public void fireEvent(_EditEvent_ editEvent) {
    fireChangeEvent(editEvent);
  }

//  public boolean isAcceptable(_Type_ value) {
//    return true;
//  }

}

