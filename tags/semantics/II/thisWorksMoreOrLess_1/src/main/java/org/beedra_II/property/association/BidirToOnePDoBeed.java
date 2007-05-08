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

package org.beedra_II.property.association;


import org.beedra_II.bean.BeanBeed;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.PropertyBeedSelector;
import org.beedra_II.property.simple.SimplePDB;
import org.beedra_II.property.simple.UndoableOldNewBEvent;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * @mudo description
 *
 * @invar getProject() != null ? getProject.getTasks().contains(this);
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BidirToOnePDoBeed<_One_ extends BeanBeed,
                               _Many_ extends BeanBeed>
    extends AbstractPropertyBeed<UndoableOldNewBEvent<BidirToOnePDoBeed<_One_, _Many_>, _One_>>
    implements SimplePDB<_One_, UndoableOldNewBEvent<BidirToOnePDoBeed<_One_, _Many_>, _One_>> {

  /**
   * @pre bean != null;
   * @pre bidirToManyPBeedSelector != null;
   */
  public BidirToOnePDoBeed(_Many_ bean, PropertyBeedSelector<_One_, BidirToManyPBeed<_One_, _Many_>> bidirToManyPBeedSelector) {
    super(bean);
    assert bidirToManyPBeedSelector != null;
    $bidirToManyPBeedSelector = bidirToManyPBeedSelector;
  }

  @SuppressWarnings("unchecked")
  public final _Many_ getOwnerBeed() {
    return (_Many_)super.getOwnerBeed();
  }

  public final PropertyBeedSelector<_One_, BidirToManyPBeed<_One_, _Many_>> getBidirToManyPBeedSelector() {
    return $bidirToManyPBeedSelector;
  }

  private final PropertyBeedSelector<_One_, BidirToManyPBeed<_One_, _Many_>> $bidirToManyPBeedSelector;

  public final _One_ get() {
    return $toManyRef == null ? null : $toManyRef.getOwnerBeed();
  }

  public final BidirToManyPBeed<_One_, _Many_> getToManyReference() {
    return $toManyRef;
  }

  public final void set(_One_ one) {
    if (get() != one) {
      if ($toManyRef != null) {
        $toManyRef.remove(getOwnerBeed());
      }
      BidirToManyPBeed<_One_, _Many_> oldManyRef = $toManyRef;
      _One_ oldValue = get();
      $toManyRef = one == null ? null : getBidirToManyPBeedSelector().getPropertyBeed(one);
      if ($toManyRef != null) {
        $toManyRef.add(getOwnerBeed());
      }
      UndoableOldNewBEvent<BidirToOnePDoBeed<_One_, _Many_>, _One_> event =
        new UndoableOldNewBEvent<BidirToOnePDoBeed<_One_, _Many_>, _One_>(this, oldValue, get());
      fireChangeEvent(event);
      // events other side
      if (oldManyRef != null) {
        oldManyRef.fireRemovedEvent(getOwnerBeed());
      }
      if ($toManyRef != null) {
        $toManyRef.fireAddedEvent(getOwnerBeed());
      }
    }
  }

//  private BidirToManyPBeed<_One_, _Many_> toManyRef(_One_ one,
//                                                           String toManyPropertyName) {
//    if (one == null) {
//      return null;
//    }
//    else {
//      try {
//        BeanUtils.getProperty(one, toManyPropertyName);
//      }
//      catch (IllegalAccessException e) {
//        throw new IllegalArgumentException(); // or something
//      }
//      catch (InvocationTargetException e) {
//        throw new IllegalArgumentException(); // or something
//      }
//      catch (NoSuchMethodException e) {
//        // MUDO we get this with instance variables instead of get/setters; beanutils doesn't work with instance variables
//        throw new IllegalArgumentException(); // or something
//      }
//      return null;
//    }
//  }

  private BidirToManyPBeed<_One_, _Many_> $toManyRef;

}

