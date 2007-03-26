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

package org.beedra_II.property.collection.set;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.event.Event;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.collection.CollectionBeed;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
import org.beedra_II.property.number.integer.long64.LongBeed;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;
import org.ppeew.smallfries_I.MathUtil;


/**
 * Support for implementations of {@link SetBeed}.
 *
 * @author Nele Smeets
 * @author  Peopleware n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractSetBeed<_Element_, _SetEvent_ extends SetEvent<_Element_>>
    extends AbstractPropertyBeed<_SetEvent_>
    implements SetBeed<_Element_, _SetEvent_> {

  /**
   * @pre ownerBeed != null;
   */
  public AbstractSetBeed(AggregateBeed ownerBeed) {
    super(ownerBeed);
  }

  /**
   * A beed representing the size of this set beed.
   *
   * @invar  get() == EditableSetBeed.this.get().size();
   */
  protected class SizeBeed
      extends AbstractPropertyBeed<ActualLongEvent>
      implements LongBeed {

    /**
     * Creates a new SizeBeed. The owner of this beed is the
     * owner of the {@link SetBeed}.
     */
    SizeBeed() {
      super(AbstractSetBeed.this.getOwner());
      $dependent.addUpdateSource(AbstractSetBeed.this);
    }

    public final boolean isEffective() {
      return true;
    }

    private final Dependent<AbstractSetBeed<_Element_, _SetEvent_>> $dependent =
      new AbstractUpdateSourceDependentDelegate<AbstractSetBeed<_Element_, _SetEvent_>, ActualLongEvent>(this) {

      @Override
      protected ActualLongEvent filteredUpdate(Map<AbstractSetBeed<_Element_, _SetEvent_>, Event> events, Edit<?> edit) {
        // We only get events from our set beed, and that can only be 1
        assert events.size() == 1;
        Iterator<Event> iter = events.values().iterator();
        Event event = iter.next();
        SetEvent<?> setEvent = (SetEvent<?>)event;
        if (setEvent.getAddedElements().size() != setEvent.getRemovedElements().size()) {
          long oldSize = $size;
          $size += setEvent.getAddedElements().size();
          $size -= setEvent.getRemovedElements().size();
          assert AbstractSetBeed.this.get().size() == $size :
                  "size in size beed should be " + AbstractSetBeed.this.get().size() +
                  " but is " + $size;
          return new ActualLongEvent(SizeBeed.this, oldSize, (long)$size, edit);
        }
        else { // the size didn't change
          assert AbstractSetBeed.this.get().size() == $size;
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
       * TODO This only works if we add no update sources during construction,
       *      so a better solution should be sought.
       */
      return $dependent == null ? 0 : $dependent.getMaximumRootUpdateSourceDistance();
    }

    public final int get() {
      return $size;
    }

    private int $size = 0;

    public final long getlong() {
      return $size;
    }

    public BigInteger getBigInteger() {
      return MathUtil.castToBigInteger(get());
    }

    public Double getDouble() {
      return MathUtil.castToDouble(get());
    }

    public final double getdouble() {
      return $size;
    }

    public Long getLong() {
      return MathUtil.castToLong(get());
    }

    public BigDecimal getBigDecimal() {
      return MathUtil.castToBigDecimal(get());
    }

    /**
     * @post  result != null;
     * @post  result.getOldValue() == null;
     * @post  result.getNewValue().equals(getLong());
     * @post  result.getEdit() == null;
     * @post  result.getEditState() == null;
     */
    @Override
    protected ActualLongEvent createInitialEvent() {
      return new ActualLongEvent(this, null, getLong(), null);
    }

    public final Set<? extends UpdateSource> getUpdateSources() {
      return $dependent.getUpdateSourcesSet();
    }

    public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
      /* fixed to make it possible to use this method during construction,
       * before $dependent is initialized. But that is bad code, and should be
       * fixed.
       */
      return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
    }

  }

  private final static Set<? extends UpdateSource> PHI = Collections.emptySet();

  /**
   * See {@link CollectionBeed#getSize()}.
   */
  public final IntegerBeed<ActualLongEvent> getSize() {
    return $sizeBeed;
  }

  /**
   * See {@link CollectionBeed#getCardinality()}.
   */
  public final IntegerBeed<ActualLongEvent> getCardinality() {
    return $sizeBeed;
  }

  protected SizeBeed $sizeBeed =  new SizeBeed();

}

