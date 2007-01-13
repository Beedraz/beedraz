package org.beedra_II.beed;


public interface BeedChangeListener<_BeedChangeEvent_ extends BeedChangeEvent> {

  /**
   * @pre event != null;
   */
  void propertyChange(_BeedChangeEvent_ event);

}

