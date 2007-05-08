package org.beedraz.semantics_II.bean;

import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.expression.association.set.BidirToManyBeed;

public class RunBeanBeed extends AbstractBeanBeed {
  /**
   * The wells contained in this run.
   */
  public final BidirToManyBeed<RunBeanBeed, WellBeanBeed> wells =
    new BidirToManyBeed<RunBeanBeed, WellBeanBeed>(this);

}