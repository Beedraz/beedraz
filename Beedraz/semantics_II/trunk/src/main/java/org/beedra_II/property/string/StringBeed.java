package org.beedra_II.property.string;


import org.beedra_II.property.simple.SimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface StringBeed<_Event_ extends StringEvent>
    extends SimplePropertyBeed<String, _Event_> {

  // NOP

}

