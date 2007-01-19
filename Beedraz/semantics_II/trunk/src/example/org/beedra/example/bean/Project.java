package org.beedra.example.bean;

import java.util.HashSet;
import java.util.Set;


/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
public class Project {

  /**
   * @basic
   */
  public final String getName() {
    return $name;
  }

  /**
   * @post name != null ? getName().equals(name) : getName() == null;
   * @post ; all registred name change listeners are warned
   */
  public final void setName(String name) {
    if ((name != $name) && (name != null) && name.equals($name)) {
      String oldName = $name;
      $name = name;
      fireNameChangeEvent(oldName);
    }
  }

  private String $name;

  /**
   * @basic
   */
  public final boolean isNameChangeListener(NameChangeListener listener) {
    return $nameChangeListeners.contains(listener);
  }

  /**
   * @post isNameChangeListener(listener);
   */
  public final void addNameChangeListener(NameChangeListener listener) {
    $nameChangeListeners.add(listener);
  }

  /**
   * @post ! isNameChangeListener(listener);
   */
  public final void removeNameChangeListener(NameChangeListener listener) {
    $nameChangeListeners.remove(listener);
  }

  private void fireNameChangeEvent(String oldValue) {
    NameChangeEvent event = new NameChangeEvent(this, "name", oldValue, $name);
    for (NameChangeListener listener : $nameChangeListeners) {
      listener.propertyChange(event);
      // same event, because is immutable
    }
  }

  private final Set<NameChangeListener> $nameChangeListeners = new HashSet<NameChangeListener>();

  /**
   * @pre task != null;
   */
  final void addTask(Task task) {
    assert task != null;
    $tasks.add(task);
    // events
  }

  final void removeTask(Task task) {
    $tasks.remove(task);
    // events
  }

  /**
   * @basic
   */
  public final boolean isTasksChangeListener(TasksChangeListener listener) {
    return $tasksChangeListeners.contains(listener);
  }

  /**
   * @post isTasksChangeListener(listener);
   */
  public final void addNameChangeListener(TasksChangeListener listener) {
    $tasksChangeListeners.add(listener);
  }

  /**
   * @post ! isTasksChangeListener(listener);
   */
  public final void removeNameChangeListener(TasksChangeListener listener) {
    $tasksChangeListeners.remove(listener);
  }

  void fireTaskAddedEvent(Task addedTask) {
    TaskAddedEvent event = new TaskAddedEvent(this, "tasks", addedTask);
    for (TasksChangeListener listener : $tasksChangeListeners) {
      listener.taskAdded(event);
      // same event, because is immutable
    }
  }

  void fireTaskRemovedEvent(Task removedTask) {
    TaskRemovedEvent event = new TaskRemovedEvent(this, "tasks", removedTask);
    for (TasksChangeListener listener : $tasksChangeListeners) {
      listener.taskRemoved(event);
      // same event, because is immutable
    }
  }

  private final Set<TasksChangeListener> $tasksChangeListeners = new HashSet<TasksChangeListener>();


  private final Set<Task> $tasks = new HashSet<Task>();

}

