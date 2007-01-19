package org.beedra.example.bean;




/**
 * @invar getSource() != null;
 * @invar getPropertyName() != null;
 * @note better invar: is a true property name of source
 * @note must be immutable
 * @note in Swing, the UndoableEdit is wrapped in the UndoableEditEvent, instead of inheritance; should we do this?
 */
public abstract class TasksChangeEvent {

  /*
   * @pre source != null;
   * @post getSource() == sourcel
   * @post getPropertyName().equals(propertyName);
   * @post getTask() == task
   * @post canUndo();
   */
  public TasksChangeEvent(Project source, String propertyName, Task task) {
    assert source != null;
    assert propertyName != null;
    $source = source;
    $propertyName = propertyName;
    $task = task;
  }

  /**
   * @basic
   */
  public final Project getSource() {
    return $source;
  }

  /**
   * @invar $source != null;
   */
  private Project $source;

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
  public final Task getTask() {
    return $task;
  }

  private Task $task;

}

