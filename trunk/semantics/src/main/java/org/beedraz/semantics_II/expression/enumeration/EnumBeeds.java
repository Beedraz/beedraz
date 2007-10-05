/*<license>
Copyright 2007 - $Date: 2007-05-08 16:22:50 +0200 (di, 08 mei 2007) $ by the authors mentioned below.

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


import static org.beedraz.semantics_II.path.Paths.path;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Convenience methods for working with {@link EnumBeed enumeration beeds}.</p>
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date: 2007-05-08 16:22:50 +0200 (di, 08 mei 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 853 $",
         date     = "$Date: 2007-05-08 16:22:50 +0200 (di, 08 mei 2007) $")
public class EnumBeeds {


  /*<section name="editableEnumBeed">*/
  //------------------------------------------------------------------

  public static <_Enum_ extends Enum<_Enum_>> EditableEnumBeed<_Enum_> editableEnumBeed(
      _Enum_ constant, AggregateBeed owner) throws IllegalEditException {
    try {
      EditableEnumBeed<_Enum_> editableEnumBeed = new EditableEnumBeed<_Enum_>(owner);
      EnumEdit<_Enum_> edit = new EnumEdit<_Enum_>(editableEnumBeed);
      edit.setGoal(constant);
      edit.perform();
      return editableEnumBeed;
    }
    catch (EditStateException e) {
      assert false : "Shouldn't happen";
      return null;
    }
  }

  /*</section>*/


  /*<section name="constantEnumBeed">*/
  //------------------------------------------------------------------

  public static <_Enum_ extends Enum<_Enum_>> ConstantEnumBeed<_Enum_> constant(_Enum_ constant) {
    return new ConstantEnumBeed<_Enum_>(constant);
  }

  /*</section>*/


  /*<section name="equalV">*/
  //------------------------------------------------------------------

  public static <_Enum_ extends Enum<_Enum_>> BooleanBeed equalV(
      EnumBeed<_Enum_> leftOperand,
      EnumBeed<_Enum_> rightOperand) {
    return equalV(path(leftOperand), path(rightOperand));
  }

  public static <_Enum_ extends Enum<_Enum_>> BooleanBeed equalV(
      EnumBeed<_Enum_> leftOperand,
      Path<? extends EnumBeed<_Enum_>> rightOperandPath) {
    return equalV(path(leftOperand), rightOperandPath);
  }

  public static <_Enum_ extends Enum<_Enum_>> BooleanBeed equalV(
      Path<? extends EnumBeed<_Enum_>> leftOperandPath,
      EnumBeed<_Enum_> rightOperand) {
    return equalV(leftOperandPath, path(rightOperand));
  }

  public static <_Enum_ extends Enum<_Enum_>> BooleanBeed equalV(
      Path<? extends EnumBeed<_Enum_>> leftOperandPath,
      Path<? extends EnumBeed<_Enum_>> rightOperandPath) {
    BooleanEQEnumBeed<_Enum_> eqBeed = new BooleanEQEnumBeed<_Enum_>();
    eqBeed.setLeftOperandPath(leftOperandPath);
    eqBeed.setRightOperandPath(rightOperandPath);
    return eqBeed;
  }

  /*</section>*/

}

