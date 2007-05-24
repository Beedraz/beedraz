/*<license>
Copyright 2007 - $Date: 2007-05-16 16:21:58 +0200 (Wed, 16 May 2007) $ by the authors mentioned below.

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

package org.beedraz.semantics_II.expression;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Superclass with support for aggregate expression beeds.
 * Aggregate expression beeds are expression beeds that are build
 * by composition of other expression beeds.
 */
@Copyright("2007 - $Date: 2007-05-16 16:21:58 +0200 (Wed, 16 May 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 902 $",
         date     = "$Date: 2007-05-16 16:21:58 +0200 (Wed, 16 May 2007) $")
public abstract class AbstractAggregateExpressionBeed
    extends AbstractAggregateBeed
    implements ExpressionBeed<AggregateEvent> {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractAggregateExpressionBeed(AggregateBeed owner) {
    super();
    if (owner != null) {
      owner.registerAggregateElement(this);
    }
  }

}

