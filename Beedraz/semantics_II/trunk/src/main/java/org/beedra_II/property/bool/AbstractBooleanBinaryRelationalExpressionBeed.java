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

package org.beedra_II.property.bool;


import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.number.real.RealBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A binary relation expression with two operands of type {@link RealBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBooleanBinaryRelationalExpressionBeed extends AbstractBooleanBinaryExpressionBeed {


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  getLeftArgument() == null;
   * @post  getRightArgument() == null;
   * @post  getBoolean() == null;
   */
  public AbstractBooleanBinaryRelationalExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  /*</construction>*/


  /*<property name="leftArgument">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final RealBeed<?> getLeftArgument() {
    return getLeftArg();
  }

  /**
   * @post getLeftArgument() == leftArgument;
   */
  public final void setLeftArgument(RealBeed<?> leftArgument) {
    setLeftArg(leftArgument);
  }

  /*</property>*/


  /*<property name="rightArgument">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final RealBeed<?> getRightArgument() {
    return getRightArg();
  }

  /**
   * @post getRightArgument() == rightArgument;
   */
  public final void setRightArgument(RealBeed<?> rightArgument) {
    setRightArg(rightArgument);
  }

  /*</property>*/

}

