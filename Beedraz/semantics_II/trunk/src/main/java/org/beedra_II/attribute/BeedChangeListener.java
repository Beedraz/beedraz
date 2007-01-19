package org.beedra_II.attribute;


import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface BeedChangeListener<_BeedChangeEvent_ extends BeedChangeEvent> {

  /**
   * @pre event != null;
   */
  void propertyChange(_BeedChangeEvent_ event);

}

