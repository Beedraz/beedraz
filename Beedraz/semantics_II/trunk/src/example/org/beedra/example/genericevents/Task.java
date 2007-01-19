package org.beedra.example.genericevents;

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

