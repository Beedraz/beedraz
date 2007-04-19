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

package org.beedra_II.property.number.real.double64;


import static org.beedra_II.path.Paths.fix;

import org.beedra_II.path.Path;
import org.beedra_II.property.collection.set.SetBeed;
import org.beedra_II.property.number.real.RealBeed;
import org.beedra_II.property.number.real.double64.stat.DoubleArithmeticMeanBeed;
import org.beedra_II.property.number.real.double64.stat.DoubleGeometricMeanBeed;
import org.beedra_II.property.number.real.double64.stat.DoubleMaxBeed;
import org.beedra_II.property.number.real.double64.stat.DoubleMinBeed;
import org.beedra_II.property.number.real.double64.stat.DoublePopulationGeometricStandardDeviationBeed;
import org.beedra_II.property.number.real.double64.stat.DoublePopulationGeometricStandardErrorBeed;
import org.beedra_II.property.number.real.double64.stat.DoublePopulationStandardDeviationBeed;
import org.beedra_II.property.number.real.double64.stat.DoublePopulationStandardErrorBeed;
import org.beedra_II.property.number.real.double64.stat.DoublePopulationVarianceBeed;
import org.beedra_II.property.number.real.double64.stat.DoubleSampleGeometricStandardDeviationBeed;
import org.beedra_II.property.number.real.double64.stat.DoubleSampleGeometricStandardErrorBeed;
import org.beedra_II.property.number.real.double64.stat.DoubleSampleStandardDeviationBeed;
import org.beedra_II.property.number.real.double64.stat.DoubleSampleStandardErrorBeed;
import org.beedra_II.property.number.real.double64.stat.DoubleSampleVarianceBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Convenience methods for working with {@link DoubleBeed double beeds}.</p>
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleBeeds {

  /*<section name="constant">*/
  //------------------------------------------------------------------

  public static DoubleBeed constant(double constant) {
    return new DoubleConstantBeed(constant);
  }

  /*</section>*/


  /*<section name="diff">*/
  //------------------------------------------------------------------

  public static DoubleBeed diff(RealBeed<?> positiveTerm, RealBeed<?> negativeTerm) {
    return diff(fix(positiveTerm), fix(negativeTerm));
  }

  public static DoubleBeed diff(Path<? extends RealBeed<?>> positiveTermPath, RealBeed<?> negativeTerm) {
    return diff(positiveTermPath, fix(negativeTerm));
  }

  public static DoubleBeed diff(RealBeed<?> positiveTerm, Path<? extends RealBeed<?>> negativeTermPath) {
    return diff(fix(positiveTerm), negativeTermPath);
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
    return exp(fix(base), fix(exponent));
  }

  public static DoubleBeed exp(Path<? extends RealBeed<?>> basePath, RealBeed<?> exponent) {
    return exp(basePath, fix(exponent));
  }

  public static DoubleBeed exp(RealBeed<?> base, Path<? extends RealBeed<?>> exponentPath) {
    return exp(fix(base), exponentPath);
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
    return exp(fix(operand));
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
    return inverse(fix(operand));
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
    return ln(fix(operand));
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
    return log10(fix(operand));
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
    return logN(fix(operand), constant);
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
    return mod(fix(dividend), fix(divisor));
  }

  public static DoubleBeed mod(Path<? extends RealBeed<?>> dividendPath, RealBeed<?> divisor) {
    return mod(dividendPath, fix(divisor));
  }

  public static DoubleBeed mod(RealBeed<?> dividend, Path<? extends RealBeed<?>> divisorPath) {
    return mod(fix(dividend), divisorPath);
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
    return negative(fix(operand));
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
    return power(fix(operand), constant);
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
   * @pre factors != null;
   */
  public static DoubleBeed product(RealBeed<?>... factors) {
    DoubleProductBeed productBeed = new DoubleProductBeed();
    for (RealBeed<?> factor : factors) {
      productBeed.addArgument(factor);
    }
    return productBeed;
  }

  /*</section>*/


  /*<section name="quotient">*/
  //------------------------------------------------------------------

  public static DoubleBeed quotient(RealBeed<?> numerator, RealBeed<?> denominator) {
    return quotient(fix(numerator), fix(denominator));
  }

  public static DoubleBeed quotient(Path<? extends RealBeed<?>> numeratorPath, RealBeed<?> denominator) {
    return quotient(numeratorPath, fix(denominator));
  }

  public static DoubleBeed quotient(RealBeed<?> numerator, Path<? extends RealBeed<?>> denominatorPath) {
    return quotient(fix(numerator), denominatorPath);
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
    return root(fix(operand), constant);
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
   * @pre terms != null;
   */
  public static DoubleBeed sum(RealBeed<?>... terms) {
    DoubleSumBeed sumBeed = new DoubleSumBeed();
    for (RealBeed<?> term : terms) {
      sumBeed.addArgument(term);
    }
    return sumBeed;
  }

  /*</section>*/


  /*<section name="a">*/
  //------------------------------------------------------------------

  public static DoubleBeed a(SetBeed<RealBeed<?>, ?> source) {
    DoubleArithmeticMeanBeed meanBeed = new DoubleArithmeticMeanBeed();
    meanBeed.setSource(source);
    return meanBeed;
  }

  /*</section>*/


  /*<section name="var_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed var_p(SetBeed<RealBeed<?>, ?> source) {
    DoublePopulationVarianceBeed varBeed = new DoublePopulationVarianceBeed();
    varBeed.setSource(source);
    return varBeed;
  }

  /*</section>*/


  /*<section name="var_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed var_s(SetBeed<RealBeed<?>, ?> source) {
    DoubleSampleVarianceBeed varBeed = new DoubleSampleVarianceBeed();
    varBeed.setSource(source);
    return varBeed;
  }

  /*</section>*/


  /*<section name="sd_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed sd_p(SetBeed<RealBeed<?>, ?> source) {
    DoublePopulationStandardDeviationBeed sdBeed = new DoublePopulationStandardDeviationBeed();
    sdBeed.setSource(source);
    return sdBeed;
  }

  /*</section>*/


  /*<section name="sd_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed sd_s(SetBeed<RealBeed<?>, ?> source) {
    DoubleSampleStandardDeviationBeed sdBeed = new DoubleSampleStandardDeviationBeed();
    sdBeed.setSource(source);
    return sdBeed;
  }

  /*</section>*/


  /*<section name="se_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed se_p(SetBeed<RealBeed<?>, ?> source) {
    DoublePopulationStandardErrorBeed seBeed = new DoublePopulationStandardErrorBeed();
    seBeed.setSource(source);
    return seBeed;
  }

  /*</section>*/


  /*<section name="se_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed se_s(SetBeed<RealBeed<?>, ?> source) {
    DoubleSampleStandardErrorBeed seBeed = new DoubleSampleStandardErrorBeed();
    seBeed.setSource(source);
    return seBeed;
  }

  /*</section>*/


  /*<section name="g">*/
  //------------------------------------------------------------------

  public static DoubleBeed g(SetBeed<RealBeed<?>, ?> source) {
    DoubleGeometricMeanBeed meanBeed = new DoubleGeometricMeanBeed();
    meanBeed.setSource(source);
    return meanBeed;
  }

  /*</section>*/


  /*<section name="gsd_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed gsd_p(SetBeed<RealBeed<?>, ?> source) {
    DoublePopulationGeometricStandardDeviationBeed sdBeed =
      new DoublePopulationGeometricStandardDeviationBeed();
    sdBeed.setSource(source);
    return sdBeed;
  }

  /*</section>*/


  /*<section name="gsd_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed gsd_s(SetBeed<RealBeed<?>, ?> source) {
    DoubleSampleGeometricStandardDeviationBeed sdBeed =
      new DoubleSampleGeometricStandardDeviationBeed();
    sdBeed.setSource(source);
    return sdBeed;
  }

  /*</section>*/


  /*<section name="gse_p">*/
  //------------------------------------------------------------------

  public static DoubleBeed gse_p(SetBeed<RealBeed<?>, ?> source) {
    DoublePopulationGeometricStandardErrorBeed seBeed =
      new DoublePopulationGeometricStandardErrorBeed();
    seBeed.setSource(source);
    return seBeed;
  }

  /*</section>*/


  /*<section name="gse_s">*/
  //------------------------------------------------------------------

  public static DoubleBeed gse_s(SetBeed<RealBeed<?>, ?> source) {
    DoubleSampleGeometricStandardErrorBeed seBeed =
      new DoubleSampleGeometricStandardErrorBeed();
    seBeed.setSource(source);
    return seBeed;
  }

  /*</section>*/


  /*<section name="max">*/
  //------------------------------------------------------------------

  public static DoubleBeed max(SetBeed<RealBeed<?>, ?> source) {
    DoubleMaxBeed maxBeed = new DoubleMaxBeed();
    maxBeed.setSource(source);
    return maxBeed;
  }

  /*</section>*/


  /*<section name="min">*/
  //------------------------------------------------------------------

  public static DoubleBeed min(SetBeed<RealBeed<?>, ?> source) {
    DoubleMinBeed minBeed = new DoubleMinBeed();
    minBeed.setSource(source);
    return minBeed;
  }

  /*</section>*/





}

