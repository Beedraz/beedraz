package org.beedraz.semantics_II.expression.enumeration;


import org.beedraz.semantics_II.expression.SimpleExpressionBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * A beed containing a {@code _Enum_} value.
 * Listeners of the beed can receive events of type
 * {@link EnumEvent}{@code <_Enum_>}.
 *
 * @author Nele Smeets
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface EnumBeed<_Enum_ extends Enum<_Enum_>>
    extends SimpleExpressionBeed<_Enum_, EnumEvent<_Enum_>> {

  // NOP

}

