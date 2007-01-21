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


/**
 * @invar getSource() != null;
 * @invar getPropertyName() != null;
 * @note better invar: is a true property name of source
 * @note must be immutable
 * @note in Swing, the UndoableEdit is wrapped in the UndoableEditEvent, instead of inheritance; should we do this?
 */
public class NameChangeEvent extends PropertyChangeEvent<Project, String> {

  /**
   * @pre source != null;
   * @post getSource() == sourcel
   * @post getPropertyName().equals(propertyName);
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post canUndo();
   */
  public NameChangeEvent(Project source, String propertyName, String oldValue, String newValue) {
    super(source, propertyName, oldValue, newValue);
  }

  @Override
  protected void setValue(Project source, String value) {
    source.setName(value);
  }

}

