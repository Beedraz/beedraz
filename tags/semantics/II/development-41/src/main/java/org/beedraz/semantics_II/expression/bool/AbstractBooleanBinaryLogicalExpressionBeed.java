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

package org.beedraz.semantics_II.expression.bool;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.path.Path;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A binary logical expression with two operands of type {@link BooleanBeed}.
 *
 *
 *
 * @mudo same code as AbstractBooleanBinaryRelationalExpressionBeed; generalize
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractBooleanBinaryLogicalExpressionBeed
    extends AbstractBooleanArgBooleanBinaryExpressionBeed {



  /*<property name="leftOperand">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends BooleanBeed> getLeftOperandPath() {
    return getLeftOprndPath();
  }

  /**
   * @return getLeftOperandPath().get();
   */
  public final BooleanBeed getLeftOperand() {
    return getLeftOprnd();
  }

  /**
   * @post getLeftOperandPath() == leftOperandPath;
   */
  public final void setLeftOperandPath(Path<? extends BooleanBeed> leftOperandPath) {
    setLeftOprndPath(leftOperandPath);
  }

  /*</property>*/


  /*<property name="rightOperand">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends BooleanBeed> getRightOperandPath() {
    return getRightOprndPath();
  }

  /**
   * @return getRightOperandPath().get();
   */
  public final BooleanBeed getRightOperand() {
    return getRightOprnd();
  }

  /**
   * @post getRightOperandPath() == rightOperandPath;
   */
  public final void setRightOperandPath(Path<? extends BooleanBeed> rightOperandPath) {
    setRightOprndPath(rightOperandPath);
  }

  /*</property>*/

}