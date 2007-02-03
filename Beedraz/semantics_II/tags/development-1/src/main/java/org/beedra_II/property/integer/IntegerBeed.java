package org.beedra_II.property.integer;


import org.beedra_II.property.simple.SimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * @note this type must still be generic
 * Short description of this type.
 *
 * @author jand FULL NAME
 * @author PeopleWare n.v.
 *
 * @invar getXXX() != null;
 *
 * TODO Write correct type comment.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface IntegerBeed<_Event_ extends IntegerEvent>
    extends SimplePropertyBeed<Integer, _Event_> {

  // NOP

}

