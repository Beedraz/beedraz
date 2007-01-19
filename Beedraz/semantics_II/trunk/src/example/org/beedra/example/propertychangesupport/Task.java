package org.beedra.example.propertychangesupport;

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

