package org.beedraz.semantics_II.bean;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.expression.association.set.BidirToManyBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class RunBeanBeed extends AbstractBeanBeed {
  /**
   * The wells contained in this run.
   */
  public final BidirToManyBeed<RunBeanBeed, WellBeanBeed> wells =
    new BidirToManyBeed<RunBeanBeed, WellBeanBeed>(this);

}