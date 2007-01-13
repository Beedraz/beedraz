package org.beedra.example.bean;


import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;


/**
 * @invar getSource() != null;
 * @invar getPropertyName() != null;
 * @note better invar: is a true property name of source
 * @note must be immutable
 * @note in Swing, the UndoableEdit is wrapped in the UndoableEditEvent, instead of inheritance; should we do this?
 */
public class ProjectChangeEvent implements UndoableEdit {

  /*
   * @pre source != null;
   * @post getSource() == sourcel
   * @post getPropertyName().equals(propertyName);
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post canUndo();
   */
  public ProjectChangeEvent(Task source, String propertyName, Project oldValue, Project newValue) {
    assert source != null;
    assert propertyName != null;
    $source = source;
    $propertyName = propertyName;
    $oldValue = oldValue;
    $newValue = newValue;
  }

  /**
   * @basic
   */
  public final Task getSource() {
    return $source;
  }

  /**
   * @invar $source != null;
   */
  private Task $source;

  /**
   * @basic
   */
  public final String getPropertyName() {
    return $propertyName;
  }

  /**
   * @invar $propertyName != name;
   */
  private String $propertyName;

  /**
   * @basic
   */
  public final Project getOldValue() {
    return $oldValue;
  }

  private Project $oldValue;

  /**
   * @basic
   */
  public final Project getNewValue() {
    return $newValue;
  }

  private Project $newValue;

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
    $source.setProject(getOldValue()); // sends event; ok? I think not????
    // try catch for validation; should not happen
    $done = false;
  }

  public final void redo() throws CannotRedoException {
    if (! canRedo()) {
      throw new CannotRedoException();
    }
    $source.setProject(getNewValue()); // sends event; ok? I think not????
    // try catch for validation; should not happen
    $done = true;
  }

  public final void die() {
    $alive = false;
  }

  private boolean $alive = true;

  public final String getPresentationName() {
    return "Setting name of project from \"" + getOldValue() +
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

