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


import org.beedra_II.BeedEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * {@link BeedEvent} that carries a simple old and new value,
 * expressing the changed that occured in {@link #getSource()}.
 * The {@link #getSource() source} must be a {@link SimplePB}.
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class OldNewBEvent<_Source_ extends SimplePB<_Type_, ? extends OldNewBEvent<_Source_, _Type_>>,
                          _Type_>
    extends BeedEvent<_Source_> {

  /**
   * @pre source != null;
   * @post getSource() == sourcel
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   */
  public OldNewBEvent(_Source_ source, _Type_ oldValue, _Type_ newValue) {
    super(source);
    $oldValue = oldValue;
    $newValue = newValue;
  }

  /**
   * @basic
   */
  public final _Type_ getOldValue() {
    return $oldValue;
  }

  private final _Type_ $oldValue;

  /**
   * @basic
   */
  public final _Type_ getNewValue() {
    return $newValue;
  }

  private final _Type_ $newValue;

}

