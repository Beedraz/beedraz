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

package org.beedraz.semantics_II;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.indent;
import static org.beedraz.semantics_II.CannotCombineEventsException.Reason.INCOMPATIBLE_STATES;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.smallfries_I.ComparisonUtil;


/**
 * {@link Event} that carries a simple old and new value,
 * expressing the changed that occured in {@link #getSource()}.
 *
 * @author Jan Dockx
 *
 * @invar (getOldValue() != null) && (getNewValue() != null) ? ! getOldValue().equals(getNewValue());
 *
 * @mudo rename to AbstractOldNewEvent or OldNewEvent
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class ActualOldNewEvent<_Type_>
    extends AbstractEvent
    implements OldNewEvent<_Type_> {

  /**
   * @pre  source != null;
   * @pre  (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @pre  (oldValue != null) && (newValue != null)
   *          ? ! oldValue.equals(newValue)
   *          : true;
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   */
  public ActualOldNewEvent(Beed<?> source,
                           _Type_ oldValue,
                           _Type_ newValue,
                           Edit<?> edit) {
    super(source, edit);
    assert ((oldValue != null) && (newValue != null) ? ! oldValue.equals(newValue) : true) :
           "oldValue: " + oldValue + "; newValue: " + newValue;
    $oldValue = safeValueCopyNull(oldValue);
    $newValue = safeValueCopyNull(newValue);
  }

  /**
   * A safe copy of {@code value}. The returned object may
   * be {@code value}, but it must be a reference to an object
   * that is either immutable or is a copy of {@code value},
   * so that if it is changed, it does not change {@code value}.
   * Often, a {@link Object#clone()} will do the trick. The default
   * implementation returns {@code value}, but this should be overwritten
   * when {@code _Type_} is a type that is to be used by-value,
   * and is not immutable.
   *
   * @result ComparisonUtil.equalsWithNull(result, value);
   *
   * @protected-return value;
   */
  private _Type_ safeValueCopyNull(_Type_ value) {
    if (value == null) {
      return null;
    }
    else {
      return safeValueCopy(value);
    }
  }

  /**
   * A safe copy of {@code value}. The returned object may
   * be {@code value}, but it must be a reference to an object
   * that is either immutable or is a copy of {@code value},
   * so that if it is changed, it does not change {@code value}.
   * Often, a {@link Object#clone()} will do the trick. The default
   * implementation returns {@code value}, but this should be overwritten
   * when {@code _Type_} is a type that is to be used by-value,
   * and is not immutable.
   *
   * @result ComparisonUtil.equalsWithNull(result, value);
   *
   * @pre value != null;
   * @protected-return value;
   */
  protected _Type_ safeValueCopy(_Type_ original) {
    return original;
  }

  /**
   * @basic
   */
  public final _Type_ getOldValue() {
    return safeValueCopyNull($oldValue);
  }

  private final _Type_ $oldValue;

  /**
   * @basic
   */
  public final _Type_ getNewValue() {
    return safeValueCopyNull($newValue);
  }

  /**
   * Not final for clone-use in {@link #safeCreateCombinedEvent(ActualOldNewEvent, CompoundEdit)}.
   */
  private _Type_ $newValue;

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", old value: " + getOldValue() +
           ", new value: " + getNewValue();
  }

  /**
   * @mudo needs unit test
   */
  @Override
  protected final ActualOldNewEvent<_Type_> createCombinedEvent(Event other, CompoundEdit<?, ?> compoundEdit)
      throws CannotCombineEventsException {
    @SuppressWarnings("unchecked")
    ActualOldNewEvent<_Type_> otherAONE = (ActualOldNewEvent<_Type_>)other;
    if (! ComparisonUtil.equalsWithNull($newValue, otherAONE.getOldValue())) {
      throw new CannotCombineEventsException(this, otherAONE, INCOMPATIBLE_STATES);
    }
    return safeCreateCombinedEvent(otherAONE, compoundEdit);
  }

  /**
   * @pre other != null;
   * @pre edit != null;
   * @pre getEdit() != null;
   * @pre other.getEdit() != null;
   * @pre edit.deepContains(this.getEdit());
   * @pre edit.deepContains(other.getEdit());
   * @pre getClass() == other.getClass();
   * @pre getSource() == other.getSource();
   * @pre isNewStateCompatibleWithOldState(other);
   * @post result.getClass() == getClass();
   * @post result.getSource() == getSource();
   * @post result.getEdit() == edit;
   * @post result.getEditState() == edit.getState();
   * @post ; result initial state is this initial state
   * @post ; result goal state is {@code other} initial state
   */
  protected final ActualOldNewEvent<_Type_> safeCreateCombinedEvent(ActualOldNewEvent<_Type_> other, CompoundEdit<?, ?> compoundEdit) {
    @SuppressWarnings("unchecked")
    ActualOldNewEvent<_Type_> result = (ActualOldNewEvent<_Type_>)clone(compoundEdit);
    /* we don't make a by-value copy of $oldValue and $newValue, because they
     * are safe when they go out, and we will not change the objects we refer to
     * internally.
     */
    result.$newValue = other.$newValue;
    return result;
  }


  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    toStringOldNew(sb, level + 1);
  }

  protected void toStringOldNew(StringBuffer sb, int level) {
    sb.append(indent(level + 1) + "old value: " + getOldValue() + "\n");
    sb.append(indent(level + 1) + "new value: " + getNewValue() + "\n");
  }

}

