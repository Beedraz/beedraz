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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.bean.BeanBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Beed that expresses whether a given {@link BeanBeed bean beed} is an instance
 * of a given type.
 *
 * @invar  get() == getBeed() instanceof _Class_;
 * @invar  getBeedClass() != null;
 * @invar  {@link BeanBeed}.class.isAssignableFrom(getBeedClass());
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
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

