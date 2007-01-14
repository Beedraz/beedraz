package org.beedra.example.beedra;

import org.beedra_II.BeedraBean;
import org.beedra_II.beed.association.ToOneReferenceDataBeed;
import org.beedra_II.beed.databeed.ImmutableValueDataBeed;

/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
public class Task implements BeedraBean {

  public final ImmutableValueDataBeed<Task, String> name =
      new ImmutableValueDataBeed<Task, String>(this);

  public final ToOneReferenceDataBeed<Project, Task> project =
      new ToOneReferenceDataBeed<Project, Task>(this, "tasks");

}

