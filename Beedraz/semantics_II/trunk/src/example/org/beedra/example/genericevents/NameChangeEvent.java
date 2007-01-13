package org.beedra.example.genericevents;


/**
 * @invar getSource() != null;
 * @invar getPropertyName() != null;
 * @note better invar: is a true property name of source
 * @note must be immutable
 * @note in Swing, the UndoableEdit is wrapped in the UndoableEditEvent, instead of inheritance; should we do this?
 */
public class NameChangeEvent extends PropertyChangeEvent<Project, String> {

  /**
   * @pre source != null;
   * @post getSource() == sourcel
   * @post getPropertyName().equals(propertyName);
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post canUndo();
   */
  public NameChangeEvent(Project source, String propertyName, String oldValue, String newValue) {
    super(source, propertyName, oldValue, newValue);
  }

  @Override
  protected void setValue(Project source, String value) {
    source.setName(value);
  }

}

