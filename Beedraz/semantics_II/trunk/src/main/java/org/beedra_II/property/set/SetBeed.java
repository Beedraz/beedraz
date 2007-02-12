package org.beedra_II.property.set;


import java.util.Set;

import org.beedra_II.edit.Edit;
import org.beedra_II.property.simple.SimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * @author  Jan Dockx
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar   get() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface SetBeed<_Element_, _EventEdit_ extends Edit<?>>
    extends SimplePropertyBeed<Set<_Element_>, SetEvent<_Element_, _EventEdit_>> {

  // NOP

}

