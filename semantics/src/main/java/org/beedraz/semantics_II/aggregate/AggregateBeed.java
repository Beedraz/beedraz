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

package org.beedraz.semantics_II.aggregate;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.Beed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Aggregate beeds are considered changed when one of their
 *   aggregate elements is changed.
 *   I.e., they listen to changes in their aggregate elements,
 *   and send {@link AggregateEvent derived events} themselves:
 *   changes are propagated.</p>
 * <p>Most aggregate beeds are bean beeds. Final propagation
 *   sources are <dfn>editable beeds</dfn>.</p>
 *
 * @author Jan Dockx
 *
 * @mudo to be added: validation, civilization, propagation
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface AggregateBeed extends Beed<AggregateEvent> {

  /**
   * @basic
   */
  boolean isAggregateElement(Beed<?> beed);

  /**
   * @pre beed != null;
   * @post isAggregateBeed(beed);
   *
   * @idea having this public is not such a good idea; make this a class?
   * @mudo change type of argument to expression beed; but this introduces
   *       a circular dependency!
   */
  void registerAggregateElement(Beed<?> beed);

  /**
   * @pre isAggregateBeed(beed);
   * @post ! isAggregateBeed(beed);
   *
   * @idea having this public is not such a good idea; make this a class?
   */
  void deregisterAggregateElement(Beed<?> b);

}

