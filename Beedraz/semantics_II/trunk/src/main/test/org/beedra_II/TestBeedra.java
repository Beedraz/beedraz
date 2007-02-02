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
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.event.EditEvent;
import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
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

  public final static String name1 = "TEST";
  public final static String name2 = "TEST ANOTHER";

  @Test
  public void projectWithName() throws EditStateException, IllegalEditException {
    Project project = new Project();
    OldNewListener nameListener = new OldNewListener();
    project.name.addListener(nameListener);
    BeanListener projectListener = new BeanListener();
    project.addListener(projectListener);

//    project.name.set(name1);
    StringEdit setter = new StringEdit(project.name);
    setter.setGoal(name1);
    setter.perform();
    validateNameBeedSet(project, null, name1, nameListener, projectListener);

//    project.name.set(name1);
    setter = new StringEdit(project.name);
    setter.setGoal(name1);
    setter.perform();
    validateNameBeedSet(project, name1, name1, nameListener, projectListener);

//    project.name.set(name2);
    setter = new StringEdit(project.name);
    setter.setGoal(name2);
    setter.perform();
    validateNameBeedSet(project, name1, name2, nameListener, projectListener);

//    project.name.set(null);
    setter = new StringEdit(project.name);
    setter.setGoal(null);
    setter.perform();
    validateNameBeedSet(project, name2, null, nameListener, projectListener);

    //  project.name.set(null);
    setter = new StringEdit(project.name);
    setter.setGoal(null);
    setter.perform();
    validateNameBeedSet(project, null, null, nameListener, projectListener);
  }

  private void validateNameBeedSet(Project project,
                                   String expectedOldName,
                                   String expectedNewName,
                                   OldNewListener nameListener,
                                   BeanListener projectListener) {
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
    Listener<EditEvent> eel = new Listener<EditEvent>() {
      public void beedChanged(EditEvent event) {
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


  public class BidirToOneListener implements Listener<BidirToOneEvent> {

    public void beedChanged(BidirToOneEvent event) {
      $event = event;
    }

    public BidirToOneEvent $event;
  }

  public class BidirToManyListener implements Listener<SetEvent> {

    public void beedChanged(SetEvent event) {
      $event = event;
    }

    public SetEvent $event;
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
    task.project.addListener(allroundListener);
    assertNull(task.project.get());
    assertTrue(task.isListener(taskListener));
    assertTrue(task.isListener(allroundListener));
    assertTrue(task.project.isListener(taskProjectListener));
    assertTrue(task.project.isListener(allroundListener));

//    task.project.set(null);
    BidirToOneEdit<Project, Task> edit = new BidirToOneEdit<Project, Task>(task.project);
    edit.setGoal(null);
    edit.perform();
    assertNull(taskListener.$event);
    assertNull(taskProjectListener.$event);
    assertNull(task.project.get());

    Project project1 = new Project();
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
    edit = new BidirToOneEdit<Project, Task>(task.project);
    edit.setGoal(project1);
    edit.perform();
    assertNotNull(taskProjectListener.$event);
    assertNotNull(project1TasksListener.$event);
    assertNotNull(taskListener.$event);
    assertNotNull(project1Listener.$event);
    assertEquals(task.project, taskProjectListener.$event.getSource());
    assertEquals(project1.tasks, project1TasksListener.$event.getSource());
    assertEquals(task, taskListener.$event.getSource());
    assertEquals(project1, project1Listener.$event.getSource());
//    assertEquals(null, taskProjectListener.$event.getOldValue());
//    assertEquals(project1, taskProjectListener.$event.getNewValue());
//    assertNotNull(project1TasksListener.$event.getAddedElements());
//    assertTrue(project1TasksListener.$event.getAddedElements().size() == 1);
//    assertTrue(project1TasksListener.$event.getAddedElements().contains(task));
//    assertNotNull(project1TasksListener.$event.getRemovedElements());
//    assertTrue(project1TasksListener.$event.getRemovedElements().isEmpty());
//    assertEquals(project1, task.project.get());
//    assertNotNull(project1.tasks.get());
//    assertTrue(project1.tasks.get().size() == 1);
//    assertTrue(project1.tasks.get().contains(task));
//
//    taskProjectListener.$event = null;
//    project1TasksListener.$event = null;
//    task.project.set(project1);
//    assertNull(taskProjectListener.$event);
//    assertNull(project1TasksListener.$event);
//    assertEquals(project1, task.project.get());
//    assertNotNull(project1.tasks.get());
//    assertTrue(project1.tasks.get().size() == 1);
//    assertTrue(project1.tasks.get().contains(task));
//
//    Project project2 = new Project();
//    TasksChangedListener project2TasksListener = new TasksChangedListener();
//    project2.tasks.addChangeListener(project2TasksListener);
//    assertNotNull(project2.tasks.get());
//    assertTrue(project2.tasks.get().isEmpty());
//    assertTrue(project2.tasks.isChangeListener(project2TasksListener));
//
//    taskProjectListener.$event = null;
//    project1TasksListener.$event = null;
//    task.project.set(project2);
//    assertNotNull(taskProjectListener.$event);
//    assertNotNull(project1TasksListener.$event);
//    assertEquals(task.project, taskProjectListener.$event.getSource());
//    assertEquals(project1.tasks, project1TasksListener.$event.getSource());
//    assertEquals(project2.tasks, project2TasksListener.$event.getSource());
//    assertEquals(project1, taskProjectListener.$event.getOldValue());
//    assertEquals(project2, taskProjectListener.$event.getNewValue());
//    assertNotNull(project1TasksListener.$event.getAddedElements());
//    assertTrue(project1TasksListener.$event.getAddedElements().isEmpty());
//    assertNotNull(project1TasksListener.$event.getRemovedElements());
//    assertTrue(project1TasksListener.$event.getRemovedElements().size() == 1);
//    assertTrue(project1TasksListener.$event.getRemovedElements().contains(task));
//    assertNotNull(project2TasksListener.$event.getAddedElements());
//    assertTrue(project2TasksListener.$event.getAddedElements().size() == 1);
//    assertNotNull(project2TasksListener.$event.getRemovedElements());
//    assertTrue(project2TasksListener.$event.getAddedElements().contains(task));
//    assertTrue(project2TasksListener.$event.getRemovedElements().isEmpty());
//    assertEquals(project2, task.project.get());
//    assertNotNull(project1.tasks.get());
//    assertTrue(project1.tasks.get().isEmpty());
//    assertNotNull(project2.tasks.get());
//    assertTrue(project2.tasks.get().size() == 1);
//    assertTrue(project2.tasks.get().contains(task));
//
//    taskProjectListener.$event = null;
//    project1TasksListener.$event = null;
//    project2TasksListener.$event = null;
//    task.project.set(null);
//    assertNotNull(taskProjectListener.$event);
//    assertNull(project1TasksListener.$event);
//    assertNotNull(project2TasksListener.$event);
//    assertEquals(task.project, taskProjectListener.$event.getSource());
//    assertEquals(project2.tasks, project2TasksListener.$event.getSource());
//    assertEquals(project2, taskProjectListener.$event.getOldValue());
//    assertNull(taskProjectListener.$event.getNewValue());
//    assertNotNull(project2TasksListener.$event.getAddedElements());
//    assertTrue(project2TasksListener.$event.getAddedElements().isEmpty());
//    assertNotNull(project2TasksListener.$event.getRemovedElements());
//    assertNull(task.project.get());
//    assertNotNull(project1.tasks.get());
//    assertTrue(project1.tasks.get().isEmpty());
//    assertNotNull(project2.tasks.get());
//    assertTrue(project2.tasks.get().isEmpty());


  }

}

