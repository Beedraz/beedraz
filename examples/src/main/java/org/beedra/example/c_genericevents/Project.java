/*<license>
Copyright 2007 - $Date$ by the authors mentioned below.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.beedra.example.c_genericevents;


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
  public final boolean isNameChangeListener(PropertyChangeListener<NameChangeEvent> listener) {
    return $nameChangeListeners.contains(listener);
  }

  /**
   * @post isNameChangeListener(listener);
   */
  public final void addNameChangeListener(PropertyChangeListener<NameChangeEvent> listener) {
    $nameChangeListeners.add(listener);
  }

  /**
   * @post ! isNameChangeListener(listener);
   */
  public final void removeNameChangeListener(PropertyChangeListener<NameChangeEvent> listener) {
    $nameChangeListeners.remove(listener);
  }

  private void fireNameChangeEvent(String oldValue) {
    NameChangeEvent event = new NameChangeEvent(this, "name", oldValue, $name);
    for (PropertyChangeListener<NameChangeEvent> listener : $nameChangeListeners) {
      listener.propertyChange(event);
      // same event, because is immutable
    }
  }

  private final Set<PropertyChangeListener<NameChangeEvent>> $nameChangeListeners = new HashSet<PropertyChangeListener<NameChangeEvent>>();

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

//  /**
//   * @basic
//   */
//  public final boolean isTasksChangeListener(TasksChangeListener listener) {
//    return $tasksChangeListeners.contains(listener);
//  }
//
//  /**
//   * @post isTasksChangeListener(listener);
//   */
//  public final void addNameChangeListener(TasksChangeListener listener) {
//    $tasksChangeListeners.add(listener);
//  }
//
//  /**
//   * @post ! isTasksChangeListener(listener);
//   */
//  public final void removeNameChangeListener(TasksChangeListener listener) {
//    $tasksChangeListeners.remove(listener);
//  }
//
//  void fireTaskAddedEvent(Task addedTask) {
//    TaskAddedEvent event = new TaskAddedEvent(this, "tasks", addedTask);
//    for (TasksChangeListener listener : $tasksChangeListeners) {
//      listener.taskAdded(event);
//      // same event, because is immutable
//    }
//  }
//
//  void fireTaskRemovedEvent(Task removedTask) {
//    TaskRemovedEvent event = new TaskRemovedEvent(this, "tasks", removedTask);
//    for (TasksChangeListener listener : $tasksChangeListeners) {
//      listener.taskRemoved(event);
//      // same event, because is immutable
//    }
//  }
//
//  private final Set<TasksChangeListener> $tasksChangeListeners = new HashSet<TasksChangeListener>();


  private final Set<Task> $tasks = new HashSet<Task>();

}

