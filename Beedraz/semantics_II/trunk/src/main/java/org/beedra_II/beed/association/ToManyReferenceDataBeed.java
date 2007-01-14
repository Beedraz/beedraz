package org.beedra_II.beed.association;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.beedra_II.BeedraBean;
import org.beedra_II.beed.AbstractBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 *
 * @invar getProject() != null ? getProject.getTasks().contains(this);
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class ToManyReferenceDataBeed<_One_ extends BeedraBean,
                                 _Many_ extends BeedraBean>
    extends AbstractBeed<_One_, Set<_Many_>> {

  public ToManyReferenceDataBeed(_One_ bean) {
    super(bean);
  }

  public final Set<_Many_> get() {
    return Collections.unmodifiableSet($many);
  }

  void add(_Many_ many) {
    assert many != null;
    $many.add(many);
  }

  void remove(_Many_ many) {
    $many.remove(many);
  }

  private final Set<_Many_> $many = new HashSet<_Many_>();

}

