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
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A binary logical expression with two operands of type {@link BooleanBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBooleanBinaryLogicalExpressionBeed
    extends AbstractBooleanArgBooleanBinaryExpressionBeed {


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @post  getLeftArgument() == null;
   * @post  getRightArgument() == null;
   * @post  getBoolean() == null;
   */
  public AbstractBooleanBinaryLogicalExpressionBeed() {
    super();
  }

  /*</construction>*/


  /*<property name="leftArgument">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends BooleanBeed> getLeftArgumentPath() {
    return getLeftArgPath();
  }

  /**
   * @return getLeftArgumentPath().get();
   */
  public final BooleanBeed getLeftArgument() {
    return getLeftArg();
  }

  /**
   * @post getLeftArgumentPath() == leftArgumentPath;
   */
  public final void setLeftArgumentPath(Path<? extends BooleanBeed> leftArgumentPath) {
    setLeftArgPath(leftArgumentPath);
  }

  /*</property>*/


  /*<property name="rightArgument">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends BooleanBeed> getRightArgumentPath() {
    return getRightArgPath();
  }

  /**
   * @return getRightArgumentPath().get();
   */
  public final BooleanBeed getRightArgument() {
    return getRightArg();
  }

  /**
   * @post getRightArgumentPath() == rightArgumentPath;
   */
  public final void setRightArgumentPath(Path<? extends BooleanBeed> rightArgumentPath) {
    setRightArgPath(rightArgumentPath);
  }

  /*</property>*/

}