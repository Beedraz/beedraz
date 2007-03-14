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

package org.beedra_II.property.decimal;


import org.beedra_II.aggregate.AggregateBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that computes the standard error of a given set of beeds of type
 * {@link DoubleBeed}.
 *
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() == SE { db.getDouble() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective,
 *        then the value of the standard error beed is the standard error of the values of
 *        all beeds in the given set. The standard error of an empty set or of a set containing
 *        only one element is null.
 *        e.g. when  getSource() = {1, 2, 3, 4}
 *             then  getDouble() = Math.sqrt(dividend/divisor)
 *             where divisor = 4*3
 *             and   dividend = (1-mean)^2 + (2-mean)^2 + (3-mean)^2 + (4-mean)^2)
 *             and   mean = (1 + 2 + 3 + 4)/4
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleStandardErrorBeed extends DoubleSetComputationBeed {


  /**
   * @pre   owner != null;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoubleStandardErrorBeed(AggregateBeed owner) {
    super(owner);
  }


  /**
   * The value of $value is recalculated. This is done by iterating over the beeds
   * in the source set beed.
   * When the source is null, the result is null.
   * When the source contains zero or one beeds, the result is null.
   * When one of the terms is null, the result is null.
   * When all terms are effective, the result is the standard error of the values
   * of the beeds.
   */
  public void recalculate() {
    Double standardError;
    if (getSource() == null) {
      standardError = null;
    }
    else if (getSource().get().size() == 0 ||
             getSource().get().size() == 1) {
      standardError = null;
    }
    else {
      assert getSource() != null;
      assert getSource().get().size() > 1;
      // compute the average
      Double average = DoubleMeanBeed.mean(getSource());
      if (average != null) {
        // we know here that the values of all beeds are effective, so we do not need
        // to check this
        standardError = 0.0;
        for (DoubleBeed<DoubleEvent> beed : getSource().get()) {
          Double beedValue = beed.getDouble();
          standardError += Math.pow(beedValue - average, 2); // autoboxing
        }
        int size = getSource().get().size();
        standardError = standardError / (size * (size - 1)); // divisor is not zero (see if-condition)!
        standardError = Math.sqrt(standardError);
      }
      else {
        standardError = null;
      }
    }
    setValue(standardError);
  }

}

