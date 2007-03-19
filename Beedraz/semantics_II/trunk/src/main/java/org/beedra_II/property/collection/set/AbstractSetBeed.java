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

import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.edit.Edit;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.collection.CollectionBeed;
import org.beedra_II.property.number.integer.IntegerBeed;
import org.beedra_II.property.number.integer.long64.ActualLongEvent;
import org.beedra_II.property.number.integer.long64.LongBeed;
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
    }

    public final int get() {
      return $size;
    }

    public final void setSize(int size) {
      $size = size;
    }

    private int $size = 0;

    public BigInteger getBigInteger() {
      return MathUtil.castToBigInteger(get());
    }

    public Double getDouble() {
      return MathUtil.castToDouble(get());
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

    /**
     * Fire an event with the following properties:
     * @result  event != null;
     * @result  result.getOldValue() != null && result.getOldValue().intValue == oldSize;
     * @result  result.getNewValue().equals(getLong());
     * @result  result.getEdit() == edit;
     * @result  result.getEditState() == edit != null ? edit.getState() : null;
     */
    public void fireEvent(int oldSize, Edit<?> edit) {
      Long oldS = Long.valueOf(oldSize);
      fireChangeEvent(new ActualLongEvent(this, oldS, getLong(), edit));
    }

  }

  /**
   * See {@link CollectionBeed#getSize()}.
   */
  public final IntegerBeed<?> getSize() {
    return $sizeBeed;
  }

  /**
   * See {@link CollectionBeed#getCardinality()}.
   */
  public final IntegerBeed<?> getCardinality() {
    return $sizeBeed;
  }

  protected SizeBeed $sizeBeed =  new SizeBeed();

}

