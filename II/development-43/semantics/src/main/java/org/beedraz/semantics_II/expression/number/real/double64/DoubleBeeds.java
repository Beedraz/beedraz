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

package org.beedraz.semantics_II.expression.number.real.double64;


import static org.beedraz.semantics_II.path.Paths.path;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleArithmeticMeanBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleGeometricMeanBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleMaxBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleMinBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoublePopulationGeometricStandardDeviationBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoublePopulationGeometricStandardErrorBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoublePopulationStandardDeviationBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoublePopulationStandardErrorBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoublePopulationVarianceBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleSampleGeometricStandardDeviationBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleSampleGeometricStandardErrorBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleSampleStandardDeviationBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleSampleStandardErrorBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleSampleVarianceBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleSetProductBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleSetSumBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Convenience methods for working with {@link DoubleBeed DoubleBeeds}.</p>
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoubleBeeds {

  /*<section name="constant">*/
  //------------------------------------------------------------------

  public static DoubleBeed constant(double constant) {
    return new DoubleConstantBeed(constant);
  }

  public static DoubleBeed constant_null() {
    return new DoubleConstantBeed(null);
  }

  /*</section>*/


  /*<section name="diff">*/
  //------------------------------------------------------------------

  public static DoubleBeed diff(RealBeed<?> positiveTerm, RealBeed<?> negativeTerm) {
    return diff(path(positiveTerm), path(negativeTerm));
  }

  public static DoubleBeed diff(Path<? extends RealBeed<?>> positiveTermPath, RealBeed<?> negativeTerm) {
    return diff(positiveTermPath, path(negativeTerm));
  }

  public static DoubleBeed diff(RealBeed<?> positiveTerm, Path<? extends RealBeed<?>> negativeTermPath) {
    return diff(path(positiveTerm), negativeTermPath);
  }

  public static DoubleBeed diff(Path<? extends RealBeed<?>> positiveTermPath,
                                Path<? extends RealBeed<?>> negativeTermPath) {
    DoubleDifferenceBeed diffBeed = new DoubleDifferenceBeed();
    diffBeed.setPositiveTermPath(positiveTermPath);
    diffBeed.setNegativeTermPath(negativeTermPath);
    return diffBeed;
  }

  /*</section>*/


  /*<section name="exp">*/
  //------------------------------------------------------------------

  public static DoubleBeed exp(RealBeed<?> base, RealBeed<?> exponent) {
    return exp(path(base), path(exponent));
  }

  public static DoubleBeed exp(Path<? extends RealBeed<?>> basePath, RealBeed<?> exponent) {
    return exp(basePath, path(exponent));
  }

  public static DoubleBeed exp(RealBeed<?> base, Path<? extends RealBeed<?>> exponentPath) {
    return exp(path(base), exponentPath);
  }

  public static DoubleBeed exp(Path<? extends RealBeed<?>> basePath,
                                Path<? extends RealBeed<?>> exponentPath) {
    DoubleExponentBeed expBeed = new DoubleExponentBeed();
    expBeed.setBasePath(basePath);
    expBeed.setExponentPath(exponentPath);
    return expBeed;
  }

  /*</section>*/


  /*<section name="exp">*/
  //------------------------------------------------------------------

  public static DoubleBeed exp(RealBeed<?> operand) {
    return exp(path(operand));
  }

  public static DoubleBeed exp(Path<? extends RealBeed<?>> operandPath) {
    DoubleEBeed eBeed = new DoubleEBeed();
    eBeed.setOperandPath(operandPath);
    return eBeed;
  }

  /*</section>*/


  /*<section name="inverse">*/
  //------------------------------------------------------------------

  public static DoubleBeed inverse(RealBeed<?> operand) {
    return inverse(path(operand));
  }

  public static DoubleBeed inverse(Path<? extends RealBeed<?>> operandPath) {
    DoubleInverseBeed inverseBeed = new DoubleInverseBeed();
    inverseBeed.setOperandPath(operandPath);
    return inverseBeed;
  }

  /*</section>*/


  /*<section name="ln">*/
  //------------------------------------------------------------------

  public static DoubleBeed ln(RealBeed<?> operand) {
    return ln(path(operand));
  }

  public static DoubleBeed ln(Path<? extends RealBeed<?>> operandPath) {
    DoubleLnBeed lnBeed = new DoubleLnBeed();
    lnBeed.setOperandPath(operandPath);
    return lnBeed;
  }

  /*</section>*/


  /*<section name="log10">*/
  //------------------------------------------------------------------

  public static DoubleBeed log10(RealBeed<?> operand) {
    return log10(path(operand));
  }

  public static DoubleBeed log10(Path<? extends RealBeed<?>> operandPath) {
    DoubleLog10Beed log10Beed = new DoubleLog10Beed();
    log10Beed.setOperandPath(operandPath);
    return log10Beed;
  }

  /*</section>*/


  /*<section name="logN">*/
  //------------------------------------------------------------------

  public static DoubleBeed logN(RealBeed<?> operand, double constant) {
    return logN(path(operand), constant);
  }

  public static DoubleBeed logN(Path<? extends RealBeed<?>> operandPath, double constant) {
    DoubleLogNBeed logNBeed = new DoubleLogNBeed(constant);
    logNBeed.setOperandPath(operandPath);
    return logNBeed;
  }

  /*</section>*/


  /*<section name="mod">*/
  //------------------------------------------------------------------

  public static DoubleBeed mod(RealBeed<?> dividend, RealBeed<?> divisor) {
    return mod(path(dividend), path(divisor));
  }

  public static DoubleBeed mod(Path<? extends RealBeed<?>> dividendPath, RealBeed<?> divisor) {
    return mod(dividendPath, path(divisor));
  }

  public static DoubleBeed mod(RealBeed<?> dividend, Path<? extends RealBeed<?>> divisorPath) {
    return mod(path(dividend), divisorPath);
  }

  public static DoubleBeed mod(Path<? extends RealBeed<?>> dividendPath,
                                Path<? extends RealBeed<?>> divisorPath) {
    DoubleModBeed modBeed = new DoubleModBeed();
    modBeed.setDividendPath(dividendPath);
    modBeed.setDivisorPath(divisorPath);
    return modBeed;
  }

  /*</section>*/


  /*<section name="negative">*/
  //------------------------------------------------------------------

  public static DoubleBeed negative(RealBeed<?> operand) {
    return negative(path(operand));
  }

  public static DoubleBeed negative(Path<? extends RealBeed<?>> operandPath) {
    DoubleNegativeBeed negativeBeed = new DoubleNegativeBeed();
    negativeBeed.setOperandPath(operandPath);
    return negativeBeed;
  }

  /*</section>*/


  /*<section name="power">*/
  //------------------------------------------------------------------

  public static DoubleBeed power(RealBeed<?> operand, double constant) {
    return power(path(operand), constant);
  }

  public static DoubleBeed power(Path<? extends RealBeed<?>> operandPath, double constant) {
    DoublePowerBeed powerBeed = new DoublePowerBeed(constant);
    powerBeed.setOperandPath(operandPath);
    return powerBeed;
  }

  /*</section>*/


  /*<section name="product">*/
  //------------------------------------------------------------------

  /**
   * @pre operands != null;
   */
  public static RealBeed<?> product(RealBeed<?>... operands) {
    switch (operands.length) {
      case 0:
        return constant(1);
      default:
        return product(operands, 0);
    }
  }

  /**
   * @pre operands != null;
   * @pre operands.length >= 1;
   * @pre start >= 0;
   * @pre start < operands.length;
   */
  private static RealBeed<?> product(RealBeed<?>[] operands, int start) {
    if (start < operands.length - 1) {
      // recursion: product(operands, start) = operands[start] AND product(operands, start + 1);
      DoubleProductBeed productBeed = new DoubleProductBeed();
      productBeed.setLeftOperandPath(path(operands[start]));
      productBeed.setRightOperandPath(path(product(operands, start + 1)));
      return productBeed;
    }
    else {
      assert start == operands.length - 1;
      return operands[start];
    }
  }

  public static DoubleBeed product(SetBeed<RealBeed<?>, ?> factors) {
    return product(path(factors));
  }

  public static DoubleBeed product(Path<? extends SetBeed<RealBeed<?>, ?>> factorsPath) {
    DoubleSetProductBeed productBeed = new DoubleSetProductBeed();
    productBeed.setSourcePath(factorsPath);
    return productBeed;
  }

  public static DoubleBeed product(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    return product(path(leftOperand), path(rightOperand));
  }

  public static DoubleBeed product(Path<? extends RealBeed<?>> leftOperandPath, RealBeed<?> rightOperand) {
    return product(leftOperandPath, path(rightOperand));
  }

  public static DoubleBeed product(RealBeed<?> leftOperand, Path<? extends RealBeed<?>> rightOperandPath) {
    return product(path(leftOperand), rightOperandPath);
  }

  public static DoubleBeed product(Path<? extends RealBeed<?>> leftOperandPath,
                                Path<? extends RealBeed<?>> rightOperandPath) {
    DoubleProductBeed productBeed = new DoubleProductBeed();
    productBeed.setLeftOperandPath(leftOperandPath);
    productBeed.setRightOperandPath(rightOperandPath);
    return productBeed;
  }

  /*</section>*/

  /*<section name="quotient">*/
  //------------------------------------------------------------------

  public static DoubleBeed quotient(RealBeed<?> numerator, RealBeed<?> denominator) {
    return quotient(path(numerator), path(denominator));
  }

  public static DoubleBeed quotient(Path<? extends RealBeed<?>> numeratorPath, RealBeed<?> denominator) {
    return quotient(numeratorPath, path(denominator));
  }

  public static DoubleBeed quotient(RealBeed<?> numerator, Path<? extends RealBeed<?>> denominatorPath) {
    return quotient(path(numerator), denominatorPath);
  }

  public static DoubleBeed quotient(Path<? extends RealBeed<?>> numeratorPath,
                                    Path<? extends RealBeed<?>> denominatorPath) {
    DoubleQuotientBeed quotientBeed = new DoubleQuotientBeed();
    quotientBeed.setNumeratorPath(numeratorPath);
    quotientBeed.setDenominatorPath(denominatorPath);
    return quotientBeed;
  }

  /*</section>*/


  /*<section name="root">*/
  //------------------------------------------------------------------

  public static DoubleBeed root(RealBeed<?> operand, double constant) {
    return root(path(operand), constant);
  }

  public static DoubleBeed root(Path<? extends RealBeed<?>> operandPath, double constant) {
    DoubleRootBeed rootBeed = new DoubleRootBeed(constant);
    rootBeed.setOperandPath(operandPath);
    return rootBeed;
  }

  /*</section>*/


  /*<section name="sum">*/
  //------------------------------------------------------------------

  /**
   * @pre operands != null;
   */
  public static RealBeed<?> sum(RealBeed<?>... operands) {
    switch (operands.length) {
      case 0:
        return constant(1);
      default:
        return sum(operands, 0);
    }
  }

  /**
   * @pre operands != null;
   * @pre operands.length >= 1;
   * @pre start >= 0;
   * @pre start < operands.length;
   */
  private static RealBeed<?> sum(RealBeed<?>[] operands, int start) {
    if (start < operands.length - 1) {
      // recursion: product(operands, start) = operands[start] AND product(operands, start + 1);
      DoubleProductBeed productBeed = new DoubleProductBeed();
      productBeed.setLeftOperandPath(path(operands[start]));
      productBeed.setRightOperandPath(path(product(operands, start + 1)));
      return productBeed;
    }
    else {
      assert start == operands.length - 1;
      return operands[start];
    }
  }

  public static DoubleBeed sum(SetBeed<RealBeed<?>, ?> terms) {
    return sum(path(terms));
  }

  public static DoubleBeed sum(Path<? extends SetBeed<RealBeed<?>, ?>> termsPath) {
    DoubleSetSumBeed sumBeed = new DoubleSetSumBeed();
    sumBeed.setSourcePath(termsPath);
    return sumBeed;
  }

  public static DoubleBeed sum(RealBeed<?> leftOperand, RealBeed<?> rightOperand) {
    return sum(path(leftOperand), path(rightOperand));
  }

  public static DoubleBeed sum(Path<? extends RealBeed<?>> leftOperandPath, RealBeed<?> rightOperand) {
    return sum(leftOperandPath, path(rightOperand));
  }

  public static DoubleBeed sum(RealBeed<?> leftOperand, Path<? extends RealBeed<?>> rightOperandPath) {
    return sum(path(leftOperand), rightOperandPath);
  }

  public static DoubleBeed sum(Path<? extends RealBeed<?>> leftOperandPath,
                               Path<? extends RealBeed<?>> rightOperandPath) {
    DoubleSumBeed sumBeed = new DoubleSumBeed();
    sumBeed.setLeftOperandPath(leftOperandPath);
    sumBeed.setRightOperandPath(rightOperandPath);
    return sumBeed;
  }

  /*</section>*/



  /*<section name="avg">*/
  //------------------------------------------------------------------

  public static DoubleBeed avg(SetBeed<RealBeed<?>, ?> source) {
    return avg(path(source));
  }

  public static DoubleBeed avg(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoubleArithmeticMeanBeed meanBeed = new DoubleArithmeticMeanBeed();
    meanBeed.setSourcePath(sourcePath);
    return meanBeed;
  }

  /*</section>*/


  /*<section name="var_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed var_p(SetBeed<RealBeed<?>, ?> source) {
    return var_p(path(source));
  }

  public static DoubleBeed var_p(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoublePopulationVarianceBeed varBeed = new DoublePopulationVarianceBeed();
    varBeed.setSourcePath(sourcePath);
    return varBeed;
  }

  /*</section>*/


  /*<section name="var_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed var_s(SetBeed<RealBeed<?>, ?> source) {
    return var_s(path(source));
  }

  public static DoubleBeed var_s(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoubleSampleVarianceBeed varBeed = new DoubleSampleVarianceBeed();
    varBeed.setSourcePath(sourcePath);
    return varBeed;
  }

  /*</section>*/


  /*<section name="sd_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed sd_p(SetBeed<RealBeed<?>, ?> source) {
    return sd_p(path(source));
  }

  public static DoubleBeed sd_p(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoublePopulationStandardDeviationBeed sdBeed = new DoublePopulationStandardDeviationBeed();
    sdBeed.setSourcePath(sourcePath);
    return sdBeed;
  }

  /*</section>*/


  /*<section name="sd_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed sd_s(SetBeed<RealBeed<?>, ?> source) {
    return sd_s(path(source));
  }

  public static DoubleBeed sd_s(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoubleSampleStandardDeviationBeed sdBeed = new DoubleSampleStandardDeviationBeed();
    sdBeed.setSourcePath(sourcePath);
    return sdBeed;
  }

  /*</section>*/


  /*<section name="se_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed se_p(SetBeed<RealBeed<?>, ?> source) {
    return se_p(path(source));
  }

  public static DoubleBeed se_p(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoublePopulationStandardErrorBeed seBeed = new DoublePopulationStandardErrorBeed();
    seBeed.setSourcePath(sourcePath);
    return seBeed;
  }

  /*</section>*/


  /*<section name="se_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed se_s(SetBeed<RealBeed<?>, ?> source) {
    return se_s(path(source));
  }

  public static DoubleBeed se_s(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoubleSampleStandardErrorBeed seBeed = new DoubleSampleStandardErrorBeed();
    seBeed.setSourcePath(sourcePath);
    return seBeed;
  }

  /*</section>*/


  /*<section name="gavg">*/
  //------------------------------------------------------------------

  public static DoubleBeed gavg(SetBeed<RealBeed<?>, ?> source) {
    return gavg(path(source));
  }

  public static DoubleBeed gavg(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoubleGeometricMeanBeed meanBeed = new DoubleGeometricMeanBeed();
    meanBeed.setSourcePath(sourcePath);
    return meanBeed;
  }
  /*</section>*/


  /*<section name="gsd_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed gsd_p(SetBeed<RealBeed<?>, ?> source) {
    return gsd_p(path(source));
  }

  public static DoubleBeed gsd_p(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoublePopulationGeometricStandardDeviationBeed sdBeed =
      new DoublePopulationGeometricStandardDeviationBeed();
    sdBeed.setSourcePath(sourcePath);
    return sdBeed;
  }

  /*</section>*/


  /*<section name="gsd_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed gsd_s(SetBeed<RealBeed<?>, ?> source) {
    return gsd_s(path(source));
  }

  public static DoubleBeed gsd_s(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoubleSampleGeometricStandardDeviationBeed sdBeed =
      new DoubleSampleGeometricStandardDeviationBeed();
    sdBeed.setSourcePath(sourcePath);
    return sdBeed;
  }

  /*</section>*/


  /*<section name="gse_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed gse_p(SetBeed<RealBeed<?>, ?> source) {
    return gse_p(path(source));
  }

  public static DoubleBeed gse_p(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoublePopulationGeometricStandardErrorBeed seBeed =
      new DoublePopulationGeometricStandardErrorBeed();
    seBeed.setSourcePath(sourcePath);
    return seBeed;
  }
  /*</section>*/


  /*<section name="gse_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed gse_s(SetBeed<RealBeed<?>, ?> source) {
    return gse_s(path(source));
  }

  public static DoubleBeed gse_s(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoubleSampleGeometricStandardErrorBeed seBeed =
      new DoubleSampleGeometricStandardErrorBeed();
    seBeed.setSourcePath(sourcePath);
    return seBeed;
  }

  /*</section>*/


  /*<section name="max">*/
  //------------------------------------------------------------------

  public static DoubleBeed max(SetBeed<RealBeed<?>, ?> source) {
    return max(path(source));
  }

  public static DoubleBeed max(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoubleMaxBeed maxBeed = new DoubleMaxBeed();
    maxBeed.setSourcePath(sourcePath);
    return maxBeed;
  }
  /*</section>*/


  /*<section name="min">*/
  //------------------------------------------------------------------

  public static DoubleBeed min(SetBeed<RealBeed<?>, ?> source) {
    return min(path(source));
  }

  public static DoubleBeed min(Path<? extends SetBeed<RealBeed<?>, ?>> sourcePath) {
    DoubleMinBeed minBeed = new DoubleMinBeed();
    minBeed.setSourcePath(sourcePath);
    return minBeed;
  }

  /*</section>*/


  /*<section name="editableDoubleBeed">*/
  //------------------------------------------------------------------

  public static EditableDoubleBeed editableDoubleBeed(double constant, AggregateBeed owner) throws IllegalEditException {
    try {
      EditableDoubleBeed editableDoubleBeed = new EditableDoubleBeed(owner);
      DoubleEdit edit = new DoubleEdit(editableDoubleBeed);
      edit.setGoal(constant);
      edit.perform();
      return editableDoubleBeed;
    }
    catch (EditStateException e) {
      assert false : "Shouldn't happen";
      return null;
    }
  }

  /*</section>*/





}

