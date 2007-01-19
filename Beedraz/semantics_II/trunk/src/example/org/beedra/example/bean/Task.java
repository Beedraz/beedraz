package org.beedra.example.bean;

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
  public final boolean isProjectChangeListener(ProjectChangeListener listener) {
    return $projectChangeListeners.contains(listener);
  }

  /**
   * @post isProjectChangeListener(listener);
   */
  public final void addProjectChangeListener(ProjectChangeListener listener) {
    $projectChangeListeners.add(listener);
  }

  /**
   * @post ! isProjectChangeListener(listener);
   */
  public final void removeProjectChangeListener(ProjectChangeListener listener) {
    $projectChangeListeners.remove(listener);
  }

  private void fireProjectChangeEvent(Project oldValue) {
    ProjectChangeEvent event = new ProjectChangeEvent(this, "project", oldValue, $project);
    for (ProjectChangeListener listener : $projectChangeListeners) {
      listener.propertyChange(event);
      // same event, because is immutable
    }
    if (oldValue != null) {
      oldValue.fireTaskRemovedEvent(this);
    }
    if ($project != null) {
      $project.fireTaskAddedEvent(this);
    }
  }

  private final Set<ProjectChangeListener> $projectChangeListeners = new HashSet<ProjectChangeListener>();

}

