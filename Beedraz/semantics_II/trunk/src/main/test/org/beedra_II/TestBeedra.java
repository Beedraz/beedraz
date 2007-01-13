package org.beedra_II;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.beedra.example.beedra.Project;
import org.beedra.example.beedra.Task;
import org.beedra_II.beed.BeedChangeEvent;
import org.beedra_II.beed.BeedChangeListener;
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
    Project project = new Project();
  }

  @Test
  public void createTask() {
    Task task = new Task();
  }

  class NameChangedListener implements BeedChangeListener<BeedChangeEvent<String>> {

    public void propertyChange(BeedChangeEvent<String> event) {
      $event = event;
    }

    public BeedChangeEvent<String> $event;

  }

  @Test
  public void projectWithName() {
    Project project = new Project();
    NameChangedListener listener = new NameChangedListener();
    project.name.addChangeListener(listener);
    project.name.set("TEST");
    assertNotNull(listener.$event);
    assertEquals(project.name, listener.$event.getSource());
    assertEquals(null, listener.$event.getOldValue());
    assertEquals("TEST", listener.$event.getNewValue());
    assertEquals("TEST", project.name.get());
    listener.$event = null;
    project.name.set("TEST");
    assertEquals(null, listener.$event);
    project.name.set("TEST ANOTHER");
    assertEquals(project.name, listener.$event.getSource());
    assertEquals("TEST", listener.$event.getOldValue());
    assertEquals("TEST ANOTHER", listener.$event.getNewValue());
    assertEquals("TEST ANOTHER", project.name.get());
  }

}

