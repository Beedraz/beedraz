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

package org.beedra_II.property.simple;


import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * @mudo definition
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class UndoableOldNewBEvent<_Source_ extends SimplePDB<_Type_, UndoableOldNewBEvent<_Source_, _Type_>>,
                                  _Type_>
    extends OldNewBEvent<_Source_, _Type_>
    implements UndoableEdit {

  /**
   * @pre source != null;
   * @post getSource() == sourcel
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   */
  public UndoableOldNewBEvent(_Source_ source, _Type_ oldValue, _Type_ newValue) {
    super(source, oldValue, newValue);
  }

  /**
   * @return false;
   */
  public final boolean addEdit(UndoableEdit anEdit) {
    return false;
  }

  /**
   * @return false;
   */
  public boolean replaceEdit(UndoableEdit anEdit) {
    return false;
  }

  /**
   * @return true;
   */
  public final boolean isSignificant() {
    return true;
  }

  /**
   * @basic
   */
  public final boolean canUndo() {
    return $alive && $done;
  }

  /**
   * @return ! canUndo();
   */
  public final boolean canRedo() {
    return ! $alive && $done;
  }

  private boolean $done = true;

  public final void undo() throws CannotUndoException {
    if (! canUndo()) {
      throw new CannotUndoException();
    }
    getSource().set(getOldValue()); // MUDO sends event; ok? I think not????
    // try catch for validation; should not happen
    $done = false;
  }

  public final void redo() throws CannotRedoException {
    if (! canRedo()) {
      throw new CannotRedoException();
    }
    getSource().set(getNewValue()); // MDUO sends event; ok? I think not????
    // try catch for validation; should not happen
    $done = true;
  }

  public final void die() {
    $alive = false;
  }

  private boolean $alive = true;

  public final String getPresentationName() {
    return "Setting XXXX from \"" + getOldValue() + // MUDO
            "\" to \"" + getNewValue() + "\"";
    // i18n
  }

  public final String getUndoPresentationName() {
    return "Undo " + getPresentationName();
    // i18n
  }

  public final String getRedoPresentationName() {
    return "Redo " + getPresentationName();
    // i18n
  }

}

