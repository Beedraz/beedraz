/*<license>
Copyright 2007 - $Date: 2007-07-27 01:49:28 +0200 (Fri, 27 Jul 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression.number.real.double64.stat;


import static org.beedraz.semantics_II.path.Paths.path;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Convenience methods for working with statistical {@link DoubleBeed DoubleBeeds}.</p>
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date: 2007-07-27 01:49:28 +0200 (Fri, 27 Jul 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1046 $",
         date     = "$Date: 2007-07-27 01:49:28 +0200 (Fri, 27 Jul 2007) $")
public final class DoubleStatBeeds {

  /**
   * Don't instantiate this class.
   */
  private DoubleStatBeeds() {
    // NOP
  }


  /*<section name="product">*/
  //------------------------------------------------------------------

  public static DoubleBeed product(Path<? extends SetBeed<RealBeed<?>, ?>> factorsPath) {
    DoubleSetProductBeed productBeed = new DoubleSetProductBeed();
    productBeed.setSourcePath(factorsPath);
    return productBeed;
  }

  public static DoubleBeed product(SetBeed<RealBeed<?>, ?> factors) {
    return product(path(factors));
  }

  /*</section>*/



  /*<section name="sum">*/
  //------------------------------------------------------------------

  public static DoubleBeed sum(Path<? extends SetBeed<RealBeed<?>, ?>> termsPath) {
    DoubleSetSumBeed sumBeed = new DoubleSetSumBeed();
    sumBeed.setSourcePath(termsPath);
    return sumBeed;
  }

  public static DoubleBeed sum(SetBeed<RealBeed<?>, ?> terms) {
    return sum(path(terms));
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

}

