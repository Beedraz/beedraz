package org.beedra_II.beed.databeed;


import org.beedra_II.BeedraBean;
import org.beedra_II.beed.AbstractBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractDataBeed<_BeedraBean_ extends BeedraBean, _Value_>
    extends AbstractBeed<_BeedraBean_, _Value_>
    implements DataBeed<_BeedraBean_, _Value_> {

  /**
   * @pre bean != null;
   */
  protected AbstractDataBeed(_BeedraBean_ bean) {
    super(bean);
  }

}
