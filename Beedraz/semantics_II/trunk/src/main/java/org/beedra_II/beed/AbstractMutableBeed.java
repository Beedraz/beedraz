package org.beedra_II.beed;


import org.beedra_II.BeedraBean;


public abstract class AbstractMutableBeed<_BeedraBean_ extends BeedraBean, _Value_>
    extends AbstractBeed<_BeedraBean_, _Value_>
    implements MutableBeed<_BeedraBean_, _Value_> {

  /**
   * @pre bean != null;
   */
  protected AbstractMutableBeed(_BeedraBean_ bean) {
    super(bean);
  }

}
