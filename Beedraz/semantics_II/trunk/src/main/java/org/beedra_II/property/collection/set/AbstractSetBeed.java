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
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;

import org.beedra_II.AbstractBeed;
import org.beedra_II.AbstractDependentBeed;
import org.beedra_II.Event;
import org.beedra_II.edit.Edit;
import org.beedra_II.property.collection.CollectionBeed;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
import org.beedra_II.property.number.integer.long64.LongBeed;
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
    extends AbstractBeed<_SetEvent_>
    implements SetBeed<_Element_, _SetEvent_> {

  /**
   * A beed representing the size of this set beed.
   *
   * @invar  get() == EditableSetBeed.this.get().size();
   */
  protected class SizeBeed
      extends AbstractDependentBeed<ActualLongEvent>
      implements LongBeed {

    /**
     * Creates a new SizeBeed. The outer object is
     * an update source of this SizeBeed: when the set changes, the
     * size is updated accordingly.
     */
    SizeBeed() {
      addUpdateSource(AbstractSetBeed.this);
    }

    public final boolean isEffective() {
      return true;
    }

    @Override
    protected ActualLongEvent filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
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

    public final void toStringDepth(StringBuffer sb, int depth, NumberFormat numberFormat) {
      sb.append(numberFormat.format(getdouble()));
    }

  }

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

