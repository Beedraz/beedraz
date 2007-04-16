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

package org.beedra_II.path;


import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.beedra_II.Beed;
import org.beedra_II.Event;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>{@link Path} that selects a {@link PropertyBeed} from a
 *   {@link BeanBeed}. The {@link #getBeanBeed() bean beed} is the result of
 *   a {@link #getBeanPath() bean beed path}. When the result of the
 *   {@link #getBeanPath() bean beed path} changes, the result of this
 *   {@code BeanPropertyPath} might change too. When this happens, this
 *   will update dependents and send events to listeners.</p>
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
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class BeanPropertyPath<_AggregateBeed_ extends AggregateBeed, _PropertyBeed_ extends Beed<?>>
    extends AbstractPath<_PropertyBeed_> {

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
    $dependent.addUpdateSource(beanBeedPath);
  }

  /*</construction>*/



  /*<section name="dependent">*/
  //-----------------------------------------------------------------

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected PathEvent<_PropertyBeed_> filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
        assert events != null;
        assert events.size() == 1;
        PathEvent<_AggregateBeed_> event = (PathEvent<_AggregateBeed_>)events.get($beanPath);
        assert event != null;
        _PropertyBeed_ oldPropertyBeed = $propertyBeed;
        _AggregateBeed_ newBeanBeed = event.getNewValue();
        assert newBeanBeed == $beanPath.get();
        $propertyBeed = newBeanBeed == null ? null : selectPropertyBeedFromBeanBeed(newBeanBeed);
        if (oldPropertyBeed != $propertyBeed) {
          return new PathEvent<_PropertyBeed_>(BeanPropertyPath.this, oldPropertyBeed, $propertyBeed, edit);
        }
        else {
          return null;
        }
      }

    };

  public final int getMaximumRootUpdateSourceDistance() {
    /* FIX FOR CONSTRUCTION PROBLEM
     * At construction, the super constructor is called with the future owner
     * of this property beed. Eventually, in the constructor code of AbstractPropertyBeed,
     * this object is registered as update source with the dependent of the
     * aggregate beed. During that registration process, the dependent
     * checks to see if we need to ++ our maximum root update source distance.
     * This involves a call to this method getMaximumRootUpdateSourceDistance.
     * Since however, we are still doing initialization in AbstractPropertyBeed,
     * initialization code (and construction code) further down is not yet executed.
     * This means that our $dependent is still null, and this results in a NullPointerException.
     * On the other hand, we cannot move the concept of $dependent up, since not all
     * property beeds have a dependent.
     * The fix implemented here is the following:
     * This problem only occurs during construction. During construction, we will
     * not have any update sources, so our maximum root update source distance is
     * effectively 0.
     */
    /*
     * TODO This only works if we only add 1 update source during construction,
     *      so a better solution should be sought.
     */
    return $dependent == null ? 0 : $dependent.getMaximumRootUpdateSourceDistance();
  }

  public final Set<? extends UpdateSource> getUpdateSources() {
    return $dependent.getUpdateSources();
  }

  private final static Set<? extends UpdateSource> PHI = Collections.emptySet();

  public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
    /* fixed to make it possible to use this method during construction,
     * before $dependent is initialized. But that is bad code, and should be
     * fixed.
     */
    return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
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

