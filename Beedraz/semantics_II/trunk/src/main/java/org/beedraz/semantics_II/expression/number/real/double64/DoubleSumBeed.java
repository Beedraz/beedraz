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


import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that is the sum of zero or more other beeds of type
 * {@link DoubleBeed}.
 *
 * @invar (forAll DoubleBeed db; ; getNbOccurrences(db) > 0 ==> db.getDouble() != null)
 *            ==> getDouble() == sum { db.getDouble() * getNbOccurrences(db) | db instanceof DoubleBeed};
 *        If all terms are effective, then the value of the sum beed is the
 *        sum of the value of each term multiplied by the corresponding
 *        number of occurrences.
 *        e.g. getDouble() = 3 * 5.2 + 2 * 11.3
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleSumBeed extends AbstractDoubleCommutativeOperationBeed {

  /**
   * @post  getDouble() == 0;
   * @post  (forall DoubleBeed db; ; getNbOccurrences(db) == 0};
   */
  public DoubleSumBeed() {
    super();
  }

  @Override
  protected double operation(double arg1, double arg2) {
    return arg1 + arg2;
  }

  @Override
  public double initialValue() {
    return 0.0;
  }

  @Override
  public String argumentsToString() {
    return "terms";
  }

  @Override
  public final String getOperatorString() {
    return "+";
  }

  @Override
  public final String getNbOccurrencesOperatorString() {
    return "*";
  }

}

