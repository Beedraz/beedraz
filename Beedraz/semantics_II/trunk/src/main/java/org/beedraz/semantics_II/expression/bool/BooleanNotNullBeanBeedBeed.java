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

package org.beedraz.semantics_II.expression.bool;


import org.beedraz.semantics_II.bean.BeanBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Beed that expresses whether a given {@link BeanBeed bean beed} is
 * effective or not.
 *
 * @invar  get() == getBeed() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanNotNullBeanBeedBeed<_BeanBeed_ extends BeanBeed>
    extends AbstractBeanArgBooleanUnaryExpressionBeed<_BeanBeed_> {

  @Override
  protected void recalculate() {
    setValue(getBeed() != null);
  }

}

