package org.beedra_II.attribute.databeed;


import org.beedra_II.BeedraBean;
import org.toryt.util_I.annotations.vcs.CvsInfo;

import static org.beedra.util_I.Comparison.equalsWithNull;


/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class ImmutableValueDataBeed<_BeedraBean_ extends BeedraBean, _Value_>
    extends AbstractDataBeed<_BeedraBean_, _Value_> {

  /**
   * @pre bean != null;
   */
  public ImmutableValueDataBeed(_BeedraBean_ bean) {
    super(bean);
  }

  /**
   * @basic
   */
  public final _Value_ get() {
    return $value;
  }

  /**
   * @post value != null ? get().equals(value) : get() == null;
   * @post ; all registred ap change listeners are warned
   */
  public final void set(_Value_ value) {
    if (! equalsWithNull(value, $value)) {
      _Value_ oldValue = $value;
      $value = value;
      UndoableBeedChangeEvent<_Value_> event =
          new UndoableBeedChangeEvent<_Value_>(this, oldValue, $value);
      fireChangeEvent(event);
    }
  }

  private _Value_ $value;

}

