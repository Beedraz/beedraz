package org.beedra_II.property.integer;


import org.beedra_II.property.simple.OldNewEvent;
import org.beedra_II.property.simple.SimplePB;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface IntegerBeed
    extends SimplePB<Integer, OldNewEvent<?extends IntegerBeed, Integer>> {

  // NOP

}

