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

package org.beedraz.semantics_II;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.util.Map;

import org.beedraz.semantics_II.expression.StubEditableSimpleExpressionBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class StubSimpleEdit extends AbstractSimpleEdit<StubEditableSimpleExpressionBeed, StubEvent> {

  public StubSimpleEdit(StubEditableSimpleExpressionBeed target) {
    super(target);
  }

  @Override
  protected Map<AbstractBeed<?>, StubEvent> createEvents() {
    $createdEvent = new StubEvent(getTarget());
    return singletonEventMap($createdEvent);
  }

  public StubEvent getCreatedEvent() {
    return $createdEvent;
  }

  private StubEvent $createdEvent;

  @Override
  public boolean isChange() {
    return $b;
  }

  public void setChange(boolean b) {
    $b = b;
  }

  private boolean $b = true;

  @Override
  protected boolean isGoalStateCurrent() {
    return $goalStateCurrent;
  }

  public void setGoalStateCurrent(boolean b) {
    $goalStateCurrent = b;
  }

  private boolean $goalStateCurrent = true;

  @Override
  protected boolean isInitialStateCurrent() {
    return $initialStateCurrent;
  }

  public void setInitialStateCurrent(boolean b) {
    $initialStateCurrent = b;
  }

  private boolean $initialStateCurrent = true;

  @Override
  protected void performance() {
    // NOP
  }

  @Override
  protected void storeInitialState() {
    $initialStateStored = true;
  }

  public boolean isInitialStateStored() {
    return $initialStateStored;
  }

  private boolean $initialStateStored = false;

  @Override
  protected void unperformance() {
    // NOP
  }

  @Override
  protected boolean isAcceptable() {
    return $acceptable;
  }

  public void setAcceptable(boolean b) {
    $acceptable = b;
    recalculateValidity();
  }

  private boolean $acceptable = true;

  public void publicTopologicalUpdateStart() {
    TopologicalUpdateAccess.topologicalUpdate(getTarget(), getCreatedEvent());
  }

}
