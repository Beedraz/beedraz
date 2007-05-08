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

package org.beedraz.semantics_II.expression.string;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.string.EditableStringBeed;
import org.beedraz.semantics_II.expression.string.StringEvent;


public class StubEditableStringBeed extends EditableStringBeed {

  public StubEditableStringBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * updateDependents is made public for testing reasons
   */
  public void publicUpdateDependents(StringEvent event) {
    updateDependents(event);
  }

  @Override
  public boolean isAcceptable(String goal) {
    return goal != null && goal.startsWith("r");
  }

}
