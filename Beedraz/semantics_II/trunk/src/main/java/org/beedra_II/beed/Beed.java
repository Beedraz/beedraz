package org.beedra_II.beed;

import org.beedra_II.BeedraBean;


/**
 * @invar getBean() != null;
 * @invar getBean() == this'getBean();
 */
public interface Beed<_BeedraBean_ extends BeedraBean, _Value_> {

  /**
   * @basic
   */
  _BeedraBean_ getBean();

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

