package org.beedra.example.propertychangesupport;


public class TaskRemovedEvent extends TasksChangeEvent {

  /*
   * @pre source != null;
   * @post getSource() == sourcel
   * @post getPropertyName().equals(propertyName);
   * @post getTask() == task
   * @post canUndo();
   */
  public TaskRemovedEvent(Project mySource, String propertyName, Task task) {
    super(mySource, propertyName, task);
  }

}

