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


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.beedra.example.z_beedra.Project;
import org.beedra.example.z_beedra.Task;
import org.beedra_II.edit.EditStateException;
import org.beedra_II.edit.IllegalEditException;
import org.beedra_II.property.association.BidirToOneEdit;
import org.beedra_II.property.string.StringEdit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestMultipleConcurrentEdits {

  @Before
  public void setUp() throws Exception {
    // NOP
  }

  @After
  public void tearDown() throws Exception {
    // NOP
  }

  @Test
  public void testConcurrentEdits1() throws EditStateException, IllegalEditException {
    Project p = new Project();
    Task task1 = new Task();
    StringEdit nameSetter = new StringEdit(task1.name);
    nameSetter.setGoal("task 1");
    nameSetter.perform();
    Task task2 = new Task();
    nameSetter = new StringEdit(task2.name);
    nameSetter.setGoal("task 2");
    nameSetter.perform();
    BidirToOneEdit<Project, Task> projectSetter = new BidirToOneEdit<Project, Task>(task1.project);
    projectSetter.setGoal(p.tasks);
    projectSetter.perform();
    projectSetter = new BidirToOneEdit<Project, Task>(task2.project);
    projectSetter.setGoal(p.tasks);
    projectSetter.perform();

    StringBuffer result = new StringBuffer();
    p.toString(result, 0);
    System.out.println(result);

    nameSetter = new StringEdit(task1.name);
    nameSetter.setGoal("common name"); // no problems, still unique
    StringEdit nameSetter2 = new StringEdit(task2.name);
    nameSetter2.setGoal("common name"); // no problems, since not committed and unrelated edits
    nameSetter2.perform(); // no problems; but nameSetter should now be invalid

    try {
      nameSetter.perform();
      result = new StringBuffer();
      p.toString(result, 0);
      System.out.println(result);
      fail("we should get an IllegalEditException: names are no longer unique");
    }
    catch (IllegalEditException ieExc) {
      // this is ok
      result = new StringBuffer();
      p.toString(result, 0);
      System.out.println(result);
    }
  }

  @Test
  public void testConcurrentEdits2() throws EditStateException, IllegalEditException {
    Project p = new Project();
    Task task1 = new Task();
    StringEdit nameSetter = new StringEdit(task1.name);
    nameSetter.setGoal("common name");
    nameSetter.perform();
    Task task2 = new Task();
    nameSetter = new StringEdit(task2.name);
    nameSetter.setGoal("common name");
    nameSetter.perform();
    BidirToOneEdit<Project, Task> projectSetter1 = new BidirToOneEdit<Project, Task>(task1.project);
    projectSetter1.setGoal(p.tasks);
    BidirToOneEdit<Project, Task> projectSetter2 = new BidirToOneEdit<Project, Task>(task2.project);
    projectSetter2.setGoal(p.tasks);

    assertTrue(projectSetter2.isValid());
    projectSetter1.perform();
//    assertFalse(projectSetter2.isValid()); // to get this, projectSetter2 needs to listen to events from all concerned parties

    try {
      projectSetter2.perform();
      StringBuffer result = new StringBuffer();
      p.toString(result, 0);
      System.out.println(result);
      fail("we should get an IllegalEditException: names are no longer unique");
    }
    catch (IllegalEditException ieExc) {
      // this is ok
      StringBuffer result = new StringBuffer();
      p.toString(result, 0);
      System.out.println(result);
    }
  }

}

