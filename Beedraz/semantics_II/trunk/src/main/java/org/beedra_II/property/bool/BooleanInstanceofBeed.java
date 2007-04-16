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


import org.beedra_II.bean.BeanBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Beed that expresses whether a given {@link BeanBeed bean beed} is an instance
 * of a given type.
 *
 * @invar  get() == getBeed() instanceof _Class_;
 * @invar  getBeedClass() != null;
 * @invar  {@link BeanBeed}.class.isAssignableFrom(getBeedClass());
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BooleanInstanceofBeed<_BeanBeed_ extends BeanBeed>
    extends AbstractBeanArgBooleanUnaryExpressionBeed<_BeanBeed_> {

  /**
   * @pre   beedClass != null;
   * @pre   BeanBeed.class.isAssignableFrom(beedClass);
   * @post  getBeed() == null;
   * @post  get() == false;
   */
  public BooleanInstanceofBeed(Class<?> beedClass) {
    assert BeanBeed.class.isAssignableFrom(beedClass);
    $beedClass = beedClass;
  }


  /*<property name="beedClass">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  protected final Class<?> getBeedClass() {
    return $beedClass;
  }

  private Class<?> $beedClass;

  /*</property>*/


  @Override
  protected void recalculate() {
    setValue(getBeedClass().isInstance(getBeed()));
  }

}

