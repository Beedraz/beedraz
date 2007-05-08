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

package org.beedra_II.bean;


import org.beedra_II.BeedEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * Event fired by {@link BeanBeed BeanBeeds} when
 * they change.
 *
 * @invar getCause() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BeanEvent extends BeedEvent<BeanBeed> {

  /**
   * @pre source != null;
   * @pre cause != null;
   * @post getSource() == source;
   * @post getCause() == cause;
   */
  public BeanEvent(BeanBeed source, BeedEvent<?> cause) {
    super(source);
    assert cause != null;
    $cause = cause;
  }

  /**
   * @basic
   */
  public BeedEvent<?> getCause() {
    return $cause;
  }

  /**
   * @invar $cause != null;
   */
  private BeedEvent<?> $cause;

}

