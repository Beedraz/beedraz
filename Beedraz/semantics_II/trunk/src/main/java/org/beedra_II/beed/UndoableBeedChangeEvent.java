package org.beedra_II.beed;


import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.beedra_II.BeedraBean;



/**
 * @invar getSource() != null;
 * @note must be immutable
 * @note in Swing, the UndoableEdit is wrapped in the UndoableEditEvent, instead of inheritance; should we do this?
 */
public class UndoableBeedChangeEvent<_Value_>
    extends BeedChangeEvent<_Value_>
    implements UndoableEdit {

  /*
   * @pre source != null;
   * @post getSource() == sourcel
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post canUndo();
   */
  public UndoableBeedChangeEvent(MutableBeed<? extends BeedraBean, _Value_> source, _Value_ oldValue, _Value_ newValue) {
    super(source, oldValue, newValue);
  }

  /**
   * @basic
   */
   public final MutableBeed<? extends BeedraBean, _Value_> getSource() {
     return (MutableBeed<? extends BeedraBean, _Value_>)super.getSource();
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
    getSource().set(getOldValue()); // sends event; ok? I think not????
    // try catch for validation; should not happen
    $done = false;
  }

  public final void redo() throws CannotRedoException {
    if (! canRedo()) {
      throw new CannotRedoException();
    }
    getSource().set(getNewValue()); // sends event; ok? I think not????
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

