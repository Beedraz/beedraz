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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * A beed that is the {@link #getExponent() exponent-power}
 * of a {@link #getBase() base} (<code>base<sup>exponent</sup></code>).
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class DoubleExponentBeed
    extends AbstractRealArgDoubleBinaryExpressionBeed {

  /**
   * @post  getDouble() == null;
   * @post  getBase() == null;
   * @post  getExponent() == null;
   */
  public DoubleExponentBeed() {
    this(null);
  }

  /**
   * @post  getDouble() == null;
   * @post  getBase() == null;
   * @post  getExponent() == null;
   * @post  owner != null ? owner.registerAggregateElement(this);
   */
  public DoubleExponentBeed(AggregateBeed owner) {
    super(owner);
  }


  /*<property name="base">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getBasePath() {
    return getLeftOprndPath();
  }

  /**
   * @return getBasePath() == null ? null : getBasePath().get();
   */
  public final RealBeed<?> getBase() {
    return getLeftOprnd();
  }

  /**
   * @post getBasePath() == basePath;
   */
  public final void setBasePath(Path<? extends RealBeed<?>> basePath) {
    setLeftOprndPath(basePath);
  }

  /*</property>*/



  /*<property name="exponent">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends RealBeed<?>> getExponentPath() {
    return getRightOprndPath();
  }

  /**
   * @return getExponentPath() == null ? null : getExponentPath().get();
   */
  public final RealBeed<?> getExponent() {
    return getRightOprnd();
  }

  /**
   * @post getExponentPath() == exponentPath;
   */
  public final void setExponentPath(Path<? extends RealBeed<?>> exponentPath) {
    setRightOprndPath(exponentPath);
  }

  /*</property>*/



  /**
   * @pre base != null;
   * @pre exponent != null;
   */
  @Override
  protected final double calculateValue(double base, double exponent) {
    return Math.pow(base, exponent);
  }

  @Override
  public final String getOperatorString() {
    return "^";
  }
}

