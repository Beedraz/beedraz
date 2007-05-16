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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>{@link Path} that selects a {@link Beed property beed} from an
 *   {@link AggregateBeed}. The {@link #getBeanBeed() bean beed} is the result of
 *   a {@link #getBeanPath() bean beed path}. When the result of the
 *   {@link #getBeanPath() bean beed path} changes, the result of this
 *   {@code BeanPropertyPath} might change too. When this happens, this
 *   will update dependents and send events to listeners.</p>
 *
 * @mudo PropertyBeed does not exist any longer: change the comments above.
 *
 * @protected
 * <p>Subclasses must implement {@link #selectPropertyBeedFromBeanBeed(BeanBeed)},
 *   to select the appropriate property beed from the bean beed given by the
 *   {link {@link #getBeanPath()} (i.e., {@link #getBeanBeed()}).</p>
 *
 * @author Jan Dockx
 *
 * @invar getBeanBeedPath() != null;
 * @invar getBeanBeed() == getBeanBeedPath().get();
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class BeanPropertyPath<_AggregateBeed_ extends AggregateBeed, _PropertyBeed_ extends Beed<?>>
    extends AbstractDependentPath<_PropertyBeed_> {

  /*<construction>*/
  //-----------------------------------------------------------------

  /**
   * @pre beanBeedPath != null;
   * @post getBeanBeedPath() == beanBeedPath;
   * @post getBeanBeed() == beanBeedPath.get();
   */
  public BeanPropertyPath(Path<? extends _AggregateBeed_> beanBeedPath) {
    assert beanBeedPath != null;
    $beanPath = beanBeedPath;
    _AggregateBeed_ beanBeed = beanBeedPath.get();
    $propertyBeed = beanBeed == null ? null : selectPropertyBeedFromBeanBeed(beanBeed);
    addUpdateSource(beanBeedPath);
  }

  /*</construction>*/



  /*<section name="dependent">*/
  //-----------------------------------------------------------------

  @Override
  protected PathEvent<_PropertyBeed_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    assert events != null;
    assert events.size() == 1;
    PathEvent<_AggregateBeed_> event = (PathEvent<_AggregateBeed_>)events.get($beanPath);
    assert event != null;
    _PropertyBeed_ oldPropertyBeed = $propertyBeed;
    _AggregateBeed_ newBeanBeed = event.getNewValue();
    assert newBeanBeed == $beanPath.get();
    $propertyBeed = newBeanBeed == null ? null : selectPropertyBeedFromBeanBeed(newBeanBeed);
    if (oldPropertyBeed != $propertyBeed) {
      return new PathEvent<_PropertyBeed_>(this, oldPropertyBeed, $propertyBeed, edit);
    }
    else {
      return null;
    }
  }

  /*</section>*/



  /*<property name="bean beed path">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends _AggregateBeed_> getBeanPath() {
    return $beanPath;
  }

  private final Path<? extends _AggregateBeed_> $beanPath;

  /*</property>*/



  /*<property name="bean beed">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final _AggregateBeed_ getBeanBeed() {
    return $beanPath.get();
  }

  /*</property>*/



  /*<property name="selected">*/
  //-----------------------------------------------------------------

  public final _PropertyBeed_ get() {
    return $propertyBeed;
  }

  /**
   * @pre beanBeed != null;
   */
  protected abstract _PropertyBeed_ selectPropertyBeedFromBeanBeed(_AggregateBeed_ beanBeed);

  private _PropertyBeed_ $propertyBeed;

  /*</property>*/

}

