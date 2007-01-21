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
 *
 * @invar getProject() != null ? getProject.getTasks().contains(this);
 */
public class Task {

  public final Project getProject() {
    return $project;
  }

  public final void setProject(Project project) {
    if (project != $project) {
      if ($project != null) {
        $project.removeTask(this);
      }
      Project oldValue = $project;
      $project = project;
      if ($project != null) {
        $project.addTask(this);
      }
      fireProjectChangeEvent(oldValue);
    }
  }

  private Project $project;

  /**
   * @basic
   */
  public final boolean isProjectChangeListener(PropertyChangeListener<ProjectChangeEvent> listener) {
    return $projectChangeListeners.contains(listener);
  }

  /**
   * @post isProjectChangeListener(listener);
   */
  public final void addProjectChangeListener(PropertyChangeListener<ProjectChangeEvent> listener) {
    $projectChangeListeners.add(listener);
  }

  /**
   * @post ! isProjectChangeListener(listener);
   */
  public final void removeProjectChangeListener(PropertyChangeListener<ProjectChangeEvent> listener) {
    $projectChangeListeners.remove(listener);
  }

  private void fireProjectChangeEvent(Project oldValue) {
    ProjectChangeEvent event = new ProjectChangeEvent(this, "project", oldValue, $project);
    for (PropertyChangeListener<ProjectChangeEvent> listener : $projectChangeListeners) {
      listener.propertyChange(event);
      // same event, because is immutable
    }
//    if (oldValue != null) {
//      oldValue.fireTaskRemovedEvent(this);
//    }
//    if ($project != null) {
//      $project.fireTaskAddedEvent(this);
//    }
  }

  private final Set<PropertyChangeListener<ProjectChangeEvent>> $projectChangeListeners =
      new HashSet<PropertyChangeListener<ProjectChangeEvent>>();

}

