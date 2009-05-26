package org.beedraz.semantics_II.bean;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.expression.association.set.EditableBidirToOneBeed;
import org.beedraz.semantics_II.expression.number.integer.long64.EditableLongBeed;
import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class WellBeanBeed extends AbstractBeanBeed {

  /**
   * The run in which the well is contained.
   */
  public final EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed> run =
    new EditableBidirToOneBeed<RunBeanBeed, WellBeanBeed>(this);

  /**
   * The Cq value of the well.
   */
  public final EditableLongBeed cq = new EditableLongBeed(this);

}