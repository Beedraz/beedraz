package org.beedra_II.attribute.integer;


import org.beedra_II.BeedraBean;
import org.beedra_II.attribute.databeed.DataBeed;
import org.beedra_II.attribute.databeed.ImmutableValueDataBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class IntegerDataBeed<_BeedraBean_ extends BeedraBean>
    extends ImmutableValueDataBeed<_BeedraBean_, Integer>
    implements IntegerBeed<_BeedraBean_>, DataBeed<_BeedraBean_, Integer> {

  /**
   * @pre bean != null;
   */
  public IntegerDataBeed(_BeedraBean_ bean) {
    super(bean);
  }

}

