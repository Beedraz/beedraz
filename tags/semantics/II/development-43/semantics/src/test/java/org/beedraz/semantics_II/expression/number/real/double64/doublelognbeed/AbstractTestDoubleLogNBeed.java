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

package org.beedraz.semantics_II.expression.number.real.double64.doublelognbeed;


import static org.junit.Assert.assertEquals;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.expression.number.real.double64.AbstractTestDoubleConstantUnaryExpressionBeed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleConstantBeed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleLnBeed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleLog10Beed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleLogNBeed;
import org.beedraz.semantics_II.path.ConstantPath;
import org.beedraz.semantics_II.path.Path;
import org.junit.Test;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractTestDoubleLogNBeed
    extends AbstractTestDoubleConstantUnaryExpressionBeed<DoubleLogNBeed> {

  protected AbstractTestDoubleLogNBeed(double constant) {
    super(constant);
  }

  @Test
  public void testConstructor() {
    DoubleLogNBeed lb = new DoubleLogNBeed($constant);
    validateConstructor(lb);
  }

  @Override
  protected DoubleLogNBeed createSubject() {
    return new DoubleLogNBeed($constant);
  }

  @Override
  protected final Double expectedValueNotNull(Double operandValue) {
    return Math.log(operandValue) / Math.log($constant);
  }

  @Test
  public void compareWithLnBeed() {
    double[] values =
      new double[] {1, 3.3, 4.4, 55.55, Double.NaN,
                    Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
    DoubleLnBeed lnBeed = new DoubleLnBeed();
    DoubleLogNBeed logNBeed = new DoubleLogNBeed(Math.E);
    for (double d : values) {
      DoubleConstantBeed constantBeed = new DoubleConstantBeed(d);
      Path<DoubleConstantBeed> constantBeedPath = new ConstantPath<DoubleConstantBeed>(constantBeed);
      lnBeed.setOperandPath(constantBeedPath);
      logNBeed.setOperandPath(constantBeedPath);
      assertEquals(lnBeed.getDouble(), logNBeed.getDouble());
    }
  }

  @Test
  public void compareWithLog10Beed() {
    double[] values =
      new double[] {1, 3.3, 4.4, 55.55, Double.NaN,
                    Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
    DoubleLog10Beed log10Beed = new DoubleLog10Beed();
    DoubleLogNBeed logNBeed = new DoubleLogNBeed(10);
    for (double d : values) {
      DoubleConstantBeed constantBeed = new DoubleConstantBeed(d);
      Path<DoubleConstantBeed> constantBeedPath = new ConstantPath<DoubleConstantBeed>(constantBeed);
      log10Beed.setOperandPath(constantBeedPath);
      logNBeed.setOperandPath(constantBeedPath);
      assertEquals(log10Beed.getDouble(), logNBeed.getDouble());
    }
  }

}
