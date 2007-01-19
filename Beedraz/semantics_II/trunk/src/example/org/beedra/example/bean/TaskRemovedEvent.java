package org.beedra.example.bean;


public class TaskRemovedEvent extends TasksChangeEvent {

  /*
   * @pre source != null;
   * @post getSource() == sourcel
   * @post getPropertyName().equals(propertyName);
   * @post getTask() == task
   * @post canUndo();
   */
  public TaskRemovedEvent(Project source, String propertyName, Task task) {
    super(source, propertyName, task);
  }

}

