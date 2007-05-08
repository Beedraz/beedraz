package org.beedra_II.property.date;


import java.util.Date;

import org.beedra_II.property.simple.SimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A beed containing a {@link Date} value.
 * Listeners of the beed can receive events of type
 * {@link DateEvent}.
 *
 * @author Nele Smeets
 * @author PeopleWare n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface DateBeed
    extends SimplePropertyBeed<Date, DateEvent> {

  // NOP

}

