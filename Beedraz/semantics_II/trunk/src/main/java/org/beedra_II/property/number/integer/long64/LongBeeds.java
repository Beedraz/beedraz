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

package org.beedra_II.property.number.integer.long64;


import static org.beedra_II.path.Paths.fix;

import org.beedra_II.path.Path;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Convenience methods for working with {@link LongBeed LongBeeds}.</p>
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class LongBeeds {


  /*<section name="negative">*/
  //------------------------------------------------------------------

  public static LongBeed negative(IntegerBeed<?> operand) {
    return negative(fix(operand));
  }

  public static LongBeed negative(Path<? extends IntegerBeed<?>> operandPath) {
    LongNegativeBeed negativeBeed = new LongNegativeBeed();
    negativeBeed.setOperandPath(operandPath);
    return negativeBeed;
  }

  /*</section>*/


  /*<section name="sum">*/
  //------------------------------------------------------------------

  /**
   * @pre terms != null;
   */
  public static LongBeed sum(IntegerBeed<?>... terms) {
    LongSumBeed sumBeed = new LongSumBeed();
    for (IntegerBeed<?> term : terms) {
      sumBeed.addTerm(term);
    }
    return sumBeed;
  }

  /*</section>*/

}

