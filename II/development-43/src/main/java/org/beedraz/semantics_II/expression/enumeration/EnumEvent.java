package org.beedraz.semantics_II.expression.enumeration;


import org.beedraz.semantics_II.ActualOldNewEvent;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.OldNewEvent;


/**
 * {@link OldNewEvent} whose source is an {@link EnumBeed} and
 * that carries a simple old and new value of type {@code _Enum_}.
 *
 * @author  Jan Dockx
 * @author  Nele Smeets
 * @author  PeopleWare n.v.
 *
 * @invar getSource() instanceof EnumBeed<_Enum_>;
 */
public final class EnumEvent<_Enum_ extends Enum<_Enum_>>
    extends ActualOldNewEvent<_Enum_> {

  /**
   * @pre  source != null;
   * @pre  (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @pre  (oldValue != null) && (newValue != null)
   *          ? ! oldValue.equals(newValue)
   *          : true;
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   */
  public EnumEvent(EnumBeed<_Enum_> source,
                   _Enum_ oldValue,
                   _Enum_ newValue,
                   Edit<?> edit) {
    super(source, oldValue, newValue, edit);
  }

}
