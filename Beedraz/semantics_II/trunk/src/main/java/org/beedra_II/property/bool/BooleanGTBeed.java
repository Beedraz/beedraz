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
 * A beed that expresses whether the first argument is strictly greater than the
 * second argument.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanGTBeed extends AbstractBooleanBinaryExpressionBeed {


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  getFirstArgument() == null;
   * @post  getSecondArgument() == null;
   * @post  getBoolean() == null;
   */
  public BooleanGTBeed(AggregateBeed owner) {
    super(owner);
  }

  /*</construction>*/


  /*<property name="firstArgument">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final RealBeed<?> getFirstArgument() {
    return getLeftArgument();
  }

  /**
   * @post getFirstArgument() == firstArgument;
   */
  public final void setFirstArgument(RealBeed<?> firstArgument) {
    setLeftArgument(firstArgument);
  }

  /*</property>*/


  /*<property name="secondArgument">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final RealBeed<?> getSecondArgument() {
    return getRightArgument();
  }

  /**
   * @post getSecondArgument() == secondArgument;
   */
  public final void setSecondArgument(RealBeed<?> secondArgument) {
    setRightArgument(secondArgument);
  }

  /*</property>*/


  /**
   * @pre leftArgument != null;
   * @pre rightArgument != null;
   */
  @Override
  protected final boolean calculateValue(double firstArgument, double secondArgument) {
    return firstArgument > secondArgument;
  }


  @Override
  public final String getOperatorString() {
    return ">";
  }

}

