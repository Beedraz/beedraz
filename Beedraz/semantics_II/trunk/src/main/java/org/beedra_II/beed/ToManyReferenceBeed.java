package org.beedra_II.beed;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.beedra_II.BeedraBean;



/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 *
 * @invar getProject() != null ? getProject.getTasks().contains(this);
 */
public class ToManyReferenceBeed<_One_ extends BeedraBean,
                                 _Many_ extends BeedraBean>
    extends AbstractBeed<_One_, _Many_> {

  public ToManyReferenceBeed(_One_ bean) {
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

  private Set<_Many_> $many = new HashSet<_Many_>();

}

