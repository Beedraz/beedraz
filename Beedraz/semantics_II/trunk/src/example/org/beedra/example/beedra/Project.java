package org.beedra.example.beedra;

import org.beedra_II.BeedraBean;
import org.beedra_II.beed.ImmutableValueDataBeed;
import org.beedra_II.beed.ToManyReferenceDataBeed;

/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
public class Project implements BeedraBean {

  public final ImmutableValueDataBeed<Project, String> name =
      new ImmutableValueDataBeed<Project, String>(this);

  public final ToManyReferenceDataBeed<Project, Task> tasks =
      new ToManyReferenceDataBeed<Project, Task>(this);

}

