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


import static org.beedra.util_I.Comparison.equalsWithNull;

import org.beedra_II.Beed;
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
public class StraightSimplePDB<_Type_>
    extends AbstractPropertyBeed<UndoableOldNewBEvent<StraightSimplePDB<_Type_>, _Type_>>
    implements SimplePDB<_Type_, UndoableOldNewBEvent<StraightSimplePDB<_Type_>, _Type_>> {

  /**
   * @pre ownerBeed != null;
   */
  public StraightSimplePDB(Beed<?> ownerBeed) {
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
  public final void set(_Type_ t) {
    if (! equalsWithNull(t, $t)) {
      _Type_ oldValue = $t;
      $t = safeValueCopy(t);
      UndoableOldNewBEvent<StraightSimplePDB<_Type_>, _Type_> event =
        new UndoableOldNewBEvent<StraightSimplePDB<_Type_>, _Type_>(
            this, safeValueCopy(oldValue), safeValueCopy($t));
      fireChangeEvent(event);
    }
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

}

