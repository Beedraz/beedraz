package org.beedraz.semantics_II.expression.enumeration;


import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.EditableSimpleExpressionBeed;


/**
 * An editable beed containing a {@code _Enum_} value.
 * Listeners of the beed can receive events of type
 * {@link EnumEvent}{@code <_Enum_>}.
 *
 * @author  Nele Smeets
 * @author  Jan Dockx
 * @author  PeopleWare n.v.
 */
public final class EditableEnumBeed<_Enum_ extends Enum<_Enum_>>
    extends EditableSimpleExpressionBeed<_Enum_, EnumEvent<_Enum_>>
    implements EnumBeed<_Enum_> {

  /**
   * @pre   owner != null;
   * @post  getOwner() == owner;
   */
  public EditableEnumBeed(AggregateBeed owner) {
    super(owner);
  }

}
