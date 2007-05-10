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

package org.beedraz.semantics_II.edit;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.edit.Edit.State;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Thrown when an {@link Edit} is told to {@link Edit#perform()},
 * {@link Edit#undo()} or {@link Edit#redo()} and can't,
 * for reasons inherent to the Edit object, e.g., it is in the wrong
 * state.
 *
 * @author  Jan Dockx
 *
 * @invar   getCurrentState() != null;
 * @invar   getExpectedState() != null;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class EditStateException extends EditException {
  // MUDO extends InternalException, StateException


  /*<construction>*/
  //-------------------------------------------------------

  /**
   * @pre  currentState != null;
   * @pre  expectedState != null;
   * @post ComparisonUtil.equalsWithNull(null, getMessage());
   * @post getCause() == null;
   * @post getEdit() == e;
   * @post getCurrentState() == currentState;
   * @post getExpectedState() == expectedState;
   */
  public EditStateException(Edit<?> e, State currentState, State expectedState) {
    super(e, null);
    $currentState = currentState;
    $expectedState = expectedState;
  }

  /*</construction>*/



  /*<property name="expectedState">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final State getExpectedState() {
    return $expectedState;
  }

  /**
   * @invar $expectedState != null;
   */
  private State $expectedState;

  /*</property>*/



  /*<property name="currentState">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final State getCurrentState() {
    return $currentState;
  }

  /**
   * @invar $currentState != null;
   */
  private State $currentState;

  /*</property>*/

}
