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

package org.beedraz.semantics_II.path;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.expression.association.set.ordered.EditableOrderedBidirToOneBeed;
import org.beedraz.semantics_II.expression.association.set.ordered.OrderedBidirToOneEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>{@link Path} that selects a {@link BeanBeed} from an
 *   {@link OrderedBidirToOneBeed}. The {@link #getToOneBeed() to-one beed} is the result of
 *   a {@link #getToOnePath() property beed path}. When the result of the
 *   {@link #getToOnePath() ordered to-one beed path} changes (the path sends events),
 *   <em>or the result changes itself</em> (the {@link EditableOrderedBidirToOneBeed}
 *   sends events), the result of this {@code OrderedToOneBeanPath} might change too.
 *   When this happens, this will update dependents and send events to listeners.</p>
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class OrderedToOneBeanPath<_One_ extends BeanBeed>
    extends AbstractDependentPath<_One_> {


  /*<construction>*/
  //-----------------------------------------------------------------

  /**
   * @pre toOneBeedPath != null;
   * @post getBeanBeedPath() == toOneBeedPath;
   * @post getBeanBeed() == beanBeedPath.get();
   */
  public OrderedToOneBeanPath(Path<? extends EditableOrderedBidirToOneBeed<_One_, ?>> toOneBeedPath) {
    assert toOneBeedPath != null;
    $toOnePath = toOneBeedPath;
    if ($toOnePath instanceof AbstractDependentPath) {
      addUpdateSource($toOnePath);
    }
    setToOneBeed(toOneBeedPath.get());
  }

//      When does this go away?
  public final void terminate() {
    assert $toOnePath != null;
    if ($toOnePath instanceof AbstractDependentPath) {
      removeUpdateSource($toOnePath);
    }
    if ($toOneBeed != null) {
      removeUpdateSource($toOneBeed);
    }
  }

  /*</construction>*/



  /*<section name="dependent">*/
  //-----------------------------------------------------------------

  @Override
  protected PathEvent<_One_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    assert events != null;
    assert events.size() >= 1;
    _One_ oldOne = $one;
    // $toOneBeed could be null
    OrderedBidirToOneEvent<_One_, ?> toOneEvent = (OrderedBidirToOneEvent<_One_, ?>)events.get($toOneBeed);
    if (toOneEvent != null) {
      assert $one == toOneEvent.getOldOne();
      $one = toOneEvent.getNewOne();
    }
    PathEvent<EditableOrderedBidirToOneBeed<_One_, ?>> pathEvent =
        (PathEvent<EditableOrderedBidirToOneBeed<_One_, ?>>)events.get($toOnePath);
    if (pathEvent != null) {
      assert $toOneBeed == pathEvent.getOldValue(); // could be null
      setToOneBeed(pathEvent.getNewValue());
    }
    if (oldOne != $one) {
      return new PathEvent<_One_>(OrderedToOneBeanPath.this, oldOne, $one, edit);
    }
    else {
      return null;
    }
  }

  /*</section>*/



  /*<property name="to-one beed path">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends EditableOrderedBidirToOneBeed<_One_, ?>> getToOnePath() {
    return $toOnePath;
  }

  private final Path<? extends EditableOrderedBidirToOneBeed<_One_, ?>> $toOnePath;

  /*</property>*/



  /*<property name="to-one beed">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final EditableOrderedBidirToOneBeed<_One_, ?> getToOneBeed() {
    return $toOneBeed;
  }

  private void setToOneBeed(EditableOrderedBidirToOneBeed<_One_, ?> toOneBeed) {
    if ($toOneBeed != null) {
      removeUpdateSource($toOneBeed);
    }
    $toOneBeed = toOneBeed;
    if ($toOneBeed != null) {
      $one = $toOneBeed.getOne();
      addUpdateSource($toOneBeed);
    }
    else {
      $one = null;
    }
  }

  private EditableOrderedBidirToOneBeed<_One_, ?> $toOneBeed;

  /*</property>*/



  /*<property name="selected">*/
  //-----------------------------------------------------------------

  public final _One_ get() {
    return $one;
  }

  private _One_ $one;

  /*</property>*/

}

