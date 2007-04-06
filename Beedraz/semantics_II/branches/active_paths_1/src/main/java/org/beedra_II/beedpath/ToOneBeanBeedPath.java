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

package org.beedra_II.beedpath;


import java.util.Map;

import org.beedra_II.Beed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.beedra_II.property.association.set.BidirToOneEvent;
import org.beedra_II.property.association.set.EditableBidirToOneBeed;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>{@link BeedPath} that selects a {@link BeanBeed} from an
 *   {@link BidirToOneBeed}. The {@link #getToOneBeed() to-one beed} is the result of
 *   a {@link #getToOneBeedPath() property beed path}. When the result of the
 *   {@link #getToOneBeedPath() to-one beed path} changes (the path sends events),
 *   <em>or the result changes itself</em> (the {@link EditableBidirToOneBeed}
 *   sends events), the result of this {@code ToOneBeanBeedPath} might change too.
 *   When this happens, this will update dependents and send events to listeners.</p>
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class ToOneBeanBeedPath<_One_ extends BeanBeed>
    extends AbstractBeedPath<_One_> {


  /*<construction>*/
  //-----------------------------------------------------------------

  /**
   * @pre toOneBeedPath != null;
   * @post getBeanBeedPath() == toOneBeedPath;
   * @post getBeanBeed() == beanBeedPath.get();
   */
  public ToOneBeanBeedPath(BeedPath<EditableBidirToOneBeed<_One_, ?>> toOneBeedPath) {
    assert toOneBeedPath != null;
    $toOneBeedPath = toOneBeedPath;
    $dependent.addUpdateSource($toOneBeedPath);
    setToOneBeed(toOneBeedPath.get());
  }

//      When does this go away?
  public final void terminate() {
    assert $toOneBeedPath != null;
    $dependent.removeUpdateSource($toOneBeedPath);
    if ($toOneBeed != null) {
      $dependent.removeUpdateSource($toOneBeed);
    }
  }

  /*</construction>*/



  /*<section name="dependent">*/
  //-----------------------------------------------------------------

  private final Dependent<Beed<?>> $dependent =
    new AbstractUpdateSourceDependentDelegate<Beed<?>, BeedPathEvent<_One_>>(this) {

      @Override
      protected BeedPathEvent<_One_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
        assert events != null;
        assert events.size() >= 1;
        _One_ oldOne = $one;
        // $toOneBeed could be null
        BidirToOneEvent<_One_, ?> toOneEvent = (BidirToOneEvent<_One_, ?>)events.get($toOneBeed);
        if (toOneEvent != null) {
          assert $one == toOneEvent.getOldOne();
          $one = toOneEvent.getNewOne();
        }
        BeedPathEvent<EditableBidirToOneBeed<_One_, ?>> pathEvent = (BeedPathEvent<EditableBidirToOneBeed<_One_, ?>>)events.get($toOneBeedPath);
        if (pathEvent != null) {
          assert $toOneBeed == pathEvent.getOldBeed(); // could be null
          setToOneBeed(pathEvent.getNewBeed());
        }
        if (oldOne != $one) {
          return new BeedPathEvent<_One_>(ToOneBeanBeedPath.this, oldOne, $one, edit);
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

  /*</section>*/



  /*<property name="to-one beed path">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final BeedPath<EditableBidirToOneBeed<_One_, ?>> getToOneBeedPath() {
    return $toOneBeedPath;
  }

  private final BeedPath<EditableBidirToOneBeed<_One_, ?>> $toOneBeedPath;

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

