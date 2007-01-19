package org.beedra.example.propertychangesupport;

import java.beans.PropertyChangeEvent;




/**
 * @invar getSource() != null;
 * @invar getPropertyName() != null;
 * @note better invar: is a true property name of source
 * @note must be immutable
 * @note in Swing, the UndoableEdit is wrapped in the UndoableEditEvent, instead of inheritance; should we do this?
 */
public abstract class TasksChangeEvent extends PropertyChangeEvent {

  /*
   * @pre source != null;
   * @post getSource() == sourcel
   * @post getPropertyName().equals(propertyName);
   * @post getTask() == task
   * @post canUndo();
   */
  public TasksChangeEvent(Project mySource, String propertyName, Task task) {
    super(mySource, propertyName, null, null);
    assert source != null;
    assert propertyName != null;
    $task = task;
  }

  /**
   * @basic
   */
  public final Task getTask() {
    return $task;
  }

  private Task $task;

}

