package org.beedra_II.property.decimal;


import org.beedra_II.property.simple.SimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A beed containing a {@link Double} value.
 * Listeners of the beed can receive events of type
 * {@link DoubleEvent}.
 *
 * @author Nele Smeets
 * @author PeopleWare n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface DoubleBeed
    extends SimplePropertyBeed<Double, DoubleEvent> {

  // NOP

}

