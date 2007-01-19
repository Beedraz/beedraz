package org.beedra.example.bean;




public interface TasksChangeListener {

  /**
   * @pre event != null;
   */
  void taskRemoved(TaskRemovedEvent event);

  /**
   * @pre event != null;
   */
  void taskAdded(TaskAddedEvent event);

}

