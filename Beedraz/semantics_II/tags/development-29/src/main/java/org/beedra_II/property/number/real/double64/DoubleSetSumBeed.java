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
import org.beedra_II.property.collection.set.SetBeed;
import org.beedra_II.property.number.real.RealBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed that computes the sum of a given set of beeds of type
 * {@link DoubleBeed}.
 *
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() == sum { db.getDouble() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective,
 *        then the value of the sum beed is the sum of the values of all beeds
 *        in the given set. The sum of an empty set is zero.
 *        e.g. getDouble() = 5.1 + 3.2 + 4.9
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleSetSumBeed extends DoubleSetComputationBeed {

  public static final double DOUBLE_ZERO = 0.0;


  /**
   * @pre   owner != null;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoubleSetSumBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * The value of this beed is recalculated.
   * See {@link #sum(SetBeed)}.
   */
  @Override
  public void recalculate() {
    setValue(sum(getSource()));
  }

  /**
   * Compute the sum of the values of the double beeds in the given
   * set beed.
   * This is done by iterating over the beeds in the source set beed.
   * When the source is null, the result is null.
   * When the source contains zero beeds, the result is zero.
   * When one of the terms is null, the result is null.
   * When all terms are effective, the result is the sum
   * of the values of the beeds.
   */
  public static Double sum(final SetBeed<RealBeed<?>, ?> source) {
    Double sum;
    if (source == null) {
      sum = null;
    }
    else if (source.get().size() == 0) {
      sum = DOUBLE_ZERO;
    }
    else {
      // assert source != null;
      assert source.get().size() > 0;
      sum = DOUBLE_ZERO;
      for (RealBeed<?> realBeed : source.get()) {
        Double value = realBeed.getDouble();
        if (value == null) {
          sum = null;
          break;
        }
        sum += value; // autoboxing
      }
    }
    return sum;
  }

}

