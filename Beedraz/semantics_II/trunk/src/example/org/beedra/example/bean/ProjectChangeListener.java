package org.beedra.example.bean;




public interface ProjectChangeListener {

  /**
   * @pre event != null;
   */
  void propertyChange(ProjectChangeEvent event);

}

