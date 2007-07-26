package org.beedraz.semantics_II.expression.enumeration;


import java.util.Map;

import org.beedraz.semantics_II.AbstractBeed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.expression.SimpleExpressionEdit;


/**
 * An {@link Edit} for a beed of type {@link EditableEnumBeed}.
 * This edit can change the value of the target beed, and send
 * {@link EnumEvent events} to the listeners of that beed.
 *
 * @author  Nele Smeets
 * @author  Jan Dockx
 * @author  PeopleWare n.v.
 */
public class EnumEdit<_Enum_ extends Enum<_Enum_>>
    extends SimpleExpressionEdit<_Enum_,
                                 EditableEnumBeed<_Enum_>,
                                 EnumEvent<_Enum_>> {

  /**
   * @pre  target != null;
   * @post getTarget() == target;
   */
  public EnumEdit(EditableEnumBeed<_Enum_> target) {
    super(target);
  }

  /**
   * @post  result.size() == 1;
   * @post  result.get(getTarget()) != null;
   * @post  result.get(getTarget()).getSource() == getTarget();
   * @post  result.get(getTarget()).getOldValue() == getOldValue();
   * @post  result.get(getTarget()).getNewValue() == getNewValue();
   * @post  result.get(getTarget()).getEdit() == this;
   */
  @Override
  protected Map<AbstractBeed<?>, EnumEvent<_Enum_>> createEvents() {
    return singletonEventMap(new EnumEvent<_Enum_>(getTarget(), getOldValue(), getNewValue(), this));
  }

}