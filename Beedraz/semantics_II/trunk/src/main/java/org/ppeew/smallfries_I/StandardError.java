/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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

package org.ppeew.smallfries_I;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.io.Serializable;

import org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Computes the standard error.
 * The standard error is the standard deviation divided by the square root
 * of the number of elements.
 * This implementation wraps a {@link StandardDeviation} instance.
 * The <code>isBiasCorrected</code> property of the wrapped {@link StandardDeviation}
 * instance is exposed, so that this class can be used to
 * compute both the "sample standard error" (using the sample standard deviation)
 * or the "population standard deviation" (using the population standard deviation).
 * See {@link StandardDeviation} for more information.
 *
 * @author Nele Smeets
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@CvsInfo(revision    = "$Revision$",
         date        = "$Date$",
         state       = "$State$",
         tag         = "$Name$")
public class StandardError extends AbstractStorelessUnivariateStatistic
    implements Serializable {

  private static final long serialVersionUID = 3627337040946565285L;

  /**
   * Constructs a StandardError. Sets the underlying {@link StandardDeviation}
   * instance's <code>isBiasCorrected</code> property to true.
   */
  public StandardError() {
      $standardDeviation = new StandardDeviation();
  }

  /**
   * Contructs a StandardError with the specified value for the
   * <code>isBiasCorrected</code> property.  If this property is set to
   * <code>true</code>, the {@link StandardDeviation} used in computing results will
   * use the bias-corrected, or "sample" formula.  See {@link StandardDeviation} for
   * details.
   *
   * @param isBiasCorrected  whether or not the standard error computation will
   *                         use the bias-corrected formula
   */
  public StandardError(boolean isBiasCorrected) {
    $standardDeviation = new StandardDeviation(isBiasCorrected);
  }

  @Override
  public void clear() {
    $standardDeviation.clear();
  }

  @Override
  public long getN() {
    return $standardDeviation.getN();
  }

  @Override
  public double getResult() {
    return $standardDeviation.getResult() / Math.sqrt($standardDeviation.getN());
  }

  @Override
  public void increment(double d) {
    $standardDeviation.increment(d);
  }

  /*<property name="standardDeviation">*/
  //------------------------------------------------------------------

  private StandardDeviation $standardDeviation = null;

  /*</property>*/

}

