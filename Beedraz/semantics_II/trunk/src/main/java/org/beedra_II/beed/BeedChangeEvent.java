package org.beedra_II.beed;


import org.beedra_II.BeedraBean;


/**
 * @invar getSource() != null;
 * @note must be immutable
 * @note in Swing, the UndoableEdit is wrapped in the UndoableEditEvent, instead of inheritance; should we do this?
 * @todo consider making this type final, to enforce immutability
 */
public class BeedChangeEvent<_Value_> {

  /*
   * @pre source != null;
   * @post getSource() == sourcel
   * @post oldValue == null ? getOldValue() == null : getOldValue().equals(oldValue);
   * @post newValue == null ? getNewValue() == null : getNewValue().equals(newValue);
   * @post canUndo();
   */
  public BeedChangeEvent(Beed<? extends BeedraBean, _Value_> source, _Value_ oldValue, _Value_ newValue) {
    assert source != null;
    $source = source;
    $oldValue = oldValue;
    $newValue = newValue;
  }

  /**
   * The type of this method is not generic, to avoid
   * a generic loop. There does not seem to be a reason
   * for this type to be generic: listeners should know
   * what object they are listening to. This method can
   * be of importance when events are gathered into
   * compound events, from different sources. But in
   * that case, the receiving listener has to be able
   * to work with different sources of different types
   * in any case (i.e., of polymorph type {@link Beed}),
   * so knowing the exact type at compile time is not
   * relevant anyway.
   *
   * @basic
   */
  public Beed<? extends BeedraBean, _Value_> getSource() {
    return $source;
  }

  /**
   * @invar $source != null;
   */
  private Beed<? extends BeedraBean, _Value_> $source;

  /**
   * @basic
   */
  public final _Value_ getOldValue() {
    return $oldValue;
  }

  private _Value_ $oldValue;

  /**
   * @basic
   */
  public final _Value_ getNewValue() {
    return $newValue;
  }

  private _Value_ $newValue;

}

