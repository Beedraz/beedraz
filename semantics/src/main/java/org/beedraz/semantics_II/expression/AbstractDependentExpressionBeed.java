/*<license>
Copyright 2007 - $Date: 2007-05-08 16:13:31 +0200 (Tue, 08 May 2007) $ by the authors mentioned below.

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

import org.beedraz.semantics_II.AbstractDependentBeed;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>This class offers a constructor that implements the registration of the {@link ExpressionBeed}
 *   with an {@link AggregateBeed}, as described in the documentation of {@link ExpressionBeed}.</p>
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date: 2007-05-08 16:13:31 +0200 (Tue, 08 May 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 849 $",
         date     = "$Date: 2007-05-08 16:13:31 +0200 (Tue, 08 May 2007) $")
public abstract class AbstractDependentExpressionBeed<_Event_ extends Event>
    extends AbstractDependentBeed<_Event_>
    implements ExpressionBeed<_Event_> {

  /**
   * @post owner != null ? owner.registerAggregateElement(this);
   */
  protected AbstractDependentExpressionBeed(AggregateBeed owner) {
    if (owner != null) {
      owner.registerAggregateElement(this);
    }
  }

}
