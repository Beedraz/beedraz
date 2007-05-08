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

package org.beedra_II;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.beedra.example.z_beedra.Project;
import org.beedra.example.z_beedra.Task;
import org.beedra_II.property.association.BidirToManyPBeed;
import org.beedra_II.property.association.BidirToOnePDoBeed;
import org.beedra_II.property.set.SetEvent;
import org.beedra_II.property.simple.StraightSimplePDB;
import org.beedra_II.property.simple.UndoableOldNewBEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestBeedra {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void createProject() {
    @SuppressWarnings("unused")
    Project project = new Project();
  }

  @Test
  public void createTask() {
    @SuppressWarnings("unused")
    Task task = new Task();
  }

  /*
   * problem? I am not interested in undoability. A listener for an BeedEvent should suffice.
   * ? super XXX?????
   */
  class NameChangedListener implements BeedListener<UndoableOldNewBEvent<StraightSimplePDB<String>, String>> {

    public void beedChanged(UndoableOldNewBEvent<StraightSimplePDB<String>, String> event) {
      $event = event;
    }

    public UndoableOldNewBEvent<StraightSimplePDB<String>, String> $event;

  }

  @Test
  public void projectWithName() {
    Project project = new Project();
    NameChangedListener listener = new NameChangedListener();
    project.name.addChangeListener(listener);
    project.name.set("TEST");
    assertNotNull(listener.$event);
    assertEquals(project.name, listener.$event.getSource());
    assertNull(listener.$event.getOldValue());
    assertEquals("TEST", listener.$event.getNewValue());
    assertEquals("TEST", project.name.get());
    listener.$event = null;
    project.name.set("TEST");
    assertNull(listener.$event);
    project.name.set("TEST ANOTHER");
    assertEquals(project.name, listener.$event.getSource());
    assertEquals("TEST", listener.$event.getOldValue());
    assertEquals("TEST ANOTHER", listener.$event.getNewValue());
    assertEquals("TEST ANOTHER", project.name.get());
  }

  /*
   * problem? I am not interested in undoability. A listener for an BeedEvent should suffice.
   * ? super XXX?????
   */
  class ProjectChangedListener implements BeedListener<UndoableOldNewBEvent<BidirToOnePDoBeed<Project, Task>, Project>> {

    public void beedChanged(UndoableOldNewBEvent<BidirToOnePDoBeed<Project, Task>, Project> event) {
      $event = event;
    }

    public UndoableOldNewBEvent<BidirToOnePDoBeed<Project, Task>, Project> $event;

  }

  class TasksChangedListener implements BeedListener<SetEvent<Task, BidirToManyPBeed<Project, Task>>> {

    public void beedChanged(SetEvent<Task, BidirToManyPBeed<Project, Task>> event) {
      $event = event;
    }

    public SetEvent<Task, BidirToManyPBeed<Project, Task>> $event;

  }

  @Test
  public void projectWithTask() {
    Task task = new Task();
    ProjectChangedListener taskProjectListener = new ProjectChangedListener();
    task.project.addChangeListener(taskProjectListener);
    assertNull(task.project.get());
    assertTrue(task.project.isChangeListener(taskProjectListener));

    task.project.set(null);
    assertNull(taskProjectListener.$event);
    assertNull(task.project.get());

    Project project1 = new Project();
    TasksChangedListener project1TasksListener = new TasksChangedListener();
    project1.tasks.addChangeListener(project1TasksListener);
    assertNotNull(project1.tasks.get());
    assertTrue(project1.tasks.get().isEmpty());
    assertTrue(project1.tasks.isChangeListener(project1TasksListener));

    task.project.set(project1);
    assertNotNull(taskProjectListener.$event);
    assertNotNull(project1TasksListener.$event);
    assertEquals(task.project, taskProjectListener.$event.getSource());
    assertEquals(project1.tasks, project1TasksListener.$event.getSource());
    assertEquals(null, taskProjectListener.$event.getOldValue());
    assertEquals(project1, taskProjectListener.$event.getNewValue());
    assertNotNull(project1TasksListener.$event.getAddedElements());
    assertTrue(project1TasksListener.$event.getAddedElements().size() == 1);
    assertTrue(project1TasksListener.$event.getAddedElements().contains(task));
    assertNotNull(project1TasksListener.$event.getRemovedElements());
    assertTrue(project1TasksListener.$event.getRemovedElements().isEmpty());
    assertEquals(project1, task.project.get());
    assertNotNull(project1.tasks.get());
    assertTrue(project1.tasks.get().size() == 1);
    assertTrue(project1.tasks.get().contains(task));

    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    task.project.set(project1);
    assertNull(taskProjectListener.$event);
    assertNull(project1TasksListener.$event);
    assertEquals(project1, task.project.get());
    assertNotNull(project1.tasks.get());
    assertTrue(project1.tasks.get().size() == 1);
    assertTrue(project1.tasks.get().contains(task));

    Project project2 = new Project();
    TasksChangedListener project2TasksListener = new TasksChangedListener();
    project2.tasks.addChangeListener(project2TasksListener);
    assertNotNull(project2.tasks.get());
    assertTrue(project2.tasks.get().isEmpty());
    assertTrue(project2.tasks.isChangeListener(project2TasksListener));

    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    task.project.set(project2);
    assertNotNull(taskProjectListener.$event);
    assertNotNull(project1TasksListener.$event);
    assertEquals(task.project, taskProjectListener.$event.getSource());
    assertEquals(project1.tasks, project1TasksListener.$event.getSource());
    assertEquals(project2.tasks, project2TasksListener.$event.getSource());
    assertEquals(project1, taskProjectListener.$event.getOldValue());
    assertEquals(project2, taskProjectListener.$event.getNewValue());
    assertNotNull(project1TasksListener.$event.getAddedElements());
    assertTrue(project1TasksListener.$event.getAddedElements().isEmpty());
    assertNotNull(project1TasksListener.$event.getRemovedElements());
    assertTrue(project1TasksListener.$event.getRemovedElements().size() == 1);
    assertTrue(project1TasksListener.$event.getRemovedElements().contains(task));
    assertNotNull(project2TasksListener.$event.getAddedElements());
    assertTrue(project2TasksListener.$event.getAddedElements().size() == 1);
    assertNotNull(project2TasksListener.$event.getRemovedElements());
    assertTrue(project2TasksListener.$event.getAddedElements().contains(task));
    assertTrue(project2TasksListener.$event.getRemovedElements().isEmpty());
    assertEquals(project2, task.project.get());
    assertNotNull(project1.tasks.get());
    assertTrue(project1.tasks.get().isEmpty());
    assertNotNull(project2.tasks.get());
    assertTrue(project2.tasks.get().size() == 1);
    assertTrue(project2.tasks.get().contains(task));

    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    project2TasksListener.$event = null;
    task.project.set(null);
    assertNotNull(taskProjectListener.$event);
    assertNull(project1TasksListener.$event);
    assertNotNull(project2TasksListener.$event);
    assertEquals(task.project, taskProjectListener.$event.getSource());
    assertEquals(project2.tasks, project2TasksListener.$event.getSource());
    assertEquals(project2, taskProjectListener.$event.getOldValue());
    assertNull(taskProjectListener.$event.getNewValue());
    assertNotNull(project2TasksListener.$event.getAddedElements());
    assertTrue(project2TasksListener.$event.getAddedElements().isEmpty());
    assertNotNull(project2TasksListener.$event.getRemovedElements());
    assertNull(task.project.get());
    assertNotNull(project1.tasks.get());
    assertTrue(project1.tasks.get().isEmpty());
    assertNotNull(project2.tasks.get());
    assertTrue(project2.tasks.get().isEmpty());
  }

}

