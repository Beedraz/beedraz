package org.beedra_II.attribute.databeed;


import org.beedra_II.BeedraBean;
import org.beedra_II.attribute.PropertyBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * {@link PropertyBeed} that holds data. Non-data beeds represent
 * offer <em>derived</em> information.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface DataBeed<_BeedraBean_ extends BeedraBean, _Value_>
    extends PropertyBeed<_BeedraBean_, _Value_> {

  /**
   * @post get().equals(value);
   *       When this is a reference type, equals is implemented
   *       as {@code ==}, so this then means {@code get() == value}.
   */
  void set(_Value_ value);

}

