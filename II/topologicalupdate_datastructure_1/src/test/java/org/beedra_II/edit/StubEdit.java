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

package org.beedra_II.edit;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.bean.StubBeanBeed;
import org.beedra_II.event.StubEvent;
import org.beedra_II.property.simple.StubEditableSimplePropertyBeed;


public class StubEdit extends AbstractSimpleEdit<StubEditableSimplePropertyBeed, StubEvent> {

  public StubEdit(StubEditableSimplePropertyBeed target) {
    super(target);
  }

  public final static BeanBeed EVENT_SOURCE = new StubBeanBeed();

  @Override
  protected StubEvent createEvent() {
    $createdEvent = new StubEvent(EVENT_SOURCE);
    return $createdEvent;
  }

  public StubEvent getCreatedEvent() {
    return $createdEvent;
  }

  private StubEvent $createdEvent;


  @Override
  protected void updateDependents(StubEvent event) {
    $firedEvent = event;
  }

//  @Override
//  protected void updateDependents(StubEvent event) {
//    $firedEvent = event;
//  }

  public StubEvent $firedEvent;

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

}
