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

package org.beedra_II.property;


import org.beedra_II.Event;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.bool.BooleanBeed;
import org.beedra_II.property.bool.BooleanEvent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of binary expression beeds, that represent a value derived
 * from two arguments of type {@link BooleanBeed}.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBooleanArgBinaryExprBeed<
                                            _Result_ extends Object,
                                            _ResultEvent_ extends Event>
    extends AbstractBinaryExprBeed<_Result_,
                                   _ResultEvent_,
                                   BooleanBeed,
                                   BooleanEvent,
                                   BooleanBeed,
                                   BooleanEvent>  {

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   * @post  getArgument() == null;
   * @post  get() == null;
   */
  public AbstractBooleanArgBinaryExprBeed(AggregateBeed owner) {
    super(owner);
  }

  @Override
  protected boolean hasEffectiveLeftArgument() {
    return getLeftArg().isEffective();
  }

  @Override
  protected boolean hasEffectiveRightArgument() {
    return getRightArg().isEffective();
  }

}

