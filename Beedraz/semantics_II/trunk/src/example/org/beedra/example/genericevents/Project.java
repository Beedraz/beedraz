package org.beedra.example.genericevents;

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

}

