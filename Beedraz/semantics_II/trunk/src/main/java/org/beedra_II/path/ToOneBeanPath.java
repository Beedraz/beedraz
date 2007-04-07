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

import org.beedra_II.Event;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.property.association.set.BidirToOneEvent;
import org.beedra_II.property.association.set.EditableBidirToOneBeed;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>{@link Path} that selects a {@link BeanBeed} from an
 *   {@link BidirToOneBeed}. The {@link #getToOneBeed() to-one beed} is the result of
 *   a {@link #getToOnePath() property beed path}. When the result of the
 *   {@link #getToOnePath() to-one beed path} changes (the path sends events),
 *   <em>or the result changes itself</em> (the {@link EditableBidirToOneBeed}
 *   sends events), the result of this {@code ToOneBeanPath} might change too.
 *   When this happens, this will update dependents and send events to listeners.</p>
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class ToOneBeanPath<_One_ extends BeanBeed>
    extends AbstractPath<_One_> {


  /*<construction>*/
  //-----------------------------------------------------------------

  /**
   * @pre toOneBeedPath != null;
   * @post getBeanBeedPath() == toOneBeedPath;
   * @post getBeanBeed() == beanBeedPath.get();
   */
  public ToOneBeanPath(Path<EditableBidirToOneBeed<_One_, ?>> toOneBeedPath) {
    assert toOneBeedPath != null;
    $toOnePath = toOneBeedPath;
    $dependent.addUpdateSource($toOnePath);
    setToOneBeed(toOneBeedPath.get());
  }

//      When does this go away?
  public final void terminate() {
    assert $toOnePath != null;
    $dependent.removeUpdateSource($toOnePath);
    if ($toOneBeed != null) {
      $dependent.removeUpdateSource($toOneBeed);
    }
  }

  /*</construction>*/



  /*<section name="dependent">*/
  //-----------------------------------------------------------------

  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

      @Override
      protected PathEvent<_One_> filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
        assert events != null;
        assert events.size() >= 1;
        _One_ oldOne = $one;
        // $toOneBeed could be null
        BidirToOneEvent<_One_, ?> toOneEvent = (BidirToOneEvent<_One_, ?>)events.get($toOneBeed);
        if (toOneEvent != null) {
          assert $one == toOneEvent.getOldOne();
          $one = toOneEvent.getNewOne();
        }
        PathEvent<EditableBidirToOneBeed<_One_, ?>> pathEvent = (PathEvent<EditableBidirToOneBeed<_One_, ?>>)events.get($toOnePath);
        if (pathEvent != null) {
          assert $toOneBeed == pathEvent.getOldValue(); // could be null
          setToOneBeed(pathEvent.getNewValue());
        }
        if (oldOne != $one) {
          return new PathEvent<_One_>(ToOneBeanPath.this, oldOne, $one, edit);
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



  /*<property name="to-one beed path">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<EditableBidirToOneBeed<_One_, ?>> getToOnePath() {
    return $toOnePath;
  }

  private final Path<EditableBidirToOneBeed<_One_, ?>> $toOnePath;

  /*</property>*/



  /*<property name="to-one beed">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final EditableBidirToOneBeed<_One_, ?> getToOneBeed() {
    return $toOneBeed;
  }

  private void setToOneBeed(EditableBidirToOneBeed<_One_, ?> toOneBeed) {
    if ($toOneBeed != null) {
      $dependent.removeUpdateSource($toOneBeed);
    }
    $toOneBeed = toOneBeed;
    if ($toOneBeed != null) {
      $one = $toOneBeed.getOne();
      $dependent.addUpdateSource($toOneBeed);
    }
    else {
      $one = null;
    }
  }

  private EditableBidirToOneBeed<_One_, ?> $toOneBeed;

  /*</property>*/



  /*<property name="selected">*/
  //-----------------------------------------------------------------

  public final _One_ get() {
    return $one;
  }

  private _One_ $one;

  /*</property>*/

}

