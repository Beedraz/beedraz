package org.beedra.example.genericevents;




public interface PropertyChangeListener<_ChangeEvent_ extends PropertyChangeEvent> {

  /**
   * @pre event != null;
   */
  void propertyChange(_ChangeEvent_ event);

}

