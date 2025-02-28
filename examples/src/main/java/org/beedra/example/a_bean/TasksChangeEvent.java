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

package org.beedra.example.a_bean;


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

