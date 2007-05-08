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


import org.beedra_II.aggregate.AggregateBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that is the product of zero or more other beeds of type
 * {@link DoubleBeed}.
 *
 * @invar (forAll DoubleBeed db; ; getNbOccurrences(db) > 0 ==> db.getDouble() != null)
 *            ==> getDouble() == product { Math.pow(db.getDouble(), getNbOccurrences(db)) | db instanceof DoubleBeed};
 *        If all terms are effective, then the value of the product beed is the
 *        product of the value of each term raised to a certain power, namely the corresponding
 *        number of occurrences.
 *        e.g. getDouble() = Math.pow(5.2, 3) * Math.pow(11.3, 5);
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleProductBeed extends AbstractDoubleCommutativeOperationBeed {

  /**
   * @pre   owner != null;
   * @post  getDouble() == 0;
   * @post  (forall DoubleBeed db; ; getNbOccurrences(db) == 0};
   */
  public DoubleProductBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected double operation(double arg1, double arg2) {
    return arg1 * arg2;
  }

  @Override
  public double initialValue() {
    return 1.0;
  }

  @Override
  public String argumentsToString() {
    return "factors";
  }

}

