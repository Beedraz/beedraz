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

package org.beedraz.semantics_II.expression.number.integer.long64;


import static org.beedraz.semantics_II.path.Paths.path;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.beedraz.semantics_II.path.Path;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Convenience methods for working with {@link LongBeed LongBeeds}.</p>
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class LongBeeds {


  /*<section name="negative">*/
  //------------------------------------------------------------------

  public static LongBeed negative(IntegerBeed<?> operand) {
    return negative(path(operand));
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

