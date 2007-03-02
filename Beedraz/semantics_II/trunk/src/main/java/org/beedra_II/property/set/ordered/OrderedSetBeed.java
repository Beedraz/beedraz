package org.beedra_II.property.set.ordered;


import java.util.List;

import org.beedra_II.property.simple.SimplePropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * A {@link SimplePropertyBeed} whose value is of type {@link List<_Element_>}
 * and that sends events of type {@link OrderedSetEvent<_Element_>}
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar   get() != null;
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface OrderedSetBeed<_Element_>
    extends SimplePropertyBeed<List<_Element_>, OrderedSetEvent<_Element_>> {

  // NOP

}

