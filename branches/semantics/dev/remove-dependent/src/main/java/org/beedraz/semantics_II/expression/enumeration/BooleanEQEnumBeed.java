/*<license>
Copyright 2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.enumeration;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * An equality expression with two operands of type {@link EnumBeed}.
 *
 * @author  Nele Smeets
 * @author  Jan Dockx
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 913 $",
         date     = "$Date: 2007-05-24 16:46:42 +0200 (do, 24 mei 2007) $")
public class BooleanEQEnumBeed<_Enum_ extends Enum<_Enum_>>
    extends AbstractEnumArgBooleanBinaryExpressionBeed<_Enum_> {


  public BooleanEQEnumBeed() {
    this(null);
  }

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected BooleanEQEnumBeed(AggregateBeed owner) {
    super(owner);
  }


  /*<property name="leftOperand">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends EnumBeed<_Enum_>> getLeftOperandPath() {
    return getLeftOprndPath();
  }

  /**
   * @return getLeftOperandPath() == null ? null : getLeftOperandPath().get();
   */
  public final EnumBeed<_Enum_> getLeftOperand() {
    return getLeftOprnd();
  }

  /**
   * @post getLeftOperandPath() == leftOperandPath;
   */
  public final void setLeftOperandPath(Path<? extends EnumBeed<_Enum_>> leftOperandPath) {
    setLeftOprndPath(leftOperandPath);
  }

  /*</property>*/


  /*<property name="rightOperand">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends EnumBeed<_Enum_>> getRightOperandPath() {
    return getRightOprndPath();
  }

  /**
   * @return getRightOperandPath() == null ? null : getRightOperandPath().get();
   */
  public final EnumBeed<_Enum_> getRightOperand() {
    return getRightOprnd();
  }

  /**
   * @post getRightOperandPath() == rightOperandPath;
   */
  public final void setRightOperandPath(Path<? extends EnumBeed<_Enum_>> rightOperandPath) {
    setRightOprndPath(rightOperandPath);
  }

  /*</property>*/


  /**
   * @pre leftOperand != null;
   * @pre rightOperand != null;
   */
  @Override
  protected final boolean calculateValue(_Enum_ leftOperand, _Enum_ rightOperand) {
    return leftOperand == rightOperand;
  }


  @Override
  public final String getOperatorString() {
    return "==";
  }

}

