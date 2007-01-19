package org.beedra.example.genericevents;


/**
 * @invar getSource() != null;
 * @invar getPropertyName() != null;
 * @note better invar: is a true property name of source
 * @note must be immutable
 * @note in Swing, the UndoableEdit is wrapped in the UndoableEditEvent, instead of inheritance; should we do this?
 */
public class ProjectChangeEvent extends PropertyChangeEvent<Task, Project> {

  /**
   * @pre source != null;
   * @post getSource() == sourcel
   * @post getPropertyName().equals(propertyName);
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post canUndo();
   */
  public ProjectChangeEvent(Task source, String propertyName, Project oldValue, Project newValue) {
    super(source, propertyName, oldValue, newValue);
  }

  @Override
  protected void setValue(Task source, Project value) {
    source.setProject(value);
  }

}

