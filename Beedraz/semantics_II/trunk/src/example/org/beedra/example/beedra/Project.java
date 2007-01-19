package org.beedra.example.beedra;

import org.beedra_II.BeedraBean;
import org.beedra_II.attribute.association.ToManyReferenceDataBeed;
import org.beedra_II.attribute.databeed.ImmutableValueDataBeed;

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

