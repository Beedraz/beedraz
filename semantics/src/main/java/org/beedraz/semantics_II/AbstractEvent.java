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


import static org.beedraz.semantics_II.Edit.State.DONE;
import static org.beedraz.semantics_II.Edit.State.UNDONE;
import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.indent;
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.objectToString;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * @author Jan Dockx
 *
 * @invar getSource() != null;
 * @invar (getEdit() != null) ? getEdit().getState() == DONE || getEdit().getState() == UNDONE;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractEvent implements Event {

  /**
   * @pre  source != null;
   * @pre  (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   */
  protected AbstractEvent(Beed<?> source, Edit<?> edit) {
    assert source != null;
    assert (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE) : true;
    $source = source;
    $edit = edit;
    initEditState();
  }

  /**
   * @result result != null;
   * @result ! result.equals(this);
   * @result result.getSource() == getSource();
   * @result result.getEdit() == getEdit();
   * @result result.getEditState() == getEditState();
   */
  @Override
  protected AbstractEvent clone() {
    try {
      AbstractEvent result = (AbstractEvent)super.clone();
      return result;
    }
    catch (CloneNotSupportedException exc) {
      assert false : "CloneNotSupportedException should not happen: " + exc;
      return null; // make compiler happy
    }
  }

  /**
   * A clone, but with another edit.
   *
   * @result result != null;
   * @result ! result.equals(this);
   * @result result.getSource() == getSource();
   * @result result.getEdit() == edit;
   * @result result.getEditState() == edit.getState();
   */
  protected AbstractEvent clone(Edit<?> edit) {
    AbstractEvent result = clone();
    result.$edit = edit;
    result.initEditState();
    return result;
  }

  public final Beed<?> getSource() {
    return $source;
  }

  /**
   * @invar $source != null;
   */
  private final Beed<?> $source;

  public final Edit<?> getEdit() {
    return $edit;
  }

  /**
   * Not final for use in {@link #clone(Edit)}.
   */
  private Edit<?> $edit;

  public Edit.State getEditState() {
    return $editState;
  }

  private void initEditState() {
    $editState = ($edit != null) ? $edit.getState() : null;
  }

  /**
   * Not final for use in {@link #clone(Edit)}.
   */
  private Edit.State $editState;

  public final Event combineWith(Event other, CompoundEdit<?, ?> compoundEdit) throws CannotCombineEventsException {
    assert other != null;
    assert compoundEdit != null;
    assert getEdit() != null;
    assert other.getEdit() != null;
    assert compoundEdit.deepContains(this.getEdit());
    assert compoundEdit.deepContains(other.getEdit());
    if (getClass() != other.getClass()) {
      throw new CannotCombineEventsException(this, other, CannotCombineEventsException.Reason.DIFFERENT_TYPE);
    }
    if (getSource() != other.getSource()) {
      throw new CannotCombineEventsException(this, other, CannotCombineEventsException.Reason.DIFFERENT_SOURCE);
    }
    return createCombinedEvent(other, compoundEdit);
  }

  /**
   * @pre other != null;
   * @pre compoundEdit != null;
   * @pre getEdit() != null;
   * @pre other.getEdit() != null;
   * @pre compoundEdit.deepContains(this.getEdit());
   * @pre compoundEdit.deepContains(other.getEdit());
   * @pre getClass() == other.getClass();
   * @pre getSource() == other.getSource();
   * @post result.getClass() == getClass();
   * @post result.getSource() == getSource();
   * @post result.getEdit() == compoundEdit;
   * @post result.getEditState() == compoundEdit.getState();
   * @post ; result initial state is this initial state
   * @post ; result goal state is {@code other} initial state
   * @throws CannotCombineEventsException
   *         exception.getReason() == CannotCombineEventsException.Reason.INCOMPATIBLE_STATES;
   *         this goal state is different from other initial state
   */
  protected abstract Event createCombinedEvent(Event other, CompoundEdit<?, ?> compoundEdit)
      throws CannotCombineEventsException;

  @Override
  public final String toString() {
    return getClass().getSimpleName() + //"@" + hashCode() +
           "[" + otherToStringInformation() + "]";
  }

  protected String otherToStringInformation() {
    return "source: " + getSource() +
           ", edit: " + getEdit() +
           ", edit state: " + getEditState();
  }

  public void toString(StringBuffer sb, int level) {
    assert sb != null;
    assert level >= 0;
    objectToString(this, sb, level);
    sb.append(indent(level + 1) + "source:\n");
    getSource().toString(sb, level + 2);
    sb.append(indent(level + 1) + "edit:\n");
    getEdit().toString(sb, level + 2);
    sb.append(indent(level + 1) + "edit state: " + getEditState() + "\n");
  }

}

