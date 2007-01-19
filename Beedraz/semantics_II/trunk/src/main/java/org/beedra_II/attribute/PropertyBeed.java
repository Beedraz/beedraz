package org.beedra_II.attribute;


import org.beedra_II.BeedraBean;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * @invar getBean() != null;
 * @invar getBean() == this'getBean();
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface PropertyBeed<_BeedraBean_ extends BeedraBean, _Value_> {

  /**
   * @basic
   */
  _BeedraBean_ getBean();

  /**
   * @basic
   */
  _Value_ get();

  /**
   * @basic
   */
  boolean isChangeListener(BeedChangeListener<BeedChangeEvent<_Value_>> listener);

  /**
   * @pre listener != null;
   * @post isChangeListener(listener);
   */
  void addChangeListener(BeedChangeListener<BeedChangeEvent<_Value_>> listener);

  /**
   * @post ! isChangeListener(listener);
   */
  void removeChangeListener(BeedChangeListener<BeedChangeEvent<_Value_>> listener);

}

