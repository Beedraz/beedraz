package org.beedra_II.property.integer;


import org.beedra_II.Beed;
import org.beedra_II.property.simple.SimpleEditablePB;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class EditableIntegerBeed
    extends SimpleEditablePB<Integer>
    implements IntegerBeed {

  /**
   * @pre bean != null;
   */
  public EditableIntegerBeed(Beed<?> bean) {
    super(bean);
  }

}

