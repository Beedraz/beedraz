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
 * A beed that computes the arithmetic mean of a given set of beeds of type
 * {@link DoubleBeed}.
 *
 * @invar getSource() != null ==>
 *        (forAll DoubleBeed db; getSource().get().contains(db); db.getDouble() != null)
 *            ==> getDouble() == avg { db.getDouble() | getSource().get().contains(db)};
 *        If the values of all beeds in the given set are effective,
 *        then the value of the arithmetic mean beed is the arithmetic mean of
 *        the values of all beeds in the given set. The mean of an empty set is NaN.
 *        e.g. getDouble() = (5.1 + 3.2 + 4.9) / 3
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class DoubleArithmeticMeanBeed extends DoubleSetComputationBeed {


  /**
   * @pre   owner != null;
   * @post  getSource() == null;
   * @post  getDouble() == null;
   */
  public DoubleArithmeticMeanBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * The value of this beed is recalculated.
   * See {@link #mean(SetBeed)}.
   */
  @Override
  public void recalculate() {
    setValue(mean(getSource()));
  }

  /**
   * Compute the arithmetic mean of the values of the double beeds in the given
   * set beed.
   * This is done by iterating over the beeds in the source set beed.
   * When the source is null, the result is null.
   * When the source contains zero beeds, the result is {@link Double.NaN}.
   * When one of the terms is null, the result is null.
   * When all terms are effective, the result is the arithmetic mean
   * of the values of the beeds.
   *
   * @mudo use commans math instead
   */
  public static Double mean(final SetBeed<RealBeed<?>, ?> source) {
    Double mean;
    if (source == null) {
      mean = null;
    }
    else if (source.get().size() == 0) {
      mean = Double.NaN;
    }
    else {
      // assert source != null;
      assert source.get().size() > 0;
      mean = 0.0;
      for (RealBeed<?> realBeed : source.get()) {
        Double value = realBeed.getDouble();
        if (value == null) {
          mean = null;
          break;
        }
        mean += value; // autoboxing
      }
      if (mean != null) {
        mean = mean / source.get().size(); // divisor is not zero (see if-condition)!
      }
    }
    return mean;
  }

}

