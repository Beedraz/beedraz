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
 * <p>Convenience methods for working with {@link BooleanBeed boolean beeds}.</p>
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


  /*<section name="true - false">*/
  //------------------------------------------------------------------

  public static BooleanBeed trueB() {
    return new BooleanTrueBeed();
  }

  public static BooleanBeed falseB() {
    return new BooleanFalseBeed();
  }

  /*</section>*/


  /*<section name="not">*/
  //------------------------------------------------------------------

  public static BooleanBeed not(BooleanBeed operand) {
    return not(fix(operand));
  }

  public static BooleanBeed not(Path<? extends BooleanBeed> operandPath) {
    BooleanNotBeed notBeed = new BooleanNotBeed();
    notBeed.setOperandPath(operandPath);
    return notBeed;
  }

  /*</section>*/


  /*<section name="and">*/
  //------------------------------------------------------------------

  /**
   * @pre operands != null;
   */
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


  /*<section name="xor">*/
  //------------------------------------------------------------------

  public static BooleanBeed xor(BooleanBeed... operands) {
    switch (operands.length) {
      case 0:
        return new BooleanFalseBeed();
      default:
        return xor(operands, 0);
    }
  }

  /**
   * @pre operands != null;
   * @pre operands.length >= 1;
   * @pre start >= 0;
   * @pre start < operands.length;
   */
  private static BooleanBeed xor(BooleanBeed[] operands, int start) {
    if (start < operands.length - 1) {
      // recursion: xor(operands, start) = operands[start] XOR xor(operands, start + 1);
      BooleanXORBeed xorBeed = new BooleanXORBeed();
      xorBeed.setLeftOperandPath(fix(operands[start]));
      xorBeed.setRightOperandPath(fix(xor(operands, start + 1)));
      return xorBeed;
    }
    else {
      assert start == operands.length - 1;
      return operands[start];
    }
  }

  /*</section>*/


  /*<section name="null">*/
  //------------------------------------------------------------------

  public static BooleanBeed nullV(RealBeed<?> operand) {
    return nullV(fix(operand));
  }

  public static BooleanBeed nullV(Path<? extends RealBeed<?>> operandPath) {
    BooleanNullBeed nullBeed = new BooleanNullBeed();
    nullBeed.setOperandPath(operandPath);
    return nullBeed;
  }

  /*</section>*/


  /*<section name="notNull">*/
  //------------------------------------------------------------------

  public static BooleanBeed notNullV(RealBeed<?> operand) {
    return notNullV(fix(operand));
  }

  public static BooleanBeed notNullV(Path<? extends RealBeed<?>> operandPath) {
    BooleanNotNullBeed notNullBeed = new BooleanNotNullBeed();
    notNullBeed.setOperandPath(operandPath);
    return notNullBeed;
  }

  /*</section>*/


  /*<section name="equalB">*/
  //------------------------------------------------------------------

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed equalB(
      _BeanBeed_ beanBeed1, _BeanBeed_ beanBeed2) {
    return equalB(fix(beanBeed1), fix(beanBeed2));
  }

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed equalB(
      _BeanBeed_ beanBeed1, Path<? extends _BeanBeed_> beanBeedPath2) {
    return equalB(fix(beanBeed1), beanBeedPath2);
  }

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed equalB(
      Path<? extends _BeanBeed_> beanBeedPath1, _BeanBeed_ beanBeed2) {
    return equalB(beanBeedPath1, fix(beanBeed2));
  }

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed equalB(
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

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed notNullB(_BeanBeed_ beanBeed) {
    return notNullB(fix(beanBeed));
  }

  public static <_BeanBeed_ extends BeanBeed> BooleanBeed notNullB(
      Path<? extends _BeanBeed_> beanBeedPath) {
    BooleanNotNullBeanBeedBeed<_BeanBeed_> notNullBeed =
      new BooleanNotNullBeanBeedBeed<_BeanBeed_>();
    notNullBeed.setBeedPath(beanBeedPath);
    return notNullBeed;
  }

  /*</section>*/


  /*<section name="equalV">*/
  //------------------------------------------------------------------

  public static BooleanBeed equalV(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    return equalV(fix(leftOperand), fix(rightOperand));
  }

  public static BooleanBeed equalV(
      Path<? extends RealBeed<?>> leftOperandPath,
      Path<? extends RealBeed<?>> rightOperandPath) {
    BooleanEQBeed eqBeed = new BooleanEQBeed();
    eqBeed.setLeftOperandPath(leftOperandPath);
    eqBeed.setRightOperandPath(rightOperandPath);
    return eqBeed;
  }

  /*</section>*/


  /*<section name="ne">*/
  //------------------------------------------------------------------

  public static BooleanBeed ne(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    return ne(fix(leftOperand), fix(rightOperand));
  }

  public static BooleanBeed ne(
      Path<? extends RealBeed<?>> leftOperandPath,
      Path<? extends RealBeed<?>> rightOperandPath) {
    BooleanNEBeed neBeed = new BooleanNEBeed();
    neBeed.setLeftOperandPath(leftOperandPath);
    neBeed.setRightOperandPath(rightOperandPath);
    return neBeed;
  }

  /*</section>*/


  /*<section name="ge">*/
  //------------------------------------------------------------------

  public static BooleanBeed ge(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    return ge(fix(leftOperand), fix(rightOperand));
  }

  public static BooleanBeed ge(
      Path<? extends RealBeed<?>> leftOperandPath,
      Path<? extends RealBeed<?>> rightOperandPath) {
    BooleanGEBeed geBeed = new BooleanGEBeed();
    geBeed.setLeftOperandPath(leftOperandPath);
    geBeed.setRightOperandPath(rightOperandPath);
    return geBeed;
  }

  /*</section>*/


  /*<section name="gt">*/
  //------------------------------------------------------------------

  public static BooleanBeed gt(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    return gt(fix(leftOperand), fix(rightOperand));
  }

  public static BooleanBeed gt(
      Path<? extends RealBeed<?>> leftOperandPath,
      Path<? extends RealBeed<?>> rightOperandPath) {
    BooleanGTBeed gtBeed = new BooleanGTBeed();
    gtBeed.setLeftOperandPath(leftOperandPath);
    gtBeed.setRightOperandPath(rightOperandPath);
    return gtBeed;
  }

  /*</section>*/


  /*<section name="le">*/
  //------------------------------------------------------------------

  public static BooleanBeed le(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    return le(fix(leftOperand), fix(rightOperand));
  }

  public static BooleanBeed le(
      Path<? extends RealBeed<?>> leftOperandPath,
      Path<? extends RealBeed<?>> rightOperandPath) {
    BooleanLEBeed leBeed = new BooleanLEBeed();
    leBeed.setLeftOperandPath(leftOperandPath);
    leBeed.setRightOperandPath(rightOperandPath);
    return leBeed;
  }

  /*</section>*/


  /*<section name="lt">*/
  //------------------------------------------------------------------

  public static BooleanBeed lt(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    return lt(fix(leftOperand), fix(rightOperand));
  }

  public static BooleanBeed lt(
      Path<? extends RealBeed<?>> leftOperandPath,
      Path<? extends RealBeed<?>> rightOperandPath) {
    BooleanLTBeed ltBeed = new BooleanLTBeed();
    ltBeed.setLeftOperandPath(leftOperandPath);
    ltBeed.setRightOperandPath(rightOperandPath);
    return ltBeed;
  }

  /*</section>*/
}

