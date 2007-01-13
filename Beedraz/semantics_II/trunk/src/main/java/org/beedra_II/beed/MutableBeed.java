package org.beedra_II.beed;


import org.beedra_II.BeedraBean;


public interface MutableBeed<_BeedraBean_ extends BeedraBean, _Value_>
    extends Beed<_BeedraBean_, _Value_> {

  /**
   * @post get().equals(value);
   *       When this is a reference type, equals is implemented
   *       as {@code ==}, so this then means {@code get() == value}.
   */
  void set(_Value_ value);

}

