package org.beedra_II.beed;


import org.beedra_II.BeedraBean;
import static org.beedra.util_I.Comparison.equalsWithNull;


/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
public class ImmutableValueBeed<_BeedraBean_ extends BeedraBean, _Value_>
    extends AbstractMutableBeed<_BeedraBean_, _Value_> {

  public ImmutableValueBeed(_BeedraBean_ bean) {
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

