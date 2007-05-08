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

package org.beedra.example.b_propertychangesupport;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


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
      $pcs.firePropertyChange("project", oldValue, project);
      if (oldValue != null) {
        oldValue.fireTaskRemoved(this);
      }
      if (project != null) {
        project.fireTaskAdded(this);
      }
    }
  }

  private Project $project;

  //  public final boolean isNameChangeListener(NameChangeListener listener) {

  /**
   * @post isPropertyChangeListener(propertyName, listener);
   */
  public final void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    $pcs.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * @post ! isPropertyChangeListener(propertyName, listener);
   */
  public final void removeNameChangeListener(String propertyName, PropertyChangeListener listener) {
    $pcs.removePropertyChangeListener(propertyName, listener);
  }

  private PropertyChangeSupport $pcs = new PropertyChangeSupport(this);

}

