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

package org.beedra_II.property.bool;


import org.beedra_II.bean.RunBeanBeed;


public class TestBooleanNotNullBeanBeedBeed
    extends AbstractTestBeanArgBooleanUnaryExpressionBeed<
                BooleanNotNullBeanBeedBeed<?>> {

  @Override
  protected RunBeanBeed initBeed() {
    return new RunBeanBeed();
  }

  @Override
  protected RunBeanBeed initRun1() {
    return new RunBeanBeed();
  }

  @Override
  protected RunBeanBeed initRun2() {
    return new RunBeanBeed();
  }

  @Override
  protected BooleanNotNullBeanBeedBeed<?> initSubject() {
    return new BooleanNotNullBeanBeedBeed<RunBeanBeed>();
  }

  @Override
  protected boolean valueFrom(RunBeanBeed beed) {
    return beed != null;
  }

}
