package org.beedra.example.beedra;

import org.beedra_II.BeedraBean;
import org.beedra_II.beed.ImmutableValueBeed;
import org.beedra_II.beed.ToManyReferenceBeed;

/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
public class Project implements BeedraBean {

  public final ImmutableValueBeed<Project, String> name =
      new ImmutableValueBeed<Project, String>(this);

  public final ToManyReferenceBeed<Project, Task> tasks =
      new ToManyReferenceBeed<Project, Task>(this);

}

