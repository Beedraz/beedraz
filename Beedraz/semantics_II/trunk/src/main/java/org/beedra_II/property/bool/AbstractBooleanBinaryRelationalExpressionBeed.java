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


import org.beedra_II.path.Path;
import org.beedra_II.property.number.real.RealBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A binary relation expression or equality expression
 * with two operands of type {@link RealBeed}.
 *
 * @mudo same code as AbstractBooleanBinaryLogicalExpressionBeed; generalize
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBooleanBinaryRelationalExpressionBeed extends AbstractRealArgBooleanBinaryExpressionBeed {


  /*<property name="leftArgument">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getLeftArgumentPath() {
    return getLeftArgPath();
  }

  /**
   * @return getLeftArgumentPath() == null ? null : getLeftArgumentPath().get();
   */
  public final RealBeed<?> getLeftArgument() {
    return getLeftArg();
  }

  /**
   * @post getLeftArgumentPath() == leftArgumentPath;
   */
  public final void setLeftArgumentPath(Path<? extends RealBeed<?>> leftArgumentPath) {
    setLeftArgPath(leftArgumentPath);
  }

  /*</property>*/


  /*<property name="rightArgument">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getRightArgumentPath() {
    return getRightArgPath();
  }

  /**
   * @return getRightArgumentPath() == null ? null : getRightArgumentPath().get();
   */
  public final RealBeed<?> getRightArgument() {
    return getRightArg();
  }

  /**
   * @post getRightArgumentPath() == rightArgumentPath;
   */
  public final void setRightArgumentPath(Path<? extends RealBeed<?>> rightArgumentPath) {
    setRightArgPath(rightArgumentPath);
  }

  /*</property>*/

}

