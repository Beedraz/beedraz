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

package org.beedraz.semantics_II.expression;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Collections;
import java.util.Set;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.StubEvent;
import org.beedraz.semantics_II.TopologicalUpdateAccess;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class StubEditableExpressionBeed extends AbstractEditableExpressionBeed<StubEvent> {

  public StubEditableExpressionBeed(AggregateBeed owner) {
    super(owner);
  }

  /**
   * Makes the updateDependents method public for testing reasons.
   */
  public void publicTopologicalUpdateStart(StubEvent event) {
    TopologicalUpdateAccess.topologicalUpdate(this, event);
  }

  public int getMaximumRootUpdateSourceDistance() {
    return 0;
  }

  public Set<? extends Beed<?>> getUpdateSources() {
    return Collections.emptySet();
  }

  public Set<? extends Beed<?>> getUpdateSourcesTransitiveClosure() {
    return Collections.emptySet();
  }

}

