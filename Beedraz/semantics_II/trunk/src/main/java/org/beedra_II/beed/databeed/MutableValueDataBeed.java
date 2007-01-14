package org.beedra_II.beed.databeed;


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
public abstract class MutableValueDataBeed<_BeedraBean_ extends BeedraBean, _Value_ extends Cloneable>
    extends AbstractDataBeed<_BeedraBean_, _Value_> {

  public MutableValueDataBeed(_BeedraBean_ bean) {
    super(bean);
  }

  /**
   * @basic
   */
  public final _Value_ get() {
    return safeValueCopy($value);
  }

  /**
   * @post value != null ? get().equals(value) : get() == null;
   * @post ; all registred ap change listeners are warned
   */
  public final void set(_Value_ value) {
    if (! equalsWithNull(value, $value)) {
      _Value_ oldValue = $value;
      $value = safeValueCopy(value);
      UndoableBeedChangeEvent<_Value_> event =
          new UndoableBeedChangeEvent<_Value_>(this,
                                               safeValueCopy(oldValue),
                                               safeValueCopy($value));
      fireChangeEvent(event);
    }
  }

  private _Value_ $value;

  /**
   * Returns a safe copy of {@code original}.
   * If {@code _Value_} is an immutable type, you can return original.
   *
   * @result equalsWithNull(result, original);
   */
  protected abstract _Value_ safeValueCopy(_Value_ original);

}

