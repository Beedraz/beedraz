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


import static org.beedra_II.path.Paths.fix;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.path.Path;
import org.beedra_II.property.number.real.RealBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Convenience methods for working with paths.</p>
 *
 * @author  Jan Dockx
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanBeeds {


  /*<section name="not">*/
  //------------------------------------------------------------------

  public static BooleanBeed not(BooleanBeed operand) {
    BooleanNotBeed notBeed = new BooleanNotBeed();
    notBeed.setOperandPath(fix(operand));
    return notBeed;
  }

  /*</section>*/


  /*<section name="and">*/
  //------------------------------------------------------------------

  public static BooleanBeed and(BooleanBeed... operands) {
    switch (operands.length) {
      case 0:
        return new BooleanTrueBeed();
      default:
        return and(operands, 0);
    }
  }

  /**
   * @pre operands != null;
   * @pre operands.length >= 1;
   * @pre start >= 0;
   * @pre start < operands.length;
   */
  private static BooleanBeed and(BooleanBeed[] operands, int start) {
    if (start < operands.length - 1) {
      // recursion: and(operands, start) = operands[start] AND and(operands, start + 1);
      BooleanANDBeed andBeed = new BooleanANDBeed();
      andBeed.setLeftOperandPath(fix(operands[start]));
      andBeed.setRightOperandPath(fix(and(operands, start + 1)));
      return andBeed;
    }
    else {
      assert start == operands.length - 1;
      return operands[start];
    }
  }

  /*</section>*/


  /*<section name="or">*/
  //------------------------------------------------------------------

  public static BooleanBeed or(BooleanBeed... operands) {
    switch (operands.length) {
      case 0:
        return new BooleanFalseBeed();
      default:
        return or(operands, 0);
    }
  }

  /**
   * @pre operands != null;
   * @pre operands.length >= 1;
   * @pre start >= 0;
   * @pre start < operands.length;
   */
  private static BooleanBeed or(BooleanBeed[] operands, int start) {
    if (start < operands.length - 1) {
      // recursion: or(operands, start) = operands[start] OR or(operands, start + 1);
      BooleanORBeed orBeed = new BooleanORBeed();
      orBeed.setLeftOperandPath(fix(operands[start]));
      orBeed.setRightOperandPath(fix(or(operands, start + 1)));
      return orBeed;
    }
    else {
      assert start == operands.length - 1;
      return operands[start];
    }
  }

  /*</section>*/


  /*<section name="notNull">*/
  //------------------------------------------------------------------

  public static BooleanBeed notNull(RealBeed<?> operand) {
    BooleanNotNullBeed notNullBeed = new BooleanNotNullBeed();
    notNullBeed.setOperandPath(fix(operand));
    return notNullBeed;
  }

  /*</section>*/


  /*<section name="equal">*/
  //------------------------------------------------------------------

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed equal(
      _BeanBeed_ beanBeed1, _BeanBeed_ beanBeed2) {
    return equal(fix(beanBeed1), fix(beanBeed2));
  }

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed equal(
      _BeanBeed_ beanBeed1, Path<? extends _BeanBeed_> beanBeedPath2) {
    return equal(fix(beanBeed1), beanBeedPath2);
  }

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed equal(
      Path<? extends _BeanBeed_> beanBeedPath1, _BeanBeed_ beanBeed2) {
    return equal(beanBeedPath1, fix(beanBeed2));
  }

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed equal(
      Path<? extends _BeanBeed_> beanBeedPath1, Path<? extends _BeanBeed_> beanBeedPath2) {
    BooleanEqualBeanBeedsBeed<_BeanBeed_> equalBeed =
      new BooleanEqualBeanBeedsBeed<_BeanBeed_>();
    equalBeed.setLeftOperandPath(beanBeedPath1);
    equalBeed.setRightOperandPath(beanBeedPath2);
    return equalBeed;
  }

  /*</section>*/


  /*<section name="instanceOf">*/
  //------------------------------------------------------------------

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed instanceOf(
      _BeanBeed_ beanBeed, Class beedClass) {
    return instanceOf(fix(beanBeed), beedClass);
  }

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed instanceOf(
      Path<? extends _BeanBeed_> beanBeedPath, Class beedClass) {
    BooleanInstanceofBeed<_BeanBeed_> instanceofBeed =
      new BooleanInstanceofBeed<_BeanBeed_>(beedClass);
    instanceofBeed.setBeedPath(beanBeedPath);
    return instanceofBeed;
  }

  /*</section>*/


  /*<section name="notNull">*/
  //------------------------------------------------------------------

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed notNull(_BeanBeed_ beanBeed) {
    return notNull(fix(beanBeed));
  }

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed notNull(
      Path<? extends _BeanBeed_> beanBeedPath) {
    BooleanNotNullBeanBeedBeed<_BeanBeed_> notNullBeed =
      new BooleanNotNullBeanBeedBeed<_BeanBeed_>();
    notNullBeed.setBeedPath(beanBeedPath);
    return notNullBeed;
  }

  /*</section>*/
}

