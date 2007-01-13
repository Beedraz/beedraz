package org.beedra.example.beedra;

import org.beedra_II.BeedraBean;
import org.beedra_II.beed.ImmutableValueBeed;
import org.beedra_II.beed.ToOneReferenceBeed;

/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
public class Task implements BeedraBean {

  public final ImmutableValueBeed<Task, String> name =
      new ImmutableValueBeed<Task, String>(this);

  public final ToOneReferenceBeed<Project, Task> project =
      new ToOneReferenceBeed<Project, Task>(this, "tasks");

}

