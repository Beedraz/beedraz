package org.beedra_II.property.set;


import java.util.Set;

import org.beedra_II.property.simple.SimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface SetBeed<_Element_>
    extends SimplePropertyBeed<Set<_Element_>, SetEvent<_Element_>> {

  // NOP

}

