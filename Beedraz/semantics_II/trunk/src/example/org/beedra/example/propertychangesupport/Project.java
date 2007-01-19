package org.beedra.example.propertychangesupport;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
      $pcs.firePropertyChange("name", oldName, name);
    }
  }

  private String $name;


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

  final void fireTaskRemoved(Task removedTask) {
    $pcs.firePropertyChange(new TaskRemovedEvent(this, "tasks", removedTask));
  }

  final void fireTaskAdded(Task addedTask) {
    $pcs.firePropertyChange(new TaskAddedEvent(this, "tasks", addedTask));
  }

  private final Set<Task> $tasks = new HashSet<Task>();

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

