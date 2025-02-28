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

package org.beedraz.semantics_II.expression.number.real.double64;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.StubListener;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.AbstractTestBinaryExprBeed;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.junit.Assert;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractTestRealArgDoubleBinaryExpressionBeed<
                                       _UEB_ extends AbstractRealArgDoubleBinaryExpressionBeed>
    extends AbstractTestBinaryExprBeed<Double,
                                       ActualDoubleEvent,
                                       Double,
                                       RealBeed<?>,
                                       RealBeed<?>,
                                       _UEB_,
                                       EditableDoubleBeed,
                                       EditableDoubleBeed> {

  @Override
  protected void initGoals() {
    $leftGoal1 = -Math.E;
    $leftGoal2 = Double.NEGATIVE_INFINITY;
    $leftGoalMIN = Double.MIN_VALUE; // - MIN_VALUE == MIN_VALUE (2-bit complement)
    $leftGoalMAX = Double.MAX_VALUE;
    $rightGoal1 = Math.PI;
    $rightGoal2 = Double.POSITIVE_INFINITY;
    $rightGoalMIN = Double.MIN_VALUE; // - MIN_VALUE == MIN_VALUE (2-bit complement)
    $rightGoalMAX = Double.MAX_VALUE;
  }

  @Override
  protected void changeLeftOperand(EditableDoubleBeed editableOperandBeed, Double newValue) {
    try {
      DoubleEdit edit = new DoubleEdit(editableOperandBeed);
      edit.setGoal(newValue);
      edit.perform();
    }
    catch (EditStateException exc) {
      Assert.fail();
    }
    catch (IllegalEditException exc) {
      Assert.fail();
    }
  }

  @Override
  protected void changeRightOperand(EditableDoubleBeed editableOperandBeed, Double newValue) {
    changeLeftOperand(editableOperandBeed, newValue);
  }

  @Override
  protected EditableDoubleBeed createEditableLeftOperandBeed(AggregateBeed owner) {
    return new EditableDoubleBeed(owner);
  }

  @Override
  protected EditableDoubleBeed createEditableRightOperandBeed(AggregateBeed owner) {
    return createEditableLeftOperandBeed(owner);
  }

  @Override
  protected StubListener<ActualDoubleEvent> createStubListener() {
    return new StubListener<ActualDoubleEvent>();
  }

  @Override
  protected Double newValueFrom(ActualDoubleEvent event) {
    return event.getNewDouble();
  }

  @Override
  protected Double oldValueFrom(ActualDoubleEvent event) {
    return event.getOldDouble();
  }

  @Override
  protected Double valueFromLeft(RealBeed<?> operandBeed) {
    return operandBeed.getDouble();
  }

  @Override
  protected Double valueFromRight(RealBeed<?> operandBeed) {
    return valueFromLeft(operandBeed);
  }

}
