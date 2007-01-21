package org.beedra_II.property.integer;


import org.beedra_II.bean.BeanBeed;
import org.beedra_II.property.databeed.DataBeed;
import org.beedra_II.property.databeed.ImmutableValueDataBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class IntegerDataBeed<_BeedraBean_ extends BeanBeed>
    extends ImmutableValueDataBeed<_BeedraBean_, Integer>
    implements IntegerBeed<_BeedraBean_>, DataBeed<_BeedraBean_, Integer> {

  /**
   * @pre bean != null;
   */
  public IntegerDataBeed(_BeedraBean_ bean) {
    super(bean);
  }

}

