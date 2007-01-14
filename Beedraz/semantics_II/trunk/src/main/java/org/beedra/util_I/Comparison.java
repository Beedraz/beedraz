package org.beedra.util_I;


import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class Comparison {

  /**
   * @post a == b ? true
   * @post a != b && a == null ? false
   * @post a != b && b == null ? false
   * @post a != b && a != null && b != null ? a.equals(b);
   */
  public static <_Value_> boolean equalsWithNull(_Value_ a, _Value_ b) {
    return (a == b) || ((a != null) && a.equals(b));
    /* this is also true when
     * # a == null and b == null: should evaluate to true, first condition does
     * # a != null and b == null: should evaluate to false, and equals does return false
     * # a == null and b != null: should evaluate to false, and a != null does return false
     * # a != null and b != null: we need to compare with equals, and we do
     */
  }

}

