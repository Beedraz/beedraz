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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.beedra.example.z_beedra.Project;
import org.beedra.example.z_beedra.Task;
import org.beedra.util_I.Comparison;
import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.bean.BeanBeed;
import org.beedra_II.bean.BeanEvent;
import org.beedra_II.edit.Edit;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.EditEvent;
import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
import org.beedra_II.property.association.BidirToManyBeed;
import org.beedra_II.property.association.BidirToOneEdit;
import org.beedra_II.property.association.BidirToOneEvent;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.FinalIntegerEvent;
import org.beedra_II.property.integer.IntegerBeed;
import org.beedra_II.property.integer.IntegerEditEvent;
import org.beedra_II.property.integer.IntegerEvent;
import org.beedra_II.property.integer.IntegerSumBeed;
import org.beedra_II.property.set.SetEvent;
import org.beedra_II.property.simple.OldNewEvent;
import org.beedra_II.property.string.StringEdit;
import org.beedra_II.property.string.StringEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestBeedra {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
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

  class OldNewListener implements Listener<StringEvent> {

    public void beedChanged(StringEvent event) {
      $event = event;
    }

    public StringEvent $event;

  }

  class BeanListener implements Listener<BeanEvent> {

    public void beedChanged(BeanEvent event) {
      $event = event;
    }

    public BeanEvent $event;

  }

  class EditListener implements Listener<EditEvent<?>> {

    public void beedChanged(EditEvent<?> event) {
      $edit = event.getEdit();
    }

    public Edit<?> $edit;

  }

  public final static String name1 = "TEST";
  public final static String name2 = "TEST ANOTHER";

  @Test
  public void projectWithName() throws EditStateException, IllegalEditException {
    Project project = new Project();
    OldNewListener nameListener = new OldNewListener();
    project.name.addListener(nameListener);
    BeanListener projectListener = new BeanListener();
    project.addListener(projectListener);
    EditListener editListener = new EditListener();
//    project.addListener(editListener); MUDO problem
    project.name.addListener(editListener);

//    project.name.set(name1);
    StringEdit setter = new StringEdit(project.name);
    setter.setGoal(name1);
    setter.perform();
    validateNameBeedSet(project, null, name1, setter, nameListener, projectListener, editListener);

    Edit<?> edit = editListener.$edit;
    editListener.$edit = null;
    edit.undo();
    validateNameBeedSet(project, name1, null, setter, nameListener, projectListener, editListener);

    edit = editListener.$edit;
    editListener.$edit = null;
    edit.redo();
    validateNameBeedSet(project, null, name1, setter, nameListener, projectListener, editListener);

//    project.name.set(name1);
    setter = new StringEdit(project.name);
    setter.setGoal(name1);
    setter.perform();
    validateNameBeedSet(project, name1, name1, null, nameListener, projectListener, editListener);

//    project.name.set(name2);
    setter = new StringEdit(project.name);
    setter.setGoal(name2);
    setter.perform();
    validateNameBeedSet(project, name1, name2, setter, nameListener, projectListener, editListener);

    edit = editListener.$edit;
    editListener.$edit = null;
    edit.undo();
    validateNameBeedSet(project, name2, name1, setter, nameListener, projectListener, editListener);

    edit = editListener.$edit;
    editListener.$edit = null;
    edit.redo();
    validateNameBeedSet(project, name1, name2, setter, nameListener, projectListener, editListener);

//    project.name.set(null);
    setter = new StringEdit(project.name);
    setter.setGoal(null);
    setter.perform();
    validateNameBeedSet(project, name2, null, setter, nameListener, projectListener, editListener);

    edit = editListener.$edit;
    editListener.$edit = null;
    edit.undo();
    validateNameBeedSet(project, null, name2, setter, nameListener, projectListener, editListener);

    edit = editListener.$edit;
    editListener.$edit = null;
    edit.redo();
    validateNameBeedSet(project, name2, null, setter, nameListener, projectListener, editListener);

    //  project.name.set(null);
    setter = new StringEdit(project.name);
    setter.setGoal(null);
    setter.perform();
    validateNameBeedSet(project, null, null, null, nameListener, projectListener, editListener);
  }

  private void validateNameBeedSet(Project project,
                                   String expectedOldName,
                                   String expectedNewName,
                                   Edit<?> expectedEdit,
                                   OldNewListener nameListener,
                                   BeanListener projectListener,
                                   EditListener editListener) {
    if (Comparison.equalsWithNull(expectedOldName, expectedNewName)) {
      assertNull(nameListener.$event);
      assertNull(projectListener.$event);
    }
    else {
      assertNotNull(nameListener.$event);
      assertEquals(project.name, nameListener.$event.getSource());
      assertEquals(expectedOldName, nameListener.$event.getOldValue());
      assertEquals(expectedNewName, nameListener.$event.getNewValue());
      assertNotNull(projectListener.$event);
      assertEquals(project, projectListener.$event.getSource());
      assertEquals(nameListener.$event, projectListener.$event.getCause());
      assertEquals(expectedEdit, editListener.$edit);
    }
    assertEquals(expectedNewName, project.name.get());
    nameListener.$event = null;
    projectListener.$event = null;
  }

  @Test
  public void listenerDemo() {
    Listener<Event> el = new Listener<Event>() {
      public void beedChanged(Event event) {
        System.out.println(event);
      }
    };
    Listener<OldNewEvent<Integer>> onel = new Listener<OldNewEvent<Integer>>() {
      public void beedChanged(OldNewEvent<Integer> event) {
        System.out.println(event);
      }
    };
    Listener<IntegerEvent> iel = new Listener<IntegerEvent>() {
      public void beedChanged(IntegerEvent event) {
        System.out.println(event);
      }
    };
    @SuppressWarnings("unused")
    Listener<FinalIntegerEvent> fiel = new Listener<FinalIntegerEvent>() {
      public void beedChanged(FinalIntegerEvent event) {
        System.out.println(event);
      }
    };
    Listener<IntegerEditEvent> ieel = new Listener<IntegerEditEvent>() {
      public void beedChanged(IntegerEditEvent event) {
        System.out.println(event);
      }
    };
    Listener<EditEvent<?>> eel = new Listener<EditEvent<?>>() {
      public void beedChanged(EditEvent<?> event) {
        System.out.println(event);
      }
    };

    BeanBeed dummy = new AbstractBeanBeed() {
      // NOP
    };

    IntegerBeed<?> ib = new IntegerSumBeed(dummy);
    ib.addListener(el);
    ib.addListener(onel);
    ib.addListener(iel);
//    ib.addListener(fiel);
//    ib.addListener(ieel);
//    ib.addListener(eel);

    EditableBeed<?> eb = new EditableIntegerBeed(dummy);
    eb.addListener(el);
//    eb.addListener(onel);
//    eb.addListener(iel);
//    eb.addListener(fiel);
//    eb.addListener(ieel);
    eb.addListener(eel);

    IntegerSumBeed isb = new IntegerSumBeed(dummy);
    isb.addListener(el);
    isb.addListener(onel);
    isb.addListener(iel);
//    isb.addListener(fiel);
//    isb.addListener(ieel);
//    isb.addListener(eel);

    EditableIntegerBeed eib = new EditableIntegerBeed(dummy);
    eib.addListener(el);
    eib.addListener(onel);
    eib.addListener(iel);
//    eib.addListener(fiel);
    eib.addListener(ieel);
    eib.addListener(eel);
  }


  public class BidirToOneListener implements Listener<BidirToOneEvent<?>> {

    public void beedChanged(BidirToOneEvent<?> event) {
      $event = event;
    }

    public BidirToOneEvent<?> $event;
  }

  public class BidirToManyListener implements Listener<SetEvent<?>> {

    public void beedChanged(SetEvent<?> event) {
      $event = event;
    }

    public SetEvent<?> $event;
  }

  @Test
  public void projectWithTask() throws EditStateException, IllegalEditException {
    Listener<Event> allroundListener = new Listener<Event>() {

      public void beedChanged(Event event) {
        StringBuffer sb = new StringBuffer();
        event.toString(sb, 0);
        System.out.println(sb);
      }

    };
    Task task = new Task();
    BeanListener taskListener = new BeanListener();
    task.addListener(taskListener);
    task.addListener(allroundListener);
    BidirToOneListener taskProjectListener = new BidirToOneListener();
    task.project.addListener(taskProjectListener);
    EditListener editListener = new EditListener();
    task.project.addListener(editListener);
    task.project.addListener(allroundListener);
    assertNull(task.project.get());
    assertTrue(task.isListener(taskListener));
    assertTrue(task.isListener(allroundListener));
    assertTrue(task.project.isListener(taskProjectListener));
    assertTrue(task.project.isListener(editListener));
    assertTrue(task.project.isListener(allroundListener));

//    task.project.set(null);
    BidirToOneEdit<Project, Task> setter = new BidirToOneEdit<Project, Task>(task.project);
    setter.setGoal(null);
    setter.perform();
    validateNoEvents(taskListener, taskProjectListener, null, null, null, null);
    validateProjectTaskState(task, null, true, null, false);

    Project project1 = new Project();
    StringEdit nameSetter = new StringEdit(project1.name);
    nameSetter.setGoal("project 1");
    nameSetter.perform();
    BeanListener project1Listener = new BeanListener();
    project1.addListener(project1Listener);
    project1.addListener(allroundListener);
    BidirToManyListener project1TasksListener = new BidirToManyListener();
    project1.tasks.addListener(project1TasksListener);
    project1.tasks.addListener(allroundListener);
    assertNotNull(project1.tasks.get());
    assertTrue(project1.tasks.get().isEmpty());
    assertTrue(project1.isListener(project1Listener));
    assertTrue(project1.isListener(allroundListener));
    assertTrue(project1.tasks.isListener(project1TasksListener));
    assertTrue(project1.tasks.isListener(allroundListener));

//    task.project.set(project1);
    setter = new BidirToOneEdit<Project, Task>(task.project);
    setter.setGoal(project1);
    setter.perform();
    assertNotNull(editListener.$edit);
    validateTaskListener(task, taskListener, taskProjectListener);
    validateProjectListener(project1, project1Listener, project1TasksListener);
    validateTaskProjectEvent(task, taskProjectListener, null, project1);
    validateTaskAdded(task, project1TasksListener, project1.tasks);
    validateProjectTaskState(task, project1, true, null, false);

    Edit<?> edit = editListener.$edit;
    editListener.$edit = null;
    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
    edit.undo();
    assertNotNull(editListener.$edit);
    validateTaskListener(task, taskListener, taskProjectListener);
    validateProjectListener(project1, project1Listener, project1TasksListener);
    validateTaskProjectEvent(task, taskProjectListener, project1, null);
    validateTaskRemoved(task, project1TasksListener, project1.tasks);
    validateProjectTaskState(task, project1, false, null, false);

    edit = editListener.$edit;
    editListener.$edit = null;
    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
    edit.redo();
    assertNotNull(editListener.$edit);
    validateTaskListener(task, taskListener, taskProjectListener);
    validateProjectListener(project1, project1Listener, project1TasksListener);
    validateTaskProjectEvent(task, taskProjectListener, null, project1);
    validateTaskAdded(task, project1TasksListener, project1.tasks);
    validateProjectTaskState(task, project1, true, null, false);


    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
//    task.project.set(project1);
    setter = new BidirToOneEdit<Project, Task>(task.project);
    setter.setGoal(project1);
    setter.perform();
    validateNoEvents(taskListener, taskProjectListener, project1Listener, project1TasksListener, null, null);
    validateProjectTaskState(task, project1, true, null, false);

    Project project2 = new Project();
    nameSetter = new StringEdit(project2.name);
    nameSetter.setGoal("project 2");
    nameSetter.perform();
    BeanListener project2Listener = new BeanListener();
    project2.addListener(project2Listener);
    project2.addListener(allroundListener);
    BidirToManyListener project2TasksListener = new BidirToManyListener();
    project2.tasks.addListener(project2TasksListener);
    project2.tasks.addListener(allroundListener);
    assertNotNull(project2.tasks.get());
    assertTrue(project2.tasks.get().isEmpty());
    assertTrue(project2.isListener(project2Listener));
    assertTrue(project2.isListener(allroundListener));
    assertTrue(project2.tasks.isListener(project2TasksListener));
    assertTrue(project2.tasks.isListener(allroundListener));

    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    project2TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
    project2Listener.$event = null;
//  task.project.set(project2);
    setter = new BidirToOneEdit<Project, Task>(task.project);
    setter.setGoal(project2);
    setter.perform();
    validateTaskListener(task, taskListener, taskProjectListener);
    validateProjectListener(project1, project1Listener, project1TasksListener);
    validateProjectListener(project2, project2Listener, project2TasksListener);
    validateTaskProjectEvent(task, taskProjectListener, project1, project2);
    validateTaskRemoved(task, project1TasksListener, project1.tasks);
    validateTaskAdded(task, project2TasksListener, project2.tasks);
    validateProjectTaskState(task, project1, false, project2, true);

    edit = editListener.$edit;
    editListener.$edit = null;
    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
    edit.undo();
    assertNotNull(editListener.$edit);
    validateTaskListener(task, taskListener, taskProjectListener);
    validateProjectListener(project1, project1Listener, project1TasksListener);
    validateProjectListener(project2, project2Listener, project2TasksListener);
    validateTaskProjectEvent(task, taskProjectListener, project2, project1);
    validateTaskAdded(task, project1TasksListener, project1.tasks);
    validateTaskRemoved(task, project2TasksListener, project2.tasks);
    validateProjectTaskState(task, project1, true, project2, false);

    edit = editListener.$edit;
    editListener.$edit = null;
    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
    edit.redo();
    validateTaskListener(task, taskListener, taskProjectListener);
    validateProjectListener(project1, project1Listener, project1TasksListener);
    validateProjectListener(project2, project2Listener, project2TasksListener);
    validateTaskProjectEvent(task, taskProjectListener, project1, project2);
    validateTaskRemoved(task, project1TasksListener, project1.tasks);
    validateTaskAdded(task, project2TasksListener, project2.tasks);
    validateProjectTaskState(task, project1, false, project2, true);

    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    project2TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
    project2Listener.$event = null;
//    task.project.set(project2);
    setter = new BidirToOneEdit<Project, Task>(task.project);
    setter.setGoal(project2);
    setter.perform();
    validateNoEvents(taskListener, taskProjectListener, project1Listener, project1TasksListener, project2Listener, project2TasksListener);
    validateProjectTaskState(task, project1, false, project2, true);


    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    project2TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
    project2Listener.$event = null;
//  task.project.set(null);
    setter = new BidirToOneEdit<Project, Task>(task.project);
//    edit.setGoal(null);
    setter.perform();
    assertNull(project1TasksListener.$event);
    assertNull(project1Listener.$event);
    validateTaskListener(task, taskListener, taskProjectListener);
    validateProjectListener(project2, project2Listener, project2TasksListener);
    validateTaskProjectEvent(task, taskProjectListener, project2, null);
    validateTaskRemoved(task, project2TasksListener, project2.tasks);
    validateProjectTaskState(task, project1, false, project2, false);

    edit = editListener.$edit;
    editListener.$edit = null;
    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
    edit.undo();
    assertNull(project1TasksListener.$event);
    assertNull(project1Listener.$event);
    validateTaskListener(task, taskListener, taskProjectListener);
    validateProjectListener(project2, project2Listener, project2TasksListener);
    validateTaskProjectEvent(task, taskProjectListener, null, project2);
    validateTaskAdded(task, project2TasksListener, project2.tasks);
    validateProjectTaskState(task, project1, false, project2, true);

    edit = editListener.$edit;
    editListener.$edit = null;
    taskProjectListener.$event = null;
    project1TasksListener.$event = null;
    taskListener.$event = null;
    project1Listener.$event = null;
    edit.redo();
    assertNull(project1TasksListener.$event);
    assertNull(project1Listener.$event);
    validateTaskListener(task, taskListener, taskProjectListener);
    validateProjectListener(project2, project2Listener, project2TasksListener);
    validateTaskProjectEvent(task, taskProjectListener, project2, null);
    validateTaskRemoved(task, project2TasksListener, project2.tasks);
    validateProjectTaskState(task, project1, false, project2, false);
}

  private void validateTaskListener(Task task, BeanListener taskListener, BidirToOneListener taskProjectListener) {
    assertNotNull(taskListener.$event);
    assertEquals(task, taskListener.$event.getSource());
    assertEquals(taskProjectListener.$event, taskListener.$event.getCause());
  }

  private void validateProjectListener(Project project, BeanListener projectListener, BidirToManyListener projectTasksListener) {
    assertNotNull(projectListener.$event);
    assertEquals(project, projectListener.$event.getSource());
    assertEquals(projectTasksListener.$event, projectListener.$event.getCause());
  }

  private void validateNoEvents(BeanListener taskListener, BidirToOneListener taskProjectListener, BeanListener project1Listener, BidirToManyListener project1TasksListener, BeanListener project2Listener, BidirToManyListener project2TasksListener) {
    assertNull(taskProjectListener.$event);
    assertNull(taskListener.$event);
    if (project1TasksListener != null) {
      assertNull(project1TasksListener.$event);
    }
    if (project1Listener != null) {
      assertNull(project1Listener.$event);
    }
    if (project2TasksListener != null) {
      assertNull(project2TasksListener.$event);
    }
    if (project2Listener != null) {
      assertNull(project2Listener.$event);
    }
  }

  private void validateTaskProjectEvent(Task task, BidirToOneListener taskProjectListener, Project oldValue, Project newValue) {
    assertNotNull(taskProjectListener.$event);
    assertEquals(task.project, taskProjectListener.$event.getSource());
    assertEquals(oldValue, taskProjectListener.$event.getOldValue());
    assertEquals(newValue, taskProjectListener.$event.getNewValue());
  }

  private void validateProjectTaskState(Task task, Project project1, boolean taskExpectedIn1, Project project2, boolean taskExpectedIn2) {
    assertEquals(taskExpectedIn1 ? project1 : taskExpectedIn2 ? project2 : null, task.project.get());
    if (project1 != null) {
      assertNotNull(project1.tasks.get());
    }
    if (project2 != null) {
      assertNotNull(project2.tasks.get());
    }
    validateTaskInProject(task, project1, taskExpectedIn1);
    validateTaskInProject(task, project2, taskExpectedIn2);
  }

  private void validateTaskInProject(Task task, Project project, boolean taskExpected) {
    if (project != null) {
      if (taskExpected) {
        assertTrue(project.tasks.get().size() == 1);
        assertTrue(project.tasks.get().contains(task));
      }
      else {
        assertTrue(project.tasks.get().isEmpty());
      }
    }
  }

  private void validateTaskAdded(Task task, BidirToManyListener projectTasksListener, BidirToManyBeed<Project, Task> expectedSource) {
    assertEquals(expectedSource, projectTasksListener.$event.getSource());
    assertNotNull(projectTasksListener.$event);
    assertNotNull(projectTasksListener.$event.getAddedElements());
    assertTrue(projectTasksListener.$event.getAddedElements().size() == 1);
    assertTrue(projectTasksListener.$event.getAddedElements().contains(task));
    assertNotNull(projectTasksListener.$event.getRemovedElements());
    assertTrue(projectTasksListener.$event.getRemovedElements().isEmpty());
  }

  private void validateTaskRemoved(Task task, BidirToManyListener projectTasksListener, BidirToManyBeed<Project, Task> expectedSource) {
    assertEquals(expectedSource, projectTasksListener.$event.getSource());
    assertNotNull(projectTasksListener.$event);
    assertNotNull(projectTasksListener.$event.getAddedElements());
    assertTrue(projectTasksListener.$event.getAddedElements().isEmpty());
    assertNotNull(projectTasksListener.$event.getRemovedElements());
    assertTrue(projectTasksListener.$event.getRemovedElements().size() == 1);
    assertTrue(projectTasksListener.$event.getRemovedElements().contains(task));
  }

}

